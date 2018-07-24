package com.kurumi.dispense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.SendMSMQUtil;
import com.kurumi.dispense.util.StringUtil;

public class Dom4jTest {
	private static Logger log = LoggerFactory.getLogger(Dom4jTest.class);
	
	/**
	   delete from sop_instance_transfer_session;
	   delete from transfer_session;
	   delete from sop_instance;
	   delete from series_instance;
	   delete from patient;
	   delete from study_instance;
	   delete from sessionid_openid;
	 */
	@Test
	public void test2() {
		List<String> studyXmlpathList = new ArrayList<String>();
		studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\A7\\8Z\\OA\\OA\\1.2.840.113619.2.55.3.2831208458.415.1327797214.572.xml");
		studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\J9\\DD\\O9\\GS\\1.2.840.113619.2.55.3.2831208458.315.1336457410.39.xml");
		studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\KT\\M5\\CK\\D7\\1.2.840.113619.2.55.3.2831208458.857.1327915158.160.xml");
	/*	studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\N3\\LC\\9R\\PD\\1.2.840.113619.2.55.3.2831208458.335.1327645840.245.xml");
		studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\N4\\11\\BV\\0N\\1.2.840.113619.2.55.3.2831208458.335.1327645842.912.xml");
		
		studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\RF\\5T\\41\\EL\\1.2.840.113619.2.55.3.2831208458.218.1288344040.593.xml");*/
		
		//studyXmlpathList.add("D:\\dicom\\indexdir\\0020000d\\BW\\47\\05\\RK\\1.2.410.200010.86.101.5.201504100030.xml");
		for (String str : studyXmlpathList) {
			getDicomInstanceList(str);
		}
	}

	/**
	 * 读取dicomxml信息
	 * 
	 * @param studyXmlPath
	 */
	public static void getDicomInstanceList(String studyXmlPath) {
		try {
			List<DicomInstanceMessage> list = new ArrayList<>();
			
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(studyXmlPath));
			Element rootElement = document.getRootElement();
			
			String sessionId = UUID.randomUUID().toString();
			//String sessionId = "cd7b3ea8-33a5-47fd-82d2-87891980be2e";
			//study信息
			String studyUid = rootElement.attributeValue("id");
			String studyId = rootElement.attributeValue("study_id");
			String studyDate = rootElement.attributeValue("date");
			String studyTime = rootElement.attributeValue("time");
			String accessionNumber = rootElement.attributeValue("accession_number");
			String hashPrefix = rootElement.attributeValue("hash_prefix").replace("\\", "/");
			
			List<Element> seriesList = rootElement.elements("series");
			
			if (seriesList != null && seriesList.size() > 0) {
				for (Element seriesElement : seriesList) {
					//series信息
					String seriesUid = seriesElement.attributeValue("id");
					String modality = seriesElement.attributeValue("modality");
					String seriesNumber = seriesElement.attributeValue("number");
					
					List<Element> instanceList = seriesElement.elements("instance");
					
					if (instanceList != null && instanceList.size() > 0) {
						for (Element instanceElement : instanceList) {
							String instanceUid = instanceElement.attributeValue("id");
							String instanceNumber = instanceElement.attributeValue("number");
							String characterSet = instanceElement.attributeValue("charset");
							String transferSyntaxUid = instanceElement.attributeValue("");
							String sopClassUid = instanceElement.attributeValue("sop_class_uid");
							String url = instanceElement.attributeValue("url").replace("\\", "/");
							String dicomStorageLocation = "D:/dicom/archdir/v0000000/" + hashPrefix + "/" + studyUid + "/" + url;
							
							DicomInstanceMessage dim = new DicomInstanceMessage();
							
							dim.setsOPInstanceUID(instanceUid);
							dim.setStudyInstanceUID(studyUid);
							dim.setSeriesInstanceUID(seriesUid);
							dim.setInstanceNumber(instanceNumber);
							dim.setCharacterSet(characterSet);
							dim.setTransferSyntaxUID(transferSyntaxUid);
							dim.setsOPClassUID(sopClassUid);
							dim.setModality(modality);
							dim.setSeriesNumber(seriesNumber);
							dim.setStudyID(studyId);
							dim.setStudyDate(studyDate);
							dim.setStudyTime(studyTime);
							dim.setAccessionNumber(accessionNumber);
							dim.setSessionId(sessionId);
							dim.setDcmFileStorageLocation(dicomStorageLocation);
							list.add(dim);
						}
					}
				}
			}
			
			//instance信息放入到队列里面
			putDicomInstanceQueue(list);
			//putSessionUrlListQueue(list, sessionId);
			List<DicomInstanceMessage> dim1List = new ArrayList<DicomInstanceMessage>();
			
			if (list != null && list.size() > 0) {
				for (DicomInstanceMessage dim : list) {
					String oldInstanceUid = dim.getsOPInstanceUID();
					Integer numberFrame = 1;
					
					try {
						String numberFrameStr = dim.getNumberFrame();
						if (StringUtils.isNotBlank(numberFrameStr)) {
							numberFrame = Integer.parseInt(numberFrameStr);
						}
						
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					for (int i = 0; i < numberFrame; i++) {
						DicomInstanceMessage dim1 = (DicomInstanceMessage) dim.clone();
						dim1.setsOPInstanceUID(oldInstanceUid + "_" + numberFrame);
						dim1List.add(dim1);
					}
					
				}
			}
			
			putSessionUrlListQueue_jsonStr(list, sessionId);
			putPatientQueue(rootElement, studyUid, sessionId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 患者信息put到队列里面
	 * 
	 * @param rootElement
	 * @param studyUid
	 * @param sessionId
	 */
	public static void putPatientQueue(Element rootElement, String studyUid, String sessionId) {
		Element element = rootElement.element("patient");
		
		String patientID = element.attributeValue("id");
		String patientName = element.attributeValue("name");
		String patientBirth = element.attributeValue("birthday");
		String sex = element.attributeValue("sex");
		//String age = element.attributeValue("sex");
		String height = element.attributeValue("height");
		String width = element.attributeValue("weight");
		String studyUID = studyUid;
		String patientKey = UUID.randomUUID().toString();
		
		Patient patient = new Patient();
		patient.setPatientID(patientID);
		patient.setPatientsName(patientName);
		patient.setPatientsNameCn(patientName);
		patient.setPatientsBirthDate(patientBirth);
		patient.setPatientsSex(sex);
		patient.setPatientsAge(11);
		patient.setPatientsHeight(height);
		patient.setPatientsWeight(width);
		patient.setStudyInstanceUID(studyUid);
		//这两个字段目前xml文件里面没有
		patient.setDepartmentName("销售部");
		patient.setDepartmentNumber("111");
		patient.setSessionId(sessionId);
		patient.setPatientKey(UUID.randomUUID().toString());
		String patientJson = JsonUtils.objectToJson(patient);
		System.out.println(patient);
		SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\patientQueue", "patientQueue", patientJson, UUID.randomUUID().toString());
	}
	
	public static void putSessionUrlListQueue(List<DicomInstanceMessage> list, String sessionId) {
		if (list != null && list.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("sessionId="+sessionId+"\r\n");
			
			for (DicomInstanceMessage dim : list) {
				sb.append("instance=studyInstanceUID"+dim.getStudyInstanceUID()+",");
				sb.append("seriesInstanceUID"+dim.getSeriesInstanceUID()+",");
				sb.append("sOPInstanceUID"+dim.getsOPInstanceUID()+",");
				sb.append("modality"+dim.getModality()+",");
				sb.append("instanceNumber"+dim.getInstanceNumber()+",");
				sb.append("studyDate"+dim.getStudyDate()+",");
				sb.append("seriesNumber"+dim.getSeriesNumber()+"\r\n");
			}
			
			System.out.println("session列表的信息-----" + sb.toString());
			
			SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue", "session", 
					 sb.toString(), UUID.randomUUID().toString());
		}
	}
	
	public static void putSessionUrlListQueue_jsonStr(List<DicomInstanceMessage> list, String sessionId) {
		SessionMessage session = new SessionMessage();
		if (list != null && list.size() > 0) {
			session.setSessionId(sessionId);
			
			List<InstanceUrl> instanceUrlList = new ArrayList<>();
			for (DicomInstanceMessage dim : list) {
				InstanceUrl instanceUrl = new InstanceUrl();
				
				instanceUrl.setStudyInstanceUID(dim.getStudyInstanceUID());
				instanceUrl.setSeriesInstanceUID(dim.getSeriesInstanceUID());
				instanceUrl.setsOPInstanceUID(dim.getsOPInstanceUID());
				instanceUrl.setModality(dim.getModality());
				instanceUrl.setInstanceNumber(Integer.parseInt(dim.getInstanceNumber()));
				instanceUrl.setStudyDate(dim.getStudyDate());
				instanceUrl.setSeriesNumber(Integer.parseInt(dim.getSeriesNumber()));
				
				instanceUrlList.add(instanceUrl);
			}
			
			session.setInstanceUrlList(instanceUrlList);
			
			String jsonStr = JsonUtils.objectToJson(SessionMessage.class);
			 SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue", "session", 
					 jsonStr, UUID.randomUUID().toString());
		}
		
	}
	
	
	/**
	 * 把instance信息put到队列里面
	 * 
	 * @param list
	 */
	public static void putDicomInstanceQueue(List<DicomInstanceMessage> list) {
		if (list != null && list.size() > 0) {
			for (DicomInstanceMessage dim : list) { //size 152
				File file = new File(dim.getDcmFileStorageLocation());
				
				if(!file.exists()) {
					System.out.println(dim.getDcmFileStorageLocation());
				}
				
				Integer frameNumber = 0;
				
				try {
					String frameNumberStr = dim.getNumberFrame(); 
					if (StringUtils.isNotBlank(frameNumberStr)) {
						frameNumber = Integer.parseInt(frameNumberStr);
					}

						
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				 StringBuffer sb = new StringBuffer();
				 sb.append("sessionId="+dim.getSessionId()+"\r\n");
				 sb.append("studyInstanceUID="+dim.getStudyInstanceUID()+"\r\n");
				 sb.append("seriesInstanceUID="+dim.getSeriesInstanceUID()+"\r\n");
				 sb.append("sOPInstanceUID="+dim.getsOPInstanceUID()+"\r\n");
				 sb.append("instanceNumber="+dim.getInstanceNumber()+"\r\n");
				 sb.append("characterSet="+dim.getCharacterSet()+"\r\n");
				 sb.append("transferSyntaxUID="+dim.getTransferSyntaxUID()+"\r\n");
				 sb.append("sOPClassUID="+dim.getsOPClassUID()+"\r\n");
				 sb.append("modality="+dim.getModality()+"\r\n");
				 sb.append("seriesNumber="+dim.getSeriesNumber()+"\r\n");
				 sb.append("dcmFileStorageLocation="+dim.getDcmFileStorageLocation()+"\r\n");
				 sb.append("studyID="+dim.getStudyID()+"\r\n");
				 sb.append("studyDate="+dim.getStudyDate()+"\r\n");
				 sb.append("studyTime="+dim.getStudyTime()+"\r\n");
				 sb.append("accessionNumber="+dim.getAccessionNumber()+"\r\n");
				 
				 Integer numberFrame = 0;
				 try {
					String numberFrameStr = dim.getNumberFrame();
					if (StringUtils.isNotBlank(numberFrameStr)) {
						numberFrame = Integer.parseInt(numberFrameStr);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
				 sb.append("frameNumber="+numberFrame);
				 
				 //put data enter queue
				 SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue", "instance", 
						 sb.toString(), UUID.randomUUID().toString());
				 log.info("往 msqm 里面存数据,instanceUid="+ dim.getsOPInstanceUID());
			}
		}
	}
	
	
	
}


