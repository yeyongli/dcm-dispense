package com.kurumi.dispense.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurumi.dispense.dao.StudyDao;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.StudyService;

@Service("studyService")
public class StudyServiceImpl implements StudyService {
	@Autowired
	private StudyDao studyDao;

	public int insertStudy(Study study) {
		return studyDao.insertStudyDao(study);
	}

	@Override
	public List<Study> findStudyListBySessionId(String sessionId) {
		return studyDao.findStudyListBySessionId(sessionId);
	}
	
	

	

	
	
	
	
}
