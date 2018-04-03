package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndividualProductMapService {
    boolean insert(IndividualProductMap map);

    boolean batchInsert(@Param("records") List<IndividualProductMap> records);

    IndividualProductMap getByAdminIdAndProductId(Long adminId, Long productId);

    /**
     * 根据用户Id和产品类型获取个人与产品关系模型
     *
     * @author qinqinyan
     * @date 2016/09/28
     */
    IndividualProductMap getByAdminIdAndProductType(Long adminId, Integer type);

}
