package com.cmcc.vrp.sms.opt;

import com.google.gson.Gson;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.sms.opt.pojos.OptMsgQueryRequestPojo;
import com.cmcc.vrp.sms.opt.pojos.OptMsgQueryResponsePojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 开放平台短信查询服务
 *
 * 文档地址: http://www.openservice.com.cn/msg.action
 *
 * 登录账号: sunyiwei@cmhi.chinamobile.com 登录密码: MjIyYTg5OGM1N2FhMzlk
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptQueryMessageServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptQueryMessageServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 短信发送状态
     *
     * @param messageID 消息ID
     * @return 短信发送状态
     */
    public OptMsgQueryResponsePojo query(String messageID) {
        Gson gson = new Gson();

        //1. 校验
        if (org.apache.commons.lang.StringUtils.isBlank(messageID)) {
            LOGGER.error("无法查询空短信ID的发送状态.");
            return null;
        }

        //2. 构建请求对象
        OptMsgQueryRequestPojo omqrp = new OptMsgQueryRequestPojo();
        omqrp.setMessageId(messageID);

        //2. 发送请求
        String responseBody = sendInternal(omqrp);
        LOGGER.info("开放平台短信通道查询请求地址为{}, 请求内容为{}, 响应内容为{}.", getQueryAddr(), gson.toJson(omqrp), responseBody);

        //4. 解析应答
        return buildResp(responseBody);
    }

    //向开放平台侧发送短信请求, 返回响应内容
    private String sendInternal(OptMsgQueryRequestPojo pojo) {
        String requestUrl = getQueryAddr();
        return HttpUtils.post(requestUrl, new Gson().toJson(pojo), "application/json");
    }

    //将应答消息转化成响应对象
    private OptMsgQueryResponsePojo buildResp(String responseBody) {
        try {
            return new Gson().fromJson(responseBody, OptMsgQueryResponsePojo.class);
        } catch (Exception e) {
            LOGGER.error("短信状态查询应答内容转化成应答对象时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public String getQueryAddr() {
        return globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_QUERY_ADDR.getKey());
    }
}
