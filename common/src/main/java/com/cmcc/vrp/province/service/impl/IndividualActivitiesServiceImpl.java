package com.cmcc.vrp.province.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketReq;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketResp;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualActivitySerialNum;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.module.ScUserInfoReq;
import com.cmcc.vrp.province.module.ScUserInfoReqData;
import com.cmcc.vrp.province.module.ScUserInfoResp;
import com.cmcc.vrp.province.module.ScUserInfoRespData;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualActivitiesService;
import com.cmcc.vrp.province.service.IndividualActivityOrderService;
import com.cmcc.vrp.province.service.IndividualActivitySerialNumService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.StringUtils;
import com.har.util.AESUtil;
import com.har.util.code.B2BRSAUtil;

/**
 * IndividualActivitiesServiceImpl.java
 * @author wujiamin
 * @date 2017年1月12日
 */
@Service("individualActivitiesService")
public class IndividualActivitiesServiceImpl implements IndividualActivitiesService{
    private static final Logger logger = LoggerFactory.getLogger(ActivitiesServiceImpl.class);
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    IndividualProductMapService individualProductMapService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    AdministerService administerService;
    @Autowired
    ActivityTemplateService activityTemplateService;
    @Autowired
    IndividualActivitySerialNumService individualActivitySerialNumService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    IndividualActivityOrderService individualActivityOrderService;

    @Override
    public IndividualRedpacketResp generateFlowRedpacket(IndividualRedpacketReq req)  {
        IndividualRedpacketActivityParam param = req.getParam();
        Activities activities = null;
       
        try {
            activities = initActivities(param.getMobile(),param.getActivityName(),
                    param.getStartTime(),param.getType(),param.getCount());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if(activities == null){
            return null;
        }
        
        ActivityPrize activityPrize = initFlowRedPackagePrize(activities, param.getSize(), param.getCount());
        ActivityTemplate template = initActivityTemplate(activities, param.getObject(), param.getRules());
        ActivityInfo activityInfo = initActivityInfo(activities, activityPrize);
        IndividualActivitySerialNum serialNum = new IndividualActivitySerialNum();
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        serialNum.setEcSerialNum(req.getEcSerialNumber());
        serialNum.setSystemNum(activities.getActivityId());
        serialNum.setUpdateTime(new Date());
        
        //校验账户余额
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(activities.getCreatorId(), 
                individualProductService.getDefaultFlowProduct().getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(activityInfo.getTotalProductSize() > account.getCount().intValue()){
            logger.info("账户余额不足，accountId={},count={},活动需要{}", account.getId(), account.getCount(), activityInfo.getTotalProductSize());
            return null;
        }
        /**
         * 1、插入红包活动信息（活动信息、创建活动账户） 2、创建活动定时任务 3、扣减账户+冻结活动账户 4、营销模板生成活动
         * */
        if (insertFlowRedpacket(activities, activityPrize, activityInfo, template)
              &&individualActivitySerialNumService.insertSelective(serialNum)
              &&activitiesService.createActivitySchedule(activities.getActivityId())
              &&frozenActivityAccount(activities.getActivityId())) {
            // 向营销模板发送活动请求
            AutoResponsePojo result = activitiesService.sendToGenerateActivity(activities.getActivityId());
            
            IndividualRedpacketResp resp = new IndividualRedpacketResp();
            if (result!=null && result.getCode() == 200) {                    
                activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
                if(activityInfo!=null && activityInfo.getUrl()!=null){
                    resp.setActivityId(activities.getActivityId());
                    resp.setActivityUrl(activityInfo.getUrl());
                    return resp;
                }
            }
            
            //失败要回退金额
            logger.info("个人红包活动创建失败，开始将剩余流量退回个人流量账户，活动ID-" + activities.getActivityId());
            if (!individualAccountService.giveBackFlow(activities.getActivityId())) {
                logger.info("个人红包活动剩余流量退回个人流量账户操作失败，活动ID-" + activities.getActivityId());
            }

        }
       
        return null;
    }
    
    @Override
    public Map generateFlowRedpacketForPage(IndividualRedpacketActivityParam param)  {
        Map resultMap = new HashMap();
        Activities activities = null;
        try {
            activities = initActivities(param.getMobile(),param.getActivityName(),
                    param.getStartTime(),param.getType(),param.getCount());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if(activities == null){
            return null;
        }
        
        //检查是否有已经进行中的活动
        List<Activities> actList = activitiesService.selectForOrder(activities.getCreatorId(), null, ActivityType.LUCKY_REDPACKET.getCode(),
                ActivityStatus.PROCESSING.getCode());
        if(actList !=null && actList.size() > 0){
            //检查该活动是否已经抢完
            for(Activities act : actList){
                //校验活动奖品数量和当前中奖用户数量，活动奖品总数需要大于当前中奖用户数量
                int count = activityWinRecordService.countChargeMobileByActivityId(act.getActivityId());
                ActivityInfo activityInfo = activityInfoService.selectByActivityId(act.getActivityId());
                if(activityInfo == null){
                    logger.info("activityInfo为空, activityId={}", act.getActivityId());
                    return null;
                }
                if(activityInfo.getPrizeCount()>count){
                    logger.info("存在进行中的活动，且该活动还没有发完奖品，无法创建新活动，activityId={}", act.getActivityId());
                    resultMap.put("url", "processing");
                    resultMap.put("activityId", act.getActivityId());
                    return resultMap;
                }
            }
            
        }
        
        ActivityPrize activityPrize = initFlowRedPackagePrize(activities, param.getSize(), param.getCount());
        ActivityTemplate template = initActivityTemplate(activities, param.getObject(), param.getRules());
        ActivityInfo activityInfo = initActivityInfo(activities, activityPrize);
        
        //校验账户余额（校验流量账户账户）
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(activities.getCreatorId(),
                individualProductService.getDefaultFlowProduct().getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(account == null){
            logger.info("用户默认流量账户不存在，adminId={}", activities.getCreatorId());
            return null;
        }
        
        if(activityInfo.getTotalProductSize() > account.getCount().intValue()){        
            logger.info("账户余额不足，accountId={},count={},活动需要{}", account.getId(), account.getCount(), activityInfo.getTotalProductSize());
            return null;
        }
        /**
         * 1、插入红包活动信息（活动信息、创建活动账户）， 插入红包-订购关系  2、创建活动定时任务  3、营销模板生成活动
         * */
        if (insertFlowRedpacket(activities, activityPrize, activityInfo, template)
              &&individualActivityOrderService.insert(activities.getActivityId(), account.getCurrentOrderId())
              &&activitiesService.createActivitySchedule(activities.getActivityId())) {
            // 向营销模板发送活动请求
            AutoResponsePojo result = activitiesService.sendToGenerateActivity(activities.getActivityId());

            if (result!=null && result.getCode() == 200) {                    
                activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
                if(activityInfo!=null && activityInfo.getUrl()!=null){
                    resultMap.put("url", activityInfo.getUrl());
                    resultMap.put("activityId", activities.getActivityId());
                    return resultMap;
                }
            }
            
            //失败将活动状态置为活动结束
            logger.info("个人红包活动创建失败，将数据库中活动的状态置为已结束，活动ID-" + activities.getActivityId());
            Activities failAct = new Activities();
            failAct.setActivityId(activities.getActivityId());
            failAct.setStatus(ActivityStatus.END.getCode());
            failAct.setUpdateTime(new Date());
            if(!activitiesService.updateByPrimaryKeySelective(failAct)){
                logger.info("更新失败！活动的状态置更新为已结束失败，活动ID-" + activities.getActivityId());
            }
        }
       
        return null;
    }
 
    @Override
    @Transactional
    public boolean insertFlowRedpacket(Activities activities, ActivityPrize
            activityPrize, ActivityInfo activityInfo, ActivityTemplate template) {
        if (activities == null || activityPrize == null) {
            return false;
        }
        // 插入活动信息
        // 1、插入活动记录 
        if (!activitiesService.insert(activities)) {
            logger.info("插入活动记录失败：" + JSONArray.toJSONString(activities));
            return false;
        }

        // 2、插入奖品记录
        if (!activityPrizeService.insertForRedpacket(activityPrize)) {
            throw new RuntimeException("奖品记录插入失败");
        }

        // 3、插入活动详情
        if (!activityInfoService.insertForRedpacket(activityInfo)) {
            throw new RuntimeException("活动详情插入失败");
        }
        
        // 4、插入活动模板
        if (!activityTemplateService.insertSelective(template)) {
            throw new RuntimeException("活动模板插入失败");
        }       

        return true;
    }
    
    private Activities initActivities(String mobile, String activityName, String startTime,
            Integer type, Long count) throws ParseException {
        // 检查被订购用户是否是平台用户
        Administer admin = administerService.selectByMobilePhone(mobile);
        if (admin == null || admin.getId() == null) {
            logger.info("订购流量的用户不是平台用户，创建用户");
            if (!administerService.insertForScJizhong(mobile)) {
                logger.error("订购流量的用户失败，mobile={}", mobile);
                return null;
            }
            admin = administerService.selectByMobilePhone(mobile);
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户             
            if(!individualAccountService.insertAccountForScJizhong(admin.getId())){
                logger.error("平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", mobile, admin.getId());
                return null;
            }
        }
        Long adminId = admin.getId();        

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date start =  sdf.parse(startTime);
        Date end = DateUtil.getDateAfter(start, 1);                
        Activities activities = new Activities();
        activities.setEntId(adminId);
        activities.setName(activityName);
        activities.setStartTime(start);
        activities.setEndTime(end);
        activities.setType(type);
        activities.setCreatorId(adminId);
        activities.setPrizeCount(count);
        activities.setActivityId(SerialNumGenerator.buildSerialNum());
        activities.setStatus(ActivityStatus.ON.getCode());
        activities.setDeleteFlag(0);
        activities.setCreateTime(new Date());
        activities.setUpdateTime(new Date());
        return activities;
    }
        
    private ActivityPrize initFlowRedPackagePrize(Activities activities, Long size, Long count) {
        ActivityPrize activityPrize = new ActivityPrize();
        //如果是拼手气红包，则是流量币总量
        activityPrize.setSize(size);
        activityPrize.setCount(count);

        activityPrize.setActivityId(activities.getActivityId());
        activityPrize.setEnterpriseId(activities.getEntId());

        // 查找相应产品
        IndividualProductMap individualProductMap = individualProductMapService.getByAdminIdAndProductType(activities.getCreatorId(), 
                IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue());
        activityPrize.setProductId(individualProductMap.getIndividualProductId());
        activityPrize.setPrizeName(individualProductMap.getProductName());
        return activityPrize;
    }
    
    private ActivityInfo initActivityInfo(Activities activities, ActivityPrize activityPrize) {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityPrize.getActivityId());
        activityInfo.setPrizeCount(activityPrize.getCount());
        activityInfo.setUserCount(activityPrize.getCount());

        if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            //拼手气红包
            activityInfo.setTotalProductSize(activityPrize.getSize());
        } else if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())) {
            activityInfo
                    .setTotalProductSize(activityPrize.getCount().longValue() * activityPrize.getSize().longValue());
        }

        // 产品信息
        IndividualProductMap individualProductMap = individualProductMapService.getByAdminIdAndProductId(activities.getCreatorId(), 
                activityPrize.getProductId());
        Long price = individualProductMap.getPrice().intValue() * activityPrize.getCount().longValue()
                * individualProductMap.getDiscount().longValue() / 100;
        activityInfo.setPrice(price);

        return activityInfo;
    }
    
    private ActivityTemplate initActivityTemplate(Activities activities, String object, String rules) {
        ActivityTemplate template = new ActivityTemplate();
        template.setActivityId(activities.getActivityId());
        template.setUserType(0);
        template.setDaily(0);
        template.setCheckType(1);
        String checkUrl = globalConfigService.get(GlobalConfigKeyEnum.JIZHONG_ACTIVITY_CHECK_URL.getKey());
        template.setCheckUrl(checkUrl);
        template.setFixedProbability(1);
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        template.setDeleteFlag(0); 
        template.setRules(rules);
        template.setObject(object);
        return template;
    }
    
    private IndividualAccount initIndividualAccount(Activities activities, ActivityPrize
            activityPrize, ActivityInfo activityInfo, Long sourceAccountId) {
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setAdminId(activities.getCreatorId());
        individualAccount.setOwnerId(activities.getId());
        individualAccount.setIndividualProductId(activityPrize.getProductId());
        individualAccount.setType(IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
        individualAccount.setCount(new BigDecimal(activityInfo.getTotalProductSize()));
        individualAccount.setCreateTime(new Date());
        individualAccount.setUpdateTime(new Date());
        individualAccount.setVersion(0);
        individualAccount.setDeleteFlag(0);
        return individualAccount;
    }
    
    @Transactional
    private boolean frozenActivityAccount(String activityId){
        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);
        Activities activity = activitiesService.selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
        if(activityPrizes==null || activityPrizes.size()!=1 || activity == null || activityInfo == null){
            return false;
        }
        //1、扣减个人流量账户
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(activity.getCreatorId(), 
                activityPrizes.get(0).getProductId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        
        try{
            if(!individualAccountService.changeAccount(account, new BigDecimal(activityInfo.getTotalProductSize()), activityId,
                    (int)AccountRecordType.OUTGO.getValue(),  "创建红包活动，扣减账户余额", ActivityType.COMMON_REDPACKET.getCode(), 0)){
                throw new RuntimeException("扣减流量账户失败");
            }
        }catch(Exception e){
            logger.error("changeAccount异常,"+e.getMessage());
            throw new RuntimeException("扣减流量账户失败");
        }
        //2、创建活动账户
        IndividualAccount individualAccount = initIndividualAccount(activity, activityPrizes.get(0), activityInfo, null);
        if (!individualAccountService.createAccountForActivity(activity, activityInfo, activityPrizes,
                individualAccount)) {
            throw new RuntimeException("创建活动冻结账户失败");
        }
        return true;
    }

    @Override
    public ScUserInfoRespData getScUserInfo(String tokenJson) {
        String sendData = null;
        String uid = StringUtils.randomString(20);
        try {            
            sendData = getSendData(tokenJson, uid);
            logger.info("封装单点登录请求参数sendData={}", sendData);
        } catch (Exception e) {
            logger.error("四川和生活单点登录接口封装请求参数异常，exception={}", e.getMessage());
            e.printStackTrace();
        }
        String resp = HttpConnection.doPost(getUrl(), sendData, "application/json", "utf-8", true);
        logger.info("单点登录返回的响应内容，response={}", resp);
        try {
            ScUserInfoRespData data = parseResponse(resp, uid);
            if(data!=null){
                return data;
            }
        } catch (Exception e) {
            logger.error("四川和生活单点登录接口解析响应参数失败，exception={}", e.getMessage());
        }
        
        return null;
    }
    
    private String getSendData(String tokenJson, String uid) throws Exception {    
        B2BRSAUtil b2bRSAUtil = new B2BRSAUtil();
        String publicKeyStr = getKeyString(getHshPublicKeyPath());
        b2bRSAUtil.loadPublicKeyStr(publicKeyStr); // 加载汇安融 RSA 公钥        
        String msgKey = StringUtils.randomString(16); //16 位随机字符
        
        ScUserInfoReqData data = new ScUserInfoReqData();     
        data.setTimeStamp(DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        //tokenJson解析成token
        JSONObject json = (JSONObject) JSONObject.parse(tokenJson);
        String token = json.getString("HARToken");
        data.setToken(token);
        data.setUid(uid);
        String dataJson = AESUtil.getInstance(msgKey).encrypt(JSONObject.toJSONString(data)); // AES 加密 data 数据、编码
        
        msgKey = b2bRSAUtil.encryptStr(msgKey); // 对 msgKey 加密、编码
        String privateKeyStr = getKeyString(getPrivateKeyPath());
        b2bRSAUtil.loadPrivateKeyStr(privateKeyStr); // 加载合作机构 RSA 私钥
        String sign = b2bRSAUtil.makeSign(JSONObject.toJSONString(data)); // 制作签名
        
        ScUserInfoReq request = new ScUserInfoReq();
        request.setAppId(getAppId());
        request.setSign(sign);
        request.setMsgKey(msgKey);
        request.setPartnerId(getPartnerId());
        request.setData(dataJson);
        
        return JSONObject.toJSONString(request);
    }
    
    private ScUserInfoRespData parseResponse(String resp, String uid) throws Exception{
        ScUserInfoResp response = JSON.parseObject(resp, ScUserInfoResp.class);
        if(!"00000000".equals(response.getRespCode())){//响应不成功，返回空对象
            return null;
        }else{
            B2BRSAUtil b2bRSAUtil = new B2BRSAUtil();
            String privateKeyStr = getKeyString(getPrivateKeyPath());
            b2bRSAUtil.loadPrivateKeyStr(privateKeyStr); // 加载合作机构 RSA 私钥
            
            String msgKey = response.getMsgKey();
            msgKey = b2bRSAUtil.decryptStr(msgKey);//获得解密后的密钥
            
            //获得解密后的data原文
            String dataStr = response.getData();
            String publicKeyStr = getKeyString(getHshPublicKeyPath());
            b2bRSAUtil.loadPublicKeyStr(publicKeyStr); // 加载汇安融 RSA 公钥
            //验证签名
            boolean sign = b2bRSAUtil.verifySign(dataStr, response.getSign());
            if(!sign){
                logger.info("签名验证不通过，data解密后{}，数字签名sign={}", dataStr, response.getSign());
                return null;
            }else{//数字签名校验通过
                //获得解密后的响应业务data
                dataStr = AESUtil.getInstance(msgKey).decrypt(dataStr);
                ScUserInfoRespData data = JSON.parseObject(dataStr, ScUserInfoRespData.class);
                if(!uid.equals(data.getUid())){
                    logger.info("请求时的uid与响应获取的uid不同，请求uid={}，响应uid={}", uid, data.getUid());
                    return null;
                }
                return data;
            }
        }
    
    }

    private String getPartnerId() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_HSH_PARTNERID.getKey());
    }

    private String getUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_HSH_SSO_URL.getKey());
    }
    
    private String getAppId(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_HSH_APPID.getKey());
    }
    
    private String getHshPublicKeyPath(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_HSH_PUBLIC_KEY.getKey());
    }
    
    private String getPrivateKeyPath(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_HSH_PRIVATE_KEY.getKey());
    }
    
    /** 
     * 获取密钥字符串
     * @Title: getKeyString 
     */
    private String getKeyString(String filePath){
        // 读取txt内容为字符串
        StringBuffer txtContent = new StringBuffer();
        // 每次读取的byte数
        byte[] b = new byte[8 * 1024];
        InputStream in = null;
        try{
            // 文件输入流
            in = new FileInputStream(filePath);
            while (in.read(b) != -1){
                // 字符串拼接
                txtContent.append(new String(b));
            }
            // 关闭流
            in.close();
        }catch (FileNotFoundException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if (in != null){
                try{
                    in.close();
                }catch (IOException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return txtContent.toString();
    }

}
