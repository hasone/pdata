package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.PotentialCustomerMapper;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PotentialCusterService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sunyiwei on 2016/3/28.
 */
@Service
public class PotentialCusterServiceImpl implements PotentialCusterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCusterServiceImpl.class);

    @Autowired
    PotentialCustomerMapper mapper;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Autowired
    AdministerService administerService;

    @Autowired
    ManagerService managerService;

    @Autowired
    EntManagerService entManagerService;

    @Autowired
    AdminManagerService adminManagerService;

    @Autowired
    AdminRoleService adminRoleService;

    @Autowired
    RoleAuthorityService roleAuthorityService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    SendMsgServiceImpl sendMsgServiceImpl;
    @Autowired
    SendMsgService sendMsgService;
    @Override
    @Transactional
    public boolean savePotentialEnterprise(Enterprise enterprise, String cmPhone, String emPhone, String emName, Long currentUserId) {
        //1、插入企业
        if (!enterprisesService.insertSelective(enterprise)) {
            LOGGER.error("用户ID:" + currentUserId + " 创建新企业名称:" + enterprise.getName() + "失败");
            throw new RuntimeException("插入新企业失败");
        }

        //2.插入默认短信模板信息
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(enterprise.getId());

        //3.创建企业管理员，并创建客户经理与企业管理员的上下关联        
        Administer cm = administerService.selectByMobilePhone(cmPhone);
        Manager manager = managerService.selectByAdminId(cm.getId());
        Manager entManager = new Manager();
        entManager.setName(enterprise.getEntName());

        //拥有可创建企业管理员权限的角色，可以创建企业管理员
        List<RoleAuthority> ars = roleAuthorityService.selectExistingRoleAuthorityByAuthorityName("ROLE_ENTERPRISE_SET_MANAGER");
        if (ars != null && ars.size() > 0) {
            entManager.setRoleId(ars.get(0).getRoleId());
        } else {
            LOGGER.error("没有企业管理员可关联的角色，需要将权限项ROLE_ENTERPRISE_SET_MANAGER分配给某个角色");
            throw new RuntimeException("没有企业管理员可关联的角色");
        }

        //创建企业管理员
        if (!managerService.createManager(entManager, manager.getId())) {
            LOGGER.error("用户ID:{}创建企业管理员失败，Manager={}", currentUserId, JSON.toJSONString(entManager));
            throw new RuntimeException("创建企业管理员失败");
        }

        //4、企业关联到企业管理员
        EntManager em = new EntManager();
        em.setEnterId(enterprise.getId());
        em.setCreatorId(currentUserId);
        em.setManagerId(entManager.getId());
        if (!entManagerService.insertEntManager(em)) {
            LOGGER.error("用户ID:{}关联企业到企业管理员，插入ent_manager失败，EntManager={}", currentUserId, JSON.toJSONString(em));
            throw new RuntimeException("关联企业和企业管理员");
        }

        //5、企业管理员的用户关联到企业管理员(相当于为用户设置企业管理员身份)
        Administer emUser = administerService.selectByMobilePhone(emPhone);
        Administer newAdmin = new Administer();
        newAdmin.setMobilePhone(emPhone);
        newAdmin.setUserName(emName);
        if ("1".equals(loginType())) {
            LOGGER.info("初始化静态密码" + "123456");
            newAdmin.setPassword("123456");
        }
        
        if ("4".equals(loginType())) {
            String password = SerialNumGenerator.buildBossReqSerialNum(6);
            
            String content = "流量平台初始化静态密码" + password + ", 请妥善保管，登录平台后请尽快更改密码。";
            LOGGER.info(content);
            sendMsgService.sendMessage(emPhone, content, MessageType.RANDOM_CODE.getCode());
            newAdmin.setPassword(password);
        }
        if (!administerService.createAdminister(entManager.getId(), newAdmin, emUser, currentUserId)) {
            LOGGER.error("操作失败，用户ID-" + currentUserId + "将手机号" + emPhone + "用户设置为ManagerId:" + entManager.getId());
            throw new RuntimeException("将用户设置为企业管理员失败");
        }

        return true;
    }


    @Override
    @Transactional
    public boolean saveEditPotential(Enterprise enterprise, String cmPhone, String emPhone, String emName, Long currentUserId) {
        //1、更新企业记录
        if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
            LOGGER.error("用户ID:{}，更新企业失败，enterprise={}", currentUserId, JSON.toJSONString(enterprise));
            throw new RuntimeException("插入新企业失败");
        }

        //2.更新客户经理和企业管理员的关联
        Long managerId = entManagerService.getManagerIdForEnter(enterprise.getId());
        Manager entManagerOrg = managerService.selectByPrimaryKey(managerId);

        //新客户经理用户号码获得的用户管理员身份
        Administer cm = administerService.selectByMobilePhone(cmPhone);
        Manager manager = managerService.selectByAdminId(cm.getId());

        Manager entManagerNew = new Manager();
        entManagerNew.setParentId(manager.getId());
        entManagerNew.setId(entManagerOrg.getId());
        if (!managerService.updateByPrimaryKeySelective(entManagerNew)) {
            LOGGER.error("用户ID:{}，更新企业管理员和客户经理关联失败，entManagerNew={}", currentUserId, JSON.toJSONString(entManagerNew));
            throw new RuntimeException("更新企业管理员和客户经理关联失败");
        }

        //3、更新企业管理员用户和企业管理员的关联
        List<Long> adminIds = adminManagerService.selectAdminIdByManagerId(managerId);
        for (Long adminId : adminIds) {
            if (!adminRoleService.deleteByAdminId(adminId)) {
                LOGGER.error("用户ID:{}，删除原用户和企业管理员角色的关联失败，adminId={}", currentUserId, adminId);
                throw new RuntimeException("更新企业管理员和客户经理关联失败");
            }
            //为原用户分配普通用户的权限
            AdminManager adminManager = new AdminManager();
            adminManager.setAdminId(adminId);
            adminManager.setManagerId(-1L);
            adminManager.setCreatorId(currentUserId);
            //插入admin_manager表
            if (!adminManagerService.insertAdminManager(adminManager)) {
                throw new RuntimeException("插入admin_manager失败！");
            }
            //插入admin_role表
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(-1L);
            if (!adminRoleService.insertAdminRole(adminRole)) {
                throw new RuntimeException("插入admin_role失败！");
            }
        }
        //删除原用户和企业管理员身份的关联
        if (!adminManagerService.deleteAdminByEntId(enterprise.getId())) {
            LOGGER.error("用户ID:{}，删除原用户和企业管理员身份的关联失败，enterprise_id={}", currentUserId, enterprise.getId());
            throw new RuntimeException("更新企业管理员和客户经理关联失败");
        }

        //新的企业管理员的用户关联到企业管理员(相当于为用户设置企业管理员身份)
        Administer emUser = administerService.selectByMobilePhone(emPhone);
        Administer newAdmin = new Administer();
        newAdmin.setMobilePhone(emPhone);
        newAdmin.setUserName(emName);
        if (!administerService.createAdminister(managerId, newAdmin, emUser, currentUserId)) {
            LOGGER.error("操作失败，用户ID-" + currentUserId + "将手机号" + emPhone + "用户设置为ManagerId:" + managerId);
            throw new RuntimeException("将用户设置为企业管理员失败");
        }

        return true;
    }
    /**
     * @return
     */
    public String loginType() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOGIN_TYPE.getKey());
//        String multCheckLogin = globalConfigService.get(GlobalConfigKeyEnum.LOGIN_TYPE.getKey());
//        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(multCheckLogin) ? "false" : multCheckLogin;
//        return Boolean.parseBoolean(finalFlag);
    }
}
