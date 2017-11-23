package com.zhongjian.webserver.beanconfiguration;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;


/**
 * Asynchronous Tasks
 * @author Xu
 *
 */
@Component
public class AsyncTask {
	
	@Async
	public void doTask1() throws InterruptedException{
		System.out.println(Thread.currentThread().getName());
        Thread.sleep(5000);
	}
	
	@Async
	public void doTask2() throws InterruptedException{
		System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
	}
}