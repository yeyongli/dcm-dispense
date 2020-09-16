package com.kurumi.dispense;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.LocalHostIpUtil;
import com.kurumi.dispense.util.SendMSMQUtil;

public class SessionInfoJsonTest {
	
	/**
	 * session列表信息 添加到msmq队列里面
	 * 
	 */
	@Test
	public void test1() {
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
		
		session.setInstanceUrlList(list);

		String jsonStr = JsonUtils.objectToJson(session);
		String msmqAddress = "direct=tcp:"+LocalHostIpUtil.getLocalHostIp()+"\\private$\\dicomInstanceQueue";
		System.out.println(jsonStr);
		SendMSMQUtil.putMessageQueue(msmqAddress, "session", jsonStr, UUID.randomUUID().toString());
		
	}

}



