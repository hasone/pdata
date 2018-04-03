package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EntECRecordMapper;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntECRecordServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月24日 上午9:29:14
 */
@Service("EntECRecordService")
public class EntECRecordServiceImpl implements EntECRecordService {

    @Autowired
    EntECRecordMapper entECRecordMapper;

    /**
     * 
     */
    @Override
    public boolean insert(EntECRecord entECRecord) {
        if (entECRecord == null) {
            return false;
        }
        return entECRecordMapper.insertSelective(entECRecord) == 1;
    }

    /**
     * 
     */
    @Override
    public List<EntECRecord> showEntEcRecordForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return entECRecordMapper.showEntEcRecordForPageResult(queryObject.toMap());
    }

    /**
     * 
     */
    @Override
    public long showEntEcRecordCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0L;
        }
        return entECRecordMapper.showEntEcRecordCount(queryObject.toMap());
    }

    /**
     * 
     */
    @Override
    public EntECRecord selectByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return entECRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 
     */
    @Override
    public List<EntECRecord> getLatestEntEcRecords(Long entId) {
        return entECRecordMapper.getLatestEntEcRecords(entId);
    }

}
