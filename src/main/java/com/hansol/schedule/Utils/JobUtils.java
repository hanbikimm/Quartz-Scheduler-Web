package com.hansol.schedule.Utils;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import java.util.Date;
import java.util.TimeZone;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.common.payload.ResponseType;
import com.hansol.schedule.dto.JobRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobUtils {
	
	@SuppressWarnings("unchecked")
    public static Class<? extends Job> getJobClass(String className) {
        String path = "com.hansol.schedule.jobClass";
        try {
            return (Class<? extends Job>) Class.forName(String.format("%s.%s", path, className));
        } catch (ClassNotFoundException e) {
        	log.info("[INFO] Job 클래스 없음: {}", e.getMessage());
            throw new ValueNotFoundException(ResponseType.JOB_CLASS_NOT_FOUND);
        }
    }
	
	public static JobDetail createJob(JobRequest payload, ApplicationContext context) {
		Class<? extends Job> clazz = getJobClass(payload.getJobClass());
		
		return JobBuilder.newJob(clazz)
				.withIdentity(payload.getJobName(), payload.getJobGroup())
				.withDescription(payload.getJobDesc())
				.usingJobData(createJobDataMap(payload))
				.storeDurably(false)
				.build();
	}
	
    private static JobDataMap createJobDataMap(JobRequest payload) {
        String key = payload.getJobDataKey();
        String value = payload.getJobDataValue();
        JobDataMap dataMap = new JobDataMap();
        
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
        	return dataMap;
        }
        
        dataMap.put(key, value);
        return dataMap;
    }
    
    public static Trigger createTrigger(JobRequest payload, Date current) {
    	Date startDt = DateUtils.convert(payload.getStartDt());
    	Date endDt = DateUtils.convert(payload.getEndDt());
    	
		if (!isValidDateTimes(current, startDt, endDt)) {
			throw new InvalidValueException(ResponseType.INVALID_TRIGGER_DATE);
		}
		
        switch (payload.getTrigger()) {
	        case SIMPLE:
	        	return createSimpleTrigger(payload, startDt, endDt);
	        case CRON:
	        	return createCronTrigger(payload, startDt, endDt);
	        default:
	        	throw new InvalidValueException(ResponseType.UNSUPPORTED_TRIGGER);
        }
    }
    
    private static Trigger createSimpleTrigger(JobRequest payload, Date startDt, Date endDt) {
    	Integer repeatCount = Integer.valueOf(payload.getRepeatCount());
    	Integer repeatInterval = Integer.valueOf(payload.getRepeatInterval());
    	
    	if (!isValidRepeats(repeatCount, repeatInterval)) {
    		log.info("[INFO] 잘못된 Simple 작업 요청 :: [MORE] 잘못된 Repeat Count 또는 Repeat Interval 값 전달");
    		throw new InvalidValueException(ResponseType.INVALID_SIMPLE_TRIGGER);
    	}
    	
		ScheduleBuilder<SimpleTrigger> schedule = SimpleScheduleBuilder
				.simpleSchedule()
				.withRepeatCount(repeatCount - 1)
				.withIntervalInSeconds(repeatInterval)
				.withMisfireHandlingInstructionNextWithExistingCount();
		
		return TriggerBuilder.newTrigger()
				.withSchedule(schedule)
				.withIdentity(payload.getJobName(), payload.getJobGroup()) // QRTZ_TRIGGERS.TRIGGER_NAME, QRTZ_TRIGGERS.TRIGGER_GROUP
				.forJob(payload.getJobName(), payload.getJobGroup()) // QRTZ_TRIGGERS.JOB_NAME, QRTZ_TRIGGERS.JOB_GROUP
				.startAt(startDt)
				.endAt(endDt)
				.build();
    }

    private static Trigger createCronTrigger(JobRequest payload, Date startDt, Date endDt) {
    	String cronExpression = payload.getCronExpression();
    	
    	try {
    		CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
    		parser.parse(cronExpression).validate();
    	} catch (IllegalArgumentException e) {
    		log.info("[INFO] 잘못된 Cron 작업 요청 :: [MORE] 잘못된 Cron 표현식 전달");
    		throw new InvalidValueException(ResponseType.INVALID_CRON_TRIGGER);
    	}
    	
		ScheduleBuilder<CronTrigger> schedule = CronScheduleBuilder
				.cronSchedule(cronExpression)
				.inTimeZone(TimeZone.getDefault())
				.withMisfireHandlingInstructionDoNothing();
		
		return TriggerBuilder.newTrigger()
				.withSchedule(schedule)
				.withIdentity(payload.getJobName(), payload.getJobGroup()) // QRTZ_TRIGGERS.TRIGGER_NAME, QRTZ_TRIGGERS.TRIGGER_GROUP
				.forJob(payload.getJobName(), payload.getJobGroup()) // QRTZ_TRIGGERS.JOB_NAME, QRTZ_TRIGGERS.JOB_GROUP
				.startAt(startDt)
				.endAt(endDt)
				.build();
    }
    
	public static boolean isValidRepeats(Integer repeatCount, Integer repeatInterval) {
		return (repeatCount != null && repeatInterval != null);
	}
	
	public static boolean isValidDateTimes(Date current, Date startDt, Date endDt) {
		if (startDt == null || endDt == null) {
			return false;
		}
		if (startDt.equals(endDt)) {
			// st.after(et) -> false, et.before(st) -> false
			return false;
		}
		log.info("[INFO] Current Date/Time: {}", current);
		log.info("[INFO] Start Date/Time: {}", startDt);
		log.info("[INFO] End Date/Time: {}", endDt);
		
		return (startDt.after(current) && endDt.after(current));
	}
}