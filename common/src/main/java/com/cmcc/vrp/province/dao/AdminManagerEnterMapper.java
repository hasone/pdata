package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AdminManagerEnter;
import com.cmcc.vrp.province.model.Administer;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * AdminManagerEnterMapper.java
 */
public interface AdminManagerEnterMapper {
    /** 
     * @Title: insert 
     */
    int insert(AdminManagerEnter record);

    /** 
     * @Title: deleteAdminManagerEnter 
     */
    int deleteAdminManagerEnter(AdminManagerEnter record);

    /** 
     * @Title: selectAdminByEnterId 
     */
    List<Administer> selectAdminByEnterId(Long enterId);

    /** 
     * @Title: updateAdminByEnterId 
     */
    int updateAdminByEnterId(@Param("adminId") Long adminId, @Param("enterId") Long enterId);

    /** 
     * @Title: selectAMEByEntId 
     */
    AdminManagerEnter selectAMEByEntId(@Param("enterId") Long entId);

}