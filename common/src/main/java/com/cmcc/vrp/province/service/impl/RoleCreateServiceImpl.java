/**
 *
 */
package com.cmcc.vrp.province.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.RoleCreateMapper;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.RoleCreate;
import com.cmcc.vrp.province.service.RoleCreateService;

/**
 * <p>Title:RoleCreateServiceImpl </p>
 * <p>Description: 创建角色服务类</p>
 *
 * @author xujue
 * @date 2016年7月15日
 */

@Service("roleCreateService")
public class RoleCreateServiceImpl implements RoleCreateService {

    @Autowired
    RoleCreateMapper roleCreateMapper;
    
    @Override
    public List<Role> getCreateRolesByRoleId(Long roleId) {
        return roleCreateMapper.getCreateRolesByRoleId(roleId);
    }

    /**
     * @param role
     * @return
     * @Title: selectRoleIdsCreateByRoleId
     * @Description: 查询该角色可以创建的角色id
     */
    @Override
    public List<Long> selectRoleIdsCreateByRoleId(Long roleId) {

        if (roleId == null) {
            return null;
        }
        return roleCreateMapper.selectRoleIdsCreateByRoleId(roleId);
    }

    /**
     * @param role        需要更新的角色
     * @param roleIdsList 用户页面选择的角色
     * @return
     * @Title: updateRoleWithRoleIdsCreate
     * @Description: 更新角色可创建的角色
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateRoleWithRoleIdsCreate(Role role, List<Long> roleIdsList) {
        if (role == null || role.getRoleId() == null) {
            return false;
        }

        //1.查找该角色更新前拥有的可创建角色的id
        List<Long> oldRoleIds = roleCreateMapper.selectRoleIdsCreateByRoleId(role.getRoleId());

        //2. 得到数据库需要新增的角色列表
        List<Long> newRoleIds = resolveOldAndNewRoles(oldRoleIds, roleIdsList);

        // 3.删除旧的权限
        if (oldRoleIds != null && oldRoleIds.size() != 0) {
            for (long deleteRoleId : oldRoleIds) {
                if (!deleteRoleCreateByRoleId(role.getRoleId(), deleteRoleId)) {
                    throw new TransactionException();
                }
            }
        }

        // 4.增加新的权限
        if (newRoleIds != null && newRoleIds.size() != 0) {
            for (long addRoleId : newRoleIds) {
                if (!insert(role.getRoleId(), addRoleId)) {
                    throw new TransactionException();
                }
            }
        }

        return true;
    }


    /**
     * @param oldRoleIds  已有的旧权限列表
     * @param roleIdsList 用户提交的权限列表
     * @return List<Long> 数据库新增加的权限
     * @Title: resolveOldAndNewRoles
     * @Description: 处理旧角色和需要添加新角色间的关系，返回值为需要新增的角色
     */
    private List<Long> resolveOldAndNewRoles(List<Long> oldRoleIds, List<Long> roleIdsList) {
        List<Long> newAuthIds = new ArrayList<Long>();
        if (roleIdsList == null || roleIdsList.size() == 0) {// 说明需要去掉所有的角色
            return null;
        } else if (oldRoleIds == null || oldRoleIds.size() == 0) {// 说明数据库没有旧的角色，即数据库需要加入所有角色roleIdsList
            return roleIdsList;
        } else {
            for (Long roleId : roleIdsList) {
                if (oldRoleIds.contains(roleId)) { // 说明要删除和要添加同一种角色，则无需操作
                    oldRoleIds.remove(roleId);
                } else { // 说明旧的角色里没有roleId,则需要添加roleId
                    newAuthIds.add(roleId);
                }
            }
            return newAuthIds;
        }
    }

    /**
     * @param roleId
     * @param createRoleId
     * @return
     * @Title: deleteRoleCreateByRoleId
     * @Description: 删除指定角色可创建的角色id
     */
    private boolean deleteRoleCreateByRoleId(Long roleId, Long createRoleId) {
        if (roleId == null || createRoleId == null) {
            return false;
        }
        RoleCreate roleCreate = new RoleCreate();
        roleCreate.setCreateRoleId(createRoleId);
        roleCreate.setRoleId(roleId);

        //如果查询没有该记录则不用删除，直接返回true
        if (select(roleId, createRoleId) == null) {
            return true;
        }
        return roleCreateMapper.deleteRoleCreateByRoleId(roleCreate) > 0;
    }

    /**
     * @param roleId
     * @param createRoleId
     * @return RoleCreate
     * @Title: select
     * @Description: 通过roleId和createRoleId查找某条记录
     */
    private RoleCreate select(Long roleId, Long createRoleId) {
        if (roleId == null || createRoleId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        params.put("createRoleId", createRoleId);
        return roleCreateMapper.select(params);
    }

    /**
     * @param roleId
     * @param createRoleId
     * @return boolean
     * @Title: insert
     * @Description: 插入新的角色和可以创建的角色
     */
    private boolean insert(Long roleId, Long createRoleId) {

        if (roleId == null || createRoleId == null) {
            return false;
        }
        RoleCreate roleCreate = new RoleCreate();
        roleCreate.setCreateRoleId(createRoleId);
        roleCreate.setRoleId(roleId);


        //插入前查重复
        RoleCreate roleSearch = select(roleCreate.getRoleId(), roleCreate.getCreateRoleId());
        if (roleSearch != null) {
            return true; //查询数据库，已经有该条记录，不用插入直接返回true
        }

        return roleCreateMapper.insert(roleCreate) > 0;
    }
}
