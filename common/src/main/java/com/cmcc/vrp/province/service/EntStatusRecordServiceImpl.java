package com.cmcc.vrp.province.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EntStatusRecordMapper;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntStatusRecordServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月25日 上午10:03:15
 */
@Service("entStatusRecordService")
public class EntStatusRecordServiceImpl implements EntStatusRecordService {

    @Autowired
    EntStatusRecordMapper entStatusRecordMapper;

    /**
     * 
     */
    @Override
    public boolean insert(EntStatusRecord entStatusRecord) {
        if (entStatusRecord == null) {
            return false;
        }
        return entStatusRecordMapper.insertSelective(entStatusRecord) == 1;
    }

    /**
     * 
     */
    @Override
    public EntStatusRecord selectByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return entStatusRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 
     */
    @Override
    public List<EntStatusRecord> showEntStatusRecordForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return entStatusRecordMapper.showEntStatusRecordForPageResult(queryObject.toMap());
    }

    /**
     * 
     */
    @Override
    public long showEntStatusRecordCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0L;
        }
        return entStatusRecordMapper.showEntStatusRecordCount(queryObject.toMap());
    }

}
