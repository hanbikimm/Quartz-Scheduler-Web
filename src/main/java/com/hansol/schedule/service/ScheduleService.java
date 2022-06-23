package com.hansol.schedule.service;

import org.quartz.JobKey;
import org.springframework.stereotype.Service;

import com.hansol.schedule.dto.JobListResponse;
import com.hansol.schedule.dto.JobRequest;
import com.hansol.schedule.dto.JobStateRequest;
import com.hansol.schedule.dto.JobUpdateRequest;
import com.hansol.schedule.dto.LogListResponse;
import com.hansol.schedule.domain.JobLabel;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.dto.JobDetailsResponse;

@Service
public interface ScheduleService {

	boolean addJob(JobRequest jobrequest);
	
	JobListResponse getscheduleList(String nowPage);
	
	boolean isJobRunning(JobKey jobKey);
	
	JobStateType getJobState(JobKey jobKey);
	
	LogListResponse getLogList(String nowPage);
	
	JobLabel getJobLabel(int labelId);
	JobDetailsResponse getSchedule(int labelId);
	boolean updateSchedule(int labelId, JobUpdateRequest payload);
	boolean updateScheduleState(int labelId, JobStateRequest payload);
	
	boolean deleteSchedule(int labelId);
}
