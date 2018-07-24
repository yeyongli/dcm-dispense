package com.kurumi.dispense.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurumi.dispense.InstanceUrl;
import com.kurumi.dispense.SessionMessage;
import com.kurumi.dispense.dao.InstanceDao;
import com.kurumi.dispense.dao.SeriesInstanceDao;
import com.kurumi.dispense.dao.SessionDao;
import com.kurumi.dispense.dao.SessionInstanceDao;
import com.kurumi.dispense.dao.StudyDao;
import com.kurumi.dispense.entity.Instance;
import com.kurumi.dispense.entity.SeriesInstance;
import com.kurumi.dispense.entity.Session;
import com.kurumi.dispense.entity.SessionInstance;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.SessionService;
import com.kurumi.dispense.strategy.GeneratePath;
import com.kurumi.dispense.strategy.GeneratePathStrategyInterface;
import com.kurumi.dispense.strategy.PicturePathStrategyA;
import com.kurumi.dispense.util.SuidToHash;

@Service("sessionService")
public class SessionServiceImpl implements SessionService{
	@Autowired
	private SessionDao sessionDao;
	@Autowired
	private InstanceDao instanceDao;
	@Autowired
	private SeriesInstanceDao seriesInstanceDao;
	@Autowired
	private StudyDao studyDao;
	@Autowired
	private SessionInstanceDao sessionInstanceDao;

	public void insertSession(SessionMessage sessionMessage) {
		Session session = new Session();
		session.setSessionId(sessionMessage.getSessionId());
		
		if(sessionDao.findSessionBySessionId(sessionMessage.getSessionId()) == 0) {
			sessionDao.insertSession(session);
		}
		
		List<InstanceUrl> list = sessionMessage.getInstanceUrlList();
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				InstanceUrl instanceUrl = list.get(i);
				Instance instance = instanceDao.findInstanceByInstanceUid(instanceUrl.getsOPInstanceUID());
				
				if (instance == null) {
					//构建3级树
					if (studyDao.findCountByStudyUid(instanceUrl.getStudyInstanceUID()) == 0) {
						Study study = new Study();
						study.setStudyInstanceUid(instanceUrl.getStudyInstanceUID());
						//检查类型
						study.setModality(instanceUrl.getModality());
						//检查日期
						study.setStudyDate(instanceUrl.getStudyDate());
						studyDao.insertStudyDao(study);
					}
					
					if (seriesInstanceDao.findCountBySeriesUid(instanceUrl.getSeriesInstanceUID()) == 0) {
						SeriesInstance seriesInstance = new SeriesInstance();
						seriesInstance.setSeriesInstanceUid(instanceUrl.getSeriesInstanceUID());
						seriesInstance.setStudyInstanceUid(instanceUrl.getStudyInstanceUID());
						//序列类别:CT..
						seriesInstance.setModality(instanceUrl.getModality());
						//序列编号
						seriesInstance.setSeriesNumber(instanceUrl.getSeriesNumber());
						
						seriesInstanceDao.insertSeriesInstance(seriesInstance);
					}
					
					Instance instance1 = new Instance();
					instance1.setSopInstanceUid(instanceUrl.getsOPInstanceUID());
					instance1.setSeriesInstanceUid(instanceUrl.getSeriesInstanceUID());
					//实例编号
					instance1.setInstanceNumber(instanceUrl.getInstanceNumber());
					instance1.setIsOk(1);
					//图片保存的路径
					GeneratePathStrategyInterface generatePathStrategy = new PicturePathStrategyA();
					GeneratePath generatePath = new GeneratePath(generatePathStrategy);
					String instancePath = generatePath.getPath(sessionMessage.getSessionId(), instanceUrl.getsOPInstanceUID(), 
							instanceUrl.getSeriesInstanceUID(), instanceUrl.getStudyInstanceUID()); 
					instance1.setInstancePath(instancePath);
					
					instanceDao.insertInstance(instance1);
					
					SessionInstance sessionInstance = new SessionInstance();
					sessionInstance.setIsOk(1);
					sessionInstance.setSessionId(session.getSessionId());
					sessionInstance.setSopInstanceUid(instanceUrl.getsOPInstanceUID());
					
					sessionInstanceDao.insertSessionInstance(sessionInstance);
				} else {
					if (sessionInstanceDao.findCountByInstanceUidAndSessionUid(session.getSessionId(), instanceUrl.getsOPInstanceUID()) == 0) {
						//中间表没有数据
						SessionInstance sessionInstance = new SessionInstance();
						sessionInstance.setSessionId(session.getSessionId());
						sessionInstance.setSopInstanceUid(instanceUrl.getsOPInstanceUID());
						sessionInstance.setIsOk(instance.getIsOk());
						sessionInstance.setProcessResult(instance.getProcessResult());
						
						sessionInstanceDao.insertSessionInstance(sessionInstance);
					} else {
						//中间表有数据更新状态值
						Map<String, Object> params = new HashMap<>();
						params.put("isOk", instance.getIsOk());
						params.put("processResult", instance.getProcessResult());
						params.put("sopInstanceUid", instance.getSopInstanceUid());
						params.put("sessionId", session.getSessionId());
						
						sessionInstanceDao.updateIsOkAndResultProcessBySessionUidAndInstanceUid(params);
					}
				}
			}
		}
	}

	@Override
	public long findSessionBySessionId(String sessionId) {
		return sessionDao.findSessionBySessionId(sessionId);
	}
	
	
	
	
	
	

	
	
}



