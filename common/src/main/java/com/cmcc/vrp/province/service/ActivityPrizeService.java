package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:12:31
*/
public interface ActivityPrizeService {

    /**
     * @param id
     * @return
     * @Title: delete
     * @Description: 根据主键ID逻辑删除
     * @return: boolean
     */
    boolean delete(Long id);

    /**
     * @param activityId
     * @return
     * @Title: deleteByActivityId
     * @Description: 根据活动ID逻辑删除
     * @return: boolean
     */
    boolean deleteByActivityId(String activityId);

    /**
     * @param activityPrize
     * @return
     * @Title: insert
     * @Description: 生成记录，要求所有字段不能为空
     * @return: boolean
     */
    boolean insert(ActivityPrize activityPrize);

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
     * @Description: 根据主键ID查询未删除的奖品详情
     * @return: ActivityPrize
     */
    ActivityPrize selectPrizeDetailByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     * @Title: updateByPrimaryKey
     * @Description: 根据主键ID更新记录，要求所有字段不能为空
     * @return: boolean
     */
    boolean updateByPrimaryKey(ActivityPrize record);

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
     * @Title: selectByActivityIdForIndividual(用于集中平台)
     * @Description: 根据活动ID查询未删除的记录
     * @return: List<ActivityPrize>
     * @author qinqinyan
     */
    List<ActivityPrize> selectByActivityIdForIndividual(String activityId);

    /**
     * @param records
     * @return
     * @Title: batchInsert
     * @Description: 批量生产记录，要求所有字段不能为空
     * @return: boolean
     */
    boolean batchInsert(List<ActivityPrize> records);
    
    /**
     * @param records
     * @return
     * @Title: batchInsertForCrowdFunding
     * @Description: 
     * @return: boolean
     */
    boolean batchInsertForCrowdFunding(List<ActivityPrize> records);

    /**
     * @param activityId
     * @return
     * @Title: getDetailByActivityId
     * @Description: 根据活动ID获取奖项的详细信息，包括企业名称、产品编码、产品名称等
     * @return: List<ActivityPrize>
     */
    List<ActivityPrize> getDetailByActivityId(String activityId);

    /**
     * 批量插入奖品信息（流量券）
     *
     * @param ctMobileList
     * @param cmMobileList
     * @param cuProductId
     * @param activities
     * @param cmProductId
     * @param ctProductId
     * @param cuMobileList
     * @author qinqinyan
     */
    boolean batchInsertForFlowCard(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId,
                                   String cmMobileList, String cuMobileList, String ctMobileList);

    /**
     * 批量插入奖品信息（流量券）
     *
     * @param activities
     * @param cmProductId
     * @param cuProductId
     * @param ctProductId
     * @author qinqinyan
     */
    boolean batchInsertForQRcord(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId);

    /**
     * 删除奖品
     *
     * @param delProdIds
     * @param activityId
     * @author qinqinyan
     */
    boolean deleteActivityPrize(List<Long> delProdIds, String activityId);

    /**
     * 增加奖品
     *
     * @param activityId
     * @param addProdIds
     * @author qinqinyan
     */
    boolean addActivityPrize(List<Long> addProdIds, String activityId,
                             Long cmMobileCnt, Long cuMobileCnt, Long ctMobileCnt);

    /**
     * 更新奖项产品
     * @param updateProdIds
     * @param activityId
     * @param cmMobileCnt
     * @param ctMobileCnt
     * @param cuMobileCnt
     * @author qinqinyan
     **/
    boolean updateActivityPrize(List<Long> updateProdIds, String activityId,
                             Long cmMobileCnt, Long cuMobileCnt, Long ctMobileCnt);

    /**
     * 插入红包奖品
     *
     * @param activityPrize
     * @author qinqinyan
     */
    boolean insertForRedpacket(ActivityPrize activityPrize);

    /**
     * 批量更新奖品信息
     * @param activityPrizes
     * @author qinqinyan
     * */
    boolean batchUpdateSelective(List<ActivityPrize> activityPrizes);
    
    /**
     * 批量更新折扣信息
     * @author qinqinyan
     * */
    boolean batchUpdateDiscount(List<ActivityPrize> activityPrizes);

}
