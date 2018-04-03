package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.AdminEnterMapper;
import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
 * @ClassName: AdminEnterService
 * @Description:企业关键人服务类
 * @author: qihang
 * @date: 2015-3-12 下午16:25:41
 */
@Service("adminEnterService")
public class AdminEnterServiceImpl implements AdminEnterService {
    @Autowired
    private AdminEnterMapper adminEnterMapper;

    @Autowired
    private EnterprisesService enterpriseService;

    /**
     * @param record
     * @return boolean
     * @Title: insert
     * @Description: 插入一条新的记录
     */
    @Override
    public boolean insert(AdminEnter record) {
        //检验参数
        if (record == null || record.getAdminId() == null || record.getEnterId() == null) {
            return false;
        }
        //检查是否重复,如果有重复返回true
        if (selectCountByQuery(record) > 0) {
            return true;
        }

        return adminEnterMapper.insert(record) > 0;
    }

    /**
     * @param adminId
     * @param enterId
     * @return boolean
     * @Title: insert
     * @Description: 插入一条新的记录
     */
    @Override
    public boolean insert(Long adminId, Long enterId) {
        AdminEnter record = new AdminEnter();
        record.setAdminId(adminId);
        record.setEnterId(enterId);
        return insert(record);
    }

    /**
     * @param adminRole
     * @return int
     * @Title:selectCountByQuery
     * @Description: 查找符合给定参数条件的AdminEnter个数，AdminEnter中各参数均可为空
     */
    @Override
    public int selectCountByQuery(AdminEnter record) {
        if (record == null) {
            return 0;
        }
        return adminEnterMapper.selectCountByQuery(record);
    }


    /**
     * @param adminId
     * @param enterId
     * @return
     * @Title: selectCountByAdminIdEntId
     * @Description: 查找符合给定参数条件的AdminEnter个数
     * @see com.cmcc.vrp.province.service.AdminEnterService#selectCountByAdminIdEntId(java.lang.Long, java.lang.Long)
     */
    @Override
    public int selectCountByAdminIdEntId(Long adminId, Long enterId) {
        if (adminId == null || enterId == null) {
            return 0;
        }
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(adminId);
        adminEnter.setEnterId(enterId);
        return adminEnterMapper.selectCountByQuery(adminEnter);
    }

    /**
     * @param adminId
     * @return boolean
     * @Title:deleteByAdminId
     * @Description: 删除adminId相关adminEnter
     */
    @Override
    public boolean deleteByAdminId(Long adminId) {
        if (adminId == null) {
            return false;
        }
        if (adminEnterMapper.selectByAdminId(adminId) != null) {
            return adminEnterMapper.deleteByAdminId(adminId) > 0;
        }
        return true;

    }

    /**
     * @param enterId
     * @return boolean
     * @Title:deleteByAdminId
     * @Description: 删除adminId相关adminEnter
     */
    @Override
    public boolean deleteByEnterId(Long enterId) {
        if (enterId == null) {
            return false;
        }


        return adminEnterMapper.deleteByEnterId(enterId) > 0;
    }

    /**
     * @param AdminEnter
     * @return boolean
     * @Title:deleteByAdminId
     * @Description: 删除adminId和enterId的相关adminEnter
     */
    @Override
    public boolean deleteAdminRole(AdminEnter record) {
        if (record == null || record.getAdminId() == null || record.getEnterId() == null) {
            return false;
        }

        return adminEnterMapper.deleteAdminEnter(record) > 0;
    }

    @Override
    public List<String> selectByAdminId(Long adminId) {
        return adminEnterMapper.selectByAdminId(adminId);
    }


    /**
     * @param adminId
     * @return
     * @Title: selecteEntIdByAdminId
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.AdminEnterService#selecteEntIdByAdminId(java.lang.Long)
     */
    @Override
    public List<Long> selecteEntIdByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }

        return adminEnterMapper.selecteEntIdByAdminId(adminId);
    }

    /**
     * @param admingId
     * @return
     * @Title: selecteEntByAdminId
     * @Description: 根据用户ID获取企业对象
     * @see com.cmcc.vrp.province.service.AdminEnterService#selecteEntByAdminId(java.lang.Long)
     */
    @Override
    public List<Enterprise> selecteEntByAdminId(Long adminId) {
        //Long entId = selecteEntIdByAdminId(adminId);
        if (adminId == null) {
            return null;
        }

        return adminEnterMapper.selecteEntByAdminId(adminId);
    }

}
