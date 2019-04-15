package com.example.demo.dispatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.base.common.exception.ApplicationException;
import com.example.demo.base.httprequest.HttpRequest;
import com.example.demo.xiaochong.models.MobileInfo;
import com.example.demo.xiaochong.models.WebsiteInfo;

public class Comsumer implements Runnable{

	private Storage<Module> storage;
	private int threadNo;

	public Comsumer(Storage<Module> storage, int threadNo) {
		super();
		this.storage = storage;
		this.threadNo = threadNo;
	}
	@Override
	public void run() {
		
		/***
		 * 
		 * 	获取仓库类的module
		 * 	拿到module中的号码模拟请求撞库操作
		 * 	获取到结果后填充入数据库
		 */
		
		try {
			
			Module module = storage.take();
			String mobile = module.getMobile();
			for(WebsiteInfo websiteInfo : module.getWebsiteList()) {
				
				
				// 设置代理
//				ProxyInfo proxy = null;
//				if (StringUtils.isNotBlank(websiteInfo.getProxyStrategy())) {
//					proxy = new ProxyInfo();
//					proxy.setRefuseInfo(websiteInfo.getProxyRefused());
//				}
				
				
				String post = null;
				if (StringUtils.isNotBlank(websiteInfo.getPost())) {
					post = StringUtils.replace(websiteInfo.getPost(), "${mobile}", module.getMobile());
					websiteInfo.setMode("POST");
				}
				
				
				String url = StringUtils.replace(websiteInfo.getUrl(), "${mobile}", module.getMobile());

				String html = HttpRequest.sendHttpRequestProxy(false, null,
						websiteInfo.getMode() == null ? "GET" : websiteInfo.getMode(), url, post, null,
						websiteInfo.getCharset(), getHeader(websiteInfo.getHeader()), false);
				
				// 处理结果
				if (html != null) {
					if (websiteInfo.getRegisteredMsg() != null) {
						if ((websiteInfo.getMatchType() == 1 && html.equals(websiteInfo.getRegisteredMsg())
								|| (websiteInfo.getMatchType() == 2
										&& html.indexOf(websiteInfo.getRegisteredMsg()) != -1))) {
							MobileInfo mobileInfo = new MobileInfo();
							mobileInfo.setMobile(module.getMobile());
							mobileInfo.setType(websiteInfo.getName());
							mobileInfo.setCreateTime(new Date());
//							mobileInfoService.insert(mobileInfo);
						}
					} else if (websiteInfo.getUnregisteredMsg() != null) {
						if ((websiteInfo.getMatchType() == 1 && !html.equals(websiteInfo.getRegisteredMsg())
								|| (websiteInfo.getMatchType() == 2
										&& html.indexOf(websiteInfo.getRegisteredMsg()) == -1))) {
							MobileInfo mobileInfo = new MobileInfo();
							mobileInfo.setMobile(module.getMobile());
							mobileInfo.setType(websiteInfo.getName());
							mobileInfo.setCreateTime(new Date());
//							mobileInfoService.insert(mobileInfo);
						}
					}
				}
				
				
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static Map<String, String> getHeader(String header) {
		Map<String, String> requestPropertyMap = null;
		if (StringUtils.isNotBlank(header)) {
			requestPropertyMap = new HashMap<String, String>();
			header = header.replaceAll("\t", "").replaceAll("\r", "");
			String[] headFields = header.split("\n");
			if (headFields != null) {
				for (String headField : headFields) {
					if (headField != null && !"".equals(headField.trim())) {
						int index = headField.indexOf(":");
						if (index != -1) {
							String key = headField.substring(0, index).trim();
							String value = headField.substring(index + 1).trim();
							requestPropertyMap.put(key, value);
						}
					}
				}
			}
		}
		return requestPropertyMap;
	}


}

