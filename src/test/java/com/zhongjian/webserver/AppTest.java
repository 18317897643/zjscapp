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
import com.zhongjian.webserver.service.PersonalCenterService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AppTest {
 
  @Autowired
  LoginAndRegisterService loginAndRegisterService;
   
  @Autowired
  PersonalCenterService personalCenterService;
  
  @Test  
  public void AsyncTaskTest() throws InterruptedException, ExecutionException {  
	 System.out.println(personalCenterService.getInformOfConsumption("15395068265"));
  } 
 
}