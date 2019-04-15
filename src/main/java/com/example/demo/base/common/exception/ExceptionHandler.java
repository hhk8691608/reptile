package com.example.demo.base.common.exception;

import com.example.demo.base.common.SystemCode;

/**
 * Handle all exceptions, assemble a error message from exception.
 */
public class ExceptionHandler {
	private Throwable t;

	public ExceptionHandler(Throwable t) {
		assert t != null : "t can NOT be null.";
		this.t = t;
	}

	/**
	 * retrieve error code from exception
	 */
	public int getErrorCode() {
		if (t instanceof AssistException) {
			return ((AssistException) t).getErrorCode();
		} else {
			return SystemCode.SYSTEM_EXCEPTION.getCode();
		}
	}

	public Object[] getMetaData() {
		if (t instanceof AssistException) {
			return ((AssistException) t).getMetaData();
		} else {
			return null;
		}
	}

	public static Throwable getCause(Throwable e) {
		Throwable cause = e.getCause();
		if (cause == null) {
			return e;
		} else {
			return getCause(cause);
		}

	}
}
