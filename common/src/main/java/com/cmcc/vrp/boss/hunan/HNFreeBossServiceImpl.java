package com.cmcc.vrp.boss.hunan;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hunan.model.HNBOSSCharge;
import com.cmcc.vrp.boss.hunan.model.HNChargeResponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.CityFee;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.CityFeeService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

/**
 * 
 * @ClassName: HNFreeBossServiceImpl 
 * @Description: 湖南0元订购调用逻辑说明：
 *      1、通过手机号码确定地市编码；（调用上游能力平台）
 *      2、校验充值产品编码是否属于该地市；（数据库查询，数据库硬编码）
 *      3、充值（包括组装通用参数、业务参数，发送充值和报文解析，调用上游能力平台）
 *      接口生效时间：2015.5.25；上游平台：能力开发平台（亚信）
 * @author: Rowe
 * @date: 2017年5月22日 下午4:09:47
 */
@Service
@Qualifier("huNanFreeBossService")
public class HNFreeBossServiceImpl extends HNBossServcieImpl {

    private static final Logger logger = LoggerFactory.getLogger(HNFreeBossServiceImpl.class);

    @Autowired
    CityFeeService cityFeeService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {

        HNChargeResponse errorResponse = new HNChargeResponse();

        //校验产品是否存在
        SupplierProduct sp = null;
        if ((sp = supplierProductService.selectByPrimaryKey(splPid)) == null) {
            errorResponse.setResultDesc("无法获取供应商产品信息");
            logger.error("湖南0元订购接口调用失败，无法获取供应商产品信息,手机号码：{},供应商产品ID：{}, 系统序列号serialNum：{}。", mobile, splPid, serialNum);
            return errorResponse;
        }

        //根据手机号码获取地市编码
        String cityCode = sendRequestForCityCode(mobile);
        if (StringUtils.isBlank(cityCode)) {
            errorResponse.setResultDesc("无法根据手机号码【" + mobile + "】获取地市信息");
            logger.error("湖南0元订购接口调用失败，无法根据手机号码从上游平台获取地市编码,手机号码：{}.", mobile);
            return errorResponse;
        }

        //根据地市编码和平台产品编码，拼接成上游最终需要的产品编码
        String bossProductCode = getBossProductCode(cityCode, sp.getCode());
        if (StringUtils.isBlank(bossProductCode)) {
            errorResponse.setResultDesc("充值号码【" + mobile + "】所属地区未订购该产品【" + sp.getCode() + "】");
            return errorResponse;
        }

        //发起充值请求
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("HNFRE", 25);
        String response = freeCharge(mobile, bossProductCode, bossReqNum, serialNum);

        //解析充值报文
        HNChargeResponse chargeResponse = parseFreeChargeRep(response);

        //充值更新流水号
        String bossRespNum = chargeResponse.getCharge() == null ? null : chargeResponse.getCharge().getX_TRADE_ID();
        if (super.updateRecord(serialNum, bossRespNum, bossReqNum)) {
            logger.info("湖南充值更新流水号成功:systemNum = " + serialNum + ",bossRespNum = " + bossRespNum + ",bossReqNum = "
                    + bossReqNum);
        } else {
            logger.info("湖南充值更新流水号失败:systemNum = " + serialNum + ",bossRespNum = " + bossRespNum + ",bossReqNum = "
                    + bossReqNum);
        }

        //返回充值结果
        return chargeResponse;
    }

    @Override
    public String getFingerPrint() {
        return "hunanFreeOfCharge";
    }

    /**
     * 
     * @Title: getFreeOfChargeMethod 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getFreeOfChargeMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FREE_OF_CHARGE_METHOD.getKey());
    }

    /**
     * 
     * @Title: getDiscntMethod 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getDiscntMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FREE_OF_DISCNT_METHOD.getKey());
    }

    /**
     * 
     * @Title: getElementTypeCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getElementTypeCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FREE_OF_ELEMENT_TYPE_CODE.getKey());
    }

    /**
     * 
     * @Title: getModifyTag 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getModifyTag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FREE_OF_MODIFY_TAG.getKey());
    }

    /**
     * 
     * @Title: getBossProductCode 
     * @Description: 根据地市编码和产品编码转换为最终的BOSS产品编码
     * @param cityCode
     * @param productCode
     * @return
     * @return: String
     */
    private String getBossProductCode(String cityCode, String productCode) {
        if (StringUtils.isBlank(cityCode) || StringUtils.isBlank(productCode)) {
            logger.error("湖南0元订购接口调用失败，地市编码或供应商产品编码为空：cityCode = {},  productCode = {}.", cityCode, productCode);
            return null;
        }
        CityFee cityFee = cityFeeService.getByCityCodeAndFeecode(cityCode, productCode);
        if (cityFee == null) {
            logger.error("湖南0元订购接口调用失败，平台不存在地市编码{}和产品编码{}的映射关系", cityCode, productCode);
            return null;
        } else {
            return new StringBuffer().append(cityFee.getPreFeeCode()).append(cityFee.getFeeCode()).toString();
        }
    }

    /**
     * 
     * @Title: sendRequestForCityCode 
     * @Description: TODO
     * @param mobile
     * @return
     * @return: String
     */
    private String sendRequestForCityCode(String mobile) {
        //组织通用参数
        String url = super.assembleCommon(getDiscntMethod());

        //组织业务参数
        StringBuffer busiData = new StringBuffer();
        busiData.append("{\"SERIAL_NUMBER\":\"").append(mobile).append("\"}");

        logger.info("湖南BOSS根据手机号码获取地市编码接口，请求url ： " + url + ",请求参数 ：" + busiData.toString());
        //发送充值请求
        String response = HttpUtils.post(url, busiData.toString(), "application/json");
        //        String response = HttpConnection.doPost(url, busiData.toString(), "application/json", "utf-8", false);

        logger.info("湖南BOSS根据手机号码获取地市编码接口,响应报文 ：" + response);

        return parseResponseForCityCode(response);

    }

    /**
     * 
     * @Title: parseResponseForCityCode 
     * @Description: TODO
     * @param xml
     * @return
     * @return: String
     */
    public static String parseResponseForCityCode(String xml) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(xml);
            String result = jsonObject.getString("result");
            result = result.substring(1, result.length() - 1);//截取result[]中间的内容
            JSONObject jsonResultObject = JSONObject.parseObject(result);
            return jsonResultObject.getString("EPARCHY_CODE");
        } catch (Exception e) {
            logger.info("湖南BOSS号码归属地查询接口响应报文解析失败！响应报文：" + xml);
            logger.info("湖南BOSS号码归属地查询接口约定的响应报文格式示例：{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":[{\"EPARCHY_CODE\":\"0731\",\"IMSI_E\":\"\",\"IMSI_S\":\"\",\"MOFFICE_ID\":\"350731\",\"SERIALNUMBER_E\":\"13507319999\",\"SERIALNUMBER_S\":\"13507310000\",\"SWITCH_ID\":\"WCS05\",\"UPDATE_DEPART_ID\":\"\",\"UPDATE_STAFF_ID\":\"SUPERUSR\",\"UPDATE_TIME\":\"\",\"X_PAGINCOUNT\":\"0\",\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\"0\",\"X_RESULTCODE\":\"0\",\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\"1\",\"X_RSPCODE\":\"0000\",\"X_RSPDESC\":\"ok\",\"X_RSPTYPE\":\"0\",\"aop-srv\":\"aopesb1\"}]}");
        }
        return null;
    }

    /**
     * 
     * @Title: freeCharge 
     * @Description: TODO
     * @param mobile
     * @param discnt
     * @param bossReqNum
     * @param systemNum
     * @return
     * @return: String
     */
    private String freeCharge(String mobile, String discnt, String bossReqNum, String systemNum) {
        //组织通用参数
        String url = super.assembleCommon(getFreeOfChargeMethod());

        //组织业务参数
        StringBuffer busiData = new StringBuffer();
        busiData.append("{\"SERIAL_NUMBER\":\"").append(mobile).append("\",\"ELEMENT_TYPE_CODE\":\"")
                .append(getElementTypeCode()).append("\",\"ELEMENT_ID\":\"").append(discnt)
                .append("\",\"MODIFY_TAG\":\"").append(getModifyTag()).append("\"}");

        //发送充值请求
        logger.info("湖南BOSS0元订购接口，充值请求url ： " + url + ",请求参数 ：" + busiData.toString());
        String response = HttpUtils.post(url, busiData.toString(), "application/json");
        //        String response = HttpConnection.doPost(url, busiData.toString(), "application/json", "utf-8", false);
        logger.info("湖南BOSS0元订购接口,响应报文 ：" + response);

        return response;
    }

    /**
     * 
     * @Title: parseFreeChargeRep 
     * @Description: TODO
     * @param xml
     * @return
     * @return: HNChargeResponse
     */
    public static HNChargeResponse parseFreeChargeRep(String xml) {
        HNChargeResponse chargeResponse = new HNChargeResponse();
        try {
            JSONObject jsonObject = JSONObject.parseObject(xml);
            String result = jsonObject.getString("result");
            result = result.substring(1, result.length() - 1);//截取result[]中间的内容
            JSONObject jsonResultObject = JSONObject.parseObject(result);

            //报文解析过程
            HNBOSSCharge charge = new HNBOSSCharge();
            charge.setX_RESULTCODE(jsonResultObject.getString("X_RESULTCODE"));//响应状态码
            charge.setX_RESULTINFO(jsonResultObject.getString("X_RESULTINFO"));//响应描述信息
            charge.setX_TRADE_ID(jsonResultObject.getString("ORDER_ID"));//响应序列号

            chargeResponse.setCharge(charge);
            chargeResponse.setX_RESULTCODE(charge.getX_RESULTCODE());
            chargeResponse.setX_RESULTINFO(charge.getX_RESULTINFO());
            chargeResponse.setResultCode(charge.getX_RESULTCODE());
            chargeResponse.setResultDesc(charge.getX_RESULTINFO());
        } catch (Exception e) {
            chargeResponse.setResultDesc("上游平台维护中!");
            logger.info("湖南BOSS0元订购接口响应报文解析失败！响应报文：" + xml);
            logger.info("湖南BOSS0元订购接口约定的响应报文格式示例：{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":[{\"DB_SOURCE\":\"0731\",\"ORDER_ID\":\"3117052343409611\",\"ORDER_TYPE_CODE\":\"110\",\"TRADE_ID\":\"3117052396560900\",\"USER_ID\":\"3103011205092395\",\"X_PAGINCOUNT\":\"0\",\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\"0\",\"X_RESULTCODE\":\"0\",\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\"1\",\"X_RSPCODE\":\"0000\",\"X_RSPDESC\":\"ok\",\"X_RSPTYPE\":\"0\",\"aop-srv\":\"aopesb1\"}]}");
        }
        return chargeResponse;
    }

    /**
     * 
     * @Title: assembleCommonPara 
     * @Description: TODO
     * @param method
     * @return
     * @return: String
     */
    public static String assembleCommonPara(String method) {
        String requestUrl = "http://111.8.20.250:19001/oppf";
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//时间戳
        String format = "json";//数据格式
        String appId = "505811";//appId,正式接入需要更换
        String status = "1";
        String flowId = timestamp;//渠道唯一流水
        String provinceCode = "HNAN";
        String inModeCode = "9";
        String tradeEparchyCode = "HNAN";
        String tradeCityCode = "XXXG";
        String tradeDepartId = "C0ZZC";
        String tradeStaffId = "ITFHYPT1";//正式接入需要更换
        String tradeDepartPasswd = "348688";//正式接入需要更换
        String tradeTerminalId = "10.154.92.35";//接入IP
        String routeEparchyCode = "HNAN";

        StringBuffer commonParam = new StringBuffer();
        commonParam.append(requestUrl).append("?method=").append(method).append("&timestamp=").append(timestamp)
                .append("&format=").append(format).append("&appId=").append(appId).append("&status=").append(status)
                .append("&flowId=").append(flowId).append("&PROVINCE_CODE=").append(provinceCode)
                .append("&IN_MODE_CODE=").append(inModeCode).append("&TRADE_EPARCHY_CODE=").append(tradeEparchyCode)
                .append("&TRADE_CITY_CODE=").append(tradeCityCode).append("&TRADE_DEPART_ID=").append(tradeDepartId)
                .append("&TRADE_STAFF_ID=").append(tradeStaffId).append("&TRADE_DEPART_PASSWD=")
                .append(tradeDepartPasswd).append("&TRADE_TERMINAL_ID=").append(tradeTerminalId)
                .append("&ROUTE_EPARCHY_CODE=").append(routeEparchyCode);

        return commonParam.toString();
    }

    //    public static void main(String args[]) throws IllegalStateException, IOException {
    //
    //        String mobile = "13507310001";
    //
    //        String feeCode = "31537949";
    //
    //        String urlMobile = assembleCommonPara("DQ_HQ_HNAN_PMphoneAddress");
    //
    //        //组织业务参数
    //        StringBuffer busiDataMobile = new StringBuffer();
    //        busiDataMobile.append("{\"SERIAL_NUMBER\":\"").append(mobile).append("\"}");
    //
    //        //发送充值请求
    //
    //        String responseMobile = HttpConnection.doPost(urlMobile, busiDataMobile.toString(), "application/json",
    //                "utf-8", false);
    //
    //        System.out.println("湖南BOSS根据手机号码获取地市编码接口，充值请求url ： " + urlMobile + ",请求参数 ：" + busiDataMobile.toString()
    //                + ",响应报文 ：" + responseMobile);
    //
    //        //地市编码
    //        String cityCode = parseResponseForCityCode(responseMobile);
    //
    //        String urlFreeCharge = assembleCommonPara("DQ_HT_HNAN_PElementChange");
    //
    //        //组织业务参数
    //        StringBuffer busiDataFreeCharge = new StringBuffer();
    //        busiDataFreeCharge.append("{\"SERIAL_NUMBER\":\"").append(mobile).append("\",\"ELEMENT_TYPE_CODE\":\"")
    //                .append("D").append("\",\"ELEMENT_ID\":\"").append(feeCode).append("\",\"MODIFY_TAG\":\"").append(0)
    //                .append("\"}");
    //
    //        //发送充值请求
    //        String responseFreeCharge = HttpConnection.doPost(urlFreeCharge, busiDataFreeCharge.toString(),
    //                "application/json", "utf-8", false);
    //
    //        System.out.println("湖南BOSS流量池充值接口，充值请求url ： " + urlFreeCharge + ",请求参数 ：" + busiDataFreeCharge.toString()
    //                + ",响应报文 ：" + responseFreeCharge);
    //
    //        HNChargeResponse chargeResult = parseFreeChargeRep(responseFreeCharge);
    //        System.out.println("充值结果：" + chargeResult.isSuccess() + ",状态码：" + chargeResult.getX_RESULTCODE() + ",描述信息："
    //                + chargeResult.getX_RESULTINFO());
    //    }
}
