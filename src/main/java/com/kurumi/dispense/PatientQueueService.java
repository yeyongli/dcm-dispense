package com.kurumi.dispense;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.util.ExceptionUtil;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.PropsUtil;
import com.kurumi.dispense.util.ReceiveMSMQUtil;

/**
 * 把患者信息放到收集器队列里面处理
 * @author yeyongli
 *
 */
public class PatientQueueService implements Runnable {
	private static Logger log= LoggerFactory.getLogger(PatientQueueService.class);
	
	private static String fullNameMsmqPatientQueue;
	static{
		Properties p = PropsUtil.loadProps("config.properties");
		fullNameMsmqPatientQueue = p.getProperty("full.name.msmq.patientQueue");
	}
	
	public void run() {
		while (true) {
			try {
				Map<String, String> patientQueueMap = ReceiveMSMQUtil.takeMessageQueue(fullNameMsmqPatientQueue);
				
				if (patientQueueMap != null) {
					String patientQueueStr = patientQueueMap.get("stringBody");
					Patient patient = JsonUtils.jsonToPojo(patientQueueStr, Patient.class);
					//放入到patient队列里面
					AccumulatorService.putPatientQueue(patient);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("从msqm--patientQueue队列中取数据异常---患者的信息为" + ExceptionUtil.getStackTrace(e));
			}
			
			
		}
	}
	
}
