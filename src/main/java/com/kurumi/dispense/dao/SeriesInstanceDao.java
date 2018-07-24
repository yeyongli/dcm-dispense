package com.kurumi.dispense.dao;

import org.apache.ibatis.annotations.Param;

import com.kurumi.dispense.entity.SeriesInstance;

public interface SeriesInstanceDao {
	
	int insertSeriesInstance(SeriesInstance seriesInstance);
	
	long findCountBySeriesUid(String seriesUid);
	
	long findCountBySeriesUidAndStudyUid(@Param("seriesUid")String seriesUid, @Param("studyUid")String studyUid);
	
	int updateSeriesInstance(SeriesInstance seriesInstance);

}
