package com.kh.spring.member.model.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.member.model.dao.MemberDAO2;
import com.kh.spring.member.model.vo.Member;

@Service
public class MemberService2Impl implements MemberService2{
	
	
	@Autowired
	private MemberDAO2 dao;
	
//	암호화를 위한 객체를 의존성 주입(DI) -> (스프링 컨테이너가 생명주기를 가지고있는 bean을 주입)
	@Autowired
	private BCryptPasswordEncoder enc;

//	아이디 중복 체크 Service 구현 
	@Override
	public int idDupCheck(String memberId) {
		
		
		return dao.idDupCheck(memberId);
	}
	
	/* 스프링에서 트랜잭션 처리 하는 방법 
	 * 
	 * 1) 코드 기반 처리 방법 -> 기존 방법 
	 * 2) 선언적 트랜잭션 처리 방법( AOP가 적용되는 방법)
	 * 	1. <tx:advice> XML 작성 방법
	 * 	2. @Transactional 어노테이션 작성 방법
	 * 	* 조건) 트랜잭션 처리를 위한 매니저가 bean으로 등록되어 있어야한다.
	 * 		매니저 역할을 하는 sqlSessionTemplate을 root-context.xml에 작성 해놓았다.
	 * 
	 * @Transactional이 commit / rollback을 하는 기준
	 * -> 해당 메소드 내에서 RuntimeException이 발생하면 rollback , 발생하지 않으면 commit
	 * 실패 == 0 , -> 실패시에는 commit , rollback을 하지 않아도 된다.
	 * RuntimeException은 알맞지 않아 
	 * -> 발생하는 예외의 기준을 바꾸는 방법 : rollbackFor 속성을 사용  
	 *  
	 * */
//	회원가입 Service 구현	
	@Transactional(rollbackFor = Exception.class) //  아무 예외 발생 시 롤백
//								 SQLException.class -> 디비 관련 오류가 나타났을때 롤백을 진행하겠다 .
	@Override
	public int signUp(Member signUpMember) {
//		암호화 추가 예정
		/* 비밀번호를 저장하는 방법 
		 * 
		 * 1. 평문 형태 그대로 저장 -> 범죄 행위
		 * 
		 * 2. SHA-512 같은 단방향 암호화 (단방향 해쉬함수)를 사용한다.
		 * 	-> 같은 비밀번호를 암호화 하면 똑같은 다이제스트가 반환된다는 문제점이 있다.(해킹에 취약하다.)  
		 * 	(암호화된 비밀번호 == 다이제스트)
		 * 	(일반적인 해킹 장비 성능으로 1초에 56억개의 다이제스트를 비교할 수 있다.)
		 * 
		 * 3. bcrypt 방식의 암호화 
		 * 	-> 비밀번호 암호화에 특화된 암호화 방식이다 .
		 * 	-> 입력된 문자열과 임의의 문자열(salt)을 첨부하여 암호화를 진행한다.
		 * 		-> 같은 비밀번호를 입력해도 서로 다른 다이제스트가 반환된다.
		 * 
		 * ** Spring-security-core 모듈 추가 ! (라이브러리) -> pom.xml에 추가 메이븐 레파지토리 홈페이지에서
		 * 
		 * */
		
//		DAO 전달 전에 비밀번호 암호화
		String encPwd = enc.encode(signUpMember.getMemberPwd());
//						 			 변경하고자하는 비밀번호 입력 
//		암호화된 비밀번호를 signUpMember에 다시 세팅
		signUpMember.setMemberPwd(encPwd);
		
		return dao.signUp(signUpMember);
//		해당 서비스 메소드에서 예외가 발생하지 않으면 마지막에 commit이 자동으로 수행된다.
	}

//	회원정보 수정 Service구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateAction(Member updateMember) {

		return dao.updateAction(updateMember);
	}

	
	// 비밀번호 변경 Service 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updatePwd(Map<String, Object> map) {
		// 현재 비밀버호, 새 비밀번호, 회원 번호

		// 1. 현재 비밀번호가 일치하는 확인 (본인 확인)
		// 비크립트 암호화가 적용되어 있기 때문에
		// DB에서 비밀번호를 조회해서 비교해야 함. == 현재 비밀번호 조회 dao 필요
		String savePwd = dao.selectPwd((int) map.get("memberNo"));

		// 결과 저장용 변수 선언
		int result = 0;

		if (savePwd != null) {

			// 비밀번호 확인
			if (enc.matches((String) map.get("memberPwd"), savePwd)) {
				// 비밀번호가 일치할 경우

				// 2. 현재 비밀번호 일치 확인 시 새 비밀번호로 변경
				// == 비밀번호를 수정할 dao 필요

				// 새 비밀번호 암호화 진행
				String encPwd = enc.encode((String) map.get("newPwd"));

				// 암호화된 비밀번호를 다시 map에 세팅
				map.put("newPwd", encPwd);

				// 비밀번호 수정 DAO 호출
				result = dao.updatePwd(map);
			}

		}

		return result;
	}

//	 회원 탈퇴
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteMember(Member loginMember) {
		
//		1) 입력받은 비밀번호가 맞는지 확인
//		--> bcrypt 암호화 사용하였기 때문에
//			DB에서 회원번호를 조건으로하여 비밀번호를 조회해 온 후 matches() 메소드를 이용하여 비교

		String savePwd = dao.selectPwd(loginMember.getMemberNo());

		int result = 0;

		if (savePwd != null) {  // 비밀번호 조회 성공 시

			if (enc.matches(loginMember.getMemberPwd(), savePwd)) {
//				2) 입력받은 비밀번호와 , 저장된 비밀번호가 같을 경우 
//					회원탈퇴 DAO 메소드 호출 

				result = dao.deleteMember(loginMember);
				
			} else {
				
				result = -1;
			}
		}

		return result;
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
