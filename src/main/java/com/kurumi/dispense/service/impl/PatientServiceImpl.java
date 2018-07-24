package com.kurumi.dispense.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurumi.dispense.dao.PatientDao;
import com.kurumi.dispense.dao.StudyDao;
import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.PatientService;

@Service("patientService")
public class PatientServiceImpl implements PatientService{

	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private StudyDao studyDao;
	
	public void insertPatient(Patient patient) {
		String studyUid = patient.getStudyInstanceUID();
		
		if (StringUtils.isNotBlank(studyUid)) {
			//判断该患者对应的study是否存在
			if (studyDao.findCountByStudyUid(studyUid) == 0) {
				//先添加study 
				Study study = new Study();
				study.setStudyInstanceUid(studyUid);
				studyDao.insertStudyDao(study);
				
				//判断该study对应的患者是否存在, 没有就添加,有就修改
				if (patientDao.findCountByStudyUid(studyUid) == 0) {
					patientDao.insertPatient(patient);
				} else {
					//修改患者的信息
					patientDao.updatePatient(patient);
				}
			} else {
				//检查已经存在  判断该study对应的患者是否存在, 没有就添加,有就修改
				if (patientDao.findCountByStudyUid(studyUid) == 0) {
					patientDao.insertPatient(patient);
				} else {
					//修改患者的信息
					patientDao.updatePatient(patient);
				}
			}
		} else {
			//患者没有检查
			patientDao.insertPatient(patient);
		}
		
	}

}
