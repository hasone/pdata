package com.cmcc.webservice.crowdfunding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.CrowdFundingJoinType;
import com.cmcc.vrp.enums.PaymentStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.EntUserQueryService;
import com.cmcc.vrp.wx.UserCheckService;
import com.cmcc.webservice.crowdfunding.pojo.ActivityPojo;
import com.cmcc.webservice.crowdfunding.pojo.ActivityResultPojo;
import com.cmcc.webservice.crowdfunding.pojo.ActivityResultResp;
import com.cmcc.webservice.crowdfunding.pojo.CFChargePojo;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeReq;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeResultResp;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeResultRespPojo;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityResp;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityResultPojo;
import com.cmcc.webservice.crowdfunding.pojo.PaymentPojo;
import com.cmcc.webservice.crowdfunding.pojo.PaymentReq;
import com.cmcc.webservice.crowdfunding.pojo.PrizePojo;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResPojo;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReqData;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityResp;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReqData;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResResp;

/**
 * 众筹接口服务（非回调部分）
 * CrowdfundingInterfaceServiceImpl.java
 * @author wujiamin
 * @date 2017年2月8日
 */
@Service
public class CrowdfundingInterfaceServiceImpl implements CrowdfundingInterfaceService{
    private static final Logger LOGGER = LoggerFactory.getLogger(CrowdfundingInterfaceServiceImpl.class);    
    
    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    ActivityPrizeService activityPrizeService;
    
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    CrowdFundingService crowdFundingService;
    
    @Autowired
    ActivityPaymentInfoService activityPaymentInfoService;
    
    @Autowired
    ChargeRecordService chargeRecordService; 
    
    @Autowired
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;

    @Autowired
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    
    @Autowired
    EntUserQueryService entUserQueryService;
    
    @Autowired
    UserCheckService userCheckService;
    
    /** 
     * 校验查询活动接口参数
     * @Title: validateQueryActivityRequest 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2017年2月8日
    */
    @Override
    public boolean validateQueryActivityRequest(QueryActivityReq request) {
        QueryActivityReqData data = request.getData();
        if(data==null){
            LOGGER.info("QueryActivityReqData为空");
            return false;
        }
        if(StringUtils.isEmpty(request.getRequestTime()) 
                || StringUtils.isEmpty(data.getActivityId()) 
                || StringUtils.isEmpty(data.getEnterpriseCode())
                || StringUtils.isEmpty(data.getEcProductCode())){
            LOGGER.info("requestTime, activityId或enterpriseCode或EcProductCode为空");
            return false;
        }
        Map decryptResult = activitiesService.decryptActivityId(data.getActivityId(), data.getEnterpriseCode(), data.getEcProductCode());
        //解析activityId成功
        if("success".equals(decryptResult.get("code"))){
            LOGGER.info("解析前活动ID={}, enterpriseCode={}, 解析后活动ID={}", data.getActivityId(), data.getEnterpriseCode(), (String)decryptResult.get("message"));
            data.setActivityId((String)decryptResult.get("message"));
        }else{
            LOGGER.info("activityId解析失败，解析前activityId={},enterpriseCode={}", data.getActivityId(), data.getEnterpriseCode());
            return false;
        }
        //替换参数中的activityId
        request.setData(data);
        return true;
    }

    /** 
     * 查询活动
     * @Title: queryActivity 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2017年2月8日
    */
    @Override
    public QueryActivityResp queryActivity(QueryActivityReq request) {
        QueryActivityResp resp = new QueryActivityResp();
        resp.setResponseTime(DateUtil.getRespTime());
        
        String activityId = request.getData().getActivityId();
        Activities activities = activitiesService.selectByActivityId(activityId);
        if(activities == null){
            resp.setMessage("活动不存在");
            resp.setCode("10001");
            return resp;
        }
        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(activityId);
        CrowdfundingActivityDetail crowdfundingActivityDetail = 
                crowdfundingActivityDetailService.selectByActivityId(activityId);        
        
        ActivityPojo record = new ActivityPojo();
        //activityId加密
        record.setActivityId(activitiesService.encryptActivityId(activities.getActivityId()));
        record.setActivityName(activities.getName());
        record.setStatus(activities.getStatus().toString());
        String type = "yyyy-MM-dd HH:mm:ss";
        record.setStartTime(DateUtil.dateToString(activities.getStartTime(), type));
        record.setEndTime(DateUtil.dateToString(activities.getEndTime(), type));
        record.setChargeType(crowdfundingActivityDetail.getChargeType().toString());
        record.setHasWhiteOrBlack(crowdfundingActivityDetail.getHasWhiteOrBlack().toString());
        record.setTargetCount(crowdfundingActivityDetail.getTargetCount().toString());
        record.setCurrentCount(crowdfundingActivityDetail.getCurrentCount().toString());
        record.setResult(crowdfundingActivityDetail.getRules());
        record.setRules(crowdfundingActivityDetail.getRules());
        record.setJoinType(crowdfundingActivityDetail.getJoinType().toString());
        
        List<PrizePojo> pojoList = new ArrayList<PrizePojo>();
        for(ActivityPrize item : prizes){
            PrizePojo pojo = new PrizePojo();
            pojo.setPrizeId(item.getId());
            pojo.setPrizeName(item.getPrizeName());
            Double discount = item.getDiscount()/10.0;
            pojo.setDiscount(discount.toString()+"折");
            pojo.setProductCode(item.getProductCode());
            pojo.setProductName(pojo.getProductName());
            
            pojoList.add(pojo);
        }
        
        record.setPrizes(pojoList);        
        resp.setActivityPojo(record);
        resp.setMessage("成功");
        resp.setCode("10000");
        
        return resp;
    }

    @Override
    public JoinActivityResp joinActivity(JoinActivityReq request) {
        JoinActivityResp resp = new JoinActivityResp();
        resp.setResponseTime(new DateTime().toString());
        //校验活动状态（进行中）  
        Activities activity = activitiesService.selectByActivityId(request.getJoinData().getActivityId());
        CrowdfundingActivityDetail crowdfundingActivityDetail = 
                crowdfundingActivityDetailService.selectByActivityId(request.getJoinData().getActivityId());
        if(activity==null || crowdfundingActivityDetail == null){
            LOGGER.info("参与众筹活动，活动不存在，activityId={}", request.getJoinData().getActivityId());
            resp.setMessage("活动不存在");
            resp.setCode("10001");
            return resp;
        }
        
        if(!crowdfundingActivityDetail.getJoinType().toString()
                .equals(CrowdFundingJoinType.Big_Enterprise.getCode().toString())){
            LOGGER.info("该活动不能通过企业报名方式参加。activityId={}",request.getJoinData().getActivityId());
            resp.setMessage("该活动不能通过接口报名方式参加");
            resp.setCode("10001");
            return resp;
        }
        
        if(!activity.getStatus().equals(ActivityStatus.PROCESSING.getCode()) || activity.getStartTime().after(new Date())
                ||activity.getEndTime().before(new Date())){
            LOGGER.info("参与众筹活动，活动状态异常，activityId={}, status={}, startTime, endTime={}", request.getJoinData().getActivityId(), activity.getStatus(), 
                    activity.getStartTime().toString(), activity.getEndTime().toString());
            resp.setMessage("活动状态异常");
            resp.setCode("10001");
            return resp;
        }
        
        //判断是否在用户列表中（白名单）
        if(!activityBlackAndWhiteService.
                weatherInPhoneList(request.getJoinData().getMobile(), activity.getActivityId())){
            LOGGER.info("mobile = {} 不在活动ID = {} 的用户列表中。", 
                    request.getJoinData().getMobile(), activity.getActivityId());
            resp.setMessage("手机号【"+request.getJoinData().getMobile()+"】不在该活动用户名单中");
            resp.setCode("10001");
            return resp;
        }
        
        //校验奖品ID是否正确
        boolean prizeTag = false;
        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(request.getJoinData().getActivityId());
        for(ActivityPrize prize : prizes){
            if(prize.getId().equals(request.getJoinData().getPrizeId())){
                prizeTag = true;
                break;
            }
        }
        if(!prizeTag){
            resp.setMessage("活动奖品不存在");
            resp.setCode("10001");
            return resp;
        }
        
        //校验该用户是否已经参加该众筹活动
        Map map = new HashMap();
        map.put("ownMobile", request.getJoinData().getMobile());
        map.put("activityId", request.getJoinData().getActivityId());
        List<ActivityWinRecord> activityWinRecord = activityWinRecordService.selectByMap(map);
        if(activityWinRecord!=null && activityWinRecord.size()>0){
            LOGGER.info("用户已经参加该活动,mobile={}, activityId={}", request.getJoinData().getMobile(), request.getJoinData().getActivityId());
            resp.setMessage("用户已参加该活动");
            resp.setCode("10001");
            return resp;
        }
        
        //参加众筹活动
        if(!activitiesService.joinCrowdfundingActivity(request.getJoinData().getActivityId(), request.getJoinData().getMobile(), 
                request.getJoinData().getPrizeId(), "EC")){
            LOGGER.info("参与众筹活动失败，activityId={}, mobile={}, prizeId={}", request.getJoinData().getActivityId(), request.getJoinData().getMobile(), 
                    request.getJoinData().getPrizeId());
            resp.setMessage("众筹活动报名失败");
            resp.setCode("10001");
            return resp;
        }
                
        //重新获取报名记录
        activityWinRecord = activityWinRecordService.selectByMap(map);
        if(activityWinRecord == null || activityWinRecord.size()!=1){
            LOGGER.info("筛选activityWinRecord失败！,mobile={}, activityId={}", request.getJoinData().getActivityId(), request.getJoinData().getMobile());
            resp.setMessage("众筹活动报名失败");
            resp.setCode("10001");
            return resp;
        }
        
        ActivityWinRecord record = activityWinRecord.get(0);
        //参加成功
        resp.setMessage("报名成功");
        resp.setCode("10000");
        
        JoinActivityResultPojo data = new JoinActivityResultPojo();
        data.setMobile(record.getOwnMobile());
        data.setRecordId(record.getRecordId());
        data.setPayResult(record.getPayResult().toString());
        String type = "yyyy-MM-dd HH:mm:ss";
        data.setWinTime(DateUtil.dateToString(record.getWinTime(), type));
        data.setPrizeId(record.getPrizeId());
        resp.setJoinResultData(data);        
        
        return resp;
    }

    @Override
    public boolean validateCFChargeReq(CFChargeReq req) {
        // TODO Auto-generated method stub
        if(req!=null && !StringUtils.isEmpty(req.getDateTime())){
            CFChargePojo pojo = req.getCfChargePojo();
            if(pojo!=null && !StringUtils.isEmpty(pojo.getRecordId())
                    && pojo.getRecordId().length()<=255
                    && !StringUtils.isEmpty(pojo.getSerialNum())
                    && StringUtils.isValidMobile(pojo.getMobile())
                    && pojo.getSerialNum().length()<=255){
                //&& !StringUtils.isEmpty(pojo.getEnterpriseCode())
                return true;
            }
        }
        return false;
    }
    
    
    @Override
    public Map<String, String> validateChargeRequirement(CFChargePojo pojo) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        String code = "fail";
        String msg = "发送充值请求失败";
        //String winRecordId = "";
        if(pojo!=null){
            ActivityWinRecord activityWinRecord = activityWinRecordService
                    .selectByRecordId(pojo.getRecordId());
            if(activityWinRecord!=null){
                //winRecordId = activityWinRecord.getRecordId();
                
                if(activityWinRecord.getOwnMobile().equals(pojo.getMobile())){
                    if(activityWinRecord.getPayResult().intValue()==PaymentStatus.Pay_Success.getStatus()){
                        //完成支付，是否已经存在充值记录
                        SerialNum serialNum = serialNumService.getByPltSerialNum(activityWinRecord.getRecordId());
                        if(serialNum==null && 
                                activityWinRecord.getStatus().toString().equals(ActivityWinRecordStatus.WAIT.getCode().toString())){
                            LOGGER.info("mobile = {} 的报名记录 recordId = {} 正常", 
                                    pojo.getMobile(), activityWinRecord.getRecordId());
                            code = "success";
                            msg = activityWinRecord.getRecordId();
                        }else{
                            LOGGER.info("mobile = {} 已经存在充值记录。平台ID reocrdId = {}"
                                    , pojo.getMobile(), activityWinRecord.getRecordId());
                            if(serialNum!=null && StringUtils.isEmpty(serialNum.getEcSerialNum())){
                                msg = "已经存在手机号【"+pojo.getMobile()+"】的充值请求,"
                                        + "充值流水号【"+activityWinRecord.getRecordId()+"】, EC流水号【"
                                        + serialNum.getEcSerialNum() +"】";
                            }else{
                                msg = "已经存在手机号【"+pojo.getMobile()+"】的充值请求，充值流水号【"+activityWinRecord.getRecordId()+"】";
                            }
                        }
                    }else{
                        LOGGER.info("mobile = {} 未完成支付。报名ID reocrdId = {}"
                                , pojo.getMobile(), activityWinRecord.getRecordId());
                        msg = "手机号【"+pojo.getMobile()+"】未完成订单【"+activityWinRecord.getRecordId()+"】的支付";
                    }
                }else{
                    LOGGER.info("根据报名ID reocrdId = {} 查找的mobile ={} 与 请求的mobile={}不符"
                            ,activityWinRecord.getRecordId(), activityWinRecord.getOwnMobile(), 
                            pojo.getMobile());
                    msg = "根据报名ID【"+activityWinRecord.getRecordId()+"】查找到的手"
                            + "机号【"+activityWinRecord.getOwnMobile()+"】与请求参数【"+pojo.getMobile()+"】不符合";
                }
            }else{
                LOGGER.info("未查找到 mobile = {} 的报名记录");
                msg = "未查找到手机号【"+pojo.getMobile()+"】的报名记录";
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    @Override
    @Transactional
    public boolean chargeForInterface(CFChargePojo pojo) {
        // TODO Auto-generated method stub
        /*ActivityWinRecord activityWinRecord = activityWinRecordService
                .selectByActivityIdAndMobile(pojo.getRecordId(), pojo.getEnterpriseCode());*/
        ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(pojo.getRecordId());
        if(activityWinRecord!=null){
            //插入流水号记录
            if (!serialNumService.insert(buildSerailNum(activityWinRecord.getRecordId(), pojo.getSerialNum()))){
                LOGGER.error("插入报名流水号,EC流水号出错. recordId = {}, serialNum.", 
                        activityWinRecord.getRecordId(), pojo.getSerialNum());
                return false;
            }
            //插入充值队列
            try{
                if(!crowdFundingService.insertRabbitmq(activityWinRecord.getRecordId())){
                    LOGGER.error("插入充值队列出错，recordId={}", activityWinRecord.getRecordId());
                    throw new RuntimeException();
                }
            } catch (RuntimeException e){
                LOGGER.error("插入充值队列出错，recordId={}", activityWinRecord.getRecordId());
                throw new RuntimeException();
            }
            
            return true;
        }
        return false;
    }
    
    private SerialNum buildSerailNum(String recordId, String serial) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(recordId);
        serialNum.setEcSerialNum(serial);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    @Override
    public boolean validatePaymentReq(PaymentReq req) {
        // TODO Auto-generated method stub
        if(req!=null && !StringUtils.isEmpty(req.getDateTime())){
            PaymentPojo pojo = req.getPaymentPojo();
            if(pojo!=null && !StringUtils.isEmpty(pojo.getActivityId())
                    && StringUtils.isValidMobile(pojo.getMobile())
                    && !StringUtils.isEmpty(pojo.getSerialNum())
                    && !StringUtils.isEmpty(pojo.getPrizeId())
                    && pojo.getSerialNum().length()<=255
                    && pojo.getActivityId().length()<=255
                    && !StringUtils.isEmpty(pojo.getEnterpriseCode())
                    && pojo.getEnterpriseCode().length()<=64
                    && !StringUtils.isEmpty(pojo.getEcProductCode())
                    && pojo.getEcProductCode().length()<=64){
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Map<String, String> notifyPayment(PaymentPojo pojo, String systemSerial) {
        Map<String, String> map = new HashMap<String, String>();
        String code = "fail";
        String msg = "";
        //校验活动状态（进行中）  
        Activities activity = activitiesService.selectByActivityId(pojo.getActivityId());
        CrowdfundingActivityDetail crowdfundingActivityDetail = 
                crowdfundingActivityDetailService.selectByActivityId(pojo.getActivityId());
        if(activity==null || crowdfundingActivityDetail == null){
            LOGGER.info("参与众筹活动，活动不存在，activityId={}", pojo.getActivityId());
            msg = "查找不到相应活动信息";
            map.put("code", code);
            map.put("msg", msg);
            return map;
        }
        //校验报名方式
        if(!crowdfundingActivityDetail.getJoinType().toString()
                .equals(CrowdFundingJoinType.Big_Enterprise.getCode().toString())){
            LOGGER.info("该活动不支持企业支付方式。activityId={}", pojo.getActivityId());
            msg = "该活动只支持个人支付方式";
            map.put("code", code);
            map.put("msg", msg);
            return map;
        }        
        
        //校验
        if(!activity.getStatus().equals(ActivityStatus.PROCESSING.getCode()) || activity.getStartTime().after(new Date())
                ||activity.getEndTime().before(new Date())){
            LOGGER.info("参与众筹活动，活动状态异常，activityId={}, status={}, startTime, endTime={}", pojo.getActivityId(), activity.getStatus(), 
                    activity.getStartTime().toString(), activity.getEndTime().toString());
            msg = "活动状态异常";
            map.put("code", code);
            map.put("msg", msg);
            return map;
        }
        
        //判断是否在用户列表中（白名单）新增为三种形式的校验
        if (crowdfundingActivityDetail.getUserList() == 2) {
            if (!userCheckService.checkMobile(pojo.getMobile(), activity.getEntId())) {
                LOGGER.info("用户不满足ADC查询，mobile={}, activityId={}", pojo.getMobile(), activity.getActivityId());
                msg = "手机号【"+pojo.getMobile()+"】不在该活动用户名单中";
                map.put("code", code);
                map.put("msg", msg);
                return map;
            }
        } else if (crowdfundingActivityDetail.getUserList() == 3) {
            if (!entUserQueryService.checkMobile(pojo.getMobile(), crowdfundingActivityDetail.getId())) {
                LOGGER.info("用户不满足企业接口查询，mobile={}, activityId={}", pojo.getMobile(), activity.getActivityId());
                msg = "手机号【"+pojo.getMobile()+"】不在该活动用户名单中";
                map.put("code", code);
                map.put("msg", msg);
                return map;
            }
        } else {          
            if(!activitiesService.checkUser(pojo.getMobile(), activity.getActivityId())){
                LOGGER.info("用户不满足黑白名单，mobile={}, activityId={}", pojo.getMobile(), activity.getActivityId());
                msg = "手机号【"+pojo.getMobile()+"】不在该活动用户名单中";
                map.put("code", code);
                map.put("msg", msg);
                return map;
            }
        }

        //校验奖品ID是否正确
        boolean prizeTag = false;
        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(pojo.getActivityId());
        for(ActivityPrize prize : prizes){
            if(prize.getId().toString().equals(pojo.getPrizeId())){
                prizeTag = true;
                break;
            }
        }
        if(!prizeTag){
            msg = "活动奖品不存在";
            map.put("code", code);
            map.put("msg", msg);
            return map;
        }
        
        //校验该用户是否已经参加该众筹活动
        Map searchMap = new HashMap();
        searchMap.put("ownMobile", pojo.getMobile());
        searchMap.put("activityId", pojo.getActivityId());
        List<ActivityWinRecord> activityWinRecord = activityWinRecordService.selectByMap(searchMap);
        if(activityWinRecord!=null && activityWinRecord.size()>0){
            LOGGER.info("用户已经参加该活动,mobile={}, activityId={}", pojo.getMobile(), pojo.getActivityId());
            msg = "用户已参加该活动";
            map.put("code", code);
            map.put("msg", msg);
            return map;
        }
        
        String recordId = "";
        //参加众筹活动
        if(activitiesService.joinCrowdfundingActivity(pojo.getActivityId(), pojo.getMobile(), Long.parseLong(pojo.getPrizeId()), "EC")){            
            //重新获取报名记录
            activityWinRecord = activityWinRecordService.selectByMap(searchMap);
            if(activityWinRecord == null || activityWinRecord.size()!=1){
                LOGGER.info("筛选activityWinRecord失败！,mobile={}, activityId={}", pojo.getActivityId(), pojo.getMobile());
                throw new RuntimeException("筛选activityWinRecord失败");
            }
            ActivityWinRecord record = activityWinRecord.get(0);  
            if(record.getPayResult().intValue() == PaymentStatus.Pay_Success.getStatus()){
                LOGGER.info("不能重复将用户【"+pojo.getMobile()+"】的报名记录ID【"+record.getRecordId()+"】更新为支付完成");
                throw new RuntimeException("不能重复将用户的报名记录更新为支付完成");
            }else{
                //1、更新中奖纪录信息为支付完成
                ActivityWinRecord newRecord = new ActivityWinRecord();
                newRecord.setRecordId(record.getRecordId());
                newRecord.setPayResult(PaymentStatus.Pay_Success.getStatus());
                newRecord.setPaySerialNum(systemSerial);
                if (!activityWinRecordService.updateByPrimaryKeySelective(newRecord)) {
                    LOGGER.error("更新支付完成出错. recordId = {}.", record.getRecordId());
                    throw new RuntimeException("更新支付完成出错");
                }
                //2、插入支付信息
                if(!activityPaymentInfoService.insertSelective(buildActivityPaymentInfo(
                        systemSerial, pojo.getSerialNum(), record.getRecordId()))){
                    LOGGER.error("插入支付流水号出错. recordId = {}.", record.getRecordId());
                    throw new RuntimeException("插入支付流水号出错");
                }
                
                if(crowdfundingActivityDetail.getResult().equals(0) 
                        && crowdfundingActivityDetail.getCurrentCount() >= crowdfundingActivityDetail.getTargetCount()){
                    crowdfundingActivityDetail.setResult(1);
                    if(!crowdfundingActivityDetailService.updateByPrimaryKeySelective(crowdfundingActivityDetail)){
                        LOGGER.info("修改众筹活动crowdfundingActivityDetail，结果为众筹成功，失败");
                    }
                    if (!crowdfundingCallbackService.notifyCrowdFundingSucceed(pojo.getActivityId())) {
                        LOGGER.info("向企业发送众筹成功通知，失败");
                    }
                }

                //哟西！成功
                code = "success";
                msg = "手机号【"+pojo.getMobile()+"】在该活动用户名单中";
                recordId = record.getRecordId();
            }
                
        }else{
            LOGGER.info("参与众筹活动失败，activityId={}, mobile={}, prizeId={}", pojo.getActivityId(), pojo.getMobile(), pojo.getPrizeId());
            throw new RuntimeException("参与众筹活动失败");
        }
        map.put("code", code);
        map.put("msg", msg);
        map.put("recordId", recordId);
        return map;       
    }
    
    private ActivityPaymentInfo buildActivityPaymentInfo(String systemSerial, 
            String serial, String recordId){
        ActivityPaymentInfo record = new ActivityPaymentInfo();
        record.setWinRecordId(recordId);
        record.setSysSerialNum(systemSerial);
        record.setReturnSerialNum(serial);
        record.setDeleteFlag(0);
        record.setStatus(PaymentStatus.Pay_Success.getStatus());
        record.setChargeTime(new Date());
        record.setChargeUpdateTime(new Date());
        return record;
    }

    @Override
    public boolean validateQueryActResReq(QueryActResReq req) {
        // TODO Auto-generated method stub
        if(req!=null && !StringUtils.isEmpty(req.getDateTime())){
            QueryActResPojo pojo = req.getQueryActResPojo();
            if(pojo!=null && !StringUtils.isEmpty(pojo.getActivityId())
                    && !StringUtils.isEmpty(pojo.getEnterpriseCode())
                    && !StringUtils.isEmpty(pojo.getEcProductCode())){
                return true;
            }
        }
        return false;
    }

    @Override
    public ActivityResultResp queryActivityResult(QueryActResPojo pojo) {
        // TODO Auto-generated method stub
        ActivityResultResp resp = new ActivityResultResp();
        ActivityResultPojo respPojo = new ActivityResultPojo();
        
        Activities activities = activitiesService.selectByActivityId(pojo.getActivityId());
        CrowdfundingActivityDetail crowdfundingActivityDetail = 
                crowdfundingActivityDetailService.selectByActivityId(pojo.getActivityId());
        if(activities!=null && crowdfundingActivityDetail!=null){
            resp.setCode(CallbackResult.SUCCESS.getCode());
            resp.setMessage("查询成功");
            //加密活动ID
            String encryptActId = activitiesService.encryptActivityId(pojo.getActivityId());
            respPojo.setActivityId(encryptActId);
            
            respPojo.setActivityName(activities.getName());
            respPojo.setResult(crowdfundingActivityDetail.getResult());
            respPojo.setTargetCount(crowdfundingActivityDetail.getTargetCount());
            respPojo.setCurrentCount(crowdfundingActivityDetail.getCurrentCount());
        }else{
            resp.setCode(CallbackResult.OTHERS.getCode());
            resp.setMessage("活动不存在");
        }
        resp.setDateTime(new DateTime().toString());
        resp.setActivityResultPojo(respPojo);
        return resp;
    }

    @Override
    public CFChargeResultResp queryCFChargeResult(String recordId) {
        // TODO Auto-generated method stub
        CFChargeResultResp cfChargeResultResp = new CFChargeResultResp();
        CFChargeResultRespPojo respPojo = new CFChargeResultRespPojo();
        String type = "yyyy-MM-dd HH:mm:ss";
        
        if(!StringUtils.isEmpty(recordId)){
            ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(recordId);
            ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(recordId);
            SerialNum serialNum = serialNumService.getByPltSerialNum(recordId);
            if(activityWinRecord != null && chargeRecord != null){
                cfChargeResultResp.setCode(CallbackResult.SUCCESS.getCode());
                cfChargeResultResp.setMessage("查询成功");
                
                Activities activities = 
                        activitiesService.selectByActivityId(activityWinRecord.getActivityId());
                ActivityPrize activityPrize = 
                        activityPrizeService.selectPrizeDetailByPrimaryKey(activityWinRecord.getPrizeId());
                
                respPojo.setStatus(activityWinRecord.getStatus().toString());
                respPojo.setReason(activityWinRecord.getReason());
                respPojo.setChargeTime(DateUtil.dateToString(chargeRecord.getChargeTime(), type));
                respPojo.setMobile(activityWinRecord.getOwnMobile());
                
                //加密后的activityid 
                String encryptActivityId = activitiesService.encryptActivityId(activityWinRecord.getActivityId()); 
                respPojo.setActivityId(encryptActivityId);
                respPojo.setActivityName(activities.getName());
                respPojo.setPrizeName(productSizeFun(activityPrize.getProductSize()));
                respPojo.setProductName(activityPrize.getProductName());    
                respPojo.setProductCode(activityPrize.getProductCode());    
                respPojo.setDiscount(activityPrize.getDiscount().intValue()/10.0 + "折");
                respPojo.setSetialNum(serialNum.getEcSerialNum());
                respPojo.setSystemNum(activityWinRecord.getRecordId());
                
                cfChargeResultResp.setCfChargeResultRespPojo(respPojo);
            }else{
                LOGGER.info("未查到相应充值记录，充值ID = {}", recordId);
                cfChargeResultResp.setCode(CallbackResult.OTHERS.getCode());
                cfChargeResultResp.setMessage("未查找到ID【"+recordId+"】对应的充值记录");
            }
            cfChargeResultResp.setDateTime(new DateTime().toString());
            return cfChargeResultResp;
        }
        return null;
    }
    
    private String productSizeFun(Long productSize){
        if(productSize!=null){
            if(productSize < 1024){
                return productSize + "KB";
            }else if(productSize >= 1024 && productSize < 1024*1024){
                return (productSize * 1.0 / 1024) + "MB";
            }else if(productSize >= 1024*1024){
                return (productSize * 1.0 / 1024 / 1024) + "GB";
            }
        }
        return "-";
    }

    @Override
    public boolean validateQueryJoinResultRequest(QueryJoinResReq req) {
        QueryJoinResReqData data = req.getData();
        if(data==null){
            LOGGER.info("QueryJoinResReqData为空");
            return false;
        }
        if(StringUtils.isEmpty(data.getActivityId()) 
                || StringUtils.isEmpty(req.getRequestTime())
                || StringUtils.isEmpty(data.getEnterpriseCode()) 
                || !StringUtils.isValidMobile(data.getMobile())
                || StringUtils.isEmpty(data.getEcProductCode())){
            LOGGER.info("存在参数为空");
            return false;
        }
        Map decryptResult = activitiesService.decryptActivityId(data.getActivityId(), data.getEnterpriseCode(), data.getEcProductCode());
        //解析activityId成功
        if("success".equals(decryptResult.get("code"))){
            LOGGER.info("解析前活动ID={}, enterpriseCode={}, 解析后活动ID={}", data.getActivityId(), data.getEnterpriseCode(), (String)decryptResult.get("message"));
            data.setActivityId((String)decryptResult.get("message"));
        }else{
            LOGGER.info("activityId解析失败，解析前activityId={},enterpriseCode={}", data.getActivityId(), data.getEnterpriseCode());
            return false;
        }
        //替换参数中的activityId
        req.setData(data);
        return true;
    }

    @Override
    public QueryJoinResResp queryJoinResult(QueryJoinResReq req) {
        QueryJoinResResp resp = new QueryJoinResResp();
        resp.setResponseTime(new DateTime().toString());
        
        String activityId = req.getData().getActivityId();
        String mobile = req.getData().getMobile();
        
        Map map = new HashMap();
        map.put("ownMobile", mobile);
        map.put("activityId", activityId);
        List<ActivityWinRecord> records = activityWinRecordService.selectByMap(map);
        
        if(records==null || records.size()==0){
            LOGGER.info("ActivityWinRecord为空");
            resp.setMessage("未查询到【"+mobile+"】的报名记录");
            resp.setCode("10001");
            return resp;
        }else{
            if(records.size()==1){
                ActivityWinRecord record = records.get(0);
                JoinActivityResultPojo data = new JoinActivityResultPojo();
                data.setMobile(record.getOwnMobile());
                data.setRecordId(record.getRecordId());
                data.setPayResult(record.getPayResult().toString());
                String type = "yyyy-MM-dd HH:mm:ss";
                data.setWinTime(DateUtil.dateToString(record.getWinTime(), type));
                data.setPrizeId(record.getPrizeId());
                resp.setData(data);
            }else{
                LOGGER.info("ActivityWinRecord有多条记录");
                resp.setMessage("查询失败");
                resp.setCode("10001");
                return resp;
            }
        }

        resp.setMessage("成功");
        resp.setCode("10000");
        
        return resp;
    }

}
