package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdministerMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.model.ChangeMobileRecord;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.module.UserStatisticModule;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.BlurService;
import com.cmcc.vrp.province.service.ChangeMobileRecordService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.PasswordEncoder;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.model.Tmpaccount;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  AdministerServiceImpl
 */
@Service("administerService")
public class AdministerServiceImpl implements AdministerService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdministerServiceImpl.class);
    @Autowired
    MdrcCardmakerService mdrcCardMakerService;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private AdministerMapper administerMapper;
    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminEnterService adminEnterService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IndividualProductService individualProductService;

    @Autowired
    private IndividualProductMapService individualProductMapService;

    @Autowired
    private IndividualAccountService individualAccountService;

    @Autowired
    private EntManagerService entManagerService;
    
    @Autowired
    private TmpaccountMapper tmpaccountMapper;
    @Autowired
    ChangeMobileRecordService changeMobileRecordService;
    
    @Autowired
    BlurService blurService;

    /**
     * @return
     * @Title: selectAdministerById
     * @Description: 通过主键寻找Administer
     */
    @Override
    public Administer selectAdministerById(Long id) {
        if (id == null) {
            return null;
        }
        Administer admin = administerMapper.selectByPrimaryKey(id);
        Manager manager = managerService.selectByAdminId(id);
        if (manager != null) {
            admin.setRoleId(manager.getRoleId());
        }

        return admin;
    }

    /**
     * @return
     * @Title: selectByUserName
     * @Description: 通过姓名寻找Administer
     */
    @Override
    public List<Administer> selectByUserName(String userName) {
        if (userName == null) {
            return null;
        }
        return administerMapper.selectByUserName(userName);
    }

    /**
     * @param administer   ： 要增加的用户
     * @param roleId       ： 角色ID
     * @param enterpriseId : 新增的用户角色为企业关键人时，这个参数不能为空
     * @return 0为失败，1为成功
     * @throws
     * @Title:insertAdminister
     * @Description: 增加用户操作，涉及到级联操作 1. 增加用户 2. 增加用户和角色的关联 3. 如果用户的角色不是超级管理员，
     * 还要增加企业相关信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertAdminister(Administer administer, Long roleId, Long enterpriseId) {
        final String FLOWCARD_ADMIN = getFLOWCARD_ADMIN();
        final String ENTERPRISE_CONTACTOR = getENTERPRISE_CONTACTOR();

        // 1.参数检查，如果插入角色是企业关键人或流量卡管理员，必须要有企业。
        if (administer == null || !checkAdminister(administer) || roleId == null
                || (roleId.toString().equals(ENTERPRISE_CONTACTOR) && enterpriseId == null)
                || (roleId.toString().equals(FLOWCARD_ADMIN) && enterpriseId == null)) {
            return false;
        }

        // 2.检查重复问题
        //		if (selectByUserName(administer.getUserName()) != null) {// 表中已有同名用户
        //			return false;
        //		}

        // 3.插入用户
        if (!insertSelective(administer)) {
            return false;
        }

        // 4.插入角色用户关系表(adminRole)
        if (!adminRoleService.insertAdminRole(administer.getId(), roleId)) {
            throw new TransactionException();
        }

        // 5.如果插入角色是企业关键人或流量卡关键人，插入用户企业表(adminEnter)
        if ((roleId.toString().equals(ENTERPRISE_CONTACTOR))
                && !adminEnterService.insert(administer.getId(), enterpriseId)) {
            throw new TransactionException();
        } else if (roleId.toString().equals(FLOWCARD_ADMIN)) {
            //6.如果是制卡专员，需要将OperatorId更新
            MdrcCardmaker mdrcCardmaker = mdrcCardMakerService.selectByPrimaryKey(enterpriseId);
            if (mdrcCardmaker == null) {
                throw new TransactionException();
            }

            mdrcCardmaker.setOperatorId(administer.getId());
            mdrcCardMakerService.update(mdrcCardmaker);
        }

        // 全部完成
        return true;
    }

    /**
     * 手机号码首次登陆平台，插入用户表
     */
    @Override
    @Transactional
    public boolean insert(String mobile) {
        Administer admin = new Administer();
        admin.setMobilePhone(mobile);
        admin.setCreatorId(0L);
        if (!insertSelective(admin)) {
            throw new RuntimeException("插入用户表失败！");
        }
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(admin.getId());
        adminManager.setManagerId(-1L);
        adminManager.setCreatorId(0L);
        //插入admin_manager表
        if (!adminManagerService.insertAdminManager(adminManager)) {
            throw new RuntimeException("插入admin_manager失败！");
        }
        //插入admin_role表
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId());
        adminRole.setRoleId(-1L);
        if (!adminRoleService.insertAdminRole(adminRole)) {
            throw new RuntimeException("插入admin_role失败！");
        }
        return true;
    }

    /**
     * 四川集中化平台的用户创建
     */
    @Override
    @Transactional
    public boolean insertForScJizhong(String mobile) {
        Administer admin = new Administer();
        admin.setMobilePhone(mobile);
        admin.setCreatorId(0L);
        if (!insertSelective(admin)) {
            throw new RuntimeException("插入用户表失败！");
        }
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(admin.getId());
        adminManager.setManagerId(-1L);
        adminManager.setCreatorId(0L);
        //插入admin_manager表
        if (!adminManagerService.insertAdminManager(adminManager)) {
            LOGGER.error("插入admin_manager失败！");
            throw new RuntimeException("插入admin_manager失败！");
        }
        //插入admin_role表
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId());
        adminRole.setRoleId(-1L);
        if (!adminRoleService.insertAdminRole(adminRole)) {
            LOGGER.error("插入admin_role失败！");
            throw new RuntimeException("插入admin_role失败！");
        }

        //插入individual_product_map
        //插入individual_account
        List<IndividualProductMap> records = new ArrayList<IndividualProductMap>();
        List<IndividualAccount> accounts = new ArrayList<IndividualAccount>();
        List<IndividualProduct> products = individualProductService.selectByDefaultValue(1);
        if (products != null && products.size() > 0) {
            for (IndividualProduct product : products) {
                IndividualProductMap record = new IndividualProductMap();
                record.setAdminId(admin.getId());
                record.setDeleteFlag(0);
                record.setDiscount(100);
                record.setPrice(product.getPrice());
                record.setIndividualProductId(product.getId());
                records.add(record);

                IndividualAccount account = new IndividualAccount();
                account.setAdminId(admin.getId());
                account.setOwnerId(admin.getId());
                account.setCount(new BigDecimal(0));
                account.setDeleteFlag(0);
                account.setIndividualProductId(product.getId());
                account.setType(-1);//创建的账户都是个人的
                account.setVersion(0);
                accounts.add(account);
            }

            if (!individualProductMapService.batchInsert(records)) {
                LOGGER.error("插入individual_product_map失败！");
                throw new RuntimeException("插入individual_product_map失败！");
            }
            if (!individualAccountService.batchInsert(accounts)) {
                LOGGER.error("插入individual_account失败！");
                throw new RuntimeException("插入individual_account失败！");
            }
        }

        return true;
    }

    /**
     * @param administer
     * @return boolean
     * @throws
     * @Title:insertSelective
     * @Description:数据库的插入操作
     */
    public boolean insertSelective(Administer administer) {
        if (administer == null) {
            return false;
        }

        String szPassword = administer.getPassword();
        if (szPassword != null) {// 密码经过Md5加密后存入数据库
            szPassword = MD5.sign(szPassword, "", "utf-8");
            administer.setPassword(szPassword);
        }

        administer.setCreateTime(new Date());
        administer.setUpdateTime(new Date());
        administer.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return administerMapper.insertSelective(administer) > 0;
    }

    /**
     * @param administer
     * @return boolean
     * @throws
     * @Title:checkAdminister
     * @Description:检查插入操作时administer的参数是否正确
     */
    private boolean checkAdminister(Administer administer) {
        if (administer == null || administer.getMobilePhone() == null || administer.getCreatorId() == null) {
            return false;
        }

        return true;
    }

    /**
     * @param administer   ： 要增加的用户
     * @param enterpriseId : 新增的用户角色为企业关键人时，这个参数不能为空
     * @return 0为失败，1为成功
     * @throws
     * @Title:updateAdminister
     * @Description: 更新用户操作，角色和企业不为空时可以更新用户相关角色和用户相关企业
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateAdminister(Administer administer, Long newRoleId, Long enterpriseId) {

        // 1. 检验参数
        if (administer == null || administer.getId() == null) {
            return false;
        }
        Long administerId = administer.getId();

        // 2. 更新用户表记录
        if (!updateSelective(administer)) {
            return false;
        }

        // 3. 角色ID不为空，代表需要更新用户角色
        if (newRoleId != null) {
            // 删除旧的用户角色和增加新的用户角色,事务处理
            adminRoleService.deleteByAdminId(administerId);
            if (!adminRoleService.insertAdminRole(administerId, newRoleId)) {
                throw new TransactionException();
            }

            //角色ID为企业管理员，需更新admin_enter表
            if (newRoleId.toString().equals(getENTERPRISE_CONTACTOR())) {
                adminEnterService.deleteByAdminId(administerId);
                adminEnterService.insert(administerId, enterpriseId);
            }
        }

        return true;
    }

    /**
     * @param administer
     * @return boolean
     * @throws
     * @Title:updateSelective
     * @Description:数据库的更新
     */
    @Override
    public boolean updateSelective(Administer administer) {
        if (administer == null || administer.getId() == null) {
            return false;
        }

        // 修改md5密码 by：qh
        if (administer.getPassword() != null) {
            String szPassword = MD5.sign(administer.getPassword(), "", "utf-8");
            administer.setPassword(szPassword);
        }

        administer.setUpdateTime(new Date());
        return administerMapper.updateByPrimaryKeySelective(administer) > 0;
    }

    /**
     * @param userId
     * @param szNewPassword
     * @return
     * @Title: updateAdministerPassword
     * @Description: 更新用户密码
     */
    @Override
    public boolean updateAdministerPassword(Long userId, String szNewPassword) {
        if (userId == null || org.apache.commons.lang.StringUtils.isBlank(szNewPassword)) {
            return false;
        }

        String salt = StringUtils.randomString(64);
        String encodedPwd = passwordEncoder.encode(szNewPassword, salt);
        return administerMapper.updatePasswordByKey(userId, encodedPwd, salt) == 1;
    }

    /**
     * @return 0为失败，1为成功
     * @throws
     * @Title:deleteById
     * @Description: 删除指定Id的用户，需要级联adminRole和adminEnter
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // 1.删除admin表中数据
        Administer administer = new Administer();
        administer.setId(id);
        administer.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());

        if (!updateSelective(administer)) {
            return false;
        }

        // 2.删除adminRole
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(id);
        int nRoleCount = adminRoleService.selectCountByQuery(adminRole);
        if (nRoleCount > 0 && !adminRoleService.deleteByAdminId(id)) {
            throw new TransactionException();
        }

        // 3.删除adminEnter
        AdminEnter adminEnter = new AdminEnter();
        adminEnter.setAdminId(id);
        int nEnterCount = adminEnterService.selectCountByQuery(adminEnter);
        if (nEnterCount > 0 && !adminEnterService.deleteByAdminId(id)) {
            throw new TransactionException();
        }

        return true;
    }

    /**
     * @return int
     * @Title: queryPaginationAdminCount
     * @Description: 分页信息的查询，按姓名称和电话名称模糊查询，返回符合的角色的个数,使用QueryObject
     */
    @Override
    public int queryPaginationAdminCount(QueryObject queryObject) {
        Map<String, Object> map = queryObject.toMap();
        Long managerId = NumberUtils.toLong(String.valueOf(map.get("managerId")));
        Long currentUserId = NumberUtils.toLong(String.valueOf(map.get("currentUserId")));

        List<Long> managerIds = managerService.getChildNode(managerId);
        if (managerIds != null) {
            map.put("managerIds", managerIds);
        } else {
            map.put("adminId", currentUserId);
        }
        return administerMapper.queryPaginationAdminCount(map);
    }

    /**
     * @return List<Role>
     * @Title: queryPaginationAdminList
     * @Description: 分页信息的查询，按姓名称和电话名称模糊查询，返回符合的分页角色,使用QueryObject
     */
    @Override
    public List<Administer> queryPaginationAdminList(QueryObject queryObject) {
        Map<String, Object> map = queryObject.toMap();
        Long managerId = NumberUtils.toLong(String.valueOf(map.get("managerId")));
        Long currentUserId = NumberUtils.toLong(String.valueOf(map.get("currentUserId")));

        List<Long> managerIds = managerService.getChildNode(managerId);
        if (managerIds != null) {
            map.put("managerIds", managerIds);
        } else {
            map.put("adminId", currentUserId);
        }
        return administerMapper.queryPaginationAdminList(map);
    }

    @Override
    public Administer selectByMobilePhone(String phone) {
        // TODO Auto-generated method stub
        Administer administer = null;
        if (StringUtils.isValidMobile(phone)) {
            administer = administerMapper.selectByMobilePhone(phone);
        }
        return administer;
    }

    /**
     * @return
     * @Title: queryUserAuthoriesByMobile
     * @Description: 从用户手机获取相应的权限
     */
    @Override
    public List<Authority> queryUserAuthoriesByMobile(String mobilePhone) {
        if (mobilePhone == null || mobilePhone.trim().length() == 0) {
            return null;
        }

        return administerMapper.queryUserAuthoriesByMobile(mobilePhone);
    }

    /**
     * @return
     * @Title: selectAllAdministers
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.AdministerService#selectAllAdministers()
     */
    @Override
    public List<Administer> selectAllAdministers() {
        return administerMapper.selectAllAdministers();
    }

    /**
     * @return
     * @throws
     * @Title: uniqueCheck
     * @Description: 校验新建或编辑的姓名称或手机号码是否已存在
     */
    public boolean checkUnique(Administer administer) {
        if (administer == null) {
            return true;
        }

        Long currentId = administer.getId();
        //        String currentName = administer.getUserName();
        String currentMobile = administer.getMobilePhone();

        List<Administer> administers = selectAllAdministers();

        for (Administer queryAdminister : administers) {
            // 编辑状态时跳过当查询出来的企业Id是自己时的检验
            if (currentId != null && queryAdminister.getId().equals(currentId)) {
                continue;
            }

            //名称不做唯一性校验：20160127
            //			if (queryAdminister.getUserName().equals(currentName)) {
            //				return false;
            //			}
            if (queryAdminister.getMobilePhone().equals(currentMobile)) {
                return false;
            }

        }
        return true;
    }

    /**
     * checkNameUnique
     */
    public boolean checkNameUnique(Long id, String name) {

        if (org.apache.commons.lang.StringUtils.isBlank(name)) {
            return false;
        }

        List<Administer> list = selectAllAdministers();
        for (Administer temp : list) {
            if (temp.getUserName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean checkPassword(String password, String mobilePhone) {
        Administer administer = administerMapper.selectByMobilePhone(mobilePhone);
        if (administer == null) {
            LOGGER.info("用户不存在. Mobile = {}.", mobilePhone);
            return false;
        }

        //判断用户新密码是否为空
        boolean emptyNewPassword = org.apache.commons.lang.StringUtils.isBlank(administer.getPasswordNew());
        if (emptyNewPassword) { //新密码为空，直接比对MD5码值
            return org.apache.commons.lang.StringUtils.isNotBlank(administer.getPassword()) //原始密码不为空
                    && administer.getPassword().equals(DigestUtils.md5Hex(password)) //校验通过, 传入的是密码明文，DB存储的是MD5
                    && updateAdministerPassword(administer.getId(), password); //更新密码
        } else { //已经使用新密码存储了
            return passwordEncoder.matches(password, administer.getPasswordNew(), administer.getSalt());
        }
    }

    @Override
    public List<Administer> queryCMByDistrictId(Long districtId, Long roleId) {
        if (districtId == null || roleId == null) {
            return null;
        }
        return administerMapper.selectCustomerByDistrictId(districtId, roleId);
    }

    /**
     * 通过企业ID获取企业管理员信息
     *
     * @param enterId
     * @return
     */
    @Override
    public List<Administer> selectEMByEnterpriseId(Long enterId) {
        if (enterId == null) {
            return null;
        }
        return administerMapper.selectEMByEnterpriseId(enterId);

    }

    public String getENTERPRISE_CONTACTOR() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
    }

    public String getFLOWCARD_ADMIN() {
        return globalConfigService.get(GlobalConfigKeyEnum.FLOW_CARD_MANAGER_ROLE_ID.getKey());
    }

    @Override
    @Transactional
    public boolean createAdminister(Long managerId, Administer administer, Administer administerDB, Long currentUserId) {
        Manager manager = managerService.selectByPrimaryKey(managerId);
        if (manager == null) {
            LOGGER.error("manager为空，managerId={}", managerId);
            return false;
        }

        // 如果不存在该用户，则新创建一个用户账号
        if (administerDB == null) {
            administer.setCreatorId(currentUserId);

            // 1.参数检查
            if (!checkAdminister(administer) || managerId == null) {
                LOGGER.error("参数检查未通过");
                return false;
            }

            // 2.插入用户表administer
            if (!insertSelective(administer)) {
                LOGGER.error("插入用户表失败，admin={}", JSON.toJSONString(administer));
                throw new RuntimeException("用户创建失败");
            }
        } else {
            administer.setId(administerDB.getId());
            //删除原admin_manager，admin_role
            if (!adminManagerService.deleteManagerByAdmin(administerDB.getId())) {
                throw new RuntimeException("删除原用户职位失败");
            }
            if (!adminRoleService.deleteByAdminId(administerDB.getId())) {
                throw new RuntimeException("删除原用户角色失败");
            }
            if (!updateSelective(administer)) {
                LOGGER.error("更新用户信息失败，admin={}", JSON.toJSONString(administer));
                throw new RuntimeException("更新用户信息失败");
            }

        }

        // 3.插入用户-管理员表admin_manager
        AdminManager am = new AdminManager();
        am.setAdminId(administer.getId());
        am.setManagerId(managerId);
        am.setCreatorId(currentUserId);
        if (!adminManagerService.insertAdminManager(am)) {
            LOGGER.error("插入admin_manager失败，adminManager={}", JSON.toJSONString(am));
            throw new RuntimeException("用户分配职位失败");
        }

        //暂时保留admin_role表
        //4.插入admin_role表
        AdminRole ar = new AdminRole();
        ar.setAdminId(administer.getId());
        ar.setRoleId(manager.getRoleId());
        if (!adminRoleService.insertAdminRole(ar)) {
            LOGGER.error("插入admin_role失败，adminRole={}", JSON.toJSONString(ar));
            throw new RuntimeException("用户设置角色失败");
        }
        // 全部完成
        return true;
    }

    @Override
    public int statisticRoleCountByManangerId(Long managerId) {
        List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
        Map map = new HashMap();
        map.put("managerId", managerId);
        map.put("managerIds", managerIds);
        return administerMapper.selectRoleCountByManagerId(map);
    }

    @Override
    public List<UserStatisticModule> statisticRoleByManagerId(Long managerId, QueryObject queryObject) {
        Map map;
        if (queryObject != null) {
            map = queryObject.toMap();
            List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
            map.put("managerId", managerId);
            map.put("managerIds", managerIds);
        } else {
            map = new HashMap();
            List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
            map.put("managerId", managerId);
            map.put("managerIds", managerIds);
        }
        return administerMapper.selectRoleByManagerId(map);
    }

    @Override
    public int statisticOneRoleCountByManangerId(Long roleId, Long managerId) {
        List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
        Map map = new HashMap();
        map.put("managerId", managerId);
        map.put("managerIds", managerIds);
        map.put("roleId", roleId);
        return administerMapper.selectOneRoleCountByManangerId(map);
    }

    @Override
    public List<UserStatisticModule> statisticOneRoleByManagerId(Long roleId, Long managerId, QueryObject queryObject) {
        Map map;
        if (queryObject != null) {
            map = queryObject.toMap();
            List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
            map.put("managerId", managerId);
            map.put("managerIds", managerIds);
            map.put("roleId", roleId);
        } else {
            map = new HashMap();
            List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
            map.put("managerId", managerId);
            map.put("managerIds", managerIds);
            map.put("roleId", roleId);
        }
        return administerMapper.selectOneRoleByManagerId(map);
    }

    /**
     * 检查密码是否超过有效期
     */
    @Override
    public boolean checkPasswordUpdateTime(String mobile) {
        Administer admin = selectByMobilePhone(mobile);
        String date = globalConfigService.get(GlobalConfigKeyEnum.PASSWORD_EXPIRE_TIME.getKey());
        if (admin != null && admin.getPasswordUpdateTime() != null && date != null && !"-1".equals(date)) {
            Date time = DateUtil.getDateBefore(new Date(), Integer.parseInt(date));
            if (admin.getPasswordUpdateTime().before(time)) {
                return false;
            }
        }
        return true;
    }

    /**
    * @Title: queryAllUsersByAuthName
    * @Description: 根据权限名查找所有用户
    */
    @Override
    public List<Administer> queryAllUsersByAuthName(String authName) {
        if (org.apache.commons.lang.StringUtils.isBlank(authName)) {
            return null;
        }
        return administerMapper.queryAllUsersByAuthName(authName);
    }

    @Override
    public boolean updateAdminister(Administer administer) {
        //参数校验
        if (administer == null || org.apache.commons.lang.StringUtils.isBlank(administer.getMobilePhone())
                || !StringUtils.isValidMobile(administer.getMobilePhone())
                || org.apache.commons.lang.StringUtils.isBlank(administer.getEnterpriseCode())) {
            LOGGER.error("更新用户时失败，用户信息不完整");
            return false;
        }

        //校验手机号码是否存在
        Administer dbAdminister = selectByMobilePhone(administer.getMobilePhone());
        if (dbAdminister == null || DELETE_FLAG.DELETED.getValue() == dbAdminister.getDeleteFlag()) {
            LOGGER.error("更新用户时失败，用户信息不存在。mobile = " + administer.getMobilePhone());
            return false;
        }

        //校验企业信息是否存在
        Enterprise enterprise = enterprisesService.selectByCode(administer.getEnterpriseCode());
        if (enterprise == null || DELETE_FLAG.DELETED.getValue() == enterprise.getDeleteFlag()) {
            LOGGER.error("更新用户时失败，企业信息不存在。 code = " + administer.getEnterpriseCode());
            return false;
        }

        //获取企业管理员职位节点信息
        Long entManagerId = entManagerService.getManagerIdForEnter(enterprise.getId());
        if (entManagerId == null) {
            LOGGER.error("更新用户时失败，企业信息管理员节点不存在。 code = " + administer.getEnterpriseCode());
            return false;
        }

        //更新用户基本信息
        dbAdminister.setUserName(administer.getUserName());
        dbAdminister.setEmail(administer.getEmail());
        dbAdminister.setUpdateTime(new Date());
        dbAdminister.setDeleteFlag(administer.getDeleteFlag());
        if (!updateSelective(dbAdminister)) {
            LOGGER.error("更新用户时失败，更新用户基本信息失败，事务回滚。");
            throw new TransactionException("更新用户时失败，更新用户基本信息失败，事务回滚。mobile = " + administer.getMobilePhone());
        }

        //更新用户与企业管理员职位节点的关联关系
        AdminManager adminManager = adminManagerService.selectByAdminId(dbAdminister.getId());
        if (adminManager == null || DELETE_FLAG.DELETED.getValue() == adminManager.getDeleteFlag()) {
            LOGGER.error("更新用户时失败，用户与企业管理员职位节点的关联关系不存在，事务回滚。");
            throw new TransactionException("更新用户时失败，用户与企业管理员职位节点的关联关系不存在，事务回滚。mobile = " + administer.getMobilePhone());
        }
        adminManager.setAdminId(dbAdminister.getId());
        adminManager.setUpdateTime(new Date());
        adminManager.setDeleteFlag(dbAdminister.getDeleteFlag().byteValue());

        if (!adminManagerService.updateByPrimaryKeySelective(adminManager)) {
            LOGGER.error("更新用户时失败，更新用户与企业管理员职位节点的关联关系时失败。mobile = " + administer.getMobilePhone());
            throw new TransactionException("更新用户时失败，更新用户与企业管理员职位节点的关联关系时失败。mobile = " + administer.getMobilePhone());
        }

        return true;
    }

    @Override
    @Transactional
    public boolean createAdminister(Administer administer) {
        //参数校验
        if (administer == null || org.apache.commons.lang.StringUtils.isBlank(administer.getMobilePhone())
                || !StringUtils.isValidMobile(administer.getMobilePhone())
                || org.apache.commons.lang.StringUtils.isBlank(administer.getEnterpriseCode())) {
            LOGGER.error("创建用户时失败，用户信息不完整");
            return false;
        }

        //校验手机号码是否存在
        Administer dbAdminister = selectByMobilePhone(administer.getMobilePhone());
        if (dbAdminister != null && DELETE_FLAG.UNDELETED.getValue() == dbAdminister.getDeleteFlag()) {
            LOGGER.error("创建用户时失败，用户信息已存在。 mobile = " + administer.getMobilePhone());
            return false;
        }

        //校验企业信息是否存在
        Enterprise enterprise = enterprisesService.selectByCode(administer.getEnterpriseCode());
        if (enterprise == null || DELETE_FLAG.DELETED.getValue() == enterprise.getDeleteFlag()) {
            LOGGER.error("创建用户时失败，企业信息不存在 。 code = " + administer.getEnterpriseCode());
            return false;
        }

        //获取企业管理员职位节点信息
        Long entManagerId = entManagerService.getManagerIdForEnter(enterprise.getId());
        if (entManagerId == null) {
            LOGGER.error("创建用户时失败，企业信息管理员节点不存在。 code = " + administer.getEnterpriseCode());
            return false;
        }

        //生成用户基本信息
        if (!insertSelective(administer)) {
            LOGGER.error("创建用户时失败，生成用户基本信息失败，事务回滚。mobile = " + administer.getMobilePhone());
            throw new TransactionException("创建用户时失败，生成用户基本信息失败，事务回滚。mobile = " + administer.getMobilePhone());
        }

        //建立用户与企业管理员职位节点的关联关系
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(administer.getId());
        adminManager.setManagerId(entManagerId);
        adminManager.setCreateTime(new Date());
        adminManager.setUpdateTime(new Date());
        adminManager.setDeleteFlag((byte) 0);
        adminManager.setCreatorId(entManagerId);

        if (!adminManagerService.insertAdminManager(adminManager)) {
            LOGGER.error("创建用户时失败，生成用户基本信息失败，事务回滚。mobile = " + administer.getMobilePhone());
            throw new TransactionException("创建用户时失败，生成用户基本信息失败，事务回滚。mobile = " + administer.getMobilePhone());
        }

        //建立用户与角色的关联关系
        if (!adminRoleService.insertAdminRole(administer.getId(), Long.valueOf(getENTERPRISE_CONTACTOR()))) {
            LOGGER.error("创建用户时失败，生成用户与角色关联关系时失败，事务回滚。mobile = " + administer.getMobilePhone());
            throw new TransactionException("创建用户时失败，生成用户与角色关联关系时失败，事务回滚。mobile = " + administer.getMobilePhone());
        }
        return true;
    }

    @Override
    public List<Administer> getByManageIds(List<Long> manageIds) {
        if (manageIds != null && manageIds.size() > 0) {
            Map map = new HashMap();
            map.put("manageIds", manageIds);
            List<Administer> administers = administerMapper.getByMap(map);
            return administers;
        }
        return null;
    }
    
    /**
     * 微信的用户创建
     */
    @Override
    @Transactional
    public boolean insertForWx(String mobile, String openid) {
        Administer admin = new Administer();
        admin.setMobilePhone(mobile);
        admin.setCreatorId(0L);
        if (!insertSelective(admin)) {
            throw new RuntimeException("插入用户表失败！");
        }
        
        List<IndividualAccount> accounts = new ArrayList<IndividualAccount>();
        List<IndividualProduct> products = individualProductService.selectByDefaultValue(1);
        if (products != null && products.size() > 0) {
            for (IndividualProduct product : products) {
                IndividualAccount account = new IndividualAccount();
                account.setAdminId(admin.getId());
                account.setOwnerId(admin.getId());
                account.setCount(new BigDecimal(0));
                account.setDeleteFlag(0);
                account.setIndividualProductId(product.getId());
                account.setType(IndividualAccountType.INDIVIDUAL_BOSS.getValue());//创建的账户都是个人的
                account.setVersion(0);
                accounts.add(account);
            }

            if (!individualAccountService.batchInsert(accounts)) {
                LOGGER.error("插入individual_account失败！");
                throw new RuntimeException("插入individual_account失败！");
            }
        }
        List<Tmpaccount> tmpAccounts = tmpaccountMapper.selectByOpenid(openid);
        if(tmpAccounts!=null && tmpAccounts.size()==1 && tmpAccounts.get(0).getCount().intValue()>0){
            IndividualProduct product = individualProductService.getIndivialPointProduct();
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            if(account!=null){
                if(!individualAccountService.changeAccount(account, tmpAccounts.get(0).getCount(), SerialNumGenerator.buildSerialNum(), 
                        (int)AccountRecordType.INCOME.getValue(), "历史流量币", -1, 0)){
                    LOGGER.error("插入历史流量币失败！");
                    throw new RuntimeException("插入历史流量币失败！");
                }
            }
        }
        
        return true;
    }

    @Override
    @Transactional
    public boolean updateAdminster(String oldMobile, String newMobile) {
        // TODO Auto-generated method stub
        LOGGER.info("原手机号:oldMobile = {}, 新手机号:newMobile = {}", oldMobile, newMobile);
        if(StringUtils.isEmpty(oldMobile) || StringUtils.isEmpty(newMobile)){
            LOGGER.info("参数异常");
            return false;
        }
        
        Administer oldAdminister = selectByMobilePhone(oldMobile);
        if(oldAdminister!=null){
            Administer newAdminister = new Administer();
            newAdminister.setId(oldAdminister.getId());
            newAdminister.setMobilePhone(newMobile);
           
            if(!updateSelective(newAdminister)){
                LOGGER.info("更新新手机号失败, adminId = {}, oldMobile = {}, newMobile = {}",
                        oldAdminister.getId(), oldMobile, newMobile);
                return false;
            }
            
            if(!changeMobileRecordService.insertSelective(createChangeMobileRecord(oldMobile, newMobile, oldAdminister.getId()))){
                LOGGER.info("插入更新新手机号记录失败, adminId = {}, oldMobile = {}, newMobile = {}",
                        oldAdminister.getId(), oldMobile, newMobile);
                throw new RuntimeException();
            }
            return true;
        }
        return false;
    }

    private ChangeMobileRecord createChangeMobileRecord(String oldMobile, String newMobile, Long adminId){
        ChangeMobileRecord record = new ChangeMobileRecord();
        record.setAdminId(adminId);
        record.setOldMobile(oldMobile);
        record.setNewMobile(newMobile);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
    
    @Override
    public boolean verifyResetPsd(Integer type, String entName, String customerManagerMobile, String mobile, String userName){       
        Administer admin = selectByMobilePhone(mobile);
        if(admin==null){
            LOGGER.info("不存在该用户，mobile={}", mobile);
            return false;
        }
        AdminManager adminManager = adminManagerService.selectByAdminId(admin.getId());
        if(adminManager == null){
            LOGGER.info("adminManager不存在");
            return false;
        }
        String entManagerID = globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
        String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if(StringUtils.isEmpty(entManagerID) || StringUtils.isEmpty(customManagerID)){
            LOGGER.info("global_config表中缺少企业管理员角色ID，或者客户经理角色ID");
            return false;
        }
        Manager manager = managerService.selectByPrimaryKey(adminManager.getManagerId());
        if(type.equals(1)){//企业管理员类型，验证企业名称、客户经理手机号码、企业管理员手机号码三者关系是否正确
            if(!manager.getRoleId().toString().equals(entManagerID)){//企业管理员
                LOGGER.info("用户mobile={}职位不是企业管理员", mobile);
                return false;
            }
            //获取企业信息
            List<EntManager> entManagers = entManagerService.selectByManagerId(manager.getId());
            if(entManagers == null || entManagers.size() != 1){
                LOGGER.info("EntManager筛选异常，managerId={}", manager.getId());
                return false;
            }
            Enterprise enter = enterprisesService.selectByPrimaryKey(entManagers.get(0).getEnterId());
            if(!enter.getName().equals(entName)){
                LOGGER.info("企业名称校验未通过，用户输入的企业名称：{}，企业Id={}", entName, enter.getId());
                return false;
            }
            //验证客户经理手机号码是否正确
            Long customManagerId = Long.parseLong(customManagerID);
            List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(enter.getId(),
                    customManagerId);
            if (managers != null && !managers.isEmpty()) {
                List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0).getId());
                if (administers != null && !administers.isEmpty()) {
                    for(Administer customerAdmin : administers){
                        if(customerAdmin.getMobilePhone().equals(customerManagerMobile)){
                            return true;
                        }
                    }                    
                }
            }
            LOGGER.info("客户经理手机号码验证未通过，用户输入的验证信息为：entName={}，customerManagerMobile={}，mobile={}", entName, customerManagerMobile, mobile);
            return false;
        
        }
        if(type.equals(2)){//平台管理员类型，验证管理员所属账号姓名、管理员手机号码关系是否正确
            //校验用户名
            if(!StringUtils.isEmpty(admin.getUserName())){
                if(admin.getUserName().equals(userName)){
                    return true;
                }
            }
            LOGGER.info("校验用户名未通过，用户输入的验证信息为：mobile={}，userName={}", mobile, userName);
            return false;
        }
        return false;
    }

    @Override
    public boolean isOverAuth(Long adminId, Long currentUserId) {
        if(adminId == null || currentUserId == null){
            return true;
        }
        
        Manager fatherManager = managerService.getManagerByAdminId(currentUserId);
        if(fatherManager == null){
            return true;
        }
        
        //省级管理员、市级管理员、客户经理、企业管理员需要校验子父节点关系,其他节点不需要校验
        if(!managerService.isProOrCityOrMangerOrEnt(fatherManager.getRoleId())){
            return false;
        }
        
        //该规则是当前用户节点的子节点用户创建时，不越权
        Manager sonManager = managerService.getManagerByAdminId(adminId);
        if (sonManager != null
                && (managerService.isParentManage(sonManager.getId(), fatherManager.getId()) || managerService
                        .isParentManage(fatherManager.getId(), sonManager.getId()))) {
            return false;
        }       
        return true;  
    }

    @Override
    public void blurAdministerInfo(Administer administer) {
        if(administer == null){
            return;
        }
        
        //模糊处理用户名
        administer.setUserName(blurService.blurUserName(administer.getUserName()));
        
        //模糊处理手机号码
        administer.setMobilePhone(blurService.blurMobile(administer.getMobilePhone()));
        
        //模糊处理邮箱
        administer.setEmail(blurService.blurEmail(administer.getEmail()));        
    }
    
}
