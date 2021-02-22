package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository
public class MemberDAO {
	
//	DI를 이용하여 SqlSession을 주입 받는다.
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	

	/** 로그인 DAO
	 * @param inputMember
	 * @return loginMember
	 */
	public Member loginAction(Member inputMember) {
		
		return sqlSession.selectOne("memberMapper.loginAction" , inputMember);
	}

}
