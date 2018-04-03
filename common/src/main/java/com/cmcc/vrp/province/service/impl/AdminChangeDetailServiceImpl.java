package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.AdminChangeDetailMapper;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.service.AdminChangeDetailService;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * AdminChangeDetailServiceImpl
 *
 */
@Service("adminChangeDetailService")
public class AdminChangeDetailServiceImpl implements AdminChangeDetailService {

    @Autowired
    AdminChangeDetailMapper adminChangeDetailMapper;
    
    /**
     * 插入
     */
    @Override
    public boolean insert(AdminChangeDetail adminChangeDetail) {
        return adminChangeDetailMapper.insertSelective(adminChangeDetail)>0;
    }

    /**
     * 通过requestId查找记录，requestId:approvalRequest的Id
     */
    @Override
    public AdminChangeDetail getByRequestId(Long requestId) {
        List<AdminChangeDetail> list = adminChangeDetailMapper.getByRequestId(requestId);
        return list.isEmpty()?null:list.get(0);
    }

    /**
     * 通过requestId查找拓展记录，requestId:approvalRequest的Id
     */
    @Override
    public AdminChangeDetail getDetailByRequestId(Long requestId) {
        List<AdminChangeDetail> list = adminChangeDetailMapper.getDetailByRequestId(requestId);
        return list.isEmpty()?null:list.get(0);
    }

    /**
     * 通过adminId，查找正在审核的个数
     */
    @Override
    public int getVerifyingCount(Long adminId) {
        return adminChangeDetailMapper.getVerifyingCount(adminId);
    }

    /**
     * 通过mobile，查找正在审核的个数
     */
    @Override
    public int getVerifyingCountByMobile(String mobile) {
        return adminChangeDetailMapper.getVerifyingCountByMobile(mobile);
    }
    
    /**
     * 历史记录分页
     */
    @Override
    public int queryPaginationAdminCount(QueryObject queryObject,boolean needVirify) {
        Map<String, Object> map = queryObject.toMap();
        if(needVirify){
            return adminChangeDetailMapper.getVirifyListPageCount(map);
        }else{
            return adminChangeDetailMapper.getNoVirifyListPageCount(map);
        }
        
    }

    /**
     * 历史记录分页
     */
    @Override
    public List<AdminChangeDetail> queryPaginationAdminList(
            QueryObject queryObject,boolean needVirify) {
        Map<String, Object> map = queryObject.toMap();
        if(needVirify){
            return adminChangeDetailMapper.getVirifyListPage(map);
        }else{
            return adminChangeDetailMapper.getNoVirifyListPage(map);
        }
    }
}
