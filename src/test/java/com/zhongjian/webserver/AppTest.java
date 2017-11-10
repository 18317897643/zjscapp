package com.zhongjian.webserver;
 
import static org.junit.Assert.assertArrayEquals;
 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.zhongjian.webserver.Application;
import com.zhongjian.webserver.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AppTest {
 
  @Autowired
  private TestService testService;
 
  @Test
  public void likeName() {
    assertArrayEquals(
        new Object[]{
        		testService.test(),
          }, 
        new Object[]{
            true,
          }
    );
  }
 
}