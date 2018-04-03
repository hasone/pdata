package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RoleAuthorityMapper;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RoleAuthorityServiceImpl
 * @Description: 角色权限管理实现类
 * @author: qihang
 * @date: 2015年3月22日 上午10:21:28
 */
@Service("roleAuthorityService")
public class RoleAuthorityServiceImpl implements RoleAuthorityService {
    @Autowired
    RoleAuthorityMapper roleAuthorityMapper;


    /**
     * @param oleAuthority
     * @return boolean
     * @Title: insert
     * @Description: 插入新的角色和权限
     */
    @Override
    public boolean insert(RoleAuthority roleAuthority) {
        //检查参数
        if (roleAuthority == null || roleAuthority.getRoleId() == null || roleAuthority.getAuthorityId() == null) {
            return false;
        }

        //插入前查重复
        RoleAuthority roleSearch = select(roleAuthority.getRoleId(), roleAuthority.getAuthorityId());
        if (roleSearch != null) {
            return false;
        }

        return roleAuthorityMapper.insert(roleAuthority) > 0;
    }

    /**
     * @param roleId
     * @param authorityId
     * @return boolean
     * @Title: insert
     * @Description: 插入新的角色和权限
     */
    @Override
    public boolean insert(Long roleId, Long authorityId) {
        //参数校验在insert(RoleAuthority)中进行
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(authorityId);
        roleAuthority.setRoleId(roleId);
        return insert(roleAuthority);
    }

    /**
     * @param roleId
     * @param authorityId
     * @return RoleAuthority
     * @Title: select
     * @Description: 通过roleId和authorityId查找某条记录
     */
    @Override
    public RoleAuthority select(Long roleId, Long authorityId) {
        if (roleId == null || authorityId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        params.put("authorityId", authorityId);
        return roleAuthorityMapper.select(params);
    }

    /**
     * @param roleId
     * @return List<RoleAuthority>
     * @Title: selectByRoleId
     * @Description: 通过roleId返回该角色相关的权限的对象
     */
    @Override
    public List<RoleAuthority> selectByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return roleAuthorityMapper.selectByRoleId(roleId);
    }


    /**
     * @param roleId
     * @param authorityId
     * @return List<Long>
     * @Title: selectAuthsByRoleId
     * @Description: 通过roleId和该角色相关的权限的list
     */
    @Override
    public List<Long> selectAuthsByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return roleAuthorityMapper.selectAuthsByRoleIds(roleId);
    }

    /**
     * @param roleAuthority
     * @return RoleAuthority
     * @Title: deleteByRoleId
     * @Description: 删除指定的roleId的角色权限
     */
    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }

        //1.查找是否有该roleId的角色权限，没有则直接返回true
        if (selectByRoleId(roleId).size() == 0) {
            return true;
        }

        //2.有记录则需要删除
        return roleAuthorityMapper.deleteByRoleId(roleId) > 0;
    }

    /**
     * @param roleId
     * @param authorityId
     * @return RoleAuthority
     * @Title: deleteRoleAuthority
     * @Description: 删除指定的roleAuthority
     */
    @Override
    public boolean deleteRoleAuthority(Long roleId, Long authorityId) {
        if (roleId == null || authorityId == null) {
            return false;
        }
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(authorityId);
        roleAuthority.setRoleId(roleId);

        //如果查询没有该记录则不用删除，直接返回true
        if (select(roleId, authorityId) == null) {
            return true;
        }
        return roleAuthorityMapper.deleteRoleAuthority(roleAuthority) > 0;
    }

    @Override
    public List<RoleAuthority> selectExistingRoleAuthorityByAuthorityName(String authorityName) {
        return roleAuthorityMapper.selectExistingRoleAuthorityByAuthorityName(authorityName);
    }

}
