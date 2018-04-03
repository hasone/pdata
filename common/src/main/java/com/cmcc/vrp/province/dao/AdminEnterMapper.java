package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.model.Enterprise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AdminEnterMapper.java
 */
public interface AdminEnterMapper {
    /** 
     * @Title: insert 
     */
    int insert(AdminEnter record);


    /** 
     * @Title: selectCountByQuery 
     */
    int selectCountByQuery(AdminEnter record);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:deleteByAdminId
     * @Description:根据用户ID，删除其对应的所有记录，当删除用户时，如果用户是企业关键人，需要执行这个操作
     */

    List<String> selectByAdminId(Long adminId);

    /** 
     * @Title: deleteByAdminId 
     */
    int deleteByAdminId(Long adminId);

    /** 
     * @Title: deleteByEnterId 
     */
    int deleteByEnterId(Long enterId);

    /** 
     * @Title: deleteAdminEnter 
     */
    int deleteAdminEnter(AdminEnter record);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:selecteEntIdByAdminId
     * @Description: 根据用户ID获取企业ID
     * @author: sunyiwei
     */
    List<Long> selecteEntIdByAdminId(Long adminId);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:selecteEntByAdminId
     * @Description: 根据用户ID获取企业
     * @author: sunyiwei
     */
    List<Enterprise> selecteEntByAdminId(Long adminId);


    /** 
     * @Title: selectAdminIdByEntId 
     */
    List<Long> selectAdminIdByEntId(Long entId);

    /** 
     * @Title: updateAdminByEntId 
     */
    int updateAdminByEntId(@Param("adminId") Long adminId, @Param("entId") Long entId);

    /** 
     * @Title: selectAdminEntByPhone 
     */
    AdminEnter selectAdminEntByPhone(@Param("mobilePhone") String phone);
}