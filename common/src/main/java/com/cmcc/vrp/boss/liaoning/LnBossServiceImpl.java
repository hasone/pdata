package com.cmcc.vrp.boss.liaoning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.openplatform.utils.AIESBConstants;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.liaoning.model.LnBossOperationResultImpl;
import com.cmcc.vrp.boss.liaoning.model.LnChargeResp;
import com.cmcc.vrp.boss.liaoning.util.LnGlobalConfig;
import com.cmcc.vrp.boss.liaoning.util.SignUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.google.gson.Gson;

import net.sf.json.JSONObject;


/**
* <p>Title: LnBossServiceImpl</p>
* <p>Description: 辽宁充值订购接口</p>
* @author lgk8023
* @date 2016年12月30日 下午3:05:30
*/
@Service("lnBossService")
public class LnBossServiceImpl implements BossService{
    private static final Logger logger = LoggerFactory.getLogger(LnBossServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    @Autowired
    private SupplierProductService supplierProductService;
    
    @Autowired
    private EnterprisesService enterprisesService;
    
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    LnGlobalConfig lnGlobalConfig;
	
    @Override
	public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
		
        logger.info("辽宁充值start!");
	        //检查参数
        SupplierProduct supplierProduct = null;
        if (splPid == null
	            || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
	            || (enterprisesService.selectByPrimaryKey(entId)) == null
	            || StringUtils.isBlank(mobile)
	            || StringUtils.isBlank(serialNum)) {
            logger.info("调辽宁BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String bossReqNum = SerialNumGenerator.genRandomNum(15);
        System.out.println(bossReqNum);
        Map<String, String> sysParams = buildSysParam(serialNum, requestTime);
        String busiParams = buildBusiParam(mobile, supplierProduct, bossReqNum, requestTime).toString();
        logger.info("辽宁充值请求系统参数" + sysParams);
        logger.info("辽宁充值请求业务参数" + busiParams);
        LnChargeResp resp = null;
        try {
            String response = SignUtil.execute(sysParams, busiParams, AIESBConstants.PROTOCOL.HTTP, lnGlobalConfig.getAppKey(), lnGlobalConfig.getUrl());
            logger.info("辽宁boss返回：" + response);
            if (StringUtils.isNotBlank(response)
                    && (resp = gson.fromJson(response, LnChargeResp.class)) != null) {
                String respCode = resp.getResponse().getRetInfo().getErrorInfo().getCode();
                String respDesc = resp.getResponse().getRetInfo().getErrorInfo().getMessage();
                if ("0000".equals(respCode)) {
                    if (!updateRecord(serialNum, resp.getResponse().getRetInfo().getSuccessNO(), bossReqNum)) {
                        logger.error("辽宁充值更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, resp.getResponse().getRetInfo().getSuccessNO());
                    }
                } else {
                    if (!updateRecord(serialNum, resp.getResponse().getRetInfo().getFailureNO(), bossReqNum)) {
                        logger.error("辽宁充值更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, resp.getResponse().getRetInfo().getSuccessNO());
                    }
                }
                LnBossOperationResultImpl bossOperationResultImpl = new LnBossOperationResultImpl(respCode, respDesc);
                bossOperationResultImpl.setEntId(entId);
                bossOperationResultImpl.setSystemNum(serialNum);
                bossOperationResultImpl.setFingerPrint(getFingerPrint());
                return bossOperationResultImpl;
            }
                
        } catch (InterruptedException e) {
            logger.error(e.getMessage());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
            
        return new LnBossOperationResultImpl("1000", "充值失败");
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
    
    @Override
	public String getFingerPrint() {
        return "liaoning";
    }

    @Override
	public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
	public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }
    
    
	/**
	 * @param token
	 * @param openid
	 * @param busiSerial
	 * @return
	 */
    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {
        
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "OI_GroupPay");
        sysParam.put("format", "json");
        sysParam.put("timestamp", requestTime);
        sysParam.put("appId", lnGlobalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("operId", lnGlobalConfig.getOperId());
        sysParam.put("accessToken", lnGlobalConfig.getAccessToken());
        sysParam.put("openId", lnGlobalConfig.getOpenId());
        sysParam.put("busiSerial", bossReqNum);
        return sysParam;
    }
	
	/**
	 * @param mobile
	 * @param sPrdouct
	 * @param bossReqNum
	 * @return
	 */
    private JSONObject buildBusiParam(String mobile, SupplierProduct sPrdouct, String bossReqNum, String requestTime) {
        JSONObject busiParam = new JSONObject();	
        busiParam.put("Request", buildRequest(mobile, requestTime, sPrdouct, bossReqNum));
        busiParam.put("PubInfo", buildPubInfo(bossReqNum, requestTime));
		
        return busiParam;
    }
	
    private JSONObject buildRequest(String mobile, String requestTime, SupplierProduct sPrdouct, String bossReqNum) {
        JSONObject request = new JSONObject();
        request.put("BusiParams", buildBusiParams(mobile, requestTime, sPrdouct, bossReqNum));
        request.put("BusiCode", "OI_GroupPay");	
        return request;
    }
	
    private JSONObject buildBusiParams(String mobile, String requestTime, SupplierProduct sPrdouct, String bossReqNum) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String createDate = sdf.format(new Date());
        JSONObject busiParam = new JSONObject();
        busiParam.put("smsFlag", "0");
        busiParam.put("AttrList", buildAttrList().toString());
        busiParam.put("CreateDate", createDate);
        busiParam.put("ApplyBillId", "13514219988");
        busiParam.put("CustId", lnGlobalConfig.getCustId());
        busiParam.put("ReviewBillId", "18740080506");
        busiParam.put("BillList", buildBillList(mobile).toString());
        //busiParam.put("ProdId", "122192200037");
        busiParam.put("ProdId", sPrdouct.getCode());
        busiParam.put("OfferId", "112001010634");
        busiParam.put("Region", "416");
        busiParam.put("EXT2", "");
        busiParam.put("EXT1", "");
        busiParam.put("BatchNO", bossReqNum);
        return busiParam;
    }
	
	/**
	 * @return
	 */
    private JSONObject buildPubInfo(String bossReqNum, String requestTime) {
        JSONObject pubInfo = new JSONObject();
        pubInfo.put("TransactionTime", requestTime);
        pubInfo.put("OrgId", "40001000");
        pubInfo.put("ClientIP", "");
        pubInfo.put("RegionCode", "400");
        pubInfo.put("CountyCode", "4000");
        pubInfo.put("InterfaceType", "88");
        pubInfo.put("TransactionId", bossReqNum);
        pubInfo.put("OpId", "40051860");
        pubInfo.put("InterfaceId", "81");
        return pubInfo;
    }
	
	/**
	 * @return
	 */
    private List<JSONObject> buildAttrList() {
        JSONObject attr1 = new JSONObject();
        attr1.put("AttrValue", lnGlobalConfig.getGiveMonth());
        attr1.put("AttrId", "152011029848");
		
        JSONObject attr2 = new JSONObject();
        attr2.put("AttrValue", lnGlobalConfig.getEffectiveWay());
        attr2.put("AttrId", "152011029726");
		
        JSONObject attr3 = new JSONObject();
        attr3.put("AttrValue", lnGlobalConfig.getSendMsg());
        attr3.put("AttrId", "152011057779");
        List<JSONObject> attrList = new ArrayList<JSONObject>();
        attrList.add(attr1);
        attrList.add(attr2);
        attrList.add(attr3);
        return attrList;	
    }
	
	/**
	 * @return
	 */
    private List<JSONObject> buildBillList(String mobile) {
        JSONObject bill = new JSONObject();
        bill.put("BillId", mobile);
        List<JSONObject> billList = new ArrayList<JSONObject>();
        billList.add(bill);
        return billList;
    }

}
