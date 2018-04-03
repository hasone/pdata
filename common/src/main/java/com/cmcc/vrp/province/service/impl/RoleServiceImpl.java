package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.RoleMapper;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色管理实现类
 * @author: qihang
 * @date: 2015年3月21日 下午5:03:28
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleAuthorityService roleAuthorityService;

    @Autowired
    AdminRoleService adminRoleService;

    /**
     * @param id
     * @return Role
     * @Title: getRoleById
     * @Description: 通过role_id或得角色
     */
    public Role getRoleById(Long id) {
        if (id == null) {
            return null;
        }
        return roleMapper.selectByPrimaryKey(id);
    }

    /**
     * @param code
     * @return Role
     * @Title: getRoleByCode
     * @Description: 通过code获得角色
     */
    @Override
    public Role getRoleByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return roleMapper.selectByCode(code);
    }

    /**
     * @param id
     * @return Role
     * @Title: getAvailableRoles
     * @Description: 通过所有的角色
     */
    public List<Role> getAvailableRoles() {
        return roleMapper.getAvailableRoles();
    }

    /**
     * @param role    插入的角色
     * @return int
     * @Title: getAvailableRoles
     * @Description: 插入新的角色，并赋予该角色指定的权限
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertNewRoleWithAuthIds(Role role, List<Long> authList) {
        if (role == null || StringUtils.isEmpty(role.getCode())
            || StringUtils.isEmpty(role.getName())
            || role.getCreator() == null) {
            return false;
        }

        // 1.查找Code是否已经在数据库中存在
        if (getRoleByCode(role.getCode()) != null) {
            return false;
        }

        // 2.插入新的权限
        setDefaultRoleProperty(role);
        if (!insert(role)) {
            throw new TransactionException();
        }

        // 3.更新角色权限表，给这个新角色添加指定的权限
        if (authList != null) {
            for (Long authId : authList) {
                RoleAuthority roleAuthority = new RoleAuthority();
                roleAuthority.setRoleId(role.getRoleId());
                roleAuthority.setAuthorityId(authId);
                if (!roleAuthorityService.insert(roleAuthority)) {
                    throw new TransactionException();
                }
            }
        }
        return true;
    }


    private void setDefaultRoleProperty(Role role) {
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        if (role.getRoleStatus() == null) {
            role.setRoleStatus(Constants.ROLE_STATUS.ON.getValue());
        }
        if (role.getCanBeDeleted() == null) {
            role.setCanBeDeleted(Constants.ROLE_CAN_BE_DELETED.YES.getValue());
        }
    }

    private boolean insert(Role role) {
        return roleMapper.insert(role) > 0;
    }

    private boolean updateByPrimaryKeySelective(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role) > 0;
    }


    /**
     * @param role         需要更新的角色，包含需要更改的属性
     * @param entryAuthIds 用户页面选择的角色
     * @return
     * @Title: updateRoleWithAuthIds
     * @Description: 更新角色，附带更新他赋予的权限
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateRoleWithAuthIds(Role role, List<Long> entryAuthIds) {
        if (role == null || role.getRoleId() == null) {
            return false;
        }
        long roleId = role.getRoleId();

        // 0.需要更改Code时，验证如果有数据库中已有Code且不为roleId自己，则不能修改
        if (role.getCode() != null) {
            Role roleCodeExist = getRoleByCode(role.getCode());
            if (roleCodeExist != null && roleId != roleCodeExist.getRoleId()) {
                return false;
            }
        }

        // 1.修改role的属性
        if (!updateByPrimaryKeySelective(role)) {
            return false;
        }

        // 2.查找该角色更新前拥有的权限
        List<Long> oldAuthIds = roleAuthorityService
            .selectAuthsByRoleId(roleId);
/*		// 得到数据库需要新增的权限列表
        List<Long> oldAuthIdsCopy = new ArrayList<Long>();
		oldAuthIdsCopy.addAll(oldAuthIds);*/
        List<Long> newAuthIds = resolveOldAndNewAuths(oldAuthIds, entryAuthIds);

        // 3.删除旧的权限
        if (oldAuthIds != null) {
            for (long deleteAuth : oldAuthIds) {
                System.out.println(deleteAuth);
                if (!roleAuthorityService.deleteRoleAuthority(roleId,
                    deleteAuth)) {
                    throw new TransactionException();
                }
            }
        }

        // 4.增加新的权限
        if (newAuthIds != null) {
            for (long addAuth : newAuthIds) {
                if (!roleAuthorityService.insert(roleId, addAuth)) {
                    throw new TransactionException();
                }
            }
        }

        // 全部完成，返回true
        return true;
    }

    /**
     * @param oldAuthIds   已有的旧权限列表
     * @param entryAuthIds 用户提交的权限列表
     * @return List<Long> 数据库新增加的权限
     * @Title: resolveOldAndNewAuths
     * @Description: 处理旧权限和需要添加新权限间的关系，运行完结果是oldAuthIds为需要删除的权限，返回值为需要新增的权限
     */
    private List<Long> resolveOldAndNewAuths(List<Long> oldAuthIds,
                                             List<Long> entryAuthIds) {
        List<Long> newAuthIds = new ArrayList<Long>();
        if (entryAuthIds == null || entryAuthIds.size() == 0) {// 说明没有需要加入的权限，则保留老的权限
            //oldAuthIds.clear();
            return null;
        } else if (oldAuthIds == null || oldAuthIds.size() == 0) {// 说明数据库没有旧的权限，即数据库需要加入所有权限entryAuthIds
            return entryAuthIds;
        } else {
            for (Long auth : entryAuthIds) {
                if (oldAuthIds.contains(auth)) { // 说明要删除和要添加同一种权限，则无需操作
                    oldAuthIds.remove(auth);
                } else { // 说明旧的权限里没有auth,则需要添加Auth
                    newAuthIds.add(auth);
                }
            }
            return newAuthIds;
        }
    }

    /**
     * @return int
     * @Title: deleteByPrimaryKey
     * @Description: 删除指定id的权限, 并且有级联删除(RoleAuthorrity和AdminRole)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteRoleById(Long roleId) {
        // 0.检查参数是否为空
        if (roleId == null) {
            return false;
        }

        // 1.查找该Role是否存在
        Role role = getRoleById(roleId);
        if (role == null) {
            return false;
        }

        // 2.删除Role表中的Role,逻辑删除
        role.setUpdateTime(new Date());
        role.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
        if (roleMapper.updateByPrimaryKeySelective(role) <= 0) {
            return false;
        }

        // 3.删除RoleAuthorrity表中相关role记录
        if (!roleAuthorityService.deleteByRoleId(roleId)) {
            throw new TransactionException();
        }

        // 4.删除AdminRole中的相关role记录
        if (!adminRoleService.deleteByRoleId(roleId)) {
            throw new TransactionException();
        }

        // 全部完成
        return true;
    }

    /**
     * @param QueryObject
     * @return int
     * @Title: queryPaginationRoleCount
     * @Description: 分页信息的查询，按角色名称模糊查询，返回符合的角色的个数,使用QueryObject
     */
    @Override
    public int queryPaginationRoleCount(QueryObject query) {
        return roleMapper.queryPaginationRoleCount(query.getQueryCriterias());
    }

    /**
     * @param QueryObject
     * @return List<Role>
     * @Title: queryPaginationRoleList
     * @Description: 分页信息的查询，按角色名称模糊查询，返回符合的分页角色,使用QueryObject
     */
    @Override
    public List<Role> queryPaginationRoleList(QueryObject queryObject) {
        return roleMapper.queryPaginationRoleList(queryObject.toMap());
    }


    /**
     * 根据用户角色查找该角色可以分配的角色
     */
    @Override
    public List<Role> getCreateRoleByRoleId(Long roleId) {
        return roleMapper.getCreateRoleByRoleId(roleId);
    }

    @Override
    public List<Role> getAllRoles() {

        return roleMapper.getAllRoles();
    }

}
