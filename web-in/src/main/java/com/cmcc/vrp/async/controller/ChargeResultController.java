package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudChargeQueryService;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.QueryItem;
import com.cmcc.vrp.ec.bean.QueryResp;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @ClassName: ChargeResultController 
 * @Description: 根据用户充值请求序列号查询充值结果
 * @author: Rowe
 * @date: 2016年11月18日 下午3:38:00
 */
@Controller
@RequestMapping(value = "chargeResult")
public class ChargeResultController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeResultController.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ChargeRecordService chargeRecordService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    SdCloudChargeQueryService sdChargeRecordQueryService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @param systemNun
     * @param request
     * @param response
     */
    @RequestMapping(value = "{serialNum}", method = RequestMethod.GET)
    @ResponseBody
    public void query(@PathVariable String serialNum, HttpServletRequest request, HttpServletResponse response) {
        QueryResp resp;
        Enterprise enterprise = null;
        String appKey = null;

        //校验认证返回的参数,返回appKey,否则认为认证失败，返回403
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))) {
            LOGGER.error("查询充值结果接口认证未通过, AppKey = {}.", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //参数非法
        if (StringUtils.isBlank(serialNum) || (enterprise = enterprisesService.selectByAppKey(appKey)) == null
                || enterprise.getDeleteFlag().intValue() != DELETE_FLAG.UNDELETED.getValue()) {
            LOGGER.error("查询充值结果接口参数非法, SerialNum = {}, AppKey = {}, Enterprise = {}.", serialNum, appKey,
                    enterprise == null ? "" : JSONObject.toJSONString(enterprise));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<ChargeRecord> chargeRecordList;
        if(isSdEnvironment()){
            chargeRecordList = sdChargeRecordQueryService.queryStatusBySerialNum(enterprise.getId(), serialNum);
        }else{
            chargeRecordList = chargeRecordService.selectRecordByEnterIdAndSerialNum(enterprise.getId(), serialNum);
        }
        
//        resp = buildResp(chargeRecordQueryService.queryStatusBySerialNum(enterprise.getId(), serialNum));
       // resp = buildResp(chargeRecordService.selectRecordByEnterIdAndSerialNum(enterprise.getId(), serialNum));
        resp = buildResp(chargeRecordList);
        writeResp(resp, response);
    }

    /**
     * 
     * @Title: writeResp 
     * @Description: 输出响应信息
     * @param resp
     * @param response
     * @return: void
     */
    private void writeResp(QueryResp resp, HttpServletResponse response) {
        XStream xStream = new XStream();
        xStream.alias("Response", QueryResp.class);
        xStream.autodetectAnnotations(true);
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应充值请求记录时出错，错误信息为{}.", e.toString());
        }
    }

    /**
     * 
     * @Title: buildResp 
     * @Description: 构造响应报文
     * @param list
     * @return
     * @return: QueryResp
     */
    private QueryResp buildResp(List<ChargeRecord> list) {
        QueryResp resp = new QueryResp();
        List<QueryItem> items = new ArrayList<QueryItem>();
        for (ChargeRecord record : list) {
            QueryItem item = new QueryItem();
            item.setEnterpriseId(record.getEnterId());
            item.setProductId(record.getPrdId());
            item.setMobile(record.getPhone());
            item.setStatus(record.getStatus());
            item.setDescription(record.getErrorMessage() == null ? ChargeRecordStatus.fromValue(record.getStatus())
                    .getMessage() : record.getErrorMessage());
            item.setChargeTime(record.getChargeTime() == null ? null : dateFormat.format(record.getChargeTime()));
            items.add(item);
        }
        resp.setItems(items);
        resp.setResponseTime(DateUtil.getRespTime());
        return resp;
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
