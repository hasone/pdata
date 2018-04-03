package com.cmcc.vrp.boss.jilin;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.jilin.model.JlResp;
import com.cmcc.vrp.boss.jilin.utils.Sign;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: 吉林boss实现</p>
* @author lgk8023
* @date 2017年3月31日 下午5:32:09
*/
@Component("jilinBossService")
public class JlBossServiceImpl implements BossService{
    private static final Logger logger = LoggerFactory.getLogger(JlBossServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    private static final String SYMBOL = "&";
    @Autowired
    private SupplierProductService supplierProductService;
    
    @Autowired
    private EnterprisesService enterprisesService;
    
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
//    public static void main(String[] args) {
////        InetAddress addr = null;
////        try {
////            addr = InetAddress.getLocalHost();
////        } catch (UnknownHostException e) {
////
////            e.printStackTrace();
////        }
////        String ip = addr.getHostAddress().toString().replace(".", "");
////        System.out.println(ip);
//        JlBossServiceImpl bossServiceImpl = new JlBossServiceImpl();
//        bossServiceImpl.charge(null, null, "15164319285", null, null);
//        
//    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        logger.info("吉林充值start!entId{},mobile{},serialNum{}", entId, mobile, serialNum);
        //检查参数
        SupplierProduct supplierProduct = null;
        Enterprise enterprise = null;
    
        if (splPid == null
            || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
            || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null
            || StringUtils.isBlank(mobile)
            || StringUtils.isBlank(serialNum)) {
            logger.info("调吉林BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        
        }        
        String prdCode = supplierProduct.getCode();
        String entCod = enterprise.getCode();
//        String prdCode="22CAZ10896";
//        String entCod="10011201736";
        String url = getChargeUrl();
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("jilin", 20);
        String requestParameters = getParameters(entCod, prdCode, mobile, bossReqNum);
        JlResp jlResp = null;
        try {
            String sign = Sign.toSign(requestParameters, getPrivateKeyFile());
            requestParameters = requestParameters + "&sign=" + sign;
            logger.info("吉林充值请求参数{}", requestParameters);
            String x= Sign.sendPost(url, requestParameters);//post请求 
            logger.info("请求响应参数{}", new String(x.getBytes("ISO-8859-1"), "UTF8"));
            System.out.println("请求响应参数：" + new String(x.getBytes("ISO-8859-1"), "UTF8"));
            Gson gson = new Gson();
            jlResp = gson.fromJson(x, JlResp.class);
            if (jlResp != null) {
                if ("0".equals(jlResp.getRETURN_CODE())) {
                    if (!updateRecord(serialNum, jlResp.getBOSSTRANSIDD(), bossReqNum)) {
                        logger.error("吉林充值更新流水号失败,serialNum:{}.bossRespNum:{}", jlResp.getBOSSTRANSIDD());
                    }   
                }
                return new JlBossOperationResultImpl(jlResp.getRETURN_CODE(), jlResp.getRETURN_MSG());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return new JlBossOperationResultImpl("-1", "充值失败");
        }
        return new JlBossOperationResultImpl("-1", "充值失败");
    }
    
    /**
     * @param entId
     * @return
     */
    public String owePayQry(Long entId) {
        logger.info("吉林查询企业余额start!entId{}", entId);
        //检查参数
        Enterprise enterprise = null;   
        if (entId == null          
            || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null) {
            logger.info("调吉林查询企业余额接口失败：参数错误. EntId = {}", entId);
            return null;
        
        }
        
        String entCod = enterprise.getCode();
        String url = getOwePayQryUrl();
        
        String requestParameters = getParameters2(entCod);
        JlResp jlResp = null;
        try {
            String sign = Sign.toSign(requestParameters, getPrivateKeyFile());
            requestParameters = requestParameters + "&sign=" + sign;
            logger.info("吉林查询企业余额请求参数{}", requestParameters);
            String x= Sign.sendPost(url, requestParameters);//post请求 
        
            logger.info("请求响应参数{}", new String(x.getBytes("ISO-8859-1"), "UTF8"));
            System.out.println("请求响应参数：" + new String(x.getBytes("ISO-8859-1"), "UTF8"));
            Gson gson = new Gson();
            jlResp = gson.fromJson(x, JlResp.class);
            if (jlResp != null) {
                if ("0".equals(jlResp.getRETURN_CODE())) {
                    return jlResp.getBALANCE();
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (org.apache.commons.lang.StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }
    
    private String getParameters2(String entCode) {
        StringBuffer urlSb = new StringBuffer();
        urlSb.append("status=" + getStatus());
        urlSb.append(SYMBOL);
        
        urlSb.append("appKey=" + getAppkey());
        urlSb.append(SYMBOL);
        
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        urlSb.append("timeStamp=" + requestTime);
        urlSb.append(SYMBOL);
        
        urlSb.append("userName=" + getUserName());
        urlSb.append(SYMBOL);
        
        urlSb.append("phone_no=" + entCode);       
             
        return urlSb.toString();
    }

    private String getParameters(String entCode, String prdCode, String mobile, String reqNum) {
        StringBuffer urlSb = new StringBuffer();
        urlSb.append("status=" + getStatus());
        urlSb.append(SYMBOL);
        
        urlSb.append("appKey=" + getAppkey());
        urlSb.append(SYMBOL);
        
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        urlSb.append("timeStamp=" + requestTime);
        urlSb.append(SYMBOL);
        
        urlSb.append("userName=" + getUserName());
        urlSb.append(SYMBOL);
        
        urlSb.append("ENTERPRISEID=" + entCode);
        urlSb.append(SYMBOL);
        
        urlSb.append("PHONENUM=" + mobile);
        urlSb.append(SYMBOL);
        
        urlSb.append("TRANSIDO=" + reqNum);
        urlSb.append(SYMBOL);
        
        urlSb.append("PRODUCTID=" + prdCode);
        urlSb.append(SYMBOL);
        
        urlSb.append("OPERATE=0");
        urlSb.append(SYMBOL);
        
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (addr != null) {
            String ip = addr.getHostAddress().toString().replace(".", "");       
            urlSb.append("CONTACT_ID=" + ip + requestTime + "01");
        } else {
            String ip = "127001";       
            urlSb.append("CONTACT_ID=" + ip + requestTime + "01");
        }
             
        return urlSb.toString();
    }
    
    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "jilin";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        // TODO Auto-generated method stub
        return false;
    }

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_CHARGE_URL.getKey());
        //return "http://211.141.63.140:18080/rest/1.0/orderPrcSubmit";
    }
    
    private String getOwePayQryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_OWEPAY_URL.getKey());
        //return "http://211.141.63.140:18080/rest/1.0/owePayQry";
    }
    
    private String getAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_APPKEY.getKey());
        //return "10001020";
    }
    
    private String getStatus() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_STATUS.getKey());
        //return "2";
    }
    
    private String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_USERNAME.getKey());
        //return "linguangkuo";
    }
    
    private String getPrivateKeyFile() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_PRIVATEKEY_FILE.getKey());
        //return "D:\\airreCharge_private_key.pem";
    }
}
