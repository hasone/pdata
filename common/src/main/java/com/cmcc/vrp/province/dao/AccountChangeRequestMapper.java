package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AccountChangeRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:AccountChangeRequestMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
*/
public interface AccountChangeRequestMapper {
    /**
    * @Title: delete
    * @Description: TODO
    */ 
    int delete(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    int insert(AccountChangeRequest record);

    /**
    * @Title: get
    * @Description: TODO
    */ 
    AccountChangeRequest get(Long id);

    /**
    * @Title: updateStatus
    * @Description: TODO
    */ 
    int updateStatus(@Param("id") Long id, @Param("newStatus") int newStatus, @Param("desc") String desc, @Param("lastOperatorId") Long operatorId);

    //根据条件查询满足条件的记录列表
    /**
    * @Title: query
    * @Description: TODO
    */ 
    List<AccountChangeRequest> query(Map<String, Object> params);

    //根据条件查询满足条件的记录数
    /**
    * @Title: queryCount
    * @Description: TODO
    */ 
    int queryCount(Map<String, Object> params);

    //更新充值金额
    /**
    * @Title: updateCount
    * @Description: TODO
    */ 
    int updateCount(@Param("id") Long id, @Param("delta") Double delta,
                    @Param("newStatus") int newStatus, @Param("desc") String desc, @Param("lastOperatorId") Long operatorId);
}