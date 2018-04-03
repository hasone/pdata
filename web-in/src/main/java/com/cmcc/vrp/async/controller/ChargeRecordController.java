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
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/5/24.
 */
@Controller
@RequestMapping(value = "chargeRecords")
public class ChargeRecordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeRecordController.class);

    @Autowired
    ChargeRecordService recordService;

    @Autowired
    EnterprisesService enterprisesService;
    
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
    @RequestMapping(value = "{systemNun}", method = RequestMethod.GET)
    @ResponseBody
    public void query(@PathVariable String systemNun, HttpServletRequest request, HttpServletResponse response) {
       
        ChargeRecord record;
        QueryResp resp;
        Enterprise enterprise = null;
        String appKey = null;
        
        if (needCheck()) {
            String type = request.getContentType();
            if (StringUtils.isBlank(type)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }

            String contentType = request.getContentType();
            if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }  
        }
        
        if (StringUtils.isBlank(systemNun)
                || StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (systemNun.length() > 50) {
            response.setStatus(422);
            return;
        }
        if ((enterprise = enterprisesService.selectByAppKey(appKey)) == null || enterprise.getDeleteFlag() != 0
                || enterprise.getInterfaceFlag() != 1) {
            LOGGER.error("企业不存在、企业暂停或接口关闭. appKey = {}", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if(isSdEnvironment()){
            record = sdChargeRecordQueryService.queryStatusBySystemNun(systemNun);
        }else{
            record = recordService.getRecordBySN(systemNun); 
        }
        
//        if ((record = chargeRecordQueryService.queryStatusBySystemNun(systemNun)) == null) {
        if (record == null) { 
            LOGGER.error("记录不存在. SystemNum = {}", systemNun);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!record.getEnterId().equals(enterprise.getId())) { //记录的归属企业与发起查询的企业不符合
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp = buildResp(record);
        writeResp(resp, response);
    }

    //输出响应信息
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

    private QueryResp buildResp(ChargeRecord record) {
        QueryResp resp = new QueryResp();
        List<QueryItem> items = new ArrayList<QueryItem>();
        QueryItem item = new QueryItem();
        item.setEnterpriseId(record.getEnterId());
        item.setProductId(record.getPrdId());
        item.setMobile(record.getPhone());
        item.setStatus(record.getStatus());
        item.setDescription(record.getErrorMessage() == null ? ChargeRecordStatus.fromValue(record.getStatus())
                .getMessage() : record.getErrorMessage());
        item.setChargeTime(record.getChargeTime() == null ? null : dateFormat.format(record.getChargeTime()));
        items.add(item);
        resp.setItems(items);
        resp.setResponseTime(DateUtil.getRespTime());
        return resp;
    }

    private QueryResp buildResp(List<ChargeRecord> list) {
        QueryResp resp = new QueryResp();
        List<QueryItem> items = new ArrayList<QueryItem>();
        for (ChargeRecord record : list) {
            QueryItem item = new QueryItem();
            item.setEnterpriseId(record.getEnterId());
            item.setProductId(record.getPrdId());
            item.setMobile(record.getPhone());
            item.setStatus(record.getStatus());
            item.setDescription(record.getErrorMessage());
            item.setChargeTime(DateUtil.date2ISO8601Time(record.getChargeTime()));
            items.add(item);
        }
        resp.setItems(items);
        resp.setResponseTime(DateUtil.getRespTime());
        return resp;
    }
    private boolean needCheck() {
        
        String checkFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_NEED_CHECK.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(checkFlag) ? "false" : checkFlag;
        return Boolean.parseBoolean(finalFlag);
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
