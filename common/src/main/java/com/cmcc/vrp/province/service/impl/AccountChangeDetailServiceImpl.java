package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeDetailMapper;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountChangeDetailService")
public class AccountChangeDetailServiceImpl implements AccountChangeDetailService {
    @Autowired
    AccountChangeDetailMapper mapper;


    @Override
    public boolean insert(AccountChangeDetail record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.insert(record) == 1;
    }

    @Override
    public AccountChangeDetail selectByRequestId(Long requestId) {
        // TODO Auto-generated method stub
        if (requestId == null) {
            return null;
        }
        return mapper.selectByRequestId(requestId);
    }

}
