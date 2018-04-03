package com.cmcc.vrp.boss.shangdong.boss.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdministerMapper;
import com.cmcc.vrp.province.dao.ManagerMapper;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.StringUtils;

/**
 * 
* <p>Description: </p>
* @author panxin
* @date 2016年11月12日 下午3:09:43
 */
@Service
public class SdAdministerServiceImpl {
    @Autowired
    @Qualifier("administerMapper")
    private AdministerMapper administerMapper;

    @Autowired
    ManagerMapper managerMapper;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    AdminRoleService adminRoleService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    ManagerService managerService;
    
    @Autowired
    EntManagerService entManagerService;
    
    @Autowired
    AdminManagerService adminManagerService;
    
    private static final Logger logger = Logger.getLogger(SdAdministerServiceImpl.class);

    /**
     * 创建客户经理
     * @param administer  账号信息
     * @param roleId      角色ID
     * @param enterpriseCode  企业code
     * @param parentRoleId   父亲角色ID
     * @return 是否成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertManager(Administer administer, Long roleId, List<String> enterpriseCode, Long parentRoleId) {

        // 1.参数检查
        if (administer == null) {
            logger.info("1.failed");
            return false;
        }

        logger.info("1.passed");
        // 2.检查重复问题
        if (administerService.selectByMobilePhone(administer.getMobilePhone()) != null) {// 表中已有同名用户
            logger.info("2.failed");
            return false;
        }
        logger.info("2.passed");
        // 无名字的用户直接用手机号做用户名
        if (StringUtils.isEmpty(administer.getUserName())) {// 表中已有同名用户
            administer.setUserName(administer.getMobilePhone());
        }

        // 3.插入用户
        if (!administerService.insertSelective(administer)) {
            logger.info("3.failed");
            return false;
        }
        logger.info("插入管理员成功：id:" + administer.getId());
        logger.info("3.passed");

        // 4.插入角色用户关系表(adminRole)
        if (!adminRoleService.insertAdminRole(administer.getId(), roleId)) {
            logger.info("4.failed");
            throw new TransactionException();
        }
        logger.info("4.passed");

        // 5.创建manager节点(指向市级节点)
        Manager parentManager = managerService.get(parentRoleId, administer.getCitys());//根据城市名称，去市级manager节点
        if (parentManager == null) {
            logger.info("5.1 failed");
            throw new TransactionException();
        }
        Manager manager = new Manager();
        manager.setRoleId(roleId);
        manager.setName(administer.getCitys());//客户经理使用手机号做manager的名字
        if (!managerService.createManager(manager, parentManager.getId())) {
            logger.info("5.2 failed");
            throw new TransactionException();
        }
        logger.info("5.passed");
        
        // 6.更新企业节点指向manager
        updateEntManager(enterpriseCode, manager.getId());
        
        //7.把客户经理挂到节点上(admin_manager表)
        if (!insertAdminManager(administer.getId(), manager.getId())) {
            logger.info("插入客户经理节点关系表失败，7.fail");
            throw new TransactionException();
        }
        logger.info("7.passed");
        // 全部完成
        return true;
    }

    /**
     * 更新客户经理关系
     * @param administer  客户经理账号信息
     * @param enterpriseCode  企业code
     * @return   是否成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateManager(Administer administer, List<String> enterpriseCode, Long roleId, Long parentRoleId) {

        if (administer == null || administer.getId() == null) {
            return false;
        }

        // 2. 更新用户表记录
        if (!administerService.updateSelective(administer)) {
            return false;
        }

        // 3.1查找市级manager节点
        Manager parentManager = managerService.get(parentRoleId, administer.getCitys());//根据城市名称，去市级manager节点
        if (parentManager == null) {
            logger.info("未找到市级manager节点， 3.1 failed");
            throw new TransactionException();
        }
        
        // 3.2查找客户经理manager节点
        Manager manager = managerService.getByPhone(administer.getMobilePhone());//根据手机号码去找自己的manager节点
        if (manager == null) {
            //未找到节点，则做新增操作
            manager = new Manager();
            manager.setRoleId(roleId);
            manager.setName(administer.getMobilePhone());//客户经理使用手机号做manager的名字
            if (!managerService.createManager(manager, parentManager.getId())) {
                logger.info("插入客户经理节点失败，3.2 failed");
                throw new TransactionException();
            }
            //把客户经理挂到节点上(admin_manager表)
            if (!insertAdminManager(administer.getId(), manager.getId())) {
                logger.info("插入客户经理节点关系表失败，3.2.fail");
                throw new TransactionException();
            }
        } else {
            //修改节点信息，指向新市级节点
            manager.setParentId(parentManager.getId());
            if (!managerService.updateByPrimaryKeySelective(manager)) {
                logger.info("修改客户经理节点失败，3.2 failed");
                throw new TransactionException();
            }
            
            //查找以前属于自己的企业，但现在不属于自己的企业节点，修改指向市级节点
            Administer administer2 = new com.cmcc.vrp.province.model.Administer();
            administer2.setId(administer.getId());
            List<Enterprise> oldChildEntList = enterprisesService.getEnterpriseListByAdminId(administer2);
            
            if (!oldChildEntList.isEmpty()){
                Manager oldChildManager;
                for (Enterprise oldChildEnt : oldChildEntList) {
                    if (!enterpriseCode.contains(oldChildEnt.getCode())) {
                        //如果不包含旧企业code，需要把旧节点指向市级节点
                        //根据企业id找到自己的节点
                        oldChildManager = entManagerService.getManagerForEnter(oldChildEnt.getId());
                        
                        //更新节点
                        if (oldChildManager != null) {
                            oldChildManager.setParentId(manager.getParentId());
                            managerService.updateByPrimaryKeySelective(oldChildManager);
                        }
                    }
                }
            }
        }
        logger.info("3.passed");
        
        // 4.更新所属企业节点指向manager
        updateEntManager(enterpriseCode, manager.getId());
        return true;
    }

    /**
     * 插入AdminManager记录
     * @param adminId 
     * @param ManagerId
     * @return  是否插入成功
     */
    private boolean insertAdminManager(Long adminId, Long managerId){
        AdminManager am = new AdminManager();
        am.setAdminId(adminId);
        am.setManagerId(managerId);
        am.setCreatorId(1L);
        return adminManagerService.insertAdminManager(am);
    }
    
    /**
     * 根据企业code，更新企业manager节点信息
     * @param enterpriseCode  企业Code
     * @param parentId   父节点id
     */
    private void updateEntManager(List<String> enterpriseCode, Long parentId) {
        if (enterpriseCode != null) {
            Manager childManager;
            for (String code : enterpriseCode) {
                logger.info("enterCode:" + code);
                childManager = entManagerService.getManagerForEnterCode(code);

                if (childManager != null) {
                    logger.info("enterCode:" + code);
                    childManager.setParentId(parentId);
                    if (!managerService.updateByPrimaryKeySelective(childManager)) {
                        logger.info("企业manager节点更新失败");
                        throw new TransactionException();
                    }
                }
            }
        }
    }
}
