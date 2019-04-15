package com.example.demo.base.common;

public enum SystemCode {
	SUCCESS(1000, "正常"), 
	REQUEST_PARAMETER_FAILURE(1210, "参数错误"),
	
	LOGIN_TIME_OUT(1250, "超时或未登录"), 
	FILE_NOT_EXIST(1255, "文件不存在"),
	VARIABLE_NOT_REPLACE_EXCEPTION(1350, "请求request变量未替换"), 
	NO_PERMISSION(1400, "无权限回复"),
	CODE_UNSUPPORTED_TRANSTYPE(1410, "不支持此交易类型"), 
	SYSTEM_CONFIGURATION_ERROR(1420, "系统配置文件错误"), 
	
	NET_CONNECT_EXCEPTION(1450, "网络连接异常"),
	HTML_FAILURE(1455, "对应请求返回登录错误信息"), 
	HTML_UNKNOWN_FAILURE(1460, "对应请求返回登录未知错误信息"), 
	VERIFICATION_CODE_FAILURE(1465, "验证码错误"),
	
	MAIL_PROTOCOL_UNSUPPORTED_TRANSTYPE(1470, "此邮箱不支持"),
	XML_ERROR(3210, "XML参数错误"),
	SYSTEM_EXCEPTION(5000, "未知异常");

	private int code;

	private String message;

	private SystemCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

