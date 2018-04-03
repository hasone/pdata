package com.cmcc.vrp.province.mdrc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MdrcBatchConfigStatusRecordMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;

/**
 * 
 * @ClassName: MdrcBatchConfigStatusRecordServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年8月16日 下午5:11:41
 */
@Service
public class MdrcBatchConfigStatusRecordServiceImpl implements MdrcBatchConfigStatusRecordService {
    /**
     * 
     */
    @Autowired
    MdrcBatchConfigStatusRecordMapper mdrcBatchConfigStatusRecordMapper;

    /**
     * 
     */
    @Override
    public boolean insertSelective(MdrcBatchConfigStatusRecord record) {
        return mdrcBatchConfigStatusRecordMapper.insertSelective(record) > 0;
    }

    /**
     * 
     */
    @Override
    public List<MdrcBatchConfigStatusRecord> selectByConfigId(Long configId, List<Integer> statusList) {
        return mdrcBatchConfigStatusRecordMapper.selectByConfigId(configId, statusList);
    }

    @Override
    public MdrcBatchConfigStatusRecord selectByPrimaryKey(Long id) {
        return mdrcBatchConfigStatusRecordMapper.selectByPrimaryKey(id);
    }

}
