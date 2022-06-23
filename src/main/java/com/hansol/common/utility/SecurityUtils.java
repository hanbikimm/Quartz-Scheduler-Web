package com.hansol.common.utility;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {
	public static String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null) return null;
        if (header.startsWith("Bearer ")) return header.substring("Bearer ".length());
        else return null;
    }
	
	public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication == null ? null : authentication.getName();
    }
	
	public static Collection<? extends GrantedAuthority> getAuthorities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return authentication == null ? null : authentication.getAuthorities();
	}
}