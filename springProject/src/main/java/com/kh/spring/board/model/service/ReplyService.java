package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Reply;

public interface ReplyService {
	
	
	/** 댓글 목록 조회 Service 
	 * @param parentBoardNo
	 * @return rList
	 */
	public abstract  List<Reply> selectReplyList(int parentBoardNo);

	/** 댓글 삽입 Service
	 * @param map
	 * @return result 
	 */ 
	public abstract int insertReply(Map<String, Object> map);

	/** 댓글 수정 Service
	 * @param updateReply
	 * @return result
	 */
	public abstract int updateReply(Reply updateReply);

}
