package com.kurumi.dispense.entity;

import java.util.Date;

public class Patient {
	private Integer id;
	private String patientKey;
	private String patientID;
	private String patientsName;
	private String patientsNameCn;
	private String patientsBirthDate;
	private String patientsSex;
	private Integer patientsAge;
	private String patientsHeight;
	private String patientsWeight;
	private String studyInstanceUID;
	private String departmentName;
	private String departmentNumber;
	private String sessionId;
	private Date createDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPatientID() {
		return patientID;
	}
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public String getPatientsName() {
		return patientsName;
	}
	public void setPatientsName(String patientsName) {
		this.patientsName = patientsName;
	}
	public String getPatientsNameCn() {
		return patientsNameCn;
	}
	public void setPatientsNameCn(String patientsNameCn) {
		this.patientsNameCn = patientsNameCn;
	}
	public String getPatientsBirthDate() {
		return patientsBirthDate;
	}
	public void setPatientsBirthDate(String patientsBirthDate) {
		this.patientsBirthDate = patientsBirthDate;
	}
	public String getPatientsSex() {
		return patientsSex;
	}
	public void setPatientsSex(String patientsSex) {
		this.patientsSex = patientsSex;
	}
	public Integer getPatientsAge() {
		return patientsAge;
	}
	public void setPatientsAge(Integer patientsAge) {
		this.patientsAge = patientsAge;
	}
	public String getPatientsHeight() {
		return patientsHeight;
	}
	public void setPatientsHeight(String patientsHeight) {
		this.patientsHeight = patientsHeight;
	}
	public String getPatientsWeight() {
		return patientsWeight;
	}
	public void setPatientsWeight(String patientsWeight) {
		this.patientsWeight = patientsWeight;
	}
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentNumber() {
		return departmentNumber;
	}
	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPatientKey() {
		return patientKey;
	}
	public void setPatientKey(String patientKey) {
		this.patientKey = patientKey;
	}
	
	@Override
	public String toString() {
		return "Patient [id=" + id + ", patientKey=" + patientKey + ", patientID=" + patientID + ", patientsName="
				+ patientsName + ", patientsNameCn=" + patientsNameCn + ", patientsBirthDate=" + patientsBirthDate
				+ ", patientsSex=" + patientsSex + ", patientsAge=" + patientsAge + ", patientsHeight=" + patientsHeight
				+ ", patientsWeight=" + patientsWeight + ", studyInstanceUID=" + studyInstanceUID + ", departmentName="
				+ departmentName + ", departmentNumber=" + departmentNumber + ", sessionId=" + sessionId
				+ ", createDate=" + createDate + "]";
	}
	
	

}


