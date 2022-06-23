package com.hansol.schedule.dto;

import java.util.List;
import com.hansol.common.utility.PayloadUtils;
import com.hansol.schedule.domain.JobStateType;
import com.hansol.schedule.domain.TriggerType;
import com.hansol.schedule.dto.JobDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "schedule.dto.JobDetailsResponse")
public class JobDetailsResponse {
	private String username; // 생성자 (사번)
	private String name; // 생성자 (이름)
	private String jobName; // 작업 이름
	private String jobGroup; // 작업 그룹 이름
	private String jobClass; // 작업 클래스 이름
	private String jobDesc; // 작업 설명
	private JobStateType jobState; // 작업 상태
	private TriggerType trigger; // 트리거 타입
	private String cronExpression; // Cron 표현식 (Cron Job)
	private String repeatCount; // 실행 횟수 (Simple Job)
	private String repeatInterval; // 실행 간격 (Simple Job)
	private String scheduledDt; // 스케줄 등록 날짜
	private String startDt; // 스케줄 시작 날짜 (JOB 등록/수정 시 입력한 시작 날짜)
	private String endDt; // 스케줄 종료 날짜 (JOB 등록/수정 시 입력한 종료 날짜)
	private String prevFireDt; // 이전 실행 시간
	private String nextFireDt; // 다음 실행 시간
	private List<IndivLogResponse> indivLogResponse; // 상세 로그
	
	@Builder
	protected JobDetailsResponse(
			String username, String name, String jobName, String jobGroup,
			String jobClass, String jobDesc, JobStateType jobState, TriggerType trigger,
			String cronExpression, Integer repeatCount, Long repeatInterval,
			String scheduledDt, String startDt, String endDt, String prevFireDt, String nextFireDt,
			List<IndivLogResponse> indivLogResponse) {
		this.username = username;
		this.name = name;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobClass = jobClass;
		this.jobDesc = jobDesc;
		this.jobState = jobState;
		this.trigger = trigger;
		this.cronExpression = PayloadUtils.isEmpty(cronExpression);
		this.repeatCount = PayloadUtils.isNull(repeatCount);
		this.repeatInterval = PayloadUtils.isNull(repeatInterval);
		this.scheduledDt = scheduledDt;
		this.startDt = startDt;
		this.endDt = endDt;
		this.prevFireDt = prevFireDt;
		this.nextFireDt = nextFireDt;
		this.indivLogResponse = indivLogResponse;
	}
}