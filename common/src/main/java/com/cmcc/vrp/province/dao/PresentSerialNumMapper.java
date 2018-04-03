package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.PresentSerialNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:31:09
*/
public interface PresentSerialNumMapper {
    /**
     * 批量插入批量赠送流水号对应关系
     *
     * @param psns 批量赠送流水号对应关系
     * @return 成功插入的记录条数
     */
    int batchInsert(@Param("psns") List<PresentSerialNum> psns);

    /**
     * 根据平台流水号获取相应的对应关系
     *
     * @param pltSn 平台流水号
     * @return 相应的流水号对应关系
     */
    PresentSerialNum getByPltSn(String pltSn);
}