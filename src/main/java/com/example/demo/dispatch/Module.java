package com.example.demo.dispatch;

import java.util.List;

import com.example.demo.xiaochong.models.WebsiteInfo;

public class Module {
	
	private String mobile;
	
	private List<WebsiteInfo> websiteList;
	
	public Module() {
		super();
	}
	
	
	public Module(String mobile, List<WebsiteInfo> websiteList) {
		super();
		this.mobile = mobile;
		this.websiteList = websiteList;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<WebsiteInfo> getWebsiteList() {
		return websiteList;
	}

	public void setWebsiteList(List<WebsiteInfo> websiteList) {
		this.websiteList = websiteList;
	}

	

}
