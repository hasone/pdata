package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SmsTemplateService;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.VerifycodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SendMsgServiceImpl
 * @Description: 发送短信的服务类
 * @author: qihang
 * @date: 2015年6月9日 下午6:57:24
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements SendMsgService {
    //发短信使用的globalConfig标志的config——key
    private final String sendMsgKey = "SENDMESSAGE_CHECK";

    //SENDMESSAGE_CHECK发送短信时数据库中的值
    private final String sendMsgValue = "OK";

    @Autowired
    SmsRedisListener smsRedisListener;

    @Autowired
    AdministerService adminisetrService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    SmsTemplateService smsTemplateService;

    @Autowired
    TaskProducer taskProducer;

    private Logger logger = LoggerFactory.getLogger(SendMsgService.class);

    /**
     * @param mobile  手机号码
     * @param smstype 短信类型
     * @return
     * @Title: sendRandomCode
     * @Description: 发送随机验证码, 正确返回null，错误返回失败原因
     * @see com.cmcc.vrp.province.service.SendMsgService#sendRandomCode(java.lang.String, com.cmcc.vrp.enums.SmsType)
     */
    @Override
    public String sendRandomCode(String mobile, SmsType smstype) {

        //随机验证码
        String randomCode = VerifycodeUtil.getRandomNum();

        //尝试在redis中设置新的手机验证码
        boolean b = smsRedisListener.setNewPass(mobile, randomCode, smstype);
        if (!b) {
            return "请勿在1分钟之内重复发送";
        }

        logger.info("手机 " + mobile + smstype.getType() + "的最新动态密码是" + randomCode);

        //填充短信模板需要的内容,只需要随机验证码
//        List<String> listContent = new LinkedList<String>();
//        listContent.add(randomCode);


        String sendMsg = "";//得到具体的短信内容

        //根据使用短信类型从数据库中查找不同的模板
        sendMsg = "您的验证码是 " + randomCode + "，有效期5分钟。";

        if (isSendMsg()) {//连接短信网关
            try {
                SmsPojo pojo = new SmsPojo(mobile, sendMsg, null, null, MessageType.RANDOM_CODE.getCode());
                taskProducer.produceDeliverVerifySmsMsg(pojo);

                return null;
            } catch (Exception e) {
                logger.error("发送短信时出错. 错误信息为{}.", e);
                return "连接短信网关失败";
            }
        }

        return null;
    }


    /**
     * @param mobile  手机号
     * @param content 短信内容
     * @return
     * @Title: sendRandomCode
     * @Description: 发送随机验证码
     * @see com.cmcc.vrp.province.service.SendMsgService#sendRandomCode(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean sendVerifyCode(String mobile, String content) {
        if (!StringUtils.isValidMobile(mobile)
            || org.apache.commons.lang.StringUtils.isBlank(content)) {
            return false;
        }


        SmsPojo pojo = new SmsPojo(mobile, content, null, null, MessageType.RANDOM_CODE.getCode());
        boolean bFlag = taskProducer.produceDeliverVerifySmsMsg(pojo);
        logger.info("短信发送【手机号 = {}, 内容 = {}】返回{}.", mobile, content, bFlag ? "成功" : "失败");

        return bFlag;
    }

    /**
     * 通用的发送通知类短信方法
     *
     * @param mobile
     * @param content
     * @param messageType
     * @return
     * @Title: sendMessage
     * @Author: wujiamin
     * @date 2016年8月26日上午11:06:09
     */
    @Override
    public boolean sendMessage(String mobile, String content, String messageType) {
        if (!StringUtils.isValidMobile(mobile)
            || org.apache.commons.lang.StringUtils.isBlank(content)) {
            return false;
        }

        SmsPojo pojo = new SmsPojo(mobile, content, null, null, messageType);
        boolean bFlag = taskProducer.produceDeliverNoticeSmsMsg(pojo);
        logger.info("短信发送【手机号 = {}, 内容 = {}】返回{}.", mobile, content, bFlag ? "成功" : "失败");

        return bFlag;
    }

    private boolean isSendMsg() {
        //从数据库的global_config表中得到config_key对应的value值，值不为"OK"则不进行检测验证码，返回
        String config_value = globalConfigService.get(sendMsgKey);

        //为了防止误删，当数据库不存在key时，一样需要发送短信
        return config_value == null || sendMsgValue.equals(config_value);
    }
}

