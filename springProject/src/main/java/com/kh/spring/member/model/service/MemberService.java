package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {
	
	/*
	 * Service Interface를 사용하는 이유
	 * 
	 * 1. 프로젝트 규칙성을 부여하기 위하여 
	 * 2. 결합도 약화를 위하여  -> 유지보수성 향상
	 * 3. 구 버전 Spring의 AOP 호환을 위해서  
	 * 
	 */
	
	
	/** 로그인 Service
	 * @param inputMember
	 * @return loginMember
	 */
	public abstract Member loginAction(Member inputMember) ;

}
