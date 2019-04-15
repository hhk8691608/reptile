package com.example.demo.base.common;                                                        

import java.io.File;

                                                                                                   
public final class ConstantContext {                                                               
	                                                                                               
	/**                                                                                            
	 * 日期格式                                                                                    
	 */                                                                                            
	public final static String DATE_FORMAT                                                         = "yyyyMMddHHmmss";
	                                                                                               
	public final static String DAY_DATE_FORMAT                                                     = "yyyy-MM-dd";
	                                                                                               
	public final static String MS_DATE_FORMAT                                                      = "yyyyMMddHHmmssSS";
	                                                                                               
	public final static String TIME_FORMAT                                                         = "yyyy-MM-dd HH:mm:ss";
	                                                                                               
	public final static String POINT_TIME_FORMAT                                                   = "yyyy.MM.dd HH:mm:ss";
	 
	/**
	 * 变量标识值
	 */
	public final static String VAR_VALUE                                                           = "\\$\\{";
	public final static String VAR_VALUE_INDEX                                                     = "${";
	
	/**                                                                                            
	 * 系统扫描包路径                                                                              
	 */                                                                                            
	public final static String DEFAULT_PACKAGE_NAME                                                = "com.xiaochongtech.sofa";
	
	/**
	 * javaFuncation脚本包名
	 */
	public final static String DEFAULT_JAVAFUNCATION_PACKAGE_NAME                                  = "com.xiaochongtech.sofa.utils";
	                                                                                               
	public final static String RPC_COOKIE_KEY                                                      = "cookieKey";
	
	public static final String LOCATION_KEY                                                        = "Location";
    
	public static final String COOKIE_KEY                                                          = "cookie";
                                                                                                   
	/**                                                                                            
	 * 文件路径                                                                                    
	 */                                                                                            
	public static final String RESOURCE_CLASSPATH                                                  = "classpath";
	                                                                                               
	public static final String RESPONSE_CODE                                                       = "responseCode";
	                                                                                               
	public static final String MESSAGE                                                             = "message";
	                                                                                               
	public static final String RESPONSE_BODY                                                       = "body";
                                                                                                   
	public static final String EXPIRE_START_TIME                                                   = "startTime";
	                                                                                               
	public static final String EXPIRE_SECONDS                                                      = "seconds";
                                                                                                   
	public static final String JEDIS_PUB_SUB_TYPE_TEMPLATE_LOADING                                 = "1";// 定义配置模板重新加载通知
}
