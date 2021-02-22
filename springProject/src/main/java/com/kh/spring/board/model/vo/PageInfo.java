package com.kh.spring.board.model.vo;

import org.springframework.stereotype.Component;

@Component
public class PageInfo {
	private int currentPage; 	// 현재 페이지 번호를 표시할 변수
	private int listCount; 		// 전체 게시글 수
	private int limit = 10; 			// 한 페이지에 보여질 게시글 수
	private int pageSize = 10; 	// 보여질 페이징바의 페이지 개수
	
	private int maxPage;	 	// 전체 페이지에서 가장 마지막 페이지
	private int startPage;	 	// 페이징바 시작 페이지 번호
	private int endPage; 	 	// 페이징바 끝 페이지 번호
	
	private int boardType; 		// 게시글 타입
	
	public PageInfo() {}
	
	public PageInfo(int currentPage, int listCount, int boardType) {
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.boardType = boardType;
		
		// 값을 입력 받은 후 계산 진행
		makePageInfo();
	}
	

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public PageInfo(int currentPage, int listCount, int limit, int pagingBarSize, int boardType) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.limit = limit;
		this.pageSize = pagingBarSize;
		this.boardType = boardType;
		
		makePageInfo();
	}


	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
		makePageInfo();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		makePageInfo();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pagingBarSize) {
		this.pageSize = pagingBarSize;
		makePageInfo();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	
	
	public int getMaxPage() {
		return maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	
	
	@Override
	public String toString() {
		return "PageInfo [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit
				+ ", pageSize=" + pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage
				+ ", endPage=" + endPage + ", boardType=" + boardType + "]";
	}

	// 페이징 처리에 필요한 값을 계산하는 메소드.
	private void makePageInfo() {
		
		// * maxPage - 총 페이지수 
		// 게시글의 개수가 100개일 경우 필요 페이지 수 : 10 페이지
		// 게시글의 개수가 101개일 경우 필요 페이지 수 : 11 페이지
		// 전체 게시글 수 / 한 페이지에 보여질 개수 결과를 올림 처리함.
		maxPage = (int)Math.ceil(( (double)listCount / limit));
		
		// * startPage - 페이징바 시작 페이지 번호
		//   아래쪽에 페이지 수가 10개씩 보여지게 할 경우
		//   1, 11, 21, 31, .....
		startPage = (currentPage-1)/pageSize * pageSize + 1;
		
		// * endPage - 페이징바 끝 페이지 번호
		//   아래쪽에 페이지 수가 10개씩 보여지게 할 경우
		//   10, 20, 30, 40, ..... 
		endPage = startPage + pageSize - 1;
		
		// 총 페이지의 수가endPage 보다 클 경우
		if(maxPage > endPage) {
			endPage = startPage + pageSize - 1;
		}
		else {
			// 총 페이지 수가 endPage보다 작을 경우
			// ex) maxPage가 13페이지고 endPage가 20페이지일 경우 13이 끝이여야 함.
			endPage = maxPage;
		}
	}
	
}
