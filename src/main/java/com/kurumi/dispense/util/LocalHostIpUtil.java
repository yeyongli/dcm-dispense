package com.kurumi.dispense.util;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurumi.dispense.AccumulatorService;

public class LocalHostIpUtil {
	private static final Logger log = LoggerFactory.getLogger(AccumulatorService.class);
	
	public static String getLocalHostIp() {
		String ip = "";
		
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			log.error("获取本地ip地址异常------" + ExceptionUtil.getStackTrace(e));
		}
		
		return ip;
	}
	
	
	

}
