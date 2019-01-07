package com.test.mybatis;

import java.util.ArrayList;

import com.test.mybatis.Member;

public interface MemberMapper {
	ArrayList<Member> getMembers();
	void insertMember(Member member);
	void updateMember(String name);
	void deleteMember(String name);
}