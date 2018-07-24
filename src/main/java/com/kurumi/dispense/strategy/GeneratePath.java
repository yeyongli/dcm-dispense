package com.kurumi.dispense.strategy;

public class GeneratePath{
	private GeneratePathStrategyInterface generatePathStrategy;
	
	public GeneratePath(GeneratePathStrategyInterface generatePathStrategy) {
		this.generatePathStrategy = generatePathStrategy;
	}
	
	public String getPath(String sessionId, String instanceUid, String seriesUid, String studyUid) {
		return generatePathStrategy.getInstancePicturePath(sessionId, instanceUid, seriesUid, studyUid);
	}
	
	
	
	
	
	
	
	
	
	
	
}
