package com.cmcc.vrp.boss.sichuan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketResp;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketRespOutdata;
import com.cmcc.vrp.boss.sichuan.service.ScFlowRedPacketService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

/**
 * 四川流量红包服务
 * @author qihang
 *
 */
@Service("scFlowRedPacketService")
public class ScFlowRedPacketServiceImpl implements ScFlowRedPacketService {
    
    private static Logger logger =  LoggerFactory.getLogger(ScFlowRedPacketServiceImpl.class);
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(FlowRedPacketReq req) {
        String params = req.getReqParams();
        String sign = null;
        try {
            sign = Sign.sign(params, getPrivateKeyPath());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        params = params + "&sign=" + sign;
        return params;
    }

    @Override
    public FlowRedPacketResp sendRequest(FlowRedPacketReq req) {
        req.setAppKey(getAppkey());
        req.setLoginNo(getLoginNo());
        req.setUserName(getUserName());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        req.setTimeStamp(dateformat.format(new Date()));
        String requestStr = generateRequestString(req);
        logger.info("流量红包发送请求参数:" + requestStr);
        try {
            String result = HttpConnection.sendGetRequest(getReqUrl(), requestStr);
           
            logger.info("流量红包返回结果:{},请求地址：{},请求参数:{}" , result , getReqUrl() , requestStr);
            
            JSONObject json = (JSONObject) JSONObject.parse(result);
            String resCode = json.getString("resCode");
            String resMsg = json.getString("resMsg");

            FlowRedPacketResp response = new FlowRedPacketResp();
            response.setResCode(resCode);
            response.setResMsg(resMsg);
            if (response.getResCode().equals("0000000")) {
                String outDataStr = json.getString("outData");
                FlowRedPacketRespOutdata outData = JSONObject.parseObject(outDataStr, FlowRedPacketRespOutdata.class);
                response.setOutData(outData);
            }
            return response;
            
        }catch (Exception e) {
            logger.info("发送http请求错误,异常:" + e.getMessage());
            logger.error(e.toString());
        }
        return null;
    }


    @Override
    public String getReqUrl() {
        //TODO 请知道地址后在数据库中和pdata.sql中添加，sql语句如下
        //INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('四川BOSS流量红包的地址', '四川BOSS流量红包的地址', 'BOSS_SC_FLOWREDPACKET_URL', 'http://localhost:8080/web-in', NOW(), NOW(), '1', '1', '0', null);
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_FLOWREDPACKET_URL.getKey());
    }

    @Override
    public String getPrivateKeyPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey());
    }

    private String getAppkey() {
        //TODO 请知道地址后在数据库中和pdata.sql中添加，sql语句如下
        //INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('四川BOSS流量红包的地址', '四川BOSS流量红包的地址', 'BOSS_SC_FLOWREDPACKET_URL', 'http://localhost:8080/web-in', NOW(), NOW(), '1', '1', '0', null);
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
    }

    private String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
    }
    
    private String getLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
    }

}
