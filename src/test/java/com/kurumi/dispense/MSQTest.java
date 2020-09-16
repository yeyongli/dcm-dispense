package com.kurumi.dispense;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.kurumi.dispense.util.ReceiveMSMQUtil;
import com.kurumi.dispense.util.SendMSMQUtil;

public class MSQTest {

	private static ExecutorService consumerThreadPool = Executors.newFixedThreadPool(10); 
	
	private static ExecutorService producerThreadPool = Executors.newFixedThreadPool(12);
	
	public static void main(String[] args) {
		//new Thread(new Producer()).start();
		for (int i = 0; i < 1; i++) {
			producerThreadPool.execute(new Producer());
		}
		
		for (int i = 0; i < 10; i++) {
			consumerThreadPool.execute(new Consumer());
		}
	}
}

//Producer Class in java
class Producer implements Runnable {
  private static int produceCount = 0;
  @Override
  public void run() {
      for(int i=0; i<100000; i++){
          try {
        	  SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.112\\private$\\javaqueue", "instance",
						 "1111111111111111111111", UUID.randomUUID().toString());
        	  synchronized (Producer.class) {
        		  produceCount = produceCount + 1;
				System.out.println("生产的个数--"+produceCount);
			}
          } catch (Exception ex) {
        	  System.out.println(ex);
          }
      }
  }

}

//Consumer Class in Java
class Consumer implements Runnable{
  private static int consumeCount = 0;
  //private static AtomicInteger consumeCount = new AtomicInteger(0);
	
  @Override
  public void run() {
      while(true){
          try {
        		  //consumeCount = consumeCount+1;
        		  ReceiveMSMQUtil.takeMessageQueue("direct=tcp:192.168.21.112\\private$\\javaqueue");
        		  
        		  synchronized (Consumer.class) {
        			  consumeCount = consumeCount + 1;
        			  System.out.println("消费的个数"+consumeCount);
				}
          } catch (Exception ex) {
        	  ex.printStackTrace();
          }
      }
  }
  
}



