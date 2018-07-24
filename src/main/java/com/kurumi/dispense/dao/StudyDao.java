package com.kurumi.dispense.dao;

import java.util.List;

import com.kurumi.dispense.entity.Study;

public interface StudyDao {
	int insertStudyDao(Study study);

	long findCountByStudyUid(String studyUid);
	
	int updateStudy(Study study);
	
	List<Study> findStudyListBySessionId(String sessionId); 
	
	
	
	
	
}
