package com.cmcc.vrp.wx.impl;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.wx.UserCheckService;
import com.cmcc.vrp.wx.beans.CheckMobileReq;
import com.cmcc.vrp.wx.beans.CheckMobileResp;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 手机号码资格验证
 * Created by leelyn on 2017/1/3.
 */
@Service
public class UserCheckServiceImpl implements UserCheckService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCheckServiceImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EnterprisesService enterprisesService;

    @Override
    public boolean checkMobile(String mobile, Long entId) {
        Enterprise enterprise;
        if (StringUtils.isBlank(mobile)
                || entId == null
                || (enterprise = enterprisesService.selectById(entId)) == null) {
            LOGGER.error("参数缺失");
            return false;
        }
        CheckMobileReq req = buildReq(mobile, enterprise.getCode());
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("CheckMobileReq", CheckMobileReq.class);

        String result = HttpUtils.post(getCheckUrl(), xStream.toXML(req) , "application/xml");
        if (StringUtils.isBlank(result)) {
            LOGGER.error("请求返回为空");
            return false;
        }
        xStream.alias("CheckMobileResp", CheckMobileResp.class);
        CheckMobileResp resp;
        if ((resp = (CheckMobileResp) xStream.fromXML(result)) == null) {
            LOGGER.error("解析错误,解析对象:{}", result);
            return false;
        }
        if (resp.getResult() == null
                || resp.getResult() != 0) {
            LOGGER.error("手机号码资格验证失败");
            return false;
        }
        return true;
    }

    /**
     * 构建请求包体
     *
     * @param mobile
     * @param ecCode
     * @return
     */
    private CheckMobileReq buildReq(String mobile, String ecCode) {
        CheckMobileReq req = new CheckMobileReq();
        req.setEcCode(ecCode);
        req.setMobile(mobile);
        return req;
    }

    /**
     * 获取地址
     *
     * @return
     */
    private String getCheckUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHECK_URL.getKey());
    }
}
