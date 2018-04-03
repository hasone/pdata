package com.cmcc.vrp.province.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.dao.ActivityPaymentInfoMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.PaymentService;
import com.cmcc.vrp.wx.beans.PayParameter;

/**
 * Created by qinqinyan on 2017/1/6.
 */
@Service("activityPaymentInfoService")
public class ActivityPaymentInfoServiceImpl implements ActivityPaymentInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPaymentInfoServiceImpl.class);
    
    @Autowired
    ActivityPaymentInfoMapper mapper;
    
    @Autowired
    ActivitiesService activitiesService;

    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    PaymentService paymentService;
    
    @Autowired
    ActivityPrizeService activityPrizeService;
    
    @Autowired
    ProductService productService;
    
    /**
     * @Description:根据主键id查找
     * @param id
     * */
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id == null){
            return false;
        }
        return mapper.deleteByPrimaryKey(id)==1;
    }

    /**
     * @Description:根据主键sysSerialNum查找
     * @param sysSerialNum
     * */
    @Override
    public boolean deleteBySysSerialNum(String sysSerialNum) {
        if(StringUtils.isEmpty(sysSerialNum)){
            return false;
        }
        return mapper.deleteBySysSerialNum(sysSerialNum)==1;
    }

    /**
     * @Description:插入
     * @param record
     * */
    @Override
    public boolean insert(ActivityPaymentInfo record) {
        if(record == null){
            return false;
        }
        return mapper.insert(record)==1;
    }

    /**
     * @Description:插入
     * @param record
     * */
    @Override
    public boolean insertSelective(ActivityPaymentInfo record) {
        if(record == null){
            return false;
        }
        return mapper.insertSelective(record)==1;
    }

    /**
     * @Description:根据id查找
     * @param id
     * */
    @Override
    public ActivityPaymentInfo selectByPrimaryKey(Long id) {
        if(id == null){
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * @Description:根据sysSerialNum查找
     * @param sysSerialNum
     * */
    @Override
    public ActivityPaymentInfo selectBySysSerialNum(String sysSerialNum) {
        if(StringUtils.isEmpty(sysSerialNum)){
            return null;
        }
        return mapper.selectBySysSerialNum(sysSerialNum);
    }

    /**
     * @Description:根据系统流水号更新记录
     * @param record
     * */
    @Override
    public boolean updateBySysSerialNumSelective(ActivityPaymentInfo record) {
        if(record == null){
            return false;
        }
        return mapper.updateBySysSerialNumSelective(record)==1;
    }

    /**
     * @Description:根据系统流水号更新记录
     * @param record
     * */
    @Override
    public boolean updateBySysSerialNum(ActivityPaymentInfo record) {
        if(record == null){
            return false;
        }
        return mapper.updateBySysSerialNum(record)==1;
    }

    /** 
     * 存在一个活动多次支付的情况（微信支付时支付失败可以重新支付）
     * @Title: selectByWinRecordId 
     * @param winRecordId
     * @return
    */
    @Override
    public List<ActivityPaymentInfo> selectByWinRecordId(String winRecordId) {
        if(StringUtils.isEmpty(winRecordId)){
            return null;
        }
        return mapper.selectByWinRecordId(winRecordId);
    }
    
    @Override
    public ActivityPaymentInfo selectByReturnSerialNum(String returnSerialNum) {
        if(StringUtils.isEmpty(returnSerialNum)){
            return null;
        }
        return mapper.selectByReturnSerialNum(returnSerialNum);
    }
    
    @Override
    public boolean callForPay(String activityWinRecordId) {
        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);
        //1、插入支付记录activity_payment_info
        ActivityPaymentInfo info = initActivityPaymentInfo(record);
        if(!insertSelective(info)){
            return false;
        }
        
        //2、发起支付请求
        PayParameter parameter = new PayParameter();
        parameter.setoId(info.getSysSerialNum());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameter.setoTime(sdf.format(new Date()));
        parameter.setpUser(record.getOwnMobile());
        parameter.setAmount(info.getPayAmount().toString());
        Activities activity = activitiesService.selectByActivityId(record.getActivityId());
        parameter.setTitle(activity.getName());
        
        if(!paymentService.sendChargeRequest(paymentService.combinePayPara(parameter))){
            LOGGER.info("发起支付请求失败");
            info.setStatus(3);//修改支付状态为支付失败
            info.setChargeUpdateTime(new Date());
            info.setErrorMessage("发起支付请求失败");
            //响应失败，则只修改activity_payment_info
            if(!updateBySysSerialNumSelective(info)){
                LOGGER.error("更新activity_payment_info失败，activity_payment_info={}", JSONObject.toJSONString(info));
            }
            return false;
        }
        
        //3、支付请求成功时，将activity_payment_info,activity_win_record支付状态修改为支付中
        info.setStatus(1);//修改支付状态为支付中
        if(!updateBySysSerialNumSelective(info)){
            LOGGER.error("更新activity_payment_info失败，activity_payment_info={}", JSONObject.toJSONString(info));
        }
        
        record.setPayResult(1);
        if(!activityWinRecordService.updateByPrimaryKeySelective(record)){
            LOGGER.error("更新activity_win_record失败，activity_win_record={}", JSONObject.toJSONString(record));
        }
      
        return true;
    }
    
    /** 
     * @Title: initActivityPaymentInfo 
     */
    private ActivityPaymentInfo initActivityPaymentInfo(ActivityWinRecord record){
        ActivityPaymentInfo info = new ActivityPaymentInfo();
        info.setWinRecordId(record.getRecordId());
        info.setSysSerialNum(payUuid(record.getActivityId()));
        info.setChargeType("wx");//TODO:支付类型需确认
        info.setChargeTime(new Date());
        info.setStatus(0);//未支付
        info.setDeleteFlag(0);
        
        ActivityPrize prize = activityPrizeService.selectByPrimaryKey(record.getPrizeId());
        Product product = productService.selectProductById(prize.getProductId());       
        Long price = (long) (product.getPrice() * prize.getDiscount()/100);
        info.setPayAmount(price);
        
        return info;
    }
    
    
    private String payUuid(String activityId){
        Activities activity = activitiesService.selectByActivityId(activityId);
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(activity.getEntId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        int[] nums = new int[6];
        String random = "";
        for(int i = 0;i < nums.length;i++){
            nums[i] = (int)(Math.random()*10);
            random = random + nums[i];
        }
        String uuid = "GZH" + enterprise.getCode() + time + random;
        return uuid;
    }

    @Override
    public List<ActivityPaymentInfo> selectByWinRecordIdAndStatus(String winRecordId, Integer status) {
        if(StringUtils.isEmpty(winRecordId) || status==null){
            return null;
        }
        return mapper.selectByWinRecordIdAndStatus(winRecordId, status);
    }
}
