package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
/**
 * 供应商产品每天充值额度mapper（以发送充值请求为统计依据）
 * @author qinqinyan
 * */
public interface SupplierProdReqUsePerDayMapper {

    /**
     * @title: insertSelective
     * */
    int insertSelective(SupplierProdReqUsePerDay record);

    /**
     * @title: selectByPrimaryKey
     * */
    SupplierProdReqUsePerDay selectByPrimaryKey(Long id);

    /**
     * @title: updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(SupplierProdReqUsePerDay record);
    
    /**
     * @title: deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);
    
    /**
     * @title: updateUsedMoney
     * */
    int updateUsedMoney(@Param("id")Long id, @Param("delta")Double delta);
    
    /**
     * @title: selectByMap
     * */
    List<SupplierProdReqUsePerDay> selectByMap(Map map);

}