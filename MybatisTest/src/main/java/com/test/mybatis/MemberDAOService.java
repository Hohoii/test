package com.test.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //�ٿ����  component-scan���� �� �ڵ����� ��ϵȴ�.
public class MemberDAOService implements MemberDAO {

	@Autowired //���輳��. root-context.xml�� ������ bean.
	private SqlSession sqlSession;

	@Override
	public ArrayList<Member> getMembers() {
		ArrayList<Member> result = new ArrayList<Member>();
		//sqlSession=sqlSessionTemplate��ü
		/*getMapper()�� ���� ���ϵ� ���۰� ���� �޼��带 �����ؼ� SQL�� ó���ϴ� ���̹�Ƽ�� �޼��带 ȣ���Ҷ� SqlSessionTemplate�� 
		SqlSession�� ������ ������ Ʈ����ǿ��� ���ɼ� �ֵ��� ����.+ SqlSessionTemplate�� �ʿ��� ������ ������ �ݰ�,Ŀ���ϰų� �ѹ��ϴ� ���� ������ ������ �����ֱ⸦ ����.*/
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		//getMembers()�� �޼ҵ��� MemberMapper.xml�� id�� �����ؾ��Ѵ�.
		result = memberMapper.getMembers();
		
		return result;
	}


	@Override
	public void insertMember(Member member) {
		MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
		memberMapper.insertMember(member);
	}


	@Override
	// ������ �����.
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