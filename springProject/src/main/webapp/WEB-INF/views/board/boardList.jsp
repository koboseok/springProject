<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>

<!-- cuba -->
	
		
<style>
.pagination {
	justify-content: center;
}

#searchForm {
	position: relative;
}

#searchForm>* {
	top: 0;
}

.boardTitle>img {
	width: 50px;
	height: 50px;
}

.board-list {
	margin: 100px auto;
}

/* 세로 가운데 정렬*/
#list-table td {
	vertical-align: middle;
	/* vertical-align : inline, inline-block 요소에만 적용 가능(td는 inline-block)*/
}

.list-wrapper {
	min-height: 540px;
}

#list-table td:hover {
	cursor: pointer;
}
</style>

</head>
<body>
	<jsp:include page="../common/header.jsp" />

	<div class="container board-list">
		<!-- 게시판 명 얻어오기 -->
		<h1>
		<%-- 	${bList[0].boardName } --%>
			<c:choose>
				<c:when test="${pInfo.boardType == 1 }">자유게시판	</c:when>
				<c:when test="${pInfo.boardType == 2 }">정보게시판	</c:when>
			</c:choose>
			
		</h1>
		<br>
		<div>
			<table class="table table-hover table-striped" id="list-table">
				<thead>
					<tr>
						<th>글번호</th>
						<th>카테고리</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty bList }">
						<tr>
							<td colspan="6">존재하는 게시글이 없습니다.</td>
						</tr>
					</c:if>

					<c:if test="${!empty bList }">
						<c:forEach var="board" items="${bList}" varStatus="vs">

							<tr>
								<td>${board.boardNo}</td>
								<td>${board.categoryName}</td>
								<td class="boardTitle">
									<!----------------- 썸네일 부분 -----------------> 
									<c:forEach items="${thList}" var="th">
										<c:if test="${th.parentBoardNo  == board.boardNo}">
											<img src="${contextPath}${th.filePath}/${th.fileName}">
													<%--/spring/resources/uploadImages/123123.jpg --%>
										</c:if>
									</c:forEach>
									${board.boardTitle}
								</td>

								<td>${board.memberId}</td>
								<td>${board.readCount}</td>
								<td>
									<%-- 날짜 출력 모양 지정 --%>
									<fmt:formatDate var="createDate" value="${board.boardCreateDate }" pattern="yyyy-MM-dd"/>
									<fmt:formatDate var="now" value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd"/> 
									<c:choose>
										<c:when test="${createDate != now}">
											${createDate }
										</c:when>
										<c:otherwise>
											<fmt:formatDate value="${board.boardCreateDate }" pattern="HH:mm"/>
										</c:otherwise>
									</c:choose>
								</td>

							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>

		<hr>

		<%-- 로그인이 되어있는 경우 --%>
		<c:if test="${!empty loginMember }">
			<a class="btn btn-success float-right" href="../${pInfo.boardType}/insert">글쓰기</a>
		</c:if>

		<!--------------------------------- pagination  ---------------------------------->

		<div class="my-4">
			<ul class="pagination">

				<%-- 주소 조합 작업 --%>
				<c:url var="pageUrl" value="${pInfo.boardType}?"/>

				<!-- 화살표에 들어갈 주소를 변수로 생성 -->
				<c:set var="firstPage" value="${pageUrl}cp=1"/>
				<c:set var="lastPage" value="${pageUrl}cp=${pInfo.maxPage}"/>
				
				<%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않는다--%>
				<%-- 
					<fmt:parseNumber>   : 숫자 형태를 지정하여 변수 선언 
					integerOnly="true"  : 정수로만 숫자 표현 (소수점 버림)
				--%>
				
				<fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / 10 }"  integerOnly="true" />
				<fmt:parseNumber var="prev" value="${ c1 * 10 }"  integerOnly="true" />
				<c:set var="prevPage" value="${pageUrl}cp=${prev}" />
				
				
				<fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / 10 }" integerOnly="true" />
				<fmt:parseNumber var="next" value="${ c2 * 10 + 1 }" integerOnly="true" />
				<c:set var="nextPage" value="${pageUrl}cp=${next}" />
				


				<c:if test="${pInfo.currentPage > pInfo.pageSize}">
					<li> <!-- 첫 페이지로 이동(<<) -->
						<a class="page-link" href="${firstPage}">&lt;&lt;</a>
					</li>
					
					<li> <!-- 이전 페이지로 이동 (<) -->
						<a class="page-link" href="${prevPage}">&lt;</a>
					</li>
				</c:if>



				<!-- 페이지 목록 -->
				<c:forEach var="page" begin="${pInfo.startPage}" end="${pInfo.endPage}" >
					<c:choose>
						<c:when test="${pInfo.currentPage == page }">
							<li>
								<a class="page-link">${page}</a>
							</li>
						</c:when>
					
						<c:otherwise>
							<li>	
								<a class="page-link" href="${pageUrl}cp=${page}">${page}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
					
		
				<%-- 다음 페이지가 마지막 페이지 이하인 경우 --%>
				<c:if test="${next <= pInfo.maxPage}">
					<li> <!-- 다음 페이지로 이동 (>) -->
						<a class="page-link" href="${nextPage}">&gt;</a>
					</li>
					
					<li> <!-- 마지막 페이지로 이동(>>) -->
						<a class="page-link" href="${lastPage}">&gt;&gt;</a>
					</li>
				</c:if>
			</ul>
		</div>

		<div>
			<div class="text-center" id="searchForm" style="margin-bottom: 100px;">
				<span> 카테고리(다중 선택 가능)<br> 
					<label for="exercise">운동</label> 
					<input type="checkbox" name="ct" value="운동" id="exercise"> &nbsp; 
					<label for="movie">영화</label> <input type="checkbox" name="ct" value="영화" id="movie"> &nbsp; 
					<label for="music">음악</label> <input type="checkbox" name="ct" value="음악" id="music"> &nbsp; 
					<label for="cooking">요리</label> <input type="checkbox" name="ct" value="요리" id="cooking"> &nbsp; 
					<label for="game">게임</label> <input type="checkbox" name="ct" value="게임" id="game"> &nbsp; 
					<label for="etc">기타</label> <input type="checkbox" name="ct" value="기타" id="etc"> &nbsp;
				</span> 
				<br> 
				<select name="sk" class="form-control" style="width: 100px; display: inline-block;">
					<option value="tit">글제목</option>
					<option value="con">내용</option>
					<option value="titcont">제목+내용</option>
				</select> 
				<input type="text" name="sv" class="form-control" style="width: 25%; display: inline-block;">
				<button class="form-control btn btn-success" id="searchBtn" type="button" style="width: 100px; display: inline-block;">검색</button>
			</div>
		</div>

	</div>
	<jsp:include page="../common/footer.jsp" />
	
	<%-- 목록으로 버튼에 사용할 URL 저장 변수 선언 --%>
	<c:set var="returnListURL" 
		value="${contextPath}/board/list/${pageUrl}cp=${pInfo.currentPage }" scope= "session"/>

	<script>
		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function(){
			
			var boardNo = $(this).parent().children().eq(0).text();
										// td     tr 				td   		첫번째(게시글 번호)
										
			// 게시글 상세조회 요청 주소 조합
			
			// 게시글 목록 : /spring/board/list/1
			// 상세조회 : /spring/board/1/500
			// 절대 경로
			//var boardViewURL = "${contextPath}/board/${pInfo.boardType}/" + boardNo;
												// 		spring /	board	/					1				/500
			
			// 상대경로 
			var boardViewURL = "../${pInfo.boardType}/" + boardNo;
			
			location.href = boardViewURL;
		});
		
	</script>
</body>
</html>
