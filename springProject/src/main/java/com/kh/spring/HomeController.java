package com.kh.spring;

import java.text.DateFormat;

import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller // 프레젠테이션 레이어 , 웹 애플리케이션에서 전달된 요청과 응답을 처리하는 클래스임을 지정  + bean 등록(제일 앞에서 받아주는 역할 )
public class HomeController {
		
//	@RequestMapping : 요청을 매핑할 주소와 전송 타입을 작성하는 어노테이션이다.
//	value : 매핑할 요청 주소 , * 같은 패턴도 사용할 수 있다.
//	method : 요청되는 데이터의 전송 방식 지정 
//			-> 생략된 경우 : 모든 전송 방식에 알아서 매핑
//	해당 어노테이션은 메소드 뿐만 아니라 클래스에도 작성이 가능하다.
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		/* Spring의 컨트롤러 메소드에서 반환형이 존재함(String , ModelAndView (int 등등) )
		 * 반환 값으로는 응답화면에 해당되는 view의 이름(파일명)을 작성 
		 * 
		 * 반환 값을 작성하는 방법이 두가지 존재
		 * 1) forward 방식(요청 위임)을 하고싶을 때 -> 파일명만 작성 (ex: "home")
		 * 		-> Dispatcher Servlet으로 돌아간 후 View Resolver에 의해 경로가 완성된다.
		 *   
		 * 2) redirect 방식(다른 요청 주소를 재요청) -> "redirect:요청주소" (ex : "redirect:/") 
		 * 		-> Dispatcher Servlet으로 돌아간 후 Handler Mapping으로 전달된다.
		 * 
		 */
		
		return "common/main";
	}
	
}
