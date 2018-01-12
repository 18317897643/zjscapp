package com.zhongjian.webserver.beanconfiguration;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.service.CoreService;

@Component
public class AsyncTasks {

	@Autowired
	CoreService coreService;
	
	@Async
	//分润任务
	public void shareBenitTask(Integer type, Integer masterUserId, Integer slaveUserId, String memo, BigDecimal ElecNum)  {
		coreService.shareBenit(type, masterUserId, slaveUserId, memo, ElecNum);
    }

}
