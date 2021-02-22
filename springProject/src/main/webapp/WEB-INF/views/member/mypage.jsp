<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내정보</title>
<style>
input[type="number"]::-webkit-outer-spin-button, input[type="number"]::-webkit-inner-spin-button
	{
	-webkit-appearance: none;
	margin: 0;
}

#content-main {
	min-height: 770px;
}
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container mt-5 pt-5" id="content-main">
		<c:set var="phone" value="${fn:split(loginMember.memberPhone,'-') }"/>
		<c:set var="address" value="${fn:split(loginMember.memberAddress,',') }"/>

		<div class="row">
			<div class="col-sm-9">
				<h1>My Page</h1>
				<hr>
				<div class="bg-white rounded shadow-sm container p-3">
					<form method="POST" action="updateAction" name="updateForm" onsubmit="return updateValidate();" class="form-horizontal" role="form">
						<!-- 아이디 -->
						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<h6>아이디</h6>
							</div>
							<div class="col-md-6">
								<h5 id="id">${loginMember.memberId }</h5>
							</div>
						</div>

						<!-- 이름 -->
						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<h6>이름</h6>
							</div>
							<div class="col-md-6">
								<h5 id="name">${loginMember.memberName }</h5>
							</div>
						</div>

						<!-- 전화번호 -->
						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<label for="phone1">전화번호</label>
							</div>

							<!-- 전화번호1 -->
							<div class="col-md-3">
								<select class="custom-select" id="phone1" name="phone1">
									<option <c:if test="${phone[0] == '010'}"> selected </c:if> >010</option>
									<option <c:if test="${phone[0] == '011'}"> selected </c:if> >011</option>
									<option <c:if test="${phone[0] == '016'}"> selected </c:if> >016</option>
									<option <c:if test="${phone[0] == '017'}"> selected </c:if> >017</option>
									<option <c:if test="${phone[0] == '019'}"> selected </c:if> >019</option>
								</select>
							</div>


							<!-- 전화번호2 -->
							<div class="col-md-3">
								<input type="number" class="form-control phone" id="phone2" name="phone2" maxlength="4" value="${phone[1]}">
							</div>

							<!-- 전화번호3 -->
							<div class="col-md-3">
								<input type="number" class="form-control phone" id="phone3" name="phone3" maxlength="4" value="${phone[2]}">
							</div>
						</div>

						<!-- 이메일 -->
						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<label for="memberEmail">Email</label>
							</div>
							<div class="col-md-6">
								<input type="email" class="form-control" id="email" name="memberEmail" value="${ loginMember.memberEmail }">
							</div>
						</div>
						<br>

						<!-- 주소 -->
						<!-- 오픈소스 도로명 주소 API -->
						<!-- https://www.poesis.org/postcodify/ -->
						<div class="row mb-3 form-row">

							<div class="col-md-3">
								<label for="postcodify_search_button">우편번호</label>
							</div>
							<div class="col-md-3">
								<input type="text" name="post" id="post" class="form-control postcodify_postcode5" value="${address[0] }">
							</div>
							<div class="col-md-3">
								<button type="button" class="btn btn-success" id="postcodify_search_button">검색</button>
							</div>
						</div>

						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<label for="address1">도로명 주소</label>
							</div>
							<div class="col-md-9">
								<input type="text" class="form-control postcodify_address" name="address1" id="address1" value="${address[1] }">
							</div>
						</div>

						<div class="row mb-3 form-row">
							<div class="col-md-3">
								<label for="address2">상세주소</label>
							</div>
							<div class="col-md-9">
								<input type="text" class="form-control postcodify_details" name="address2" id="address2" value="${address[2] }">
							</div>
						</div>


						<!-- 관심분야 -->
						<hr class="mb-4">
						<div class="row">
							<div class="col-md-3">
								<label>관심 분야</label>
							</div>
							<div class="col-md-9 custom-control custom-checkbox">
								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="sports" value="운동" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="sports">운동</label>
								</div>
								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="movie" value="영화" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="movie">영화</label>
								</div>
								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="music" value="음악" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="music">음악</label>
								</div>
								<br>
								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="cooking" value="요리" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="cooking">요리</label>
								</div>
								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="game" value="게임" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="game">게임</label>
								</div>

								<div class="form-check form-check-inline">
									<input type="checkbox" name="memberInterest" id="etc" value="기타" class="form-check-input custom-control-input"> <label class="form-check-label custom-control-label" for="etc">기타</label>
								</div>
							</div>



						</div>

						<hr class="mb-4">
						<button class="btn btn-success btn-lg btn-block" type="submit">수정</button>
					</form>
				</div>
			</div>

			<jsp:include page="sideMenu.jsp" />
		</div>
	</div>
	<jsp:include page="../common/footer.jsp" />

	<!-- 도로명 주소 API -->
	<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
	<script>
	
		// 도로명 주소 API
		// 검색 단추를 누르면 팝업 레이어가 열리도록 설정한다.
		$(function() {
			$("#postcodify_search_button")
					.postcodifyPopUp();
		});
		
		// 전화번호 숫자 4글자 이상 작성 방지
		$(".phone").on("input", function() {
			if ($(this).val().length > $(this).prop("maxLength")) {
				$(this).val($(this).val().slice(0, $(this).prop("maxLength")));
			}
		});
		
		

		// 각 유효성 검사 결과를 저장할 객체
		var validateCheck = {
			"phone2" : false,
			"email" : false
		};

		
		// submit 동작
		function updateValidate() {
			
			var $phone2 = $("#phone2");
			var $phone3 = $("#phone3");
			var $email = $("#email");

			// 전화번호 유효성 검사
			var regExp1 = /^\d{3,4}$/; // 숫자 3~4 글자
			var regExp2 = /^\d{4,4}$/; // 숫자 4 글자

			if (!regExp1.test($phone2.val()) || !regExp2.test($phone3.val())) {
				validateCheck.phone2 = false;
			} else {
				validateCheck.phone2 = true;
			}

			// 이메일 유효성 검사
			var regExp3 = /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;

			if (!regExp3.test($email.val())) {
				validateCheck.email = false;
			} else {
				validateCheck.email = true;
			}

			for ( var key in validateCheck) {
				if (!validateCheck[key]) {
					var str;
					switch (key) {
					case "phone2": str = "전화번호"; break;
					case "email": str = "이메일";	break;
					}

					swal({icon : "warning",	title : str + " 형식이 유효하지 않습니다."})
					.then(function() {
						var id = "#" + key;
						$(id).focus();
					});

					return false;
				}
			}

			$memberPhone = $("<input>", {
								type : "hidden",
								name : "memberPhone",
								value : $("#phone1").val() + "-" + $("#phone2").val() + "-" + $("#phone3").val()
							});

			$memberAddress = $("<input>", {
								type : "hidden",
								name : "memberAddress",
								value : $("#post").val() + "," + $("#address1").val() + "," + $("#address2").val()
							});

			$("form[name='updateForm']").append($memberPhone).append($memberAddress);
			
		}
		
		
		
		
		//******************** 관심 분야 체크  ***************************
			// 관심 분야 중 회원정보와 일치하는 부분 체크하기
		(function(){
			
			// 회원정보에서 관심분야 문자열을 얻어와 ','를 구분자로하여 분리하기
			var interest = "${loginMember.memberInterest}".split(",");
			// 체크박스 요소들 모두 선택하여 반복 접근
			$("input[name='memberInterest']").each(function(index, item){
				
				// interest 배열 내에 
				// 현재 접근중인 체크박스 value와 일치하는 요소가 있는지 확인
				// indexOf 일치하면 0 반환 일치하지 않으면 -1 반환하는 속성을 이용해서 != -1 조건을 주어 중복을 제거 한다.
				if(interest.indexOf($(item).val()) != -1){
					$(item).prop("checked",true);
				}
			});
			
			
		})();
			
		
		//******************** 관심 분야 체크  ***************************
	</script>

</body>
</html>
