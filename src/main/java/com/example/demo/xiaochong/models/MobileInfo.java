package com.example.demo.xiaochong.models;

import java.io.Serializable;

public class MobileInfo implements Serializable, Cloneable {
	private Long id;
	private String mobile;
	private String type;
	private java.util.Date createTime;
	private String remark;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile() {
		return mobile;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
  
	private static final String Table = "sm_mobile_info";
	private static final String Alias = "as_sm_mobile_info";
	private static final long serialVersionUID = 16454654984465L;
 
	public static final String DBStrId = Alias+".`id`";
	public static final String DBStrMobile = Alias+".`mobile`";
	public static final String DBStrType = Alias+".`type`";
	public static final String DBStrCreateTime = Alias+".`create_time`";
	public static final String DBStrRemark = Alias+".`remark`";
	
	@Override
	public MobileInfo clone() {
		MobileInfo o = null;
		try{
			o = (MobileInfo)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return o;
	}
}


