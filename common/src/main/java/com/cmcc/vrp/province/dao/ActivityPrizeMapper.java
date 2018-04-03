package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityPrize;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ActivityPrizeMapper
 */
public interface ActivityPrizeMapper {
    /**
     * @param id
     * @return
     * @Title: deleteByPrimaryKey
     * @Description: 根据主键ID逻辑删除记录
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     * @Title: insert
     * @Description: 插入记录，要求所有字段不为空
     * @return: int
     */
    int insert(ActivityPrize record);

    /**
     * @param id
     * @return
     * @Title: selectByPrimaryKey
     * @Description: 根据主键ID查询未删除的记录
     * @return: ActivityPrize
     */
    ActivityPrize selectByPrimaryKey(Long id);
    
    /**
     * @param id
     * @return
     * @Title: selectPrizeDetailByPrimaryKey
     * @Description: 根据主键ID查询未删除的奖品信息详情
     * @return: ActivityPrize
     */
    ActivityPrize selectPrizeDetailByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     * @Title: updateByPrimaryKey
     * @Description: 根据主键ID更新记录，要求所有字段不能为空
     * @return: int
     */
    int updateByPrimaryKey(ActivityPrize record);

    /**
     * @param record
     * @return
     * @Title: updateCountByPrimaryKey
     * @Description: 根据主键ID更新产品个数
     * @return: int
     */
    int updateCountByPrimaryKey(ActivityPrize record);

    /**
     * @param activityId
     * @return
     * @Title: selectByActivityId
     * @Description: 根据活动ID查询未删除的记录
     * @return: List<ActivityPrize>
     */
    List<ActivityPrize> selectByActivityId(String activityId);

    /**
     * @param activityId
     * @return
     * @Title: selectByActivityIdForIndividual
     * @Description: 根据活动ID查询未删除的记录(用于集中平台)
     * @return: List<ActivityPrize>
     */
    List<ActivityPrize> selectByActivityIdForIndividual(String activityId);

    /**
     * @param activityId
     * @return
     * @Title: getDetailByActivityId
     * @Description: 根据活动ID获取奖项的详细信息，包括企业名称、产品编码、产品名称等
     * @return: List<ActivityPrize>
     */
    List<ActivityPrize> getDetailByActivityId(String activityId);

    /**
     * @param activityId
     * @return
     * @Title: deleteByActivityId
     * @Description: 根据活动ID逻辑删除
     * @return: boolean
     */
    int deleteByActivityId(String activityId);

    /**
     * @param map
     * @return
     * @Title: batchInsert
     * @Description: 批量生产记录，要求所有字段不能为空
     * @return: boolean
     */
    int batchInsert(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @Title: batchInsertForCrowdFunding
     * @Description: 
     * @return: boolean
     */
    int batchInsertForCrowdFunding(Map<String, Object> map);
    
    /** 
     * @Title: deleteActivityPrize 
     */
    int deleteActivityPrize(@Param("delProdIds") List<Long> delProdIds, @Param("activityId") String activityId);

    /**
     * 插入红包
     *
     * @param activityPrize
     * @author qinqinyan
     */
    int insertForRedpacket(ActivityPrize activityPrize);

    /**
     * 批量更新活动奖品信息
     * @param map
     * @author qinqinyan
     * */
    int batchUpdateSelective(Map<String, Object> map);
    
    /**
     * 批量更新折扣，用于广东流量众筹
     * @param map
     * @author qinqinyan
     * */
    int batchUpdateDiscount(Map<String, Object> map);
}