<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Spring Project</title>
<link rel="canonical" href="https://getbootstrap.com/docs/4.5/examples/carousel/">
<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}

.chatting-area{
   width : 100%;
   height : 450px;
}

.display-chatting{
   width: 100%;
   height: 300px;
   border : 1px solid black;
   overflow: auto;
   list-style : none;
   padding : 10px 10px;
}

.chat{
   display: inline-block;
   border-radius: 20%;
   padding : 5px;
   background-color: #eee;
}


.input-area{
   width: 100%;
   height: 80px;
   display: flex;
}

#inputChatting{
   width : 90%;
   resize : none;
}

#send{
   width : 10%;
   height : 100%;
}

.myChat{
   text-align: right;
}

</style>
<!-- Custom styles for this template -->
<link href="resources/css/carousel.css" rel="stylesheet">
</head>
<body>
	<!-- jsp 액션 태그를 이용한 동적 include -->
	<jsp:include page="header.jsp" />

	<main role="main">

		<!-- carousel 부트스트랩 -->
		<div id="myCarousel" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators">
				<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			</ol>
			<div class="carousel-inner">
				<div class="carousel-item active">
					<svg class="bd-placeholder-img" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img">
					<rect width="100%" height="100%" fill="#eee" /></svg>
					<div class="container">
						<div class="carousel-caption text-left">
							<h1>Spring Framework 수업용 페이지 입니다.</h1><br><br>

							<h5>
								Spring, Mybatis, Maven, Bootstrap등의 다양한 프레임워크를 이용해서<br> Spring MVC 형태로 개발 중입니다.
							</h5>
							<a class="btn btn-lg btn-success" href="#" role="button">View details</a>
						</div>
					</div>
				</div>
			</div>

			<!-- 이전/다음 화살표 -->
			<a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev"> <span class="carousel-control-prev-icon" aria-hidden="true"></span> <span class="sr-only">Previous</span>
			</a> <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next"> <span class="carousel-control-next-icon" aria-hidden="true"></span> <span class="sr-only">Next</span>
			</a>
		</div>


		<!-- Marketing messaging and featurettes
  ================================================== -->
		<!-- Wrap the rest of the page in another container to center all the content. -->
		<hr>
		<div class="container marketing">

			<!-- Three columns of text below the carousel -->
			<div class="row">
				<div class="col-lg-4">
					<h3>자유게시판 조회수 Top5</h3>
					<table align="center">
						<thead>
							<th>글번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>조회수</th>
						</thead>

						<tbody id="freeBoard-topViews">

						</tbody>
					</table>
				</div>

				<div class="col-lg-8">
					<div class="chatting-area">
						<ul class="display-chatting"></ul>

						<div class="input-area">
							<textarea id="inputChatting" rows="3"></textarea>
							<button id="send">보내기</button>
						</div>
					</div>
				</div>


			</div>
		</div>
		<!-- /.row -->
	</main>
	
	 <!-------------------- sockjs를 이용한 WebSocket 구현을 위해 라이브러리 추가 --------------------->
	 <!-- 자바스크립트에서 웹소켓을 생성하고 사용하기 위한 -->
   <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>

	<script>
		//--------------------- WebSocket -------------------------
		
		var chattingSock; // SockJs를 이용한 WebSocket 객체 저장 변수 선언
		
		// 로그인이 되어 있을때만 
		// 서버 측 /chat 이라는 주소로 통신을 할 수 있는 WebSocket 객체 생성
		<c:if test="${!empty loginMember}">
			chattingSock = new SockJS("${contextPath}/chat");
		</c:if>
		
		// 로그인된 회원이 채팅을 입력한 후 보내기 버튼을 클릭한 경우 
		// (전달할 내용 : 입력한 채팅 + 로그인한 회원의 아이디 )
		
		var memberId = "${loginMember.memberId}";
		
		$("#inputChatting").keyup(function(e){
			
			if(e.keyCode == 13){
				$("#send").click();
			}
			
		});
		
		
		$("#send").on("click",function(){
			
			
				
			if(memberId == ""){ // 로그인이 되어있지 않은 경우
				
				alert("로그인 후 이용해주세요");
				
			} else {
				
				var chat = $("#inputChatting").val(); // 채팅창에 입력된 값을 얻어온다.
				
				if(chat.trim().length == 0){ // 채팅이 입력되지 않은 상태로 보내기 버튼을 클릭한 경우
					
					alert("채팅을 입력해주세요.");
					
				} else {
					
					// 자바스크립트의 객체를 문자열로 표현해주는게 JSON이다.
					var obj = {}; // 비어있는 객체 선언 
					obj.memberId = memberId; // obj객체에 memberId 속성을 추가하고 값으로 memberId 변수 값을 추가 
					obj.chat = chat;
					
					// 작성자와 채팅 내용이 담긴 obj 객체를 JSON 형태로 반환하여 웹소켓 핸들러로 보내기
					chattingSock.send(JSON.stringify(obj));
					// JSON.stringify(obj) : 자바스크립트 객체 obj를 JSON 형태의 문자열로 변환하는 자바스크립트 내장 함수 
					// 자바스크립트에서 자바로 객체를 보낼때 사용한다.
					
					$("#inputChatting").val(""); // 채팅을 보낸 후 채팅창에 남아있는 글자를 삭제 
					
					
					
				}
				
			}
			
		});
		
		// WebSocket 객체 ChattingSock이 서버로부터 받은 메세지가 있을 경우 수행되는 콜백 함수
		chattingSock.onmessage = function(event){
			// event : 서버로부터 메세지를 전달받은 이벤트와 관련된 모든 내용이 저장된 객체
			// event.data : 전달받은 메세지 
			var obj = JSON.parse(event.data);
			// JSON.parse() : JSON 데이터를 자바스크립트 객체로 변환하는 함수 
			// console.log(obj.memberId);
			// console.log(obj.chat);
      var li = $("<li>");
      var p = $("<p class='chat'>");
      
      var writer = obj.memberId;
      var chat = obj.chat.replace(/\n/g, "<br>");   
      
      
      if(obj.memberId == memberId){
         li.addClass("myChat");
         p.html(chat).css("backgroundColor", "yellow");
      }else{
         li.html("<b>" + writer + "</b><br>");
         p.html(chat);
      }
      
      li.append(p);
      $(".display-chatting").append(li);
      
      // 채팅 입력 시 스크롤을 가장 아래로 내리기
      $(".display-chatting").scrollTop($(".display-chatting")[0].scrollHeight);
      
		}
	
	</script>


	<jsp:include page="footer.jsp" />
</body>
</html>