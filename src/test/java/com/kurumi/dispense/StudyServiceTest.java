package com.kurumi.dispense;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.StudyService;
import com.kurumi.dispense.util.ApplicationContextUtil;

public class StudyServiceTest {

	@Test
	public void test() {
		ApplicationContext context = ApplicationContextUtil.getApplicationContext();;
		
		StudyService studyService = (StudyService) context.getBean("studyService");
		List<Study> list = studyService.findStudyListBySessionId("edac6631-cfc1-4ce4-b6bb-d89f5bdc4471");
		System.out.println(list);
		
	}

	
}
