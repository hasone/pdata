package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ActivityCreator;

/**
 * ActivityCreatorMapper
 */
public interface ActivityCreatorMapper {
    /**
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     */
    int insert(ActivityCreator record);

    /**
     * 
     */
    int insertSelective(ActivityCreator record);

    /**
     * 
     */
    ActivityCreator selectByPrimaryKey(Long id);

    /**
     * 
     */
    int updateByPrimaryKeySelective(ActivityCreator record);

    /**
     * 
     */
    int updateByPrimaryKey(ActivityCreator record);
    
    /**
     * 
     * @param activityId
     * @param activityType
     * @return
     */
    List<ActivityCreator> selectByActId(@Param("activityId") Long activityId,@Param("activityType") Integer activityType);
}