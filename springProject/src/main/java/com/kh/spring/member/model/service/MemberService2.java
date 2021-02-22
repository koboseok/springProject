package com.kh.spring.member.model.service;

import java.util.Map;

import com.kh.spring.member.model.vo.Member;

public interface MemberService2 {
	
//	인터페이스 내에 작성되는 모든 필드는 public static final이다.
//	인터페이스 내에 작성되는 모든 메소드는 묵시적으로 public abstract이다 .
//	그러므로 , 
//	public abstract int idDupCheck(String memberId); 
//	자동완성을 하는 경우 public abstract 가 위처럼 생성되지 않고 생략되어 생성된다.
	
	/** 아이디 중복 검사 Service
	 * @param memberId
	 * @return result
	 */
	int idDupCheck(String memberId);

	
	
	/** 회원가입 Service
	 * @param signUpMember
	 * @return result
	 */
	int signUp(Member signUpMember);



	/** 회원정보 수정 Service
	 * @param updateMember
	 * @return result
	 */
	int updateAction(Member updateMember);



	/** 비밀번호 변경 Service
	 * @param map
	 * @return result
	 */
	int updatePwd(Map<String, Object> map);



	/** 회원탈퇴 Service
	 * @param loginMember
	 * @return
	 */
	int deleteMember(Member loginMember);
	
	

}
