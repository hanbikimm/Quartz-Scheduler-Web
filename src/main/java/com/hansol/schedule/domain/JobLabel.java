package com.hansol.schedule.domain;

import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class JobLabel {
	private int label_id;
	private int member_id;
	private String job_name;
	private String job_group;
	private String job_class;
	private String job_desc;
	private JobStateType job_state;
	private Date created_dt;
	private Date updated_dt;
}
