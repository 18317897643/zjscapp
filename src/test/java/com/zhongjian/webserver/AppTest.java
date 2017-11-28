package com.zhongjian.webserver;
 

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.zhongjian.webserver.Application;
import com.zhongjian.webserver.service.LoginAndRegisterService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AppTest {
 
  @Autowired
  LoginAndRegisterService loginAndRegisterService;
   
	
  @Test  
  public void AsyncTaskTest() throws InterruptedException, ExecutionException {  
	 loginAndRegisterService.checkUserExists("177680770641");
  } 
 
}