package com.example.demo.base.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ConfigurableApplicationContext;

public class HitlibConfig {
	private static HitlibConfig instance = null;

	ConfigurableApplicationContext applicationContext;

	private static ExecutorService pool = null;

	/**
	 * 获取线程池
	 * 
	 * @return
	 */
	public synchronized static ExecutorService getThreadPool() {
		if (pool == null) {
			pool = Executors.newCachedThreadPool();
		}
		return pool;
	}

	public synchronized static HitlibConfig getInstance() {
		try {
			if (instance == null) {
				instance = new HitlibConfig();
			}
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getInt(String key, int defaultValue) {
		String tmp = this.get(key, "" + defaultValue);
		try {
			return Integer.parseInt(tmp);
		} catch (Exception e) {
			throw new IllegalArgumentException("The entry(key=" + key + ",value=" + tmp + ") is not INT type.");
		}
	}

	public int getInt(String key) {
		String tmp = this.get(key);
		try {
			return Integer.parseInt(tmp);
		} catch (Exception e) {
			throw new IllegalArgumentException("The entry(key=" + key + ",value=" + tmp + ") is not INT type.");
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value = this.get(key, String.valueOf(defaultValue));
		if (value != null && value.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public boolean getBoolean(String key) {
		String value = this.get(key);
		if (value != null && value.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public double getDouble(String key, double defaultValue) {
		String tmp = this.get(key, String.valueOf(defaultValue));
		try {
			return Double.parseDouble(tmp);
		} catch (Exception e) {
			throw new IllegalArgumentException("The entry(key=" + key + ",value=" + tmp + ") is not DOUBLE type.");
		}
	}

	public double getDouble(String key) {
		String tmp = this.get(key);
		try {
			return Double.parseDouble(tmp);
		} catch (Exception e) {
			throw new IllegalArgumentException("The entry(key=" + key + ",value=" + tmp + ") is not DOUBLE type.");
		}
	}

	public String get(String key) {
		return applicationContext == null ? null : applicationContext.getEnvironment().getProperty(key);
	}

	public String get(String key, String defaultValue) {
		String value = this.get(key);
		return (value == null) ? defaultValue : value;
	}

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
