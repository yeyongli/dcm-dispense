package com.kurumi.dispense;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kurumi.dispense.service.InstanceService;
import com.kurumi.dispense.util.FileUtil;
import com.kurumi.dispense.util.JsonUtils;

public class InstanceServiceTest {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		InstanceService instanceService = (InstanceService) context.getBean("instanceService");
		Map<String, Object> result = new HashMap<>();
		long count = instanceService.findInstanceNoSuccessCountBySessionId("1c8b0064-76e3-4185-9ddc-af3f10c0c432");
		List<Map<String, Object>> list = instanceService.findInstanceListBySessionId("1c8b0064-76e3-4185-9ddc-af3f10c0c432");
		result.put("notUploadList", list);
		result.put("needUploadSun", count);
		result.put("notUploadCount", list.size());
		
		String jsonStr = JsonUtils.objectToJson(result);
		String filePath = "1c8b0064-76e3-4185-9ddc-af3f10c0c432.txt";
		FileUtil.appendInfoToFile("D://dicom//log//sessionUrl//"+filePath, "该sessionId还没有成功上传的instance:"+jsonStr);
	}
	
	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		InstanceService instanceService = (InstanceService) context.getBean("instanceService");
		
		long count = instanceService.findInstanceNoSuccessCountBySessionId("1c8b0064-76e3-4185-9ddc-af3f10c0c432");
		System.out.println(count);
	}
	
	
	
	

}



