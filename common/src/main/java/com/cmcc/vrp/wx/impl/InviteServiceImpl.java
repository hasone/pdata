package com.cmcc.vrp.wx.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.wx.InviteService;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxInviteQrcodeService;
import com.cmcc.vrp.wx.WxInviteRecordService;
import com.cmcc.vrp.wx.beans.InviteQrcodeResp;
import com.cmcc.vrp.wx.beans.InvitedRecordPojo;
import com.cmcc.vrp.wx.model.WxInviteQrcode;
import com.cmcc.vrp.wx.model.WxInviteRecord;

/**
 * InviteServiceImpl.java
 * @author wujiamin
 * @date 2017年2月23日
 */
@Service
public class InviteServiceImpl implements InviteService{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(InviteServiceImpl.class);
    
    private static final String FIRST_INVITE_KEY = "firstInvite";
    
    @Autowired
    RedisUtilService redisUtilService;
    
    @Autowired
    WxAdministerService wxAdministerService;
    
    @Autowired
    WxInviteQrcodeService wxInviteQrcodeService;
    
    @Autowired
    WetchatService wetchatService;
    
    @Autowired
    WxInviteRecordService wxInviteRecordService;
    
    @Autowired
    IndividualProductService individualProductService;

    @Autowired
    private IndividualAccountService individualAccountService;
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String getFirstFlag(Long adminId) {
        String firstFlag = redisUtilService.get(FIRST_INVITE_KEY + adminId.toString());
        if(StringUtils.isEmpty(firstFlag)){
            //第一次点击邀请页面的标记为空。在缓存中加上标记，并返回true
            redisUtilService.set(FIRST_INVITE_KEY + adminId.toString(), "false", null);
            return "true";
        }
        return firstFlag;
    }
    
    @Override
    public boolean refreshQrcode(String mobile, String openid) {
        WxAdminister admin = wxAdministerService.selectByMobilePhone(mobile);
        if(admin == null || admin.getId() == null){
            LOGGER.info("根据手机号{}查找的用户为空", mobile);
            return false;
        }
        WxInviteQrcode wxInviteQrcode = wxInviteQrcodeService.selectByAdminId(admin.getId());
        //用户无二维码记录，或二维码5天内将过期，重新生成二维码
        if(wxInviteQrcode == null || wxInviteQrcode.getExpireTime().before(DateUtil.getDateAfter(new Date(), 5))){
            //向微信公众号发送请求生成二维码
            LOGGER.info("向微信公众号发送请求生成二维码");
            WxInviteQrcode newQrcode = new WxInviteQrcode();
            newQrcode.setCreateTime(new Date());

            /**
             * edit by qinqinyan on 2017/07/12
             * 创建携带字符串参数的临时二维码
             * 
             * 为了兼容后面的设计需求  
             * eventKey是以"qrscene_"为前缀后面携带二维码参数，所以二位参数构造成"qrscene_|平台adminsterId|openid"
             * 由于openid是可能是由"_"下划线构成，所以构造的参数改成 "|"分隔
             * adminId|openid
             * */
            //InviteQrcodeResp resp = wetchatService.getInviteQrcode(admin.getId().intValue());
            InviteQrcodeResp resp = wetchatService.getInviteQrcode("|"+admin.getId().toString()+"|"+openid);
            if(resp == null){
                return false;
            }            
            
            newQrcode.setAdminId(admin.getId());
            newQrcode.setDeleteFlag(0);
            newQrcode.setExpireSeconds(Integer.parseInt(resp.getExpireSeconds()));            
            newQrcode.setExpireTime(DateUtil.getDateAfterSeconds(newQrcode.getCreateTime(), Integer.parseInt(resp.getExpireSeconds())));            
            newQrcode.setOpenid(openid);
            newQrcode.setTicket(resp.getTicket());
            newQrcode.setUpdateTime(new Date());
            newQrcode.setUrl(resp.getUrl());            
            
            int times = wxInviteQrcodeService.insertOrUpdateSelective(newQrcode);                    
            return (wxInviteQrcode == null)?(times==1):(times==2);
        }
            
        return true;
    }
    
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean processInvited(String param) {
        InvitedRecordPojo pojo = null;
        try{
            pojo = JSONObject.parseObject(param, InvitedRecordPojo.class);                      
        }catch(Exception e){
            LOGGER.error("解析InvitedRecordPojo失败，msg={}", e.getMessage());
            return false;
        }
        //1、校验参数是否合法
        WxAdminister invitedAdmin = wxAdministerService.selectByMobilePhone(pojo.getMobile());
        Long inviteAdminId = Long.parseLong(pojo.getAdminId());
        WxAdminister inviteAdmin = wxAdministerService.selectWxAdministerById(inviteAdminId);
        if(invitedAdmin == null || inviteAdminId == null){
            return false;
        }
        //2、存储被邀请记录
        WxInviteRecord inviteRecord = new WxInviteRecord();
        inviteRecord.setInviteAdminId(inviteAdminId);
        inviteRecord.setInvitedAdminId(invitedAdmin.getId());
        inviteRecord.setInvitedOpenid(pojo.getOpenid());
        inviteRecord.setTicket(pojo.getTicket());
        inviteRecord.setInviteTime(new Date());
        inviteRecord.setInviteSerial(SerialNumGenerator.buildSerialNum());
        if(!wxInviteRecordService.insert(inviteRecord)){//数据库设置invitedAdminId唯一索引
            LOGGER.error("wxInviteRecordService.insert失败，inviteRecord={}", JSONObject.toJSON(inviteRecord));
            return false;
        }
        
        //3、增加积分
        //获取积分产品
        IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
        if (individualPointProduct == null) {
            LOGGER.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
            throw new RuntimeException("积分产品为空");
        }

        //增加积分账户
        String point = globalConfigService.get(GlobalConfigKeyEnum.INVITE_POINT.getKey());
        if(StringUtils.isEmpty(point)){
            point = "10";
        }
        //邀请者增加积分
        if (!individualAccountService.addCountForcely(inviteAdmin.getMobilePhone(), individualPointProduct.getId(), inviteRecord.getInviteSerial(), 
                new BigDecimal(point), ActivityType.INVITE, "邀请好友-"+invitedAdmin.getMobilePhone())) {
            LOGGER.error("邀请好友增加积分时失败. 加积分用户的Mobile = {}, 邀请记录SerialNum = {}.", pojo.getMobile(), inviteRecord.getInviteSerial());
        }
        //被邀请者增加积分(被邀请者不增加积分20170417) by wujiamin
        //被邀请者增加积分 20170712 pdata-1463,1467 by qinqinyan 由于发送模板消息写了10个，即不支持配置，因此写死
        if (!individualAccountService.addCountForcely(invitedAdmin.getMobilePhone(), individualPointProduct.getId(), inviteRecord.getInviteSerial(), 
                new BigDecimal(10), ActivityType.INVITED, "被邀请-"+inviteAdmin.getMobilePhone())) {
            LOGGER.error("被邀请增加积分时失败. 加积分用户的Mobile = {}, 邀请记录SerialNum = {}.", pojo.getMobile(), inviteRecord.getInviteSerial());
        }

        //可以了!
        return true;
        
    }
    

}
