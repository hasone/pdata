package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.HistoryEnterpriseFileMapper;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qinqinyan on 2016/10/12.
 */
@Service("historyEnterpriseFileService")
public class HistoryEnterpriseFileServiceImpl implements HistoryEnterpriseFileService {

    @Autowired
    HistoryEnterpriseFileMapper mapper;

    @Override
    public boolean insertSelective(HistoryEnterpriseFile record) {
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public HistoryEnterpriseFile selectByRequestId(Long requestId) {
        if(requestId!=null){
            return mapper.selectByRequestId(requestId);
        }
        return null;
    }
}
