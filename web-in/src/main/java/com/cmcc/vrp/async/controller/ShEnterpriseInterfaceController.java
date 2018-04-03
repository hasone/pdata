package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.ShEnterprise;
import com.cmcc.vrp.ec.bean.ShEnterpriseInfo;
import com.cmcc.vrp.ec.bean.ShEnterpriseResponse;
import com.cmcc.vrp.enums.InterfaceApprovalStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "/shEnterprises")
public class ShEnterpriseInterfaceController {
    private static final Logger logger = LoggerFactory.getLogger(ShEnterpriseInterfaceController.class);
    private static final int MAX_LENGTH_255 = 255;
    private static final int MAX_LENGTH_64 = 64;
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    ManagerService managerService;
    private XStream xStream;

    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", ShEnterprise.class);
        xStream.alias("Response", ShEnterpriseResponse.class);
        xStream.autodetectAnnotations(true);
    }
    /**
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public void enterpriseOperation(final HttpServletRequest request, final HttpServletResponse response) {
        String appKey = null;
        Enterprise enterprise;
        ShEnterpriseResponse shEnterpriseResponse = new ShEnterpriseResponse();
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
               || (enterprise = enterprisesService.selectByAppKey(appKey)) == null
               || !getEntCode().equals(enterprise.getCode())) {
            logger.error("认证未通过, AppKey = {}.", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        ShEnterprise enterReqStr = retrieveEnterReq(request);
        ShEnterpriseInfo shEnterpriseInfo = null;
        logger.info("收到企业接口报文：" + JSONObject.toJSONString(enterReqStr));
        if (enterReqStr == null
                || StringUtils.isBlank(enterReqStr.getOpr())
                || !"Enterprise".equals(enterReqStr.getOpr())
                || (shEnterpriseInfo = enterReqStr.getInfo()) == null
                || com.cmcc.vrp.util.StringUtils.isEmpty(enterReqStr.getType())
                || enterReqStr.getType().length() > MAX_LENGTH_64) {
            shEnterpriseResponse.setCode("0001");
            shEnterpriseResponse.setMessage("请求报文错误");
            try {
                StreamUtils.copy(xStream.toXML(shEnterpriseResponse), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                logger.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        
        try {
            if (!handleEnterprise(shEnterpriseInfo, enterReqStr.getType())) {
                shEnterpriseResponse.setCode("0002");
                shEnterpriseResponse.setMessage("创建企业失败");
                try {
                    StreamUtils.copy(xStream.toXML(shEnterpriseResponse), Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e) {
                    logger.error("响应出错，错误信息为{}.", e.toString());
                }
                return;
            }
        } catch (Exception e) {
            shEnterpriseResponse.setCode("0003");
            shEnterpriseResponse.setMessage(e.getMessage());
            try {
                StreamUtils.copy(xStream.toXML(shEnterpriseResponse), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e1) {
                logger.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        shEnterpriseResponse.setCode("0000");
        shEnterpriseResponse.setMessage("success");
        try {
            StreamUtils.copy(xStream.toXML(shEnterpriseResponse), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应出错，错误信息为{}.", e.toString());
        }
        
    }
    /**
     * 
     * @Title: jsonToEnterpise 
     * @Description: 接口报文转为Model类型
     * @param json
     * @return
     * @return: Enterprise
     */
    private Enterprise xmlToEnterpise(ShEnterpriseInfo enterInfo) {
        Enterprise enterprise = new Enterprise();

////        //获取客户经理角色（手机号码或者地市）
////        Administer cmAdminister = administerService.selectByMobilePhone(enterInfo.getAccountManagerTel());
//        if (cmAdminister == null
//                || !String.valueOf(cmAdminister.getRoleId()).equals(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey()))) {
//            logger.info("客户经理手机号码错误，mobile{}",enterInfo.getAccountManagerTel());
//            String roleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
//            Manager cityManager = managerService.get(Long.valueOf(roleId), enterInfo.getCounty());
//            if (cityManager == null) {
//                logger.error("根据客户经理手机号码和地市没有找到相关客户经理角色！");
//                return enterprise;
//            }
//            ArrayList<Long> manageIds = new ArrayList<Long>();
//            manageIds.add(cityManager.getId());
//            cmAdminister = administerService.getByManageIds(manageIds).get(0);           
//        }
     // 获取客户经理职位节点信息
        String roleId = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if (StringUtils.isBlank(roleId)) {
            logger.error("创建企业时失败，无法获取客户经理角色ID。config_key = " + GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
            throw new RuntimeException("创建企业时失败，无法获取客户经理角色ID");
        }
        Manager cityManager = managerService.get(Long.valueOf(roleId), enterInfo.getCounty());
        if (cityManager == null || DELETE_FLAG.DELETED.getValue() == cityManager.getDeleteFlag().intValue()) {
            logger.error("创建企业时失败，客户经理职位节点不存在。city = " + enterInfo.getCounty());
            throw new RuntimeException(enterInfo.getCounty() + "地区客户经理职位不存在");
        }
        ArrayList<Long> manageIds = new ArrayList<Long>();
        manageIds.add(cityManager.getId());
        List<Administer> administers = administerService.getByManageIds(manageIds);
        if (administers.isEmpty()) {
            logger.error("创建企业时失败，客户经理用户不存在。city = " + enterprise.getEnterpriseCity());
            throw new RuntimeException(enterInfo.getCounty() + "地区客户经理用户不存在");
        }
        Administer cmAdminister = administerService.getByManageIds(manageIds).get(0);   
        enterprise.setCode(enterInfo.getEnterCode());
        enterprise.setName(enterInfo.getName());
        enterprise.setEntName(enterInfo.getName());
        enterprise.setEnterpriseCity(enterInfo.getCounty());
        enterprise.setPhone(enterInfo.getEntManagerTel());
        enterprise.setCmManagerName(cmAdminister.getUserName());
        enterprise.setCmPhone(cmAdminister.getMobilePhone());
        enterprise.setEnterpriseManagerName(enterInfo.getEntManagerName());
        enterprise.setEnterpriseManagerPhone(enterInfo.getEntManagerTel());
        return enterprise;
    }

    /**
     * 
     * @Title: checkEnterprise 
     * @Description: 企业报文校验
     * @param enterprise
     * @return
     * @return: boolean
     */
    private boolean checkEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            return false;
        } else if (StringUtils.isBlank(enterprise.getName()) || enterprise.getName().length() > MAX_LENGTH_255) {//企业名称，必填
            logger.error("企业信息报文校验失败：name非法，约定name非空且最大长度为" + MAX_LENGTH_255 + ",实际name=" + enterprise.getName());
            return false;
        } else if (StringUtils.isBlank(enterprise.getCode()) || enterprise.getCode().length() > MAX_LENGTH_64) {//企业编码，必填
            logger.error("企业信息报文校验失败：id非法，约定id非空且最大长度为" + MAX_LENGTH_64 + ",实际id=" + enterprise.getCode());
            return false;
        } else if (StringUtils.isBlank(enterprise.getEnterpriseCity())) {//企业地市信息必填
            logger.error("企业信息报文校验失败：city非法，约定city非空,实际city=" + enterprise.getEnterpriseCity());
            throw new RuntimeException("企业地区非空");
        } else if (StringUtils.isBlank(enterprise.getEnterpriseManagerPhone()) || enterprise.getEnterpriseManagerPhone().length() > MAX_LENGTH_64) {
            logger.error("企业信息报文校验失败：企业管理员手机号码非空,实际=" + enterprise.getEnterpriseManagerPhone());
            throw new RuntimeException("企业管理员手机号码非空");
        } else if (StringUtils.isBlank(enterprise.getEnterpriseManagerName()) || enterprise.getEnterpriseManagerName().length() > MAX_LENGTH_64) {
            logger.error("企业信息报文校验失败：企业管理员手姓名非空,实际=" + enterprise.getEnterpriseManagerName());
            throw new RuntimeException("企业管理员姓名非空");
        } else if (enterprisesService.selectByCode(enterprise.getCode()) != null) {
            logger.error("该企业已存在，不可以重复创建，enterCode{}", enterprise.getCode());
            throw new RuntimeException("该企业已存在，不可以重复创建");
        }
        return true;
    }
    private boolean handleEnterprise(ShEnterpriseInfo enterInfo, String type) {
        //报文转换
        Enterprise enterprise = xmlToEnterpise(enterInfo);

        if (!"NEW".equals(type)) {
            return true;
        }
        //参数校验
        if (!checkEnterprise(enterprise)) {
            return false;
        }

        if ("DELETE".equals(type)) {
            enterprise.setDeleteFlag(DELETE_FLAG.DELETED.getValue());
        } else {
            initEnterprise(enterprise);
            enterprise.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        }
        return enterprisesService.createOrUpdateEnterprise(enterprise);
    }
    /**
     * 
     * @Title: initEnterprise 
     * @Description: 初始化默认参数
     * @param enterprise
     * @return: void
     */
    private void initEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            return;
        }
        //初始化默认参数
        //设置企业状态
        enterprise.setStatus((byte) 3);
        //设置企业appSecret
        //enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        //企业默认无折扣
        enterprise.setDiscount(1L);
        //设置EC接口开关，默认关闭
        enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());
        Date startTime = new Date();
        Date endTime = DateUtil.getDateAfter(startTime, 365 * 100);//有效期100年
        //设置接口有效期
        enterprise.setInterfaceExpireTime(endTime);
        //设置EC接口默认审核通过
        enterprise.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
        //设置合作开始时间和结束时间
        enterprise.setStartTime(startTime);
        enterprise.setEndTime(endTime);
        //enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        //设置工商营业执有效期
        enterprise.setLicenceStartTime(startTime);
        enterprise.setLicenceEndTime(endTime);
    }
    private ShEnterprise retrieveEnterReq(HttpServletRequest request) {
        try {
            String enterReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (ShEnterprise) xStream.fromXML(enterReqStr);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }
    public String getEntCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.SH_BOSS_ENTER_CODE.getKey());
    }
}
