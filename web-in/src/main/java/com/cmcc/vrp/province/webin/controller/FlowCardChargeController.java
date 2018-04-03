/**
 * @Title: FlowCardController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: qihang
 * @date: 2015年5月19日 上午10:28:36
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量卡充值
 *
 * @author wujiamin
 * @date 2016年8月17日下午1:53:15
 */
@Controller
@RequestMapping("/manage/flowcard/charge")
public class FlowCardChargeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FlowCardChargeController.class);


    @Autowired
    PhoneRegionService phoneRegionService;

    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Autowired
    SmsRedisListener smsRedisListener;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    ActivitiesService activitiesService;

    /**
     * 流量券通用链接，通过短信验证码和手机号登录进去
     * @param type 登录渠道，目前这个是上海的需求
     * @author qinqinyan
     * */
    @RequestMapping("chargeLogin")
    public String chargeLogin(ModelMap map, String type){
        String provinceFlag = getProvinceFlag();
        map.addAttribute("channel", type);
        if("shanghai".equals(provinceFlag)){
            //目前流量券记录充值渠道只在上海流量平台实现
            return "flowcard/chargeLogin-shanghai.ftl";
        }
        return "flowcard/chargeLogin.ftl";
    }


    /**
     * @param map
     * @param secrectPhone
     * @return
     * @Title: loginInde
     * @Description: 流量券登录页面
     * @Author: wujiamin
     * @date 2016年8月23日上午10:50:32
     */
    @RequestMapping("{secretPhone}")
    public String loginIndex(ModelMap map, @PathVariable String secretPhone) {
        //解密
        String ownerPhone = decryptOwnerPhone(secretPhone);

        //校验是否是有效的号码
        if (!StringUtils.isValidMobile(ownerPhone)) {
            logger.info("非有效的电话号码:" + ownerPhone);
            map.put("errorMsg", "非合法的流量券登录链接!");
            return "error.ftl";
        }

        map.put("secretPhone", secretPhone);
        return "flowcard/flowCardChargeIndex.ftl";

    }

    /**
     * 登陆后：流量券可用列表
     *
     * @param model
     * @param secretPhone
     * @param channel
     * @return
     * @Title: list
     * @Author: wujiamin
     * @date 2016年8月23日上午10:50:49
     * 
     * edit by qinqinyan on 22/6/2017
     * 增加充值渠道标示
     */
    @RequestMapping("list/{secretPhone}")
    public String list(ModelMap model, @PathVariable String secretPhone, String channel) {
        //充值渠道
        model.addAttribute("channel", channel);
        
        //解密
        String ownerPhone = decryptOwnerPhone(secretPhone);
        logger.info("解密后的手机号："+ownerPhone);

        //校验是否是有效的号码
        if (!StringUtils.isValidMobile(ownerPhone)) {
            logger.info("非有效的电话号码:" + ownerPhone);
            model.put("errorMsg", "非合法的流量券登录链接!");
            return "error.ftl";
        }

        //显示可用的流量券列表
        Map map = new HashMap();
        map.put("ownMobile", ownerPhone);
        map.put("status", ChargeRecordStatus.UNUSED.getCode().toString());
        map.put("activityStatus", ActivityStatus.PROCESSING.getCode().toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("now", format.format(new Date()));
        List<ActivityWinRecord> records = activityWinRecordService.selectByMap(map);

        model.put("secretPhone", secretPhone);
        model.put("ownerPhone", ownerPhone);
        model.put("records", records);
        
        model.put("hasRecords", records!=null&&records.size()>0?"true":"false");

        //获取手机号码地区和运营商
        PhoneRegion phoneRegion = phoneRegionService.query(ownerPhone);
        if (phoneRegion != null) {
            String isp = null;
            if (phoneRegion.getSupplier().equals("M")) {
                isp = "移动";
            }
            if (phoneRegion.getSupplier().equals("T")) {
                isp = "电信";
            }
            if (phoneRegion.getSupplier().equals("U")) {
                isp = "联通";
            }
            model.put("province", phoneRegion.getProvince());
            model.put("isp", isp);
        }
        
        String provinceFlag = getProvinceFlag();
        model.addAttribute("provinceFlag", provinceFlag);

        return "flowcard/flowCardCharge.ftl";

    }


    /**
     * 流量券充值提交
     *
     * @param recordIds
     * @param chargePhone
     * @param ownerPhone
     * @param channel 充值渠道
     * @param res
     * @param map
     * @throws
     * @Title: submit
     * @Author: wujiamin
     * @date 2016年8月17日下午4:38:53
     */
    @RequestMapping("submit")
    public void submit(String recordIds, String chargePhone, String secretPhone, 
            String channel, HttpServletResponse res, ModelMap map) {
        JSONObject json = new JSONObject();
        String[] ids = com.cmcc.vrp.util.StringUtils.split(recordIds, ",");
        //解密
        String ownerPhone = decryptOwnerPhone(secretPhone);

        //校验是否是有效的号码
        if (!StringUtils.isValidMobile(ownerPhone)) {
            logger.info("非有效的电话号码:" + ownerPhone);
            json.put("result", "充值提交失败！");
            try {
                res.getWriter().write(json.toString());
                return;
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("异常信息:" + e.getMessage());
            }
        }

        //校验流量券充值参数
        String checkMsg = checkFlowCardCharge(ids, ownerPhone, chargePhone);
        if (!StringUtils.isEmpty(checkMsg)) {
            json.put("result", checkMsg);
        }else {//流量卡充值提交  
            Map<Object, Object> extendParams = new LinkedHashMap<Object, Object>();
            extendParams.put("recordIds", recordIds);
            extendParams.put("ownerPhone", ownerPhone);

            if (activitiesService.participate("flowcard", chargePhone, extendParams, channel)) {
                json.put("result", "success");
            } else {
                json.put("result", "充值失败！");
            }
            if("shanghai".equals(getProvinceFlag())){
                json.put("channel", channel);
            }
        }
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param chargePhone
     * @param ownerPhone
     * @return
     * @Title: checkAvaliableCharge
     * @Description: 检查待充值手机号供应商是否可用
     * @Author: wujiamin
     * @date 2016年8月17日下午5:04:50
     */
    private String checkAvaliableCharge(String chargePhone, String ownerPhone) {
        PhoneRegion phoneRegionOwner = phoneRegionService.query(ownerPhone);
        if (phoneRegionOwner == null) {
            logger.error("无法获取手机号{}的归属地信息.", ownerPhone);
            return "无法获取流量券用户手机号码归属地";
        }
        String supplierName = null;
        if (phoneRegionOwner.getSupplier().equals("T")) {
            supplierName = "电信";
        } else if (phoneRegionOwner.getSupplier().equals("M")) {
            supplierName = "移动";
        } else if (phoneRegionOwner.getSupplier().equals("U")) {
            supplierName = "联通";
        } else {
            supplierName = "未知";
        }

        PhoneRegion phoneRegion = phoneRegionService.query(chargePhone);
        if (phoneRegion == null) {
            logger.error("无法获取手机号{}的归属地信息.", chargePhone);
            return "请输入" + supplierName + "手机号";
        }
        if (!phoneRegionOwner.getSupplier().equals(phoneRegion.getSupplier())) {
            logger.info("ownerPhone={},归属地={}.chargePhone={},归属地={}",
                ownerPhone, phoneRegionOwner.getSupplier(), chargePhone, phoneRegion.getSupplier());
            return "请输入" + supplierName + "手机号";
        }
        return "true";
    }

    /**
     * 检查流量券充值提交的参数是否合法
     *
     * @param ids
     * @param ownerPhone
     * @param chargePhone
     * @return
     * @Title: checkFlowCardCharge
     * @Author: wujiamin
     * @date 2016年8月17日下午5:05:26
     */
    private String checkFlowCardCharge(String[] ids, String ownerPhone, String chargePhone) {
        String msg = null;

        if (ids.length <= 0) {
            msg = "未选择充值的流量券";
            return msg;
        }
        if (StringUtils.isEmpty(chargePhone)) {
            msg = "请输入手机号";
            return msg;
        }
        if (!StringUtils.isValidMobile(chargePhone)) {
            msg = "请输入正确格式手机号";
            return msg;
        }
        //判断手机号与流量券运营商
        String supplierCheck = checkAvaliableCharge(chargePhone, ownerPhone);
        if (!"true".equals(supplierCheck)) {
            msg = supplierCheck;
            return msg;
        }
        return msg;
    }

    /**
     * 校验流量券登录验证码
     *
     * @param code
     * @param phone
     * @return
     * @throws RuntimeException
     * @Title: checkRandomCode
     * @Author: wujiamin
     * @date 2016年8月23日上午11:08:19
     */
    private boolean checkRandomCode(String code, String phone) throws RuntimeException {
        LoginSmsPojo pojo = smsRedisListener.getLoginInfo(phone, SmsType.FLOWCARD_SMS);
        if (pojo == null) {
            throw new RuntimeException("请先获取短信验证码!");
        }

        //判断是否已超过5分钟
        Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
        if (after.getTime() < (new Date()).getTime()) {
            throw new RuntimeException("短信验证码已超时!");
        }

        if (!code.equals(pojo.getPassword())) {
            throw new RuntimeException("请输入正确短信验证码!");
        }
        return true;

    }

    /**
     * 流量券登录页面验证图形验证码
     *
     * @param secretPhone
     * @param res
     * @throws IOException
     * @Title: sendFlowcardLogin
     * @Author: wujiamin
     * @date 2016年8月23日下午2:11:56
     */
    @RequestMapping(value = "checkImgCode")
    public void checkImgCode(String code, HttpServletResponse res, HttpServletRequest request)
        throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        String sessionCode = (String) request.getSession(true).getAttribute("virifyCode");//得到session中的验证码
        if (StringUtils.isEmpty(code) || !(code.equalsIgnoreCase(sessionCode))) {
            map.put("message", "图形验证码错误!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            map.put("message", "success");
            res.getWriter().write(JSON.toJSONString(map));
        }
    }

    /**
     * 获取加密秘钥
     */
    public String getActivityURLKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
    }

    /**
     * demo不校验短信验证码
     * */
    @RequestMapping(value = "checkMsgCodeTest")
    public void checkMsgCodeTest(String code, String phone, HttpServletResponse res)
        throws IOException {
        logger.info("code:"+code+" phone："+phone);
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        try {
            String secretPhone = "";
            String result;
            String msg = "";
            if(StringUtils.isValidMobile(phone)){
                String key = getActivityURLKey();
                byte[] encryptResult = AES.encrypt(phone.getBytes(), key.getBytes());
                secretPhone = DatatypeConverter.printHexBinary(encryptResult);

                logger.info("加密后的手机号："+secretPhone);
                result = "success";
            }else{
                result = "fail";
                msg = "输入手的机号不合法";
            }
            map.put("secretPhone", secretPhone);
            map.put("message", result);
            map.put("msg", msg);
            res.getWriter().write(JSON.toJSONString(map));
            return;
        } catch (RuntimeException e) {
            map.put("message", "fail");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }


    /**
     * 检查短信验证码
     *
     * @param code
     * @param res
     * @param request
     * @throws IOException
     * @Title: checkMsgCode
     * @Author: wujiamin
     * @date 2016年8月23日下午3:30:23
     */
    @RequestMapping(value = "checkMsgCode")
    public void checkMsgCode(String code, String secretPhone, HttpServletResponse res, HttpServletRequest request)
        throws IOException {

        //解密
        String ownerPhone = "";
        if("shanghai".equals(getProvinceFlag()) || secretPhone.length()==11){
            ownerPhone = secretPhone;
            secretPhone = encryptOwnerPhone(ownerPhone);
        }else{
            ownerPhone = decryptOwnerPhone(secretPhone);
        }

        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        //校验是否是有效的号码
        if (!StringUtils.isValidMobile(ownerPhone)) {
            logger.info("非有效的电话号码:" + ownerPhone);
            map.put("message", "非有效的电话号码!");
            try {
                res.getWriter().write(JSON.toJSONString(map));
                return;
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("异常信息:" + e.getMessage());
            }
        }


        try {
            if (checkRandomCode(code, ownerPhone)) {
                map.put("message", "success");
                map.put("secretPhone", secretPhone);
                res.getWriter().write(JSON.toJSONString(map));
                return;
            }
        } catch (RuntimeException e) {
            map.put("message", e.getMessage());
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }

    /**
     * 查询手机号归属地运运营商
     *
     * @param mobile
     * @param isp
     * @param res
     * @param request
     * @throws IOException
     * @Title: queryPhone
     * @Author: wujiamin
     * @date 2016年8月23日下午7:34:22
     */
    @RequestMapping(value = "queryPhone")
    public void queryPhone(String mobile, String isp, HttpServletResponse res, HttpServletRequest request)
        throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        //获取手机号码地区和运营商
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if (phoneRegion != null) {
            String isp2 = null;
            if (phoneRegion.getSupplier().equals("M")) {
                isp2 = "移动";
            }
            if (phoneRegion.getSupplier().equals("T")) {
                isp2 = "电信";
            }
            if (phoneRegion.getSupplier().equals("U")) {
                isp2 = "联通";
            }
            if (isp.equals(isp2)) {
                map.put("success", "success");
                map.put("message", phoneRegion.getProvince() + isp2);
                res.getWriter().write(JSON.toJSONString(map));
                return;
            }
            map.put("message", "请输入" + isp + "手机号");
            res.getWriter().write(JSON.toJSONString(map));
            return;

        } else {
            map.put("message", "请输入" + isp + "手机号");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

    }

    /**
     * 解密电话号码
     *
     * @param secretPhone
     * @return
     * @Title: decryptOwnerPhone
     * @Author: wujiamin
     * @date 2016年8月24日下午7:19:57
     */
    private String decryptOwnerPhone(String secretPhone) {
        String key = globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
        String phone = AES.decryptString(secretPhone, key);
        logger.info("解密后的电话号码为:" + phone);
        return phone;
    }
    
    private String encryptOwnerPhone(String phone){
        String key = getActivityURLKey();
        byte[] encryptResult = AES.encrypt(phone.getBytes(), key.getBytes());
        String secretPhone = DatatypeConverter.printHexBinary(encryptResult);
        logger.info("加密后的电话号码为:" + secretPhone);
        return secretPhone;
    }

}
