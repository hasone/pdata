package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * AdminChangeDetailService
 * 用户更改信息
 *
 */
public interface AdminChangeDetailService {
    /**
     * 插入
     */
    boolean insert(AdminChangeDetail adminChangeDetail);
    
    /**
     * 通过requestId查找记录，requestId:approvalRequest的Id
     */
    AdminChangeDetail getByRequestId(Long requestId);
    
    /**
     * 通过requestId查找拓展记录，requestId:approvalRequest的Id
     */
    AdminChangeDetail getDetailByRequestId(Long requestId);
    
    /**
     * 通过adminId，查找正在审核的个数
     */
    int getVerifyingCount(Long adminId);
    
    /**
     * 通过mobile，查找正在审核的个数
     */
    int getVerifyingCountByMobile(String mobile);
    
    /**
     * 历史记录分页
     */
    int queryPaginationAdminCount(QueryObject queryObject,boolean needVirify);
    
    /**
     * 历史记录分页
     */
    List<AdminChangeDetail> queryPaginationAdminList(QueryObject queryObject,boolean needVirify);
}
