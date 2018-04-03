package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.province.dao.ManagerMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>Title: </p> <p>Description: </p>
 */
@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerServiceImpl.class);
    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    AdministerService administerService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public Manager selectByAdminId(Long adminId) {
        return managerMapper.selectByAdminId(adminId);
    }

    @Override
    public boolean createManager(Manager manager, Long currentManagerId) {
        if (manager.getRoleId() == null || StringUtils.isEmpty(manager.getName()) || currentManagerId == null) {
            LOGGER.error("创建manager时缺少必要参数,roleId={},name={},currentManagerId={}", manager.getRoleId(),
                    manager.getName(), currentManagerId);
            return false;
        }

        manager.setParentId(currentManagerId);
        manager.setCreateTime(new Date());
        manager.setUpdateTime(new Date());
        manager.setDeleteFlag((byte) DELETE_FLAG.UNDELETED.getValue());

        return managerMapper.insert(manager) == 1;
    }

    @Override
    public int selectByParentIdCount(Long parentId, QueryObject queryObject) {
        Map map = queryObject.toMap();
        map.put("parentId", parentId);
        return managerMapper.selectByParentIdCount(map);
    }

    @Override
    public List<Manager> selectByParentId(Long parentId) {
        return managerMapper.selectByParentId(parentId);
    }

    @Override
    public String getChildNodeString(Long rootId) {
        List<Long> children = getChildNodeInternal(rootId);
        return (children != null && !children.isEmpty()) ? org.apache.commons.lang.StringUtils.join(children, ','): null;
    }
    
    @Override
    public List<Long> getChildNode(Long rootId) {
        return getChildNodeInternal(rootId);
    }

    //不依赖于存储过程,由应用层进行递归的实现
    private List<Long> getChildNodeInternal(Long rootId) {
        long curr = System.currentTimeMillis();
        LOGGER.debug("start getChildNodeInternal...");

        //生成父结点
        List<Long> result = new LinkedList<Long>();
        result.add(rootId); //默认把当前节点加到结果中

        List<Long> tmp = new LinkedList<Long>();
        tmp.add(rootId);

        while (true) {
            //获取直接子节点
            tmp = managerMapper.getDirectChild(tmp);
            if (tmp == null || tmp.isEmpty()) {
                break;
            }

            //如果节点集合不为空,则继续向下查找
            result.addAll(tmp); //将所有结点加入到result中
        }

        LOGGER.debug("complete getChildNodeInternal in {}s.", ((double) (System.currentTimeMillis() - curr)) / 1000.);
        return result;
    }

    @Override
    public Manager selectByPrimaryKey(Long id) {
        return managerMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(Manager record) {
        record.setUpdateTime(new Date());
        return managerMapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public List<Manager> selectByParentIdForPage(Long parentId, QueryObject queryObject) {
        Map map = queryObject.toMap();
        map.put("parentId", parentId);
        return managerMapper.selectByParentIdForPage(map);
    }

    /**
     * 获取以节点为起始的子树
     */
    @Override
    public List<Manager> getSonTreeByManangId(Long manageId) {
        if (manageId == null) {
            return null;
        }

        //至少会返回当前节点,因此不需要进行其它判断
        List<Long> managerIds = getSonTreeIdByManageId(manageId);
        return managerMapper.getManagers(managerIds);
    }

    /**
     * 通过用户ID获取对应的管理员
     */
    @Override
    public Manager getManagerByAdminId(Long adminId) {
        return adminId == null ? null : managerMapper.selectManagerByadminId(adminId);
    }

    @Override
    public List<Long> getSonTreeIdByManageId(Long manageId) {
        if (manageId == null) {
            return null;
        }

        return getChildNodeInternal(manageId);
    }

    @Override
    public Manager selectHigherFatherNodeByParentId(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return managerMapper.selectHigherFatherNodeByParentId(parentId);
    }

    /**
     * 通过父节点ID找它所有的儿子节点ID  (补充：这个方法只能查找到下一层的所有子节点)
     */
    @Override
    public List<Long> selectSonIdsByParentId(Long manageId) {
        return manageId == null ? null : managerMapper.selectSonIdsByParentId(manageId);
    }

    @Override
    public String getFullNameByCurrentManagerId(String fullname, Long managerId) {
        Manager manager = selectByPrimaryKey(managerId);
        if (manager == null || manager.getId().equals(manager.getParentId())) {
            return fullname;
        } else {
            //企业管理员--》客户经理--》市级管理员--》省级管理员，其中山东客户经理与市级管理员所属地区相同，避免重复添加
            if (org.apache.commons.lang.StringUtils.isNotBlank(manager.getName())
                    && !manager.getName().equalsIgnoreCase(fullname)) {
                fullname = manager.getName() + fullname;
            }
            return getFullNameByCurrentManagerId(fullname, manager.getParentId());
        }
    }

    @Override
    public Manager get(Long roleId, String name) {
        if (roleId == null || org.apache.commons.lang.StringUtils.isBlank(name)) {
            return null;
        }

        return managerMapper.getByRoleIdAndName(roleId, name);
    }

    @Override
    public Manager getByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }

        return managerMapper.getByPhone(phone);
    }

    @Override
    public List<Manager> selectEntParentNodeByEnterIdOrRoleId(Long enterId, Long roleId) {
        if (enterId == null) {
            return null;
        }
        return managerMapper.selectEntParentNodeByEnterIdOrRoleId(enterId, roleId);
    }

    @Override
    public List<Administer> getChildAdminByCurrentManageId(Long manageId) {
        if (manageId != null) {
            List<Long> childIds = getSonTreeIdByManageId(manageId);
            if (childIds != null && childIds.size() > 0) {

                List<Administer> administers = administerService.getByManageIds(childIds);

                return administers;
            }
        }
        return null;
    }

    @Override
    public Long selectManagerIdByEntIdAndRoleId(Long entManageId, Long roleId) {
        if (entManageId == null || roleId == null) {
            return null;
        }
        return managerMapper.selectManagerIdByEntIdAndRoleId(entManageId, roleId);
    }

    @Override
    public List<Long> getByRoleId(Long roleId) {
        // TODO Auto-generated method stub
        return managerMapper.getByRoleId(roleId);
    }

    @Override
    public boolean isParentManage(Long sonManageId, Long currentManageId) {
        //传入的id需要是当前用户的子节点
        String childrenStr = getChildNodeString(currentManageId);
        if (org.apache.commons.lang.StringUtils.isBlank(childrenStr)) {
            return false;
        }
        String[] children = childrenStr.split(",");
        for (String child : children) {
            if (child.equals(String.valueOf(sonManageId))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean managedByManageId(Long entId, Long manageId) {
        // TODO Auto-generated method stub
        //获取企业列表
        Manager manager = selectByPrimaryKey(manageId);
        if(manager!=null){
            //8是制卡专员，制卡专员下不挂企业，所以直接返回true
            if("8".equals(manager.getRoleId().toString())){
                return true;
            }
        }
        List<Enterprise> enterprises = enterprisesService.getNomalEnterByManagerId(manageId);
        if(enterprises!=null && enterprises.size()>0){
            for(Enterprise item : enterprises){
                if(item.getId().toString().equals(entId.toString())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isProOrCityOrMangerOrEnt(Long roleId) {
        if (roleId == null) {
            return false;
        }
        if (roleId.toString().equalsIgnoreCase(
                globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_MANAGER_ROLE_ID.getKey()))
                || roleId.toString().equalsIgnoreCase(
                        globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey()))
                || roleId.toString().equalsIgnoreCase(
                        globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey()))
                || roleId.toString().equalsIgnoreCase(
                        globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey()))) {
            return true;
        }
        return false;
    }

    @Override
    public List<Manager> getParentTreeByManangId(Long manageId) {
        if(manageId == null){
            return new ArrayList<Manager>();
        }
        
        //至少会返回当前节点,因此不需要进行其它判断
        List<Long> managerIds = getParentTreeIds(manageId);
        return managerMapper.getManagers(managerIds);
    } 
    
    /**
     * 不依赖于存储过程,由应用层进行递归的实现查找父树
     */
    protected List<Long> getParentTreeIds(Long rootId) {
        //生成所有
        List<Long> result = new LinkedList<Long>();
        result.add(rootId); //默认把当前节点加到结果中

        Long currentId = rootId;
        while (true) {
            
            Manager manager = selectByPrimaryKey(currentId);
            
            //如果无法向上获取到节点，或者已经到达跟节点，则结束
            if(manager == null || manager.getParentId() == null || 
                    manager.getParentId().equals(currentId)){
                break;
            }
            
            result.add(manager.getParentId());
            currentId = manager.getParentId();
        }

        return result;
    }

    @Override
    public Manager getParentNodeByRoleId(Long manageId, Long roleId) {
        List<Manager> managers = getParentTreeByManangId(manageId);
        for(Manager manager : managers){
            if(manager.getRoleId() != null && manager.getRoleId().equals(roleId)){
                return manager;
            }
        }
        return null;
    }
}
