package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Title:HistoryEnterpriseFileMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public interface HistoryEnterpriseFileMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: 
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: 
    */ 
    int insert(HistoryEnterpriseFile record);

    /**
    * @Title: insertSelective
    * @Description: 
    */ 
    int insertSelective(HistoryEnterpriseFile record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: 
    */ 
    HistoryEnterpriseFile selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: 
    */ 
    int updateByPrimaryKeySelective(HistoryEnterpriseFile record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: 
    */ 
    int updateByPrimaryKey(HistoryEnterpriseFile record);

    /**
    * @Title: selectByRequestId
    * @Description: 
    */ 
    HistoryEnterpriseFile selectByRequestId(@Param("requestId") Long requestId);
}