package com.kurumi.dispense.util;

/**
 * 常量
 * @author yeyongli
 *
 */
public class Constants {
	public static final int PROCESS_SUCCESS = 1;
	public static final int PROCESS_FAIL = 2;
	
	//(0028,1051)坐标表示窗宽
	public static final String DICOM_WINDOW_WIDTH = "(0028,1051)";
	//(0028,1050)表示窗位
	public static final String DICOM_WINDOW_CENTER = "(0028,1050)";
	//(0028,0101)表示位数
	public static final String DICOM_BIT_STORED = "(0028,0101)";
	//(0020,000D)表示studyUid
	public static final String DICOM_STUDY_UID = "(0020,000d)";
	//(0020,000e)表示seriesUid
	public static final String DICOM_SERIES_UID = "(0020,000e)";
	//(0008,0018)表示instanceUid
	public static final String DICOM_INSTANCE_UID = "(0008,0018)";
	//(0020,0013)表示InstanceNumber
	public static final String DICOM_INSTANCE_NUMBER = "(0020,0013)";
	//(0008,0005)表示characterSet
	public static final String DICOM_CHARACTER_SET = "(0008,0005)";
	//(0002,0010)表示TransferSyntaxUID
	public static final String DICOM_TRANSFER_SYNTAX_UID = "(0002,0010)";
	//(0008,0016)表示SOPClassUID
	public static final String DICOM_SOP_CLASS_UID = "(0008,0016)";
	//(0008,0060)表示Modality
	public static final String DICOM_MODALITY = "(0008,0060)";
	//(0020,0011)表示SeriesNumber
	public static final String DICOM_SERIES_NUMBER = "(0020,0011)";
	//(0020,0010)表示studyId
	public static final String DICOM_STUDY_ID = "(0020,0010)";
	//(0008,0020)表示StudyDate
	public static final String DICOM_STUDY_DATE = "(0008,0020)";
	//(0008,0030)表示studyTime
	public static final String DICOM_STUDY_TIME = "(0008,0030)";
	//(0008,0050)表示accessionNumber
	public static final String DICOM_ACCESSION_NUMBER = "(0008,0050)";
	//(0028,0008) NumberOfFrames
	public static final String DICOM_NUMBER_FRAMES = "(0028,0008)";
	//session是否存在
	public static final int NO_HAVE_SESSION = 0;
	public static final int HAVE_SESSION = 1;
	
	
	
	
	
	
	
	

}
