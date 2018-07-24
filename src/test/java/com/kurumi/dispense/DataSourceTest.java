package com.kurumi.dispense;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kurumi.dispense.service.SessionService;

public class DataSourceTest {
	ApplicationContext context = null;
	
	@Before
	public void before() {
		 context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	@Test
	public void test() {
		for (int i = 0; i < 100000; i++) {
			SessionService sessionService = (SessionService) context.getBean("sessionService");
			SessionMessage sessionMessage = new SessionMessage();
			sessionMessage.setSessionId(UUID.randomUUID().toString());
			sessionService.insertSession(sessionMessage);
		
			System.out.println(i+1);
		}
		
	}
	
	

}
