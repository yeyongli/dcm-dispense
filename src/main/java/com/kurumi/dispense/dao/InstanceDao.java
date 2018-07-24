package com.kurumi.dispense.dao;

import java.util.List;
import java.util.Map;

import com.kurumi.dispense.entity.Instance;

public interface InstanceDao {
	int insertInstance(Instance instance);
	
	long findCountByInstanceUid(String instanceUid);
	
	Instance findInstanceByInstanceUid(String instanceUid);
	
	int updateInstance(Instance instance);
	
	List<Map<String, Object>> findInstanceListBySessionId(String sessionId);
	
	long findInstanceNoSuccessCountBySessionId(String sessionId);
	
	
	

	
}


