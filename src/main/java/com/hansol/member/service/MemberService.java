package com.hansol.member.service;

import com.hansol.common.exception.DuplicateValueException;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.member.domain.Member;
import com.hansol.member.payload.MemberCreationRequest;
import com.hansol.member.payload.MemberLoginRequest;

public interface MemberService {
	Member getMember(int memberId) throws ValueNotFoundException;
	Member getMember(String usernameOrEmail) throws ValueNotFoundException;
	boolean isDuplicateUsername(String username);
	boolean isDuplicateEmail(String email);
	Member register(MemberCreationRequest payload) throws DuplicateValueException;
	String login(MemberLoginRequest payload);
//	String recreateToken();
}