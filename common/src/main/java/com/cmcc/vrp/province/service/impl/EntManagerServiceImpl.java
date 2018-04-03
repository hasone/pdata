package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.province.dao.EntManagerMapper;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EntManagerServiceImpl implements EntManagerService {
    @Autowired
    EntManagerMapper mapper;

    @Override
    public boolean insertEntManager(EntManager entManager) {
        entManager.setCreateTime(new Date());
        entManager.setUpdateTime(new Date());
        entManager.setDeleteFlag(Constants.UNDELETED_FLAG);
        return mapper.insert(entManager) == 1;
    }

    @Override
    public Long getManagerIdForEnter(Long enterId) {
        return mapper.getManagerIdForEnter(enterId);
    }

    @Override
    public Manager getManagerForEnter(Long enterId) {
        return mapper.getManagerForEnter(enterId);
    }

    @Override
    public Manager getManagerForEnterCode(String enterCode) {
        return mapper.getManagerForEnterCode(enterCode);
    }

    @Override
    public List<EntManager> selectByManagerId(Long managerId) {
        return mapper.selectByManagerId(managerId);
    }
}
