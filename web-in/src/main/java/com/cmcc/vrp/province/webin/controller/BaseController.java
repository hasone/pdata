package com.cmcc.vrp.province.webin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Masking;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MaskingService;
import com.cmcc.vrp.province.service.MobileBlackListService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.service.impl.EntManagerServiceImpl;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;

/**
 * BaseController.java
 */
public abstract class BaseController {
    private static Logger logger = Logger.getLogger(BaseController.class);
    public final String SESSION_SC_REDPACKET_MOBILE = "scRedpacketMobile";
    //活动标识
    private final String ACTIVE_ID = "gdzc_";
    public final String SESSION_KEY = ACTIVE_ID + "wxOpenId";
    public final String SESSION_MOBILE_KEY = ACTIVE_ID + "mobile";
    protected String zyProvinceFlagValue = "ziying";
    @Autowired
    protected AdminRoleService adminRoleService;
    @Autowired
    protected EntManagerServiceImpl entManagerService;
    @Autowired
    private AdministerService administerService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ProductService productService;

    @Autowired
    private VirtualProductService virtualProductService;

    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    MobileBlackListService mobileBlackListService;
    @Autowired
    MaskingService maskingService;
    @Autowired
    WxAdministerService wxAdministerService;
    @Autowired
    RedisUtilService redisUtilService;

    //活动标识
    public final String SESSION_ADMIN_ID = ACTIVE_ID + "adminId";

    /**
     * SESSION_YQX_MOBILE
     * 云企信订购的用户手机号码在session中的标识
     */
    public final String SESSION_YQX_MOBILE = "yqxMobile";

    /**
     * SESSION_YQX_APP
     * 云企信应用标识在session中的标识
     */
    public final String SESSION_YQX_APP = "yqxApp";

    /**
     * @Title:getSession
     * @Description: 返回当前会话
     * @author: sunyiwei
     */
    public HttpSession getSession() {
        HttpSession session = null;
        HttpServletRequest request = getRequest();
        if(request != null){
            try {
                session = request.getSession();
            } catch (Exception e) {
                logger.error("获取session异常", e);
            }
        }
          
        return session;
    }

    /**
     * @Title:getRequest
     * @Description: 返回请求
     * @author: sunyiwei
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }
    
    /**
     * getWxCurrentUser
     * 得到当前微信用户对象
     * @author qinqinyan
     * @date 2017/09/19
     **/
    public WxAdminister getWxCurrentUser() {
        Long wxAdministerId = null;
        HttpSession httpSession = getSession();
        
        if(httpSession != null){
            wxAdministerId = (Long) httpSession.getAttribute("currentUserId");
        }
        
        if (wxAdministerId == null) {
            return null;
        }
        return wxAdministerService.selectWxAdministerById(wxAdministerId);
    }

    /**
     * @Title:getCurrentUser
     * @Description: 得到当前登录的用户对象
     * @author: sunyiwei
     */
    public Administer getCurrentUser() {
        Long administerId = null;
        HttpSession httpSession = getSession();
        
        if(httpSession != null){
            administerId = (Long) httpSession.getAttribute("currentUserId");
        }
        
        if (administerId == null) {
            return null;
        }

        return administerService.selectAdministerById(administerId);
    }

    /**
     * 获取当前登录用户ID
     */
    public Long getCurrentAdminID() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }
        return (Long) httpSession.getAttribute("currentUserId");
    }

    /**
     * @Title:getAuthorities
     * @Description: 得到当前会话的登录用户的权限项
     * @author: sunyiwei
     */
    public List<String> getAuthorities() {
        HttpSession httpSession = getSession();
        Administer administer = getCurrentUser();
        if (administer == null || httpSession == null) {
            return null;
        }

        return (List<String>) httpSession.getAttribute("authNames");
    }

    /**
     * 判断角色是否为客户经理
     */
    protected boolean isCustomManager() {
        Administer admin = getCurrentUser();
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId().longValue());
        adminRole.setRoleId(Long.parseLong(getCustomManager()));
        if (adminRoleService.selectCountByQuery(adminRole) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断角色是否为企业关键人
     */
    protected boolean isEnterpriseContactor() {
        Administer admin = getCurrentUser();
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId().longValue());
        adminRole.setRoleId(Long.parseLong(getENTERPRISE_CONTACTOR()));
        if (adminRoleService.selectCountByQuery(adminRole) > 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 是否市级管理员
     */
    protected boolean isCityManager(){
        Manager manager =getCurrentUserManager();
        if(manager != null && manager.getRoleId() != null){
            if(manager.getRoleId().toString().equals(getCityManager())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 是否省级管理员
     */
    protected boolean isProvinceManager(){
        Manager manager =getCurrentUserManager();
        if(manager != null && manager.getRoleId() != null){
            if(manager.getRoleId().toString().equals(getProvinceManager())){
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * 是否超级管理员
     */
    protected boolean isSuperAdmin(){
        Manager manager =getCurrentUserManager();
        if(manager != null && manager.getRoleId() != null){
            if(manager.getRoleId().toString().equals(getSuperAdminRoleId())){
                return true;
            }
        }
        return false;
    }
    
    

    protected boolean isRole(Long roleId) {
        if (roleId == null) {
            return false;
        }

        Administer admin = getCurrentUser();
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId().longValue());
        adminRole.setRoleId(roleId);
        if (adminRoleService.selectCountByQuery(adminRole) > 0) {
            return true;
        }
        return false;
    }

    /**
     * @Title:setQueryParameter
     * @Description: 获取并设置查询参数
     * @author: sunyiwei
     */
    public void setQueryParameter(String name, QueryObject queryObject) {
        HttpSession httpSession = getSession();
        
        String ps = getRequest().getParameter("pageSize");
        if (StringUtils.isEmpty(ps) && httpSession!=null) {
            int pageSize = NumberUtils.toInt(String.valueOf(httpSession.getAttribute("pageSize")), 10);
            queryObject.setPageSize(pageSize);
        }
        
        setQueryParameter(name, queryObject, null);
    }

    /**
     * @Title:setQueryParameter
     * @Description: 获取并设置参数
     * @author: sunyiwei
     */
    public void setQueryParameter(String name, QueryObject queryObject, Object defaultValue) {
        if (getRequest() == null) {
            return;
        }
        
        //如果是返回操作，尝试从redis中取搜索条件
        String redisPageSizeKey = "SEARCH_PageSize_" + getCurrentAdminID() + getRequest().getRequestURI();//PageSize
        String redisPageNumKey = "SEARCH_PageNum_" + getCurrentAdminID() + getRequest().getRequestURI();//PageNum
        String redisParaKey = "SEARCH_" + name + "_" + getCurrentAdminID() + getRequest().getRequestURL();//搜索条件
        if(queryObject.isBack()){
            //设置PageSize            
            String redisPageSizeValue = redisUtilService.get(redisPageSizeKey);
            if (org.apache.commons.lang.StringUtils.isNotBlank(redisPageSizeValue)) {
                queryObject.setPageSize(Integer.valueOf(redisPageSizeValue));
            }

            //设置PageNum            
            String redisPageNumValue = redisUtilService.get(redisPageNumKey);
            if (org.apache.commons.lang.StringUtils.isNotBlank(redisPageNumValue)) {
                queryObject.setPageNum(Integer.valueOf(redisPageNumValue));
            }

            //设置搜索框           
            String redisParaValue = redisUtilService.get(redisParaKey);
            if (org.apache.commons.lang.StringUtils.isNotBlank(redisParaValue)) {
                queryObject.getQueryCriterias().put(name, redisParaValue);
                return;
            }         
        }else {

            //设置PageSize
            redisUtilService.set(redisPageSizeKey, String.valueOf(queryObject.getPageSize()), null);
            
            //设置PageNum
            redisUtilService.set(redisPageNumKey, String.valueOf(queryObject.getPageNum()), null);
            
            //设置搜索条件
            String value = StringUtils.trimAllString(getRequest().getParameter(name));
            String newValue = transferQuery(value);
            if (!StringUtils.isEmpty(newValue)) {
                queryObject.getQueryCriterias().put(name, newValue);                
                //将搜索条件放入redis中
                redisUtilService.set(redisParaKey, newValue, null);
            } else {              
                //没有缓存，设置默认值
                if (defaultValue != null) {
                    queryObject.getQueryCriterias().put(name, defaultValue);                    
                }
                
                //清空搜索条件
                redisUtilService.del(redisParaKey);
            }
        }
              
       
    }

    protected String getLoginAddress() {
        if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sc_jizhong")) {
            return "redirect:/manage/portal/personallogin.html";
        }
        return "redirect:/manage/user/login.html";
    }

    /**
     * @Title:deleteBlank
     * @Description: 去除字符串的中间的空格
     * @author: xuwanlin
     */
    public String deleteBlank(String str) {
        char[] chars = str.toCharArray();
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (!(chars[i] == ' ')) {
                strBuffer.append(str.substring(i, i + 1));
            }
        }

        return strBuffer.toString();
    }

    protected boolean hasAuthority(Long entId) {

        if (entId == null) {
            return false;
        }

        Long requiredManagerId = entManagerService.getManagerIdForEnter(entId);
        if (requiredManagerId == null) {
            return false;
        }

        return isParentOf(requiredManagerId);
    }

    //判断当前用户是否为要求用户的父节点
    protected boolean isParentOf(Long requiredManagerId) {
        Long currentManageId = getCurrentUserManager().getId();
        if (currentManageId.equals(requiredManagerId)) { //当前用户等于要求的用户，直接返回有权限！
            return true;
        }

        //当前用户不是要求的用户，则看看当前用户是否是它的祖先节点
        String childrenStr = managerService.getChildNodeString(currentManageId);
        if (org.apache.commons.lang.StringUtils.isBlank(childrenStr)) {
            return false;
        }

        String[] children = childrenStr.split(",");
        for (String child : children) {
            if (child.equals(String.valueOf(requiredManagerId))) {
                return true;
            }
        }

        //啥都没满足，再见！
        return false;
    }

    /**
     * @Title:transferQuery
     * @Description: 在模糊查询时，对输入的内容中含有：%和_ 进行转义
     * @author: xuwanlin
     */
    public String transferQuery(String query) {
        if (org.apache.commons.lang.StringUtils.isBlank(query)) {
            return null;
        }
        char[] chars = query.toCharArray();
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '%' || chars[i] == '_') {
                strBuffer.append("\\" + chars[i]);
            } else {
                strBuffer.append(chars[i]);
            }
        }

        return strBuffer.toString();
    }

    public String getCustomManager() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
    }

    public String getENTERPRISE_CONTACTOR() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
    }

    //制卡专员
    public String getCardMaker() {
        return globalConfigService.get(GlobalConfigKeyEnum.CARD_MAKER_ROLE_ID.getKey());
    }

    public String getSuperAdminRoleId() {
        return globalConfigService.get(GlobalConfigKeyEnum.SUPER_ADMIN_ROLE_ID.getKey());
    }

    public String getFLOWCARD_ADMIN() {
        return globalConfigService.get(GlobalConfigKeyEnum.FLOW_CARD_MANAGER_ROLE_ID.getKey());
    }

    public String getProvinceManager() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_MANAGER_ROLE_ID.getKey());
    }

    public String getCityManager() {
        return globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
    }

    public String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    //是否是自营平台
    public Boolean isZiying() {
        if ("ziying".equals(getProvinceFlag())) {
            return true;
        }
        return false;
    }

    //是否使用产品模板
    public Boolean getUseProductTemplate() {
        String result = globalConfigService.get(GlobalConfigKeyEnum.USE_PRODUCT_TEMPLATE.getKey());
        if (StringUtils.isEmpty(result) || !"YES".equals(result)) {
            return false;
        }
        return true;
    }

    public Boolean getIsCrowdfundingPlatform() {
        if ("YES".equals(globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey()))) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前用户的管理员角色
     *
     * @date 2016年7月15日
     * @author wujiamin
     */
    public Manager getCurrentUserManager() {
        HttpSession httpSession = getSession();
        Long administerId = null;
        
        if(httpSession != null){
            administerId = (Long) httpSession.getAttribute("currentUserId");
        }
        
        if (administerId == null) {
            return null;
        }

        return managerService.selectByAdminId(administerId);
    }

    /**
     * 获取标签key
     */
    public String getLocalLabel() {
        return "";
    }

    /**
     * 初始化平台产品
     */
    private Product initProduct(Product supProduct, String size) {
        Integer type = supProduct.getType();
        Product product = new Product();
        product.setFlowAccountFlag(com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode());//虚拟产品
        product.setType(type);
        product.setPrice(supProduct.getPrice() * Integer.parseInt(size));
        product.setName(definePlatPrdName(supProduct, size));
        product.setIsp(supProduct.getIsp());
        product.setOwnershipRegion(supProduct.getOwnershipRegion());
        product.setRoamingRegion(supProduct.getRoamingRegion());
        product.setProductCode(definePlatPrdCode(supProduct, size));
        product.setStatus(ProductStatus.NORMAL.getCode());
        product.setProductSize(Long.parseLong(size));
        product.setCreateTime(new Date());
        product.setDeleteFlag(com.cmcc.vrp.util.Constants.DELETE_FLAG.UNDELETED.getValue());
        product.setFlowAccountProductId(supProduct.getId());
        product.setConfigurableNumFlag(com.cmcc.vrp.util.Constants.CONFIGURABLE_NUM.YES.getValue());

        return product;
    }

    private String definePlatPrdName(Product p, String size) {
        Integer type = p.getType();
        if (type == com.cmcc.vrp.ec.bean.Constants.ProductType.MOBILE_FEE.getValue()) {
            double price = (Integer.parseInt(size)) / 100.00;
            return p.getName() + "-" + String.valueOf(price) + "元";
        } else if (type == com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_ACCOUNT.getValue()) {
            return p.getName() + "-" + SizeUnits.KB.toMB(Long.valueOf(size)) + "M";
        }
        return "undefine";
    }

    private String definePlatPrdCode(Product p, String size) {
        return p.getProductCode() + "-" + size;
    }

    /**
     * <p> 营销活动：产品中如果是流量池产品，需要进行产品转换
     *
     * @Title: flowAccountConvertPrize
     */
    public List<ActivityPrize> flowAccountConvertPrize(String activityPrizesParams) throws ProductInitException {
        List<ActivityPrize> activityPrizeList = JSON.parseArray(activityPrizesParams, ActivityPrize.class);
        List<ActivityPrize> prizes = new ArrayList<ActivityPrize>(); //转化后的奖品列表
        if (activityPrizeList != null && activityPrizeList.size() > 0) {
            for (ActivityPrize prize : activityPrizeList) {
                Product p = productService.selectProductById(prize.getProductId());
                //转换概率为小数
                Double probability = Double.parseDouble(prize.getProbability()) / 100.0;
                prize.setProbability(probability.toString());
                if (p != null) {
                    if (p.getType().intValue() == (int) com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_ACCOUNT
                            .getValue()) {
                        //                    if (Constants.FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().equals(p.getFlowAccountFlag())) {
                        //流量池
                        //Integer sizeMB = new Long(SizeUnits.KB.toMB(prize.getProductSize())).intValue();
                        /**
                         * fix by qinqinyan on 2017/3/21
                         * step1:比较 产品p(productSize)的大小 与 prize的奖品(productSize)大小是否一样，一样就是step2，否则step3
                         * step2:如果一样，说明未修改该奖品，结束，重新开始循环
                         * step3:如果不一样，根据p的flow_account_flag的属性判断是虚拟产品还是真实产品，是虚拟就step4，否则step7
                         * step4：是虚拟产品，就根据flow_account_product_id和prize的productSize查找是否有相应虚拟产品newP，有step5，否则step6
                         * step5：将newP与prize绑定，结束，重新开始循环
                         * step6：创建新的虚拟平台产品createP，将createP与prize绑定，结束，重新开始循环
                         * step7：将p的id作为flow_account_product_id和prize的productSize查找是否有相应虚拟产品newP，有step5，否则step6
                         * */
                        //step1
                        if (!p.getProductSize().toString().equals(prize.getProductSize().toString())) {
                            Product newProduct = virtualProductService.initProcess(p.getFlowAccountProductId(), prize
                                    .getProductSize().toString());
                            prize.setProductId(newProduct.getId());

                            prizes.add(prize);
                        } else {
                            prizes.add(prize);
                        }
                    } else {
                        prizes.add(prize);
                    }
                } else {
                    prizes.add(prize);
                }
            }
        }
        return prizes;
    }

    //是否需要扩展的企业信息
    protected boolean needExtEntInfo() {
        return "YES".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.EXT_ENTERPRISE_INFO.getKey()));
    }

    //是否需要审批流程的短信通知
    protected boolean needApprovalNotice() {

        String approvalNoticeFlag = globalConfigService.get(GlobalConfigKeyEnum.APPROVAL_NOTICE.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(approvalNoticeFlag) ? "false"
                : approvalNoticeFlag;
        return Boolean.parseBoolean(finalFlag);
    }

    /**
     * 获取用户openId
     *
     * @author qinqinyan
     */
    public String getWxOpenid() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }
        String wxOpenid = (String) httpSession.getAttribute(SESSION_KEY);
        return wxOpenid;
    }

    /**
     * 得到当前用户的mobile
     *
     * @author qinqinyan
     */
    public String getMobile() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }
        String mobile = (String) httpSession.getAttribute(SESSION_MOBILE_KEY);
        return mobile;
    }

    /**
     * 将当前登录用户的mobile放到session中
     *
     * @author qinqinyan
     */
    public boolean setMobile(String mobile, String openid) {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            logger.info("getSession() return null.");
            return false;
        }
        
        logger.info("mobile = " + mobile + " ; openid = " + openid);
        if (!StringUtils.isValidMobile(mobile)) {
            return false;
        }
        logger.info("set mobile " + mobile + " in session");
        httpSession.setAttribute(SESSION_MOBILE_KEY, mobile);
        //1、判断用户是否已经存在
        Administer admin = administerService.selectByMobilePhone(mobile);
        //2、插入用户
        if (admin == null) {
            logger.info("根据 mobile = " + mobile + "未查找到用户信息");
            if (!administerService.insertForWx(mobile, openid)) {
                logger.info("插入用户失败，mobile=" + mobile + ", openid=" + openid);
                return false;
            }
            //之前没有用户，需要重新获取用户
            admin = administerService.selectByMobilePhone(mobile);
        } else {
            //已存在的用户要检查是否存在个人账户
            if (!individualAccountService.checkAndInsertAccountForWx(admin.getId(), openid)) {
                logger.info("用户检查并创建个人账户失败adminId=" + admin.getId() + ",openid=" + openid + "失败");
                return false;
            }
        }
        httpSession.setAttribute("currentUserId", admin.getId());//将adminId放到session中
        return true;
    }

    /**
     * 将当前登录用户的openid放到session中
     */
    public boolean setWxOpenid(String wxOpenid) {
        HttpSession httpSession = getSession();
        
        if (!StringUtils.isValidOpenId(wxOpenid) || httpSession == null) {
            return false;
        }
        httpSession.setAttribute(SESSION_KEY, wxOpenid);
        return true;
    }

    /** 
    protected boolean allowAllPlatformProduct() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.ALL_PLATFORM_PRODUCT.getKey());
        return org.apache.commons.lang.StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value);
    }

    /**
     * 向session中存储四川流量红包登录用户的手机号码
     *
     * @Title: setScRedpacketMobile
     */
    public boolean setScRedpacketMobile(String mobile) {
        HttpSession httpSession = getSession();
        if (!StringUtils.isValidMobile(mobile) || httpSession == null) {
            return false;
        }
        getSession().setAttribute(SESSION_SC_REDPACKET_MOBILE, mobile);
        return true;
    }

    /**
     * 从session中获取四川流量红包登录用户的手机号码
     *
     * @Title: getScRedpacketMobile
     */
    public String getScRedpacketMobile() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }
        String mobile = (String) httpSession.getAttribute(SESSION_SC_REDPACKET_MOBILE);
        return mobile;
    }

    /**
     * 
     * @Title: getEffectType 
     * @Description: TODO
     * @return
     * @return: boolean
     */
    public boolean getEffectType() {
        String effectType = globalConfigService.get(GlobalConfigKeyEnum.EFFECT_TYPE.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(effectType) ? "false" : effectType;
        return Boolean.parseBoolean(finalFlag);
    }

    /**
     * 
     * @Title: allowAllPlatformProduct 
     * @Description: TODO
     * @return
     * @return: boolean
     */
    protected boolean allowAllPlatformProduct() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.ALL_PLATFORM_PRODUCT.getKey());
        return org.apache.commons.lang.StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value);
    }

    /**
     * 
     * @Title: setCqFlag 
     * @Description: TODO
     * @param map
     * @return: void
     */
    protected void setCqFlag(ModelMap map) {
        if ("chongqing".equals(getProvinceFlag()) || "xj".equals(getProvinceFlag())) { //新疆也不需要显示企业余额
            map.addAttribute("cqFlag", 1);
        } else {
            map.addAttribute("cqFlag", 0);
        }
    }

    /**
     * 
     * @Title: getPhoneRegion 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getPhoneRegion() {
        return globalConfigService.get(GlobalConfigKeyEnum.PHONE_REGION.getKey());
    }

    /**
     * @return
     */
    public boolean needCheckPhoneRegion() {

        String needCheckPhoneRegion = globalConfigService.get(GlobalConfigKeyEnum.NEED_CHECK_PHONE_REGION.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(needCheckPhoneRegion) ? "false"
                : needCheckPhoneRegion;
        return Boolean.parseBoolean(finalFlag);
    }

    /**
     * @return
     */
    public String loginType() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOGIN_TYPE.getKey());
        //        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(multCheckLogin) ? "false" : multCheckLogin;
        //        return Boolean.parseBoolean(finalFlag);
    }

    /**
     * 向session中存储云企信用户的手机号码
     *
     * @Title: setYqxMobile
     */
    public boolean setYqxMobile(String mobile) {
        HttpSession httpSession = getSession();
        if (!StringUtils.isValidMobile(mobile) || httpSession == null) {
            return false;
        }
        httpSession.setAttribute(SESSION_YQX_MOBILE, mobile);
        logger.info("放到session中数据YqxMobile,key=" + SESSION_YQX_MOBILE + ",mobile=" + mobile);
        return true;
    }

    /**
     * 从session中获取云企信用户的手机号码
     *
     * @Title: getYqxMobile
     */
    public String getYqxMobile() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }

        String mobile = (String) getSession().getAttribute(SESSION_YQX_MOBILE);
        logger.info("从session中获取数据YqxMobile,key=" + SESSION_YQX_MOBILE + ",mobile=" + mobile);
        return mobile;
    }

    /**
     * 向session中存储云企信应用标识
     *
     * @Title: setYqxAppTag
     */
    public void setYqxAppTag(String mobile) {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return;
        }
        
        httpSession.setAttribute(SESSION_YQX_APP, mobile);
        logger.info("放到session中数据AppTag,key=" + SESSION_YQX_APP + ",appTag=" + mobile);
    }

    /**
     * 从session中获取云企信应用标识
     *
     * @Title: getYqxAppTag
     */
    public String getYqxAppTag() {
        HttpSession httpSession = getSession();
        if(httpSession == null){
            return null;
        }
        
        String appTag = (String) httpSession.getAttribute(SESSION_YQX_APP);
        logger.info("从session中获取数据AppTag,key=" + SESSION_YQX_APP + ",appTag=" + appTag);
        return appTag;
    }

    /**
     * 
     * @Title: isSmsBlack 
     * @Description: TODO
     * @param mobile
     * @param type
     * @return
     * @return: boolean
     */
    public boolean isSmsBlack(String mobile, String type) {
        if (mobileBlackListService.getByMobileAndType(mobile, type) != null) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @Title: needECApprovalNotice 
     * @Description: 是否需要审批流程的短信通知
     * @return
     * @return: boolean
     */
    protected boolean needECApprovalNotice() {

        String approvalNoticeFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_APPROVAL_NOTICE.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(approvalNoticeFlag) ? "false"
                : approvalNoticeFlag;
        return Boolean.parseBoolean(finalFlag);
    }

    /**
     * @return
     */
    public boolean needCrowdfundingMask() {
        Administer administer = getCurrentUser();
        Masking masking = maskingService.getByAdminId(administer.getId());
        if (masking != null && masking.getCrowdfundingMask() == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    public boolean needMdrcMask() {
        Administer administer = getCurrentUser();
        Masking masking = maskingService.getByAdminId(administer.getId());
        if (masking != null && masking.getMdrcMask() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @Title: hasAuthority 
     * @Description: TODO
     * @param authorityName
     * @return
     * @return: boolean
     */
    public boolean hasAuthority(String authorityName) {
        List<String> authList = getAuthorities();
        if (authList != null && authList.size() > 0) {
            for (String authName : authList) {
                if (authName.equalsIgnoreCase(authorityName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public String getProductSizeLimit() {
        return globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_FLOW_PRODUCT.getKey());
    }
}
