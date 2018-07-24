package com.kurumi.dispense.dao;

import com.kurumi.dispense.entity.Patient;

public interface PatientDao {
	int insertPatient(Patient patient);
	
	long findCountByStudyUid(String studyUid);
	
	int updatePatient(Patient patient);

}
