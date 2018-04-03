package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityInfo;

/**
 * <p>Title:ActivityInfoMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
*/
public interface ActivityInfoMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: TODO
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    int insert(ActivityInfo record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    int insertSelective(ActivityInfo record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */ 
    ActivityInfo selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */ 
    int updateByPrimaryKeySelective(ActivityInfo record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: TODO
    */ 
    int updateByPrimaryKey(ActivityInfo record);

    /**
    * @Title: selectByActivityId
    * @Description: TODO
    */ 
    ActivityInfo selectByActivityId(String activityId);

    /**
    * @Title: insertForRedpacket
    * @Description: TODO
    */ 
    int insertForRedpacket(ActivityInfo record);

    /**
     * @Title: updateForRendomPacket
     * @Description: TODO
     */
    int updateForRendomPacket(ActivityInfo activityInfo);
}