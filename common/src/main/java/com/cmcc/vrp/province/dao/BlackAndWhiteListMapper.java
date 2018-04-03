package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.BlackAndWhiteList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BlackAndWhiteListMapper.java
 */
public interface BlackAndWhiteListMapper {

    /** 
     * 利用手机号获取对象
     * 用list表示，防止同一活动搜索出多个手机号码，增强容错处理
     */
    List<BlackAndWhiteList> get(@Param("phone") String phone, @Param("activityId") Long activityId);

    /** 
     * 插入对象
     * @Title: insert 
     */
    int insert(BlackAndWhiteList blackAndWhiteList);

    /** 
     * 根据手机号和活动id删除名单对象
     * @Title: delete 
     */
    int delete(@Param("phone") String phone, @Param("activityId") Long activityId);

    /** 
     * 根据活动id删除名单对象
     * @Title: deleteByActivityId 
     */
    int deleteByActivityId(Long activityId);

    /** 
     * 根据活动id计算该活动有多少未删除的手机号码
     * @Title: countByActivityId 
     */
    int countByActivityId(Long activityId);

    /** 
     * 根据ID删除名单对象
     * @Title: deleteById 
     */
    int deleteById(Long id);


    /** 
     * @Title: update 
     */
    int update(@Param("phone") String phone, @Param("activityId") Long activityId);

    /** 
     * @Title: updateBatch 
     */
    int updateBatch(List<BlackAndWhiteList> list);

    /** 
     * 批量插入
     * @Title: insertBatch 
     */
    int insertBatch(List<BlackAndWhiteList> list);

    /*
     * 根据活动id查询所有未删除名单手机号
     */
    List<BlackAndWhiteList> getPhonesByActivityId(Long activityId);

    /*
     * 根据活动id查询未删除所有名单手机号
     */
    List<BlackAndWhiteList> getDelPhonesByActivityId(Long activityId);

    /** 
     * 批量删除
     * @Title: batchDelete 
     */
    int batchDelete(List<String> phonelist, Long activityId);

    /** 
     * 批量将已删除修改成未删除
     * @Title: batchUpdate 
     */
    int batchUpdate(List<String> phonelist, Long activityId);

}
