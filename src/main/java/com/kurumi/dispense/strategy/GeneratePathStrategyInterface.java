package com.kurumi.dispense.strategy;

public interface GeneratePathStrategyInterface {
	public String getInstancePicturePath(String sessionId, String instanceUid, 
			String seriesUid, String studyUid);
	

}
