/**
 *
 */
package com.cmcc.vrp.province.dao;


import com.cmcc.vrp.province.model.ProductChangeOperator;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>Title:ProductChangeOperatorMapper </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月21日
 */
public interface ProductChangeOperatorMapper {


    /**
     * 批量插入
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(Map<String, Object> map);

    /**
     * 读取记录
     * @Title:getProductChangeRecordByEntId
     * @Description:
     * */
    List<ProductChangeOperator> getProductChangeRecordByEntId(@Param("entId") Long entId);

    /**
     * 逻辑删除记录
     * @Title:deleteProductChangeRecordByEntId
     * @Description:
     * */
    int deleteProductChangeRecordByEntId(@Param("entId") Long entId);
}
