package com.hansol.schedule.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogListResponse {
	private List<LogResponse> logs;
	private Paging paging;
}
