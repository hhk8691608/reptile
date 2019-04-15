package com.example.demo.dispatch;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.xiaochong.models.WebsiteInfo;

public class Producter  implements Runnable{
	
	private static String MOBILE_INFO = "1562514";
	private Storage<Module>  storage;
	
	public Producter() {
		this.storage= storage;
	}

	@Override
	public void run() {
		
		/**
		 *  获取撞库网站
		 * 	读取号码段
		 * 	补充后码段后面的四位号码
		 * 	填充module并入仓库类
		 * 
		 * 
		 */
		Byte enable = new Byte((byte) 1);
		WebsiteInfo e = new WebsiteInfo();
		e.setEnable(enable);
		
		List<WebsiteInfo> websiteList = new ArrayList<WebsiteInfo>();
		websiteList.add(e);
		
		
		for(int i = 10000;i<20000;i++) {
			String mobile = MOBILE_INFO + String.valueOf(i).substring(1);
			Module module = new Module();
			module.setMobile(mobile);
			module.setWebsiteList(websiteList);
			try {
				this.storage.put(module);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		storage.setIsExist(true);
		
	}

}
