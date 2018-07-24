package com.kurumi.dispense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurumi.dispense.DicomInstanceMessage;
import com.kurumi.dispense.dao.InstanceDao;
import com.kurumi.dispense.dao.SeriesInstanceDao;
import com.kurumi.dispense.dao.SessionInstanceDao;
import com.kurumi.dispense.dao.StudyDao;
import com.kurumi.dispense.entity.Instance;
import com.kurumi.dispense.entity.SeriesInstance;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.InstanceService;
import com.kurumi.dispense.strategy.GeneratePath;
import com.kurumi.dispense.strategy.GeneratePathStrategyInterface;
import com.kurumi.dispense.strategy.PicturePathStrategyA;
import com.kurumi.dispense.util.Result;

@Service("instanceService")
public class InstanceServiceImpl implements InstanceService{
	@Autowired
	private InstanceDao instanceDao;
	@Autowired
	private SeriesInstanceDao seriesInstanceDao;
	@Autowired
	private StudyDao studyDao;
	@Autowired
	private SessionInstanceDao sessionInstanceDao;
	
	public Result insertInstance(DicomInstanceMessage dicomInstanceMessage) {
		//session是否存在---0不存在  1存在
		int select = 0;
		
		String sessionId = dicomInstanceMessage.getSessionId();
		String instanceUid = dicomInstanceMessage.getsOPInstanceUID();
		String seriesUid = dicomInstanceMessage.getSeriesInstanceUID();
		String studyUid = dicomInstanceMessage.getStudyInstanceUID();
		Instance instance = instanceDao.findInstanceByInstanceUid(instanceUid);
		
		Study study = new Study();
		study.setStudyInstanceUid(studyUid);
		study.setStydyId(dicomInstanceMessage.getStudyID());
		study.setStudyDate(dicomInstanceMessage.getStudyDate());
		study.setStudyTime(dicomInstanceMessage.getStudyTime());
		study.setAccessionNumber(dicomInstanceMessage.getAccessionNumber());
		study.setModality(dicomInstanceMessage.getModality());
		
		SeriesInstance seriesInstance = new SeriesInstance();
		seriesInstance.setSeriesInstanceUid(seriesUid);
		seriesInstance.setStudyInstanceUid(studyUid);
		seriesInstance.setModality(dicomInstanceMessage.getModality());
		seriesInstance.setSeriesNumber(Integer.parseInt(dicomInstanceMessage.getSeriesNumber()));
		
		Instance instance1 = new Instance();
		instance1.setSopInstanceUid(instanceUid);
		instance1.setSeriesInstanceUid(seriesUid);
		instance1.setIsOk(2);
		instance1.setProcessResult(dicomInstanceMessage.getProcessedStatus());
		instance1.setInstanceNumber(Integer.parseInt(dicomInstanceMessage.getInstanceNumber()));
		instance1.setCharacterSet(dicomInstanceMessage.getCharacterSet());
		instance1.setTransferSyntaxUid(dicomInstanceMessage.getTransferSyntaxUID());
		instance1.setSopClassUid(dicomInstanceMessage.getsOPClassUID());
		instance1.setWw(dicomInstanceMessage.getWw());
		instance1.setWl(dicomInstanceMessage.getWl());
		instance1.setDcmFileStorageLocation(dicomInstanceMessage.getDcmFileStorageLocation());
		instance1.setFrameIndex(dicomInstanceMessage.getFrameIndex());
		
		GeneratePathStrategyInterface gsi = new PicturePathStrategyA();
		GeneratePath generatePath = new GeneratePath(gsi);
		String instancePath = generatePath.getPath(sessionId, instanceUid, seriesUid, studyUid);
		instance1.setInstancePath(instancePath);
		
		if (instance == null) {
			//构建3级树
			if (studyDao.findCountByStudyUid(studyUid) == 0) {
				studyDao.insertStudyDao(study);
			} else {
				studyDao.updateStudy(study);
			}
			
			if (seriesInstanceDao.findCountBySeriesUid(seriesUid) == 0) {
				seriesInstanceDao.insertSeriesInstance(seriesInstance);
			} else {
				seriesInstanceDao.updateSeriesInstance(seriesInstance);
			}
			
			instanceDao.insertInstance(instance1);
			
			//session已经存在
			if (sessionInstanceDao.findCountByInstanceUidAndSessionUid(sessionId, instanceUid) > 0) {
				Map<String, Object> params = new HashMap<>();
				params.put("isOk", 2);
				params.put("processResult", dicomInstanceMessage.getProcessedStatus());
				params.put("sopInstanceUid", instanceUid);
				params.put("sessionId", sessionId);
				//修改状态
				sessionInstanceDao.updateIsOkAndResultProcessBySessionUidAndInstanceUid(params);
				select = 1;
			} 
		} else {
			//instance已经存在, session列表在url之前
			studyDao.updateStudy(study);
			seriesInstanceDao.updateSeriesInstance(seriesInstance);
		
			instanceDao.updateInstance(instance1);
			
			//修改中间表的状态
			if (sessionInstanceDao.findCountByInstanceUidAndSessionUid(sessionId, instanceUid) > 0) {
				Map<String, Object> params = new HashMap<>();
				params.put("isOk", 2);
				params.put("processResult", dicomInstanceMessage.getProcessedStatus());
				params.put("sopInstanceUid", instanceUid);
				params.put("sessionId", sessionId);
				//修改状态
				sessionInstanceDao.updateIsOkAndResultProcessBySessionUidAndInstanceUid(params);
				select = 1;
			} 
			
		}
		return new Result(true, "添加成功!", select);
	}

	/**
	 *session关闭后, 还没有上传成功的instance 
	 */
	@Override
	public List<Map<String, Object>> findInstanceListBySessionId(String sessionId) {
		return instanceDao.findInstanceListBySessionId(sessionId);
	}

	@Override
	public long findInstanceNoSuccessCountBySessionId(String sessionId) {
		return instanceDao.findInstanceNoSuccessCountBySessionId(sessionId);
	}
	
	
	




}





