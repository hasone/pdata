package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EntCallbackAddrMapper;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业回调地址服务
 * <p>
 * Created by sunyiwei on 2016/7/6.
 */
@Service
public class EntCallbackAddrServiceImpl implements EntCallbackAddrService {
    @Autowired
    private EntCallbackAddrMapper entCallbackAddrMapper;

    @Override
    public EntCallbackAddr get(Long entId) {
        return entId == null ? null : entCallbackAddrMapper.getByEntId(entId);
    }

    @Override
    public boolean delete(Long entId) {
        return entId != null && entCallbackAddrMapper.deleteByEntId(entId) >= 0;
    }

    @Override
    public boolean update(Long entId, String newCallbackAddr) {
        return entId != null && StringUtils.isNotBlank(newCallbackAddr)
                && entCallbackAddrMapper.update(entId, newCallbackAddr) == 1;
    }

    @Override
    public boolean insert(EntCallbackAddr entCallbackAddr) {
        if (entCallbackAddr != null) {
            return entCallbackAddrMapper.insert(entCallbackAddr) == 1;
        }
        return false;
    }
}
