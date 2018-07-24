package com.kurumi.dispense.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

public class ReceiveMSMQUtil {
	private static Logger log = LoggerFactory.getLogger(ReceiveMSMQUtil.class);
	
	static{
		System.loadLibrary("MsmqJava");
		//System.load("C:\\Users\\h2oco2\\Desktop\\MsmqJava.dll");
	}
	
	public static Map<String, String> takeMessageQueue(String fullname) {
		Map<String, String> map = new HashMap<>();
		Queue queue= null;  
		
		try {
			 queue= new Queue(fullname);
             map = takeMsmqQueue(queue);
             
             return map;
		} catch (Exception e) {  
           e.printStackTrace();
           log.error("----第一次从队列里面获取信息失败,异常信息为----" + ExceptionUtil.getStackTrace(e));
           
           if (queue == null) {
        	   try {
				queue = new Queue(fullname);
			} catch (MessageQueueException e1) {
				e1.printStackTrace();
			}
           }
           
           //重新获取队列的信息
           Map<String, String> retryMap = retryTakeMsmqQueue(queue);
           
           if (retryMap != null) {
        	   log.info("---------重新从队列里面获取数据成功!-----------");
           } else {
        	   log.info("---------重新从队列里面获取数据失败!-----------");
           }
           
           return retryMap;
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
	
	/**
	 * 重新获取队列的信息
	 * 
	 * @param queue
	 * @return
	 */
	public static Map<String, String> retryTakeMsmqQueue(Queue queue) {
		try {
			 if (queue != null) {
				 Map<String, String> retryMap = takeMsmqQueue(queue);
	     		 return retryMap;
			 }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("重新获取队列信息失败,异常信息为---" + ExceptionUtil.getStackTrace(e));
		}
        
        return null;
	}
	
	/**
	 * 从Msmq队列里面取数据
	 * 
	 * @param queue
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> takeMsmqQueue(Queue queue) throws Exception{
		Map<String, String> map = new HashMap<>();
		//队列里面没有数据则阻塞
        Message message=queue.receive(-1);  
        String stringBody = message.getBodyAsString();
        String label = message.getLabel();
        map.put("stringBody", stringBody);
        map.put("label", label);
		
		return map;
	}
	
//	public static void main(String[] args) {
//		  try {  
//			//  takeMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue");
//			  for(int i = 0; i < 152; i++) {
//				 // String stringBody = takeMessageQueue("direct=tcp:192.168.21.85\\private$\\dicomInstanceQueue");
//			  }
//		  	}  
//	       catch (Exception e) {  
//	            e.printStackTrace();  
//	        }  
//	}
//	
	

}
