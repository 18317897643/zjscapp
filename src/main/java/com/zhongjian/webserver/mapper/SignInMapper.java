package com.zhongjian.webserver.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.SigninAward;

public interface SignInMapper {

	Integer addSignRecord(@Param("UserId") Integer UserId, @Param("SignTime") Date signTime);

	Date getLastSignTimeByUserId(@Param("UserId") Integer UserId);

	Integer getContinueDayByUserId(@Param("UserId") Integer UserId);

	Integer addSigninTermediateByUserId(@Param("UserId") Integer UserId, @Param("LastSignTime") Date lastSignTime,
			@Param("ContinueDay") Integer continueDay);

	Integer updateSigninTermediateByUserId(@Param("UserId") Integer UserId, @Param("LastSignTime") Date lastSignTime);
	
	Integer reSetContinueDayByUserId(@Param("UserId") Integer UserId, @Param("LastSignTime") Date lastSignTime);

	SigninAward getAwardsByUserId(@Param("UserId") Integer UserId, @Param("CurrentDate") String currentDate);

	List<java.sql.Date>getTheMonthSignData(@Param("UserId") Integer UserId, @Param("CurrentDate") String currentDate);
	
	Integer addSigninAward(@Param("UserId") Integer UserId, @Param("CurrentDate") Date currentDate);

	Integer updateSigninAwardSeven(@Param("UserId") Integer UserId, @Param("CurrentDate") String currentDate,
			@Param("Sevendaysaward") Integer Sevendaysaward,@Param("Condition") Integer Condition);

	Integer updateSigninAwardFourteen(@Param("UserId") Integer UserId, @Param("CurrentDate") String currentDate,
			@Param("Fourteendaysaward") Integer Fourteendaysaward,@Param("Condition") Integer Condition);

	Integer updateSigninAwardThirty(@Param("UserId") Integer UserId, @Param("CurrentDate") String currentDate,
			@Param("Thirtydaysaward") Integer Thirtydaysaward,@Param("Condition") Integer Condition);
	
	Integer deleteDatedSignInData(@Param("CurrentDate") String currentDate);
	
	Integer deleteDatedSignAwardData(@Param("CurrentDate") String currentDate);
}
