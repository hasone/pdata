package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.UserGainRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:32:14
*/
public interface UserGainRecordMapper {
    /*
     * 插入新的用户记录
     */
    /**
     * @param record
     * @return
     */
    int insert(UserGainRecord record);

    /**
     * 根据用户Id获取用户记录
     */
    List<UserGainRecord> get(@Param("userId") Long userId,
                             @Param("sourceName") String sourceName);

    /**
     * 根据手机号获取记录
     */
    List<UserGainRecord> getByMobile(@Param("mobile") String mobile,
                                     @Param("sourceName") String sourceName);

    /**
     * 统计次数
     */
    Long count(@Param("mobile") String mobile,
               @Param("sourceName") String sourceName);

    /**
     * 根据用户ID删除记录， 逻辑删除
     */
    int delete(Long userId);

    /**
     * 根据手机号码删除记录，逻辑删除
     */
    int deleteByMobile(String mobile);

    /**
     * 当月用户在游戏中获赠的流量总数
     */
    Long monthAmount(@Param("mobile") String mobile,
                     @Param("sourceName") String sourceName);

    /**
     * 更改充值记录状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}