package com.hansol.schedule.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndivLog {
	private int label_id;
	private String message;
	
}
