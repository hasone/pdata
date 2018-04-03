package com.cmcc.vrp.boss.hebei;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.hebei.model.BizProcReq;
import com.cmcc.vrp.boss.hebei.model.BizProcRsp;
import com.cmcc.vrp.boss.hebei.model.HbReturnCode;
import com.cmcc.vrp.boss.hebei.model.InterBOSSReq;
import com.cmcc.vrp.boss.hebei.model.InterBOSSResp;
import com.cmcc.vrp.boss.hebei.model.Response;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/8/19.
 */
@Service
public class HbBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HbBossServiceImpl.class);
    private static final String XMLHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    @Autowired
    private SupplierProductService productService;
    @Autowired
    private SerialNumService serialNumService;
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("河北渠道充值开始");
        SupplierProduct sPrdouct;
        String pCode;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            return new HbBossOperationResultImpl(HbReturnCode.PARA_ERROR);
        }
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("hebei", 25);
        BizProcReq procReq = buildBPR(mobile, pCode);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("BizProcReq", BizProcReq.class);
        String bizReqStr = buildSocContStr(xStream.toXML(procReq));
        InterBOSSReq bossReq = buildIBR("XXXXXX", bossReqNum);
        xStream.alias("InterBOSS", InterBOSSReq.class);
        String reqStr = xStream.toXML(bossReq);
        reqStr = reqStr.replace("XXXXXX", bizReqStr);
        reqStr = buildReqStr(reqStr);
        // 发起请求
        LOGGER.info("河北渠道地址:{},充值请求:{}", getURL(), reqStr);
        String result = HttpUtils.post(getURL(), reqStr, "application/x-www-form-urlencoded");
        LOGGER.info("河北渠道充值响应:{}", result);
        InterBOSSResp bossResp;
        Response response = null;
        String rspCode = null;
        String svcCont = null;
        String bossRespNum = null;
        if (StringUtils.isNotBlank(result)
                && (bossResp = xml2Obj(InterBOSSResp.class, "InterBOSS", result)) != null
                && (response = bossResp.getResponse()) != null
                && StringUtils.isNotBlank(rspCode = response.getRspCode())
                && StringUtils.isNotBlank(svcCont = bossResp.getSvcCont())) {
            xStream.alias("BizProcRsp", BizProcRsp.class);
            BizProcRsp rsp = (BizProcRsp) xStream.fromXML(svcCont);
            bossRespNum = rsp.getOID();
            if (rspCode.equals(HbReturnCode.SUCCESS.getCode())) {
                HbBossOperationResultImpl operationResult = new HbBossOperationResultImpl(HbReturnCode.SUCCESS);
                operationResult.setEntId(entId);
                operationResult.setSystemNum(serialNum);
                operationResult.setFingerPrint(getFingerPrint());
                //更新流水
                if (!updateRecord(serialNum, bossRespNum, bossReqNum)) {
                    LOGGER.error("更新流水失败");
                }
                return operationResult;
            } else if (rspCode.equals(HbReturnCode.SYSTEM_ERROR.getCode())) {
                //根据BOSS的要求,这种状态可能已充值成功,调接口查询流水状态
                LOGGER.info("这种状态可能已充值成功,需查询流水状态，原因:{}", response.getRspDesc());
                HbBossOperationResultImpl operationResult = new HbBossOperationResultImpl(HbReturnCode.SUCCESS);
                operationResult.setEntId(entId);
                operationResult.setSystemNum(serialNum);
                operationResult.setFingerPrint(getFingerPrint());
                //更新流水
                if (!updateRecord(serialNum, bossRespNum, bossReqNum)) {
                    LOGGER.error("更新流水失败");
                }
                return operationResult;
            } else if (rspCode.equals(HbReturnCode.PARA_ERROR.getCode())) {
                LOGGER.error("充值失败,原因:{}", response.getRspDesc());
                //更新流水
                if (!updateRecord(serialNum, bossRespNum, bossReqNum)) {
                    LOGGER.error("更新流水失败");
                }
                return new HbBossOperationResultImpl(HbReturnCode.PARA_ERROR);
            }
        }
        if (response != null) {
            LOGGER.error("充值失败,原因:{}", response.getRspDesc());
        }
        //更新流水
        if (updateRecord(serialNum, bossRespNum = StringUtils.isBlank(bossRespNum) ? SerialNumGenerator.buildNullBossRespNum("Hebei")
                : bossRespNum, bossReqNum)) {
            LOGGER.error("更新流水失败");
        }
        return new HbBossOperationResultImpl(HbReturnCode.FAILD);
    }

    private InterBOSSReq buildIBR(String svccont, String bossReqNum) {
        InterBOSSReq req = new InterBOSSReq();
        req.setOrigDomain("ZSLL");
        req.setHomeDomain("BOSS");
        req.setBipCode("ZSB0001");
        req.setActivtyCode("ZST0001");
        req.setActionCode("0");
        req.setProcID(bossReqNum);
        req.setTransIDO(bossReqNum);
        req.setProcessTime(DateUtil.getHBBossTime());
        req.setSvcCont(svccont);
        return req;
    }

    private BizProcReq buildBPR(String mobile, String pCode) {
        BizProcReq req = new BizProcReq();
        req.setGroupNo(getGroupNo());
        req.setGrpsubSid(getGrpsubsid());
        req.setMsisdn(mobile);
        req.setFluxamt(pCode);
        req.setSendsms("0");
        req.setASYNORDER("2");
        return req;
    }

    private <T> T xml2Obj(Class<T> cls, String rootTag, String data) {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias(rootTag, cls);
        return (T) xStream.fromXML(data);
    }

    private String buildSocContStr(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append("<![CDATA[");
        sb.append(XMLHEADER);
        sb.append(data);
        sb.append("]]>");
        return sb.toString();
    }

    private String buildReqStr(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append(XMLHEADER);
        sb.append(data);
        return sb.toString();
    }

    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum)
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
        return "hebei123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    private String getGroupNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_GROUPNO.getKey());
    }

    private String getGrpsubsid() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_GRPSUBSID.getKey());
    }

    private String getURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_URL.getKey());
    }
}
