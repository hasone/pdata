package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountChangeOperator;

import java.util.List;
import java.util.Map;

/**
 * @author wujiamin
 * @date 2016年10月18日上午11:21:40
 */
public interface AccountChangeOperatorService {
    
    /** 
     * @Title: insert 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:21:59
    */
    boolean insert(AccountChangeOperator record);

    /** 
     * @Title: selectByPrimaryKey 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:26:27
    */
    AccountChangeOperator selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:22:10
    */
    boolean updateByPrimaryKeySelective(AccountChangeOperator record);

    /** 
     * @Title: selectByMap 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:22:18
    */
    List<AccountChangeOperator> selectByMap(Map map);

    /** 
     * @Title: countByMap 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:22:22
    */
    Long countByMap(Map map);

    /** 
     * @Title: deleteBySerialNum 
     * @param serialNum
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:22:31
    */
    boolean deleteBySerialNum(String serialNum);
}
