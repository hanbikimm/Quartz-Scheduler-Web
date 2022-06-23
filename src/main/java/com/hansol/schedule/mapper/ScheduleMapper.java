package com.hansol.schedule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hansol.schedule.domain.IndivLog;
import com.hansol.schedule.domain.JobLabel;
import com.hansol.schedule.domain.JobLog;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.domain.Users;
import com.hansol.schedule.dto.IndivLogResponse;
import com.hansol.schedule.dto.LogResponse;

@Mapper
public interface ScheduleMapper {

int addJob(JobLabel jobLabel);

int findLabelId(@Param("job_name")String job_name, @Param("job_group")String job_group);
int findLabelId2(@Param("job_name")String job_name, @Param("job_group")String job_group);

int addLog(JobLog jobLog);

Users findUsername(@Param("job_name")String job_name, @Param("job_group")String job_group);

JobLabel findLabel(@Param("label_id") int label_id);

boolean existsByNames(@Param("job_name") String job_name, @Param("job_group") String job_group);

List<LogResponse> getLogList(@Param("start") int start, @Param("end") int end);

int updateJob(JobLabel jobLabel);

int updateJobState(@Param("job_name") String job_name, @Param("job_group") String job_group, @Param("job_state") JobStateType job_state);

int deleteJob(@Param("label_id") int labe_id);

int countAllLog();

int addIndivLog(IndivLog indivLog);

List<IndivLogResponse> getIndivLog(int label_Id);
}
