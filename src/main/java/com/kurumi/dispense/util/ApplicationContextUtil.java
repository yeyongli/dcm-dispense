package com.kurumi.dispense.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextUtil {

	private static  ApplicationContext context =  new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	//private static  ApplicationContext context =  new ClassPathXmlApplicationContext("file:C:/Users/h2oco2/Desktop/applicationContext.xml");
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	
	
	
	
	
	
	
}
