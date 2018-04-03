package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.HistoryEnterprises;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title:HistoryEnterprisesMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public interface HistoryEnterprisesMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: TODO
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    int insert(HistoryEnterprises record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    int insertSelective(HistoryEnterprises record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */ 
    HistoryEnterprises selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */ 
    int updateByPrimaryKeySelective(HistoryEnterprises record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: TODO
    */ 
    int updateByPrimaryKey(HistoryEnterprises record);

    /**
    * @Title: selectByRequestId
    * @Description: TODO
    */ 
    HistoryEnterprises selectByRequestId(@Param("requestId") Long requestId);
    
    /**
     * @Title: selectHistoryEnterpriseByRequestId
     * @Description: TODO
     */ 
    HistoryEnterprises selectHistoryEnterpriseByRequestId(@Param("requestId") Long requestId);

    /**
     * @Title: updateStatusByRequestId
     * @Description: TODO
     * */
    int updateStatusByRequestId(HistoryEnterprises record);

    /**
     * @Title: selectByEntIdAndStatus
     * @Description: TODO
     * */
    List<HistoryEnterprises> selectByEntIdAndStatus(@Param("entId")Long entId, @Param("status") Integer status);
}