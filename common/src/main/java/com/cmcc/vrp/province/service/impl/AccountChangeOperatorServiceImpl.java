package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeOperatorMapper;
import com.cmcc.vrp.province.model.AccountChangeOperator;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @date 2016年12月2日 下午3:52:44
*/
@Service("accountChangeOperatorService")
public class AccountChangeOperatorServiceImpl implements AccountChangeOperatorService {
    @Autowired
    AccountChangeOperatorMapper mapper;

    @Override
    public boolean insert(AccountChangeOperator record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.insert(record) == 1;
    }

    @Override
    public AccountChangeOperator selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(AccountChangeOperator record) {
        if (record == null) {
            return false;
        }
        return mapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<AccountChangeOperator> selectByMap(Map map) {
        // TODO Auto-generated method stub
        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        return mapper.selectByMap(map);
    }

    @Override
    public Long countByMap(Map map) {
        // TODO Auto-generated method stub
        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        return mapper.countByMap(map);
    }

    @Override
    public boolean deleteBySerialNum(String serialNum) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(serialNum)) {
            return false;
        }
        return mapper.deleteBySerialNum(serialNum) == 1;
    }

}
