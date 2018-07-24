package com.kurumi.dispense.entity;

import java.util.Date;

public class SessionInstance {
	//序列UID
	private String sopInstanceUid;
	//session编号
	private String sessionId;
	//1未处理2处理中3处理完
	private Integer isOk;
	//1处理成功 2处理失败
	private Integer processResult;
	//创建日期
	private Date createDate;
		
	public String getSopInstanceUid() {
		return sopInstanceUid;
	}
	public void setSopInstanceUid(String sopInstanceUid) {
		this.sopInstanceUid = sopInstanceUid;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getIsOk() {
		return isOk;
	}
	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}
	public Integer getProcessResult() {
		return processResult;
	}
	public void setProcessResult(Integer processResult) {
		this.processResult = processResult;
	}
	
	
}
