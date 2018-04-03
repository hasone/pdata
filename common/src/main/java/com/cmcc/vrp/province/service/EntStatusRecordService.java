package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntStatusRecordService 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月25日 上午9:17:44
 */
public interface EntStatusRecordService {

    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param entStatusRecord
     * @return
     * @return: boolean
     */
    boolean insert(EntStatusRecord entStatusRecord);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: 查找记录
     * @param id
     * @return
     * @return: EntStatusRecord
     */
    EntStatusRecord selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: showEntStatusRecordForPageResult 
     * @Description: 记录列表
     * @param queryObject
     * @return
     * @return: List<EntStatusRecord>
     */
    List<EntStatusRecord> showEntStatusRecordForPageResult(QueryObject queryObject);

    /**
     * 
     * @Title: showEntStatusRecordCount 
     * @Description: 记录总数
     * @param queryObject
     * @return
     * @return: long
     */
    long showEntStatusRecordCount(QueryObject queryObject);
}
