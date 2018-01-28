package com.zhongjian.webserver;
 

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.zhongjian.webserver.Application;
import com.zhongjian.webserver.mapper.ShoppingCartMapper;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.MemberShipService;
import com.zhongjian.webserver.service.PersonalCenterService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AppTest {
 
  @Autowired
  LoginAndRegisterService loginAndRegisterService;
   
  @Autowired
  PersonalCenterService personalCenterService;
  
  @Autowired
  MemberShipService memberShipService;
  
  @Autowired
  ShoppingCartMapper shoppingCartMapper;
  
  @Test  
  public void AsyncTaskTest() {  
	  System.out.println(1);
 
}
  }