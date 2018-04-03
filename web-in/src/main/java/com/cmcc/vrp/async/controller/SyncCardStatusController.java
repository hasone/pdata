package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.SyncCardStatusReq;
import com.cmcc.vrp.ec.bean.SyncCardStatusReqData;
import com.cmcc.vrp.ec.bean.SyncCardStatusResp;
import com.cmcc.vrp.ec.bean.SyncCardStatusRespData;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
* <p>Title: </p>
* <p>Description: 同步卡状态（海南）</p>
* @author lgk8023
* @date 2017年1月10日 下午4:06:14
*/
@Controller
public class SyncCardStatusController {
    private static final Logger logger = LoggerFactory.getLogger(SyncCardStatusController.class);
    private XStream xStream;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    CardNumAndPwdService cardNumAndPwdService;

    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    
    @Autowired
    GlobalConfigService globalConfigService;

    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", SyncCardStatusReq.class);
        xStream.alias("Response", SyncCardStatusResp.class);
        xStream.autodetectAnnotations(true);
    }
    
    /**
     * 
     */
    @RequestMapping(value = "sync_card_status", method = RequestMethod.POST)
    @ResponseBody
    public void syncCardStatus(HttpServletRequest request, HttpServletResponse response) {
        String appKey = null;
        SyncCardStatusReq req = null;
        Enterprise enterprise = null;;
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
                || (req = retrieveReq(request)) == null
                || !validate(req)
                || (enterprise = enterprisesService.selectByAppKey(appKey)) == null
                || enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
            logger.error("无效的同步请求参数, AppKey = {}, Enterprise = {}.", appKey, enterprise == null ? "" : JSONObject.toJSONString(enterprise));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SyncCardStatusReqData syncCardStatusReqData = req.getSyncCardStatusReqData();
        Integer flag = 0;  //0: 按时间段查询，1：按批次号查询
        if(StringUtils.isBlank(syncCardStatusReqData.getStartTime())
                || StringUtils.isBlank(syncCardStatusReqData.getEndTime())) {
            flag = 1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = sdf.format(new Date()) + ".txt";
        if (flag == 0) {
            String startTime = syncCardStatusReqData.getStartTime();
            String endTime = syncCardStatusReqData.getEndTime();          
            String type = syncCardStatusReqData.getType();
            Integer typeFlag = buildTypeFlag(type);
            DateTime start = parse(startTime);
            DateTime end = parse(endTime);
            List<MdrcCardInfo> mdrcCardInfos = getMdrcCardInfos(start, end, typeFlag);
            List<SyncCardStatusRespData> syncCardStatusRespDatas = new ArrayList<SyncCardStatusRespData>();
            for (MdrcCardInfo mdrcCardInfo:mdrcCardInfos) {
                SyncCardStatusRespData syncCardStatusRespData = new SyncCardStatusRespData();
                syncCardStatusRespData.setCardNumber(mdrcCardInfo.getCardNumber());
                syncCardStatusRespData.setSyncCode(mdrcCardInfo.getStatus().toString());
                for (MdrcCardStatus item : MdrcCardStatus.values()) {
                    if (item.getCode().equals(mdrcCardInfo.getStatus())) {
                        syncCardStatusRespData.setSyncInfo(item.getMessage());
                    }
                }
                syncCardStatusRespDatas.add(syncCardStatusRespData);
            }

            List<String> title = new ArrayList<String>();
            title.add("卡号");
            title.add("状态码");
            title.add("状态信息");
            List<String> rowList = new ArrayList<String>();
            for (SyncCardStatusRespData syncCardStatusRespData : syncCardStatusRespDatas) {
                rowList.add(syncCardStatusRespData.getCardNumber());

                if (syncCardStatusRespData.getSyncCode() != null) {
                    rowList.add(syncCardStatusRespData.getSyncCode());
                    rowList.add(syncCardStatusRespData.getSyncInfo());
                } else {
                    rowList.add("");
                    rowList.add("");
                }
            }
            InputStream inputStream;
            try {
                inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
                FTPUtil.uploadFile(getHostName(), NumberUtils.toInt(getPort()), getUserName(),
                        getPassWord(), "/", fileName, inputStream);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            String serialNum = syncCardStatusReqData.getSerialNumber();
            String year = syncCardStatusReqData.getYear();
            MdrcBatchConfig mBatchConfig = null;
            if (!com.cmcc.vrp.util.StringUtils.isEmpty(serialNum)
                    && !com.cmcc.vrp.util.StringUtils.isEmpty(year)) {
                mBatchConfig = mdrcBatchConfigService.selectBySerialNum(serialNum, year);
            } 
            if (mBatchConfig != null) {
                List<MdrcCardInfo> mdrcCardInfos = cardNumAndPwdService.getBySerialNum(mBatchConfig.getId(), mBatchConfig.getThisYear());
                List<SyncCardStatusRespData> syncCardStatusRespDatas = new ArrayList<SyncCardStatusRespData>();
                for (MdrcCardInfo mdrcCardInfo:mdrcCardInfos) {
                    SyncCardStatusRespData syncCardStatusRespData = new SyncCardStatusRespData();
                    syncCardStatusRespData.setCardNumber(mdrcCardInfo.getCardNumber());
                    syncCardStatusRespData.setSyncCode(mdrcCardInfo.getStatus().toString());
                    for (MdrcCardStatus item : MdrcCardStatus.values()) {
                        if (item.getCode().equals(mdrcCardInfo.getStatus())) {
                            syncCardStatusRespData.setSyncInfo(item.getMessage());
                        }
                    }
                    syncCardStatusRespDatas.add(syncCardStatusRespData);
                }
                List<String> title = new ArrayList<String>();
                title.add("卡号");
                title.add("状态码");
                title.add("状态信息");
                List<String> rowList = new ArrayList<String>();
                for (SyncCardStatusRespData syncCardStatusRespData : syncCardStatusRespDatas) {
                    rowList.add(syncCardStatusRespData.getCardNumber());

                    if (syncCardStatusRespData.getSyncCode() != null) {
                        rowList.add(syncCardStatusRespData.getSyncCode());
                        rowList.add(syncCardStatusRespData.getSyncInfo());
                    } else {
                        rowList.add("");
                        rowList.add("");
                    }
                }
                InputStream inputStream;
                try {
                    inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
                    FTPUtil.uploadFile(getHostName(), NumberUtils.toInt(getPort()), getUserName(),
                            getPassWord(), "/", fileName, inputStream);
                } catch (Exception e1) {
                    logger.error("响应出错，错误信息为{}.", e1.toString());
                }  
            }             
        }
        try {
            StreamUtils.copy(fileName, Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应出错，错误信息为{}.", e.toString());
        }
    }
    
    private List<MdrcCardInfo> getMdrcCardInfos(DateTime start, DateTime end, Integer typeFlag) {
        List<MdrcCardInfo> mdrcCardInfos = new ArrayList<MdrcCardInfo>();
        if (typeFlag == 1) {
            mdrcCardInfos = cardNumAndPwdService.getBycreateTime(start, end);
        } else if (typeFlag == 2) {
            mdrcCardInfos = cardNumAndPwdService.getByStoredTime(start, end);
        } else if (typeFlag == 3) {
            mdrcCardInfos = cardNumAndPwdService.getByActivatedTime(start, end);
        } else if (typeFlag == 4) {
            mdrcCardInfos = cardNumAndPwdService.getByBoundTime(start, end);
        } else if (typeFlag == 5) {
            mdrcCardInfos = cardNumAndPwdService.getByUsedTime(start, end);
        } else if (typeFlag == 6) {
            mdrcCardInfos = cardNumAndPwdService.getByLockedTime(start, end);
        } else if (typeFlag == 7) {
            mdrcCardInfos = cardNumAndPwdService.getByDeactivateTime(start, end);
        } else if (typeFlag == 8) {
            mdrcCardInfos = cardNumAndPwdService.getByUnlockTime(start, end);
        }
        return mdrcCardInfos;
    }

    private Integer buildTypeFlag(String type) {
        Integer flag = 1;
        if (StringUtils.isBlank(type)) {
            flag = 1;
        } else if ("1".equals(type)) {
            flag = 1;
        } else if ("2".equals(type)) {
            flag = 2;
        } else if ("3".equals(type)) {
            flag = 3;
        } else if ("4".equals(type)) {
            flag = 4;
        } else if ("5".equals(type)) {
            flag = 5;
        } else if ("6".equals(type)) {
            flag = 6;
        } else if ("7".equals(type)) {
            flag = 7;
        } else if ("8".equals(type)) {
            flag = 8;
        }
        return flag;
    }

    private SyncCardStatusReq retrieveReq(HttpServletRequest request) {
        try {
            String syncCardStatusReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (SyncCardStatusReq) xStream.fromXML(syncCardStatusReqStr);
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean validate(SyncCardStatusReq req) {
        return req != null
            && (req.getSyncCardStatusReqData()) != null;
    }  

    private DateTime parse(String time) {

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
        final String REGEXP = "^[0-9]{4}-(0?[0-9]|1[0-2])-(0?[0-9]|[1-3][0-9]) ";
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
    
    private String getHostName() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_HOSTNAME.getKey());
        //return "192.168.170.190";
    }
    
    private String getPort() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_PORT.getKey());
        //return "21";
    }
    
    private String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_USERNAME.getKey());
        //return "lgk8023";
    }
    
    private String getPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.HAINAN_FTP_PASSWORD.getKey());
        //return "lgk8023";
    }
}
