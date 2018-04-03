package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.IndividualAccountRecordMapper;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualProductService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @date 2017年4月19日 上午10:12:09
*/
@Service("individualAccountRecord")
public class IndividualAccountRecordServiceImpl implements IndividualAccountRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualAccountRecordServiceImpl.class);

    @Autowired
    IndividualAccountRecordMapper mapper;
        
    @Autowired
    AdministerService administerService;
    
    @Autowired
    IndividualProductService individualProductService;

    @Override
    public boolean create(IndividualAccountRecord record) {
        if (!validate(record)) {
            return false;
        }
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        return mapper.insert(record) == 1;
    }

    private boolean validate(IndividualAccountRecord ar) {
        boolean flag = ar != null && ar.getAdminId() != null && ar.getOwnerId() != null && ar.getAccountId() != null
                && ar.getType() != null && ar.getActivityType() != null && StringUtils.isNotBlank(ar.getSerialNum());
        if (!flag) {
            LOGGER.info("校验个人帐户记录，返回false，AccountRecord = {}.", JSONObject.toJSONString(ar));
        }
        return flag;
    }

    @Override
    public boolean updateByPrimaryKeySelective(IndividualAccountRecord record) {
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public List<IndividualAccountRecord> selectByMap(Map<String, Object> map) {
        return mapper.selectByMap(map);
    }

    /** 
     * @Title: selectAccumulateAccount 
     * @param accountId
     * @return
     * @Author: wujiamin
     * 获取累积的流量币（积分），累积流量币不包含类型是流量币兑换的收入
    */
    @Override
    public BigDecimal selectAccumulateAccount(Long accountId) {
        return mapper.selectAccumulateAccount(accountId);
    }
    
    @Override
    public int countDetailRecordByMap(Map<String, Object> map) {
        List<String> mobiles = new ArrayList<String>();
        //根据openid获取手机号，加入mobiles
        //根据nickname获取手机号列表，加入mobiles
        if(map.get("mobile") != null){
            mobiles.add((String)map.get("mobile"));
        }
        map.put("mobiles", mobiles);
        map.put("individualAccountType", IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        map.put("individualProductId", product.getId());
        return mapper.countDetailRecordByMap(map);
    }

    @Override
    public List<IndividualAccountRecord> selectDetailRecordByMap(Map<String, Object> map) {
        List<String> mobiles = new ArrayList<String>();
        //根据openid获取手机号，加入mobiles
        //根据nickname获取手机号列表，加入mobiles
        if(map.get("mobile") != null){
            mobiles.add((String)map.get("mobile"));
        }
        map.put("mobiles", mobiles);
        map.put("individualAccountType", IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        map.put("individualProductId", product.getId());
        return mapper.selectDetailRecordByMap(map);
    }
}
