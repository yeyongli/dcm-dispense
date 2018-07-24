package com.kurumi.dispense;

/**
 * dicom instance描述信息
 * 
 * @author yeyongli
 * 
 */
public class DicomInstanceMessage implements Cloneable{
	private String sessionId;
	//检查UID
	private String studyInstanceUID;
	//序列UID
	private String seriesInstanceUID;
	//实例UID
	private String sOPInstanceUID;
	//实例号
	private String instanceNumber;
	//字符集
	private String characterSet;
	//压缩类型
	private String transferSyntaxUID;
	//SOP类型
	private String sOPClassUID;
	//检查类型
	private String modality;
	//序列编号
	private String seriesNumber;
	
	//dicom文件存储的位置
	private String dcmFileStorageLocation;
	
	private String studyID;
	private String studyDate;
	private String studyTime;
	private String accessionNumber;
	
	//图片存储的位置
	private String pictureFileStorageLocation;
	//处理状态
	private Integer status;
	//处理完结束状态 (成功,失败)
	private Integer processedStatus;
	private String patientKey;
	//窗宽
	private String ww;
	//窗位
	private String wl;
	
	private String numberFrame;
	
	private Integer frameIndex;
	
	public DicomInstanceMessage(String sessionId, String studyInstanceUID, String seriesInstanceUID, String sOPInstanceUID,
			String instanceNumber, String characterSet, String transferSyntaxUID, String sOPClassUID, String modality,
			String seriesNumber, String dcmFileStorageLocation, String studyID, String studyDate, String studyTime, String accessionNumber, String frameNumber) {
		super();
		this.sessionId = sessionId;
		this.studyInstanceUID = studyInstanceUID;
		this.seriesInstanceUID = seriesInstanceUID;
		this.sOPInstanceUID = sOPInstanceUID;
		this.instanceNumber = instanceNumber;
		this.characterSet = characterSet;
		this.transferSyntaxUID = transferSyntaxUID;
		this.sOPClassUID = sOPClassUID;
		this.modality = modality;
		this.seriesNumber = seriesNumber;
		this.dcmFileStorageLocation = dcmFileStorageLocation;
		this.studyID = studyID;
		this.studyDate = studyDate;
		this.studyTime = studyTime;
		this.accessionNumber = accessionNumber;
		this.numberFrame = frameNumber;
	}

	public DicomInstanceMessage() {
		super();
	}

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

	public String getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public String getTransferSyntaxUID() {
		return transferSyntaxUID;
	}

	public void setTransferSyntaxUID(String transferSyntaxUID) {
		this.transferSyntaxUID = transferSyntaxUID;
	}

	public String getsOPClassUID() {
		return sOPClassUID;
	}

	public void setsOPClassUID(String sOPClassUID) {
		this.sOPClassUID = sOPClassUID;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getSeriesNumber() {
		return seriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}

	public String getDcmFileStorageLocation() {
		return dcmFileStorageLocation;
	}

	public void setDcmFileStorageLocation(String dcmFileStorageLocation) {
		this.dcmFileStorageLocation = dcmFileStorageLocation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProcessedStatus() {
		return processedStatus;
	}

	public void setProcessedStatus(Integer processedStatus) {
		this.processedStatus = processedStatus;
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

	public String getPictureFileStorageLocation() {
		return pictureFileStorageLocation;
	}

	public void setPictureFileStorageLocation(String pictureFileStorageLocation) {
		this.pictureFileStorageLocation = pictureFileStorageLocation;
	}

	public String getStudyID() {
		return studyID;
	}

	public void setStudyID(String studyID) {
		this.studyID = studyID;
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
	
	

	public String getNumberFrame() {
		return numberFrame;
	}

	public void setNumberFrame(String numberFrame) {
		this.numberFrame = numberFrame;
	}
	
	

	public Integer getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(Integer frameIndex) {
		this.frameIndex = frameIndex;
	}

	@Override
	public String toString() {
		return "DicomInstanceMessage [sessionId=" + sessionId + ", studyInstanceUID=" + studyInstanceUID
				+ ", seriesInstanceUID=" + seriesInstanceUID + ", sOPInstanceUID=" + sOPInstanceUID
				+ ", instanceNumber=" + instanceNumber + ", characterSet=" + characterSet + ", transferSyntaxUID="
				+ transferSyntaxUID + ", sOPClassUID=" + sOPClassUID + ", modality=" + modality + ", seriesNumber="
				+ seriesNumber + ", dcmFileStorageLocation=" + dcmFileStorageLocation + ", studyID=" + studyID
				+ ", studyDate=" + studyDate + ", studyTime=" + studyTime + ", accessionNumber=" + accessionNumber
				+ ", pictureFileStorageLocation=" + pictureFileStorageLocation + ", status=" + status
				+ ", processedStatus=" + processedStatus + ", patientKey=" + patientKey + ", ww=" + ww + ", wl=" + wl
				+ ", numberFrame=" + numberFrame + "]";
	}

	
	 @Override  
	    public Object clone() {  
		 DicomInstanceMessage in = null;  
	        try{  
	            in = (DicomInstanceMessage)super.clone();  
	        }catch(CloneNotSupportedException e) {  
	            e.printStackTrace();  
	        }  
	        return in;  
	    } 
	
	

	
}



