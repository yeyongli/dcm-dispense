package com.kurumi.dispense.entity;

import java.util.Date;

/**
 * 实例entry
 * @author yeyongli
 *
 */
public class Instance {
	//实例UID
	private String sopInstanceUid;
	//序列UID
	private String seriesInstanceUid;
	//实例号
	private Integer instanceNumber;
	//字符集
	private String characterSet;
	//压缩类型
	private String transferSyntaxUid;
	//SOP类型
	private String sopClassUid;
	private Date createDate;
	private Integer processResult;
	private Integer isOk;
	//图片存储的位置
	private String instancePath;
	//sessionId
	private String sessionId;
	//窗宽
	private String ww;
	//窗位
	private String wl;
	//dicom图片保存的位置
	private String dcmFileStorageLocation;
	private Integer frameIndex;
	
	public String getSopInstanceUid() {
		return sopInstanceUid;
	}
	public void setSopInstanceUid(String sopInstanceUid) {
		this.sopInstanceUid = sopInstanceUid;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public Integer getInstanceNumber() {
		return instanceNumber;
	}
	public void setInstanceNumber(Integer instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	public String getCharacterSet() {
		return characterSet;
	}
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
	public String getTransferSyntaxUid() {
		return transferSyntaxUid;
	}
	public void setTransferSyntaxUid(String transferSyntaxUid) {
		this.transferSyntaxUid = transferSyntaxUid;
	}
	public String getSopClassUid() {
		return sopClassUid;
	}
	public void setSopClassUid(String sopClassUid) {
		this.sopClassUid = sopClassUid;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getProcessResult() {
		return processResult;
	}
	public void setProcessResult(Integer processResult) {
		this.processResult = processResult;
	}
	public Integer getIsOk() {
		return isOk;
	}
	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getInstancePath() {
		return instancePath;
	}
	public void setInstancePath(String instancePath) {
		this.instancePath = instancePath;
	}
	public String getWw() {
		return ww;
	}
	public void setWw(String ww) {
		this.ww = ww;
	}
	public String getWl() {
		return wl;
	}
	public void setWl(String wl) {
		this.wl = wl;
	}
	public String getDcmFileStorageLocation() {
		return dcmFileStorageLocation;
	}
	public void setDcmFileStorageLocation(String dcmFileStorageLocation) {
		this.dcmFileStorageLocation = dcmFileStorageLocation;
	}
	public Integer getFrameIndex() {
		return frameIndex;
	}
	public void setFrameIndex(Integer frameIndex) {
		this.frameIndex = frameIndex;
	}
	
	
}
