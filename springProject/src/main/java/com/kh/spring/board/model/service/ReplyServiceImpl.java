package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.ReplyDAO;
import com.kh.spring.board.model.vo.Reply;

@Service // 서비스 임을 알려줌 + Bean 등록
public class ReplyServiceImpl implements ReplyService{

	@Autowired // 등록된 Bean 중 타입이 일치하는 Bean을 자동으로 의존성 주입
	private ReplyDAO dao;

//	댓글 목록 조회 Service 구현 
	@Override
	public List<Reply> selectReplyList(int parentBoardNo) {
		return dao.selectReplyList(parentBoardNo);
	}

//	댓글 삽입 service 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertReply(Map<String, Object> map) {
		
		// 크로스 사이트 스크립팅 방지
		map.put("replyContent", replaceParameter((String) map.get("replyContent")));

		// ajax로 textarea 내용을 얻어올 경우 개행문자가 \n으로 취급됨.
		// 개행문자 처리 \n -> <br>
		map.put("replyContent", ((String) map.get("replyContent")).replaceAll("\n", "<br>"));
		
		return dao.insertReply(map);
	}
	
//	댓글 수정 Service 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateReply(Reply updateReply) {
		
		// 크로스 사이트 스크립팅 방지
		updateReply.setReplyContent(replaceParameter(updateReply.getReplyContent()));
		
		// ajax로 textarea 내용을 얻어올 경우 개행문자가 \n으로 취급됨.
		// 개행문자 처리 \n -> <br>
		updateReply.setReplyContent(updateReply.getReplyContent().replaceAll("\n", "<br>"));
		
		return dao.updateReply(updateReply);
	}
	
	
//	댓글 삭제 Service 구현
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteReply(int replyNo) {
		
		return dao.deleteReply(replyNo);
	}
	
//	대댓글 삽입 Service 구현 
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertChildReply(Map<String, Object> map) {

		// 크로스 사이트 스크립팅 방지
		map.put("replyContent", replaceParameter((String) map.get("replyContent")));

		// ajax로 textarea 내용을 얻어올 경우 개행문자가 \n으로 취급됨.
		// 개행문자 처리 \n -> <br>
		map.put("replyContent", ((String) map.get("replyContent")).replaceAll("\n", "<br>"));
		
		return dao.insertChildReply(map);
	}
	
	
	
	// 크로스 사이트 스크립트 방지 메소드
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
	
	

	

	
}
