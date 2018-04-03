package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.EntBillRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * EntBillService.java
 * @author wujiamin
 * @date 2016年12月27日
 */
public interface EntBillService {
    /** 
     * @Title: showPageList 
     */
    List<EntBillRecord> showPageList(QueryObject queryObject);
    
    /** 
     * @Title: showPageCount 
     */
    int showPageCount(QueryObject queryObject);
    
    /** 
     * @Title: sumEntBillPriceByMap 
     */
    Double sumEntBillPriceByMap(Map map);
}
