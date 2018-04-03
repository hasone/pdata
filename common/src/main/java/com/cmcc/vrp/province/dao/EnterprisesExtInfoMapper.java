package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.EnterprisesExtInfo;

import org.apache.ibatis.annotations.Param;

/**
 * 企业扩展信息
 */
public interface EnterprisesExtInfoMapper {
    /**
     * 逻辑删除企业的扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    int delete(Long entId);

    /**
     * 插入新的企业扩展信息
     *
     * @param record 新的企业扩展信息
     * @return
     */
    int insert(EnterprisesExtInfo record);

    /**
     * 根据企业ID获取企业扩展信息
     *
     * @param entId 企业ID
     * @return
     */
    EnterprisesExtInfo get(Long entId);

    /**
     * 更新企业扩展信息
     *
     * @param enterprisesExtInfo 更新企业扩展信息
     * @return
     */
    int updateByEntId(EnterprisesExtInfo enterprisesExtInfo);

    /**
     * 插入新的企业扩展信息
     * @param record 新的企业扩展信息
     * @return
     */
    int insertSelective(EnterprisesExtInfo record);

    /**
     * 将企业回调地址设置为空
     * @param enterId
     * */
    int setCallbackUrlNullByEntId(@Param("enterId") Long enterId);

    /** 
     * @Title: selectByEcCodeAndEcPrdCode 
     */
    List<EnterprisesExtInfo> selectByEcCodeAndEcPrdCode(@Param("ecCode")String ecCode, @Param("ecPrdCode")String ecPrdCode);
}