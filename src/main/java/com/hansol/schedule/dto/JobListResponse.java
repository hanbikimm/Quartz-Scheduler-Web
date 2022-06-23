package com.hansol.schedule.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobListResponse {
	private NumResponse nums;
	private List<JobResponse> jobs;
	private Paging paging;
}
