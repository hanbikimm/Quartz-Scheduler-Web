package com.hansol.common.security;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.hansol.common.exception.AuthenticationException;
import com.hansol.common.payload.ResponseType;
import com.hansol.member.mapper.MemberMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationProvider implements InitializingBean {
	private Key key;
	private final String secretKey;
	private final long validityTime;
	private final MemberMapper memberMapper;
	
	public static final String TOKEN_SUBJECT = "token";
    public static final String TOKEN_ISSUER = "scheduler-api";
    public static final String CLAIM_MEMBER_KEY = "member";
    public static final String CLAIM_AUTHORITIES_KEY = "authorities";
	
	public JwtAuthenticationProvider(
			@Value("${server.jwt.secret-key}") String secretKey,
			@Value("${server.jwt.validity-time}") long validityTime,
			MemberMapper memberMapper) {
		this.secretKey = secretKey;
		this.validityTime = validityTime;
		this.memberMapper = memberMapper;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	public String createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(""));
		
		Date createdDt = new Date();
        Date expiredDt = new Date(createdDt.getTime() + validityTime);
		
		return Jwts.builder()
				.setSubject(TOKEN_SUBJECT)
				.claim(CLAIM_MEMBER_KEY, authentication.getName())
				.claim(CLAIM_AUTHORITIES_KEY, authorities)
				.setIssuer(TOKEN_ISSUER)
				.setIssuedAt(createdDt)
				.setExpiration(expiredDt)
				.signWith(key, SignatureAlgorithm.HS512).compact();
	}
	
	public boolean validateToken(String token) {
//		// 잘못된 형식 및 유효 기간이 만료된 토큰을 확인한다.
//		Claims claims = this.getClaims(token);
//		if (claims == null) return false;
//		
//		// 토큰에 세팅된 username 값을 확인한다.
//		String username = String.valueOf(claims.get(CLAIM_MEMBER_KEY));
//		if (StringUtils.isEmpty(username)) return false;
//		
//		// 전달된 토큰과 DB에 저장된 토큰의 일치 여부를 확인한다.
//		String storedToken = memberMapper.findTokenByUsername(username);
//		if (StringUtils.isEmpty(storedToken)) return false;
//		if (token.equals(storedToken)) return true;
//		
//		return false;
		
		return this.getClaims(token) != null;
	}
	
	public Authentication getAuthentication(String token) throws AuthenticationException {
		Claims claims = this.getClaims(token);
		if (claims == null) {
			throw new AuthenticationException(ResponseType.INVALID_TOKEN);
		}
		
		Collection<? extends GrantedAuthority> authorities = 
				Collections.singletonList(new SimpleGrantedAuthority(claims.get(CLAIM_AUTHORITIES_KEY).toString()));
		
		return new UsernamePasswordAuthenticationToken(claims.get(CLAIM_MEMBER_KEY), "", authorities);
	}
	
	private Claims getClaims(String token) {
		try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (UnsupportedJwtException e) {
            log.info("[INFO] 지원하지 않는 형식의 토큰", e.getMessage());
        } catch (SecurityException | JwtException e) {
            log.info("[INFO] 토큰이 만료되었거나 잘못된 토큰", e.getMessage());
        }
		
		return null;
	}
}