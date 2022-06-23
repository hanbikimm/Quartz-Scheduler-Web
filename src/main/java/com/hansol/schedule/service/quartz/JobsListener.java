package com.hansol.schedule.service.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class JobsListener implements JobListener {
	
	@Override
	public String getName() {
		return "globalJob";
	}

	//Job 실행전
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobToBeExecuted :: jobKey : {}", jobKey);
	}

	//Job 중단. Trigger 중복 실행 방지 등으로 Job 실행이 중지되었을 때.
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobExecutionVetoed :: jobKey : {}", jobKey);
	}

	//Job 실행 완료 triggerListener의 triggerComplete로 이동한다.
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobWasExecuted :: jobKey : {}", jobKey);
	}
}
