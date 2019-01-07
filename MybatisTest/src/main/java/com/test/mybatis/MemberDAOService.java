package com.test.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //붙여줘야  component-scan했을 때 자동으로 등록된다.
public class MemberDAOService implements MemberDAO {

	@Autowired //관계설정. root-context.xml에 정의한 bean.
	private SqlSession sqlSession;

	@Override
	public ArrayList<Member> getMembers() {
		ArrayList<Member> result = new ArrayList<Member>();
		//sqlSession=sqlSessionTemplate객체
		/*getMapper()에 의해 리턴된 매퍼가 가진 메서드를 포함해서 SQL을 처리하는 마이바티스 메서드를 호출할때 SqlSessionTemplate은 
		SqlSession이 현재의 스프링 트랜잭션에서 사용될수 있도록 보장.+ SqlSessionTemplate은 필요한 시점에 세션을 닫고,커밋하거나 롤백하는 것을 포함한 세션의 생명주기를 관리.*/
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		//getMembers()의 메소드명과 MemberMapper.xml과 id는 동일해야한다.
		result = memberMapper.getMembers();
		
		return result;
	}


	@Override
	public void insertMember(Member member) {
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		memberMapper.insertMember(member);
	}


	@Override
	// 내용은 비워둠.
	public void updateMember(String name) {
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		memberMapper.updateMember(name);
	}


	@Override
	public void deleteMember(String name) {
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		memberMapper.deleteMember(name);
	}

}