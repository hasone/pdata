package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动审批mapper
 */
public interface ActivityApprovalDetailMapper {
    /**
     * 根据主键ID删除记录
     *
     * @param id 主键ID
     * @return 删除成功的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入活动审批记录
     *
     * @param record 记录对象
     * @return 插入成功的记录数量
     */
    int insert(ActivityApprovalDetail record);

    /**
     * 选择性插入活动审批记录
     *
     * @param record 活动审批记录
     * @return 插入成功的记录数量
     */
    int insertSelective(ActivityApprovalDetail record);

    /**
     * 根据主键ID获取活动审批记录
     *
     * @param id 主键ID
     * @return 获取的活动审批记录对象
     */
    ActivityApprovalDetail selectByPrimaryKey(Long id);

    /**
     * 更新活动审批记录
     *
     * @param record 活动审批记录对象
     * @return 更新成功的活动审批记录数量
     */
    int updateByPrimaryKeySelective(ActivityApprovalDetail record);

    /**
     * 根据主键ID更新活动审批记录
     *
     * @param record 活动审批记录对象
     * @return 更新成功的活动审批记录数量
     */
    int updateByPrimaryKey(ActivityApprovalDetail record);

    /**
     * 根据请求ID获取活动审批详情
     *
     * @param requestId 请求ID
     * @return 活动审批记录详情
     */
    ActivityApprovalDetail selectByRequestId(@Param("requestId") Long requestId);

    /**
     * 根据活动ID获取相应的审批记录详情
     *
     * @param activityId 活动ID
     * @return 相应的审批记录详情列表
     */
    List<ActivityApprovalDetail> selectByActivityId(@Param("activityId") String activityId);
}