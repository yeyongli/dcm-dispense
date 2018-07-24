package com.kurumi.dispense.strategy;

import com.kurumi.dispense.util.SuidToHash;

/**
 * 图片的路径生成策略
 * @author yeyongli
 *
 */
public class PicturePathStrategyA implements GeneratePathStrategyInterface{

	@Override
	public String getInstancePicturePath(String sessionId, String instanceUid, String seriesUid, String studyUid) {
		String picturePath = SuidToHash.getHash(sessionId) + "/" + 
				sessionId + "/" + studyUid + 
				"/" + seriesUid + "/" + instanceUid+ ".jpg";
		return picturePath;
	}

	
	
	
}
