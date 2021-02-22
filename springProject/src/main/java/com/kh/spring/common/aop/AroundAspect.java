package com.kh.spring.common.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kh.spring.member.model.vo.Member;
@Aspect // 공통 관심사(공통적으로 수행될 코드)가 작성된 클래스임을 명시
@Component // Spring Container가 제어하기 때문에 bean으로 등록
public class AroundAspect {
	
	private Logger logger = LoggerFactory.getLogger(AroundAspect.class);
	
	// Pointcut : 공통 관심사가 수행되는 지점
	// "excution(* com.kh.spring..*Impl*.*(..))"
	//		[접근제한자]
	// *  : 모든
	// .. : 하위 모든 패키지 or 0개 이상의 매개변수
	
	// 접근 제한자 상관 없이 com.kh.spring 하위에 있는 클래스 중
	// 클래스명에 Impl이 들어가 클래스의 모든 메소드가 수행될 경우
	@Around("execution(* com.kh.spring..*Impl*.*(..))")
	public Object aroundLog(ProceedingJoinPoint pp) throws Throwable { // ServiceImpl 클래스의 메소드가 실행되기 전/후에 log를 출력하는 메소드 
		
		// JoinPoint 인터페이스 : Pointcut으로 지정된 메소드 수행 전/후 정보(파라미터 or 리턴값)를 참조할 수 있음.
		// ProceedingJoinPoint 인터페이스 : @Around 어노테이션 사용 시 사용해야되는 JointPoint 인터페이스의 자식 인터페이스
		
		HttpServletRequest request = null;
		String ip = null;
		Member loginMember = null;
		try { // 요청이 아닌 스프링 스케쥴러가 서비스를 호출하는 경우 에러가 발생하기 때문에 try-catch로 에러를 처리함
			
			// 접속자 IP 얻어오기
			request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			ip = request.getRemoteAddr();
			
			// 로그인된 회원 정보 얻어오기
			loginMember = (Member)request.getSession().getAttribute("loginMember");
		} catch (Exception e) {
			ip = "Spring Scheduler";
		}
		
		
		// 수행된 클래스명
		String className = pp.getTarget().getClass().getSimpleName(); // 대상 객체의 간단한 클래스명(패키지명 제외)
		
		// 수행된 메소드명
		String methodName = pp.getSignature().getName(); // 대상 객체 메소드의 정보 중 메소드명을 반환. 
		
		logger.debug("[Start] : " + className + " - " + methodName + "()");
		logger.debug("[parameter] : " + Arrays.toString(pp.getArgs()));
		// pp.getArgs() : 대상 메소드의 매개 변수를 반환한다.
		
		logger.debug("[ip] : " + ip);
		if(loginMember != null) {
			logger.debug("[id] : " + loginMember.getMemberId());
		}
		
		long startMs = System.currentTimeMillis(); // 서비스 시작 시의 ms 값
		
		// ProceedingJoinPoint.proceed() 메소드 : 메소드 수행 전/후를 나누는 기준
		Object obj = pp.proceed(); 
		
		long endMs = System.currentTimeMillis(); // 서비스 종료 시의 ms 값
		
		logger.debug("[Return Value] : " + obj.toString());
		logger.debug("[Running Time] : " + (endMs- startMs) + "ms\n");
		
		return obj;
	}
	
	
	// IP 를 IPv4 형식으로 출력하기
	// 메뉴 - Run - Run Configurations - Argumnet - VM arguments에 아래내용 추가
	// -Djava.net.preferIPv4Stack=true
}
