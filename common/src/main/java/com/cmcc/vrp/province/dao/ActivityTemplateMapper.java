package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityTemplate;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:ActivityTemplateMapper
 * @Description:todo
 * */
public interface ActivityTemplateMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:todo
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:todo
     * */
    int insert(ActivityTemplate record);

    /**
     * @Title:insertSelective
     * @Description:todo
     * */
    int insertSelective(ActivityTemplate record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:todo
     * */
    ActivityTemplate selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:todo
     * */
    int updateByPrimaryKeySelective(ActivityTemplate record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:todo
     * */
    int updateByPrimaryKey(ActivityTemplate record);

    /**
     * @Title:selectByActivityId
     * @Description:todo
     * */
    ActivityTemplate selectByActivityId(@Param("activityId") String activityId);
}