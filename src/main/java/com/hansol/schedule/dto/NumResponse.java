package com.hansol.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumResponse {
	private int numOfJobs;
	private int numOfGroups;
	private int numOfRunningJobs;
	private int numOfPausedJobs;

}
