package com.zhongjian.webserver.component;

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
	
	@Async
	//赠送名额任务
	public void presentTask(Integer type, Integer userId)  {
		coreService.present(type, userId);
    }

}
