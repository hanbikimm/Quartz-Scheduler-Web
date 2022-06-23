package com.hansol.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogResponse {
	private String logId;
	private String jobName;
	private String jobGroup;
	private String jobState; //실행성공/실패, 등록, 삭제, 종료
	private String startDt;
	private String endDt;
}
