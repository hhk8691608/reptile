package com.example.demo.base.common.exception;

import com.example.demo.base.common.SystemCode;

/**
 * A checked exception, system can handle this type of exception.
 */
public class ApplicationException extends Exception implements AssistException {
	private static final long serialVersionUID = -3331722252825081257L;

	private int errorCode;

	private Object[] metaData;

	public ApplicationException(int code, Object[] metaData) {
		this.metaData = metaData;
		this.errorCode = code;
	}

	public ApplicationException(int code, String message) {
		super(message);
		this.errorCode = code;
	}

	public ApplicationException(int code, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = code;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
		this.errorCode = SystemCode.SYSTEM_EXCEPTION.getCode();
	}

	public ApplicationException(int code, Object[] metaData, Throwable cause) {
		super(cause);
		this.errorCode = code;
		this.metaData = metaData;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public Object[] getMetaData() {
		return this.metaData;
	}
}
