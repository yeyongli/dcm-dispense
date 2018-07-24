package com.kurumi.dispense.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kurumi.dispense.entity.SessionInstance;

public interface SessionInstanceDao {
	int insertSessionInstance(SessionInstance sessionInstance);
	
	long findCountByInstanceUidAndSessionUid(@Param("sessionUid") String sessionUid, @Param("instanceUid") String instanceUid);

	int updateIsOkAndResultProcessBySessionUidAndInstanceUid(Map<String, Object> params);
	
	
}
