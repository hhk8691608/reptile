package com.example.demo.xiaochong.models;

import java.io.Serializable;

public class WebsiteInfo implements Serializable, Cloneable {
	private Long id;
	private String name;
	private String website;
	private String url;
	private String mode;
	private String post;
	private String header;
	private String registeredMsg;
	private String unregisteredMsg;
	private Byte matchType;
	private String proxyStrategy;
	private String proxyRefused;
	private Byte enable;
	private String charset;
	private String createBy;
	private java.util.Date createTime;
	private String updateBy;
	private java.util.Date updateTime;
	private String remark;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getWebsite() {
		return website;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getMode() {
		return mode;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getPost() {
		return post;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getHeader() {
		return header;
	}
	public void setRegisteredMsg(String registeredMsg) {
		this.registeredMsg = registeredMsg;
	}
	public String getRegisteredMsg() {
		return registeredMsg;
	}
	public void setUnregisteredMsg(String unregisteredMsg) {
		this.unregisteredMsg = unregisteredMsg;
	}
	public String getUnregisteredMsg() {
		return unregisteredMsg;
	}
	public void setMatchType(Byte matchType) {
		this.matchType = matchType;
	}
	public Byte getMatchType() {
		return matchType;
	}
	public void setProxyStrategy(String proxyStrategy) {
		this.proxyStrategy = proxyStrategy;
	}
	public String getProxyStrategy() {
		return proxyStrategy;
	}
	public void setProxyRefused(String proxyRefused) {
		this.proxyRefused = proxyRefused;
	}
	public String getProxyRefused() {
		return proxyRefused;
	}
	public void setEnable(Byte enable) {
		this.enable = enable;
	}
	public Byte getEnable() {
		return enable;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getCharset() {
		return charset;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
  
	private static final String Table = "sm_website_info";
	private static final String Alias = "as_sm_website_info";
	private static final long serialVersionUID = 16454654984465L;
 
	public static final String DBStrId = Alias+".`id`";
	public static final String DBStrName = Alias+".`name`";
	public static final String DBStrWebsite = Alias+".`website`";
	public static final String DBStrUrl = Alias+".`url`";
	public static final String DBStrMode = Alias+".`mode`";
	public static final String DBStrPost = Alias+".`post`";
	public static final String DBStrHeader = Alias+".`header`";
	public static final String DBStrRegisteredMsg = Alias+".`registered_msg`";
	public static final String DBStrUnregisteredMsg = Alias+".`unregistered_msg`";
	public static final String DBStrMatchType = Alias+".`match_type`";
	public static final String DBStrProxyStrategy = Alias+".`proxy_strategy`";
	public static final String DBStrProxyRefused = Alias+".`proxy_refused`";
	public static final String DBStrEnable = Alias+".`enable`";
	public static final String DBStrCharset = Alias+".`charset`";
	public static final String DBStrCreateBy = Alias+".`create_by`";
	public static final String DBStrCreateTime = Alias+".`create_time`";
	public static final String DBStrUpdateBy = Alias+".`update_by`";
	public static final String DBStrUpdateTime = Alias+".`update_time`";
	public static final String DBStrRemark = Alias+".`remark`";
	
	@Override
	public WebsiteInfo clone() {
		WebsiteInfo o = null;
		try{
			o = (WebsiteInfo)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return o;
	}
}

