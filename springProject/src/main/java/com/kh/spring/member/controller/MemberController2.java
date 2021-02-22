package com.kh.spring.member.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.service.MemberService2;
import com.kh.spring.member.model.vo.Member;

@Controller
@RequestMapping("/member2/*")
@SessionAttributes({"loginMember"}) // 세션에 있는 "loginMember" 키 값을 가진 데이터를 
									// 해당 클래스내의 Model이 얻어가서 쓸수 있게한다.
public class MemberController2 {
	
	@Autowired // 해당 자료형과 일치하는 bean 의존성 주입(DI)
	private MemberService2 service;
	
//	sweet alert 메시지 전달용 변수 선언 
	private String swalIcon;
	private String swalTitle;
	private String swalText;
	
	
	
	
//	회원가입 화면 전환용 Controller
	
	@RequestMapping("signUp")
	public String signUpView() {
		
		return "member/signUpView";
	}
	
//	아이디 중복체크 Controller (AJAX)
	@RequestMapping("idDupCheck")
	@ResponseBody
	public int idDupCheck(@RequestParam("memberId") String memberId) {
		
//		System.out.println(memberId);
		
//		아이디 중복 검사 서비스 호출 
		int result = service.idDupCheck(memberId);
		
//		컨트롤러에서 반환되는 값은 forward 또는 redirect를 위한 경로 / 주소가 작성되는게 일반적이다.
//		-> 컨트롤러에서 반환 시 Dispatcher Servlet으로 반환 값이 이동되어
//			View Resolver 또는 Handler Mapping으로 연결된다.
		
//		AJAX에서 반환값이 주소 / 경로가 아닌 값 자체로 인식해서 요청 부분으로 돌아가게하는 별도의 방법이 존재한다.
//		== @ResponseBody 
		
		
		return result;
	}
	
//	회원 가입 Controller
	@RequestMapping("signUpAction")
	public String signUpAction(@ModelAttribute Member signUpMember,
								RedirectAttributes ra // 리다이렉트 시 데이터 전달용 객체
								/*String memberInterest*/) {
		
//		signUpMember : 회원가입 시 입력한 값들이 저장된 커맨드 객체 
//		System.out.println(signUpMember);
		
//		동일한 name 속성을 가진 input태그 값은 
//		String[]에 저장할 경우 , 배열 요소로 저장되며 ,String 으로 저장할 경우 ","로 구분된 한 줄의 문자열이 된다.
//		System.out.println(memberInterest);
		
//		회원가입 서비스 호출 (성공 시 1 , 실패 시 0이 반환된다.(Mybatis-insert) (성공한 행의 개수가 반환되는것이 아님)) 
		int result = service.signUp(signUpMember);
		
//		회원가입 성공 여부에 따른 메세지 
		if(result > 0) { // 회원가입 성공 시
			
			swalIcon = "success";
			swalTitle = "회원가입이 완료되었습니다.";
			swalText = "회원이 되신걸 환영합니다.";
			
		} else { // 회원가입 실패 시
			
			swalIcon = "error";
			swalTitle = "회원가입이 실패 되었습니다.";
			swalText = "회원가입 과정에서 문제가 발생하였습니다.";
			
		}
		
//		redirect시 세션으로 스왈을 넘기기 위함 (redirect는 requestScope)
		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);
		ra.addFlashAttribute("swalText", swalText);
		
		
		return "redirect:/"; // 메인화면 재요청
	}
	
//	내 정보 페이지 전환용 Controller
	@RequestMapping("mypage")
	public String myPage() {
		
		return "member/mypage";
	}
	
	
//	회원 정보 수정 Controller
	@RequestMapping(value = "updateAction", method = RequestMethod.POST)
	public String updateAction(@ModelAttribute Member updateMember , Model model ,
						@ModelAttribute(name = "loginMember" , binding = false) Member loginMember) {
//		Model로 인해 세션에 올라간 것을 받아오는 구문  			
// 		binding 속성 : 요청 파라미터를 해당 객체에 반영 할 것인가 ? (파라미터를 여기다 세팅하지말아라) (디비는 바뀌지않고 세션만 바뀌는 현상을 방지)
//		updateMember : 이메일 , 전화번호 , 주소 , 관심분야
		
//		세션에서 회원정보를 얻어오는 방법 
//		1. HttpSession의 getAttribute("loginMember");
		
//		2. Model , @SessionAttributes로 세션에 등록된 값은 반대로 얻어오는 것도 가능하다 .
//		Member loginMember =  (Member)model.getAttribute("loginMember");
		
//		3.@ModelAttribute를 이용하여 Model로 새팅한 값을 반대로 얻어오는것도 가능하다.
//		매개변수에 @ModelAttribute("모델로 등록한 key값") 자료형 변수명 
				
//		System.out.println(loginMember);
		
//		수정된 회원정보 + 로그인된 회원의 번호를 가지고 Service 수행
//		로그인된 회원의 식별 역할을 할 수 있는 맴버넘버를 세팅하여 하나의 매개변수로 넘긴다. ( 왜 ? 둘다 Member VO )
		updateMember.setMemberNo(loginMember.getMemberNo());
		
		int result = service.updateAction(updateMember);
		
//		회원정보 수정 성공 여부에 따른 메세지 
		if(result > 0) { // 수정 성공 시
			
			loginMember.setMemberEmail(updateMember.getMemberEmail());
			loginMember.setMemberAddress(updateMember.getMemberAddress());
			loginMember.setMemberPhone(updateMember.getMemberPhone());
			loginMember.setMemberInterest(updateMember.getMemberInterest());
			
			model.addAttribute("loginMember" , loginMember);
			
			swalIcon = "success";
			swalTitle = "회원정보 수정 성공 !";
			swalText = "회원정보 수정이 정상적으로 이루어졌습니다.";
			
		} else { // 회원가입 실패 시
			
			swalIcon = "error";
			swalTitle = "회원정보 수정 성공 실패";
			swalText = "회원정보 수정이 정상적으로 이루어지지 않았습니다. ";
			
		}
		
		model.addAttribute("swalIcon", swalIcon);
		model.addAttribute("swalTitle", swalTitle);
		model.addAttribute("swalText", swalText);
		
		return "member/mypage";
	}
	
//	비밀번호 변경 화면 전환용  Controller
	@RequestMapping("changePwd")
	public String changePwd() {
		
		
		return "member/changePwd";
		
	}
	
//	비빌번호 변경 Controller
	@RequestMapping(value = "updatePwd" , method = RequestMethod.POST)
	public String updatePwd(@RequestParam("memberPwd") String memberPwd ,
							@RequestParam("newPwd1") String newPwd,
							@ModelAttribute(name = "loginMember" , binding = false) Member loginMember,
							RedirectAttributes ra) {
		
//		Map을 이용하여 필요한 데이터를 하나로 묶어 둔다.
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberPwd" , memberPwd);
		map.put("newPwd" , newPwd);
		map.put("memberNo" , loginMember.getMemberNo());
		
//		비밀번호 변경 Service 호출 
		int result = service.updatePwd(map);
		
		String url = null;
		
//		비밀번호 변경 성공 시 
//		success , 비밀번호 변경 성공 , 마이페이지 재요청
		
		if(result > 0 ) { // 비밀번호 변경 성공 시 
			
			swalIcon = "success";
			swalTitle = "비밀번호 변경 성공 !";
			
			url = "mypage";
			
//		비밀번호 변경 실패 시 
//		error , 비밀번호 변경 실패 , 비밀번호 변경 페이지 재요청 
			
		} else { // 비밀번호 변경 실패 시 
			
			swalIcon = "error";
			swalTitle = "비밀번호 변경 실패 !";
			
			url = "changePwd";
			
		}
		
		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);
		
		
		
		return "redirect:" + url;
		
	}
	
	
//	회원 탈퇴 화면 전환용  Controller
	@RequestMapping("secession")
	public String secession() {
		
		
		return "member/secession";
		
	}
	
//	회원탈퇴 Controller
	@RequestMapping("deleteMember")
	public String deleteMember(@ModelAttribute(name = "loginMember", binding = false) Member loginMember,
			RedirectAttributes ra, /* @RequestParam("memberPwd") String memberPwd, */ SessionStatus status) {
		
//		회원 번호가 필요 == SEssion에 있는 loginMember에 저장되어 있음
//		--> @ModelAttribute("loginMember")를 통해서 얻어온다.
		
//		입력받은 현재 비밀번호 필요 == parameter로 전달 받음(memberPwd)
//		--> @ModelAttribute를 통해 Member 객체에 자동으로 세팅된다.
		
//		회원번호, 현재 비밀번호를 하나의 VO에 담아서 Service로 전달할 예정
//		--> 이 작업을 별도로 진행하지 않고 @ModelAttribute를 이용하여 진행
		
//		loginMember.setMemberPwd(memberPwd);

		int result = service.deleteMember(loginMember);

		String url = "";

		if (result > 0) {
			swalIcon = "success";
			swalTitle = "회원 탈퇴 성공";
			url = "/"; // 메인 페이지
			
//			탈퇴 성공 시 로그아웃 
			status.setComplete();

		} else if (result == 0) {
			swalIcon = "error";
			swalTitle = "회원 탈퇴 과정에서 문제 발생";
			url = "secession"; 

		} else {
			swalIcon = "warning";
			swalTitle = "현재 비밀번호가 틀렸습니다.";
			url = "secession";
		}
		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

		return "redirect:" + url;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
}
