package com.hansol.schedule.dto;

import com.hansol.schedule.domain.JobStateType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponse {
	private int labelId;
	private String name;
	private String username;
	private String jobName;
	private String jobGroup;
	private String prevFireDt;
	private String nextFireDt;
	private JobStateType jobState;
}
