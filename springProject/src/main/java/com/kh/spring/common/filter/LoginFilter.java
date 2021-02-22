package com.kh.spring.common.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 전체 요청이 필터를 거치게 한다.
@WebFilter(urlPatterns = {"/*"})

public class LoginFilter implements Filter {

//	로그인이 되어있지 않아도 접근 가능한(허용되는) 경로를 모아둘 Set 생성 
	private static final Set<String> ALLOWED_PATH = new HashSet<String>();
//	final -> 상수면 변수명 모두 대문자
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
//		로그인이 되어있지 않아도 접근 가능한 경로 추가 
//		ALLOWED_PATH.add("/"); // 메인 페이지 
//		ALLOWED_PATH.add("/resources/\\w"); // 이미지, css, js 파일 등을 접근할 수 있는 경로 추가
		
//		회원 전용 페이지 중 로그인이 되어있지 않아도 접근 가능한 페이지 경로 추가
		ALLOWED_PATH.add("/member/login"); // 로그인 페이지 요청 
		ALLOWED_PATH.add("/member/loginAction"); // 로그인 요청 
		ALLOWED_PATH.add("/member/signUp"); // 회원가입 페이지 요청 
		ALLOWED_PATH.add("/member/signUpAction"); // 회원가입  요청 
		ALLOWED_PATH.add("/member/idDupCheck"); // 아이디 중복 검사 요청 
		
//		ServletRequest 매개변수를 HttpServletRequest로 다운 캐스팅
		HttpServletRequest req = (HttpServletRequest)request; 
		HttpServletResponse res = (HttpServletResponse)response;
//		Session 얻어오기
		HttpSession session = req.getSession();
		
//		요청 주소 확인 
		String path = req.getRequestURI().substring(req.getContextPath().length());
//					/spring/member/login				/spring
//		-> /spring을 잘라내  /member/login을 path에 저장
		
//		로그인 여부 확인 
		boolean isLogin = session.getAttribute("loginMember") != null ;
//		null이 아니면 t , null이면 f
		
//		요청 주소가 허용 목록에 있는 주소인지 확인 
		boolean isAllowedPath = false;
		
		for(String p : ALLOWED_PATH) {
			
//			ALLOWED_PATH 목록의 내용과 요청주소가 일치할 경우 
			if(Pattern.matches(p, path)) {
//				ALLOWED_PATH == p , 요청주소 == path
//				요청주소가 p와 같은지 판별
				isAllowedPath = true;
				break;
			}
		}
		
//		로그인이 되어있지 않고 , 
//		로그인이 되어있지 않아도 접근 가능한 경로로 요청이 온 경우 
//		== 비로그인 상태로 요청 허용된 주소 

		if( !isLogin && isAllowedPath ) { // * 로그인을 안하고도 이용 가능한 요청 주소들 
			chain.doFilter(request, response);
	
		}else if(isLogin && !isAllowedPath ) {
//			로그인이 되어있을 경우 접근 불가능한 페이지 
//			== 로그인이 되었을 때만 이용 가능한 요청 주소 
			chain.doFilter(request, response);
			
		} else { // 로그인이나 허용 주소 여부 관계없이 
			if(Pattern.matches("/", path) || Pattern.matches("/resources/.*", path) 
					|| Pattern.matches("/board/list/.*", path) ) {
				chain.doFilter(request, response);
			}else {
				res.sendRedirect(req.getContextPath()); // 메인페이지로 이동
			}
		}

		
		
		
		
	}
	
    public LoginFilter() {}
    public void init(FilterConfig fConfig) throws ServletException {}
	public void destroy() {}
	
	
	


	
}
