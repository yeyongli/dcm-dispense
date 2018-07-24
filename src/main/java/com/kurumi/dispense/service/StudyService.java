package com.kurumi.dispense.service;


import java.util.List;

import com.kurumi.dispense.entity.Study;

public interface StudyService {
	
	int insertStudy(Study study);
	
	List<Study> findStudyListBySessionId(String sessionId); 

}
