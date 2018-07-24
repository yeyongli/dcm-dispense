package com.kurumi.dispense;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.util.Constants;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.Result;

public class WriteLogTest {
	private static Logger logger = LoggerFactory.getLogger(WriteLogTest.class);
	
	@Test
	public void test() {
		logger.info("info ----------------");
		//logger.error("系统异常1111111");
		//logger.warn("WARM");
		
	}
	
	@Test
	public void test1( ) {
		int number = 10;
		for (int i = 0; i < number; i++) {
				System.out.println(i+1);
			}
	}

	
	
}
