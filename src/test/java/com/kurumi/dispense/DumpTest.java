package com.kurumi.dispense;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.util.Constants;
import com.kurumi.dispense.util.ExceptionUtil;

public class DumpTest {
	private static final Logger log = LoggerFactory.getLogger(DumpTest.class);
	
	
	@Test
	public void test1() {
		//aa bbb
		String dirStr = "C:\\Users\\h2oco2\\Desktop\\ge1";
		File dirFile = new File(dirStr);
		File[] files = dirFile.listFiles();
		List<DicomInstanceMessage> list = new ArrayList<DicomInstanceMessage>();
		
		List<DicomInstanceMessage> newList = new ArrayList<DicomInstanceMessage>();
		String sessionId = new Random().toString();
		
		//读取某个文件下面的所有的文件
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				//读取dicom的tag值
				DicomInstanceMessage dim = getWindowWidthAndWindowCenter(file);
				dim.setSessionId(sessionId);
				list.add(dim);
			}
		}
		
		//设置session的信息
		for(DicomInstanceMessage dim : list) {
			String frameNumber = dim.getNumberFrame();
			System.out.println(dim.getNumberFrame() +"------" + dim.getDcmFileStorageLocation());
			int number = Integer.parseInt(frameNumber);
			String instanceUid = dim.getsOPInstanceUID();
			
			for (int j = 0; j < number; j++) {
				DicomInstanceMessage dim1 = (DicomInstanceMessage) dim.clone();
				//图片第几帧
				dim1.setsOPInstanceUID(instanceUid + "_" + (j+1));
				newList.add(dim1);
			}
		}
		
		Dom4jTest.putDicomInstanceQueue(list);
		Dom4jTest.putSessionUrlListQueue(newList, sessionId);
		
	}
	
	public static String getDicomTagValue(String str, String tagName, String filePath) {
		String resultStr = "";
		
		try {
			resultStr = str.substring(str.indexOf("[")+1, str.indexOf("]"));
		} catch (Exception e) {
			log.error("----文件的路径"+ filePath +"------从dump文件读取" + tagName + "异常-----" + ExceptionUtil.getStackTrace(e));
		}
		
		return resultStr;
	}
	
	/**
	 * 获取dicom里面的tag值
	 * 
	 * @param context
	 * @return
	 */
	public static DicomInstanceMessage getWindowWidthAndWindowCenter(File file) {
		   DicomInstanceMessage dicomInstanceMessage = new DicomInstanceMessage();
		
		   InputStreamReader isr = null;
           BufferedReader br = null;  
           
           try {  
        	   isr = new InputStreamReader(new FileInputStream(file));
        	   br = new BufferedReader(isr);  
               String line = null;  

               while ((line = br.readLine()) != null) {
                     if (StringUtils.isNotBlank(line)){
                    	 if (line.contains(Constants.DICOM_WINDOW_WIDTH)) {
                    		 	//窗宽
                    			 String ww = getDicomTagValue(line, "窗宽", file.getAbsolutePath());
                        		 if (StringUtils.isNotBlank(ww)) {
                        			 dicomInstanceMessage.setWw(ww.trim());
                        		 }
                    	 } else if (line.contains(Constants.DICOM_WINDOW_CENTER)) {
                    		 	//窗位
                    			 String wl = getDicomTagValue(line, "窗位", file.getAbsolutePath());
                        		 if (StringUtils.isNotBlank(wl)) {
                        			 dicomInstanceMessage.setWl(wl.trim());
                        		 }
                    	 }  else if (line.contains(Constants.DICOM_INSTANCE_UID)) {
                    			 //instanceUid
                        		 String instanceUid = getDicomTagValue(line, "instanceUid", file.getAbsolutePath());
                        		 if (StringUtils.isNotBlank(instanceUid)) {
                        			 dicomInstanceMessage.setsOPInstanceUID(instanceUid.trim());
                        		 }
                    	 } else if (line.contains(Constants.DICOM_SERIES_UID) ) {
                    		 	//seriesUid
                    			 String seriesUid = getDicomTagValue(line, "seriesUid", file.getAbsolutePath());
                    			 if (StringUtils.isNotBlank(seriesUid)) {
                    				 dicomInstanceMessage.setSeriesInstanceUID(seriesUid.trim());
                    			 }
                    	 } else if (line.contains(Constants.DICOM_STUDY_UID)) {
                    		 	//studyUid
                    			 String studyUid = getDicomTagValue(line, "studyUid", file.getAbsolutePath());
                    			 if (StringUtils.isNotBlank(studyUid)) {
                    				 dicomInstanceMessage.setStudyInstanceUID(studyUid.trim());
                    			 }
                    	 } else if (line.contains(Constants.DICOM_INSTANCE_NUMBER)) {
                    		 	//实例号
                    			 String instanceNumber = getDicomTagValue(line, "instanceNumber", file.getAbsolutePath());
                        		 if(StringUtils.isNotBlank(instanceNumber)) {
                        			 dicomInstanceMessage.setInstanceNumber(instanceNumber.trim());
                        		 }
							
                    	 } else if (line.contains(Constants.DICOM_CHARACTER_SET)) {
                    		 	 //字符集
                    			 String characterSet = getDicomTagValue(line, "setCharacterSet", file.getAbsolutePath());
                        		 if (StringUtils.isNotBlank(characterSet)) {
                        			 dicomInstanceMessage.setCharacterSet(characterSet.trim());
                        		 }
                    	 } else if (line.contains(Constants.DICOM_TRANSFER_SYNTAX_UID)) {
                    		 //压缩类型
                    		 try {
								String transferSyntaxUID = line.substring(line.indexOf("=")+1,line.indexOf(":"));
								if (StringUtils.isNotBlank(transferSyntaxUID)) {
									dicomInstanceMessage.setTransferSyntaxUID(transferSyntaxUID);
								}
							} catch (Exception e) {
								log.error("----文件的路径"+ file.getAbsolutePath() +"----从dump文件读取characterSet异常---");
							}
                    	 } else if (line.contains(Constants.DICOM_SOP_CLASS_UID)) {
                    		 //SOP类型
                    		 try {
								String sOPClassUID = line.substring(line.indexOf("=")+1, line.indexOf("#"));
                    			 if (StringUtils.isNotBlank(sOPClassUID)) {
                    				 dicomInstanceMessage.setsOPClassUID(sOPClassUID);
                    			 }
							} catch (Exception e) {
								log.error("----文件的路径"+ file.getAbsolutePath() +"----从dump文件读取sOPClassUID异常---");
							}
                    		 
                    	 } else if (line.contains(Constants.DICOM_MODALITY)) {
                    		 	//检查类型
								String modality = getDicomTagValue(line, "modality", file.getAbsolutePath());
								if (StringUtils.isNotBlank(modality)) {
									dicomInstanceMessage.setModality(modality.trim());
								}
							
                    	 } else if (line.contains(Constants.DICOM_SERIES_NUMBER)) {
                    		 	//序列编号
								String seriesNumber = getDicomTagValue(line, "seriesNumber", file.getAbsolutePath());
								if (StringUtils.isNotBlank(seriesNumber)) {
									dicomInstanceMessage.setSeriesNumber(seriesNumber.trim());
								}
                    	 } else if (line.contains(Constants.DICOM_STUDY_ID)) {
                    		 	//studyId
								String studyId = getDicomTagValue(line, "studyId", file.getAbsolutePath());
								if (StringUtils.isNotBlank(studyId)) {
									dicomInstanceMessage.setStudyID(studyId);
								}
							
                    	 } else if (line.contains(Constants.DICOM_STUDY_DATE)) {
                    		 	//studyDate
								String studyDate = getDicomTagValue(line, "studyDate", file.getAbsolutePath());
								if (StringUtils.isNotBlank(studyDate)) {
									dicomInstanceMessage.setStudyDate(studyDate);
								}
                    	 } else if (line.contains(Constants.DICOM_STUDY_TIME)) {
                    		 //studyTime
								String studyTime = getDicomTagValue(line, "studyTime", file.getAbsolutePath());
								if (StringUtils.isNotBlank(studyTime)) {
									dicomInstanceMessage.setStudyTime(studyTime);
								}
                    	 } else if (line.contains(Constants.DICOM_ACCESSION_NUMBER)) {
                    		 //accessionNumber
                    			 String accessionNumber = getDicomTagValue(line, "accessionNumber", file.getAbsolutePath());
                        		 if (StringUtils.isNotBlank(accessionNumber)) {
                        			 dicomInstanceMessage.setAccessionNumber(accessionNumber);
                        		 }
                    	 } else if (line.contains(Constants.DICOM_NUMBER_FRAMES)) {
                    		 //帧的数量
								String numberFrame = getDicomTagValue(line, "numberFrame", file.getAbsolutePath());
								if (StringUtils.isNotBlank(numberFrame)) {
                       			 dicomInstanceMessage.setNumberFrame(numberFrame);
                       		 }
                    	 }
              } 
               
               }
            } catch (Exception e) {
			} finally {
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
           
           //存储路径
           dicomInstanceMessage.setDcmFileStorageLocation("C:\\Users\\h2oco2\\Desktop\\ge\\" + file.getName().replace(".txt", ""));
           
           if (StringUtils.isBlank(dicomInstanceMessage.getNumberFrame())) {
        	   dicomInstanceMessage.setNumberFrame("1");
           }
           
		return dicomInstanceMessage;
	}
	

	

}











