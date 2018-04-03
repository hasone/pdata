package com.cmcc.vrp.boss.hebei;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.hebei.model.HbQueryReq;
import com.cmcc.vrp.boss.hebei.model.HbQueryResp;
import com.cmcc.vrp.boss.hebei.model.HbQueryReturnCode;
import com.cmcc.vrp.boss.hebei.model.HbWBQryAsynDealResultRsp;
import com.cmcc.vrp.boss.hebei.model.WBNetQryNewBalanceReq;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

import org.apache.axis.client.Call;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import java.net.MalformedURLException;
import java.rmi.RemoteException;


/**
 * Created by leelyn on 2016/9/28.
 */
@Service
public class HbQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(HbQueryServiceImpl.class);

    private XStream xStream;

    private static final String XMLHEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>";

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    SerialNumService serialNumService;

    /**
     * 初始化Xstream
     */
    @PostConstruct
    public void init() {
        xStream = new XStream();
        xStream.autodetectAnnotations(true);
    }

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("河北渠道开始查询");
        SerialNum serialNum;
        String bossRespNum;
        if (StringUtils.isBlank(systemNum)
                || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
                || StringUtils.isBlank(bossRespNum = serialNum.getBossRespSerialNum())) {
            LOGGER.error("订单号为空，查询失败");
            return BossQueryResult.FAILD;
        }
        String svcContReq = buildSvcCont(bossRespNum);
        String intXml = buildReqXml();
        intXml = intXml.replace("xxxxxx", svcContReq);
        //declare variable to store endpoint address,supplied by CCBS
        String endpointAddress = getQueryURL();
        //10.10.10.10:8888/axis2/services/WSInterface”;
        //initiate webservice intstance
        javax.xml.rpc.Service service = new org.apache.axis.client.Service();
        //create client Call instance
        Call call = null;
        HbWBQryAsynDealResultRsp resultRsp = null;
        try {
            call = (Call) service.createCall();
            //set endpoint address to Call instance
            call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
            //set targetnamespace of Call instance,supplied by CCBS
            QName qn = new QName("http://ws.huawei.com", "intCall");
            //invoke webservice interface
            Object obj = null;
            obj = call.invoke(qn, new Object[]{intXml});
            //get response message of webservice interface
            String ret = (String) obj;
            if (StringUtils.isBlank(ret)) {
                LOGGER.error("查询返回包体为空");
                return BossQueryResult.FAILD;
            }
            xStream.alias("IntMsg", HbQueryResp.class);
            HbQueryResp resp = (HbQueryResp) xStream.fromXML(ret);
            String svcCont;
            xStream.alias("WBQryAsynDealResultRsp", HbWBQryAsynDealResultRsp.class);

            String errCode;
            if (resp != null
                    && StringUtils.isNotBlank(svcCont = resp.getSvcCont())
                    && (resultRsp = (HbWBQryAsynDealResultRsp) xStream.fromXML(svcCont)) != null
                    && (StringUtils.isNotBlank(errCode = resultRsp.getErrCode()))) {
                if (errCode.equals(HbQueryReturnCode.SUCCESS.getCode())) {
                    return BossQueryResult.SUCCESS;
                } else if (errCode.equals(HbQueryReturnCode.PROCESSING.getCode())) {
                    LOGGER.info("订单在处理中,返回描述信息:{}", resultRsp.getErrInfo());
                    return BossQueryResult.PROCESSING;
                } else if (errCode.equals(HbQueryReturnCode.ENVIRONMENT_ERRO.getCode())) {
                    //环境问题,这种状态可能已充值成功,需继续查询
                    LOGGER.info("订单可能已经成功,需继续查询,返回描述信息:{}", resultRsp.getErrInfo());
                    return BossQueryResult.PROCESSING;
                } else if (errCode.equals(HbQueryReturnCode.FAILD.getCode())) {
                    LOGGER.info("订单失败,返回描述信息:{}", resultRsp.getErrInfo());
                    return BossQueryResult.FAILD;
                }

            }
        } catch (ServiceException e) {
            LOGGER.error("河北查询抛出异常:{}", e.getMessage());
            return BossQueryResult.PROCESSING;
        } catch (MalformedURLException e) {
            LOGGER.error("河北查询抛出异常:{}", e.getMessage());
            return BossQueryResult.PROCESSING;
        } catch (RemoteException e) {
            LOGGER.error("河北查询抛出异常:{}", e.getMessage());
            return BossQueryResult.PROCESSING;
        }
        if (resultRsp != null) {
            LOGGER.info("订单中失败,返回描述信息:{}", resultRsp.getErrInfo());
        }
        return BossQueryResult.FAILD;
    }
    
    @Override
    public BossOperationResult queryStatusAndMsg(final String systemNum) {
        final BossQueryResult queryResult = queryStatus(systemNum);
               
        return new BossOperationResult(){

            @Override
            public String getResultCode() {        
                return queryResult.getCode();
            }

            @Override
            public boolean isSuccess() {
                return queryResult.getCode().equals(BossQueryResult.SUCCESS.getCode());
            }

            @Override
            public boolean isAsync() {
                return false;
            }

            @Override
            public String getResultDesc() {
                return queryResult.getMsg();
            }

            @Override
            public Object getOperationResult() {
                return null;
            }

            @Override
            public boolean isNeedQuery() {
                return false;
            }

            @Override
            public String getFingerPrint() {
                return null;
            }

            @Override
            public String getSystemNum() {
                return systemNum;
            }

            @Override
            public Long getEntId() {
                return null;
            }
            
        };
    }

    @Override
    public String getFingerPrint() {
        return "hebei123456789";
    }

    private String buildReqXml() {
        HbQueryReq req = new HbQueryReq();
        StringBuffer sb = new StringBuffer();
        req.setChannel("nb");
        req.setCommandID("WBQryAsynDealResult");
        req.setOperId("");
        req.setPassword("9C59153D22D9F7CC021B17B425CC31C5");
        req.setProcessTime(DateUtil.getHBBossTime());
        req.setProvinceId("311");
        req.setSvcCont("xxxxxx");
        req.setTestFlag("1");
        xStream.alias("IntMsg", HbQueryReq.class);
        sb.append(XMLHEADER);
        sb.append(xStream.toXML(req));
        return sb.toString();
    }

    private String buildSvcCont(String bossRespNum) {
        WBNetQryNewBalanceReq req = new WBNetQryNewBalanceReq();
        StringBuffer sb = new StringBuffer();
        req.setAtsvNum("");
        req.setChannelId("NB");
        req.setDonorNum(getGrpsubsid());
        req.setOid(bossRespNum);
        req.setTouchOid("111111");
        xStream.alias("WBNetQryNewBalanceReq", WBNetQryNewBalanceReq.class);
        sb.append("<![CDATA[");
        sb.append(XMLHEADER);
        sb.append(xStream.toXML(req));
        sb.append("]]>");
        return sb.toString();
    }

    private String getQueryURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_QUERY_URL.getKey());
    }

    private String getGrpsubsid() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_GRPSUBSID.getKey());
    }
}