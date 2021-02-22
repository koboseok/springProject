package com.kh.spring.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect // 공통 관심사 ( 공통적으로 수행될 코드) 가 작성된 클래스임을 명시 
@Component // Spring Container가 제어하기 때문에 bean으로 등록한다.
public class TestAspect {
	
	private Logger logger = LoggerFactory.getLogger(TestAspect.class);
	
//	Pointcut : 공통 관심사가 수행되는 지점
//	excution(* com.kh.spring..*Impl*.*(..))
//	제일 앞에있는 별 [접근제한자](public...)
//	*  : 모든 
//	.. : 하위 모든 패키지 or 0개 이상의 매개변수
	
//	접근 제한자 상관없이 com.kh.spring 하위에 있는 클래스 중 
//	클래스명에 Impl이 들어가 클래스의 모든 메소드가 수핼 될 경우 
	@Before("execution(* com.kh.spring..*Impl*.*(..))")
	public void serviceStart( ) {
		logger.debug("----------- Service 수행 ----------");
	}
}
