package com.hansol.common.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.hansol.common.payload.ResponseType;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException e) throws IOException, ServletException {
		log.info("[ERROR] 접근 권한이 필요한 요청, {}", e.getMessage());
		// response.sendRedirect("/error/unauthorized");
		
		setResponse(response, ResponseType.UNAUTHORIZED);
	}
	
	private void setResponse(HttpServletResponse response, ResponseType type) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(type.getStatus().value());
		
		JSONObject result = new JSONObject();
		result.put("success", false);
		result.put("code", type.getCode());
		result.put("message", type.getMessage());
		response.getWriter().print(result);
	}
}