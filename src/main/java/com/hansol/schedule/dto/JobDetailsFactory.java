package com.hansol.schedule.dto;

import com.hansol.schedule.dto.JobDetailsFactory;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.payload.ResponseType;
import com.hansol.common.utility.PayloadUtils;
import com.hansol.schedule.Utils.DateUtils;
import com.hansol.schedule.domain.JobLabel;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.domain.TriggerType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobDetailsFactory {
	public static JobDetailsResponse of(JobLabel jobLabel, JobStateType jobState, Trigger trigger, String username, String name, List<IndivLogResponse> indivLogResponse) {
		if (trigger instanceof SimpleTriggerImpl) return ofSimple(jobLabel, jobState, trigger, username, name, indivLogResponse);
		if (trigger instanceof CronTriggerImpl) return ofCron(jobLabel, jobState, trigger, username, name, indivLogResponse);
		
		throw new InvalidValueException(ResponseType.UNSUPPORTED_TRIGGER);
    }
	
	/**
	 * ofDefault() + repeatCount, repeatInterval (QRTZ_SIMPLE_TRIGGERS)
	 */
	private static JobDetailsResponse ofSimple(JobLabel jobLabel, JobStateType jobState, Trigger trigger, String username, String name, List<IndivLogResponse> indivLogResponse) {
		log.info("[INFO] 작업 상세 조회 (Simple Type)");
		SimpleTrigger simple = (SimpleTrigger) trigger;
		
		JobDetailsResponse result = ofDefault(jobLabel, jobState, simple, username, name, indivLogResponse);
		result.setTrigger(TriggerType.SIMPLE);
		result.setRepeatCount(String.valueOf(simple.getRepeatCount() + 1));
		result.setRepeatInterval(String.valueOf(simple.getRepeatInterval() / 1000));
		
		return result;
	}
	
	/**
	 * ofDefault() + cronExpression (QRTZ_CRON_TRIGGERS)
	 */
	private static JobDetailsResponse ofCron(JobLabel jobLabel, JobStateType jobState, Trigger trigger, String username, String name, List<IndivLogResponse> indivLogResponse) {
		log.info("[INFO] 작업 상세 조회 (Cron Type)");
		CronTrigger cron = (CronTrigger) trigger;
		
		JobDetailsResponse result = ofDefault(jobLabel, jobState, cron, username, name, indivLogResponse);
		result.setTrigger(TriggerType.CRON);
		result.setCronExpression(cron.getCronExpression());
		
		return result;
	}
	
	private static JobDetailsResponse ofDefault(JobLabel jobLabel, JobStateType jobState, Trigger trigger, String username, String name, List<IndivLogResponse> indivLogResponse) {
		String startTime = PayloadUtils.toString(trigger.getStartTime());
		String endTime = PayloadUtils.toString(trigger.getEndTime());
		
		String prevFireTime = PayloadUtils.toString(trigger.getPreviousFireTime());
		String nextFireTime =  PayloadUtils.toString(trigger.getNextFireTime());
		
		return JobDetailsResponse.builder()
				.username(username).name(name)
				.jobName(jobLabel.getJob_name())
				.jobGroup(jobLabel.getJob_group())
				.jobClass(jobLabel.getJob_class())
				.jobDesc(jobLabel.getJob_desc())
				.jobState(jobState)
				.scheduledDt(DateUtils.toString(jobLabel.getCreated_dt()))
				.startDt(startTime).endDt(endTime)
				.prevFireDt(prevFireTime).nextFireDt(nextFireTime)
				.indivLogResponse(indivLogResponse)
				.build();
	}
}