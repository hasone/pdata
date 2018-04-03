package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MonthlyPresentRecordCopyMapper;
import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;
import com.cmcc.vrp.province.service.MonthlyPresentRecordCopyService;

/**   
* @Title: MonthlyPresentRecordCopyServiceImpl.java 
* @Package com.cmcc.vrp.province.service.impl 
* @Description: 
* @author lgk8023   
* @date 2017年11月14日 下午5:11:33 
* @version V1.0   
*/
@Service
public class MonthlyPresentRecordCopyServiceImpl implements MonthlyPresentRecordCopyService {
    private static Logger LOGGER = LoggerFactory.getLogger(MonthlyPresentRecordCopyServiceImpl.class);
    @Autowired
    MonthlyPresentRecordCopyMapper monthlyPresentRecordCopyMapper;
    @Override
    public boolean batchInsert(List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList) {

        if (monthlyPresentRecordCopyList == null
                || monthlyPresentRecordCopyList.size() == 0) {
            LOGGER.error("参数错误");
            return false;
        }
        return monthlyPresentRecordCopyMapper.batchInsert(monthlyPresentRecordCopyList) == monthlyPresentRecordCopyList.size();
    }
    @Override
    public List<MonthlyPresentRecordCopy> getByRuleId(Long ruleId) {
        return monthlyPresentRecordCopyMapper.getByRuleId(ruleId);
    }

}
