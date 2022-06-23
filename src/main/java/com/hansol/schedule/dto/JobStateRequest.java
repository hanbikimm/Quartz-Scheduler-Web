package com.hansol.schedule.dto;

import com.hansol.common.annotation.EnumPattern;
import com.hansol.schedule.domain.JobStateType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "schedule.dto.JobStateRequest (작업 상태 수정)")
public class JobStateRequest {
	
	@Schema(example = "PAUSED")
	@EnumPattern(enums = {"PAUSED", "RESUMED"}, message = "PAUSED, RESUMED 값만 입력할 수 있습니다.")
	private JobStateType jobState;
}