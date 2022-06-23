package com.hansol.schedule.jobClass;

import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hansol.schedule.domain.IndivLog;
import com.hansol.schedule.mapper.ScheduleMapper;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class CountJob extends QuartzJobBean implements InterruptableJob {
    private volatile boolean isJobInterrupted = false;
    private int MAX_SLEEP_IN_SECONDS = 5;
    private volatile Thread currThread;

	@Autowired
	private ScheduleMapper scheduleMapper;
	private String message;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        int label_id = scheduleMapper.findLabelId(jobKey.getName(),jobKey.getGroup());
        
        if (!isJobInterrupted) {
            currThread = Thread.currentThread();
            log.info("============================================================================");
            log.info("CountJob started :: jobKey : {} - {}", jobKey, currThread.getName());
            message="[INFO] CountJob executed :: Thread - "+currThread.getName();
            
            IntStream.range(0, 2).forEach(i -> {
                log.info("CountJob Counting - {}", i);
                try {
                    TimeUnit.SECONDS.sleep(MAX_SLEEP_IN_SECONDS);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    message=message+"\n[ERROR] An InterruptedException error occured";
                    IndivLog indivLog=IndivLog.builder()
                    		.label_id(label_id)
                    		.message(message)
                    		.build();
                    scheduleMapper.addIndivLog(indivLog);
                }
            });

            log.info("CountJob ended :: jobKey : {} - {}", jobKey, currThread.getName());
            IndivLog indivLog=IndivLog.builder()
            		.label_id(label_id)
            		.message(message)
            		.build();
           scheduleMapper.addIndivLog(indivLog);
            log.info("============================================================================");
        }
    }

    @Override
    public void interrupt() {
        isJobInterrupted = true;
        if (currThread != null) {
            log.info("interrupting - {}", currThread.getName());
            currThread.interrupt();
        }
    }
}