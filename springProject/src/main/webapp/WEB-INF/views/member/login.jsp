<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}" scope="application" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="canonical" href="https://getbootstrap.com/docs/4.5/examples/floating-labels/">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<title>Spring Project</title>
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
</style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
<!-- Third party plugin JS-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
<!-- Custom styles for this template -->
<link href="${contextPath}/resources/css/floating-labels.css" rel="stylesheet">
<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>

	<c:if test="${!empty swalTitle}">
		<script>
			swal({
				icon : "${swalIcon}",
				title : "${swalTitle}",
				text : "${swalText}"
			});
		</script>
	</c:if>

	<div id="login-container">
		<div style="margin: auto; width: 200px; margin-bottom: 20px">
			<a class="navbar-brand js-scroll-trigger mx-0" href="${contextPath}"> 
				<img class="img-fluid img-profile rounded-circle mx-auto mb-2" src="https://github.com/Baek-dh/image_repository/blob/main/Spring_logo(green).png?raw=true" width="200px" height="200px" />
			</a>
		</div>

		<%-- 로그인 form --%>
		<form action="loginAction" method="post" class="form-signin">

			<!-- form-label-group : input태그와 연결된 label의 내용을 input의 placeholder처럼 보이게해주고, 
				값이 들어올 경우사라지지 않고 글씨가 작아진 상태로 위쪽으로 올라가게 하는 부트스트랩-->
			<div class="form-label-group">
				<input type="text" id="memberId" name="memberId" placeholder="ID" class="form-control" value="${cookie.saveId.value }" required autofocus> 
				<label for="memberId">ID</label>
			</div>

			<div class="form-label-group">
				<input type="password" id="memberPwd" name="memberPwd" placeholder="PASSWORD" class="form-control" required> 
				<label for="memberPwd">Password</label>
			</div>

			<div class="checkbox mb-3">
				<label> 
					<input type="checkbox" name="saveId"
						<c:if test="${!empty cookie.saveId.value }"> checked </c:if>
					> 아이디 저장
				</label>
			</div>
			<button class="btn btn-lg btn-success btn-block" type="submit">Login</button>
			<a class="btn btn-lg btn-success btn-block" href="${contextPath }/member2/signUp">Sign Up</a>
		</form>
	</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>