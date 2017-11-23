package com.zhongjian.webserver;
 
import static org.junit.Assert.assertArrayEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.zhongjian.webserver.Application;
import com.zhongjian.webserver.beanconfiguration.AsyncTask;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AppTest {
 
  @Autowired
  private AsyncTask asyncTask;
 
  @Test  
  public void AsyncTaskTest() throws InterruptedException, ExecutionException {  
	  System.out.println(Thread.currentThread().getName()); 
       asyncTask.doTask1();  
       asyncTask.doTask2();  
        

          Thread.sleep(1000);  
        
      System.out.println(("All tasks finished."));  
  } 
 
}