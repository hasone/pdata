package com.cmcc.vrp.async.controller;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.statement.FingerprintStat;
import com.cmcc.vrp.province.model.statement.TotalStat;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunyiwei on 2016/9/19.
 */
@Controller
public class StatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("Response", TotalStat.class);
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private ChargeRecordService chargeRecordService;
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * @param startTime
     * @param endTime
     * @param fingerprint
     * @param request
     * @param response
     */
    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    public void get(String startTime, String endTime, String fingerprint,
                    HttpServletRequest request, HttpServletResponse response) {
        DateTime begin;
        DateTime end;
        
        if (needCheck()) {
            String type = request.getContentType();
            if(StringUtils.isBlank(type)){
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return; 
            }
            
            String contentType = request.getContentType().split(";")[0];
            if (!("application/xml".equals(contentType)
                    || "text/xml".equals(contentType))) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
        }
        
        //根据appKey获取相应的企业信息
        String appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR);

        boolean needFilter = StringUtils.isNotBlank((String) request.getAttribute(Constants.FINGERPRINT));

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
            LOGGER.error("无效的请求参数. StartTime = {}， EndTime = {}， FingerPrint = {}.", startTime, endTime, fingerprint);
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
            LOGGER.error("请求参数错误.StartTime = {}， EndTime = {}， FingerPrint = {}.", startTime, endTime, fingerprint);
            response.setStatus(422);
            return;
        }

        boolean needDetails = (fingerprint != null && !needFilter && 
                (enterprise.getCode().equals(getShuangchuangCode()) || enterprise.getCode().equals(getJiaKaiCode())));
        List<FingerprintStat> stats = chargeRecordService.statement(begin, end, enterprise.getId());
        if (stats == null) {
            LOGGER.error("stats为空. StartTime = {}， EndTime = {}， FingerPrint = {}.", startTime, endTime, fingerprint);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //如果头信息中有fingerprint的信息，代表当前企业是某平台中的企业，不是流量平台的企业！
        if (needFilter) {
            stats = filter(stats, (String) request.getAttribute(Constants.FINGERPRINT));
        }

        try {
            StreamUtils.copy(buildResp(stats, needDetails), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("输出报表响应信息时出错，错误信息为{}.", e.getStackTrace());
        }
    }

    //过滤
    private List<FingerprintStat> filter(List<FingerprintStat> stats, String fingerprint) {
        List<FingerprintStat> newStats = new LinkedList<FingerprintStat>();
        if (stats != null && fingerprint != null) {
            for (FingerprintStat newStat : stats) {
                if (fingerprint.equals(newStat.getName())) {
                    newStats.add(newStat);
                }
            }
        }

        return newStats;
    }

    private String buildResp(List<FingerprintStat> stats, boolean needDetails) {
        TotalStat totalStat = new TotalStat();
        totalStat.setTotalCount(sumCount(stats));
        totalStat.setTotalFlowCount(sumFlowCount(stats));
        totalStat.setTotalPrice(sumPrice(stats));

        if (needDetails) {
            totalStat.setFingerprintStatList(stats);
        }

        return xStream.toXML(totalStat);
    }

    private int sumCount(List<FingerprintStat> stats) {
        int sum = 0;
        for (FingerprintStat stat : stats) {
            sum += stat.getCount();
        }

        return sum;
    }

    private float sumPrice(List<FingerprintStat> stats) {
        float sum = 0f;
        for (FingerprintStat stat : stats) {
            sum += stat.getPrice();
        }

        return sum;
    }

    private float sumFlowCount(List<FingerprintStat> stats) {
        float sum = 0f;
        for (FingerprintStat stat : stats) {
            sum += stat.getFlowCount();
        }

        return sum;
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

    //正则匹配: yyyy-MM-ddThh:mm:ss+08:00
   /* private boolean match(String time) {
        final String REGEXP = "^[0-9]{4}-[0-9]{2}-[0-9]{2}(T[0-9]{2}:[0-9]{2}:[0-9]{2}(.[0-9]{2})?(\\+|\\-)[0-9]{2}:[0-9]{2})?$";
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }*/

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
                && end.isBeforeNow()
                && !begin.plusMonths(1).isBefore(end);
    }

    private String getShuangchuangCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHUANCHANG_CODE.getKey());
    }
    private String getJiaKaiCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.JIAKAI_CODE.getKey());
    }
    private boolean needCheck() {
        
        String checkFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_NEED_CHECK.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(checkFlag) ? "false" : checkFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}
