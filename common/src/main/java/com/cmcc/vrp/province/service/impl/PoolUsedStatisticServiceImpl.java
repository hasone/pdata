package com.cmcc.vrp.province.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.PoolUsedStatisticMapper;
import com.cmcc.vrp.province.model.PoolUsedStatistic;
import com.cmcc.vrp.province.service.PoolUsedStatisticService;

/**
 * 流量池使用统计使用服务类
 * <p>
 * Created by qihang on 2016/12/06.
 */
@Service
public class PoolUsedStatisticServiceImpl implements PoolUsedStatisticService {

    @Autowired
    PoolUsedStatisticMapper poolUsedStatisticMapper;

    @Override
    public PoolUsedStatistic getById(Long id) {
        return poolUsedStatisticMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return poolUsedStatisticMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insertSelective(PoolUsedStatistic record) {
        return poolUsedStatisticMapper.insertSelective(record) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(PoolUsedStatistic record) {
        return poolUsedStatisticMapper.updateByPrimaryKeySelective(record) > 0;
    }

    /**
     * GROUP BY mobile, enter_code, product_code
     */
    @Override
    public List<PoolUsedStatistic> getStatisticByTime(String startTime,
            String endTime) {
        return poolUsedStatisticMapper.getStatisticByTime(startTime, endTime);
    }

    @Override
    public Map<String, Long> getUsedFlowByTime(String startTime, String endTime) {
        List<PoolUsedStatistic> list = poolUsedStatisticMapper
                .getStatisticByTime(startTime, endTime);

        Map<String, Long> map = new HashMap<String, Long>();

        for (PoolUsedStatistic statistic : list) {
            String key = statistic.getMobile() + "_" + statistic.getEnterCode()
                    + "_" + statistic.getProductCode();
            map.put(key, statistic.getUsedAmount());
        }
        return map;
    }

    @Override
    public boolean deleteByDate(String date) {
        return poolUsedStatisticMapper.deleteByDate(date) > 0;
    }

}
