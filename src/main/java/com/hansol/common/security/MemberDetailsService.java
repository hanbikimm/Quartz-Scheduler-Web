package com.hansol.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.common.payload.ResponseType;
import com.hansol.member.domain.Member;
import com.hansol.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
	private final MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws ValueNotFoundException {
		log.info("[INFO] 계정 로그인 시도 (Username/Email): {}", usernameOrEmail);
		
		Member member = memberMapper
				.findOne(usernameOrEmail)
				// .orElseThrow(() -> new UsernameNotFoundException(""));
				.orElseThrow(() -> new ValueNotFoundException(ResponseType.MEMBER_NOT_FOUND));
		
		log.info("[INFO] 계정 로그인 확인 (Username): {}", member.getUsername());
		
		return MemberDetails.create(member);
	}
}