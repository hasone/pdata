package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.MdrcBillResp;
import com.cmcc.vrp.ec.bean.MdrcBillRespData;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
* <p>Title: MdrcBillController</p>
* <p>Description: 海南流量卡对账</p>
* @author lgk8023
* @date 2017年2月15日 上午11:30:04
*/
@Controller
public class MdrcBillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static XStream xStream;
    @Autowired
    private EnterprisesService enterprisesService;
    
    @Autowired
    CardNumAndPwdService cardNumAndPwdService;
    
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    static {
        xStream = new XStream();
        xStream.alias("Response", MdrcBillResp.class);
        xStream.autodetectAnnotations(true);
    }
    
    /**
     * @param startTime
     * @param endTime
     * @param request
     * @param response
     */
    @RequestMapping(value = "/bills", method = RequestMethod.GET)
    public void bills(String startTime, String endTime,
            HttpServletRequest request, HttpServletResponse response) {
        
        DateTime begin;
        DateTime end;
        String type = request.getContentType();
        if(StringUtils.isBlank(type)){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return; 
        }
        
        if (!(type.indexOf("application/xml") != -1 || type.indexOf("text/xml") != -1)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        
        //根据appKey获取相应的企业信息
        String appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR);

        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
        if (enterprise == null
                || enterprise.getDeleteFlag() != 0
                || enterprise.getInterfaceFlag() != 1) {
            LOGGER.error("企业不存在、企业暂停或接口关闭. appKey = {}", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }       
        
        //校验并获取相应的请求参数
        if (StringUtils.isBlank(startTime)
                || StringUtils.isBlank(endTime)) { 
            LOGGER.error("无效的请求参数. StartTime = {}， EndTime = {}.", startTime, endTime);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            begin = parse(decode(startTime));
            if ((end = parse(decode(endTime))).isAfterNow()){
                SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
                String requestTime = sdf.format(new Date());
                end = parse(requestTime);
            }
            LOGGER.info("begin = {}, end = {}", begin, end);
        } catch (Exception e) {
            LOGGER.error("解码时间出错, appkey = {}, e = {}", appKey, e.getMessage());
            response.setStatus(422);
            return;
        }
        
        if (!validate(begin, end)) {
            LOGGER.error("请求参数错误.StartTime = {}， EndTime = {}.", startTime, endTime);
            response.setStatus(422);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = sdf.format(new Date()) + ".txt";
        List<MdrcCardInfo> mdrcCardInfos = cardNumAndPwdService.getBill(begin, end);
        List<MdrcBillRespData> mdrcBillRespDatas = new ArrayList<MdrcBillRespData>();
        for (MdrcCardInfo mdrcCardInfo:mdrcCardInfos) {
            MdrcBillRespData mdrcBillRespData = new MdrcBillRespData();
            mdrcBillRespData.setCardNum(mdrcCardInfo.getCardNumber());
            mdrcBillRespData.setStatus(String.valueOf(mdrcCardInfo.getChargeStatus()));
            mdrcBillRespData.setMessage(mdrcCardInfo.getChargeMsg());
            mdrcBillRespData.setTransId(mdrcCardInfo.getRequestSerialNumber());
            mdrcBillRespData.setTransIdBoss(mdrcCardInfo.getReponseSerialNumber());
            
            mdrcBillRespDatas.add(mdrcBillRespData);
        }

        List<String> title = new ArrayList<String>();
        title.add("卡号");
        title.add("状态码");
        title.add("状态信息");
        title.add("平台方流水号");
        title.add("BOSS方流水号");
        List<String> rowList = new ArrayList<String>();
        for (MdrcBillRespData mdrcBillRespData : mdrcBillRespDatas) {
            rowList.add(mdrcBillRespData.getCardNum());

            if (mdrcBillRespData.getStatus() != null) {
                rowList.add(mdrcBillRespData.getStatus());
            } else {
                rowList.add("");
            }
            if (mdrcBillRespData.getMessage() != null) {
                rowList.add(mdrcBillRespData.getMessage());
            } else {
                rowList.add("");
            }
            if (mdrcBillRespData.getTransId() != null) {
                rowList.add(mdrcBillRespData.getTransId());
            } else {
                rowList.add("");
            }
            if (mdrcBillRespData.getTransIdBoss() != null) {
                rowList.add(mdrcBillRespData.getTransIdBoss());
            } else {
                rowList.add("");
            }
        }
        InputStream inputStream;
        try {
            
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            FTPUtil.uploadFile(getHostName(), NumberUtils.toInt(getPort()), getUserName(),
                    getPassWord(), "/", fileName, inputStream);
        } catch (Exception e1) {
            LOGGER.error("响应出错，错误信息为{}.", e1.toString());
        }  
        try {
            StreamUtils.copy(fileName, Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应出错，错误信息为{}.", e.toString());
        }
    }

    private String decode(String time) {
        return new String(Base64.decodeBase64(time.getBytes(Charsets.UTF_8)));
    }
    private DateTime parse(String time) {
        //先进行正则匹配!不要问我为什么!测试组要求的!
        if (!match(time)) {
            return null;
        }
        //测试要求，日期时间可以不带0，也可以只有日期没有时间，但是时区必须是规范的时区
        if (!matchTimeZone(time)) {
            return null;
        }
        DateTime dateTime;
        try {
            dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(time);
        } catch (Exception e) {
            dateTime = null;
        }
        return dateTime;
    }


    //正则匹配: yyyy-MM-ddThh:mm:ss+08:00 可无0
    private boolean match(String time) {
        final String REGEXP = "^[0-9]{4}-(0?[0-9]|1[0-2])-(0?[0-9]|[1-3][0-9])(T(0?[0-9]|[1-5][0-9]):"
                + "(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])(.[0-9]{3})?(\\+|\\-)[0-9]{2}:[0-9]{2})?$";
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    //要求校验市区，-12:00~+12:00
    private boolean matchTimeZone(String time) {
        int index1 = time.indexOf("+");
        String[] strs = time.split("-");
        int index2 = time.lastIndexOf("-");
        if (index1 > 0) {
            final String REGEXP = "^((\\+|\\-)(0[0-9]{1}|1[0-2]{1}):[0-9]{2})?$";
            Pattern pattern = Pattern.compile(REGEXP);
            Matcher matcher = pattern.matcher(time.substring(index1));
            return matcher.matches();
        } else if (strs.length == 4 && index2 > 0) {
            final String REGEXP = "^((\\+|\\-)(0[0-9]{1}|1[0-2]{1}):[0-9]{2})?$";
            Pattern pattern = Pattern.compile(REGEXP);
            Matcher matcher = pattern.matcher(time.substring(index2));
            return matcher.matches();
        } else {
            return true;
        }
    }

    private boolean validate(DateTime begin, DateTime end) {
        return begin != null
                && end != null
                && begin.isBeforeNow()
                && end.isBeforeNow();
    }
    private String getHostName() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_HOSTNAME.getKey());
    }
    
    private String getPort() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_PORT.getKey());
    }
    
    private String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_USERNAME.getKey());
    }
    
    private String getPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_PASSWORD.getKey());
    }
}
