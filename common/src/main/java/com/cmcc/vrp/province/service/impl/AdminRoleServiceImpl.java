package com.cmcc.vrp.province.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.AdminRoleMapper;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RoleService;

/**
 * @ClassName: AdminRoleServiceImpl
 * @Description: 用户角色实现类
 * @author: qihang
 * @date: 2015年3月18日 下午14:05:00
 * <p>
 * 用户与角色间的关系是多对一，即每个用户只能拥有一种角色 。adminRole表是多对多,mapper是按多对多处理。需要特别注意
 */
@Service("adminRoleService")
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RoleService roleService;

    /**
     * @param adminId
     * @param roleId
     * @return boolean
     * @Title:insertAdminRole
     * @Description: 插入一条新的adminRole
     */
    @Override
    public boolean insertAdminRole(Long adminId, Long roleId) {
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(adminId);
        adminRole.setRoleId(roleId);
        return insertAdminRole(adminRole);
    }

    /**
     * @param adminRole
     * @return boolean
     * @Title:insertAdminRole
     * @Description: 插入一条新的adminRole
     */
    @Override
    public boolean insertAdminRole(AdminRole adminRole) {
        if (adminRole == null || adminRole.getAdminId() == null || adminRole.getRoleId() == null) {
            return false;
        }

        //插入前检查是否已存在,存在返回true
        if (selectCountByQuery(adminRole) > 0) {
            return true;
        }

        return adminRoleMapper.insert(adminRole) > 0;
    }

    /**
     * @param adminRole
     * @return int
     * @Title:selectCountByQuery
     * @Description: 查找符合给定参数条件的AdminRole个数，adminRole中各参数均可为空
     */
    @Override
    public int selectCountByQuery(AdminRole adminRole) {
        if (adminRole == null) {
            return 0;
        }
        return adminRoleMapper.selectCountByQuery(adminRole);
    }


    /**
     * @param adminId
     * @return List<Long>
     * @Title:getRoleIdByAdminId
     * @Description: 通过adminId找到所有的角色项目
     */
    @Override
    public Long getRoleIdByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }
        return adminRoleMapper.selectRoleIdByAdminId(adminId);
    }

    /**
     * @param roleId
     * @return List<AdminRole>
     * @Title:deleteByRoleId
     * @Description: 删除roleId相关adminRole
     */
    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }

        List<AdminRole> list = adminRoleMapper.selectAdminRoleByRoleId(roleId);
        if (list.size() == 0) {
            return true;
        }

        return adminRoleMapper.deleteByRoleId(roleId) > 0;
    }

    /**
     * @param adminId
     * @return boolean
     * @Title:deleteByAdminId
     * @Description: 删除adminId相关adminRole
     */
    @Override
    public boolean deleteByAdminId(Long adminId) {
        if (adminId == null) {
            return false;
        }
        List<AdminRole> ar = adminRoleMapper.selectAdminRoleByAdminId(adminId);
        if (ar == null || ar.size() == 0) {
            return true;
        } else {
            return adminRoleMapper.deleteByAdminId(adminId) > 0;
        }
    }

    /**
     * @param adminId
     * @return
     * @Title: getRoleNameByAdminId
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.AdminRoleService#getRoleNameByAdminId(java.lang.Long)
     */
    @Override
    public String getRoleNameByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }

        Long roleId = adminRoleMapper.selectRoleIdByAdminId(adminId);
        if (roleId != null) {
            return adminRoleMapper.getRoleNameByAdminId(adminId);
        }

        return null;
    }
}
