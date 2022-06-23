package com.hansol.common.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.hansol.common.payload.ResponseType;
import com.hansol.common.utility.PayloadUtils;
import com.hansol.common.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtAuthenticationProvider tokenProvider;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String url = request.getRequestURI();
	    String contentType = request.getContentType();
		log.info("[INFO] 요청된 URL: {}, {}", url, contentType);
		
		String token = SecurityUtils.getToken(request);
		
		if (PayloadUtils.hasToken(token)) {
			if (tokenProvider.validateToken(token)) {
				SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));
			} else {
				log.info("[INFO] 유효하지 않은 토큰 (계정 재인증 필요)");
				request.setAttribute("exception", ResponseType.INVALID_TOKEN.getCode());
				// response.sendRedirect("/error/invalid-token");
				// return;
			}
		} else {
			log.info("[INFO] 전달된 토큰 없음 (계정 인증 필요)");
			request.setAttribute("exception", ResponseType.UNAUTHENTICATED.getCode());
			// response.sendRedirect("/error/unauthenticated");
			// return;
		}
		
		filterChain.doFilter(request, response);
	}
}