package com.cmcc.vrp.gd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.gd.EntSrvRegReq;
import com.cmcc.vrp.ec.bean.gd.EntSrvRegResp;
import com.cmcc.vrp.ec.bean.gd.Service;
import com.cmcc.vrp.ec.bean.gd.USERINFOMAP;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceApprovalStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GDRegion;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "/gd")
public class EntSrvRegController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntSrvRegController.class);
    
    private static XStream xstream;
    private static final int MAX_LENGTH_255 = 255;
    private static final int MAX_LENGTH_64 = 64;

    static {
        xstream = new XStream();
        xstream.alias("EntSrvRegReq", EntSrvRegReq.class);
        xstream.alias("EntSrvRegResp", EntSrvRegResp.class);
        xstream.autodetectAnnotations(true);
    }
    
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AdministerService administerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    ProductService productService;
   
    /**
     * @param request
     * @param response
     * @throws IOException 
     */
    @RequestMapping(value = "entSrvReg", method = RequestMethod.POST)
    @ResponseBody
    public void entSrvReg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Response result = new Response();
        Gson gson = new Gson();
        EntSrvRegResp entSrvRegResp = new EntSrvRegResp();
        EntSrvRegReq entSrvRegReq = parse(request);
        if (entSrvRegReq == null) {
            entSrvRegResp.setResult("1");
            entSrvRegResp.setResultMsg("解析报文出错");
            try {
                result.setCode("1");
                result.setMsg(xstream.toXML(entSrvRegResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        if (!validate(entSrvRegReq)) {
            entSrvRegResp.setResult("2");
            entSrvRegResp.setResultMsg("必要字段缺失或为空");
            try {
                result.setCode("2");
                result.setMsg(xstream.toXML(entSrvRegResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        if (!getPlatform().equals(entSrvRegReq.getPrdCode())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if(!"0".equals(entSrvRegReq.getOptCode())) {
            entSrvRegResp.setResult("0");
            entSrvRegResp.setResultMsg("success");
            try {
                result.setCode("0");
                result.setMsg(xstream.toXML(entSrvRegResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e1) {
                LOGGER.error("响应出错，错误信息为{}.", e1.toString());
            }
            return;
        }
        Enterprise enterprise = null;
        EnterprisesExtInfo enterprisesExtInfo = null;
        try {
            enterprise = initEnterprise(entSrvRegReq);
            enterprisesExtInfo = initEnterpriseExtInfo(entSrvRegReq);
            if (!checkEnterprise(enterprise, enterprisesExtInfo)) {
                entSrvRegResp.setResult("4");
                entSrvRegResp.setResultMsg("部分字段超过最大长度限制");
                try {
                    result.setCode("4");
                    result.setMsg(xstream.toXML(entSrvRegResp));
                    StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e1) {
                    LOGGER.error("响应出错，错误信息为{}.", e1.toString());
                }
                return;
            }
            if (!createNewEnterprise(enterprise, enterprisesExtInfo)) {
                entSrvRegResp.setResult("5");
                entSrvRegResp.setResultMsg("创建企业失败");
                try {
                    result.setCode("5");
                    result.setMsg(xstream.toXML(entSrvRegResp));
                    StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e1) {
                    LOGGER.error("响应出错，错误信息为{}.", e1.toString());
                }
                return;
            }
        } catch (Exception e) {
            entSrvRegResp.setResult("3");
            entSrvRegResp.setResultMsg(e.getMessage());
            try {
                result.setCode("3");
                result.setMsg(xstream.toXML(entSrvRegResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e1) {
                LOGGER.error("响应出错，错误信息为{}.", e1.toString());
            }
            return;
        }
        
        entSrvRegResp.setResult("0");
        entSrvRegResp.setResultMsg("success");
        try {
            result.setCode("0");
            result.setMsg(xstream.toXML(entSrvRegResp));
            StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应出错，错误信息为{}.", e.toString());
        }
        return;
        
    }
    private EnterprisesExtInfo initEnterpriseExtInfo(EntSrvRegReq entSrvRegReq) {
        EnterprisesExtInfo enterprisesExtInfo = new EnterprisesExtInfo();
        if ("YES".equals(globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey()))) {
            Integer joinType = 1;
            Service service = entSrvRegReq.getService();
            List<USERINFOMAP> userinfomaps = service.getUserinfomap();
            if (userinfomaps != null
                && userinfomaps.size() != 0){
                for (USERINFOMAP userinfomap:userinfomaps) {
                    if ("MemPayMode".equals(userinfomap.getItemName())) {
                        joinType = Integer.valueOf(userinfomap.getItemValue());
                        break;
                    }
                }
            }
            enterprisesExtInfo.setJoinType(joinType);   
        }       
        
        enterprisesExtInfo.setEcCode(entSrvRegReq.getEntCode());
        enterprisesExtInfo.setEcPrdCode(entSrvRegReq.getPrdOrdCode());

        return enterprisesExtInfo;
    }
    /**
     * @param enterprise
     * @param enterprisesExtInfo
     * @return
     */
    @Transactional
    public boolean createNewEnterprise(Enterprise enterprise, EnterprisesExtInfo enterprisesExtInfo) {
        if (enterprise == null || StringUtils.isEmpty(enterprise.getCode())
                || StringUtils.isEmpty(enterprise.getEnterpriseCity())) {
            return false;
        }
        Administer dbAdminister = administerService.selectByMobilePhone(enterprise.getEnterpriseManagerPhone());
        if (dbAdminister != null && DELETE_FLAG.UNDELETED.getValue() == dbAdminister.getDeleteFlag()) {
            LOGGER.error("创建用户时失败，用户信息已存在。 mobile = " + enterprise.getEnterpriseManagerPhone());
            throw new TransactionException("用户信息已存在");
        }
        String roleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        Manager accountManager = managerService.get(Long.valueOf(roleId), enterprise.getEnterpriseCity());

        // 生成企业基本信息
        if (!enterprisesService.insertSelective(enterprise)) {
            throw new TransactionException("创建企业时失败:生成企业基本信息时失败 code=" + enterprise.getCode());
        }

        // 为企业设置默认短信模板
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(enterprise.getId());

        // 新建企业管理员节点
        String entManagerRoleId = globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
        if (StringUtils.isEmpty(entManagerRoleId)) {
            LOGGER.error(
                    "创建企业时失败，无法获取企业管理员角色ID。config_key = " + GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
            throw new TransactionException("无法获取企业管理员角色ID");
        }

        Manager manager = new Manager();
        manager.setName(enterprise.getName());
        manager.setRoleId(Long.valueOf(entManagerRoleId));
        if (!managerService.createManager(manager, accountManager.getId())) {
            LOGGER.error("创建企业时失败，生成企业管理员角色节点时失败。企业编码 = " + enterprise.getCode());
            throw new TransactionException("生成企业管理员职位节点时失败");

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
            throw new TransactionException("生成企业与企业管理员职位节点关联关系时失败 ");
        }
        if (!StringUtils.isEmpty(enterprise.getEnterpriseManagerPhone())
                && !StringUtils.isEmpty(enterprise.getEnterpriseManagerName())) {
          //5、企业管理员的用户关联到企业管理员(相当于为用户设置企业管理员身份)
            Administer cmUser = administerService.selectByMobilePhone(enterprise.getCmPhone());
            Administer emUser = administerService.selectByMobilePhone(enterprise.getEnterpriseManagerPhone());
            Administer newAdmin = new Administer();
            newAdmin.setMobilePhone(enterprise.getEnterpriseManagerPhone());
            newAdmin.setUserName(enterprise.getEnterpriseManagerName());
            if (!StringUtils.isEmpty(enterprise.getEmail())) {
                newAdmin.setEmail(enterprise.getEmail());
            }
            if (!administerService.createAdminister(manager.getId(), newAdmin, emUser, cmUser.getId())) {
                LOGGER.error("操作失败，用户ID-" + cmUser.getId() + "将手机号" + enterprise.getEnterpriseManagerPhone() + "用户设置为ManagerId:" + entManager.getId());
                throw new RuntimeException("将用户设置为企业管理员失败");
            }
        }
        enterprisesExtInfo.setEnterId(enterprise.getId());
        enterprisesExtInfo.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        enterprisesExtInfo.setCreateTime(new Date());
        enterprisesExtInfo.setUpdateTime(new Date());
        if (!enterprisesExtInfoService.insert(enterprisesExtInfo)) {
            LOGGER.info("增加企业拓展信息失败. ExtEntInfo = {}.", new Gson().toJson(enterprisesExtInfo));
            throw new RuntimeException("创建企业失败");
        }
        return true;
    }
    private boolean validate(EntSrvRegReq entSrvRegReq) {

        return !StringUtils.isEmpty(entSrvRegReq.getOptCode())
                && !StringUtils.isEmpty(entSrvRegReq.getEntCode())
                && !StringUtils.isEmpty(entSrvRegReq.getEntName())
                && !StringUtils.isEmpty(entSrvRegReq.getAreaCode())
                && !StringUtils.isEmpty(entSrvRegReq.getSiCode())
                && !StringUtils.isEmpty(entSrvRegReq.getSiName())
                && !StringUtils.isEmpty(entSrvRegReq.getPrdCode())
                && !StringUtils.isEmpty(entSrvRegReq.getPrdOrdCode())
                && !StringUtils.isEmpty(entSrvRegReq.getStartEfft())
                && !StringUtils.isEmpty(entSrvRegReq.getEndEfft())
//                && !StringUtils.isEmpty(entSrvRegReq.getPrdAdminUser())
//                && !StringUtils.isEmpty(entSrvRegReq.getPrdAdminName())
//                && !StringUtils.isEmpty(entSrvRegReq.getPrdAdminMobile())
//                && !StringUtils.isEmpty(entSrvRegReq.getPrdAdminMail())
                && !StringUtils.isEmpty(entSrvRegReq.getPrdChannel())
                && !StringUtils.isEmpty(entSrvRegReq.getPrdDevUserId());
    }
    private Enterprise initEnterprise(EntSrvRegReq entSrvRegReq) {
        Enterprise enterprise = new Enterprise();
        Long discount = 100L;
        Service service = entSrvRegReq.getService();
        List<USERINFOMAP> userinfomaps = service.getUserinfomap();
        if (userinfomaps != null
            && userinfomaps.size() != 0){
            for (USERINFOMAP userinfomap:userinfomaps) {
                if ("discount8588M".equals(userinfomap.getItemName())) {
                    discount = Long.parseLong(userinfomap.getItemValue());
                    break;
                }
            }
        }
        // 获取客户经理职位节点信息
        String roleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if (StringUtils.isEmpty(roleId)) {
            LOGGER.error("创建企业时失败，无法获取客户经理角色ID。config_key = " + GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
            throw new RuntimeException("创建企业时失败，无法获取客户经理角色ID");
        }
        Manager cityManager = managerService.get(Long.valueOf(roleId), GDRegion.getAreaName(entSrvRegReq.getAreaCode()));
        if (cityManager == null || DELETE_FLAG.DELETED.getValue() == cityManager.getDeleteFlag().intValue()) {
            LOGGER.error("创建企业时失败，客户经理职位节点不存在。city = " + GDRegion.getAreaName(entSrvRegReq.getAreaCode()));
            throw new RuntimeException(entSrvRegReq.getAreaCode() + "地区客户经理职位不存在");
        }
        ArrayList<Long> manageIds = new ArrayList<Long>();
        manageIds.add(cityManager.getId());
        List<Administer> administers = administerService.getByManageIds(manageIds);
        if (administers.isEmpty()) {
            LOGGER.error("创建企业时失败，客户经理用户不存在。city = " + GDRegion.getAreaName(entSrvRegReq.getAreaCode()));
            throw new RuntimeException(GDRegion.getAreaName(entSrvRegReq.getAreaCode()) + "地区客户经理用户不存在");
        }
        Administer cmAdminister = administerService.getByManageIds(manageIds).get(0);

        enterprise.setCode(entSrvRegReq.getEntCode());
        enterprise.setName(entSrvRegReq.getEntName());
        enterprise.setEntName(entSrvRegReq.getEntName());
        enterprise.setEnterpriseCity(GDRegion.getAreaName(entSrvRegReq.getAreaCode()));
        enterprise.setPhone(entSrvRegReq.getPrdAdminMobile());
        enterprise.setCmManagerName(cmAdminister.getUserName());
        enterprise.setCmPhone(cmAdminister.getMobilePhone());
        if (!StringUtils.isEmpty(entSrvRegReq.getPrdAdminName())) {
            enterprise.setEnterpriseManagerName(entSrvRegReq.getPrdAdminName());
        }
        if (!StringUtils.isEmpty(entSrvRegReq.getPrdAdminMobile())) {
            enterprise.setEnterpriseManagerPhone(entSrvRegReq.getPrdAdminMobile());
        }
        if (!StringUtils.isEmpty(entSrvRegReq.getPrdAdminMail())) {
            enterprise.setEmail(entSrvRegReq.getPrdAdminMail());
        }
        enterprise.setDiscount(discount);
        
        Date startTime = DateUtil.parse("yyyy-MM-dd HH:mm:SS", entSrvRegReq.getStartEfft());
        Date endTime = DateUtil.parse("yyyy-MM-dd HH:mm:SS", entSrvRegReq.getEndEfft());
        //设置合作开始时间和结束时间
        enterprise.setStartTime(startTime);
        enterprise.setEndTime(endTime);
        //设置工商营业执有效期
        enterprise.setLicenceStartTime(startTime);
        enterprise.setLicenceEndTime(endTime);
        enterprise.setInterfaceExpireTime(endTime);
        enterprise.setCustomerTypeId(1l);
        if ("0".equals(entSrvRegReq.getOptCode())) {
            initEnterprise(enterprise);
            enterprise.setDeleteFlag(EnterpriseStatus.CONFIRM.getCode());
        } 

        return enterprise;
    }
    private void initEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            return;
        }
        //设置企业状态
        enterprise.setStatus((byte) 3);
        //设置企业秘钥
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        //设置EC接口开关，默认打开
        enterprise.setInterfaceFlag(InterfaceStatus.OPEN.getCode());
        enterprise.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
        enterprise.setInterfaceExpireTime(DateUtil.converStrYMDToDate("2099-01-01 00:00:00"));
    }
    
    private boolean checkEnterprise(Enterprise enterprise, EnterprisesExtInfo enterprisesExtInfo) {
        List<EnterprisesExtInfo> enterprisesExtInfos = 
                enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(enterprisesExtInfo.getEcCode(), enterprisesExtInfo.getEcPrdCode());
        if (enterprise == null) {
            return false;
        } else if (StringUtils.isEmpty(enterprise.getName()) || enterprise.getName().length() > MAX_LENGTH_255) {//企业名称，必填
            LOGGER.error("企业信息报文校验失败：name非法，约定name非空且最大长度为" + MAX_LENGTH_255 + ",实际name=" + enterprise.getName());
            return false;
        } else if (StringUtils.isEmpty(enterprisesExtInfo.getEcPrdCode()) || enterprisesExtInfo.getEcPrdCode().length() > MAX_LENGTH_64) {
            LOGGER.error("企业信息报文校验失败：集团产品号码非法，约定集团产品号码非空且最大长度为" + MAX_LENGTH_64 + ",实际集团产品号码=" + enterprisesExtInfo.getEcPrdCode());
            return false;
        } else if (StringUtils.isEmpty(enterprise.getCode()) || enterprise.getCode().length() > MAX_LENGTH_64) {//企业编码，必填
            LOGGER.error("企业信息报文校验失败：id非法，约定id非空且最大长度为" + MAX_LENGTH_64 + ",实际id=" + enterprise.getCode());
            return false;
        } else if (StringUtils.isEmpty(enterprise.getEnterpriseCity())) {//企业地市信息必填
            LOGGER.error("企业信息报文校验失败：city非法，约定city非空,实际city=" + enterprise.getEnterpriseCity());
            throw new RuntimeException("企业地区非空");
//        } else if (StringUtils.isEmpty(enterprise.getEnterpriseManagerPhone()) || enterprise.getEnterpriseManagerPhone().length() > MAX_LENGTH_64) {
//            LOGGER.error("企业信息报文校验失败：企业管理员手机号码非空,实际=" + enterprise.getEnterpriseManagerPhone());
//            throw new RuntimeException("企业管理员手机号码非空");
//        } else if (StringUtils.isEmpty(enterprise.getEnterpriseManagerName()) || enterprise.getEnterpriseManagerName().length() > MAX_LENGTH_64) {
//            LOGGER.error("企业信息报文校验失败：企业管理员手姓名非空,实际=" + enterprise.getEnterpriseManagerName());
//            throw new RuntimeException("企业管理员姓名非空");
        } else if (enterprisesExtInfos != null
                && enterprisesExtInfos.size() != 0) {
            LOGGER.error("该企业已存在，不可以重复创建，enterCode{},entPrdCode{}", enterprisesExtInfo.getEcCode(), enterprisesExtInfo.getEcPrdCode());
            throw new RuntimeException("该企业已存在，不可以重复创建");
        }
        return true;
    }
    private EntSrvRegReq parse(HttpServletRequest request) {
        String reqStr = null;
        try {
            reqStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
            LOGGER.info("从BOSS侧接收到订购关系信息， 信息内容为{}.", reqStr == null ? "空" : reqStr);
            return (EntSrvRegReq) xstream.fromXML(reqStr);
        } catch (Exception e) {
            LOGGER.error("解析回调参数时出错.");
            return null;
        }
    }
    private String getPlatform() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_PLATFORM.getKey());
    }
}
