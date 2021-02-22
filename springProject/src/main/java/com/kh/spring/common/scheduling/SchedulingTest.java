package com.kh.spring.common.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // bean 등록 
public class SchedulingTest {

	/*	Spring Scheduling ( == Scheduler ) 
	 * 	- 스프링에서 제공하는 스케줄러로 지정된 시간 또는 특정 간격마다 작업(job)을 진행하도록 하는 기능	
	 *	
	 *	사용 조건 
	 *	1. dispatcher servlet ( servlet-context.xml ) 파일에 
	 *		task namespace를 추가 + <task:annotation-driven/> 추가 -> 스케쥴러 관련 어노테이션 활성화
	 * 
	 * 	2. 작업이 작성된 클래스를 Bean으로 등록 ( == bean 등록 == 스프링 컨테이너가 제거가능(IOC))
	 * 		* bean 등록 어노테이션 : @Controller , @Service , @Repository , @Component
	 * 
	 * 	3. 지정된 작업이 기록된 메소드를 작성 
	 * 		+ @Scheduled 어노테이션 작성 
	 * 	
	 * 	* @Scheduled 어노테이션 속성
	 * 	
	 * 	- fixedDelay : 이전 작업이 끝난 시점으로부터 고정된 시간(ms)만큼 지난 후 수행 
	 * 	- fixedRate  : 이전 작업이 시작된 시점으로부터 고정된 시간(ms)만큼 지난 후 수행 
	 * 
	 *  - cron : UNIX 계열 잡 스케쥴러 표현식 
	 *  
	 * 	cron="초 분 시 일 월 요일 [년도]"
	 *  (요일 : 일 == 1 , 토 == 7  ( 월 2, 화 3, 수 4, 목 5, 금 6))
	 * 	
	 * 	특수문자 
	 * 	* : 모든 수
	 * 	- : 두 수 사이의 값	ex) 10-15 : 10이상 15이하
	 *  , : 특정 값 지정 	ex) 3,4,7 : 3,4,7 마다 수행되게 지정
	 *  / : 값의 증가		ex) 0/5	  : 0부터 시작해서 5씩 증가할 때 마다  (5분간격 , 5일간격,5초간격)
	 *  ? : 특별한 값이 없음  (월 , 요일에만 해당) 
	 *  L : 마지막<Last>  (월 ,요일에만 해당)   
	 * 	
	 * 	2021년 2월 18일 목요일 10시 20분 30초 
	 * 	cron="30 20 10 18 2 5 2021"
	 * 
	 * 	매년 2월 18일 10시 20분 30초 
	 * 	cron = "30 20 10 18 2 * *"
	 * 	cron = "30 20 10 18 2 * " (년도 생략 가능) 
	 * 
	 * 	매일 10시 20분 30초
	 * 	cron="30 20 10 * * * *" 
	 * 	cron="30 20 10 * * *" (년도 생략 가능)
	 * 
	 * 	매일 자정 (0시)
	 * 	cron="0 0 0 * * *"
	 * 
	 */
	
	public static int num = 0;
	
	
//	매 분마다 반복
//	@Scheduled(cron="0 * * * * *")
//	@Scheduled(fixedDelay = 5000 ) // 이전 작업 종료 후 5초마다
//	@Scheduled(fixedRate = 5000 ) // 이전 작업 시작 후 5초마다 ( 일정 간격마다 수행 시 가장 정확하다)
	public void test() { 
//		* 주의사항 
//		@Scheduled 어노테이션이 작성된 메소드는 매개변수가 없어야 한다.
		
//		임의의 숫자 생성후 otp같은 기능도 만들수 있다 .
		
		System.out.println("스프링 스케쥴러 테스트 중입니다." + num++);
		
	}
	
}
