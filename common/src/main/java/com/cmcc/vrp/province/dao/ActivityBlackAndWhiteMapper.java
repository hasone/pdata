package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityBlackAndWhite;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:ActivityBlackAndWhiteMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
*/
public interface ActivityBlackAndWhiteMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: TODO
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    int insert(ActivityBlackAndWhite record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    int insertSelective(ActivityBlackAndWhite record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */ 
    ActivityBlackAndWhite selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */ 
    int updateByPrimaryKeySelective(ActivityBlackAndWhite record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: TODO
    */ 
    int updateByPrimaryKey(ActivityBlackAndWhite record);

    /**
    * @Title: selectPhonesByMap
    * @Description: TODO
    */ 
    List<String> selectPhonesByMap(Map map);

    /**
    * @Title: batchInsert
    * @Description: TODO
    */ 
    int batchInsert(@Param("records") List<ActivityBlackAndWhite> records);

    /**
    * @Title: deleteByActivityId
    * @Description: TODO
    */ 
    int deleteByActivityId(@Param("activityId") String activityId);

    /**
    * @Title: updateIsWhiteByActivityId
    * @Description: TODO
    */ 
    int updateIsWhiteByActivityId(@Param("activityId") String activityId, @Param("isWhite") Integer isWhite);
}