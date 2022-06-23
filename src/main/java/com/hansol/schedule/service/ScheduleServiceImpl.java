package com.hansol.schedule.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.common.payload.ResponseType;
import com.hansol.common.utility.SecurityUtils;
import com.hansol.member.domain.Member;
import com.hansol.member.service.MemberService;
import com.hansol.schedule.Utils.DateUtils;
import com.hansol.schedule.Utils.JobUtils;
import com.hansol.schedule.Utils.PagingUtils;
import com.hansol.schedule.domain.JobLabel;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.domain.Users;
import com.hansol.schedule.dto.JobDetailsResponse;
import com.hansol.schedule.dto.JobListResponse;
import com.hansol.schedule.dto.JobRequest;
import com.hansol.schedule.dto.JobResponse;
import com.hansol.schedule.dto.JobStateRequest;
import com.hansol.schedule.dto.JobUpdateRequest;
import com.hansol.schedule.dto.LogListResponse;
import com.hansol.schedule.dto.LogResponse;
import com.hansol.schedule.dto.NumResponse;
import com.hansol.schedule.dto.Paging;
import com.hansol.schedule.mapper.ScheduleMapper;
import com.hansol.schedule.dto.IndivLogResponse;
import com.hansol.schedule.dto.JobDetailsFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	private final ScheduleMapper scheduleMapper;
	private final SchedulerFactoryBean schedulerFactoryBean;
	private final ApplicationContext context;
	private final MemberService memberService;

	public boolean existsJobLabel(JobKey key) {
		String jobName = key.getName();
		String jobGroup = key.getGroup();

		return scheduleMapper.existsByNames(jobName, jobGroup);
	}

	public boolean isJobExists(JobKey jobkey) {
		// FactoryBean에 저장된 job 중복 체크.
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			if (scheduler.checkExists(jobkey)) {
				return true;
			}
		} catch (SchedulerException e) {
			log.error("job 중복 체크 중 오류 발생 :: jobkey : {}", jobkey, e);
		}
		return false;
	};

	@Override
	@Transactional
	public boolean addJob(JobRequest jobRequest) {
		JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
		if (existsJobLabel(jobKey) || isJobExists(jobKey)) {
			throw new InvalidValueException(ResponseType.DUPLICATE_SCHEDULE);
		}

		JobDetail jobDetail;
		Trigger trigger;

		JobLabel jobLabel;

		try {
			// factoryBean에 job schedule을 등록하는 과정
			// jobClass에 따라 createTrigger, cronExpression 유효성 검사 등이 바뀌어야함!

			trigger = JobUtils.createTrigger(jobRequest, new Date());
			jobDetail = JobUtils.createJob(jobRequest, context);

			Date dt = schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
			log.debug("Job with jobKey : {} scheduled successfully at : {}", jobDetail.getKey(), dt);

			// DB Job_Label 등록
			int memberId = memberService.getMember(SecurityUtils.getUsername()).getMemberId();

			jobLabel = JobLabel.builder().member_id(memberId).job_group(jobRequest.getJobGroup())
					.job_name(jobRequest.getJobName()).job_class(jobRequest.getJobClass())
					.job_desc(jobRequest.getJobDesc()).job_state(JobStateType.USING).build();
			scheduleMapper.addJob(jobLabel);
			return true;
		} catch (SchedulerException e) {
			log.error("Error occurred while scheduling with jobKey : {}", jobKey, e);
			throw new InvalidValueException(ResponseType.SCHEDULE_CREATE_ERROR);
		}

	}

	@Override
	public JobListResponse getscheduleList(String nowPage) {
		List<JobResponse> jobs = new ArrayList<>();
		int numOfJobs = 0;
		int numOfGroups = 0;
		int numOfRunningJobs = 0;
		int numOfPausedJobs = 0;
		int total = 0;
		Paging paging = null;
		JobResponse jobResponse;
		Map<Integer, JobKey> sortingMap  = new TreeMap<Integer, JobKey>(Collections.reverseOrder());
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			// paging & sorting
			for (String groupName : scheduler.getJobGroupNames()) {
				numOfGroups++;
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					Integer labelId = scheduleMapper.findLabelId2(jobKey.getName(), jobKey.getGroup());
					if (labelId != null) {
						sortingMap.put(labelId, jobKey);
						total++;

						if (isJobRunning(jobKey)) {
							numOfRunningJobs++;
						} else {
							JobStateType jobState = getJobState(jobKey);
							if (jobState.equals(JobStateType.SCHEDULED)) {
								numOfRunningJobs++;
							} else if (jobState.equals(JobStateType.PAUSED)) {
								numOfPausedJobs++;
							}
						}
					}
				}
			}
			paging = PagingUtils.setPaging(total, Integer.parseInt(nowPage), 8);

			for (int labelId : sortingMap.keySet()) {
				JobKey jobKey = sortingMap.get(labelId);
				@SuppressWarnings("unchecked")
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);// job에서 실행되는 트리거 가져오기

				Users users = scheduleMapper.findUsername(jobKey.getName(), jobKey.getGroup());
				if (users == null) {
					continue;
				}

				numOfJobs++;

				if (numOfJobs < paging.getStart()) {
					continue;
				}
				if (numOfJobs > paging.getEnd()) {
					continue;
				}

				String prevFireDt = triggers.get(0).getPreviousFireTime() != null
						? DateUtils.toString(triggers.get(0).getPreviousFireTime())
						: "N/A";
				String nextFireDt = triggers.get(0).getNextFireTime() != null
						? DateUtils.toString(triggers.get(0).getNextFireTime())
						: "N/A";

				jobResponse = JobResponse.builder().labelId(labelId).username(users.getUsername()).name(users.getName())
						.jobName(jobKey.getName()).jobGroup(jobKey.getGroup()).prevFireDt(prevFireDt)
						.nextFireDt(nextFireDt).build();

				if (isJobRunning(jobKey)) {
					jobResponse.setJobState(JobStateType.RUNNING);
				} else {
					JobStateType jobState = getJobState(jobKey);
					jobResponse.setJobState(jobState);
				}

				jobs.add(jobResponse);
			}

		} catch (SchedulerException e) {
			log.error("Error occurred while getting jobList", e);
			throw new InvalidValueException(ResponseType.SCHEDULE_SELECT_ERROR);
		}
		if (total != numOfJobs) {
			throw new InvalidValueException(ResponseType.SCHEDULE_SELECT_ERROR);
		}
		NumResponse nums=NumResponse.builder()
				.numOfGroups(numOfGroups)
				.numOfJobs(numOfJobs)
				.numOfPausedJobs(numOfPausedJobs)
				.numOfRunningJobs(numOfRunningJobs)
				.build();
		return JobListResponse.builder().jobs(jobs).nums(nums).paging(paging).build();
	}

	@Override
	public boolean isJobRunning(JobKey jobKey) {
		try {// 현재 실행 중인 job 목록 중 지금 찾는 jobName이 존재하면 true 반환
			List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
			if (currentJobs != null) {
				for (JobExecutionContext ctx : currentJobs) {
					JobKey exctKey=ctx.getJobDetail().getKey();
					if (jobKey.getGroup().equals(exctKey.getGroup())&&
							jobKey.getName().equals(exctKey.getName())) {
						return true;
					}
				}
			}
		} catch (SchedulerException e) {
			log.error("Error occurred while checking job with jobKey : {}", jobKey, e);
		}
		return false;
	}

	@Override
	public JobStateType getJobState(JobKey jobKey) {
		try {// 존재하는 trigger 중, NORMAL이면 scheduled, PAUSE면 stopped 반환
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			
			if (isJobRunning(jobKey)) {
				return JobStateType.RUNNING;
			}

			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

			if (triggers != null && triggers.size() > 0) {
				for (Trigger trigger : triggers) {
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
						return JobStateType.SCHEDULED;
					} else if (Trigger.TriggerState.PAUSED.equals(triggerState)) {
						return JobStateType.PAUSED;
					}
					return JobStateType.UNKNOWN;
				}
			}
		} catch (SchedulerException e) {
			log.error("Error occurred while getting job state jobKey : {}", jobKey, e);
		}
		return null;
	}

	@Override
	public LogListResponse getLogList(String nowPage) {
		int total = scheduleMapper.countAllLog();
		Paging paging = PagingUtils.setPaging(total, Integer.parseInt(nowPage), 10);
		List<LogResponse> logs = scheduleMapper.getLogList(paging.getStart(), paging.getEnd());
		return LogListResponse.builder().logs(logs).paging(paging).build();
	}

	@Override
	public JobLabel getJobLabel(int labelId) {
		JobLabel jobLabel = scheduleMapper.findLabel(labelId);
		if (jobLabel == null) { // (-> refactor: Optional<JobLabel>.orElseThrow())
			throw new ValueNotFoundException(ResponseType.JOB_LABEL_NOT_FOUND);
		}

		return jobLabel;
	}

	@Override
	public JobDetailsResponse getSchedule(int labelId) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobLabel jobLabel = getJobLabel(labelId);
		String jobName = jobLabel.getJob_name();
		String jobGroup = jobLabel.getJob_group();
		Member member = memberService.getMember(jobLabel.getMember_id());
		
		try {
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroup));
			if (jobDetail == null) {
				log.info("[INFO] Job Detail 없음");
				throw new ValueNotFoundException(ResponseType.SCHEDULE_SELECT_ERROR);
			}
			
			JobStateType jobState = getJobState(JobKey.jobKey(jobName, jobGroup));
			Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(jobName, jobGroup));
			
			List<IndivLogResponse> indivLogResponse = scheduleMapper.getIndivLog(labelId);
			
			return JobDetailsFactory.of(jobLabel, jobState, trigger, member.getUsername(), member.getName(), indivLogResponse);
		} catch (ValueNotFoundException | SchedulerException e) {
			log.info("[INFO] 스케줄 상세 조회 실패 :: [MORE] Message: {}", e.getMessage());
			throw new InvalidValueException(ResponseType.SCHEDULE_SELECT_ERROR);
		}
	}

	@Override
	@Transactional
	public boolean updateSchedule(int labelId, JobUpdateRequest payload) {
		JobLabel jobLabel = getJobLabel(labelId);
		String jobName = jobLabel.getJob_name();
		String jobGroup = jobLabel.getJob_group();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		JobStateType jobLabelState = jobLabel.getJob_state();
		
		// 상태 확인 및 일시 정지 후에 수정한다.
		this.checkScheduleState(jobKey, jobLabelState);
		this.pauseSchedule(jobKey);
		
		try {
			// Quartz 테이블에서 기존 작업을 삭제한다.
			schedulerFactoryBean.getScheduler().deleteJob(jobKey);
			
			// Quartz 테이블에 새 작업을 등록한다.
			JobRequest request = JobUpdateRequest.of(jobName, jobGroup, payload);
			Trigger trigger = JobUtils.createTrigger(request, new Date());
			JobDetail jobDetail = JobUtils.createJob(request, context);

			Date dt = schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
			log.debug("[INFO] 스케줄 재등록 성공 :: [MORE] Job Key: {}, Time: {}", jobDetail.getKey(), dt);
			
			// DB에 저장된 작업 설명을 수정한다.
			jobLabel.setJob_class(payload.getJobClass());
			jobLabel.setJob_desc(payload.getJobDesc());
			scheduleMapper.updateJob(jobLabel);

			return true;
		} catch (SchedulerException e) {
			throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
		}
	}
	
	private void checkScheduleState(JobKey jobKey, JobStateType jobState) {
		if (!this.isJobExists(jobKey)) {
			log.info("[INFO] 스케줄 수정/삭제 실패 :: [MORE] 해당 작업을 스케줄에서 찾을 수 없음");
			throw new ValueNotFoundException(ResponseType.SCHEDULE_SELECT_ERROR);
		}
		if (this.isJobRunning(jobKey) || jobState == JobStateType.DELETED) {
			log.info("[INFO] 스케줄 수정/삭제 실패 :: [MORE] 실행 중인 작업 또는 이미 삭제된 작업");
			throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
		}
	}

	@Override
	@Transactional
	public boolean updateScheduleState(int labelId, JobStateRequest payload) {
		JobLabel jobLabel = getJobLabel(labelId);
		String jobName = jobLabel.getJob_name();
		String jobGroup = jobLabel.getJob_group();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		JobStateType jobLabelState = jobLabel.getJob_state();
		
		// 상태 확인 및 일시 정지 후에 수정한다.
		this.checkScheduleState(jobKey, jobLabelState);
		this.pauseSchedule(jobKey);
		
		switch (payload.getJobState()) {
			case PAUSED:
				log.info("[INFO] 스케줄 정지, {}", jobKey);
				return this.pauseSchedule(jobKey);
			case RESUMED:
				log.info("[INFO] 스케줄 재시작: {}", jobKey);
				return this.resumeSchedule(jobKey);
//			case STOPPED:
//				log.info("[INFO] 스케줄 중지: {}", jobKey);
//				return this.stopSchedule(jobKey);
			default:
				log.info("[INFO] 스케줄 상태 수정 실패 :: [MORE] PAUSED, RESUMED 상태 수정만 허용됨");
				throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
		}
	}

	private boolean pauseSchedule(JobKey jobKey) {
		try {
			schedulerFactoryBean.getScheduler().pauseJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.info("[INFO] 스케줄 정지 실패 :: [MORE] Message: {}", e.getMessage());
			throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
		}
	}

	private boolean resumeSchedule(JobKey jobKey) {
		try {
			schedulerFactoryBean.getScheduler().resumeJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.info("[INFO] 스케줄 재시작 실패 :: [MORE] Message: {}", e.getMessage());
			throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
		}
	}
	
//	private boolean stopSchedule(JobKey jobKey) {
//		try {
//			schedulerFactoryBean.getScheduler().interrupt(jobKey);
//			scheduleMapper.updateJobState(jobKey.getName(), jobKey.getGroup(), JobStateType.UNUSING);
//			return true;
//		} catch (SchedulerException e) {
//			log.info("[INFO] 스케줄 중지 실패 :: [MORE] Message: {}", e.getMessage());
//			throw new InvalidValueException(ResponseType.SCHEDULE_UPDATE_ERROR);
//		}
//	}

	@Transactional
	public boolean deleteSchedule(int labelId) {
		JobLabel jobLabel = getJobLabel(labelId);
		String jobName = jobLabel.getJob_name();
		String jobGroup = jobLabel.getJob_group();
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		JobStateType jobLabelState = jobLabel.getJob_state();
		
		// 상태 확인 및 일시 정지 후에 삭제한다.
		this.checkScheduleState(jobKey, jobLabelState);
		this.pauseSchedule(jobKey);
		
		try {
			schedulerFactoryBean.getScheduler().deleteJob(jobKey);
			scheduleMapper.deleteJob(labelId);
			return true;
		}catch (SchedulerException e) {
			throw new InvalidValueException(ResponseType.SCHEDULE_DELETE_ERROR);
		}
	}
}
