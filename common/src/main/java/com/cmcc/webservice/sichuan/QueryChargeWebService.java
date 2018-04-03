package com.cmcc.webservice.sichuan;

import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.util.MD5;
import com.cmcc.webservice.sichuan.pojo.QueryResult;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

//@Component
//@Path("query")
/**
 * @author
 */
public class QueryChargeWebService {
    private static Logger logger = Logger.getLogger(QueryChargeWebService.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    InterfaceRecordService interfaceRecordService;

    /**
     * 查询返回对象
     * @param serialNumber  系列号
     * @param account       账号
     * @param bisCode       bisCode
     * @param timeStamp     时间戳
     * @param sign          签名
     * @return              查询返回对象     
     */
    @Consumes("application/x-www-form-urlencoded")
    @POST
    public Response query(@FormParam("serialNumber") String serialNumber, @FormParam("account") String account, @FormParam("bisCode") String bisCode,
                          @FormParam("timeStamp") String timeStamp, @FormParam("sign") String sign) {
        logger.info("接收企业查询接口调用，接口参数为：" + "serialNumber：" + serialNumber + " ,account：" + account + " ,bisCode:"
                + bisCode + " ,timeStamp:" + timeStamp + " ,sign:" + sign);

        return Response.status(404).entity("无效的地址").build();
        /*Map map = new HashMap();

        //1、校验参数
        ReturnCode returnCode = validateIfLegalRequest(serialNumber, account, bisCode, timeStamp, sign);
        if (!returnCode.equals(ReturnCode.success)) {
            map.put("resultCode", returnCode.getCode());
            map.put("resultMsg", returnCode.getMsg());
            String resultStr = JSON.toJSONString(map);
            //response返回数据
            return Response.status(200).entity(resultStr).build();
        }

        //获企业编码
        Enterprise enterprise = enterprisesService.selectByAppKey(bisCode);
        String code = enterprise.getCode();

        InterfaceRecord record = interfaceRecordService.get(code, serialNumber, null);
        if (record == null) {
            map.put("resultCode", ReturnCode.invalid_record.getCode());
            map.put("resultMsg", ReturnCode.invalid_record.getMsg());
        } else {
            if (record.getStatus() == 3) {
                map.put("resultCode", ReturnCode.success.getCode());
                map.put("resultMsg", ReturnCode.success.getMsg());
            } else {
                map.put("resultCode", ReturnCode.other_error.getCode());
                if (record.getErrMsg() != null) {
                    map.put("resultMsg", record.getErrMsg());
                } else {
                    map.put("resultMsg", ReturnCode.other_error.getMsg());
                }
            }
            QueryResult result = convertToQueryResult(record);
            map.put("result", JSON.toJSONString(result));
        }

        String resultStr = JSON.toJSONString(map);

        logger.info("查询参数, serialNumber：" + serialNumber + " ,bisCode:" + bisCode + ",返回:" + resultStr);
        //response返回数据
        return Response.status(200).entity(resultStr).build();*/
    }


    /**
     * 检查所有数据是否正确，返回相应enum中的结果
     *
     * @param chargeInfo
     * @return
     */
    public ReturnCode validateIfLegalRequest(String serialNumber, String account, String bisCode, String timeStamp, String sign) {

        //1、检查参数是否正确
        if (!checkIfEmpty(serialNumber, account, bisCode, timeStamp, sign)) {
            return ReturnCode.parameter_error;
        }

        //2、检查账号是否存在,企业账号状态是否正常，企业EC接口是否开通
        Enterprise enterprise = enterprisesService.selectByAppKey(bisCode);
        if (enterprise == null || enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
            return ReturnCode.invalid_account;
        }

        //3、检查md5是否正确
        if (!virifyMD5(serialNumber, account, bisCode, timeStamp, enterprise.getAppSecret(), sign)) {
            return ReturnCode.invalid_sign;
        }

        //4、检查时间戳是否在5分钟之内
        long createTime = Long.parseLong(timeStamp);
        long currentTime = System.currentTimeMillis();
        if (currentTime - createTime > 5 * 60 * 1000) {
            return ReturnCode.request_more_than_5min;
        }

        return ReturnCode.success;
    }

    private Boolean checkIfEmpty(String serialNumber, String account, String bisCode, String timeStamp, String sign) {
        if (StringUtils.isEmpty(serialNumber) ||
            StringUtils.isEmpty(account) ||
            StringUtils.isEmpty(bisCode) ||
            StringUtils.isEmpty(timeStamp)) {
            return false;
        }
        return true;
    }

    Boolean virifyMD5(String serialNumber, String account, String bisCode, String timeStamp, String key, String sign) {
        String content = serialNumber + account + bisCode + timeStamp;
        String signStr = MD5.sign(content, key, "UTF-8");
        if (!signStr.equals(sign)) {
            logger.info("MD5签名验证错误，签名应为:" + signStr);
            return false;
        }
        return true;
    }

    QueryResult convertToQueryResult(InterfaceRecord record) {
        QueryResult result = new QueryResult();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result.setServiceCode("771");
        if (record.getChargeTime() != null && record.getStatus().equals(ChargeRecordStatus.COMPLETE.getCode())) {
            result.setStartDate(format.format(record.getChargeTime()));
        }
        result.setDiscntCode(record.getProductCode());
        result.setEffectiveWay("0");
        result.setMobile(record.getPhoneNum());
        result.setSerialNumber(record.getSerialNum());
        return result;

    }
}
