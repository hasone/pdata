package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AdminDistrict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AdminDistrictMapper.java
 */
public interface AdminDistrictMapper {

    /** 
     * @Title: selectAdminDistrictByAdminId 
     */
    List<AdminDistrict> selectAdminDistrictByAdminId(Long adminId);

    /** 
     * @Title: insert 
     */
    int insert(AdminDistrict adminDistrict);

    /** 
     * @Title: delete 
     */
    int delete(AdminDistrict adminDistrict);

    /** 
     * @Title: deleteByAdminId 
     */
    int deleteByAdminId(Long adminId);

    /** 
     * @Title: selectADByadminId 
     */
    AdminDistrict selectADByadminId(Long adminId);

    /** 
     * @Title: updateDistricByAdminId 
     */
    int updateDistricByAdminId(@Param("districtId") Long districtId, @Param("adminId") Long adminId);

    /** 
     * @Title: selectAdminDistrictByDistId 
     */
    List<AdminDistrict> selectAdminDistrictByDistId(@Param("districtId") Long districtId);

}