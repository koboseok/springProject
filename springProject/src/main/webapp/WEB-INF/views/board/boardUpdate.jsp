<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>	

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<style>
    .insert-label {
      display: inline-block;
      width: 80px;
      line-height: 40px
    }
    
    #content-main{ margin: 100px auto;}
    
    .deleteImg{
	    position: absolute;
	    display : inline-block;
	    margin-left: -15px;
	    border: none;
	    background-color: rgba(1,1,1,0);
	    width: 20px;
	    height: 20px;
	    cursor: pointer;
    }
</style>
</head>
<body>
	<div class="container">
		<jsp:include page="../common/header.jsp"/>

		<div class="container pb-5 mb-5" id="content-main">

			<h3>게시글 수정</h3>
			<hr>
			<form action="updateAction" method="post" enctype="multipart/form-data" name="updateForm" role="form" onsubmit="return validate();">
				<div class="mb-2">
					<label class="input-group-addon mr-3 insert-label">카테고리</label> 
					<select	class="custom-select" id="category" name="categoryName" style="width: 150px;">
						<option value="10">운동</option>
						<option value="20">영화</option>
						<option value="30">음악</option>
						<option value="40">요리</option>
						<option value="50">게임</option>
						<option value="60">기타</option>
					</select>
				</div>
				
				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">제목</label> 
					<input type="text" class="form-control" id="title" name="boardTitle" size="70"
						value="${board.boardTitle }">
				</div>

				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">작성자</label>
					<h5 class="my-0" id="writer">${loginMember.memberId}</h5>
				</div>


				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">작성일</label>
					<h5 class="my-0" id="today">
						<jsp:useBean id="now" class="java.util.Date" />
						<fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/>
					</h5>
				</div>

				<hr>
				
				
				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">썸네일</label>
					<div class="mx-2 boardImg" id="titleImgArea">
						<img id="titleImg" width="200" height="200"> 
						<span class="deleteImg">x</span>
					</div>
				</div>
	
				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">업로드<br>이미지</label>
					<div class="mx-2 boardImg" id="contentImgArea1">
						<img id="contentImg1" width="150" height="150">
						<span class="deleteImg">x</span>
					</div>
	
					<div class="mx-2 boardImg" id="contentImgArea2">
						<img id="contentImg2" width="150" height="150">
						<span class="deleteImg">x</span>
					</div>
	
					<div class="mx-2 boardImg" id="contentImgArea3">
						<img id="contentImg3" width="150" height="150">
						<span class="deleteImg">x</span>
					</div>
					
				</div>
				

				<!-- 파일 업로드 하는 부분 -->
				<div id="fileArea">
					<input type="file" id="img0" name="images" onchange="LoadImg(this,0)"> 
					<input type="file" id="img1" name="images" onchange="LoadImg(this,1)">
					<input type="file" id="img2" name="images" onchange="LoadImg(this,2)"> 
					<input type="file" id="img3" name="images" onchange="LoadImg(this,3)">
				</div>

				<div class="form-group">
					<div>
						<label for="content">내용</label>
					</div>
					<textarea class="form-control" id="content" name="boardContent"
						rows="10" style="resize: none;">${board.boardContent }</textarea>
				</div>


				<hr class="mb-4">

				<div class="text-center">
					<button type="submit" class="btn btn-primary">수정</button>
					<a class="btn btn-primary float-right" href="${header.referer}">목록으로</a>
				</div>

			</form>
		</div>

	</div>
	<jsp:include page="../common/footer.jsp"/>


	<script>
	
	// 게시글에 업로드 된 이미지 삭제
	var deleteImages = [];
	// 배열을 생성하여 이미지 삭제 버튼 수 만큼 배열에 false요소를 추가 
	// -> 배열에 4개 false가 추가됨 == 인덱스는 0 ~ 3 == fileLevel과 같음 
	// --> 이미지 삭제 버튼이 클릭 될 경우 해당 fileLevel과 같은 인덱스 값을 true로 변경  
	// ---> 해당 이미지가 삭제 되었음을 전달하기 위한 용도로 사용할 예정
	
	
	// deleteImages 배열에 false 4개 추가 
	for(var i=0; i<$(".deleteImg").length; i++){
		deleteImages.push(false);
	}
	
	
	
		// 카테고리 선택
		$.each($("#category > option"), function(index, item){
			if($(item).text() == "${board.categoryName}"){
				$(item).prop("selected", "true");
			}
		});
		
		// 이미지 배치
		<c:forEach var="at" items="${attachmentList}">
			$(".boardImg").eq(${at.fileLevel}).children("img").attr("src", "${contextPath}${at.filePath}/${at.fileName}");
		</c:forEach>
	
	
		// 이미지 영역을 클릭할 때 파일 첨부 창이 뜨도록 설정하는 함수
		$(function(){
			$("#fileArea").hide(); // #fileArea 요소를 숨김.		
			
			$(".boardImg").on("click", function(){ // 이미지 영역이 클릭 되었을 때
				var index = $(".boardImg").index(this);// 클릭한 이미지 영역 인덱스 얻어오기
				$("#img" + index).click(); // 클릭된 영역 인덱스에 맞는 input file 태그 클릭
			});
			
		});
		 
		
		 // 각각의 영역에 파일을 첨부 했을 경우 미리 보기가 가능하도록 하는 함수
		 function LoadImg(value, num) {
		  
			if(value.files && value.files[0]){ // 해당 요소에 업로드된 파일이 있을 경우
				
				var reader = new FileReader();
		    	reader.readAsDataURL(value.files[0]);
		    	
		    	reader.onload = function(e){ // 파일 업로드 성공 시 동작
					$(".boardImg").eq(num).children("img").attr("src", e.target.result);
		    	
		    	// 특정 fileLevel에 이미지가 업로드된 경우
		    	// == deleteImages 배열에서 해당 fileLevel과 일치하는 인덱스의 값을 
		    	// false로 바꿔 삭제되지 않음을 알려준다. 
					
		    	deleteImages[num] = false;
					
					
		    	}
			}
		}
		
	  
		// 유효성 검사
		function validate() {
			if ($("#title").val().trim().length == 0) {
				alert("제목을 입력해 주세요.");
				$("#title").focus();
				return false;
			}

			if ($("#content").val().trim().length == 0) {
				alert("내용을 입력해 주세요.");
				$("#content").focus();
				return false;
			}
			
			// 유효성 검사에서 문제가 없을 경우
			// 유효성 검사에서 문제가 없을 경우 서버에 제출 전
			// deleteImages배열의 내용을 hidden 타입으로 하여 form태그 마지막에 추가하여 파라미터로 전달
			for(var i=0 ; i<deleteImages.length ; i++){
				$deleteImages = $("<input>", {type : "hidden", name : "deleteImages", value : deleteImages[i]});
				$("form[name=updateForm]").append($deleteImages);
			}

		}
		
			
		
		// 이미지 삭제 버튼 동작
    $(".deleteImg").on("click", function(event){
         // event : 현재 발생한 이벤트에 대한 정보가 담긴 객체
         event.stopPropagation(); // 이벤트가 연달아 실행되는 것을 방지

         // 기존 img 태그를 삭제하고 새로운 img 태그를 만들어서 제자리에 추가 

         // 기존 img 태그
         var $beforeImg = $(this).prev(); // 이벤트가 발생한 요소의 이전 요소 선택

         // 기존 정보를 토대로 새로운 img 태그 생성
         var $newImg = $("<img>", { id : $beforeImg.attr("id"), 
                                                              width : $beforeImg.css("width"),
                                                              height : $beforeImg.css("height")    });

         $(this).prev().remove(); // 기존 img 태그 삭제 / prev (선택)
         $(this).before($newImg); // 새로운 img 태그 추가 / before (추가)

				// 특정 fileLevel의 요소가 삭제 되었음을 알리기 위해 deleteImages에 기록 
				// == 클릭된 삭제 버튼 인덱스와 일치하는 deleteImages 인덱스 값을 true로 변경
				
				// $(".dleteImg").index(this) : deleteImg 클래스 중 현재 클릭된 버튼의 인덱스를 반환 
				deleteImages[ $(".deleteImg").index(this)] = true;
				console.log(deleteImages);
				
				// 삭제 버튼이 클릭된 경우
				// 해당 이미지와 연결된 input type="file" 태그의 값을 없앤다.
				$("#img" + ( $(".deleteImg").index(this) )).val("");
			
     });
			

			
		// });
		
		
		
		
		
		
		
	
		
		
	</script>
</body>
</html>
