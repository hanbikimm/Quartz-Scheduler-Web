package com.hansol.schedule.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hansol.common.annotation.EnumPattern;
import com.hansol.schedule.domain.TriggerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "schedule.payload.JobUpdateRequest (작업 수정)")
public class JobUpdateRequest {
	
	@EnumPattern(enums = {"SIMPLE", "CRON"}, message = "SIMPLE, CRON 값만 입력할 수 있습니다.")
	private TriggerType trigger;
	
	@Size(max = 25, message = "클래스 이름은 25자 이내로 입력하세요.")
	@Schema(example = "CronJob")
	@NotBlank(message = "실행할 JobClass를 입력해주세요.")
	private String jobClass;
	
	@Size(max = 50, message = "설명은 50자 이내로 입력하세요.")
	@Schema(example = "설명")
	private String jobDesc;
	
	// @Size(max = 50, message = "추가할 잡 데이터 키는 50자 이내로 입력하세요.")
	@Schema(example = "sampleKey")
	private String jobDataKey;
	
	// @Size(max = 50, message = "추가할 잡 데이터 값은 50자 이내로 입력하세요.")
	@Schema(example = "sampleValue")
	private String jobDataValue;
	
	@Size(max = 8, message = "반복 횟수는 8자 이내로 입력하세요.")
	@Schema(example = "3")
	private String repeatCount;
	
	@Size(max = 8, message = "반복 간격은 8자 이내로 입력하세요.")
	@Schema(example = "60")
	private String repeatInterval;
	
	@Size(max = 100, message = "크론 표현식은 100자 이내로 입력하세요.")
	@Schema(example = "0 0/5 * * * ?")
	private String cronExpression;
	
	@NotNull(message = "시작 시간을 입력하세요.")
	@Schema(type = "string", example = "2022-05-02 14:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime startDt;
	
	@NotNull(message = "종료 시간을 입력하세요.")
	@Schema(type = "string", example = "2022-05-02 14:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime endDt;
	
	public static JobRequest of(String jobName, String jobGroup, JobUpdateRequest payload) {
		return JobRequest.builder()
				.trigger(payload.getTrigger())
				.jobName(jobName)
				.jobGroup(jobGroup)
				.jobClass(payload.getJobClass())
				.jobDesc(payload.getJobDesc())
				.jobDataKey(payload.getJobDataKey())
				.jobDataValue(payload.getJobDataValue())
				.repeatCount(payload.getRepeatCount())
				.repeatInterval(payload.getRepeatInterval())
				.cronExpression(payload.getCronExpression())
				.startDt(payload.getStartDt())
				.endDt(payload.getEndDt())
				.build();
	}
}