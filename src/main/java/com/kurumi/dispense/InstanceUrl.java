package com.kurumi.dispense;

/**
 *session文件里包含的instance信息 
 * @author yeyongli
 *
 */
public class InstanceUrl {
	//检查UID
	private String studyInstanceUID;
	//序列UID
	private String seriesInstanceUID;
	//实列UID
	private String sOPInstanceUID;
	//检查类型
	private String modality;
	//实例号
	private Integer instanceNumber;
	//序列UID
	private Integer seriesNumber;
	//检查日期
	private String studyDate;
	
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}
	public String getSeriesInstanceUID() {
		return seriesInstanceUID;
	}
	public void setSeriesInstanceUID(String seriesInstanceUID) {
		this.seriesInstanceUID = seriesInstanceUID;
	}
	public String getsOPInstanceUID() {
		return sOPInstanceUID;
	}
	public void setsOPInstanceUID(String sOPInstanceUID) {
		this.sOPInstanceUID = sOPInstanceUID;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public Integer getSeriesNumber() {
		return seriesNumber;
	}
	public void setSeriesNumber(Integer seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	public Integer getInstanceNumber() {
		return instanceNumber;
	}
	public void setInstanceNumber(Integer instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	public String getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}
	

}
