package com.kurumi.dispense.entity;

import java.util.Date;
import java.util.List;

public class SeriesInstance {
	//序列UID
	private String seriesInstanceUid;
	//检查UID
	private String studyInstanceUid;
	//检查类型
	private String modality;
	//序列编号
	private Integer seriesNumber;
	//创建日期
	private Date create_date;
	
	private List<Instance> instanceList;
	
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}
	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
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
	public List<Instance> getInstanceList() {
		return instanceList;
	}
	public void setInstanceList(List<Instance> instanceList) {
		this.instanceList = instanceList;
	}
	
	

}
