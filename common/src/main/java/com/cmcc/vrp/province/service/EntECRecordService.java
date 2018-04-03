package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntECRecordService 
 * @Description: 企业EC操作记录
 * @author: Rowe
 * @date: 2017年4月24日 上午9:22:28
 */
public interface EntECRecordService {

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param entECRecord
     * @return
     * @return: boolean
     */
    boolean insert(EntECRecord entECRecord);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: EntECRecord
     */
    EntECRecord selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: showEntEcRecordForPageResult 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<Enterprise>
     */
    List<EntECRecord> showEntEcRecordForPageResult(QueryObject queryObject);

    /**
     * 
     * @Title: showEntEcRecordCount 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: long
     */
    long showEntEcRecordCount(QueryObject queryObject);
    
    /**
     * getLatestEntEcRecord 得到最新Ec变更记录
     */
    List<EntECRecord> getLatestEntEcRecords(Long entId);
}
