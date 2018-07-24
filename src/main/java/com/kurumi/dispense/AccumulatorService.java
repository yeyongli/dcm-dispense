package com.kurumi.dispense;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.entity.Session;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.InstanceService;
import com.kurumi.dispense.service.PatientService;
import com.kurumi.dispense.service.SessionService;
import com.kurumi.dispense.service.StudyService;
import com.kurumi.dispense.util.ApplicationContextUtil;
import com.kurumi.dispense.util.Constants;
import com.kurumi.dispense.util.ExceptionUtil;
import com.kurumi.dispense.util.FileUtil;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.PropsUtil;
import com.kurumi.dispense.util.Result;
import com.kurumi.dispense.util.SuidToHash;

/**
 * 
 * 收集器处理队列服务
 * @author yeyongli
 *
 */
public class AccumulatorService implements Runnable{
	private Logger log = LoggerFactory.getLogger(AccumulatorService.class);
	
	//保存instance或者session信息 (双向队列)
	private static BlockingDeque<Object> accumulatorQueue = new LinkedBlockingDeque<Object>();
	//保存患者信息队列(单向队列)
	private static BlockingQueue<Patient> patientQueue = new LinkedBlockingQueue<Patient>();
	
	/**
	 * 
	 * 串行从队列里面取patient信息,
	 * sessionUrl信息, instance信息,
	 * 并生成json文件
	 * 
	 */
	public void run() {
		while(true) {
			if (patientQueue.size() == 0) {
				//每次取十个元素, 不重复的sessionId有多少个, 记录日志
				int length = accumulatorQueue.size();
				
				if (length > 0) {
					//List<> list = new ArrayList<Object>();
					length = length >= 10 ? 10 : length;
					Set<String> sessionSet = new HashSet();
					
					for(int i = 0; i < length; i++) {
						try {
							Object object = accumulatorQueue.take();
							
							if (object instanceof SessionMessage) {
								SessionMessage sessionMessage = (SessionMessage)object;
								ApplicationContext context = ApplicationContextUtil.getApplicationContext();
								SessionService sessionService = (SessionService) context.getBean("sessionService");
								sessionService.insertSession(sessionMessage);
								sessionSet.add(sessionMessage.getSessionId());
								
								//生成sessionUrlJson文件
								createSessionUrlJsonFile(sessionMessage.getSessionId());
							} else if (object instanceof DicomInstanceMessage) {
								System.out.println("i----------------"+i);
								//instance 信息
								DicomInstanceMessage dicomInstanceMessage = (DicomInstanceMessage)object;
								ApplicationContext context = ApplicationContextUtil.getApplicationContext();
								InstanceService instanceService = (InstanceService) context.getBean("instanceService");
								Result result = instanceService.insertInstance(dicomInstanceMessage);
								if ((int)result.getData() == Constants.HAVE_SESSION) {
									sessionSet.add(dicomInstanceMessage.getSessionId());
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							log.error("从accumulatorQueue队列里面取数据-----"+ ExceptionUtil.getStackTrace(e));
						} catch (Exception e) {
							e.printStackTrace();
							log.error("从accumulatorQueue队列里面取数据-----"+ ExceptionUtil.getStackTrace(e));
						}
					}
					
					//统计session会话关闭, 生成日志信息
					try {
						if (sessionSet != null && sessionSet.size() > 0) {
							Iterator<String> it = sessionSet.iterator();
							while(it.hasNext()) {
								String sessionId = it.next();
								
								//总共有多少个instance, 失败的有多少个
								ApplicationContext context = ApplicationContextUtil.getApplicationContext();
								InstanceService instanceService = (InstanceService) context.getBean("instanceService");
								Map<String, Object> result = new HashMap<>();
								long count = instanceService.findInstanceNoSuccessCountBySessionId(sessionId);
								List<Map<String, Object>> list = instanceService.findInstanceListBySessionId(sessionId);
								result.put("notUploadList", list);
								result.put("needUploadSun", count);
								result.put("notUploadCount", list.size());
								
								String jsonStr = JsonUtils.objectToJson(result);
								String filePath = sessionId + ".txt";
								Properties properties = PropsUtil.loadProps("config.properties");
								String logSessionUrl = properties.getProperty("log.session.url"); 
								FileUtil.createDir(logSessionUrl);
								FileUtil.appendInfoToFile(logSessionUrl + "/" + filePath, "该sessionId还没有成功上传的instance:"+jsonStr);
								
								//写入sessionUrl文件
								StudyService studyService = (StudyService) context.getBean("studyService");
								List<Study> studyList = studyService.findStudyListBySessionId(sessionId);
								String sessionUrl = properties.getProperty("session.url");
								String sessionDir = sessionUrl + "/" + SuidToHash.getHash(sessionId);
								FileUtil.createDir(sessionDir);
								String sessionPath = sessionDir + "/" + sessionId + ".json";
								Map<String, Object> map = new HashMap<>();
								Session session = new Session();
								session.setSessionId(sessionId);
								map.put("studyList", studyList);
								map.put("session", session);
								
								String sessionJson = JsonUtils.objectToJson(map);
								FileUtil.writeFile(new File(sessionPath), sessionJson);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.error("统计session会话关闭, 生成日志信息---------" + ExceptionUtil.getStackTrace(e));
					}
				}
			} else {
				//先从patientQueue里面取
				try {
					Patient patient = patientQueue.take();
					ApplicationContext context = ApplicationContextUtil.getApplicationContext();
					PatientService patientService = (PatientService) context.getBean("patientService");
					
					//添加用户的信息
					patientService.insertPatient(patient);
					
					//生成patientJson文件
					createPatientUrlJsonFile(patient);
					
					//生成sessionUrlJson文件
					createSessionUrlJsonFile(patient.getSessionId());
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error("-----从队列里面取患者信息并且保存到数据库失败!----异常信息为:" + ExceptionUtil.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * 生成sessionUrlJson文件
	 * 
	 * @param sessionMessage
	 */
	public void createSessionUrlJsonFile(String sessionId) {
		try {
			//生成sessionUrl列表文件
			ApplicationContext context = ApplicationContextUtil.getApplicationContext();
			StudyService studyService = (StudyService) context.getBean("studyService");
			List<Study> studyList = studyService.findStudyListBySessionId(sessionId);
			
			Properties properties = PropsUtil.loadProps("config.properties");
			String sessionUrl = properties.getProperty("session.url");
			String sessionDir = sessionUrl + "/" + SuidToHash.getHash(sessionId);
			FileUtil.createDir(sessionDir);
			String sessionPath = sessionDir + "/" + sessionId + ".json";
			Map<String, Object> map = new HashMap<>();
			Session session = new Session();
			session.setSessionId(sessionId);
			map.put("studyList", studyList);
			map.put("session", session);
			
			String sessionJson = JsonUtils.objectToJson(map);
			FileUtil.writeFile(new File(sessionPath), sessionJson);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("------生成sessionUrlJson文件异常,sessionId=-----" + sessionId + "--异常信息为--" + ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 生成patientJson文件
	 * 
	 * @param patient
	 */
	public void createPatientUrlJsonFile(Patient patient) {
		try {
			//把患者的信息保存到本地
			String sessionId = patient.getSessionId();
			Properties properties = PropsUtil.loadProps("config.properties");
			String patientJsonPath = properties.getProperty("patient.json.path");
			String patientDir = patientJsonPath + "/" + SuidToHash.getHash(sessionId) + "/" + 
									patient.getStudyInstanceUID();
			FileUtil.createDir(patientDir);
			String patientPath = patientDir + "/" + patient.getPatientKey() + ".json";
			String patientJson = JsonUtils.objectToJson(patient);
			FileUtil.writeFile(new File(patientPath), patientJson);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("--------生成patientJson文件异常,patient信息为--------"+patient.toString()+"---异常信息为:"+ExceptionUtil.getStackTrace(e));
		}
	}

	public static Object takeAccumulatorQueue() throws InterruptedException {
		return accumulatorQueue.take();
	}
	
	public static void putAccumulatorQueue(Object object) throws InterruptedException {
		 accumulatorQueue.put(object);
	}
	
	public static void putFirstAccumulatorQueue(Object object) throws InterruptedException {
		accumulatorQueue.putFirst(object);
	}
	
	public static Patient takePatientQueue() throws InterruptedException {
		return patientQueue.take();
	}
	
	public static void putPatientQueue(Patient patient) throws InterruptedException {
		 patientQueue.put(patient);
	}
	
	


}
