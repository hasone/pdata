package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AccountChangeDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Title:AccountChangeDetailMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
*/
public interface AccountChangeDetailMapper {
    /**
    * @Title: deleteByPrimaryKey
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    */ 
    int insert(AccountChangeDetail record);

    /**
    * @Title: insertSelective
    */ 
    int insertSelective(AccountChangeDetail record);

    /**
    * @Title: selectByPrimaryKey
    */ 
    AccountChangeDetail selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    */ 
    int updateByPrimaryKeySelective(AccountChangeDetail record);

    /**
    * @Title: updateByPrimaryKey
    */ 
    int updateByPrimaryKey(AccountChangeDetail record);

    /**
    * @Title: selectByRequestId
    */ 
    AccountChangeDetail selectByRequestId(@Param("requestId") Long requestId);
}