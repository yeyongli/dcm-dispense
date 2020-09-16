package com.kurumi.dispense;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.util.ExceptionUtil;
import com.kurumi.dispense.util.PropsUtil;

/**
 * @author yeyongli
 */
public class DicomDispenseApplication {
	private static Logger log = LoggerFactory.getLogger(DicomDispenseApplication.class);
	
	public static void main(String[] args) {
		try {
			initService();
			//监听patientQueue队列
			new Thread(new PatientQueueService()).start();
			//收集器监听session, instance队列
			new Thread(new AccumulatorService()).start();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("服务初始化异常-------" + ExceptionUtil.getStackTrace(e));
		}
	}

	public static void initService() {
		Properties p = PropsUtil.loadProps("config.properties");
		String serviceList = p.getProperty("compression.decompression.service.list");
		
		if (StringUtils.isNotBlank(serviceList)) {
			String [] serviceListArray = serviceList.split(",");
			
			for (int i = 0; i < serviceListArray.length; i++) {
				DispenseQueueService dispenseQueue = new DispenseQueueService();
				new Thread(dispenseQueue).start();
			}
			
		}
	}
	
	
	
}









