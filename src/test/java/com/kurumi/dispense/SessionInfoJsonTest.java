package com.kurumi.dispense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.ReceiveMSMQUtil;
import com.kurumi.dispense.util.SendMSMQUtil;

public class SessionInfoJsonTest {
	public static void main(String[] args) {
		SessionMessage session = new SessionMessage();
		session.setSessionId("12323");
		List<InstanceUrl> list = new ArrayList<InstanceUrl>();
		InstanceUrl instance1 = new InstanceUrl();
		instance1.setInstanceNumber(1);
		instance1.setModality("ct");
		instance1.setSeriesInstanceUID("234343");
		instance1.setsOPInstanceUID("234343");
		instance1.setStudyDate("2017-01-02");
		instance1.setStudyInstanceUID("1243243");
		
		InstanceUrl instance2 = new InstanceUrl();
		instance2.setInstanceNumber(2);
		instance2.setModality("ct");
		instance2.setSeriesInstanceUID("23434355");
		instance2.setsOPInstanceUID("23434355");
		instance2.setStudyDate("2017-01-02");
		instance2.setStudyInstanceUID("1243243555");
		
		list.add(instance1);
		list.add(instance2);

		String jsonStr = JsonUtils.objectToJson(SessionMessage.class);
		/*SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue", "session", 
				 jsonStr, UUID.randomUUID().toString());*/
		
		//从队列里面获取session的信息
		Map<String, String> map = ReceiveMSMQUtil.takeMessageQueue("direct=tcp:192.168.21.85\\\\private$\\\\dicomInstanceQueue");
		
		/*if (map != null) {
			String label = map.get("label");
			String jsonStr1 = map.get("stringBody");
			System.out.println(label);
			System.out.println(jsonStr1);
		}*/
		
	}

}



