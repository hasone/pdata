package com.cmcc.vrp.province.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.dao.AdminManagerEnterMapper;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.EntProductMapper;
import com.cmcc.vrp.province.dao.EnterpriseMapper;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplate;
import com.cmcc.vrp.province.module.EnterpriseBenefitModule;
import com.cmcc.vrp.province.module.EnterpriseStatisticModule;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJob;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJobPojo;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseInterfaceExpireJob;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseInterfaceExpireJobPojo;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseLicenceExpireJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.BlurService;
import com.cmcc.vrp.province.service.DistrictService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.QueryObject;

/**
 * @ClassName: EnterprisesServiceImpl
 * @Description: TODO
 */
@Service("enterprisesService")
public class EnterprisesServiceImpl implements EnterprisesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterprisesServiceImpl.class);

    @Autowired
    TaskProducer producer;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    AdminDistrictMapper adminDistrictMapper;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    DistrictService districtService;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    ProductService productService;
    @Autowired
    AccountService accountService;
    @Autowired
    AdminManagerEnterMapper adminManagerEnterMapper;
    @Autowired
    EntProductService entProductService;
    @Autowired
    DiscountMapper discountMapper;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EntProductMapper entProductMapper;
    @Autowired
    private AdministerService administerService;
    @Autowired
    private GiveMoneyEnterService gmeService;
    @Autowired
    private EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Autowired
    ProductTemplateService productTemplateService;
    @Autowired
    PlatformProductTemplateMapService platformProductTemplateMapService;
    
    @Autowired
    BlurService blurService;

    @Override
    @Transactional
    public boolean insertSelective(Enterprise record) {
        if (record == null) {
            return false;
        }
        if (record.getName() == null || record.getEntName() == null) {
            return false;
        }
        record.setName(record.getName().replaceAll(" ", ""));

        record.setCreateTime(new Date());
        if (record.getDeleteFlag() == null) {
            record.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
        }
        record.setUpdateTime(new Date());
        if (enterpriseMapper.insertSelective(record) <= 0) {
            throw new RuntimeException();
        }

        Long entId = record.getId();
        Map<Long, Double> infos = new HashMap<Long, Double>();
        // 获取资金产品
        Product currentProd = productService.getCurrencyProduct();
        if (currentProd != null) {
            infos.put(currentProd.getId(), 0.0D);
        }

        //要判断是否使用了三网产品
        if(getUseProductTemplate()){
            //使用三网产品
            try{
                if(!productTemplateService.relatedDefaultProductTemplate(entId)){
                    throw new RuntimeException();
                }
            }catch (RuntimeException e){
                LOGGER.error(e.getMessage());
                throw new RuntimeException();
            }

            // 创建账户
            LOGGER.info("创建企业对象成功, 开始创建企业帐户信息.");
            ProductTemplate productTemplate = productTemplateService.getDefaultProductTemplate();
            List<PlatformProductTemplateMap> defaultMaps = platformProductTemplateMapService.selectByTemplateId(productTemplate.getId());
            for (PlatformProductTemplateMap map : defaultMaps) {
                // 排除掉资金产品(万一把现金产品也选成了默认产品，会重复插入企业的现金账户)
                if (map.getPlatformProductId().equals(currentProd.getId())) {
                    continue;
                }
                infos.put(map.getPlatformProductId(), 0.0D);
            }
        }else{
            // 向ent_product表中填入product表中default标识为1的产品
            List<Product> products = productMapper.selectDefaultProductByCustomerType(record.getCustomerTypeId());
            if (products != null && products.size() > 0) {
                List<EntProduct> records = new ArrayList();
                for (Product p : products) {
                    EntProduct entProduct = new EntProduct();
                    entProduct.setEnterprizeId(entId);
                    entProduct.setProductId(p.getId());
                    entProduct.setCreateTime(new Date());
                    entProduct.setUpdateTime(new Date());
                    entProduct.setDiscount(100);
                    entProduct.setDeleteFlag(0);
                    records.add(entProduct);
                }
                if (entProductMapper.batchInsert(records) <= 0) {
                    throw new RuntimeException();
                }
            }

            // 创建账户
            LOGGER.info("创建企业对象成功, 开始创建企业帐户信息.");
            if (products != null && products.size() > 0) {
                for (Product product : products) {
                    // 排除掉资金产品(万一把现金产品也选成了默认产品，会重复插入企业的现金账户)
                    if (product.getId().equals(currentProd.getId())) {
                        continue;
                    }
                    infos.put(product.getId(), 0.0D);
                }
            }
        }

        if (accountService.createEnterAccount(entId, infos, DigestUtils.md5Hex(UUID.randomUUID().toString()))) {
            LOGGER.info("创建企业帐户成功.");
        } else {
            LOGGER.error("创建企业帐户失败.");
            throw new RuntimeException();
        }

        return true;
    }

    //是否使用产品模板
    public Boolean getUseProductTemplate() {
        String result = globalConfigService.get(GlobalConfigKeyEnum.USE_PRODUCT_TEMPLATE.getKey());
        if(StringUtils.isEmpty(result) || !"YES".equals(result)){
            return false;
        }
        return true;
    }

    @Override
    public Enterprise selectByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return enterpriseMapper.selectByPrimaryKey(id);
    }

    @Override
    public Enterprise selectByPrimaryKeyForActivity(Long id) {
        if (id != null) {
            return enterpriseMapper.selectByPrimaryKeyForActivity(id);
        }
        return null;
    }

    /**
     * 通过企业编码查找相关企业
     *
     * @Title: selectByCode
     * @Description: 通过企业编码查找相关企业
     * @see com.cmcc.vrp.province.service.EnterprisesService#selectByCode(java.lang.String)
     */
    @Override
    public Enterprise selectByCode(String code) {
        if (code == null) {
            LOGGER.error("企业编码为空. 查询不到企业信息，返回null.");
            return null;
        }
        List<Enterprise> listEnterList = enterpriseMapper.selectEnterprisesByCode(code);

        return (listEnterList != null && listEnterList.size() > 0) ? listEnterList.get(0) : null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Enterprise record) {
        if (record != null && record.getId() != null) {
            record.setUpdateTime(new Date());
            return enterpriseMapper.updateByPrimaryKeySelective(record) == 1;
        }

        return false;
    }

    /**
     * @return List<Enterprise>
     * @Title: showEnterprisesForPageResult
     * @Description: 查找符合queryObject条件的List<Enterprise>
     * @author qihang
     */
    @Override
    public List<Enterprise> showEnterprisesForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();

        Long managerId = Long.parseLong(map.get("managerId").toString());
        String managerIds = managerService.getChildNodeString(managerId);
        map.put("managerIds", managerIds);

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        if(map.get("productTemplateName")!=null
            && "无产品模板".equals(map.get("productTemplateName").toString())){
            return enterpriseMapper.showEnterprisesButNotUsedProdTemplate(map);
        }else if (map.get("extEntInfoFlag") != null
                && "true".equals(map.get("extEntInfoFlag").toString())) {
            return enterpriseMapper.showGdEnterprisesForPageResult(map);
        } else {
            return enterpriseMapper.showEnterprisesForPageResult(map);
        }
    }

    /**
     * 根据各种条件搜索企业
     */
    @Override
    public List<Enterprise> selectEnterpriseByMap(Map map) {
        String managerIdStr = (String) map.get("managerId");
        if (!StringUtils.isEmpty(managerIdStr)) {
            Long managerId = Long.parseLong(managerIdStr);
            String managerIds = managerService.getChildNodeString(managerId);
            map.put("managerIds", managerIds);
        }

        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + "23:59:59";
            map.put("endTime", endTime);
        }

        return enterpriseMapper.selectEnterpriseByMap(map);
    }

    /**
     * @return List<Enterprise>
     * @Title: queryWithNameCode
     * @Description: 查找符合name和code条件的<Enterprise>
     * @author qihang
     */
    @Override
    public List<Enterprise> getEnterpriseList() {
        return enterpriseMapper.queryAllEnterpriseList();
    }

    /**
     * @return int
     * @Title: showForPageResultCount
     * @Description: 查找符合queryObject条件的List<Enterprise> 总个数
     * @author qihang
     */
    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();
        Long managerId = Long.parseLong(map.get("managerId").toString());
        String managerIds = managerService.getChildNodeString(managerId);
        map.put("managerIds", managerIds);

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        if(map.get("productTemplateName")!=null
            && "无产品模板".equals(map.get("productTemplateName").toString())){
            return enterpriseMapper.countEnterprisesButNotUsedProdTemplate(map);
        }else if (map.get("extEntInfoFlag") != null
                && "true".equals(map.get("extEntInfoFlag").toString())) {
            return enterpriseMapper.showGdEnterprisesForPageResultCount(map);
        } else{
            return enterpriseMapper.showEnterprisesForPageResultCount(map);
        }
    }

    /**
     * @Title: getEnterpriseListByAdminId
     * @Description: 根据用户筛选出下属企业列表
     */
    @Override
    public List<Enterprise> getEnterpriseListByAdminId(Administer admin) {
        if (admin == null || admin.getId() == null) {
            return null;
        }

        Long adminId = admin.getId();
        Long managerId = adminManagerService.selectManagerIdByAdminId(adminId);
        if (managerId == null) {
            return null;
        }

        List<Enterprise> list = getEnterByManagerId(managerId);

        return list;
    }

    @Override
    public Enterprise queryEnterpriseByCode(String code) {

        return enterpriseMapper.queryEnterpriseByCode(code);
    }

    @Override
    public boolean countExists(Long exceptId, String name, String code, String phone) {
        return enterpriseMapper.countExists(exceptId, name, code, phone) > 0;
    }

    @Override
    public List<EnterpriseStatisticModule> statistictEnterpriseByCreateDay(Long managerId, QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            map.put("endTime", endTime);
        }
        List<Long> managerIds = managerService.getSonTreeIdByManageId(managerId);
        map.put("managerIds", managerIds);
        return enterpriseMapper.statistictEnterpriseByCreateDay(map);
    }

    @Override
    public List<EnterpriseBenefitModule> statistictBenefitForEnterprise(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
            map.put("endTime", endTime);
        }
        String managerId = (String) map.get("managerId");
        List<Long> managerIds = managerService.getSonTreeIdByManageId(Long.parseLong(managerId));
        map.put("managerIds", managerIds);
        return enterpriseMapper.statistictBenefitForEnterprise(map);
    }

    @Override
    public Enterprise selectByAppKey(String appKey) {
        return StringUtils.isBlank(appKey) ? null : enterpriseMapper.selectByAppKey(appKey);
    }

    @Override
    public boolean createEnterpriseExpireSchedule(Enterprise enterprise) {
        EnterpriseExpireJobPojo pojo = new EnterpriseExpireJobPojo(enterprise.getId());
        String jsonStr = JSON.toJSONString(pojo);

        // 创建企业合同到期定时任务
        String msg = scheduleService.createScheduleJob(EnterpriseExpireJob.class,
                SchedulerType.ENTERPRISE_EXPIRE.getCode(), jsonStr, enterprise.getId().toString(),
                enterprise.getEndTime());
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean changeEnterManager(Administer newAdmin, Long enterId, Long currentUserId) {
        Enterprise e = selectByPrimaryKey(enterId);
        if (e == null) {
            return false;
        }

        List<Long> enterpriseManagerIds = adminManagerService.getAdminIdForEnter(enterId);
        // 目前设计为一个企业只可能有一个企管用户
        if (null != enterpriseManagerIds && enterpriseManagerIds.size() > 0) {
            Long enterpriseManagerId = enterpriseManagerIds.get(0);
            // ///////////////////1、删除企业管理员角色表admin_role,暂时还维护admin_role表
            if (!adminRoleService.deleteByAdminId(enterpriseManagerId)) {
                LOGGER.error("删除企业管理员角色表admin_role失败");
                throw new RuntimeException("删除原企业管理员角色失败");
            }
            // 2、删除用户和企业管理员的关系admin_manager
            if (!adminManagerService.deleteAdminByEntId(enterId)) {
                LOGGER.error("删除用户和企业管理员admin_manager失败");
                throw new RuntimeException("删除用户和企业管理员关联失败");
            }

            // 为用户分配普通用户角色
            AdminManager adminManager = new AdminManager();
            adminManager.setAdminId(enterpriseManagerId);
            adminManager.setManagerId(-1L);
            adminManager.setCreatorId(currentUserId);
            // 插入admin_manager表
            if (!adminManagerService.insertAdminManager(adminManager)) {
                throw new RuntimeException("插入admin_manager失败！");
            }
            // 插入admin_role表
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(enterpriseManagerId);
            adminRole.setRoleId(-1L);
            if (!adminRoleService.insertAdminRole(adminRole)) {
                throw new RuntimeException("插入admin_role失败！");
            }

            // 3、企业管理员的用户关联到企业管理员(相当于为用户设置企业管理员身份)

            // 该企业的企业管理员managerId
            Long managerId = entManagerService.getManagerIdForEnter(enterId);
            Administer administerDB = administerService.selectByMobilePhone(newAdmin.getMobilePhone());

            if (!administerService.createAdminister(managerId, newAdmin, administerDB, currentUserId)) {
                LOGGER.error("为用户设置企业管理员身份失败");
                throw new TransactionException("为用户设置企业管理员身份失败");
            }
            return true;
        }
        throw new RuntimeException("该企业不存在企业管理员用户");
    }

    /**
     * 保存编辑认证信息页面
     *
     * @author qinqinyan
     */
    @Override
    @Transactional
    public boolean saveEditQualification(Long currentUserId, Enterprise enterprise,
                                         MultipartHttpServletRequest request) {

        enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));
        // 1、根据企业id找到原企业信息
        // Enterprise oldEnter = selectByPrimaryKey(enterprise.getId());

        // 2、更新企业信息和创建定时任务
        enterprise.setDeleteFlag(EnterpriseStatus.QUALIFICATION_APPROVAL.getCode());
        if (!(updateByPrimaryKeySelective(enterprise))) {
            LOGGER.info("更新企业合作信息或者创建定时任务失败，id={}", enterprise.getId());
            throw new RuntimeException();
        }

        // 3、构建待更新的ef对象并判断是否有需要上传的文件
        EnterpriseFile ef = initEnterpriseFile(enterprise, request);
        if (StringUtils.isBlank(ef.getAuthorizationCertificate()) && StringUtils.isBlank(ef.getBusinessLicence())
                && StringUtils.isBlank(ef.getIdentificationBack()) && StringUtils.isBlank(ef.getIdentificationCard())) {
            // 没有要上传的文件，直接返回true
            return true;
        }

        // 4、上传文件
        uploadFiles(enterprise, request, ef);

        // 5、更新文件
        if (!enterpriseFileService.update(ef)) {
            LOGGER.info("更新数据库失败");
            throw new RuntimeException("更新数据库失败");
        }
        return true;
    }

    private void uploadFiles(Enterprise enterprise, MultipartHttpServletRequest request, EnterpriseFile ef) {
        // 历史数据
        EnterpriseFile efOrigin = enterpriseFileService.selectByEntId(enterprise.getId());
        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());

            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            if (!StringUtils.isBlank(originalFilename)) {
                if (multipartFile.getName().equals("licenceImage")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getLicenceKey())) {
                        key = efOrigin.getLicenceKey();
                    }
                    ef.setLicenceKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("authorization")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getAuthorizationKey())) {
                        key = efOrigin.getAuthorizationKey();
                    }
                    ef.setAuthorizationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identification")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationKey())) {
                        key = efOrigin.getIdentificationKey();
                    }
                    ef.setIdentificationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identificationBack")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationBackKey())) {
                        key = efOrigin.getIdentificationBackKey();
                    }
                    ef.setIdentificationBackKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("customerFile")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getCustomerfileKey())) {
                        key = efOrigin.getCustomerfileKey();
                    }
                    ef.setCustomerfileKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("contract")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getContractKey())) {
                        key = efOrigin.getContractKey();
                    }
                    ef.setContractKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("image")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getImageKey())) {
                        key = efOrigin.getImageKey();
                    }
                    ef.setImageKey(key);
                    fileStoreService.save(key, file);
                }
            }
        }
    }

    private EnterpriseFile initEnterpriseFile(Enterprise enterprise, MultipartHttpServletRequest request) {
        EnterpriseFile ef = new EnterpriseFile();
        ef.setEntId(enterprise.getId());

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());
            String originalFilename = multipartFile.getOriginalFilename();
            System.out.println(multipartFile.getName().toString());
            if (!StringUtils.isBlank(originalFilename)) {
                if (multipartFile.getName().toString().equals("licenceImage")) {
                    ef.setBusinessLicence(originalFilename);
                } else if (multipartFile.getName().toString().equals("authorization")) {
                    ef.setAuthorizationCertificate(originalFilename);
                } else if (multipartFile.getName().toString().equals("identification")) {
                    ef.setIdentificationCard(originalFilename);
                } else if (multipartFile.getName().toString().equals("identificationBack")) {
                    ef.setIdentificationBack(originalFilename);
                } else if (multipartFile.getName().toString().equals("customerFile")) {
                    ef.setCustomerfileName(originalFilename);
                } else if (multipartFile.getName().toString().equals("contract")) {
                    ef.setContractName(originalFilename);
                } else if (multipartFile.getName().toString().equals("image")) {
                    ef.setImageName(originalFilename);
                }
            }
        }
        ef.setCreateTime(new Date());
        ef.setUpdateTime(new Date());
        return ef;
    }

    @Override
    @Transactional
    public boolean saveQualification(Long currentUserId, Enterprise enterprise, MultipartHttpServletRequest request) {
        // 将企业小状态delete_flag设置为认证审核中
        enterprise.setDeleteFlag(EnterpriseStatus.QUALIFICATION_APPROVAL.getCode());
        enterprise.setStatus((byte) 1);

        enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));

        // 1、根据企业id找到原企业信息
        // Enterprise oldEnter = selectByPrimaryKey(enterprise.getId());

        EnterpriseFile enterpriseFile = new EnterpriseFile();
        enterpriseFile.setEntId(enterprise.getId());
        enterpriseFile.setCreateTime(new Date());
        enterpriseFile.setUpdateTime(new Date());

        // s3Until s3Until = new s3Until();

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());

            /*
             * CommonsMultipartFile cf= (CommonsMultipartFile)multipartFile;
             * DiskFileItem fi = (DiskFileItem)cf.getFileItem(); File file =
             * fi.getStoreLocation();
             */

            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            EnterpriseFile efOrigin = enterpriseFileService.selectByEntId(enterprise.getId());// 将S3中的原文件覆盖
            if (!"".equals(originalFilename)) {
                if (multipartFile.getName().equals("licenceImage")) {
                    enterpriseFile.setBusinessLicence(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getLicenceKey())) {
                        key = efOrigin.getLicenceKey();
                    }
                    enterpriseFile.setLicenceKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("authorization")) {
                    enterpriseFile.setAuthorizationCertificate(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getAuthorizationKey())) {
                        key = efOrigin.getAuthorizationKey();
                    }
                    enterpriseFile.setAuthorizationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identification")) {
                    enterpriseFile.setIdentificationCard(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationKey())) {
                        key = efOrigin.getIdentificationKey();
                    }
                    enterpriseFile.setIdentificationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identificationBack")) {
                    enterpriseFile.setIdentificationBack(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationBackKey())) {
                        key = efOrigin.getIdentificationBackKey();
                    }
                    enterpriseFile.setIdentificationBackKey(key);
                    fileStoreService.save(key, file);
                }
            }
        }

        // 3、插入文件数据库
        // 4、企业表更新合同时间
        if (enterpriseFileService.selectByEntId(enterprise.getId()) != null) {
            if (enterpriseFileService.update(enterpriseFile) && updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                LOGGER.info("1更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        } else {
            if (enterpriseFileService.insert(enterpriseFile) > 0 && updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                LOGGER.info("2更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        }
    }

    @Override
    public boolean createEnterpriseLicenceExpireSchedule(Enterprise enterprise) {
        EnterpriseExpireJobPojo pojo = new EnterpriseExpireJobPojo(enterprise.getId());
        String jsonStr = JSON.toJSONString(pojo);

        // 创建企业合同到期定时任务
        String msg = scheduleService.createScheduleJob(EnterpriseLicenceExpireJob.class,
                SchedulerType.ENTERPRISE_LICENCE_EXPIRE.getCode(), jsonStr, enterprise.getId().toString(),
                enterprise.getLicenceEndTime());
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }

    /**
     * 保存全部企业信息
     *
     * @author qinqinyan
     */
    @Override
    public boolean saveEditEntInfo(Enterprise enterprise, Long currentUserId, MultipartHttpServletRequest request) {

        if (enterprise == null || enterprise.getId() == null) {
            return false;
        }
        if (enterprise.getLicenceEndTime() != null) {
            enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));
        }
        if (enterprise.getEndTime() != null) {
            enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));
        }

        // 3、更新企业信息
        if (!(updateByPrimaryKeySelective(enterprise))) {
            LOGGER.info("更新企业合作信息或者创建定时任务失败，id={}", enterprise.getId());
            throw new RuntimeException();
        }

        // 5、自营没有存送需要保存
        if (enterprise.getGiveMoneyId() != null) {
            if (gmeService.selectByEnterId(enterprise.getId()) != null) {
                if (!gmeService.updateByEnterId(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比更新失败");
                    throw new RuntimeException();
                }
            } else {
                if (!gmeService.insert(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比插入失败");
                    throw new RuntimeException();
                }
            }
        }

        if (!StringUtils.isEmpty((String) request.getParameter("minCount"))) {
            Double minCount = Double.parseDouble((String) request.getParameter("minCount"));
            // 6、保存信用额度
            if (minCount != null && !accountService.setMinCount(enterprise.getId(), minCount * (-100))) {
                LOGGER.info("信用额度保存失败");
                throw new RuntimeException("信用额度保存失败");
            }
        }

        // 7、保存客户经理邮箱
        String email = (String) request.getParameter("customerManagerEmail");
        enterprise.setCmEmail(email);
        // 找到企业的企业管理员的父节点
        /*
         * Manager manager =
         * entManagerService.getManagerForEnter(enterprise.getId()); Long
         * adminId = null; if (manager != null && (adminId =
         * adminManagerService.
         * selectAdminIdByManagerId(manager.getParentId()).get(0)) != null) {
         * Administer admin = new Administer(); admin.setEmail(email);
         * admin.setId(adminId); if (!administerService.updateSelective(admin))
         * { LOGGER.info("客户经理邮箱保存失败"); throw new
         * RuntimeException("客户经理邮箱保存失败"); } } else { LOGGER.info("客户经理邮箱保存失败");
         * throw new RuntimeException("客户经理邮箱保存失败"); }
         */
        // 8、企业合作信息保存了企业折扣，需要更新到ent_product表中
        Discount discount = discountMapper.selectByPrimaryKey(enterprise.getDiscount());
        if (discount != null) {
            Double dis = discount.getDiscount() * 100;
            if (!entProductService.updateDiscountByEnterId(enterprise.getId(), dis.intValue())) {
                LOGGER.info("企业产品折扣信息更新失败");
                throw new RuntimeException("企业产品折扣信息更新失败");
            }
        }
        // 9、更新企业信息
        if (!updateByPrimaryKeySelective(enterprise)) {
            LOGGER.info("更新企业信息失败！");
            throw new RuntimeException("更新企业信息失败！");
        }

        // 10、构建待更新的ef对象并判断是否有需要上传的文件
        EnterpriseFile ef = initEnterpriseFile(enterprise, request);
        if (ef != null && StringUtils.isBlank(ef.getAuthorizationCertificate())
                && StringUtils.isBlank(ef.getBusinessLicence()) && StringUtils.isBlank(ef.getIdentificationBack())
                && StringUtils.isBlank(ef.getIdentificationCard()) && StringUtils.isBlank(ef.getContractName())
                && StringUtils.isBlank(ef.getCustomerfileName()) && StringUtils.isBlank(ef.getImageName())) {
            // 没有要上传的文件，直接返回true
            return true;
        }

        // 11、上传文件
        uploadFiles(enterprise, request, ef);

        // 12、更新文件
        if (!enterpriseFileService.update(ef)) {
            LOGGER.info("更新数据库失败");
            throw new RuntimeException("更新数据库失败");
        }
        return true;
    }

    /**
     * 保存合作信息
     *
     * @author qinqinyan
     **/
    @Override
    @Transactional
    public boolean saveEditCooperation(Enterprise enterprise, Long currentUserId, MultipartHttpServletRequest request) {
        if (enterprise == null || enterprise.getId() == null) {
            return false;
        }
        // Enterprise oldEnter = selectByPrimaryKey(enterprise.getId());

        // 自营没有存送需要保存
        if (enterprise.getGiveMoneyId() != null) {
            if (gmeService.selectByEnterId(enterprise.getId()) != null) {
                if (!gmeService.updateByEnterId(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比更新失败");
                    throw new RuntimeException();
                }
            } else {
                if (!gmeService.insert(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比插入失败");
                    throw new RuntimeException();
                }
            }
        }

        if (!StringUtils.isEmpty((String) request.getParameter("minCount"))) {
            Double minCount = Double.parseDouble((String) request.getParameter("minCount"));
            // 保存信用额度
            if (minCount != null && !accountService.setMinCount(enterprise.getId(), minCount * (-100))) {
                LOGGER.info("信用额度保存失败");
                throw new RuntimeException("信用额度保存失败");
            }
        }

        // 保存客户经理邮箱
        // String email = (String) request.getParameter("customerManagerEmail");
        // 找到企业的企业管理员的父节点
        /*
         * Manager manager =
         * entManagerService.getManagerForEnter(enterprise.getId()); Long
         * adminId = null; if (manager != null && (adminId =
         * adminManagerService.
         * selectAdminIdByManagerId(manager.getParentId()).get(0)) != null) {
         * Administer admin = new Administer(); admin.setEmail(email);
         * admin.setId(adminId); if (!administerService.updateSelective(admin))
         * { LOGGER.info("客户经理邮箱保存失败"); throw new
         * RuntimeException("客户经理邮箱保存失败"); } } else { LOGGER.info("客户经理邮箱保存失败");
         * throw new RuntimeException("客户经理邮箱保存失败"); }
         */
        // enterprise.setCmEmail(email);

        // 企业合作信息保存了企业折扣，需要更新到ent_product表中
        Discount discount = discountMapper.selectByPrimaryKey(enterprise.getDiscount());
        if (discount != null) {
            Double dis = discount.getDiscount() * 100;
            if (!entProductService.updateDiscountByEnterId(enterprise.getId(), dis.intValue())) {
                LOGGER.info("企业产品折扣信息更新失败");
                throw new RuntimeException("企业产品折扣信息更新失败");
            }
        }

        if (!updateByPrimaryKeySelective(enterprise)) {
            LOGGER.info("更新企业信息失败！");
            throw new RuntimeException("更新企业信息失败！");
        }

        // 3、构建待更新的ef对象并判断是否有需要上传的文件
        EnterpriseFile ef = initEnterpriseFile(enterprise, request);

        if (ef != null && StringUtils.isBlank(ef.getContractName()) && StringUtils.isBlank(ef.getCustomerfileName())
                && StringUtils.isBlank(ef.getImageName())) {
            return true;
        }
        uploadFiles(enterprise, request, ef);
        if (!enterpriseFileService.update(ef)) {
            throw new RuntimeException("企业文件表更新失败或企业更新失败");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveCooperation(Enterprise enterprise, Long currentUserId, MultipartHttpServletRequest request) {
        // 插入审批意见-1、认证通过的审批意见，2、提交市级管理员审核的审批意见
        Long entId = enterprise.getId();

        // 企业状态更改
        enterprise.setDeleteFlag(EnterpriseStatus.COOPERATE_INFO.getCode());
        enterprise.setStatus((byte) 1);
        // Enterprise oldEnter = selectByPrimaryKey(entId);

        // 自营没有存送需要保存
        if (enterprise.getGiveMoneyId() != null) {
            if (gmeService.selectByEnterId(enterprise.getId()) != null) {
                if (!gmeService.updateByEnterId(enterprise.getGiveMoneyId(), entId)) {
                    LOGGER.info("存送比更新失败");
                    throw new RuntimeException();
                }
            } else {
                if (!gmeService.insert(enterprise.getGiveMoneyId(), entId)) {
                    LOGGER.info("存送比插入失败");
                    throw new RuntimeException();
                }
            }
        }

        if (!StringUtils.isEmpty((String) request.getParameter("minCount"))) {
            Double minCount = Double.parseDouble((String) request.getParameter("minCount"));
            // 保存信用额度
            if (minCount != null && !accountService.setMinCount(entId, minCount * (-100))) {
                LOGGER.info("信用额度保存失败");
                throw new RuntimeException("信用额度保存失败");
            }
        }

        // 保存客户经理邮箱
        // String email = (String) request.getParameter("customerManagerEmail");
        /*
         * //找到企业的企业管理员的父节点 Manager manager =
         * entManagerService.getManagerForEnter(enterprise.getId()); Long
         * adminId = null; if (manager != null && (adminId =
         * adminManagerService.
         * selectAdminIdByManagerId(manager.getParentId()).get(0)) != null) {
         * Administer admin = new Administer(); admin.setEmail(email);
         * admin.setId(adminId); if (!administerService.updateSelective(admin))
         * { LOGGER.info("客户经理邮箱保存失败"); throw new
         * RuntimeException("客户经理邮箱保存失败"); } } else { LOGGER.info("客户经理邮箱保存失败");
         * throw new RuntimeException("客户经理邮箱保存失败"); }
         */
        // 客户经理邮箱作为企业信息的一个字段保存，不去修改客户经理的个人邮箱
        // enterprise.setCmEmail(email);

        // 上传文件并插入数据库表
        EnterpriseFile enterpriseFile = new EnterpriseFile();
        enterpriseFile.setEntId(enterprise.getId());
        enterpriseFile.setUpdateTime(new Date());

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());
            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            EnterpriseFile efOrigin = enterpriseFileService.selectByEntId(enterprise.getId());// 将S3中的原文件覆盖

            if (!"".equals(originalFilename)) {
                if (multipartFile.getName().equals("customerFile")) {
                    enterpriseFile.setCustomerfileName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getCustomerfileKey())) {
                        key = efOrigin.getCustomerfileKey();
                    }
                    enterpriseFile.setCustomerfileKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("contract")) {
                    enterpriseFile.setContractName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getContractKey())) {
                        key = efOrigin.getContractKey();
                    }
                    enterpriseFile.setContractKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("image")) {
                    enterpriseFile.setImageName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getImageKey())) {
                        key = efOrigin.getImageKey();
                    }
                    enterpriseFile.setImageKey(key);
                    fileStoreService.save(key, file);
                }
            }
        }

        // 企业合作信息保存了企业折扣，需要更新到ent_product表中
        Discount discount = discountMapper.selectByPrimaryKey(enterprise.getDiscount());
        if (discount != null) {
            Double dis = discount.getDiscount() * 100;
            if (!entProductService.updateDiscountByEnterId(enterprise.getId(), dis.intValue())) {
                LOGGER.info("企业产品折扣信息更新失败");
                throw new RuntimeException("企业产品折扣信息更新失败");
            }
        }

        if (enterpriseFileService.update(enterpriseFile) && updateByPrimaryKeySelective(enterprise)) {
            return true;
        } else {
            throw new RuntimeException("企业文件表更新失败或企业更新失败");
        }

    }

    @Override
    public List<Enterprise> queryPotentialEnterList(QueryObject queryObject, Long currentManagerId) {
        if (queryObject == null || currentManagerId == null) {
            return null;
        }

        Map<String, Object> map = queryObject.toMap();

        String managerIds = managerService.getChildNodeString(currentManagerId);
        map.put("managerIds", managerIds);

        if (map.get("deleteFlag") != null) {
            map.put("deleteFlags", map.get("deleteFlag").toString().split(","));
            // map.remove("deleteFlag");
        }

        return enterpriseMapper.queryPotentialEnt(map);
    }

    /**
     * 企业与用户关联用了两张表 1 admin-enter 2 admin-manage-enter
     */
    @Override
    public List<Enterprise> getNormalEnterpriseListByAdminId(Administer admin) {
        if (admin == null || admin.getId() == null) {
            return null;
        }

        Long adminId = admin.getId();
        Long managerId = adminManagerService.selectManagerIdByAdminId(adminId);
        if (managerId == null) {
            return null;
        }
        List<Enterprise> list = getNormalEnterByManagerId(managerId);

        return list;
    }

    @Override
    public List<Enterprise> getAllEnterpriseListByAdminId(Administer admin) {
        if (admin == null || admin.getId() == null) {
            return null;
        }
        Long adminId = admin.getId();
        Long managerId = adminManagerService.selectManagerIdByAdminId(adminId);
        if (managerId == null) {
            return null;
        }
        List<Enterprise> list = getAllEnterByManagerId(managerId);
        return list;
    }

    /**
     * 获取平台所有正常状态的企业
     *
     * @date 2016年8月3日
     * @author wujiamin
     */
    @Override
    public List<Enterprise> getNormalEnterpriseList() {
        return enterpriseMapper.getNormalEnter();
    }

    /**
     * 筛选出的是所有状态的企业
     */
    @Override
    public List<Enterprise> getEnterByManagerId(Long managerId) {
        String managerIds = managerService.getChildNodeString(managerId);
        return enterpriseMapper.getEnterByManagerId(managerIds);
    }

    /**
     * 筛选出的是正常的企业
     */
    @Override
    public List<Enterprise> getNomalEnterByManagerId(Long managerId) {
        String managerIds = managerService.getChildNodeString(managerId);
        return enterpriseMapper.getNomalEnterByManagerId(managerIds);
    }

    @Override
    public List<Enterprise> getPotentialEnterByManagerId(Long managerId) {
        if (managerId == null) {
            return null;
        }
        String managerIds = managerService.getChildNodeString(managerId);
        List<Enterprise> lists = enterpriseMapper.getPotentialEnterByManagerId(managerIds);
        if (lists == null || lists.size() == 0) {
            return null;
        }
        return lists;
    }

    /**
     * 删选出所有正常状态下的企业delete_flag=0 and status=3
     *
     * @date 2016年7月21日
     * @author wujiamin
     */
    @Override
    public List<Enterprise> getNormalEnterByManagerId(Long managerId) {
        String managerIds = managerService.getChildNodeString(managerId);
        return enterpriseMapper.getNormalEnterByManagerId(managerIds);
    }

    @Override
    public List<Enterprise> getAllEnterByManagerId(Long managerId) {
        String managerIds = managerService.getChildNodeString(managerId);
        return enterpriseMapper.getAllEnterByManagerId(managerIds);
    }

    @Override
    public List<Long> getEnterIdByManagerId(Long managerId) {
        if (managerId == null) {
            return null;
        }
        List<Enterprise> list = getEnterByManagerId(managerId);
        List<Long> ids = new ArrayList<Long>();
        for (Enterprise enterprise : list) {
            ids.add(enterprise.getId());
        }
        return ids;
    }

    @Override
    public List<Long> getEnterpriseIdByAdminId(Administer admin) {
        if (admin == null || admin.getId() == null) {
            return null;
        }

        Long adminId = admin.getId();
        Long managerId = adminManagerService.selectManagerIdByAdminId(adminId);
        if (managerId == null) {
            return null;
        }

        List<Long> list = getEnterIdByManagerId(managerId);

        return list;
    }

    @Override
    public List<EnterpriseStatisticModule> statistictEnterpriseByManagerTree(QueryObject queryObject) {
        Map map = queryObject.toMap();
        return enterpriseMapper.selectEnterpriseByManagerTree(map);
    }

    @Override
    public int statistictEnterpriseCountByManagerTree(QueryObject queryObject) {
        Map map = queryObject.toMap();
        return enterpriseMapper.selectEnterpriseCountByManagerTree(map);
    }

    @Override
    @Transactional
    public boolean saveQualificationCooperation(Long currentUserId, Enterprise enterprise,
                                                MultipartHttpServletRequest request) {
        // 将企业小状态delete_flag设置为认证审核中
        enterprise.setDeleteFlag(EnterpriseStatus.COOPERATE_INFO.getCode());
        enterprise.setStatus((byte) 1);

        // 合作时间和营业执照时间转换为23:59:59
        if (enterprise.getLicenceEndTime() != null) {
            enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));
        }
        if (enterprise.getEndTime() != null) {
            enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));
        }

        // 企业合作信息保存了企业折扣，需要更新到ent_product表中
        Discount discount = discountMapper.selectByPrimaryKey(enterprise.getDiscount());
        if (discount != null) {
            Double dis = discount.getDiscount() * 100;
            if (!entProductService.updateDiscountByEnterId(enterprise.getId(), dis.intValue())) {
                LOGGER.info("企业产品折扣信息更新失败");
                throw new RuntimeException("企业产品折扣信息更新失败");
            }
        }

        // 1、根据企业id找到原企业信息
        // Enterprise oldEnter = selectByPrimaryKey(enterprise.getId());
        // if (oldEnter.getEndTime() != null) {
        // //2、删除企业营业执照到期定时任务
        // deleteEnterpriseLicenceExpireSchedule(oldEnter.getLicenceEndTime(),
        // enterprise.getId());
        // }

        // 自营没有存送需要保存
        if (enterprise.getGiveMoneyId() != null) {
            if (gmeService.selectByEnterId(enterprise.getId()) != null) {
                if (!gmeService.updateByEnterId(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比更新失败");
                    throw new RuntimeException();
                }
            } else {
                if (!gmeService.insert(enterprise.getGiveMoneyId(), enterprise.getId())) {
                    LOGGER.info("存送比插入失败");
                    throw new RuntimeException();
                }
            }
        }

        if (!StringUtils.isEmpty((String) request.getParameter("minCount"))) {
            Double minCount = Double.parseDouble((String) request.getParameter("minCount"));
            // 保存信用额度
            if (minCount != null && !accountService.setMinCount(enterprise.getId(), minCount * (-100))) {
                LOGGER.info("信用额度保存失败");
                throw new RuntimeException("信用额度保存失败");
            }
        }

        // 保存客户经理邮箱
        String email = (String) request.getParameter("customerManagerEmail");
        enterprise.setCmEmail(email);
        EnterpriseFile enterpriseFile = new EnterpriseFile();
        enterpriseFile.setEntId(enterprise.getId());
        enterpriseFile.setCreateTime(new Date());
        enterpriseFile.setUpdateTime(new Date());

        // s3Until s3Until = new s3Until();

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());

            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            EnterpriseFile efOrigin = enterpriseFileService.selectByEntId(enterprise.getId());// 将S3中的原文件覆盖
            if (!"".equals(originalFilename)) {
                if (multipartFile.getName().equals("licenceImage")) {
                    enterpriseFile.setBusinessLicence(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getLicenceKey())) {
                        key = efOrigin.getLicenceKey();
                    }
                    enterpriseFile.setLicenceKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("authorization")) {
                    enterpriseFile.setAuthorizationCertificate(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getAuthorizationKey())) {
                        key = efOrigin.getAuthorizationKey();
                    }
                    enterpriseFile.setAuthorizationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identification")) {
                    enterpriseFile.setIdentificationCard(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationKey())) {
                        key = efOrigin.getIdentificationKey();
                    }
                    enterpriseFile.setIdentificationKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("identificationBack")) {
                    enterpriseFile.setIdentificationBack(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getIdentificationBackKey())) {
                        key = efOrigin.getIdentificationBackKey();
                    }
                    enterpriseFile.setIdentificationBackKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("customerFile")) {
                    enterpriseFile.setCustomerfileName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getCustomerfileKey())) {
                        key = efOrigin.getCustomerfileKey();
                    }
                    enterpriseFile.setCustomerfileKey(key);
                    fileStoreService.save(key, file);
                }
                if (multipartFile.getName().equals("contract")) {
                    enterpriseFile.setContractName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getContractKey())) {
                        key = efOrigin.getContractKey();
                    }
                    enterpriseFile.setContractKey(key);
                    fileStoreService.save(key, file);
                }

                if (multipartFile.getName().equals("image")) {
                    enterpriseFile.setImageName(originalFilename);
                    String key = MD5.md5(UUID.randomUUID().toString());
                    if (efOrigin != null && !StringUtils.isEmpty(efOrigin.getImageKey())) {
                        key = efOrigin.getImageKey();
                    }
                    enterpriseFile.setImageKey(key);
                    fileStoreService.save(key, file);
                }
            }
        }

        // 3、插入文件数据库
        // 4、企业表更新合同时间
        if (enterpriseFileService.selectByEntId(enterprise.getId()) != null) {
            if (enterpriseFileService.update(enterpriseFile) && updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                LOGGER.info("1更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        } else {
            if (enterpriseFileService.insert(enterpriseFile) > 0 && updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                LOGGER.info("2更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        }

    }

    /**
     * 填写企业认证信息权限
     */
    @Override
    public Boolean hasAuthToFillInQuafilication(Long roleId) {
        return (roleId != null && (roleId.toString().equals(getAccountManager().toString())
                || roleId.toString().equals(getEnterpriseContactor().toString()))) ? true : false;
    }

    /**
     * 填写企业合作信息权限
     */
    @Override
    public Boolean hasAuthToFillInCooperation(Long roleId) {
        return (roleId != null && roleId.toString().equals(getAccountManager().toString())) ? true : false;
    }

    /**
     * 用于自营
     */
    @Override
    public Boolean hasAuthToFillInForProvince(Long roleId) {
        return (roleId != null && roleId.toString().equals(getAccountManager().toString())) ? true : false;
    }

    @Override
    public Boolean hasAuthToEdit(Long roleId) {
        if (roleId == null) {
            return false;
        }
        if (roleId.toString().equals(getAccountManager().toString())
                || roleId.toString().equals(getEnterpriseContactor().toString())) {
            return true;
        }
        return false;
    }

    public Long getAccountManager() {
        return NumberUtils.toLong(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey()));
    }

    public Long getEnterpriseContactor() {
        return NumberUtils.toLong(globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey()));
    }

    /**
     * @Title: countPotentialEnterList
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.EnterprisesService#countPotentialEnterList(com.cmcc.vrp.util.QueryObject,
     * java.lang.Long)
     */
    @Override
    public Long countPotentialEnterList(QueryObject queryObject, Long currentManagerId) {
        if (queryObject == null || currentManagerId == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();

        String managerIds = managerService.getChildNodeString(currentManagerId);
        map.put("managerIds", managerIds);

        if (map.get("deleteFlag") != null) {
            map.put("deleteFlags", map.get("deleteFlag").toString().split(","));
            // map.remove("deleteFlag");
        }

        return enterpriseMapper.countPotentialEnt(map);
    }

    @Override
    public String judgeEnterprise(Long entId) {
        if (entId != null) {
            Enterprise enter = selectByPrimaryKey(entId);
            if ((enter == null || !enter.getDeleteFlag().toString().equals(EnterpriseStatus.NORMAL.getCode().toString())
                    || (enter.getStartTime() != null && enter.getStartTime().after(new Date()))
                    || (enter.getEndTime() != null && (new Date()).after(enter.getEndTime()))
                    || (enter.getLicenceStartTime() != null && enter.getLicenceStartTime().after(new Date()))
                    || (enter.getLicenceEndTime() != null && (new Date()).after(enter.getLicenceEndTime())))) {
                return "企业处于非正常状态！";
            }
            return "success";
        }
        return "查找不到相关企业";
    }

    /**
     * @Title: undoEnterpriseExireSchedule
     * @Description: 取消企业合同到期定时任务
     */
    @Override
    public boolean undoEnterpriseExireSchedule(Enterprise enter) {

        String msg = scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(),
                SchedulerType.ENTERPRISE_EXPIRE.getCode(), enter.getId().toString(), enter.getEndTime());
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }

    /**
     * @Title: undoEnterpriseLicenceExpireSchedule
     * @Description: 取消企业营业执照到期定时任务
     */
    @Override
    public boolean undoEnterpriseLicenceExpireSchedule(Enterprise enter) {
        String msg = scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(),
                SchedulerType.ENTERPRISE_LICENCE_EXPIRE.getCode(), enter.getId().toString(), enter.getLicenceEndTime());
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }

    @Override
    public Enterprise selectById(Long entId) {
        if (entId != null) {
            return enterpriseMapper.selectById(entId);
        }
        return null;
    }

    @Override
    public List<Enterprise> queryByEntName(String entName) {
        if (StringUtils.isBlank(entName)) {
            return null;
        }

        return enterpriseMapper.queryByEntName(entName);
    }

    /**
     * @Title: changeAppkey
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    @Override
    @Transactional
    public boolean changeAppkey(Long id) {
        // 删除企业appkey过期通知定时任务
        deleteEnterpriseInterfaceExpireJob(id);

        Enterprise enterprise = new Enterprise();
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setId(id);

        String date = globalConfigService.get(GlobalConfigKeyEnum.INTERFACE_EXPIRE_TIME.getKey());
        if (date != null && !"-1".equals(date)) {
            Date expireTime = DateUtil.getDateAfter(DateUtil.getBeginOfDay(new Date()), Integer.parseInt(date));
            enterprise.setInterfaceExpireTime(expireTime);

            // 创建企业合同到期定时任务
            if (java.lang.Integer.parseInt(date) >= 10) {
                createEnterpriseInterfaceExpireJob(id, "10", expireTime);
                createEnterpriseInterfaceExpireJob(id, "5", expireTime);
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 5 && Integer.parseInt(date) < 10) {
                createEnterpriseInterfaceExpireJob(id, "5", expireTime);
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 3 && Integer.parseInt(date) < 5) {
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 1 && Integer.parseInt(date) < 3) {
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            }
        }
        // 更新企业记录
        if (!updateByPrimaryKeySelective(enterprise)) {
            return false;
        }
        return true;

    }

    /**
     * @Title: createAppkey
     */
    @Override
    @Transactional
    public boolean createAppkey(Long id) {

        Enterprise enterprise = new Enterprise();
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setId(id);

        String date = globalConfigService.get(GlobalConfigKeyEnum.INTERFACE_EXPIRE_TIME.getKey());
        if (date != null && !"-1".equals(date)) {
            Date expireTime = DateUtil.getDateAfter(DateUtil.getBeginOfDay(new Date()), Integer.parseInt(date));
            enterprise.setInterfaceExpireTime(expireTime);

            // 创建企业appkey到期定时任务
            if (Integer.parseInt(date) >= 10) {
                createEnterpriseInterfaceExpireJob(id, "10", expireTime);
                createEnterpriseInterfaceExpireJob(id, "5", expireTime);
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 5 && Integer.parseInt(date) < 10) {
                createEnterpriseInterfaceExpireJob(id, "5", expireTime);
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 3 && Integer.parseInt(date) < 5) {
                createEnterpriseInterfaceExpireJob(id, "3", expireTime);
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            } else if (Integer.parseInt(date) >= 1 && Integer.parseInt(date) < 3) {
                createEnterpriseInterfaceExpireJob(id, "1", expireTime);
            }
        }
        // 更新企业记录
        if (!updateByPrimaryKeySelective(enterprise)) {
            return false;
        }
        return true;

    }

    private boolean createEnterpriseInterfaceExpireJob(Long entId, String date, Date expireTime) {
        EnterpriseInterfaceExpireJobPojo pojo = new EnterpriseInterfaceExpireJobPojo(date, entId);
        String jsonStr = JSON.toJSONString(pojo);
        Date dateTime = DateUtil.getDateBefore(expireTime, Integer.parseInt(date));

        String msg = scheduleService.createScheduleJobForEnterpriseInterfaceExpire(EnterpriseInterfaceExpireJob.class,
                SchedulerType.ENTERPRISE_INTERFACE_EXPIRE.getCode(), jsonStr, entId.toString(), date, dateTime);

        return "success".equals(msg);

    }

    /**
     * 删除企业Interface接口定时任务
     *
     * @Title: deleteEnterpriseInterfaceExpireJob
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    private void deleteEnterpriseInterfaceExpireJob(Long entId) {
        scheduleService.undoScheduleJob(SchedulerType.ENTERPRISE_INTERFACE_EXPIRE.getCode() + "_" + entId + "_10");
        scheduleService.undoScheduleJob(SchedulerType.ENTERPRISE_INTERFACE_EXPIRE.getCode() + "_" + entId + "_5");
        scheduleService.undoScheduleJob(SchedulerType.ENTERPRISE_INTERFACE_EXPIRE.getCode() + "_" + entId + "_3");
        scheduleService.undoScheduleJob(SchedulerType.ENTERPRISE_INTERFACE_EXPIRE.getCode() + "_" + entId + "_1");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean createNewEnterprise(Enterprise enterprise) {
        // 参数校验
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())
                || StringUtils.isBlank(enterprise.getEnterpriseCity())) {
            return false;
        }

        // 校验企业是否存在
        Enterprise dbEnterprise = selectByCode(enterprise.getCode());
        if (dbEnterprise != null && dbEnterprise.getDeleteFlag().intValue() != DELETE_FLAG.UNDELETED.getValue()) {
            LOGGER.error("创建企业时失败，企业已存在。code = " + enterprise.getCode());
            return false;
        }
        String roleId = globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
        // 获取市级管理员职位节点信息
        if ("shanghai".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))) {
            roleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        }
        
        if (StringUtils.isBlank(roleId)) {
            LOGGER.error("创建企业时失败，无法获取市级管理员角色ID。config_key = " + GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
            return false;
        }
        Manager cityManager = managerService.get(Long.valueOf(roleId), enterprise.getEnterpriseCity());
        if (cityManager == null || DELETE_FLAG.DELETED.getValue() == cityManager.getDeleteFlag().intValue()) {
            LOGGER.error("创建企业时失败，市级管理员职位节点不存在。city = " + enterprise.getEnterpriseCity());
            return false;
        }

        // 生成企业基本信息
        if (!insertSelective(enterprise)) {
            throw new TransactionException("创建企业时失败:生成企业基本信息时失败，事务回滚！ code = " + enterprise.getCode());
        }

        // 为企业设置默认短信模板
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(enterprise.getId());

        // 新建企业管理员节点
        String entManagerRoleId = globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
        if (StringUtils.isBlank(entManagerRoleId)) {
            LOGGER.error(
                    "创建企业时失败，无法获取企业管理员角色ID。config_key = " + GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
            throw new TransactionException("创建企业时失败，无法获取企业管理员角色ID，事务回滚！");
        }

        Manager manager = new Manager();
        manager.setName(enterprise.getName());
        manager.setRoleId(Long.valueOf(entManagerRoleId));
        if (!managerService.createManager(manager, cityManager.getId())) {
            LOGGER.error("创建企业时失败，生成企业管理员角色节点时失败。企业编码 = " + enterprise.getCode());
            throw new TransactionException("创建企业时失败，生成企业管理员职位节点时失败，事务回滚！");

        }

        // 建立企业与企业管理员职位节点之间的关联关系
        EntManager entManager = new EntManager();
        entManager.setCreateTime(new Date());
        entManager.setUpdateTime(new Date());
        entManager.setDeleteFlag(new java.lang.Integer(Constants.DELETE_FLAG.UNDELETED.getValue()).byteValue());
        entManager.setEnterId(enterprise.getId());
        entManager.setManagerId(manager.getId());
        entManager.setCreatorId(manager.getId());

        if (!entManagerService.insertEntManager(entManager)) {
            LOGGER.error("创建企业时失败,生成企业与企业管理员职位节点关联关系时失败。企业编码 = " + enterprise.getCode());
            throw new TransactionException("创建企业时失败,生成企业与企业管理员职位节点关联关系时失败，事务回滚！ ");
        }
        if ("shanghai".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))) {
          //5、企业管理员的用户关联到企业管理员(相当于为用户设置企业管理员身份)
            Administer cmUser = administerService.selectByMobilePhone(enterprise.getCmPhone());
            Administer emUser = administerService.selectByMobilePhone(enterprise.getEnterpriseManagerPhone());
            Administer newAdmin = new Administer();
            newAdmin.setMobilePhone(enterprise.getEnterpriseManagerPhone());
            newAdmin.setUserName(enterprise.getEnterpriseManagerName());
            if (!administerService.createAdminister(manager.getId(), newAdmin, emUser, cmUser.getId())) {
                LOGGER.error("操作失败，用户ID-" + cmUser.getId() + "将手机号" + enterprise.getEnterpriseManagerPhone() + "用户设置为ManagerId:" + entManager.getId());
                throw new RuntimeException("将用户设置为企业管理员失败");
            }
        }
      
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean createOrUpdateEnterprise(Enterprise enterprise) {
        // 参数校验
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())) {
            return false;
        }

        // 判断企业是否存在
        Enterprise dbEnterprise = selectByCode(enterprise.getCode());

        // 企业不存在则新增
        if (dbEnterprise == null || DELETE_FLAG.DELETED.getValue() == dbEnterprise.getDeleteFlag().intValue()) {
            enterprise.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            return createNewEnterprise(enterprise);
        }

        // 更新企业基本信息
        dbEnterprise
                .setName(StringUtils.isNotBlank(enterprise.getName()) ? enterprise.getName() : dbEnterprise.getName());
        dbEnterprise.setEntName(
                StringUtils.isNotBlank(enterprise.getName()) ? enterprise.getName() : dbEnterprise.getName());
        dbEnterprise.setPhone(
                StringUtils.isNotBlank(enterprise.getPhone()) ? enterprise.getPhone() : dbEnterprise.getPhone());
        dbEnterprise.setUpdateTime(new Date());
        if(enterprise.getDeleteFlag() != null){
            dbEnterprise.setDeleteFlag(enterprise.getDeleteFlag());
        }
        // 如果是删除操作，必须先删除企业管理员
        if (enterprise.getDeleteFlag() != null
                && DELETE_FLAG.DELETED.getValue() == enterprise.getDeleteFlag().intValue()) {// 删除操作
            Long mangerId = entManagerService.getManagerIdForEnter(dbEnterprise.getId());
            List<Long> adminIds = adminManagerService.selectAdminIdByManagerId(mangerId);
            if (adminIds != null && !adminIds.isEmpty()) {
                LOGGER.error("删除企业失败，企业管理员存在,请先删除企业管理员。企业code = " + enterprise.getCode() + ",企业管理员ID = "
                        + adminIds.toString());
                return false;
            }
        }

        if (!updateByPrimaryKeySelective(dbEnterprise)) {
            LOGGER.error("更新企业信息时失败，更新企业基本信息操作失败，事务回滚。code = " + enterprise.getCode());
            throw new TransactionException("更新企业信息时失败，更新企业基本信息操作失败，事务回滚!");
        }

        // 获取企业管理员职位节点信息
        Manager entAmdin = entManagerService.getManagerForEnter(dbEnterprise.getId());
        if (entAmdin == null || DELETE_FLAG.DELETED.getValue() == entAmdin.getDeleteFlag().intValue()) {
            LOGGER.error("更新企业时失败，无法获取企业管理员职位节点。企业Id = " + dbEnterprise.getId());
            throw new TransactionException("更新企业时失败，无法获取企业管理员职位节点，事务回滚!");
        }

        // 获取客户经理职位节点
        String entManagerRoleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if (StringUtils.isBlank(entManagerRoleId)) {
            LOGGER.error("更新企业时失败，无法获取客户经理角色ID。config_key = " + GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
            throw new TransactionException("更新企业时失败，无法获取客户经理角色ID，事务回滚!");

        }

        // 获取企业的父节点：客户经理或市级管理员
        List<Manager> entManagers = managerService.selectEntParentNodeByEnterIdOrRoleId(dbEnterprise.getId(),
                Long.valueOf(entManagerRoleId));
        if (entManagers == null || entManagers.size() <= 0) {// 客户经理职位节点不存在，将企业管理员职位节点挂到市级管理员节点下
            // 获取市级管理员职位节点
            String cityManageRoldId = globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
            if (StringUtils.isBlank(cityManageRoldId)) {
                LOGGER.error("更新企业时失败，无法获取市级管理员角色ID。config_key = " + GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
                throw new TransactionException("更新企业时失败，无法获取市级管理员角色ID，事务回滚!");
            }
            Manager cityManager = managerService.get(Long.valueOf(cityManageRoldId), enterprise.getEnterpriseCity());
            if (cityManager == null || DELETE_FLAG.DELETED.getValue() == cityManager.getDeleteFlag().intValue()) {
                LOGGER.error("更新企业时失败，无法获取市级管理员职位节点。企业归属地 = " + enterprise.getEnterpriseCity());
                throw new TransactionException("更新企业时失败，无法获取市级管理员职位节点，事务回滚!");
            }

            // 更新企业管理员职位节点信息
            entAmdin.setParentId(cityManager.getId());
            if(enterprise.getDeleteFlag() != null){
                entAmdin.setDeleteFlag(enterprise.getDeleteFlag().byteValue());
            }
            entAmdin.setUpdateTime(new Date());
        } else {// 客户经理存在
            Manager oldCityManager = entManagers.get(0);
            if (!oldCityManager.getName().equals(enterprise.getEnterpriseCity())) {// 地市信息发生变化，重新挂到新的市级管理员节点下
                // 获取市级管理员职位节点信息
                String roleId = globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
                if (StringUtils.isBlank(roleId)) {
                    LOGGER.error(
                            "更新企业时失败，无法获取市级管理员角色ID。config_key = " + GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
                    throw new TransactionException("更新企业时失败，更新企业管理员与市级管理员关联关系时失败，事务回滚! code = " + enterprise.getCode());
                }
                Manager newCityManager = managerService.get(Long.valueOf(roleId), enterprise.getEnterpriseCity());
                if (newCityManager == null
                        || DELETE_FLAG.DELETED.getValue() == newCityManager.getDeleteFlag().intValue()) {
                    LOGGER.error("更新企业时失败，市级管理员职位节点不存在。code = " + enterprise.getCode());
                    throw new TransactionException("更新企业时失败，更新企业管理员与市级管理员关联关系时失败，事务回滚! code = " + enterprise.getCode());
                }
                // 更新企业管理员职位节点信息
                entAmdin.setParentId(newCityManager.getId());
            }
        }
        if(enterprise.getDeleteFlag() != null){
            entAmdin.setDeleteFlag(enterprise.getDeleteFlag().byteValue()); 
        }
        entAmdin.setUpdateTime(new Date());
        if (!managerService.updateByPrimaryKeySelective(entAmdin)) {
            LOGGER.error("更新企业时失败，更新企业管理员与市级管理员关联关系时失败，事务回滚。企业Id = " + dbEnterprise.getId());
            throw new TransactionException("更新企业时失败，更新企业管理员与市级管理员关联关系时失败，事务回滚! code = " + enterprise.getCode());
        }
        return true;
    }

    @Override
    public List<Enterprise> showEnterprisesAccountsForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();

        Long managerId = Long.parseLong(map.get("managerId").toString());
        String managerIds = managerService.getChildNodeString(managerId);
        map.put("managerIds", managerIds);

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        return enterpriseMapper.showEnterprisesAccountsForPageResult(map);
    }

    @Override
    public int showEnterprisesAccountsCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();
        Long managerId = Long.parseLong(map.get("managerId").toString());
        String managerIds = managerService.getChildNodeString(managerId);
        map.put("managerIds", managerIds);

        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 23:59:59");
        }
        return enterpriseMapper.showEnterprisesAccountsCount(map);
    }

    @Override
    public Map<Long, Double> queryAccounts(List<Long> ids) {
        List<Account> accounts = accountService.getCurrencyAccounts(ids);
        if (accounts == null) {
            return null;
        }

        Map<Long, Double> map = new LinkedHashMap<Long, Double>();
        for (Account account : accounts) {
            map.put(account.getEnterId(), account.getCount());
        }
        return map;
    }

    @Override
    public boolean isParentManage(Long entId, Long currentManageId) {
        //传入的id需要是当前用户的子节点
        String childrenStr = managerService.getChildNodeString(currentManageId);
        if (org.apache.commons.lang.StringUtils.isBlank(childrenStr)) {
            return false;
        }
        Manager manager = entManagerService.getManagerForEnter(entId);
        if (manager == null) {
            return false;
        }
        String[] children = childrenStr.split(",");
        for (String child : children) {
            if (child.equals(String.valueOf(manager.getId()))) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public Enterprise selectByPhone(String phone) {
        if(StringUtils.isBlank(phone)){
            return null;
        }
        return enterpriseMapper.selectByPhone(phone);
    }

    @Override
    public void blurEnterpriseInfo(Enterprise enterprise) {
        if(enterprise == null){
            return ;
        }       
        //企业名称模糊处理
        enterprise.setEntName(blurService.blurEntName(enterprise.getEntName()));
        enterprise.setName(blurService.blurEntName(enterprise.getName()));
        //企业编码模糊处理
        enterprise.setCode(blurService.blurEntCode(enterprise.getCode()));
        //邮箱模糊处理
        enterprise.setCmEmail(blurService.blurEmail(enterprise.getCmEmail()));
        enterprise.setEmail(blurService.blurEmail(enterprise.getEmail()));
        //手机号码模糊处理
        enterprise.setEnterpriseManagerPhone(blurService.blurMobile(enterprise.getEnterpriseManagerPhone()));
        enterprise.setPhone(blurService.blurMobile(enterprise.getPhone()));
        enterprise.setCustomerManagerPhone(blurService.blurMobile(enterprise.getCustomerManagerPhone()));        
    }

    @Override
    public List<Enterprise> getEnterByManagerIdEnterName(Long managerId,
            String entName) {
        String managerIds = managerService.getChildNodeString(managerId);
        return enterpriseMapper.getEnterByManagerIdEnterName(managerIds,entName);
    }
}
