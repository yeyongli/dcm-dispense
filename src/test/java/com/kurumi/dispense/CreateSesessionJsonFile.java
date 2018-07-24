package com.kurumi.dispense;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.kurumi.dispense.entity.Session;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.StudyService;
import com.kurumi.dispense.util.ApplicationContextUtil;
import com.kurumi.dispense.util.FileUtil;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.PropsUtil;
import com.kurumi.dispense.util.SuidToHash;

/**
 *
 insert into sop_instance_transfer_session
(
sop_instance_uid,
session_id,
create_date,
is_ok,
process_result

)
select 
sop_instance_uid,
'cc2b05de-6110-47cb-a66e-9acf9eb3dd35',
now(),
2,
1
from sop_instance

 * @author h2oco2
 *
 */
public class CreateSesessionJsonFile {
	
	@Test
	public void test() {
		Properties properties = PropsUtil.loadProps("config.properties");
		String sessionId = "cc2b05de-6110-47cb-a66e-9acf9eb3dd35";
		ApplicationContext context = ApplicationContextUtil.getApplicationContext();
		StudyService studyService = (StudyService) context.getBean("studyService");
		List<Study> studyList = studyService.findStudyListBySessionId(sessionId);
		String sessionUrl = properties.getProperty("session.url");
		String sessionDir = sessionUrl + "/" + SuidToHash.getHash(sessionId);
		FileUtil.createDir(sessionDir);
		String sessionPath = sessionDir + "/" + sessionId + ".json";
		Map<String, Object> map = new HashMap<>();
		Session session = new Session();
		session.setSessionId(sessionId);
		map.put("studyList", studyList);
		map.put("session", session);
		
		String sessionJson = JsonUtils.objectToJson(map);
		FileUtil.writeFile(new File(sessionPath), sessionJson);
		
		
	}

}
