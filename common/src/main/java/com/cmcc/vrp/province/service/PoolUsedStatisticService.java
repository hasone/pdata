package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.PoolUsedStatistic;

/**
 * 流量池使用统计使用服务类
 * <p>
 * Created by qihang on 2016/12/06.
 */
public interface PoolUsedStatisticService {
    
    /**
     * 查询
     */
    PoolUsedStatistic getById(Long id);
    
    /**
     * 按主键删除
     */
    boolean deleteByPrimaryKey(Long id);

    /**
     * 插入
     */
    boolean insertSelective(PoolUsedStatistic record);

    /**
     * 更新
     */
    boolean updateByPrimaryKeySelective(PoolUsedStatistic record);

    /**
     * 跟据起始时间和结束时间，计算手机号使用的总额 startTime 和 endTime的格式为YYYYMMDD
     */
    List<PoolUsedStatistic> getStatisticByTime(String startTime, String endTime);
    
    /**
     * 跟据起始时间和结束时间，计算手机号使用的总额 startTime 和 endTime的格式为YYYYMMDD
     * 
     * @return Map<String, Long>  key值格式为  mobile_enterCode_prdcode , value值为使用流量
     */
    Map<String, Long> getUsedFlowByTime(String startTime, String endTime);

    /**
     * 删除某一天的所有记录,date格式为YYYYMMDD
     */
    boolean deleteByDate(String date);
}
