<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
	<title>Spring Project</title>
	
	
	<c:set var="contextPath" value="${pageContext.servletContext.contextPath}" scope="application"/>
	
	
	
	
    <!-- Font Awesome icons (free version)-->
    <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" crossorigin="anonymous"></script>
   
    <!-- Google fonts-->
    <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:500,700" rel="stylesheet" type="text/css" />
    <link href="https://fonts.googleapis.com/css?family=Muli:400,400i,800,800i" rel="stylesheet" type="text/css" />
    
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="${contextPath}/resources/css/resume-styles.css" rel="stylesheet" />
   	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
   	
	<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
  	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
   
   	<style>
	html{height: 100%;}
   	body {
	  margin: 0;
	  font-family: "Muli", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
	  font-size: 1rem;
	  font-weight: 400;
	  line-height: 1.5;
	  color: #212529;
	  text-align: left;
/* 	  background-color: #fff; */
	  min-height: 85%;
	  height: 100%;
	}
	
	h1, h2, h3, h4, h5, h6 {
	    text-transform: initial;
	}

	/* 추가부분 */	
	main, #content-main{
	   min-height: 100%;
	}
/* 	
	#sideNav .navbar-brand{
		margin : 10% auto 50px auto;
	} */
		
		
	/* 팝업 레이어 */
	#bell {
		cursor: pointer;
	}
	
	.popupLayer {
		position: absolute;
		display: none;
		background-color: #ffffff;
		border: solid 2px #d0d0d0;
		width: 250px;
		height: 130px;
		padding: 10px;
		overflow: auto;
		z-index : 10000;
		
	}
	
	#noticeInfo{
		font-size : 11px;
	}
	
	.deleteNotice{
		color : red;
		font-weight: bold;
		cursor: pointer;
	}
   	</style>
</head>
<body id="page-top">
	<c:if test="${!empty swalTitle}">
		<script>
			swal({icon : "${swalIcon}",
				 title : "${swalTitle}",
				 text : "${swalText}"});
		</script>
	<%-- 	<c:remove var="swalIcon"/>
		<c:remove var="swalTitle"/>
		<c:remove var="swalText"/> --%>
	</c:if>


	<!-- Navigation-->
    <nav class="navbar navbar-expand-lg navbar-dark bg-success fixed-top" id="sideNav">
        <a class="navbar-brand js-scroll-trigger" href="${contextPath }">
            <span class="d-block d-lg-none">Spring Project</span>
            <span class="d-none d-lg-block">
            	<%-- <img class="img-fluid img-profile rounded-circle mx-auto mb-2" src="${contextPath}/resources/images/logo-1.png" alt="" /> --%>
            	<img class="img-fluid img-profile rounded-circle mx-auto mb-2" src="https://github.com/Baek-dh/image_repository/blob/main/Spring_logo(green).png?raw=true">
            </span>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav">
            
            	<c:choose>
            		<%-- 로그인이 되어있지 않은 경우 --%>
            		<c:when test="${empty sessionScope.loginMember }">
		                <li class="nav-item"><a class="nav-link" href="${contextPath}/member/login">Login</a></li>
            		</c:when>
            		
            		<%-- 로그인이 되어있는 경우 --%>
            		<c:otherwise>
		                <li class="nav-item">
		                	<a class="nav-link" href="${contextPath}/member2/mypage" style="display:inline">${loginMember.memberName }</a>
		                	
		                </li>
		                <li class="nav-item"><a class="nav-link" href="${contextPath}/member/logout">Logout</a></li>
            		</c:otherwise>
            	</c:choose>
            	
                <li class="nav-item"><a class="nav-link" href="${contextPath}/board/list/1">Free Board</a></li>
                
                <!-- Summernote Text Editor API를 활용하는 게시판 -->
                <li class="nav-item"><a class="nav-link" href="${contextPath}/board/list/2">Information Board</a></li>
                <!-- Summernote Text Editor API를 활용하는 게시판 -->
                <li class="nav-item"><a class="nav-link" href="${contextPath}/board/list/3">Test Board</a></li>
            </ul>
        </div>
    </nav>
    
   	<!-- Bootstrap core JS-->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
	<!-- Third party plugin JS-->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
	<!-- Core theme JS-->
	<script src="${contextPath}/resources/js/resume-scripts.js"></script>
	
	
</body>
</html>