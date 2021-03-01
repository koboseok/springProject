package com.kh.spring.common.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.member.model.vo.Member;

public class ChattingWebsocketHandler extends TextWebSocketHandler {
	@Autowired
	private BoardService service;
	
	   /* WebSocket
    - 브라우저와 웹서버간의 전이중통신을 지원하는 프로토콜이다
    - HTML5버전부터 지원하는 기능이다.
    - 자바 톰캣7버전부터 지원했으나 8버전부터 본격적으로 지원한다.
    - spring4부터 웹소켓을 지원한다. 
    (전이중 통신(Full Duplex): 두 대의 단말기가 데이터를 송수신하기 위해 동시에 각각 독립된 회선을 사용하는 통신 방식. 대표적으로 전화망, 고속 데이터 통신)
    
    
    
    WebSocketHandler 인터페이스 : 웹소켓을 위한 메소드를 지원하는 인터페이스
       -> WebSocketHandler 인터페이스를 상속받은 클래스를 이용해 웹소켓 기능을 구현
    
    
    WebSocketHandler 주요 메소드
            
       void handlerMessage(WebSocketSession session, WebSocketMessage message)
       - 클라이언트로부터 메세지가 도착하면 실행
       
       void afterConnectionEstablished(WebSocketSession session)
       - 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행

       void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
       - 클라이언트와 연결이 종료되면 실행

       void handleTransportError(WebSocketSession session, Throwable exception)
       - 메세지 전송중 에러가 발생하면 실행 
    
    
    ----------------------------------------------------------------------------
    
    TextWebSocketHandler :  WebSocketHandler 인터페이스를 상속받아 구현한 텍스트 메세지 전용 웹소켓 핸들러 클래스
   
       handlerTextMessage(WebSocketSession session, TextMessage message)
       - 클라이언트로부터 텍스트 메세지를 받았을때 실행

*/
	
//	로그 사용을 위한 로거 선언 
	private Logger logger = LoggerFactory.getLogger(ChattingWebsocketHandler.class);
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());
	// synchronizedSet : 동기화된 Set 반환(HashSet은 기본적으로 비동기)
	// -> 멀티스레드 환경에서 하나의 컬렉션에 여러 스레드가 접근하여 의도치 않은 문제가 발생되지 않게 하기 위해
	// 동기화를 진행하여 스레드가 여러 순서대로 한 컬렉션에 순서대로 접근할 수 있게 변경.

//	alt shift s -> v == 자동완성
//	클라이언트와 연결이 완료되고 , 통신 준비가 되면 실행되는 메소드 
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
//		WebSocketSession : 서버간 전이중통신을 담당하는 객체 
//		-> WebSocketSession은 HttpSession의 속성을 가로채서 똑같이 사용할 수 있다는 특징이 있다.
		
//		중괄호 안에 session.getId()가 포함된다.
		logger.info("{}연결됨", session.getId());
		
		sessions.add(session);
//		클라이언트별로 최소 생성된 WebSocketSession을 sessions Set에 저장하여 
//		모든 접속자 정보를 저장한다.
//		--> 모든 접속자 정보를 알아야 채팅 내용을 모두에게 실시간 전달 할 수 있다.
	}

//	클라이언트로부터 텍스트(문자열) 메세지를 받았을 때 수행 
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
//		TextMessage : 웹소켓으로 전달받은 메세시가 담겨있는 객체
		logger.info("전달받은 내용 : " + message.getPayload());
//		Payload : 전송되는 데이터 
		
//		JSON 데이터를 JsonObject로 변형하여 필요한 데이터 꺼내서 사용하기
		JsonObject obj = new Gson().fromJson(message.getPayload(), JsonObject.class);
		
		logger.info("채팅 작성자 : " + obj.get("memberId").toString());
		logger.info("채팅 내용 : " + obj.get("chat").toString());
		
		
		
//		client -> 채팅 입력 -> Server -> 접속한 모든 사용자에게 전달  -> 받은 메세지를 화면에 표시
		
//		sessions에 로그인된 사용자 정보가 모두 담겨있다.
//		어떻게 알지 ? WebSocketSession은 servlet-context.xml 파일에 작성된 interceptor에 의해서
//		로그인된 사용자 정보가 담긴 HttpSession 객체를 가로채서 자신의 것처럼 사용할 수 있기 때문이다 .
		
		
		for(WebSocketSession s : sessions) {
			
//			현재 접속중인 회원의 아이디 얻어오기 
			String loginMemberId = ((Member)s.getAttributes().get("loginMember")).getMemberId();
			logger.info(loginMemberId + "회원에게 채팅 전달");
			
			s.sendMessage(new TextMessage(message.getPayload()));
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
