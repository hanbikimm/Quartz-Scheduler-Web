package com.hansol.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndivLogResponse {
	private String loggingDt;
	private String message;
	
}
