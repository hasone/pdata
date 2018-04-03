package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:12:33
*/
public interface EnterpriseAccountMapper {
    /*
     * 插入记录
     */
    /**
     * @param record
     * @return
     */
    int insert(EnterpriseAccount record);

    /**
     * 获取企业帐户余额信息
     *
     * @param eid 企业ID
     * @param pid 流量包ID
     */
    EnterpriseAccount get(@Param("eid") Long eid, @Param("pid") Long pid);

    /**
     * 删除企业帐户
     *
     * @param eid 企业ID
     * @param pid 产品ID
     */
    int delete(@Param("eid") Long eid, @Param("pid") Long pid);

    /**
     * 删除企业帐户下所有的余额
     *
     * @param eid 企业ID
     */
    int deleteByEnterId(Long eid);

    /**
     * 更新企业帐户信息
     *
     */
    int update(EnterpriseAccount enterpriseAccount);

    /*
     * 获取企业帐户所有的流量包信息
     */
    List<EnterpriseAccount> getByEntId(Long eid);
}