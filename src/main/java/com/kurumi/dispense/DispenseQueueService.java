package com.kurumi.dispense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.util.Constants;
import com.kurumi.dispense.util.Dicom2Jpg;
import com.kurumi.dispense.util.ExceptionUtil;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.PropsUtil;
import com.kurumi.dispense.util.ReceiveMSMQUtil;
import com.kurumi.dispense.util.StringUtil;
import com.kurumi.dispense.util.SuidToHash;

public class DispenseQueueService implements Runnable{
	private static Logger log = LoggerFactory.getLogger(DispenseQueueService.class);
	
	private static String fullNameMsmq;
	private static String dicomPicture;
	private static String dicomResource;
	
	static{
		Properties p = PropsUtil.loadProps("config.properties");
		fullNameMsmq = p.getProperty("full.name.msmq");
		dicomPicture = p.getProperty("dicom.picture");
		dicomResource = p.getProperty("dicom.resource");
	}
	
	/**
	 * 解析从msmq队列里面获取的instance信息
	 * 
	 * @param instanceStr
	 * @return
	 */
	public DicomInstanceMessage getDicomInstanceMessage(String instanceStr) {
		DicomInstanceMessage dim = null;
		try {
			String [] instanceArray = instanceStr.split("\r\n");
			String sessionId = StringUtil.getArrayOfElement(instanceArray[0]);
			String studyInstanceUID = StringUtil.getArrayOfElement(instanceArray[1]);
			String seriesInstanceUID = StringUtil.getArrayOfElement(instanceArray[2]);
			String sOPInstanceUID = StringUtil.getArrayOfElement(instanceArray[3]);
			String instanceNumber = StringUtil.getArrayOfElement(instanceArray[4]);
			String characterSet = StringUtil.getArrayOfElement(instanceArray[5]);
			String transferSyntaxUID = StringUtil.getArrayOfElement(instanceArray[6]);
			String sOPClassUID = StringUtil.getArrayOfElement(instanceArray[7]);
			String modality = StringUtil.getArrayOfElement(instanceArray[8]);
			String seriesNumber = StringUtil.getArrayOfElement(instanceArray[9]);
			String dcmFileStorageLocation = StringUtil.getArrayOfElement(instanceArray[10]);
			String studyID = StringUtil.getArrayOfElement(instanceArray[11]);
			String studyDate = StringUtil.getArrayOfElement(instanceArray[12]);
			String studyTime = StringUtil.getArrayOfElement(instanceArray[13]);
			String accessionNumber = StringUtil.getArrayOfElement(instanceArray[14]); 
			String frameNumber = StringUtil.getArrayOfElement(instanceArray[15]);
			
			//log.info("从msqm队列里面取数据,instanceUid" + sOPInstanceUID);
		    dim = new DicomInstanceMessage(sessionId, studyInstanceUID, seriesInstanceUID, sOPInstanceUID,
					instanceNumber, characterSet, transferSyntaxUID, sOPClassUID, modality,
					seriesNumber, dcmFileStorageLocation, studyID, studyDate, studyTime, accessionNumber, frameNumber);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-----------解析从队列里面获取instance信息-----------" + instanceStr + "异常信息为:" + ExceptionUtil.getStackTrace(e));
		}
		return dim;
	}
	
	/**
	 * dicom文件转换为图片
	 * 
	 * @param dim
	 * @return
	 */
	public boolean dicomFileTransformPicture(DicomInstanceMessage dim, int frameIndex) {
		boolean flag = false;
		
		try {
			//保存dicom文件
			String sessionId = dim.getSessionId();
			String studyInstanceUID = dim.getStudyInstanceUID();
			String seriesInstanceUID = dim.getSeriesInstanceUID();
			String sOPInstanceUID = dim.getsOPInstanceUID();
			String dcmFileStorageLocation = dim.getDcmFileStorageLocation();
			//压缩类型
			String transferSyntaxUID = dim.getTransferSyntaxUID();
			String dicomDir = dicomResource + "/" + SuidToHash.getHash(sessionId) + "/" + 
									sessionId + "/" + studyInstanceUID + "/" + seriesInstanceUID;
			File dicomDirFile = new File(dicomDir);
			if (!dicomDirFile.exists()) {
				dicomDirFile.mkdirs();
			}
			
			String dicomPath = dicomDir + "/" + sOPInstanceUID;
			//保存dicom文件
			//FileUtil.copyFile(dcmFileStorageLocation, dicomPath);
			//压缩解压缩 dicom目标文件保存的路径 sessionHash(分4级创建)/session/studyUid/seriesUid/instance
			String pictureDir = dicomPicture + "/" + SuidToHash.getHash(sessionId) + "/" + 
									sessionId + "/" + studyInstanceUID + "/" + seriesInstanceUID;
			
			File pictureDirFile = new File(pictureDir);
			
			if (!pictureDirFile.exists()) {
				pictureDirFile.mkdirs();
			}
			
			String picturePath = pictureDir + "/" + sOPInstanceUID + ".jpg"; 
		    flag = Dicom2Jpg.runbat("Jp2kLossLess", dcmFileStorageLocation, picturePath, frameIndex);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("dicom转换为图片异常------dicom原路径:"+dim.getDcmFileStorageLocation()+ "---异常信息为--" + ExceptionUtil.getStackTrace(e));
		}
		return flag;
	}
	
	/**
	 * 解析从msmq队列里面获取session信息
	 * 
	 * @param instanceStr
	 * @return
	 */
	public SessionMessage getSessionMessage(String instanceStr) {
		SessionMessage sessionMessage = null;
		
		try {
			//sessionUrl列表
			String [] instanceArray = instanceStr.split("\r\n");
			String sessionId =  StringUtil.getArrayOfElement(instanceArray[0]);
		    sessionMessage = new SessionMessage();
			sessionMessage.setSessionId(sessionId);
			List<InstanceUrl> instanceUrlList = new ArrayList<InstanceUrl>();
			
			if (instanceArray.length > 1) {
				for(int i = 1; i < instanceArray.length; i++) {
					InstanceUrl instanceUrl = new InstanceUrl();
					String instanceUrlStr = instanceArray[i].replaceFirst("instance=", "");
					String[]instanceUrlArray = instanceUrlStr.split(",");
					String studyInstanceUID = instanceUrlArray[0].replaceFirst("studyInstanceUID", "");
					String seriesInstanceUID = instanceUrlArray[1].replaceFirst("seriesInstanceUID", "");
					String sOPInstanceUID = instanceUrlArray[2].replaceFirst("sOPInstanceUID", "");
					String modality = instanceUrlArray[3].replaceFirst("modality", "");
					String instanceNumber = instanceUrlArray[4].replaceFirst("instanceNumber", "");
					String studyDate = instanceUrlArray[5].replaceFirst("studyDate", "");
					String seriesNumber = instanceUrlArray[6].replaceFirst("seriesNumber", "");
					
					Integer instanceNum = instanceNumber!=null?Integer.parseInt(instanceNumber):null;
					Integer seriesNum = seriesNumber!=null?Integer.parseInt(seriesNumber):null;
					
					instanceUrl.setStudyInstanceUID(studyInstanceUID);
					instanceUrl.setSeriesInstanceUID(seriesInstanceUID);
					instanceUrl.setsOPInstanceUID(sOPInstanceUID);
					instanceUrl.setModality(modality);
					instanceUrl.setInstanceNumber(instanceNum);
					instanceUrl.setSeriesNumber(seriesNum);
					instanceUrl.setStudyDate(studyDate);
					
					instanceUrlList.add(instanceUrl);
				}
			}
			
			sessionMessage.setInstanceUrlList(instanceUrlList);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("---------解析队列里面的session字符串信息失败!-------" + instanceStr + "异常信息为:" + ExceptionUtil.getStackTrace(e));
		}
		
		return sessionMessage;
	}

	/**
	 * 解析从msmq队列里面获取session信息
	 * 
	 * @param instanceJsonStr
	 * @return
	 */
	public SessionMessage getSessionMessage_JsonStr(String instanceJsonStr) {
		SessionMessage sessionMessage = null;
		
		try {
			//sessionUrl列表
			sessionMessage = JsonUtils.jsonToPojo(instanceJsonStr, SessionMessage.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("---------解析队列里面的session字符串信息失败!-------" + instanceJsonStr + "异常信息为:" + ExceptionUtil.getStackTrace(e));
		}
		
		return sessionMessage;
	}
	
	/**
	 * 设置窗宽和窗位
	 * 
	 * @param dim
	 * @return
	 */
	public DicomInstanceMessage setDicomWWAndWL(DicomInstanceMessage dim) {
		//有可能从dim获取instanceUid异常
		String instanceUid = null;
		try {
			instanceUid = dim.getsOPInstanceUID();
			Map<String, String> map = Dicom2Jpg.getWindowWidthAndWindowCenter(dim.getDcmFileStorageLocation());
			
			if (map.containsKey("ww") && map.containsKey("wl")) {
				//有窗宽
				dim.setWw(map.get("ww"));
				dim.setWl(map.get("wl"));
			} else {
				//没有窗宽, 有可能转换的时候出现异常所以要判断一下
				if (map.containsKey("bitStored")) {
					String ww = Math.pow(2, Integer.parseInt(map.get("bitStored"))) + "";
					
					dim.setWw(ww);
					dim.setWl("0");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("----------获取窗宽和窗位异常,instanceUId=" + instanceUid+"-----异常信息为" + ExceptionUtil.getStackTrace(e));
		}
		
		return dim;
	}
	
	/**
	 * 这个线程一直重复从msmq队列里面读取数据
	 */
	public void run() {
			while(true) {
				System.out.println("--------当前线程-------" + Thread.currentThread().getName());
				
				Map<String, String> map = ReceiveMSMQUtil.takeMessageQueue(fullNameMsmq);
				
				if (map != null) {
					String instanceStr = map.get("stringBody");
					String label = map.get("label");
					
					if (label.equals("instance")) {
						try {
							DicomInstanceMessage dim = getDicomInstanceMessage(instanceStr);
							dim = setDicomWWAndWL(dim);
							if (dim != null) {
								String frameNumber = dim.getNumberFrame();
								String instanceUid = dim.getsOPInstanceUID();
								if (StringUtils.isNotBlank(frameNumber)) {
									int number = Integer.parseInt(frameNumber);
									
									for (int i = 0; i < number; i++) {
										DicomInstanceMessage newDim = (DicomInstanceMessage) dim.clone();
										newDim.setsOPInstanceUID(instanceUid+ "_" + (i+1));
										//第几帧的图片
										newDim.setFrameIndex(i+1);
										boolean flag =  dicomFileTransformPicture(newDim, i+1);
										
										if (flag) {
											//处理成功
											newDim.setProcessedStatus(Constants.PROCESS_SUCCESS);
										} else {
											//处理失败
											newDim.setProcessedStatus(Constants.PROCESS_FAIL);
										}
										//放入到收集器队列里面
										AccumulatorService.putAccumulatorQueue(newDim);
									}
									
								}
							
							} else {
								log.warn("-------------从msqm里面取instance的信息转换的过程中出现异常-----------");
							}
							
						} catch (InterruptedException e) {
							e.printStackTrace();
							log.error("intance信息放入到AccumulatorQueue队列异常----" + ExceptionUtil.getStackTrace(e));
						}
					} else if (label.equals("session")) {
						//添加sessionUrl
						//AccumulatorService.putAccumulatorQueue(sessionMessage);
						try {
							SessionMessage sessionMessage = getSessionMessage(instanceStr);
							if (sessionMessage != null) {
								AccumulatorService.putFirstAccumulatorQueue(sessionMessage);
							} else {
								log.warn("-------------从msqm里面取session的信息转换的过程中出现异常-----------");
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							log.error("sessionUrl信息放入到AccumulatorQueue队列异常----" + "异常信息为---" + ExceptionUtil.getStackTrace(e));
						}
					}
				}
			}
	}

	
	

}
