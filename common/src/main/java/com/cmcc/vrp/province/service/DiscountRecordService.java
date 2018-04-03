package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.DiscountRecord;

/**
 * 
 * @ClassName: DiscountRecordService 
 * @Description: 折扣更改记录表
 * @author: Rowe
 * @date: 2016年11月16日 上午10:02:35
 */
public interface DiscountRecordService {
	
    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param discountRecord
     * @return
     * @return: boolean
     */
    boolean insert(DiscountRecord discountRecord);
	
    /**
     * 
     * @Title: batchInsert 
     * @Description: 批量生成记录
     * @param records
     * @return
     * @return: boolean
     */
    boolean batchInsert(List<DiscountRecord> records);
    
    /**
     * @Title: getOneDayDiscount 
     * @Description: 得到某天某userId某prdCode的所有的折扣变更记录
     * @param date
     * @param userId
     * @param prdCode
     * @return
     */
    List<DiscountRecord> getOneDayDiscount(String date, String userId,
            String prdCode);
    
    /**
     * @Title: findDiscount 
     * @Description: 得到某天的时间格式，billTime样式为 20160820153556,
     * @param date
     * @param userId
     * @param prdCode
     * @return
     */
    String findDiscount(List<DiscountRecord> list, String billTime);
}
