package com.kurumi.dispense.util;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

public class SendMSMQUtil {
	private static Logger log = LoggerFactory.getLogger(SendMSMQUtil.class);
	
	static{
		//系统去加载MsmqJava默认在项目的根路径下面
		System.loadLibrary("MsmqJava");
		//指定dll的加载路径
		//System.load("C:\\Users\\h2oco2\\Desktop\\MsmqJava.dll");
	}
	
	/**
	 * 
	 * @param fullname
	 * @param label 标题 
	 * @param body 内容
	 * @param correlationId
	 * 
	 */
	public static void putMessageQueue(String fullname, String label, String body, String correlationId) {
		 Queue queue= null;  
		 try {  
	            //192.32.12.76为本机的IP地址（经测试不能使用127.0.0.1,不然会报错）  
	            //private$\\myqueue是队列名字  
	             queue= new Queue(fullname);  
	            
	             putMsmqQueue(queue, label, body, correlationId);
	         } catch (Exception e) {    
	            e.printStackTrace();  
	            log.error("-------第一次往msmq队列里面存信息失败,异常信息为-------" + ExceptionUtil.getStackTrace(e));
	            
	            //重新往队列里面存信息
	            if (queue == null) {
	            	try {
						queue = new Queue(fullname);
					} catch (MessageQueueException e1) {
						e1.printStackTrace();
					}
	            } 
	            
	            //重试
	            retryMsmqQueue(queue, label, body, correlationId);
	        } finally {
	        	if (queue != null) {
	        		try {
						queue.close();
					} catch (MessageQueueException e) {
						e.printStackTrace();
					}
	        	}
	        }
	}
	
	public static void putMsmqQueue(Queue queue, String label, String body,
			String correlationId) throws Exception{
			Message msg = new Message(body, label, correlationId);
			queue.send(msg, true);
	}
	
	public static void retryMsmqQueue(Queue queue, String label, String body,
			String correlationId) {
		try {
			if (queue != null) {
				putMsmqQueue(queue, label, body, correlationId);
				log.info("-------重新往msmq队列里面存信息成功!--------");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("----------重新往msmq队列里面存信息失败!异常信息为----" + ExceptionUtil.getStackTrace(e));
		}
	}
	
	public static void main(String[] args) {
		 try {  
			 
				 putMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue", "instance",
						 "11", UUID.randomUUID().toString());
			 }
			 
	       catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		 
	}
	
	
	
}
