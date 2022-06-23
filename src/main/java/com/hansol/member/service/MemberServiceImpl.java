package com.hansol.member.service;

import java.time.LocalDateTime;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hansol.common.exception.AuthenticationException;
import com.hansol.common.exception.DuplicateValueException;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.common.payload.ResponseType;
import com.hansol.common.security.JwtAuthenticationProvider;
import com.hansol.member.domain.Member;
import com.hansol.member.domain.RoleType;
import com.hansol.member.mapper.MemberMapper;
import com.hansol.member.payload.MemberCreationRequest;
import com.hansol.member.payload.MemberLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtAuthenticationProvider tokenProvider;
	
	@Override
	public Member getMember(int memberId) throws ValueNotFoundException {
		return memberMapper
				.findOneById(memberId)
				.orElseThrow(() -> new ValueNotFoundException(ResponseType.MEMBER_NOT_FOUND));
	}
	
	@Override
	public Member getMember(String usernameOrEmail) throws ValueNotFoundException {
		return memberMapper
				.findOne(usernameOrEmail)
				.orElseThrow(() -> new ValueNotFoundException(ResponseType.MEMBER_NOT_FOUND));
	}
	
	@Override
	public boolean isDuplicateUsername(String username) {
		return memberMapper.existsByUsername(username);
	}
	
	@Override
	public boolean isDuplicateEmail(String email) {
		return memberMapper.existsByEmail(email);
	}
	
	@Override
	@Transactional
	public Member register(MemberCreationRequest payload) throws DuplicateValueException {
		if (isDuplicateUsername(payload.getUsername())) {
			throw new DuplicateValueException(ResponseType.DUPLICATE_USERNAME);
		}
		
		if (isDuplicateEmail(payload.getEmail())) {
			throw new DuplicateValueException(ResponseType.DUPLICATE_EMAIL);
		}
		
		LocalDateTime current = LocalDateTime.now();
		Member member = Member.builder()
				.username(payload.getUsername())
				.password(passwordEncoder.encode(payload.getPassword()))
				.email(payload.getEmail())
				.name(payload.getName())
				.role(RoleType.DEFAULT)
				.createdDt(current)
				.updatedDt(current)
				.build();
		
		memberMapper.create(member);
		log.info("[INFO] 계정 정보 저장 (Member ID): {}", member.getMemberId());
		
		return member;
	}
	
	@Override
	@Transactional
	public String login(MemberLoginRequest payload) {
		Authentication authentication = authenticate(payload.getUsernameOrEmail(), payload.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return this.createToken(authentication);
	}
	
//	@Override
//	@Transactional
//	public String recreateToken() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null) {
//			throw new AuthenticationException(ResponseType.UNAUTHENTICATED);
//		}
//		
//		return this.createToken(authentication);
//	}
	
	private Authentication authenticate(String usernameOrEmail, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException(ResponseType.MEMBER_LOGIN_FAILURE);
        }
    }
	
	private String createToken(Authentication authentication) {
		String token = tokenProvider.createToken(authentication);
//		String username = authentication.getName();
//
//		memberMapper.updateToken(token, username);

		return token;
	}
}