package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EnterprisesExtInfo;

/**
 * 企业扩展信息服务
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public interface EnterprisesExtInfoService {
    /**
     * 根据企业ID获取企业扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    EnterprisesExtInfo get(Long entId);

    /**
     * 删除企业的扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    boolean delete(Long entId);

    /**
     * 插入新的企业扩展信息
     *
     * @param enterprisesExtInfo 新的企业扩展信息
     * @return
     */
    boolean insert(EnterprisesExtInfo enterprisesExtInfo);

    /**
     * 更新企业扩展信息
     *
     * @param enterprisesExtInfo 更新企业扩展信息，根据企业ID
     * @return
     */
    boolean update(EnterprisesExtInfo enterprisesExtInfo);
    
    /** 
     * @Title: selectByEcCodeAndEcPrdCode 
     */
    List<EnterprisesExtInfo> selectByEcCodeAndEcPrdCode(String ecCode, String ecPrdCode);
}
