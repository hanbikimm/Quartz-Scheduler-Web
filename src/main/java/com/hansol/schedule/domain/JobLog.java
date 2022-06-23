package com.hansol.schedule.domain;


import java.util.Date;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobLog {
	private int log_id;
	private int label_id;
	private String exec_result;
	private Date start_dt;
	private Date end_dt;
}
