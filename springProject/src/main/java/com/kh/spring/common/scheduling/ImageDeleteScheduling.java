package com.kh.spring.common.scheduling;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.spring.board.model.service.BoardService;

@Component
public class ImageDeleteScheduling {
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private BoardService boardService; // DB 이미지 정보 파일 리스트를 조회하기 위한 
	
//	@Scheduled(cron="0 0 * * * * ") // 매 시간마다 
	@Scheduled(cron="0 * * * * *") // 매 분마다 진행하는걸로 테스트
	public void deleteImage() {
//		최근 3일 제외 저장된 파일 중 서버에는 있지만 DB에는 존재하지 않는 이미지 파일을 매 시간마다 검사하여 삭제
		
//		1. 서버와 DB에 있는 파일 리스트를 조회 (최근 3일 이내 데이터만)
//		1) 서버 이미지 파일 리스트 조회
		
//		이미지 파일이 저장된 실제 경로를 얻어옴
		String savePath = servletContext.getRealPath("/resources/infoImages");
//		System.out.println(savePath);
		
//		해당 경로에 있는 모든 파일을 읽어 온다.
		File folder = new File(savePath);
		
		File[] fileList = folder.listFiles(); // savePath 폴더에 있는 모든 이미지 정보가 배열로 반환
		
//		fileList에서 최근 3일을 제외한 나머지 이미지 파일만 새로운 List에 저장 
		List<File> serverFileList = new ArrayList<File>();
		
		Date threeDaysAgo = new Date(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000));
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
		
		String standard = sdf.format(threeDaysAgo);
//		3일 전 날짜가 지정된 패턴  모양의 문자열 반환
		
		for(File file : fileList) {
			
//			fileList 배열에 반복 접근하여 파일명만 얻어오기
			String fileName = file.toString().substring(file.toString().lastIndexOf("\\") + 1);
//			System.out.println(file.toString());
//			System.out.println(fileName);
			
//			21021512				21021714 || 1546_91125.PNG
//			기존(3일 전 시간)보단 더 이전에 만들어진 파일 일 경우 
//			A.compareTo(B)
//			A가 B보다 크면 양수
//			A와 B가 같다면 0 
//			기준 > 비교 (1) / 기준==비교(0) / 기준< 비교 (-1)
			if( standard.compareTo(fileName.substring(0,8)) >= 0) {
//				서버 파일 리스트에 추가 
				serverFileList.add(file);
			}
			
		}
		
//		2) DB 이미지 정보 파일 리스트 조회 (최근 3일을 제외한 나머지 )
		List<String> dbFileList = boardService.selectDBFileList();
		
//		for(String fileName : dbFileList) {
//			System.out.println(fileName);
//		}
		
//		2. 두 리스트를 비교하여 불일치 되는 서버 이미지 파일을 삭제 
		if(dbFileList != null && !serverFileList.isEmpty()) {
			
//			serverFileList 반복 접근 
			for(File serverFile : serverFileList) {
				
				String fileName = serverFile.toString().substring(serverFile.toString().lastIndexOf("\\") + 1);
				
//				indexOf(비교값) : 비교값이 List에 존재하면 해당 index 반환 , 없으면 -1 반환
				if(dbFileList.indexOf(fileName) < 0) { // 없을경우
//					같은 파일 네임이 디비에서 조회한 내용에 있으면 index반환 , 없으면 -1 반환
					serverFile.delete();
					System.out.println(fileName + "삭제");
				}
			}
		}
		
		
		
//		fileList가 존재하고 파일이 1개 이상 있을 경우 
//		if(fileList != null && fileList.length > 0) {
			
//			for(File image : fileList) {
//				System.out.println(image);
//			}
			
//		}
		
		
		
		
		
		
	}
}
