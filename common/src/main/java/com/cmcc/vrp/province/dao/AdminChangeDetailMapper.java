package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.AdminChangeDetail;
/**
 * 
 * AdminChangeDetailMapper
 *
 */
public interface AdminChangeDetailMapper {
    /**
     * deleteByPrimaryKey(Long id)
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert(AdminChangeDetail record);
     */
    int insert(AdminChangeDetail record);

    /**
     * insertSelective(AdminChangeDetail record);
     */
    int insertSelective(AdminChangeDetail record);

    /**
     * selectByPrimaryKey(Long id);
     */
    AdminChangeDetail selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective(AdminChangeDetail record);
     */
    int updateByPrimaryKeySelective(AdminChangeDetail record);

    /**
     * deleteByPrimaryKey(Long id)
     */
    int updateByPrimaryKey(AdminChangeDetail record);
    
    /**
     * getByRequestId(Long requestId);
     */
    List<AdminChangeDetail> getByRequestId(Long requestId);
    
    /**
     * getDetailByRequestId(Long requestId);
     */
    List<AdminChangeDetail> getDetailByRequestId(Long requestId);
    
    /**
     * getVerifyingCount(Long adminId);
     */
    int getVerifyingCount(Long adminId);
    
    /**
     * getVerifyingCountByMobile(String mobile);
     */
    int getVerifyingCountByMobile(String mobile);
    
    /**
     * getVirifyListPage(Map<String, Object> paramMap);
     */
    List<AdminChangeDetail> getVirifyListPage(Map<String, Object> paramMap);
    
    /**
     * getVirifyListPageCount(Map<String, Object> paramMap);
     */
    int getVirifyListPageCount(Map<String, Object> paramMap);
    
    /**
     * getVirifyListPage(Map<String, Object> paramMap);
     */
    List<AdminChangeDetail> getNoVirifyListPage(Map<String, Object> paramMap);
    
    /**
     * getVirifyListPageCount(Map<String, Object> paramMap);
     */
    int getNoVirifyListPageCount(Map<String, Object> paramMap);
    
}