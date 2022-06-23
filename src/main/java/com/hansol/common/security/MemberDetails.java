package com.hansol.common.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.hansol.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Member member;
	private Collection<? extends GrantedAuthority> authorities;
	
	public static MemberDetails create(Member member) {
        List<GrantedAuthority> authorities = Collections.singletonList(
        		new SimpleGrantedAuthority(member.getRole().name())
		);
        
		return new MemberDetails(member, authorities);
	}
	
	@Override
	public String getUsername() {
		return member.getUsername();
	}
	
	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}