package com.hansol.member.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hansol.member.domain.Member;

@Mapper
public interface MemberMapper {
	int create(Member member);
	
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	Optional<Member> findOne(@Param("usernameOrEmail") String usernameOrEmail);
	Optional<Member> findOneById(@Param("memberId") int memberId);
//	String findTokenByUsername(@Param("username") String username);
	
//	int updateToken(@Param("token") String token, @Param("username") String username);
}