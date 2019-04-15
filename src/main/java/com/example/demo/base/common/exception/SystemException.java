package com.example.demo.base.common.exception;

import com.example.demo.base.common.SystemCode;

/**
 * A unchecked exception, system can not handle this type of exception.
 */
public class SystemException extends RuntimeException implements AssistException {

	private static final long serialVersionUID = 7171482777723261520L;

	private int errorCode = SystemCode.SYSTEM_EXCEPTION.getCode();

	// metaData will be used to assemble response message.
	private Object metaData[];

	public SystemException(int code, Object metaData[]) {
		this.errorCode = code;
		this.metaData = metaData;
	}

	public SystemException(int code, String message) {
		super(message);
		this.errorCode = code;
	}

	public SystemException(Exception cause) {
		super(cause);
	}

	public SystemException(int code, Object[] metaData, Throwable cause) {
		super(cause);
		this.errorCode = code;
		this.metaData = metaData;
	}

	public SystemException(int code, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = code;
	}

	public SystemException(String message) {
		super(message);
		this.errorCode = SystemCode.SYSTEM_EXCEPTION.getCode();
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = SystemCode.SYSTEM_EXCEPTION.getCode();
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public Object[] getMetaData() {
		return this.metaData;
	}
}
