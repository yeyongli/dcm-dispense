package com.kurumi.dispense.dao;

import com.kurumi.dispense.entity.Session;

public interface SessionDao {

	int insertSession(Session session);
	
	long findSessionBySessionId(String sessionId);
	
	
	
}
