package com.kh.spring.board.model.service;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dao.BoardDAO;
import com.kh.spring.board.model.exception.InsertAttachmentFailException;
import com.kh.spring.board.model.exception.UpdateAttachmentFailException;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;

@Service // Service임을 알려줌 + bean 등록 
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardDAO dao;

	@Override
	public PageInfo getPageInfo(int type, int cp) {
//		전체 게시글 수 조회
		int listCount = dao.getListCount(type);
		
		return new PageInfo(cp, listCount, type);
	}

	
//	게시글 목록 조회 Service 구현
	@Override
	public List<Board> selectList(PageInfo pInfo) {
		
		return dao.selectList(pInfo);
	}
	
//	썸네일 목록 조회 Service 구현
	@Override
	public List<Attachment> selectThumbnailList(List<Board> bList) {
		
		return dao.selectThumbnailList(bList);
	}


	
	

//	게시글 상세 조회 Service 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Board selectBoard(int boardNo, int type) {
//		1. 게시글 상세 조회
		Board temp = new Board();
		temp.setBoardNo(boardNo);
		temp.setBoardCode(type);
		
		Board board = dao.selectBoard(temp);
		
//		2. 상세 조회 성공 시 조회 수 증가
		if(board != null) {
			int result = dao.increaseReadCount(boardNo);
			
			if(result > 0) { // DB 조회수 증가 성공 시 
//				먼저 조회된 board에 조회 수 1 증가
				board.setReadCount(board.getReadCount() + 1);
			}
		}
		
		return board;
	} // selectBoard method end
	
	
//	게시글에 포함된 이미지 목록 조회 Service 구현 
	@Override
	public List<Attachment> selectAttachmentList(int boardNo) {
		
		return dao.selectAttachmentList(boardNo);
		
	}

	

//	서비스에서 트랜잭션 처리를 하는 이유 ? 
//	- 서비스의 목적이 비즈니스 로직 처리  == 데이터 가공이나 필요한 여러 DAO 메소드를 호출하는 역할
//	-> 수행된 여러 DAO 구문의 트랜잭션 내용을 한번에 DB에 Commit 또는 Rollback하기 위해 
//		트랜잭션 처리는 Service에서 수행된다 . 
	
//	게시글 삽입(+ 파일업로드) Serivce 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Map<String, Object> map, List<MultipartFile> images, String savePath) {
		int result = 0; // 최종 결과 저장 변수 선언 
		
//		1. 게시글 번호 얻어오기 -> SEQ_BNO.NEXTVAL를 통해 얻어온다.
		int boardNo = dao.selectNextNo();

//		2. 게시글 삽입 
		if(boardNo > 0) { // 다음 게시글 번호를 얻어온 경우 
			map.put("boardNo",boardNo); // map에 boardNo 추가 
			
//			크로스 사이트 스크립팅 방지 처리 
//			추후 summernote api 사용을 염두하여 게시판 타입별로 
//			크로스 사이트 스크립팅 방지 처리를 선택적으로 진행
			
			if((int)map.get("boardType") == 1) { // 자유 게시판
				
				String boardTitle = (String)map.get("boardTitle"); 
				String boardContent = (String)map.get("boardContent");
				
//				크로스 사이트 스크립팅 방지 처리 적용 
				boardTitle = replaceParameter(boardTitle);
				boardContent = replaceParameter(boardContent);
				
//				처리된 문자열을 다시 map에 세팅
				map.put("boardTitle" , boardTitle);
				map.put("boardContent" , boardContent);
				
//				개행문자 처리 -> 화면에서 JSTL을 이용해서 처리할 예정
			}
			
//			게시글 삽입 DAO 메소드 호출 
			result = dao.insertBoard(map);
			
//			3. 게시글 삽입 성공 시 이미지 정보 삽입
			if(result > 0) {
				
//				이미지 정보를 Attachment 객체에 저장하여 List에 추가 
				List<Attachment> uploadImages = new ArrayList<Attachment>();
				
//				images.get(i).getOriginalName() -> 업로드된 파일의 원본 파일명 
//				--> 중복 상황을 대비하여 파일명을 변경하는 코드가 필요하다. (rename() 메소드)
				
//				DB에 저장할 웹상 접근 주소 ( filePath)
				String filePath = null;
						
				if((int)map.get("boardType") == 1) {
					
					filePath = "/resources/uploadImages";
					
				}else {
					
					filePath = "/resources/infoImages";
				}
				
//				for문을 이용하여 파일정보가 담긴 images를 반복접근 
//				-> 업로드된 파일이 있을 경우에만 uploadImages 리스트에 추가
//				향상된 포문을 안쓰는 이유는 i 값을 이용해서 파일레벨을 지정하기 위해서 i == 인덱스 == fileLevel과 같은값
				for(int i=0; i<images.size(); i++) {
//					현재 접근한 images의 요소(MultipartFile)에 업로드된 파일이 있는지 확인
					if(!images.get(i).getOriginalFilename().equals("")) { // 빈문자열이 아니라면 == 파일이 업로드 되었다면 
//						파일이 업로드 된 경우 == 업로드된 원본 파일명이 있는 경우
						
//						원본 파일명 변경 
						String fileName = rename(images.get(i).getOriginalFilename());
						
//						Attachment 객체 생성
						Attachment at = new Attachment(filePath, fileName, i, boardNo);
						
//						리스트에 추가
						uploadImages.add(at); 
						
					}
				}
				
//				uploadImage 확인
//				for(Attachment at : uploadImages) {
//					System.out.println(at);
//				}
				
//				--------------------------- summernote -----------------------------
				
//				게시판 타입이 2번 (summernote를 이용한 게시글 작성 ) 일 경우 
//				boardContent 내부에 업로드된 이미지 정보 (filePath, fileName)이 들어있다.
//				-> boardContent에서 <img> 태그만을 골라내어 img 태그의 src 속성 값을 추출 후 filePath, fileName을 얻어낸다.
				if((int)map.get("boardType") == 2) {
					Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");//img 태그 src 추출 정규표현식
					
					// SummerNote에 작성된 내용 중 img태그의 src속성의 값을 검사하여 매칭되는 값을 Matcher객체에 저장함.
					Matcher matcher = pattern.matcher((String)map.get("boardContent"));     
					 
					String fileName = null; // 파일명 변환 후 저장할 임시 참조 변수
					String src = null; // src 속성값을 저장할 임시 참조 변수
					
					// matcher.find() : Matcher 객체에 저장된 값(검사를 통해 매칭된 src 속성 값)에 반복 접근하여 값이 있을 경우 true 
					while(matcher.find()){
						src=  matcher.group(1); // 매칭된 src 속성값을  Matcher 객체에서 꺼내서 src에 저장 
						
						System.out.println(src);
						
						
						filePath = src.substring(src.indexOf("/", 2), src.lastIndexOf("/")); // 파일명을 제외한 경로만 별도로 저장.
						// /resources/infoImages/ 		두번째 / 부터 마지막 / 까지
						
						fileName = src.substring(src.lastIndexOf("/")+ 1); // 업로드된 파일명만 잘라서 별도로 저장.
						// 2102121212121_2156.jpg
						
						// Attachment 객체를 이용하여 DB에 파일 정보를 저장
						Attachment at = new Attachment(filePath, fileName, 1, boardNo);
						uploadImages.add(at);
					}
				}
				
//				--------------------------- summernote -----------------------------
				
				
/*				images -> 실제로 저장해야 될 파일을 가지고 있는 리스트
				uploadImages -> 데이터 베이스에 들어가야되는 정보를 가지고 있는 리스트
				
				uploadImages 만들 때 images의 인덱스로 파일 레벨을 만듬
				-> 반대로 생각하면 uploadImages의 파일레벨로 images의 인덱스를 알아낼 수 있음
				
				파일을 저장할때 반복문을 써야하니까 images의 인덱스가 필요한데 0부터 1씩 늘어나는 정수로는 해결 불가능
				그래서 uploadImages에 있는 파일레벨 == images의 인덱스니까 파일레벨로 인덱스를 찾고
				경로 + 파일 이름으로 transferTo 메소드를 이용해서 실제로 서버에 파일을 저장
 
 * */				
				
				if(!uploadImages.isEmpty()) { // 업로드된 이미지가 있을경우
					
//					파일 정보 삽입 DAO 호출 
					result = dao.insertAttachmentList(uploadImages);
//					result == 삽입된 행의 개수 
					
//					모든 데이터가 정상 삽입 되었을 경우 -> 서버에 파일 저장 
					if(result == uploadImages.size()) { 
						result = boardNo; // result에 boardNo저장
						
//						MultipartFile.transferTo()
//						-> MutipartFile 객체에 저장된 파일을 지정된 경로에 실제 파일의 형태로 변환하여 저장하는 메소드 
//						uploadImages 리스트 안에 Attachment들이 담겨있다.
//						Attachment안에 파일 레벨이 정해져있다.
						
						int size = 0;
						
						if((int)map.get("boardType") == 1) {
							size = uploadImages.size();
						}else if(!images.get(0).getOriginalFilename().equals("")){
//							이미지의 0번 인덱스가 비어있지 않을때
							size = images.size();
						}
						
						
						for(int i=0; i < size; i++) {
//							uploadImages : 업로드된 이미지 정보를 담고있는 Attachment가 모여있는 List
//							images : <input type="file"> 태그의 정보를 담은 MultipartFile이 모여있는 List
							
//							uploadImages를 만들 때 각 요소의 파일 레벨은 images의 index를 이용하여 부여한다.
//							파일 레벨은 인덱스로 만들었다 인덱스를 알면 파일 레벨을 알 수 있다 .
						
							try {
								
//								images의 i번째 요소를 꺼내와서 
								images.get(uploadImages.get(i).getFileLevel())
									.transferTo(new File(savePath + "/" + uploadImages.get(i).getFileName()));
								
							}catch (Exception e) {
								e.printStackTrace();
								
//								transferTo()는 IOException을 발생 시킨다. (CheckedException 이므로 try-catch문 필요)
//								-> 어쩔수 없이 try-catch 작성 
//								--> 예외가 처리되어버리면 @Transactional이 정상적으로 작동하지 못하는 문제가 발생한다.
//								@Transactional : 예외가 발생하면 롤백처리를 하는 어노테이션 
//								---> 꼭 예외처리를 하지 않아도 되는 UncheckedException을 강제로 발생시켜 
//													@Transactional이 예외가 발생했음을 감지하게 만든다.
//								* 상황에 맞는 사용자 정의 예외를 작성 
								throw new InsertAttachmentFailException("파일 서버 저장 실패");
							}
						}
						
					}else { // 파일 정보를 DB에 삽입하는데 실패 했을 때
						throw new InsertAttachmentFailException("파일 정보 DB 삽입 실패");
					}
					
				} else { // 업로드된 이미지가 없을 경우 
					result = boardNo;
				}
			}
		}


		return result;
	}
	
	
//  크로스 사이트 스크립트 방지 처리 메소드
	private String replaceParameter(String param) {
		String result = param;
		if (param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}

		return result;
	}

// 	파일명 변경 메소드
	public String rename(String originFileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new java.util.Date(System.currentTimeMillis()));

		int ranNum = (int) (Math.random() * 100000); // 5자리 랜덤 숫자 생성

		String str = "_" + String.format("%05d", ranNum);
		// String.format : 문자열을 지정된 패턴의 형식으로 변경하는 메소드
		// %05d : 오른쪽 정렬된 십진 정수(d) 5자리(5)형태로 변경. 빈자리는 0으로 채움(0)
		String ext = originFileName.substring(originFileName.lastIndexOf("."));

		return date + str + ext;
	}


//	게시글 수정 Service 구현 
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBoard(Board updateBoard, List<MultipartFile> images, String savePath, boolean[] deleteImages) {
		
//		1. 게시글 수정 
//		제목 , 내용 크로스사이트 스크립팅 방지 처리 
		updateBoard.setBoardTitle(replaceParameter(updateBoard.getBoardTitle()));
		updateBoard.setBoardContent(replaceParameter(updateBoard.getBoardContent()));
		
//		게시글 수정 DAO 호출 
		int result = dao.updateBoard(updateBoard);
//		2. 이미지 수정 
		if(result > 0) {
			
//			수정 전 업로드 되어있던 파일 정보를 얻어온다.
//			-> 새롭게 삽입 또는 수정되는 파일과 비교하기 위함이다.
			List<Attachment> oldFiles = dao.selectAttachmentList(updateBoard.getBoardNo());
//			새로 업로드된 파일 정보를 담을 리스트
			List<Attachment> uploadImages = new ArrayList<Attachment>();
			
//			삭제 되어야할 파일 정보를 담을 리스트
			List<Attachment> removeFileList = new ArrayList<Attachment>();
			
//			DB에 저장할 웹상 이미지 접근 경로 
			String filePath = "/resources/uploadImages";

//			새롭게 업로드된 파일 정보를 가지고 있는 images에 반복 접근 
			for(int i=0; i<images.size(); i++) {
				
				
//				업로드된 이미지가 있을 경우 
				if(!images.get(i).getOriginalFilename().equals("")) {
					
//					파일명 변경
					String fileName = rename(images.get(i).getOriginalFilename());
					
//					Attachment 객체 생성
					Attachment at = new Attachment(filePath, fileName, i, updateBoard.getBoardNo());
					
					uploadImages.add(at); // 업로드 이미지 리스트에 추가 
					
//					true : update 진행
//					false : insert 진행 
					boolean flag = false;
					
//					새로운 파일 정보와 이전 파일 정보를 비교하는 반복문 
					for(Attachment old : oldFiles) {
						
						if(old.getFileLevel() == i) {
//							현재 접근한 이전 파일의 레벨이 새롭게 업로드한 파일의 레발과 같은 경우 
//							새롭게 업로드한 파일의 레벨과 같은 경우 
//							== 같은 레벨에 새로운 이미지 업로드 --> update 진행 
							flag = true;
							
//							DB에서 파일 번호가 일치하는 행의 내용을 수정하기 위해 파일번호를 얻어온다.
							at.setFileNo(old.getFileNo());
							
							removeFileList.add(old); // 삭제할 파일 목록에 이전 파일 정보 추가 
						}
					}
					
//					flag 값에 따른 insert / update 제어 
					if(flag) { // true : update 진행
						
						result = dao.updateAttachment(at);
						
					}else {
						
						result = dao.insertAttachment(at);
						
					}
					
//					insert 또는 update 싪채시 rollback 수행
//					-> 예외를 발생 시켜서 @Transactional을 이용해 수행 
					if(result <= 0) {
						throw new UpdateAttachmentFailException("파일 정보 수정 실패");
					}
					
				}else { // 업로드된 이미지가 없을 경우
//					deleteIamges 배열 : 화면에서 X버튼을 클릭해서 삭제한 배열 인덱스를 표시하는 역할
//					-> 배열 요소 중 true가 되어있는 부분은 해당 인덱스 (== 파일레벨)에 있던 이미지가 삭제되었다는 의미
//					--> DB에서 해당 파일 정보를 삭제 
					if(deleteImages[i]) { // t / f 결과값 
						
						
//						X 버튼으로 삭제가 되었다고 deleteImages에 true로 저장되어 있지만 , 
//						혹시라도 이미지가 없는데 X버튼을 누른걸 수 도 있으니 진짜 이전 이미지가 있었는지 검사
						for(Attachment old : oldFiles) {
							
							if(old.getFileLevel() == i) {
								
								result = dao.deleteAttachment(old.getFileNo());
								
								if(result > 0) { // 삭제 성공 
//									removeFileList : 서버에서 삭제할 파일 정보를 모아둔 리스트
									removeFileList.add(old); // 서버 파일 삭제 리스트에 추가 
								} else { // 삭제 실패
									throw new UpdateAttachmentFailException("파일 정보 삭제 실패");
									
								}
							}
						}
					}
					
					
				}
				
			}// images 반복 접근 for 문 종료 
			
//			uploadImages == 업로드된 파일 정보 --> 서버에 파일 저장
//			removeFileList == 제거하야될 파일 정보 --> 서버에 파일 삭제 
//			수정되거나 새롭게 삽입된 이미지를 서버에 저장하기 위해 transferTo() 수행
			/*
			 * if (result > 0) { for (int i = 0; i < images.size(); i++) { if
			 * (!images.get(i).getOriginalFilename().equals("")) { try {
			 * images.get(i).transferTo(new File(savePath + "/" +
			 * uploadImages.get(i).getFileName())); } catch (Exception e) {
			 * 
			 * e.printStackTrace(); throw new UpdateAttachmentFailException("서버 파일 저장 실패");
			 * } } } }
			 */
			
			if (result > 0) {
				for (int i = 0; i < uploadImages.size(); i++) {

					try {
						images.get(uploadImages.get(i).getFileLevel())
								.transferTo(new File(savePath + "/" + uploadImages.get(i).getFileName()));
					} catch (Exception e) {
						e.printStackTrace();
						throw new UpdateAttachmentFailException("파일 정보 수정 실패");
					}
				}
			}

//			--------------------------------------------------------------------
//			이전 파일 서버에서 삭제하는 코드
			for (Attachment removeFile : removeFileList) {
				File tmp = new File(savePath + "/" + removeFile.getFileName());
				tmp.delete();
			}
//			--------------------------------------------------------------------

		}
		return result;
	}


//	summernote 업로드 이미지 저장 Service
	@Override
	public Attachment insertImage(MultipartFile uploadFile, String savePath) {
		
//		파일이름이 중복되지 않게 파일 명을 바꿔주는 작업
		String fileName = rename(uploadFile.getOriginalFilename());
		
//		웹상 접근 주소 
		String filePath = "/resources/infoImages";
		
//		돌려 보내줄 파일 정보를 Attachment 객체에 담아서 전달
		Attachment at = new Attachment();
		at.setFilePath(filePath);
		at.setFileName(fileName);

//		서버에 파일 저장(transferTo())
		
		try {
			
			uploadFile.transferTo(new File(savePath + "/" + fileName));
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			throw new InsertAttachmentFailException("summernote 파일 업로드 실패 ");
		}
		
		
		
		return at;
	}


//	DB에 저장된 최근 3일 제외 파일 정보 조회 Service 구현 
	@Override
	public List<String> selectDBFileList() {
		
		return dao.selectDBFileList();
	}



	
	
	
	
	
	
	
	
}
