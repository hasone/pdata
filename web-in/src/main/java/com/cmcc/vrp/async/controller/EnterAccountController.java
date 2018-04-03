package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.EntAccountReq;
import com.cmcc.vrp.ec.bean.EntAccountResp;
import com.cmcc.vrp.ec.bean.EntInfoReqData;
import com.cmcc.vrp.ec.bean.EntInfoRespData;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PotentialCusterService;
import com.cmcc.vrp.util.DateUtil;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 * 2016/11/09
 */

@Controller
public class EnterAccountController {
    private static final Logger logger = LoggerFactory.getLogger(EnterAccountController.class);
    public static final String MOBILE_REGEX = "^1[3-8][0-9]{9}$";
    public final static String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
            + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	
    @Autowired
	AdministerService administerService;
	
    @Autowired
	AuthorityService authorityService;
	
    @Autowired
	ManagerService managerService; 
	
    @Autowired
	AdminManagerService adminManagerService;
	
    @Autowired
	PotentialCusterService potentialCusterService;
	
    @Autowired
	EnterprisesService enterprisesService;
	
    @Autowired
	GiveMoneyEnterService gmeService;
	
    @Autowired
	EnterpriseFileService enterpriseFileService;
	
    @Autowired
	AccountService accountService;
    
    @Autowired 
    EntWhiteListService entWhiteListService;
    
    @Autowired
    EntCallbackAddrService entCallbackAddrService;
	
    private XStream xStream;
	
    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", EntAccountReq.class);
        xStream.alias("Response", EntAccountResp.class);
        xStream.autodetectAnnotations(true);
    }
	
	/**
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "enterAccount", method = RequestMethod.POST)
	@ResponseBody
	public void enterAccount(HttpServletRequest request, HttpServletResponse response) {
        EntAccountReq req = null;
        String appKey = null;
        Enterprise enterprise = null;
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
				|| (req = retrieveEntAccountReq(request)) == null
				|| !validate(req)
				|| (enterprise = enterprisesService.selectByAppKey(appKey)) == null
                || enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
            logger.error("无效的开户请求参数：entAccountReq = {}. enterprise = {}", 
					JSONObject.toJSONString(req), enterprise == null ? "" : JSONObject.toJSONString(enterprise));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (enterprisesService.selectByCode(req.getEntInfoReqData().getCode()) != null) {
            logger.error("该企业编码已存在, code={}", req.getEntInfoReqData().getCode());
            response.setStatus(422);
            return;
        }
        if (!validate1(req.getEntInfoReqData())) {
            response.setStatus(422);
            return;
        }
		
        EntInfoReqData entInfoReqData = req.getEntInfoReqData();
        Enterprise newEnterprise = new Enterprise();
		
        String newAppKey = UUID.randomUUID().toString().replace("-", "");
        String newAppSecret = UUID.randomUUID().toString().replace("-", "");
        newEnterprise.setAppSecret(newAppSecret);
        newEnterprise.setAppKey(newAppKey);
        newEnterprise.setName(entInfoReqData.getName());
        newEnterprise.setCode(entInfoReqData.getCode());
        newEnterprise.setEntName(entInfoReqData.getEntName());
        newEnterprise.setEmail(entInfoReqData.getEmail());
        newEnterprise.setPhone(entInfoReqData.getPhone());
        newEnterprise.setEnterpriseManagerName(entInfoReqData.getEntManageName());
        newEnterprise.setEnterpriseManagerPhone(entInfoReqData.getEntManagePhone());
        newEnterprise.setCmPhone(entInfoReqData.getCmPhone());
        newEnterprise.setCmEmail(entInfoReqData.getCmEmail());
        newEnterprise.setCustomerTypeId(NumberUtils.toLong(entInfoReqData.getCustomerType()));
        newEnterprise.setPayTypeId(NumberUtils.toLong(entInfoReqData.getPayType()));
        newEnterprise.setDiscount(NumberUtils.toLong(entInfoReqData.getDiscount()));
        newEnterprise.setGiveMoneyId(NumberUtils.toLong(entInfoReqData.getGiveMoney()));
        newEnterprise.setLicenceStartTime(DateUtil.parse("yyyy-MM-dd HH:mm:ss", entInfoReqData.getLicenceStartTime()));
        newEnterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(DateUtil.parse("yyyy-MM-dd HH:mm:ss", entInfoReqData.getLicenceEndTime())));
        newEnterprise.setStartTime(DateUtil.parse("yyyy-MM-dd HH:mm:ss", entInfoReqData.getStartTime()));
        newEnterprise.setEndTime(DateUtil.getEndTimeOfDate(DateUtil.parse("yyyy-MM-dd HH:mm:ss", entInfoReqData.getLicenceEndTime())));
		
        String cmPhone = entInfoReqData.getCmPhone();
        String emPhone = entInfoReqData.getEntManagePhone();
        String emName = entInfoReqData.getEntManageName();
        
        Administer administer = administerService.selectByMobilePhone(cmPhone);
		
        if (!validatePotEnterprise(newEnterprise, cmPhone, emPhone, emName)) {
            response.setStatus(422);
            return;
        }
        initPotentialEnt(newEnterprise, administer);
        
        EnterpriseFile enterpriseFile = new EnterpriseFile();
        enterpriseFile.setBusinessLicence(entInfoReqData.getLicenceImageName());
        enterpriseFile.setLicenceKey(entInfoReqData.getLicenceImageKey());
        enterpriseFile.setAuthorizationCertificate(entInfoReqData.getAuthorizationName());
        enterpriseFile.setAuthorizationKey(entInfoReqData.getAuthorizationKey());
        enterpriseFile.setIdentificationCard(entInfoReqData.getIdentificationName());
        enterpriseFile.setIdentificationKey(entInfoReqData.getIdentificationKey());
        enterpriseFile.setIdentificationBack(entInfoReqData.getIdentificationBackName());
        enterpriseFile.setIdentificationBackKey(entInfoReqData.getIdentificationBackKey());
        enterpriseFile.setCustomerfileName(entInfoReqData.getCustomerFileName());
        enterpriseFile.setCustomerfileKey(entInfoReqData.getCustomerFileKey());
        enterpriseFile.setContractName(entInfoReqData.getContractName());
        enterpriseFile.setContractKey(entInfoReqData.getContractKey());
        enterpriseFile.setImageName(entInfoReqData.getApproveImageName());
        enterpriseFile.setImageKey(entInfoReqData.getApproveImageKey());
        try {
            if (potentialCusterService.savePotentialEnterprise(newEnterprise, cmPhone, emPhone, emName, administer.getId())
        			&& saveQualificationCooperation(enterprisesService.selectByAppKey(newAppKey), enterpriseFile)) {
            	logger.info("用户ID:" + administer.getId() + " 创建新企业成功"
        					+ " 企业名称：" + newEnterprise.getName() + " 企业编码：" + newEnterprise.getCode()
        					+ "企业品牌名：" + newEnterprise.getEntName());
            } else {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            	logger.error("企业开户失败");
            	return;
            }
        } catch (Exception e){
            logger.error("企业开户失败" + e.getMessage());
        }
        Long enterId = enterprisesService.selectByAppKey(newAppKey).getId();
        
        if (newEnterprise.getGiveMoneyId() != null) {
            if (gmeService.selectByEnterId(enterId) != null) {
                if (!gmeService.updateByEnterId(newEnterprise.getGiveMoneyId(),
                		enterId)) {
                    logger.error("存送比更新失败");
                    throw new RuntimeException();
                }
            } else {
                if (!gmeService.insert(newEnterprise.getGiveMoneyId(),
                		enterId)) {
                    logger.error("存送比插入失败");
                    throw new RuntimeException();
                }
            }
        }
        
        insertIpAndCallbackAddrs(entInfoReqData.getIpAddr(), entInfoReqData.getCallbackAddr(), enterId);
        
        
        EntAccountResp entAccountResp = new EntAccountResp();
        EntInfoRespData entInfoRespData = new EntInfoRespData();
        entInfoRespData.setAppkey(newAppKey);
        entInfoRespData.setAppsecret(newAppSecret);
        entInfoRespData.setId(enterId);   
        entAccountResp.setEntInfoRespData(entInfoRespData);
        entAccountResp.setResponseTime(DateUtil.getRespTime());
        try {
            StreamUtils.copy(xStream.toXML(entAccountResp), Charsets.UTF_8, response.getOutputStream());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        
    }
	

    private void insertIpAndCallbackAddrs(String ipAddr, String callbackAddr, Long entId) {
    	List<String> ips = new ArrayList<String>();
    	ips.add(ipAddr);
    	if(!entWhiteListService.insertIps(ips, entId)){
            throw new RuntimeException("ip白名单插入失败！entId=" + entId);
        } 
    	EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setCallbackAddr(callbackAddr);
        entCallbackAddr.setEntId(entId);
        entCallbackAddr.setCreateTime(new Date());
        entCallbackAddr.setUpdateTime(new Date());
        entCallbackAddr.setDeleteFlag(0);
        if(!entCallbackAddrService.insert(entCallbackAddr)){
            throw new RuntimeException("回调地址插入失败！entId=" + entId);
        }
    }
	
    //获取开户请求参数
    private EntAccountReq retrieveEntAccountReq(HttpServletRequest request) {
    	try {
            String entAccountStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (EntAccountReq) xStream.fromXML(entAccountStr);
    	} catch (Exception e) {
    	    return null;
    	}
    }
	
	
    private void initPotentialEnt(Enterprise enterprise, Administer administer) {
    	enterprise.setInterfaceFlag(1);
        enterprise.setStatus((byte) 3);
        enterprise.setCreateTime(new Date());
        enterprise.setUpdateTime(new Date());
        enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
        enterprise.setCreatorId(administer.getId());
    }
	
    private boolean validatePotEnterprise(Enterprise enterprise, String cMPhone, String eMPhone, String eMName) {
    	if (enterprise == null) {
            return false;
    	}

    	try {
            enterprise.selfCheck();
    	} catch (Exception e) {
            return false;
        }

        return checkCM2Ent(cMPhone) && checkEM2Ent(enterprise, eMPhone, eMName);
    }
	
	
    private boolean checkCM2Ent(String cMPhone) {
    	//判断用户是否已经有managerId
    	Administer admin = administerService.selectByMobilePhone(cMPhone);
    	if (admin == null) {
    	    logger.info("该号码的客户经理不存在phone={}", cMPhone);
    	    return false;
    	} else {
    	    Manager manager = managerService.selectByAdminId(admin.getId());
    	    if (manager == null) {
    	    	logger.info("该号码的用户没有管理员身份phone={},adminId={}", cMPhone, admin.getId());
    	    	return false;
    	    } else {
    	    	//查找该用户的管理员身份所对应的角色，是否具有成为企业管理员父节点的权限
    	    	boolean roleFlag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");
    	    	if (!roleFlag) {
    	    	    logger.info("客户经理手机号码对应的用户的角色，不能成为企业管理员父节点");
    	    	    return false;
    	    	}
    	    }
    	}

    	return true;
    }

	    /**
	     * 检查企业与企业管理员之间的关系
	     *
	     * @param model
	     * @param enterprise
	     * @param EMPhone
	     * @return
	     */
    private boolean checkEM2Ent(Enterprise enterprise, String eMPhone, String eMName) {

    	//判断用户是否已经有managerId
    	Administer admin = administerService.selectByMobilePhone(eMPhone);
    	if (admin == null) {
    	    return true;
    	} else {
    		//企业ID已存在，说明是编辑
    	    if (enterprise.getId() != null) {
	                //如果企业管理员的用户已经是该企业的企业管理员对应的用户，就不报错
    	    	List<Long> adminIds = adminManagerService.getAdminIdForEnter(enterprise.getId());
    	    	if (adminIds != null && adminIds.size() > 0 && adminIds.contains(admin.getId())) {
    	    	    return true;
    	    	}
    	    }

    	    Manager manager = managerService.selectByAdminId(admin.getId());
    	    if (manager != null && !manager.getId().equals(-1L)) {
    	    	return false;
    	    }
    	}

    	return true;
    }
	
	 //校验开户请求参数
    private boolean validate(EntAccountReq req) {
    	EntInfoReqData reqData;

        return req != null
            && (reqData = req.getEntInfoReqData()) != null
            && StringUtils.isNotBlank(reqData.getName())
            && StringUtils.isNotBlank(reqData.getCode())
            && StringUtils.isNotBlank(reqData.getEntName())
            && StringUtils.isNotBlank(reqData.getEmail())
            && StringUtils.isNotBlank(reqData.getPhone())
            && StringUtils.isNotBlank(reqData.getEntManageName())
            && StringUtils.isNotBlank(reqData.getEntManagePhone())
            && StringUtils.isNotBlank(reqData.getCmPhone())
            && StringUtils.isNotBlank(reqData.getCmEmail())
            && StringUtils.isNotBlank(reqData.getCustomerType())
            && StringUtils.isNotBlank(reqData.getFlowType())
            && StringUtils.isNotBlank(reqData.getProType())           
            && StringUtils.isNotBlank(reqData.getPayType())
            && StringUtils.isNotBlank(reqData.getDiscount())
            && StringUtils.isNotBlank(reqData.getGiveMoney())
        	&& StringUtils.isNotBlank(reqData.getStartTime())
        	&& StringUtils.isNotBlank(reqData.getEndTime())
        	&& StringUtils.isNotBlank(reqData.getLicenceStartTime())
        	&& StringUtils.isNotBlank(reqData.getLicenceEndTime())
        	&& StringUtils.isNotBlank(reqData.getLicenceImageName())
        	&& StringUtils.isNotBlank(reqData.getLicenceImageKey())
        	&& StringUtils.isNotBlank(reqData.getAuthorizationName())
        	&& StringUtils.isNotBlank(reqData.getAuthorizationKey())
        	&& StringUtils.isNotBlank(reqData.getIdentificationName())
        	&& StringUtils.isNotBlank(reqData.getIdentificationKey())
        	&& StringUtils.isNotBlank(reqData.getIdentificationBackName())
        	&& StringUtils.isNotBlank(reqData.getIdentificationBackKey())
        	&& StringUtils.isNotBlank(reqData.getCustomerFileName())
        	&& StringUtils.isNotBlank(reqData.getCustomerFileKey())
        	&& StringUtils.isNotBlank(reqData.getContractName())
        	&& StringUtils.isNotBlank(reqData.getContractKey())
        	&& StringUtils.isNotBlank(reqData.getApproveImageName())
        	&& StringUtils.isNotBlank(reqData.getApproveImageKey());
    }
    
    private boolean validate1(EntInfoReqData reqData) {
    	return Pattern.compile(MOBILE_REGEX).matcher(reqData.getPhone()).matches()
            && Pattern.compile(MOBILE_REGEX).matcher(reqData.getCmPhone()).matches()
           	&& Pattern.compile(MOBILE_REGEX).matcher(reqData.getEntManagePhone()).matches()
           	&& Pattern.compile(EMAIL_REGEX).matcher(reqData.getEmail()).matches()
           	&& Pattern.compile(EMAIL_REGEX).matcher(reqData.getCmEmail()).matches()
           	&& reqData.getEmail().length() <= 100
           	&& reqData.getCmEmail().length() <= 100
           	&& reqData.getStartTime().length() <= 100
           	&& reqData.getEndTime().length() <= 100
           	&& reqData.getLicenceStartTime().length() <= 100
           	&& reqData.getLicenceEndTime().length() <= 100
           	&& reqData.getCode().length() <= 64
           	&& reqData.getName().length() <= 255
           	&& reqData.getEntManageName().length() <= 64
           	&& reqData.getEntName().length() <= 255
           	&& reqData.getCustomerType().length() <= 16
           	&& reqData.getFlowType().length() <= 255
           	&& reqData.getProType().length() <= 255
           	&& reqData.getPayType().length() <= 16
           	&& reqData.getDiscount().length() <= 16
           	&& reqData.getGiveMoney().length() <= 16
           	&& reqData.getLicenceImageName().length() <= 255
           	&& reqData.getLicenceImageKey().length() <= 255
           	&& reqData.getAuthorizationName().length() <= 255
           	&& reqData.getAuthorizationKey().length() <= 255
           	&& reqData.getIdentificationName().length() <= 255
            && reqData.getIdentificationKey().length() <= 255
            && reqData.getIdentificationBackName().length() <= 255
            && reqData.getIdentificationBackKey().length() <= 255
            && reqData.getCustomerFileName().length() <= 255
            && reqData.getCustomerFileKey().length() <= 255
            && reqData.getContractName().length() <= 255
            && reqData.getContractKey().length() <= 255
            && reqData.getApproveImageName().length() <= 255
            && reqData.getApproveImageKey().length() <= 255;
    }
    
    
    @Transactional
    private boolean saveQualificationCooperation(Enterprise enterprise, EnterpriseFile enterpriseFile) {
        enterpriseFile.setEntId(enterprise.getId());
        enterpriseFile.setCreateTime(new Date());
        enterpriseFile.setUpdateTime(new Date());

        // 插入文件数据库
        // 企业表更新合同时间
        if (enterpriseFileService.selectByEntId(enterprise.getId()) != null) {
            if (enterpriseFileService.update(enterpriseFile)
                && enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                logger.info("1更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        } else {
            if (enterpriseFileService.insert(enterpriseFile) > 0
                && enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                return true;
            } else {
                logger.info("2更新数据库失败");
                throw new RuntimeException("更新数据库失败");
            }
        }

    }

}
