package com.cmcc.vrp.boss.shanghai.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.util.json.JSONObject;
import com.chinamobile.cn.openapi.sdk.v2.client.OpenapiHttpCilent;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;
import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.queryallnet.QanAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.queryallnet.QanItem;
import com.cmcc.vrp.boss.shanghai.model.queryallnet.QanRetInfo;
import com.cmcc.vrp.boss.shanghai.model.queryallnet.QueryAllNetReq;
import com.cmcc.vrp.boss.shanghai.model.querycount.InfoList;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlow;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlowReq;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlowResult;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlowRetInfo;
import com.cmcc.vrp.boss.shanghai.model.querymemberrole.QmrAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.querymemberrole.QmrRetInfo;
import com.cmcc.vrp.boss.shanghai.model.querymemberrole.QueryMemberRoleReq;
import com.cmcc.vrp.boss.shanghai.model.querymemberrole.RoleItem;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QueryOrderStateReq;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.ProductItem;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.QpAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.QpRetInfo;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.QueryProductReq;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.ShBossProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by lilin on 2016/8/23.
 */
public abstract class AbstractShBossService implements ShBossService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbstractShBossService.class);

    @Autowired
    private Gson gson = new Gson();

    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private SupplierProductService supplierProductService;

    public AbstractShBossService() {
        super();
    }

    protected abstract InMap getMap(Map map);

    protected abstract Request getRequest(InMap inMap);

    @Override
    public AsiaDTO queryAllGroupOrderInfo(String custServiceId) {
        String reqStr;
        String serialNum;
        Map map = new HashMap();
        map.put("custServiceId", custServiceId);
        InMap inMap = getMap(map);
        LOGGER.info("集团全网订购查询 inMap:", gson.toJson(inMap));
        QueryAllNetReq allNetReq = buildQANR(inMap);
        try {
            OpenapiHttpCilent cilent = new OpenapiHttpCilent(getAppCode(), getApk());
            reqStr = new Gson().toJson(allNetReq);
            serialNum = SerialNumGenerator.buildSerialNum();
            LOGGER.info("集团全网订购查询请求包体:{},接口请求流水:{}", reqStr, serialNum);
            String resp = cilent.call(getApiCode(), serialNum, reqStr);
            LOGGER.info("集团全网订购查询响应包体:{}", resp);
            JSONObject jsonObj;
            String status;
            String response;
            QanAsiaResult result;
            ErrorInfo errorInfo;
            QanRetInfo qalRetInfo;
            if (StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, QanAsiaResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (qalRetInfo = result.getResponse().getRetInfo()) != null
                    && (qalRetInfo.getReturnCode().equals(ShReturnCode.SUCCESS.getCode()))) {
                List<QanItem> list = qalRetInfo.getReturnContent();
                if (!list.isEmpty()) {
                    AsiaDTO asiaDTO = new AsiaDTO();
                    asiaDTO.setOfferId(list.get(0).getOfferId());
                    asiaDTO.setBbossInsOfferId(list.get(0).getBbossInsOfferId());
                    return asiaDTO;
                } else {
                    LOGGER.error("集团编码下挂的订购组为空");
                }
            }
        } catch (Exception e) {
            LOGGER.error("集团全网订购查询抛出异常:{}", e.getMessage());
        }
        return null;
    }

    @Override
    public AsiaDTO queryMemberRoleByOfferId(AsiaDTO asiaDTO) {
        String reqStr;
        String serialNum;
        Map map = new HashMap();
        map.put("offerId", asiaDTO.getOfferId());
        InMap inMap = getMap(map);
        LOGGER.info("策划编号查询成员角色 inMap:", gson.toJson(inMap));
        QueryMemberRoleReq memberRoleReq = buildQMRR(inMap);
        try {
            OpenapiHttpCilent cilent = new OpenapiHttpCilent(getAppCode(), getApk());
            reqStr = new Gson().toJson(memberRoleReq);
            serialNum = SerialNumGenerator.buildSerialNum();
            LOGGER.info("策划编号查询成员角色请求包体:{},接口请求流水:{}", reqStr, serialNum);
            String resp = cilent.call(getApiCode(), serialNum, reqStr);
            LOGGER.info("策划编号查询成员角色响应包体:{}", resp);
            JSONObject jsonObj;
            String status;
            String response;
            QmrAsiaResult result;
            ErrorInfo errorInfo;
            QmrRetInfo qmrRetInfo;
            if (StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, QmrAsiaResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (qmrRetInfo = result.getResponse().getRetInfo()) != null
                    && (qmrRetInfo.getReturnCode().equals(ShReturnCode.SUCCESS.getCode()))) {
                List<RoleItem> list = qmrRetInfo.getReturnContent();
                if (!list.isEmpty()) {
                    String roleId;
                    for (RoleItem item : list) {
                        if (item.getIsMain().equals("0")
                                && StringUtils.isNotBlank(roleId = item.getRoleId())) {
                            asiaDTO.setRoleId(roleId);
                            return asiaDTO;
                        }
                    }
                } else {
                    LOGGER.error("策划编号对应的角色为空");
                }
            }
        } catch (Exception e) {
            LOGGER.error("策划编号查询成员角色抛出异常:{}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<ProductItem> queryProductByOfferIdAndRoleId(AsiaDTO asiaDTO) {
        
        List<ProductItem> productItems = getProductItems();
        if (needMock()) {
            return productItems;
        }
   
        
        String reqStr;
        String serialNum;
        Map map = new HashMap();
        map.put("offerId", asiaDTO.getOfferId());
        map.put("roleId", asiaDTO.getRoleId());
        InMap inMap = getMap(map);
        System.out.println("策划编号及成员角色查询套餐下挂产品 inMap:"+ gson.toJson(inMap));
        QueryProductReq productReq = buildQPR(inMap);
        try {
            com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent cilent = 
                    new com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent(getAppCode(), getApk(), getSecurityUrl(), getOpenapiUrl());
            reqStr = new Gson().toJson(productReq);
            serialNum = SerialNumGenerator.buildSerialNum();
            System.out.println("策划编号及成员角色查询套餐下挂产品请求包体:{},接口请求流水:{}" + reqStr +serialNum);
            String resp = cilent.call("CRM4186", serialNum, reqStr);
            System.out.println("策划编号及成员角色查询套餐下挂产品响应包体:{}" + resp);
            JSONObject jsonObj;
            String status;
            String response;
            QpAsiaResult result;
            ErrorInfo errorInfo;
            QpRetInfo qpRetInfo;
            if (StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, QpAsiaResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (qpRetInfo = result.getResponse().getRetInfo()) != null
                    && (qpRetInfo.getReturnCode().equals(ShReturnCode.SUCCESS.getCode()))) {
                LOGGER.info("成功获取订购组中的产品列表");
                return qpRetInfo.getReturnContent();
            }
        } catch (Exception e) {
            LOGGER.error("策划编号及成员角色查询套餐下挂产品抛出异常:{}", e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<InfoList> queryUsableBalanceOfFlow(QueryUsableBalanceOfFlow queryUsableBalanceOfFlow) {
        LOGGER.info("流量统付订购组的账户余额查询start");
        List<InfoList> infoLists = new ArrayList<InfoList>();
        InfoList infoList = new InfoList();
        infoList.setTotalFee(SerialNumGenerator.genRandomNum(3));
        infoLists.add(infoList);
        if (needMock()) {
            return infoLists;
        }
        String reqStr;
        String serialNum;
        Map map = new HashMap();
        map.put("acctId", queryUsableBalanceOfFlow.getAccId());
        map.put("mainBillId", queryUsableBalanceOfFlow.getMainBillId());
        map.put("offerId", queryUsableBalanceOfFlow.getOfferId());
        InMap inMap = getMap(map);
        LOGGER.info("流量统付订购组的账户余额查询 inMap:", gson.toJson(inMap));
        QueryUsableBalanceOfFlowReq queryReq = buildQUBOF(inMap);
        try {
            com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent cilent = 
                    new com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent(getNewAppCode(), getNewApk(), getSecurityUrl(), getOpenapiUrl());
            reqStr = new Gson().toJson(queryReq);
            serialNum = SerialNumGenerator.buildSerialNum();
            LOGGER.info("流量统付订购组的账户余额查询请求包体:{},接口请求流水:{}", reqStr, serialNum);
            String resp = cilent.call("CRM4731", serialNum, reqStr);
            JSONObject jsonObj;
            String status;
            String response;
            QueryUsableBalanceOfFlowResult result;
            ErrorInfo errorInfo;
            QueryUsableBalanceOfFlowRetInfo retInfo;
            if (StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, QueryUsableBalanceOfFlowResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (retInfo = result.getResponse().getRetInfo()) != null
                    && (!retInfo.getInfoList().isEmpty())) {
                LOGGER.info("成功获取订购组中的余额信息");
                return retInfo.getInfoList();
            }
        } catch (Exception e) {
            LOGGER.error("流量统付订购组的账户余额查询抛出异常:{}" + e.getMessage());
        }
        return null;
    }
    
    private QueryUsableBalanceOfFlowReq buildQUBOF(InMap inMap) {
        QueryUsableBalanceOfFlowReq queryReq = new QueryUsableBalanceOfFlowReq();
        queryReq.setPubInfo(getQUBOFPubInfo());
        queryReq.setRequest(getRequest(inMap));
        return queryReq;
    }
    
    private PubInfo getQUBOFPubInfo() {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setClientIP("10.10.141.98");
        pubInfo.setCountyCode("021");
        pubInfo.setInterfaceId("115");
        pubInfo.setOpId("999990077");
        pubInfo.setRegionCode("210");
        pubInfo.setTransactionId(SerialNumGenerator.buildSerialNum());
        pubInfo.setTransactionTime(DateUtil.getShBossTime());
        pubInfo.setOrgId("1");
        pubInfo.setInterfaceType("06");
        return pubInfo;
    }

    private QueryOrderStateReq buildQOSR(InMap inMap) {
        QueryOrderStateReq stateReq = new QueryOrderStateReq();
        stateReq.setPubInfo(getPubInfo());
        stateReq.setRequest(getRequest(inMap));
        return stateReq;
    }

    private QueryAllNetReq buildQANR(InMap inMap) {
        QueryAllNetReq netReq = new QueryAllNetReq();
        netReq.setRequest(getRequest(inMap));
        netReq.setPubInfo(getPubInfo());
        return netReq;
    }

    private QueryMemberRoleReq buildQMRR(InMap inMap) {
        QueryMemberRoleReq roleReq = new QueryMemberRoleReq();
        roleReq.setRequest(getRequest(inMap));
        roleReq.setPubInfo(getPubInfo());
        return roleReq;
    }

    private QueryProductReq buildQPR(InMap inMap) {
        QueryProductReq productReq = new QueryProductReq();
        productReq.setPubInfo(getPubInfo());
        productReq.setRequest(getRequest(inMap));
        return productReq;
    }


    private PubInfo getPubInfo() {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setClientIP("10.10.141.98");
        pubInfo.setCountyCode("021");
        pubInfo.setInterfaceId("79");
        pubInfo.setOpId("999990077");
        pubInfo.setRegionCode("021");
        pubInfo.setTransactionId(SerialNumGenerator.buildSerialNum());
        pubInfo.setTransactionTime(DateUtil.getShBossTime());
        pubInfo.setOrgId("1");
        pubInfo.setInterfaceType("06");
        return pubInfo;
    }
    /**
     * @title: getDynamicProxyFlag
     * */
    private Boolean needMock() {
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEED_MOCK.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? "false" : dynamicFlag;
        return Boolean.parseBoolean(finalFlag);
    }
    
    /**
     * mock
     * @return
     */
    public List<ProductItem> getProductItems() {
        List<ProductItem> productItems = new ArrayList<ProductItem>();
        List<ShBossProduct> shBossProducts = supplierProductService.getShBossProducts();
        for(ShBossProduct shBossProduct:shBossProducts) {
            ProductItem productItem = new ProductItem();
            productItem.setProdRate(String.valueOf(shBossProduct.getSupplierProductId()));
            productItem.setProdId(String.valueOf(shBossProduct.getSupplierProductId()));
            productItem.setProductName("通用流量" + shBossProduct.getSupplierProductSize());
            productItems.add(productItem);
        }
        return productItems;
    }

    private String getAppCode() {
        //return "A0002019";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APPCODE.getKey());
    }

    private String getApk() {
        //return "hgH7terOSW5uYQvHUY/jrXZKcAjsyF3q";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APK.getKey());
    }

    private String getApiCode() {
        //return "CRM4731";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APICODE.getKey());
    }
    
    private String getNewAppCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_APPCODE.getKey());
    }
    
    private String getNewApk() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_APK.getKey());
    }
    
    private String getSecurityUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_SECURITY_URL.getKey());
        //return "http://211.136.164.123/open/security";
    }
    
    private String getOpenapiUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_OPENAPI_URL.getKey());
        //return "http://211.136.164.123/open/service";
    }

}
