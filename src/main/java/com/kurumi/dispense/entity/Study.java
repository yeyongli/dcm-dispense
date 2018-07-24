package com.kurumi.dispense.entity;

import java.util.Date;
import java.util.List;

public class Study {
	//检查UID
	private String studyInstanceUid;
	//非检查KEY
	private String stydyId;
	//20101101
	private String studyDate;
	//173730
	private String studyTime;
	private String accessionNumber;
	//检查类型
	private String modality;
	//患者信息
	private Patient patient;
	private List<SeriesInstance> seriesList;
	private Date createDate;
	
	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}
	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
	}
	public String getStydyId() {
		return stydyId;
	}
	public void setStydyId(String stydyId) {
		this.stydyId = stydyId;
	}
	public String getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}
	public String getStudyTime() {
		return studyTime;
	}
	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}
	public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public List<SeriesInstance> getSeriesList() {
		return seriesList;
	}
	public void setSeriesList(List<SeriesInstance> seriesList) {
		this.seriesList = seriesList;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}



}
