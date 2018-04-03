package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.Encrypter;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MatrixToImageWriter;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WxInviteQrcodeService;
import com.cmcc.vrp.wx.model.WxInviteQrcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * CommonController.java
 * 该类为微信公众号内普通的链接，不需要用户属性的（不过拦截器）
 * 
 * @author wujiamin
 * @date 2017年2月22日
 */
@Controller
@RequestMapping("/wx/common")
public class CommonController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    WxAdministerService wxAdministerService;

    @Autowired
    WxInviteQrcodeService wxInviteQrcodeService;
    
    @Autowired
    ActivityTemplateService activityTemplateService;
    
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    IndividualAccountService accountService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    ActivityInfoService activityInfoService;

    /** 
     * 微信帮助页面
     * @Title: help 
     */
    @RequestMapping("help")
    public String help() {
        return "weixin/help.ftl";
    }
    
    /** 
     * 获取最新的大转盘活动链接
     * @Title: lottery 
     */
    @RequestMapping("lottery")
    public String lottery() {
        String entId = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_LOTTERY_ENTER_ID.getKey());
        Map map = new HashMap();
        map.put("entId", Long.parseLong(entId));
        map.put("type", ActivityType.LOTTERY.getCode());
        List<Activities> activities = activitiesService.selectLastWxLottery(map);
        if(activities != null){
            for(Activities activity : activities){//找到最新一个有活动链接的活动
                ActivityInfo info = activityInfoService.selectByActivityId(activity.getActivityId());
                if(!StringUtils.isEmpty(info.getUrl())){
                    return "redirect:" + info.getUrl();
                }
            }
        }
        return "404.ftl";
    }

    /** 
     * 显示邀请的二维码（qrcodeId为加密后的手机号码）
     * @Title: getQRCode 
     */
    @RequestMapping("qrCode")
    public void getQrCode(HttpServletResponse resp, String qrcodeId) {
        try {
            String mobile = Encrypter.decrypt(qrcodeId);
            if(StringUtils.isEmpty(mobile)){
                LOGGER.info("用户mobile为空");
                return;
            }
            WxAdminister admin = wxAdministerService.selectByMobilePhone(mobile);
            WxInviteQrcode wxInviteQrcode = wxInviteQrcodeService.selectByAdminId(admin.getId());
            if(wxInviteQrcode == null){
                LOGGER.info("二维码信息为空");
                return;
            }
            String url = wxInviteQrcode.getUrl();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            int nMargin = 1; //定义边框大小
            int nWidth = 256;
            BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, nWidth, nWidth, hints);
            bitMatrix = activityTemplateService.updateBit(bitMatrix, nMargin);

            MatrixToImageWriter.writeToStream(bitMatrix, "png", resp.getOutputStream());
        } catch (WriterException e) {
        } catch (IOException e) {
        }
    }
    
    
    /** 
     * 我的积分，积分明细（收入）
     * @Title: scoreDetail 
     */
    @RequestMapping("/inScoreDetail")
    public void inScoreDetail(QueryObject queryObject, int pageSize, int pageNo, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        WxAdminister admin = getWxCurrentUser();
        if(admin == null){
            LOGGER.info("session中没有用户adminId");
            return;
        }
        LOGGER.info("我的积分明细（收入）adminId = {}, mobile = {}", admin.getId(), admin.getMobilePhone());
        /**
         * 分页转换
         */
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        
        queryObject.getQueryCriterias().put("type", (int)AccountRecordType.INCOME.getValue());
        
        //获取账户accountId
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        if(product == null || product.getId() == null){
            LOGGER.info("平台未配置个人积分产品");
            return;
        }
        IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(account == null){
            LOGGER.info("该用户没有积分账户，adminId={}", admin.getId());
            return;
        }
        queryObject.getQueryCriterias().put("accountId", account.getId());
        List<IndividualAccountRecord> records = individualAccountRecordService.selectByMap(queryObject.toMap());
       
        try {
            Map result = new HashMap();
            result.put("data", records);
            res.getWriter().write(JSONObject.toJSONString(result));            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 我的积分，积分明细（支出）
     * @Title: outScoreDetail 
     */
    @RequestMapping("/outScoreDetail")
    public void outScoreDetail(QueryObject queryObject, int pageSize, int pageNo, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        WxAdminister admin = getWxCurrentUser();
        
        if(admin == null){
            LOGGER.info("session中没有用户adminId");
            return;
        }
        LOGGER.info("我的积分明细（支出）adminId = {}, mobile = {}", admin.getId(), admin.getMobilePhone());
        /**
         * 分页转换
         */
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        
        queryObject.getQueryCriterias().put("type", (int)AccountRecordType.OUTGO.getValue());
        
        //获取账户accountId
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        if(product == null || product.getId() == null){
            LOGGER.info("平台未配置个人积分产品");
            return;
        }
        IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(account == null){
            LOGGER.info("该用户没有积分账户，adminId={}", admin.getId());
            return;
        }
        queryObject.getQueryCriterias().put("accountId", account.getId());
        List<IndividualAccountRecord> records = individualAccountRecordService.selectByMap(queryObject.toMap());
       
        try {
            Map result = new HashMap();
            result.put("data", records);
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
