package com.hansol.common.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.hansol.common.payload.ResponseType;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("[ERROR] 인증이 필요한 요청, {}", e.getMessage());
		// response.sendRedirect("/error/unauthenticated");
		
		String code = String.valueOf(request.getAttribute("exception"));
		
		if (StringUtils.isEmpty(code)) {
			setResponse(response, ResponseType.UNAUTHENTICATED);
		}
		
		ResponseType type = ResponseType.of(code);
		setResponse(response, type);
	}
	
	private void setResponse(HttpServletResponse response, ResponseType type) throws IOException {
		log.info("[ERROR] 식별 코드: {}", type.getCode());
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(type.getStatus().value());
		
		JSONObject result = new JSONObject();
		result.put("success", false);
		result.put("code", type.getCode());
		result.put("message", type.getMessage());
		response.getWriter().print(result);
	}
}