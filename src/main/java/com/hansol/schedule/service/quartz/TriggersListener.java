package com.hansol.schedule.service.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.payload.ResponseType;
import com.hansol.schedule.domain.IndivLog;
import com.hansol.schedule.domain.JobLog;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.mapper.ScheduleMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class TriggersListener implements TriggerListener {
	private final ScheduleMapper scheduleMapper;
	private final HashMap<JobKey, Date> map ;
	
    @Override
    public String getName() {
        return "globalTrigger";
    }

    //Trigger 실행 시작
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        JobKey jobKey = trigger.getJobKey();
        if(map.containsKey(jobKey)) {
        	map.remove(jobKey);
        }
        Date now=new Date();
        map.put(jobKey, now);
        
        int label_id = scheduleMapper.findLabelId(jobKey.getName(),jobKey.getGroup());
        log.info("triggerFired at {} :: jobKey : {}", now, jobKey);
        IndivLog indivLog=IndivLog.builder()
        		.label_id(label_id)
                .message("[INFO] triggerFired at "+now+" :: jobKey : "+jobKey)
        		.build();
        scheduleMapper.addIndivLog(indivLog);
    }

    
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
    	// Job 실행 전, Trigger 중단 여부를 확인하는 메소드
    	//조건을 추가하여 중단 시킬 수 있다.
        // 리턴값이 false면 Job을 실행, true면 JobListener의 jobExecutionVetoed로 넘어간다.
        return false;
    }

    //Trigger 시간 만료, 정지 등으로 misfired 처리된 상태
    @Override
    public void triggerMisfired(Trigger trigger) {
    	JobKey jobKey = trigger.getJobKey();
    	int label_id = scheduleMapper.findLabelId(jobKey.getName(),jobKey.getGroup());
    	JobLog jobLog=JobLog.builder()
    			.start_dt(map.get(jobKey))
    			.label_id(label_id)
    			.exec_result("FAIL")
    			.build();
        scheduleMapper.addLog(jobLog);
        
        Date now=new Date();
        log.info("triggerMisfired at {} :: jobKey : {}", now, jobKey);
        IndivLog indivLog=IndivLog.builder()
        		.label_id(label_id)
                .message("[INFO] triggerMisfired at "+now+" :: jobKey : "+jobKey)
        		.build();
        scheduleMapper.addIndivLog(indivLog);
    }

    //Trigger 완료.
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
								Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        try{
            JobKey jobKey = trigger.getJobKey();
        	int label_id = scheduleMapper.findLabelId(jobKey.getName(),jobKey.getGroup());
        	JobLog jobLog=JobLog.builder()
        			.start_dt(map.get(jobKey))
        			.label_id(label_id)
        			.exec_result("SUCCESS")
        			.build();
            scheduleMapper.addLog(jobLog);
            
            Date now=new Date();
            log.info("triggerComplete at {} :: jobKey : {}", now, jobKey);
            IndivLog indivLog=IndivLog.builder()
            		.label_id(label_id)
                    .message("[INFO] triggerComplete at "+now+" :: jobKey : "+jobKey)
            		.build();
            scheduleMapper.addIndivLog(indivLog);
            if (context.getNextFireTime() == null) {
        		scheduleMapper.updateJobState(jobKey.getName(), jobKey.getGroup(), JobStateType.TERMINATED);
        	}
        }catch(NullPointerException e) {
        	throw new InvalidValueException(ResponseType.DETAULT_SERVER_ERROR);
        }
        
    }
}
