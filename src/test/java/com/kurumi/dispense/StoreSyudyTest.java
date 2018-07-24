package com.kurumi.dispense;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.kurumi.dispense.entity.Instance;
import com.kurumi.dispense.entity.SeriesInstance;
import com.kurumi.dispense.entity.Study;
import com.kurumi.dispense.service.StudyService;
import com.kurumi.dispense.util.ApplicationContextUtil;
import com.kurumi.dispense.util.FileUtil;

public class StoreSyudyTest {
	private static final Logger log = LoggerFactory.getLogger(StoreSyudyTest.class);
	//series初始值的编号
	private static Integer seriesInitNumber = 0;
	//instance初始值的编号
	private static Integer instanceIintNumber = 0;
	//series文件名的长度
	private static Integer seriesFileNameLength = 3;
	//instance文件名的长度
	private static Integer instanceFileNameLength = 5;
	//生成dicom图片保存的路径
	private static String baseDir = "C:/Users/h2oco2/Desktop/dicom";
	//原来dicom图片保存的路径
	private static String dicomPictureBaseDir = "D:/dicom/dicom_picture2";
	
	@Test
	public void test() {
		try {
			ApplicationContext context = ApplicationContextUtil.getApplicationContext();
			StudyService studyService = (StudyService) context.getBean("studyService");
			//通过sessionId查找所有的study
			List<Study> studyList = studyService.findStudyListBySessionId("4d9ef4b6-ff19-48ed-98ff-33f364cbbfd4");
			
			if (studyList != null && studyList.size() > 0) {
				for (int i = 0; i < studyList.size(); i++) {
					Study study = studyList.get(i);
					String studyDir = baseDir + "/" + study.getStudyInstanceUid();
					FileUtil.createDir(studyDir);
					
					List<SeriesInstance> seriesList = study.getSeriesList();
					if (seriesList != null && seriesList.size() > 0) {
						for (int j = 0; j < seriesList.size(); j++) {
							SeriesInstance seriesInstance = seriesList.get(j);
							String seriesUid = seriesInstance.getSeriesInstanceUid();
							String seriesNumber = generateSeriesNumber(i);
							//String instanceFileName = generateInstanceNumber(seriesNumber, j);
							
							List<Instance> instanceList = seriesInstance.getInstanceList();
							createInstanceFile(studyDir, seriesNumber, instanceList);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 在某个study文件夹下面存放所有dicom转换后的图片
	 * 存储格式: series(3位) + instance(4位)为图片的文件名,有序的排列
	 * 
	 * @param seriesUid
	 * @param instance
	 */
	private static void createInstanceFile(String studyDir, String seriesNumber, List<Instance> instanceList) {
		try {
			if (instanceList != null && instanceList.size() > 0) {
				for (int i = 0; i < instanceList.size(); i++) {
					Instance instance = instanceList.get(i);
					//获取图片的存储路径
					String picturePath = dicomPictureBaseDir + "/" + instance.getInstancePath();
					String suffixName = ""; 
							
					if (StringUtils.isNotBlank(picturePath) && picturePath.lastIndexOf(".") != -1) {
						suffixName = picturePath.substring(picturePath.lastIndexOf("."));
						//获取instance的编号
						String instanceNumber = generateInstanceNumber(seriesNumber, i);
						String newPicturePath = studyDir + "/" + instanceNumber + suffixName;
						
						boolean flag = FileUtil.copyFile1(picturePath, newPicturePath);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 生成instance编号
	 * 
	 * @param sereisNumber
	 * @param instanceNumber
	 * @return
	 */
	private static String generateInstanceNumber(String sereisNumber, Integer instanceNumber) {
		String number = "";
		
		try {
				number = (instanceNumber + instanceIintNumber) + "";
				
				if (number.length() < instanceFileNameLength) {
					int fillLength = instanceFileNameLength - number.length();
					
					for (int i = 0; i < fillLength; i++) {
						number = "0" + number;
					}
				}
				
				number = sereisNumber + number;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-------生成instance编号异常!----------");
		}
		return number;
	}
	
	
	/**
	 * 生成序列编号
	 * 
	 * @return
	 */
	private static String generateSeriesNumber(Integer seriesNumber) {
		String number = "";
		
		try {
			number = (seriesNumber + seriesInitNumber) + "";
			
		    if (number.length() < seriesFileNameLength) {
				 int fillLength = seriesFileNameLength - number.length();
					
				 for (int i = 0; i < fillLength; i++) {
						number = "0" + number;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("-------生成序列编号异常!----------");
		}
		
		return number;
	}
	
	
	

}
