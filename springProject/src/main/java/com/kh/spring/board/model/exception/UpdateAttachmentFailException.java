package com.kh.spring.board.model.exception;

public class UpdateAttachmentFailException extends RuntimeException {
//	RuntimeException은 예외처리를 꼭 하지 않아도 되는 unChecked Exception의 최상위 부모이다 .
	
//	이를 상속받은 자식은 모두 unChecked Exception이 된다.
	
	
	public UpdateAttachmentFailException() {
		super();
	}
	
	public UpdateAttachmentFailException(String message) {
		super(message);
	}
	

}
