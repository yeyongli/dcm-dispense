package com.kurumi.dispense.service;

import java.util.List;
import java.util.Map;

import com.kurumi.dispense.DicomInstanceMessage;
import com.kurumi.dispense.util.Result;

public interface InstanceService {
	Result insertInstance(DicomInstanceMessage dicomInstanceMessage);

	List<Map<String, Object>> findInstanceListBySessionId(String sessionId);
	
	long findInstanceNoSuccessCountBySessionId(String sessionId);
	
	
	
	
	
}
