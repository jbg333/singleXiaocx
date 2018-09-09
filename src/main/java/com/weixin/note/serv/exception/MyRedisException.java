package com.weixin.note.serv.exception;

public class MyRedisException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyRedisException() {
		super();
	}

	public MyRedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MyRedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRedisException(String message) {
		super(message);
	}

	public MyRedisException(Throwable cause) {
		super(cause);
	}
	

}
