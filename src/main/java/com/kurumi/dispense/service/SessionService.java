package com.kurumi.dispense.service;

import com.kurumi.dispense.SessionMessage;

public interface SessionService {
	void insertSession(SessionMessage sessionMessage);
	
	long findSessionBySessionId(String sessionId);
	
	
	
}
