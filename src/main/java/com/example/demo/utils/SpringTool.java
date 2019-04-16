package com.example.demo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringTool {

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringTool.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static String get(String key) {
		return applicationContext.getEnvironment().getProperty(key);
	}

	public static int getInt(String key) {
		return Integer.parseInt(applicationContext.getEnvironment().getProperty(key));
	}

	/**
	 * 根据Bean名称获取实例
	 * 
	 * @param name
	 *            Bean注册名称
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static <T> T getBean(String name, Class<T> cls) throws BeansException {
		return (T) applicationContext.getBean(name, cls);
	}

	/**
	 * 根据Bean名称获取实例
	 * 
	 * @param name
	 *            Bean注册名称
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 根据Class获取实例
	 * 
	 * @param name
	 *            Bean注册名称
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static Object getBean(Class cls) throws BeansException {
		return applicationContext.getBean(cls);
	}

	/**
	 * 根据Class获取实例
	 * 
	 * @param name
	 *            Bean注册名称
	 * 
	 * @return bean实例
	 * 
	 * @throws BeansException
	 */
	public static boolean containsBean(String name) throws BeansException {
		return applicationContext.containsBean(name);
	}
}
