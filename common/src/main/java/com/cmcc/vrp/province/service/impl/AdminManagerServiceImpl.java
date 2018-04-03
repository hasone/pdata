package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.province.dao.AdminManagerMapper;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.ManagerService;

/**
 * 
 * @ClassName: AdminManagerServiceImpl 
 * @Description: TODO
 */
@Service("adminManagerService")
public class AdminManagerServiceImpl implements AdminManagerService {
    @Autowired
    AdminManagerMapper adminManagerMapper;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    AdminRoleService adminRoleService;

    @Override
    public boolean insertAdminManager(AdminManager adminManager) {
        adminManager.setCreateTime(new Date());
        adminManager.setUpdateTime(new Date());
        adminManager.setDeleteFlag(Constants.UNDELETED_FLAG);
        if (adminManager.getAdminId() == null || adminManager.getCreatorId() == null
                || adminManager.getManagerId() == null) {
            return false;
        }
        return adminManagerMapper.insert(adminManager) == 1;
    }

    @Override
    public List<Long> getAdminIdForEnter(Long entId) {
        return adminManagerMapper.getAdminIdForEnter(entId);
    }

    @Override
    public boolean deleteAdminByEntId(Long entId) {
        return adminManagerMapper.deleteAdminByEntId(entId) > 0;
    }

    @Override
    public List<Long> selectAdminIdByManagerId(Long managerId) {
        return adminManagerMapper.selectAdminIdByManagerId(managerId);
    }

    @Override
    public List<Administer> getAdminForEnter(Long entId) {
        return adminManagerMapper.getAdminForEnter(entId);
    }

    @Override
    public Long selectManagerIdByAdminId(Long adminId) {
        return adminManagerMapper.selectManagerIdByAdminId(adminId);
    }

    @Override
    public List<Administer> getCustomerManagerByEntId(Long entId) {
        if (entId == null) {
            return null;
        }
        Manager manager = entManagerService.getManagerForEnter(entId);
        Manager fatherManager = managerService.selectHigherFatherNodeByParentId(manager.getParentId());
        return getAdminByManageId(fatherManager.getId());
    }

    @Override
    public List<Administer> getAdminByManageId(Long manageId) {
        // TODO Auto-generated method stub
        if (manageId == null) {
            return null;
        }
        return adminManagerMapper.getAdminByManageId(manageId);
    }

    @Override
    @Transactional
    public boolean editUserManager(AdminManager adminManager) {
        if (adminManager.getManagerId().equals(-1L)) {
            //插入admin_manager表
            AdminManager adminManagerNew = new AdminManager();
            adminManagerNew.setAdminId(adminManager.getAdminId());
            adminManagerNew.setManagerId(-1L);
            adminManagerNew.setCreatorId(adminManager.getCreatorId());

            //插入admin_role表
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminManager.getAdminId());
            adminRole.setRoleId(-1L);

            if (adminManagerMapper.deleteByAdminId(adminManager.getAdminId()) == 1
                    && adminRoleService.deleteByAdminId(adminManager.getAdminId())
                    && insertAdminManager(adminManagerNew) && adminRoleService.insertAdminRole(adminRole)) {
                return true;
            }
        } else {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminManager.getAdminId());
            Manager newManager = managerService.selectByPrimaryKey(adminManager.getManagerId());
            if (newManager != null) {
                adminRole.setRoleId(newManager.getRoleId());
                if (adminManagerMapper.deleteByAdminId(adminManager.getAdminId()) == 1
                        && insertAdminManager(adminManager)
                        && adminRoleService.deleteByAdminId(adminManager.getAdminId())
                        && adminRoleService.insertAdminRole(adminRole)) {
                    return true;
                }
            }
        }

        throw new RuntimeException();
    }

    @Override
    public boolean deleteManagerByAdmin(Long adminId) {
        if (adminManagerMapper.selectManagerIdByAdminId(adminId) != null) {
            return adminManagerMapper.deleteByAdminId(adminId) == 1;
        }
        return true;
    }

    @Override
    public boolean updateByPrimaryKeySelective(AdminManager record) {
        if (record == null || record.getId() == null) {
            return false;
        }
        return adminManagerMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public AdminManager selectByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }
        return adminManagerMapper.selectByAdminId(adminId);
    }

    @Override
    public Manager getManagerByAdminId(Long adminId) {
        List<Manager> list = adminManagerMapper.getManagerForAdminId(adminId);
        return list.isEmpty()?null:list.get(0);
    }

    @Override
    public List<Administer> getAdminByManageIds(List<Manager> manageIds) {
        if(manageIds.isEmpty()){
            return new ArrayList<Administer>();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("managers", manageIds);
        return adminManagerMapper.getAdminByManageIds(map);
    }

}
