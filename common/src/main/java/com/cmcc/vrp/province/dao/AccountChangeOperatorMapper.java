package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AccountChangeOperator;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wujiamin
 * @date 2016年10月18日上午11:17:42
 */
public interface AccountChangeOperatorMapper {
    
    /** 
     * @Title: deleteByPrimaryKey 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:17:52
    */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:17:56
    */
    int insert(AccountChangeOperator record);

    /** 
     * @Title: insertSelective 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:17:59
    */
    int insertSelective(AccountChangeOperator record);

    /** 
     * @Title: selectByPrimaryKey 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:02
    */
    AccountChangeOperator selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:06
    */
    int updateByPrimaryKeySelective(AccountChangeOperator record);

    /** 
     * @Title: updateByPrimaryKey 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:10
    */
    int updateByPrimaryKey(AccountChangeOperator record);

    /** 
     * @Title: selectByMap 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:13
    */
    List<AccountChangeOperator> selectByMap(Map map);

    /** 
     * @Title: countByMap 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:18
    */
    Long countByMap(Map map);

    /** 
     * @Title: deleteBySerialNum 
     * @param serialNum
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日上午11:18:22
    */
    int deleteBySerialNum(@Param("serialNum") String serialNum);
}