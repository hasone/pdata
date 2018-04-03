package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
/**
 * 统计供应商每天发送充值请求的金额mapper（以成功发送请求充值为统计依据）
 * @author qinqinyan
 * */
public interface SupplierReqUsePerDayMapper {

    /**
     * @title:insertSelective
     * */
    int insertSelective(SupplierReqUsePerDay record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierReqUsePerDay selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(SupplierReqUsePerDay record);
    
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);
    
    /**
     * @title:updateUsedMoney
     * */
    int updateUsedMoney(@Param("id")Long id, @Param("delta")Double delta);
    
    /**
     * @title: selectByMap
     * */
    List<SupplierReqUsePerDay> selectByMap(Map map);
    
    /**
     * @title: batchDelete
     * */
    int batchDelete(Map map);

}