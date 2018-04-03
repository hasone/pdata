package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Activities;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:ActivitiesMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public interface ActivitiesMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: TODO
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    int insert(Activities record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    int insertSelective(Activities record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */ 
    Activities selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */ 
    int updateByPrimaryKeySelective(Activities record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: TODO
    */ 
    int updateByPrimaryKey(Activities record);

    /**
    * @Title: selectByActivityId
    * @Description: TODO
    */ 
    Activities selectByActivityId(String activityId);

    /**
     * @Title:selectActivityListByActivityId
     * */
    List<Activities> selectActivityListByActivityId(String activityId);

    /**
    * @Title: selectByMap
    * @Description: TODO
    */ 
    List<Activities> selectByMap(Map map);

    /**
     * @Title: selectByMapForActivityTemplate
     * @Description: TODO
     */
    List<Activities> selectByMapForActivityTemplate(Map map);

    /**
    * @Title: countByMap
    * @Description: TODO
    */ 
    Long countByMap(Map map);

    /**
    * @Title: selectByMapForRedpacket
    * @Description: TODO
    */ 
    List<Activities> selectByMapForRedpacket(Map map);

    /**
    * @Title: countByMapForRedpacket
    * @Description: TODO
    */ 
    Long countByMapForRedpacket(Map map);

    /**
    * @Title: changeStatus
    * @Description: TODO
    */ 
    int changeStatus(@Param("activityId") String activityId, @Param("status") Integer status);

    /**
    * @Title: selectByEntId
    * @Description: TODO
    */ 
    List<Activities> selectByEntId(Map map);

    /**
    * @Title: batchChangeStatus
    * @Description: TODO
    */ 
    int batchChangeStatus(Map map);

    /** 
     * @Title: selectMyCrowdfundingActivityList 
     */
    List<Activities> selectMyCrowdfundingActivityList(Map map);
    /** 
     * @Title: selectWxCrowdfundingActivityList 
     */
    List<Activities> selectWxCrowdfundingActivityList(Map map);
    /**
     * @Title: selectByMapForGDCrowdFunding
     * @Description: TODO
     */
    List<Activities> selectByMapForGDCrowdFunding(Map map);

    /**
     * @Title: countByMapGDCrowdFunding
     * @Description: TODO
     */
    Long countByMapGDCrowdFunding(Map map);

    /** 
     * @Title: selectForOrder 
     */
    List<Activities> selectForOrder(@Param("creatorId")Long creatorId, @Param("orderSystemNum")String orderSystemNum, 
            @Param("type")Integer type, @Param("status")Integer status);


    /** 
     * @Title: selectLastWxLottery 
     */
    List<Activities> selectLastWxLottery(Map map);

}
