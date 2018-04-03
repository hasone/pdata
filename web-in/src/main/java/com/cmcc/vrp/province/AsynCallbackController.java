package com.cmcc.vrp.province;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.boss.bjym.enums.BjymChargeStateEnum;
import com.cmcc.vrp.boss.bjym.pojos.BjymCallbackRequest;
import com.cmcc.vrp.boss.bjym.pojos.BjymCallbackRequestData;
import com.cmcc.vrp.boss.bjym.pojos.BjymCallbackRequestMessage;
import com.cmcc.vrp.boss.bjym.pojos.BjymCallbackResponse;
import com.cmcc.vrp.boss.core.model.CoreCallbackReq;
import com.cmcc.vrp.boss.core.model.FailInfo;
import com.cmcc.vrp.boss.core.model.SuccInfo;
import com.cmcc.vrp.boss.heilongjiang.model.HLJBOSSChargeResponse;
import com.cmcc.vrp.boss.heilongjiang.model.HLJCallBackPojo;
import com.cmcc.vrp.boss.jsof.enums.JsofChargeStateEnum;
import com.cmcc.vrp.boss.jsof.model.JsofCallback;
import com.cmcc.vrp.boss.shangdong.boss.model.SdRespPojo;
import com.cmcc.vrp.boss.shyc.pojos.ShycCallbackReq;
import com.cmcc.vrp.boss.xiangshang.XsSignService;
import com.cmcc.vrp.boss.xiangshang.pojo.ErrCode;
import com.cmcc.vrp.boss.xiangshang.pojo.ResponsePojo;
import com.cmcc.vrp.boss.zhuowang.bean.OrderFailInfo;
import com.cmcc.vrp.boss.zhuowang.bean.OrderHandleResult;
import com.cmcc.vrp.boss.zhuowang.bean.ParseResultXml;
import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.ec.bean.CallBackReqData;
import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AsyncCallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.YqxChargeService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;

/**
 * Created by sunyiwei on 2016/7/6.
 */
@RequestMapping("/charge")
@Controller
public class AsynCallbackController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynCallbackController.class);
    private static final String SD_CLOUD_CODE = "99999";//山东云平台
    private static XStream sdXstream;
    private static XStream coreXstream;

    static {
        sdXstream = new XStream();
        sdXstream.alias("Request", CallBackReq.class);
        sdXstream.alias("Response", CallbackResp.class);
        sdXstream.autodetectAnnotations(true);

        coreXstream = new XStream();
        coreXstream.alias("Request", CoreCallbackReq.class);
        coreXstream.autodetectAnnotations(true);
    }

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    AsyncCallbackService asyncCallbackService;

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    XsSignService signService;

    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private YqxChargeService yqxChargeService;

    /**
     * core渠道的回调（包括网宿和unionflow)
     *
     * @param request 请求
     */
    @RequestMapping(value = "/core/callback", method = RequestMethod.POST)
    public void coreCallback(HttpServletRequest request) throws IOException {
        final String callbackReqStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
        LOGGER.info("从BOSS侧收到回调信息{}.", callbackReqStr);

        //获取core平台定义的回调对象
        final CoreCallbackReq callbackReq = (CoreCallbackReq) coreXstream.fromXML(callbackReqStr);

        //转换成平台定义的回调对象
        AsyncCallbackReq acr = convert(callbackReq);

        //处理回调
        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return new Gson().toJson(callbackReq);
            }

            //empty response
            @Override
            public void resp(boolean flag) {
            }
        });
    }

    /**
     * @param param
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping(value = "/jsof/callback", method = RequestMethod.POST)
    public void jsofCallback(HttpServletRequest request, final HttpServletResponse response) throws IOException {

        Map<String, String> map = getQueryParams(request);
        Gson gson = new Gson();
        final String jsonStr = gson.toJson(map);
        LOGGER.info("解析后的回调信息{}.", jsonStr);
        JsofCallback jsofCallback = gson.fromJson(jsonStr, JsofCallback.class);
        AsyncCallbackReq acr = convert(jsofCallback);
        //处理回调
        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return jsonStr;
            }
            @Override
            public void resp(boolean flag) {
                try {
                    String resp = bulidSdResp(flag);
                    LOGGER.info("异步回调响应报文：" + resp);
                    StreamUtils.copy(resp, Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e) {
                    LOGGER.error("响应回调请求时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                }
            }
        });
    }
    private AsyncCallbackReq convert(JsofCallback jsofCallback) {
        if (!validate(jsofCallback)) {
            LOGGER.error("校验江苏欧飞回调请求出错， 回调请求内容为{}.", jsofCallback == null ? "空" : new Gson().toJson(jsofCallback));
            return null;
        }

        ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(jsofCallback.getSporderId());
        if (chargeRecord == null) {
            LOGGER.error("根据回调流水号查询不到订单{}.", new Gson().toJson(jsofCallback));
            return null;
        }
        AsyncCallbackReq asyncCallbackReq = new AsyncCallbackReq();
        asyncCallbackReq.setSystemSerialNum(jsofCallback.getSporderId());
        asyncCallbackReq.setErrorMsg(jsofCallback.getErrMsg());
        asyncCallbackReq.setMobile(chargeRecord.getPhone());

        //设置充值状态
        JsofChargeStateEnum chargeStateEnum = JsofChargeStateEnum.fromCode(jsofCallback.getRetCode());
        asyncCallbackReq.setChargeRecordStatus(
                (chargeStateEnum != null && chargeStateEnum == JsofChargeStateEnum.SUCCESS)
                        ? ChargeRecordStatus.COMPLETE.getCode()
                        : ChargeRecordStatus.FAILED.getCode());

        return asyncCallbackReq;
    }

    private boolean validate(JsofCallback jsofCallback) {
        if (jsofCallback == null
                || StringUtils.isBlank(jsofCallback.getSporderId())
                || StringUtils.isBlank(jsofCallback.getRetCode())) {
            return false;
        }
        return true;
    }

    private Map<String, String> getQueryParams(HttpServletRequest request) {
        String retCode = request.getParameter("ret_code");
        String errMsg = request.getParameter("err_msg");
        String sporderId = request.getParameter("sporder_id");
        String ordersuccesstime = request.getParameter("ordersuccesstime");

        LOGGER.info("从江苏欧飞侧收到回调信息ret_code={},err_msg={},sporder_id={},ordersuccesstime={}.", retCode, errMsg, sporderId, ordersuccesstime);
        Map<String, String> params = new HashMap<String, String>();
        params.put("ret_code", retCode);
        params.put("err_msg", errMsg);
        params.put("sporder_id", sporderId);
        params.put("ordersuccesstime", ordersuccesstime);
        return params;
    }
    /**
     * 北京云漫的回调
     */
    @RequestMapping(value = "/bjym/callback", method = RequestMethod.POST)
    public void bjymCallback(String param, final HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("从北京云漫侧收到回调信息{}.", param);

        //获取北京云漫平台定义的回调对象
        final BjymCallbackRequest callbackReq = parseBjymCallbackPojo(param);

        //转换成平台定义的回调对象
        AsyncCallbackReq acr = convert(callbackReq);

        //处理回调
        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return new Gson().toJson(callbackReq);
            }

            @Override
            public void resp(boolean flag) {
                invokeResp(flag, httpServletResponse, new RespBuilder() {
                    @Override
                    public String build(CallbackResult callbackResult) {
                        if (callbackResult == CallbackResult.SUCCESS) {
                            return BjymCallbackResponse.SUCCESS.getMessage();
                        } else {
                            return BjymCallbackResponse.FAIL.getMessage();
                        }
                    }
                });
            }
        });
    }

    private String stripPrefix(String originStr) {
        int index = originStr.indexOf("{");
        return index == -1 ? null : originStr.substring(index);
    }


    /**
     * 上海月呈侧的回调
     */
    @RequestMapping(value = "/shyc/callback", method = RequestMethod.POST)
    public void shycCallback(Integer type, String orderno, String taskid, String code, String message,
                             String phone, String sign,
                             final HttpServletRequest httpServletRequest,
                             final HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("从上海月呈侧收到回调信息: type = {}, orderNo = {}, taskId = {}, code = {}, message = {}, phone = {}, sign = {}.",
                type, orderno, taskid, code, message, phone, sign);

        //获取core平台定义的回调对象
        final ShycCallbackReq callbackReq = parseShyc(type, orderno, taskid, phone, code, message, sign);

        //转换成平台定义的回调对象
        AsyncCallbackReq acr = convert(callbackReq);

        //处理回调
        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return new Gson().toJson(callbackReq);
            }

            @Override
            public void resp(boolean flag) {
                invokeResp(flag, httpServletResponse, new RespBuilder() {
                    @Override
                    public String build(CallbackResult callbackResult) {
                        if (CallbackResult.SUCCESS == callbackResult) {
                            return "success";
                        } else {
                            return "fail";
                        }
                    }
                });
            }
        });
    }

    /**
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     */
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    public void pltCallback(HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        //获取平台的回调对象
        final CallBackReq callBackReq = parse(httpServletRequest);
        LOGGER.info("从平台BOSS侧接收回调信息， 回调内容为{}.", callBackReq == null ? "空" : new Gson().toJson(callBackReq));

        //包装成平台的回调对象
        AsyncCallbackReq acr = convert(callBackReq);

        //处理回调
        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return new Gson().toJson(callBackReq);
            }

            @Override
            public void resp(boolean flag) {
                invokeResp(flag, httpServletResponse, new RespBuilder() {
                    @Override
                    public String build(CallbackResult callbackResult) {
                        return buildCommonRespStr(callbackResult.getCode(), callbackResult.getMessage());
                    }
                });
            }
        });
    }

    //通用的响应内容
    private void invokeResp(boolean flag, HttpServletResponse httpServletResponse, RespBuilder respBuilder) {
        //响应
        try {
            if (flag) {
                StreamUtils.copy(respBuilder.build(CallbackResult.SUCCESS), Charsets.UTF_8, httpServletResponse.getOutputStream());
            } else {
                StreamUtils.copy(respBuilder.build(CallbackResult.OTHERS), Charsets.UTF_8, httpServletResponse.getOutputStream());
            }
        } catch (Exception e) {
            LOGGER.error("响应回调时出错，错误信息为{}.", e.getMessage());
        }
    }

    /**
     * 接受卓望接口的回调信息
     *
     * @param req 回调请求
     * @param res 回调响应
     * @throws Exception 异常信息
     */
    @RequestMapping(value = "/zw/callback", method = RequestMethod.POST)
    public void zwCallback(HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String xmlhead = req.getParameter("xmlhead");
        final String xmlbody = req.getParameter("xmlbody");
        LOGGER.info("接收自服务平台异步报文头：" + xmlhead);
        LOGGER.info("接收自服务平台异步报文体：" + xmlbody);

        //解析
        OrderHandleResult result = ParseResultXml.parseHandleResultXml(xmlbody);

        //包装成平台的回调对象
        AsyncCallbackReq acr = convert(result);

        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return String.format("XmlHead = %s, XmlBody = %s.%n", xmlhead, xmlbody);
            }

            @Override
            public void resp(boolean flag) {
                //发送响应内容
                buildZwResp(xmlhead, res, flag);
            }
        });
    }

    /**
     * 接收向上公司的回调接口
     *
     * @param responsePojo 向上响应对象
     * @param res          响应对象
     */
    @RequestMapping(value = "/xs/callback", method = RequestMethod.GET)
    public void xsCallback(final ResponsePojo responsePojo, final HttpServletResponse res) {
        LOGGER.info("收到向上渠道的回调请求，请求内容为{}.", new Gson().toJson(responsePojo));

        //包装成平台的回调对象
        AsyncCallbackReq acr = convert(responsePojo);

        process(acr, new Operator() {
            @Override
            public String getErrorInfo() {
                return String.format("ResponsePojo = %s.%n", new Gson().toJson(responsePojo));
            }

            @Override
            public void resp(boolean flag) {
                if (flag) {
                    try {
                        StreamUtils.copy("true", Charsets.UTF_8, res.getOutputStream());
                    } catch (IOException e) {
                        LOGGER.error("响应回调请求时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                    }
                }
            }
        });
    }

    /**
     * 山东云平台的回调接口,先按照post的标准做,暂时不加任何的限制 当前先不开放,1.16日山东上线版本测试时打开
     */
    @RequestMapping(value = "/sd/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sdCallback(HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String callbackReqStr = (String) req.getAttribute(Constants.BODY_XML_ATTR);//参数获取，JSON格式字符串
        try {
            String appKey = null;//企业APPKEY
            String systemSerialNum = null;//系统流水号

            //校验认证返回的参数,返回appKey和 systemSerialNum,否则认为认证失败，返回403
            if (StringUtils.isBlank(appKey = (String) req.getAttribute(Constants.APP_KEY_ATTR))
                    || StringUtils.isBlank(systemSerialNum = (String) req.getAttribute(Constants.SYSTEM_NUM_ATTR))) {
                LOGGER.error("山东云平台推送异步回调消息认证未通过, AppKey = {}, SystemSerialNum = {}.", appKey, systemSerialNum);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            //校验是否是山东云平台发送的请求
            Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
            if (enterprise == null || DELETE_FLAG.UNDELETED.getValue() != enterprise.getDeleteFlag().intValue()
                    || !SD_CLOUD_CODE.equalsIgnoreCase(enterprise.getCode())) {
                LOGGER.error("非山东云平台推送异步回调消息,约定只能使用山东云平台【code = " + SD_CLOUD_CODE + "】推送异步回调消息.");
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            LOGGER.info("山东流量平台收到云平台异步回调报文：" + callbackReqStr);

            final SdRespPojo pojo = new Gson().fromJson(callbackReqStr, SdRespPojo.class);

            AsyncCallbackReq acr = convert(pojo);

            if (acr == null) {
                LOGGER.error("boss返回的流水号在数据库没有找到相应的记录，数据为{}.", callbackReqStr);
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            //处理回调
            process(acr, new Operator() {
                @Override
                public String getErrorInfo() {
                    return pojo.getMsg();
                }

                //empty response
                @Override
                public void resp(boolean flag) {
                    try {
                        String resp = bulidSdResp(flag);
                        LOGGER.info("山东流量平台异步回调请求报文：" + callbackReqStr + ",响应报文：" + resp);
                        StreamUtils.copy(resp, Charsets.UTF_8, res.getOutputStream());
                    } catch (IOException e) {
                        LOGGER.error("响应回调请求时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                    }

                }
            });

        } catch (JsonSyntaxException e) {
            LOGGER.error("boss返回的数据无法解析，数据为{},异常信息为{}.", callbackReqStr, e.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    //构建山东异步回调响应报文
    private String bulidSdResp(boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String requestTime = sdf.format(new Date());
        if (flag) {
            return "<Response><Datetime>" + requestTime + "</Datetime><Code>10000</Code><Message>成功</Message></Response>";
        } else {
            return "<Response><Datetime>" + requestTime + "</Datetime><Code>9998</Code><Message>失败</Message></Response>";
        }
    }

    //构建卓望回调的响应，并发送给全国接口的应答
    private void buildZwResp(String xmlHead, HttpServletResponse res, boolean flag) {
        StringBuilder xmlbodyBuff = new StringBuilder();

        //修改xmlhead中的ActionCode=1
        String xmlheadRep = xmlHead.replaceAll("<ActionCode>0</ActionCode>", "<ActionCode>1</ActionCode>");
        String resp = buildZwRespStr(flag);
        xmlbodyBuff.append(xmlheadRep.substring(0, xmlheadRep.lastIndexOf("</TransInfo>")))
                .append("<TransIDH>").append("ZJHY").append(SerialNumGenerator.buildBossReqSerialNum(26)).append("</TransIDH>")
                .append("<TransIDHTime>").append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).append("</TransIDHTime>")
                .append("</TransInfo>").append(resp)
                .append("</InterBOSS>");

        LOGGER.info("卓望回调接口的响应内容为{}.", xmlbodyBuff.toString());

        // 发送应答给全国接口
        try {
            res.setContentType("multipart/mixed");
            res.getOutputStream().print(xmlbodyBuff.toString());
            res.getOutputStream().flush();
            res.getOutputStream().close();
        } catch (Exception e) {
            LOGGER.error("发送应答给全国接口时出错， 错误信息为{}.", e.getMessage());
        }
    }

    //构建回调响应
    private String buildZwRespStr(boolean flag) {
        if (flag) {
            return "<Response><RspType>0</RspType><RspCode>0000</RspCode><RspDesc>Success</RspDesc></Response>";
        } else {
            return "<Response><RspType>4</RspType><RspCode>9998</RspCode><RspDesc>Fail</RspDesc></Response>";  //服务异常
        }
    }

    //处理回调
    private void process(AsyncCallbackReq acr, Operator operator) {
        boolean flag = false;
        try {
            if (!processCallback(acr)) { //回调失败
                LOGGER.error("根据回调信息更新充值状态时出错, 回调信息为{}.", operator.getErrorInfo());
                flag = false;
            } else { //回调成功
                LOGGER.info("根据回调信息更新充值状态成功, 回调信息为{}.", operator.getErrorInfo());
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error("输出响应流时出错. 错误信息为{}，错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            flag = false;
        }

        operator.resp(flag);
    }

    //接收到由卓望回调的信息后，组装成平台的回调请求
    private AsyncCallbackReq convert(final OrderHandleResult ohr) {
        return convert(new Converter() {
            @Override
            public Info buildInfo() {
                return parse(ohr);
            }

            @Override
            public String buildErrorMsg() {
                return ohr == null ? "空" : new Gson().toJson(ohr);
            }

            @Override
            public String getBossRespSerialNum() {
                return ohr.getOperSeq();
            }
        });
    }

    //接收到由core（包括fdn和unionflow)的回调信息后，组装成平台的回调请求
    private AsyncCallbackReq convert(final CoreCallbackReq callbackReq) {
        return convert(new Converter() {
            @Override
            public Info buildInfo() {
                return parse(callbackReq);
            }

            @Override
            public String buildErrorMsg() {
                return callbackReq == null ? "空" : new Gson().toJson(callbackReq);
            }

            @Override
            public String getBossRespSerialNum() {
                return callbackReq.getOperSeq();
            }
        });
    }

    private AsyncCallbackReq convert(Converter converter) {
        AsyncCallbackReq acr = new AsyncCallbackReq();

        Info info = converter.buildInfo();
        if (info == null) {
            LOGGER.error("解析回调请求出错， 回调请求内容为{}.", converter.buildErrorMsg());
            return null;
        }

        String bossRespSerialNum = converter.getBossRespSerialNum();
        SerialNum serialNum = serialNumService.getByBossRespSerialNum(bossRespSerialNum);
        if (serialNum == null || StringUtils.isBlank(serialNum.getPlatformSerialNum())) {
            LOGGER.error("无法根据回调的BOSS响应序列号获取平台序列号, BossRespSerialNum = {}.", bossRespSerialNum);
            return null;
        }

        acr.setSystemSerialNum(serialNum.getPlatformSerialNum());
        acr.setMobile(info.getMobile());
        acr.setChargeRecordStatus(info.getChargeStatus());
        acr.setErrorMsg(info.errorMsg);

        return acr;
    }

    //解析卓望侧返回的回调信息
    private Info parse(OrderHandleResult ohr) {
        if (!validate(ohr)) {
            return null;
        }

        if (ohr.getSuccNum() == 1) {
            return new Info(ohr.getSuccMobNum().get(0), ChargeRecordStatus.COMPLETE);
        } else {
            OrderFailInfo ofi = ohr.getFailInfos().get(0);
            return new Info(ofi.getMobNum(), ChargeRecordStatus.FAILED.getCode(), ofi.getErrorDesc());
        }
    }

    //解析core侧返回的回调信息
    private Info parse(CoreCallbackReq callbackReq) {
        if (!validate(callbackReq)) {
            return null;
        }

        return parse(callbackReq.getSuccInfo(), callbackReq.getFailInfo());
    }

    //接收到上海月呈侧的请求报文后,组成平台的回调请求
    private AsyncCallbackReq convert(ShycCallbackReq shycCallbackReq) {
        if (!validate(shycCallbackReq)) {
            LOGGER.error("解析回调请求出错， 回调请求内容为{}.", shycCallbackReq == null ? "空" : new Gson().toJson(shycCallbackReq));
            return null;
        }

        String pltSn = shycCallbackReq.getOrderNo();

        //设置平台流水号
        AsyncCallbackReq acr = new AsyncCallbackReq();
        acr.setSystemSerialNum(pltSn);

        //设置充值手机号
        acr.setMobile(shycCallbackReq.getPhone());

        //设置充值状态
        acr.setChargeRecordStatus(shycCallbackReq.getCode().equals("4")
                ? ChargeRecordStatus.COMPLETE.getCode()
                : ChargeRecordStatus.FAILED.getCode());

        //设置充值消息
        acr.setErrorMsg(shycCallbackReq.getMessage());
        return acr;
    }

    //接收到山东平台的回调后，组成成平台的回调请求
    private AsyncCallbackReq convert(CallBackReq callBackReq) {
        if (!validate(callBackReq)) {
            LOGGER.error("解析回调请求出错， 回调请求内容为{}.", callBackReq == null ? "空" : new Gson().toJson(callBackReq));
            return null;
        }

        CallBackReqData cbrd = callBackReq.getCallBackReqData();

        AsyncCallbackReq acr = new AsyncCallbackReq();
        acr.setSystemSerialNum(cbrd.getEcSerialNum());
        acr.setMobile(cbrd.getMobile());
        acr.setChargeRecordStatus(cbrd.getStatus());
        acr.setErrorMsg(cbrd.getDescription());

        return acr;
    }

    //接收向上渠道的回调后，组装成平台的回调请求
    private AsyncCallbackReq convert(ResponsePojo responsePojo) {
        if (!validate(responsePojo)) {
            LOGGER.error("请求内容校验不通过， 请求内容为{}.", new Gson().toJson(responsePojo));
            return null;
        }

        AsyncCallbackReq acr = new AsyncCallbackReq();

        //设置充值流水号
        String bossReqSn = responsePojo.getOrderid();
        SerialNum serialNum = serialNumService.getByBossReqSerialNum(bossReqSn);
        if (serialNum == null) {
            LOGGER.error("无法根据BOSS侧请求流水号获取平台流水号， BossReqSn = {}.", responsePojo.getOrderid());
            return null;
        }

        String pltSn = serialNum.getPlatformSerialNum();
        acr.setSystemSerialNum(pltSn);

        //设置充值手机号
        ChargeRecord cr = chargeRecordService.getRecordBySN(pltSn);
        if (cr == null) {
            LOGGER.error("无法根据充值流水号获取相应的充值记录. PltSn = {}.", pltSn);
            return null;
        }
        acr.setMobile(cr.getPhone());

        //设置充值状态
        ErrCode errCode = ErrCode.fromCode(responsePojo.getErrcode());
        if (errCode == null) {
            LOGGER.error("无效的错误码标识，错误码为{}.", responsePojo.getErrcode());
            return null;
        }

        acr.setChargeRecordStatus(errCode == ErrCode.OrderSuccess
                ? ChargeRecordStatus.COMPLETE.getCode()
                : ChargeRecordStatus.FAILED.getCode());

        //设置充值信息
        acr.setErrorMsg(responsePojo.getErrinfo());
        return acr;
    }

    //接收山东云平台的回调后，组装成平台的回调请求
    private AsyncCallbackReq convert(SdRespPojo responsePojo) {
        if (!validate(responsePojo)) {
            LOGGER.error("请求内容校验不通过， 请求内容为{}.", new Gson().toJson(responsePojo));
            return null;
        }

        AsyncCallbackReq acr = new AsyncCallbackReq();

        //设置充值流水号
        String bossReqSn = responsePojo.getPkgSeq();
        SerialNum serialNum = serialNumService.getByBossReqSerialNum(bossReqSn);
        if (serialNum == null) {
            LOGGER.error("无法根据BOSS侧请求流水号获取平台流水号， BossReqSn = {}.", responsePojo.getPkgSeq());
            return null;
        }

        String pltSn = serialNum.getPlatformSerialNum();
        acr.setSystemSerialNum(pltSn);

        //设置充值手机号
        ChargeRecord cr = chargeRecordService.getRecordBySN(pltSn);
        if (cr == null) {
            LOGGER.error("无法根据充值流水号获取相应的充值记录. PltSn = {}.", pltSn);
            return null;
        }
        acr.setMobile(cr.getPhone());

        //设置充值是否成功
        acr.setChargeRecordStatus(responsePojo.getCode().equals(200)
                ? ChargeRecordStatus.COMPLETE.getCode()
                : ChargeRecordStatus.FAILED.getCode());

        //设置充值信息
        acr.setErrorMsg(responsePojo.getMsg());
        return acr;
    }


    private boolean validate(CoreCallbackReq callbackReq) {
        return callbackReq != null
                && StringUtils.isNotBlank(callbackReq.getOperSeq())
                && validate(callbackReq.getSuccInfo(), callbackReq.getFailInfo());
    }

    private boolean validate(OrderHandleResult ohr) {
        return ohr != null
                && StringUtils.isNotBlank(ohr.getOperSeq())
                && ohr.getSuccNum() == (ohr.getSuccMobNum() == null ? 0 : ohr.getSuccMobNum().size())
                && ohr.getFailNum() == (ohr.getFailInfos() == null ? 0 : ohr.getFailInfos().size())
                && ohr.getSuccNum() + ohr.getFailNum() == 1;
    }

    //向上渠道的回调响应对象是否正确
    private boolean validate(ResponsePojo responsePojo) {
        return responsePojo != null
                && StringUtils.isNotBlank(responsePojo.getId())
                && StringUtils.isNotBlank(responsePojo.getOrderid())
                && responsePojo.getDeno() > 0
                && responsePojo.getSuccessdeno() > 0
                && StringUtils.isNotBlank(responsePojo.getErrcode())
                && StringUtils.isNotBlank(responsePojo.getErrinfo())
                && signService.validate(responsePojo);
    }

    //山东渠道的回调响应对象是否正确
    private boolean validate(SdRespPojo responsePojo) {
        return responsePojo != null
                && StringUtils.isNotBlank(responsePojo.getPkgSeq())
                && responsePojo.getCode() != null
                && StringUtils.isNotBlank(responsePojo.getMsg());
    }

    //由于当前平台只支持单个充值，因此返回的成功和失败信息有且只能有一个, 此为解析部分
    private Info parse(List<SuccInfo> succs, List<FailInfo> fails) {
        if (!validate(succs, fails)) {
            return null;
        }

        boolean isSucess = (succs != null && !succs.isEmpty());
        String mobile = isSucess ? succs.get(0).getMobNum() : fails.get(0).getMobNum();
        String message = isSucess ? ChargeRecordStatus.COMPLETE.getMessage() : fails.get(0).getFailDesc();
        ChargeRecordStatus status = isSucess ? ChargeRecordStatus.COMPLETE : ChargeRecordStatus.FAILED;

        return new Info(mobile, status.getCode(), message);
    }

    //由于当前平台只支持单个充值，因此返回的成功和失败信息有且只能有一个
    private boolean validate(List<SuccInfo> succs, List<FailInfo> fails) {
        int count = (succs == null ? 0 : succs.size()) + (fails == null ? 0 : fails.size());
        return count == 1;
    }

    //校验上海月呈返回的回调内容
    private boolean validate(ShycCallbackReq shycCallbackReq) {
        String orderNo;
        String taskId;

        return shycCallbackReq != null
                && StringUtils.isNotBlank(orderNo = shycCallbackReq.getOrderNo()) //平台侧流水不能为空
                && StringUtils.isNotBlank(taskId = shycCallbackReq.getTaskId()) //boss侧流水不能为空
                && StringUtils.isNotBlank(shycCallbackReq.getCode()) //充值状态不能为空
                && StringUtils.isNotBlank(shycCallbackReq.getPhone()) //手机号码不能为空
                && shycCallbackReq.getSign().equals(DigestUtils.md5Hex(orderNo + taskId + getShycApiKey())); //校验签名
    }

    private boolean validate(CallBackReq callBackReq) {
        return callBackReq != null
                && callBackReq.getCallBackReqData() != null
                && StringUtils.isNotBlank(callBackReq.getCallBackReqData().getEcSerialNum())
                && StringUtils.isNotBlank(callBackReq.getCallBackReqData().getMobile())
                && callBackReq.getCallBackReqData().getStatus() != null;
    }

    private boolean processCallback(AsyncCallbackReq acr) {
        if (acr == null || !asyncCallbackService.process(acr)) {
            LOGGER.error("BOSS侧回调信息处理失败，具体信息为{}.", acr == null ? "回调请求对象为空" : new Gson().toJson(acr));
            return false;
        } else {
            LOGGER.info("BOSS侧回调信息处理成功，具体信息为{}.", new Gson().toJson(acr));
            return true;
        }
    }

    private String getShycApiKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHYC_API_KEY.getKey());
    }

    private String buildCommonRespStr(String code, String respMsg) {
        CallbackResp resp = buildResp(code, respMsg);
        return sdXstream.toXML(resp);
    }

    //fdn平台的回调信息
    private String buildCoreRespStr(String code, String respMsg) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("code", code);
        map.put("message", respMsg);

        return new Gson().toJson(map);
    }

    private CallbackResp buildResp(String code, String respMsg) {
        CallbackResp resp = new CallbackResp();

        resp.setCode(code);
        resp.setDateTime(new DateTime().toString());
        resp.setMessage(respMsg);

        return resp;
    }

    //解析上海月呈侧的回调内容
    private ShycCallbackReq parseShyc(Integer type, String orderNo, String taskId,
                                      String phone, String code, String message, String sign) {
        ShycCallbackReq scr = new ShycCallbackReq();
        scr.setType(type);
        scr.setOrderNo(orderNo);
        scr.setTaskId(taskId);
        scr.setPhone(phone);
        scr.setCode(code);
        scr.setMessage(message);
        scr.setSign(sign);

        return scr;
    }

    private CallBackReq parse(HttpServletRequest request) {
        String respStr = null;
        try {
            respStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("解析回调参数时出错.");
            return null;
        }

        return (CallBackReq) sdXstream.fromXML(respStr);
    }


    //北京云漫的回调对象转化成平台的回调对象
    private AsyncCallbackReq convert(BjymCallbackRequest bjymCallbackRequest) {
        if (!validate(bjymCallbackRequest)) {
            LOGGER.error("校验北京云漫回调请求出错， 回调请求内容为{}.", bjymCallbackRequest == null ? "空" : new Gson().toJson(bjymCallbackRequest));
            return null;
        }

        BjymCallbackRequestMessage message = bjymCallbackRequest.getData().getCallbackRequestMessageList().get(0);
        AsyncCallbackReq asyncCallbackReq = new AsyncCallbackReq();
        asyncCallbackReq.setSystemSerialNum(message.getRequestid());
        asyncCallbackReq.setErrorMsg(message.getStateDes());
        asyncCallbackReq.setMobile(message.getMobile());

        //设置充值状态
        BjymChargeStateEnum chargeStateEnum = BjymChargeStateEnum.fromCode(message.getState());
        asyncCallbackReq.setChargeRecordStatus(
                (chargeStateEnum != null && chargeStateEnum == BjymChargeStateEnum.SUCCESS)
                        ? ChargeRecordStatus.COMPLETE.getCode()
                        : ChargeRecordStatus.FAILED.getCode());

        return asyncCallbackReq;
    }

    //校验北京云漫的回调请求参数
    private boolean validate(BjymCallbackRequest bjymCallbackRequest) {
        return bjymCallbackRequest != null
                && StringUtils.isNotBlank(bjymCallbackRequest.getPartyId())
                && StringUtils.isNotBlank(bjymCallbackRequest.getTime())
                && validate(bjymCallbackRequest.getData())
                && validateSign(bjymCallbackRequest);
    }

    private boolean validate(BjymCallbackRequestData data) {
        return data != null
                && NumberUtils.toInt(data.getSize(), -1) == 1
                && StringUtils.isNotBlank(data.getType())
                && validate(data.getCallbackRequestMessageList().get(0));
    }

    private boolean validate(BjymCallbackRequestMessage message) {
        return message != null
                && StringUtils.isNotBlank(message.getMobile())
                && StringUtils.isNotBlank(message.getState())
                && StringUtils.isNotBlank(message.getSendID())
                && StringUtils.isNotBlank(message.getRecvTime())
                && StringUtils.isNotBlank(message.getUserPackage())
                && StringUtils.isNotBlank(message.getStateDes())
                && StringUtils.isNotBlank(message.getRequestid());
    }

    private boolean validateSign(BjymCallbackRequest bjymCallbackRequest) {
        if (StringUtils.isBlank(bjymCallbackRequest.getSign())) {
            return false;
        }

        //北京云漫侧说回调接口的sign字段暂时不用...不要问我为什么...我也很无奈....
        return true;
//        BjymCallbackRequestMessage message = bjymCallbackRequest.getData().getCallbackRequestMessageList().get(0);
//        return bjymCallbackRequest.getSign().equals(DigestUtils.md5Hex(
//                "sendID" + message.getSendID()
//                        + "key" + getBjymKey()
//                        + "partyid" + getBjymPartyId()
//        ));
    }

    private BjymCallbackRequest parseBjymCallbackPojo(String callbackReq) {
        if (StringUtils.isBlank(callbackReq)) {
            LOGGER.error("北京云漫侧的回调参数为空.");
            return null;
        }

        try {
            return new Gson().fromJson(callbackReq, BjymCallbackRequest.class);
        } catch (Exception e) {
            LOGGER.error("解析北京云漫侧回调参数时出错,错误信息为{}, 错误堆栈为{}, 回调内容为{}.", e.getMessage(), e.getStackTrace(), callbackReq);
        }

        return null;
    }

    /**
     * @Title: convert
     * @Description: 黑龙江充值结果对象转换成平台通用对象
     * @return: AsyncCallbackReq
     */
    private AsyncCallbackReq convert(HLJBOSSChargeResponse hljBOSSChargeResponse) {
        AsyncCallbackReq asyncCallbackReq = new AsyncCallbackReq();
        if (hljBOSSChargeResponse == null || hljBOSSChargeResponse.getCharge() == null
                || StringUtils.isEmpty(hljBOSSChargeResponse.getCharge().getOpNumber())) {
            LOGGER.error("BOSS报文封装类转换为平台通用类对象失败：参数缺失。");
            return asyncCallbackReq;
        }
        //注：黑龙江BOSS只返回一个序列号，BOSS请求序列号
        String bossResp = hljBOSSChargeResponse.getCharge().getOpNumber();
        SerialNum serialNum = serialNumService.getByBossReqSerialNum(bossResp);
        if (serialNum == null || StringUtils.isBlank(serialNum.getPlatformSerialNum())) {
            LOGGER.error("无法根据回调的BOSS响应序列号获取平台序列号, BossReqSerialNum = {}.", bossResp);
            return asyncCallbackReq;
        }

        asyncCallbackReq.setSystemSerialNum(serialNum.getPlatformSerialNum());
        asyncCallbackReq.setMobile(hljBOSSChargeResponse.getCharge().getPhoneNo());
        asyncCallbackReq.setChargeRecordStatus(hljBOSSChargeResponse.isSuccess() ? ChargeRecordStatus.COMPLETE
                .getCode() : ChargeRecordStatus.FAILED.getCode());
        asyncCallbackReq.setErrorMsg(hljBOSSChargeResponse.getResultDesc());
        return asyncCallbackReq;
    }

    private String getBjymKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BJYM_KEY.getKey());
    }

    private String getBjymPartyId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BJYM_PARTY_ID.getKey());
    }

    /**
     * @Title: bulidHLJResp
     * @Description: 构建黑龙江异步响应报文
     * @return: String
     */
    private String bulidHLJResp(boolean flag, String platformSerialNum) {
        String resultCode = null;
        String resultMsg = null;
        if (flag) {
            resultCode = "0000";//0000代表成功 ，其他代表失败。
            resultMsg = "处理成功";
        } else {
            resultCode = "9998";//0000代表成功 ，其他代表失败。
            resultMsg = "处理失败";
        }
        SerialNum serialNum = serialNumService.getByPltSerialNum(platformSerialNum);
        String bossReqNum = serialNum == null ? "" : serialNum.getBossReqSerialNum();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String responseTime = sdf.format(new Date());
        StringBuilder resp = new StringBuilder();
        resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<InterBOSS><Response><RspType>0</RspType><RspCode>")
                .append(resultCode)
                .append("</RspCode><RspDesc>")
                .append(resultMsg)
                .append("</RspDesc></Response><OrigDomain>BOSS</OrigDomain><HomeDomain>HYKP</HomeDomain><BIPCode>BIP9B279</BIPCode>")
                .append("<ActivityCode>T0000009</ActivityCode><ActionCode>1</ActionCode><TransIDO>").append(bossReqNum)
                .append("</TransIDO><ProcessTime>").append(responseTime)
                .append("</ProcessTime><TestFlag>0</TestFlag><SvcCont><![CDATA[]]></SvcCont></InterBOSS>");
        return resp.toString();
    }

    private interface RespBuilder {
        String build(CallbackResult callbackResult);
    }

    private interface Operator {
        //获取错误信息
        String getErrorInfo();

        //构建并发回响应
        void resp(boolean flag);
    }

    private interface Converter {
        //转换成info对象
        Info buildInfo();

        //构建错误信息
        String buildErrorMsg();

        //获取boss侧响应流水号
        String getBossRespSerialNum();
    }

    private class Info {
        private String mobile;
        private int chargeStatus;
        private String errorMsg;

        public Info(String mobile, int chargeStatus, String errorMsg) {
            this.mobile = mobile;
            this.chargeStatus = chargeStatus;
            this.errorMsg = errorMsg;
        }

        public Info(String mobile, ChargeRecordStatus chargeRecordStatus) {
            this.mobile = mobile;

            this.chargeStatus = chargeRecordStatus.getCode();
            this.errorMsg = chargeRecordStatus.getMessage();
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(int chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
    
    

    /**
     * 云企信接收充值回调
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     */
    @RequestMapping(value = "/yqx/callback", method = RequestMethod.POST)
    public void yqxCallback(HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        //获取平台的回调对象
        final CallBackReq callBackReq = parse(httpServletRequest);
        LOGGER.info("从平台BOSS侧接收回调信息， 回调内容为{}.", callBackReq == null ? "空" : new Gson().toJson(callBackReq));

        //包装成平台的回调对象
        AsyncCallbackReq acr = convert(callBackReq);
        boolean flag = yqxChargeService.processingCallback(acr);
        invokeResp(flag, httpServletResponse, new RespBuilder() {
            @Override
            public String build(CallbackResult callbackResult) {
                return buildCommonRespStr(callbackResult.getCode(), callbackResult.getMessage());
            }
        });
    }
    
    /**
     * 
     * @Title: hljCallback 
     * @Description: 黑龙江充值接口异步回调
     * @param req
     * @param res
     * @throws Exception
     * @return: void
     */
    @RequestMapping(value = "/hlj/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public void hljCallback(HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String callbackReqStr = StreamUtils.copyToString(req.getInputStream(), Charsets.UTF_8);
        try {
            LOGGER.info("黑龙江流量平台收到思特奇平台异步回调报文：" + callbackReqStr);
            XStream xStream = new XStream();
            xStream.autodetectAnnotations(true);
            xStream.alias("Request", HLJCallBackPojo.class);
            final HLJCallBackPojo pojo = (HLJCallBackPojo) xStream.fromXML(callbackReqStr);

            AsyncCallbackReq acr = convert(pojo);

            if (acr == null) {
                LOGGER.error("boss返回的流水号在数据库没有找到相应的记录，数据为{}.", callbackReqStr);
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            //处理回调
            process(acr, new Operator() {
                @Override
                public String getErrorInfo() {
                    return pojo.getReturnMsg();
                }

                //empty response
                @Override
                public void resp(boolean flag) {
                    try {
                        String resp = bulidSdResp(flag);
                        LOGGER.info("黑龙江流量平台异步回调请求报文：" + callbackReqStr + ",响应报文：" + resp);
                        StreamUtils.copy(resp, Charsets.UTF_8, res.getOutputStream());
                    } catch (IOException e) {
                        LOGGER.error("响应回调请求时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                    }

                }
            });

        } catch (JsonSyntaxException e) {
            LOGGER.error("boss返回的数据无法解析，数据为{},异常信息为{}.", callbackReqStr, e.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
    
    /**
     * 
     * @Title: convert 
     * @Description: 转换黑龙江流量流量平台斯特返回的异步回调报文
     * @param ohr
     * @return
     * @return: AsyncCallbackReq
     */
    private AsyncCallbackReq convert(final HLJCallBackPojo hljCallBackPojo) {
        if (hljCallBackPojo == null || StringUtils.isBlank(hljCallBackPojo.getExtOrderId())
                || StringUtils.isBlank(hljCallBackPojo.getReturnCode())) {
            LOGGER.error("回调报文参数缺失, 回调信息为{}.", new Gson().toJson(hljCallBackPojo));
            return null;
        }

        //根据订单好查询充值记录
        AsyncCallbackReq acr = new AsyncCallbackReq();

        //查找充值记录
        SerialNum serialNum = serialNumService.getByBossReqSerialNum(hljCallBackPojo.getExtOrderId());
        if (serialNum == null) {
            LOGGER.error("无法根据BOSS请求流水号获取相应的serialNum记录. bossRespSerialNum = {}.", hljCallBackPojo.getExtOrderId());
            return null;
        }

        ChargeRecord cr = chargeRecordService.getRecordBySN(serialNum.getPlatformSerialNum());
        if (cr == null) {
            LOGGER.error("无法根据充值流水号获取相应的充值记录. systemNum = {}.", serialNum.getPlatformSerialNum());
            return null;
        }
        acr.setMobile(cr.getPhone());
        acr.setSystemSerialNum(serialNum.getPlatformSerialNum());

        //设置充值是否成功
        acr.setChargeRecordStatus(hljCallBackPojo.getReturnCode().equals("0") ? ChargeRecordStatus.COMPLETE.getCode()
                : ChargeRecordStatus.FAILED.getCode());

        //设置充值信息
        acr.setErrorMsg(hljCallBackPojo.getReturnMsg());
        return acr;
    } 

}
