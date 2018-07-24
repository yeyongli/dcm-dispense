package com.kurumi.dispense;

import java.util.List;

/**
 * session文件的信息
 * @author yeyongli
 *
 */
public class SessionMessage {
	private String sessionId;
	private List<InstanceUrl> instanceUrlList;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public List<InstanceUrl> getInstanceUrlList() {
		return instanceUrlList;
	}
	public void setInstanceUrlList(List<InstanceUrl> instanceUrlList) {
		this.instanceUrlList = instanceUrlList;
	}

	

	
	
}
