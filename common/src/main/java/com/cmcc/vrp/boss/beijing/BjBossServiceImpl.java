package com.cmcc.vrp.boss.beijing;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.beijing.model.BjReturnCode;
import com.cmcc.vrp.boss.beijing.model.OrderReqBody;
import com.cmcc.vrp.boss.beijing.model.OrderReqHeader;
import com.cmcc.vrp.boss.beijing.model.OrderReqPara;
import com.cmcc.vrp.boss.beijing.model.OrderRespHeader;
import com.cmcc.vrp.boss.beijing.model.OrderWebRequest;
import com.cmcc.vrp.boss.beijing.model.OrderWebResponse;
import com.cmcc.vrp.boss.beijing.util.TeaUtil;
import com.cmcc.vrp.boss.beijing.webservice.TfllbServiceServiceLocator;
import com.cmcc.vrp.boss.beijing.webservice.TfllbService_PortType;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * Created by leelyn on 2016/7/19.
 */
@Service
public class BjBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjBossServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("北京渠道开始充值");
        SupplierProduct sPrdouct;
        String respStr = null;
        String pCode = null;
        String reqStr = null;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            return new BjBossOperationResultImpl(BjReturnCode.PARA_ILLEGALITY);
        }
        TfllbServiceServiceLocator serviceLocator = new TfllbServiceServiceLocator();
        OrderWebResponse orderResp;
        try {
            TfllbService_PortType portType = serviceLocator.getTfllbService();
            OrderReqHeader reqHeader = buildOrderHeader();
            OrderReqPara reqPara = buildORPara(mobile, pCode);
            OrderReqBody reqBody = buildOrderBody(reqPara);
            if (StringUtils.isNotBlank(reqStr = buildOrderXML(reqHeader, reqBody))
                    && StringUtils.isNotBlank(respStr = portType.tf_tjzd(reqStr))) {
                XStream xStream = new XStream();
                xStream.autodetectAnnotations(true);
                xStream.alias("WebResponse", OrderWebResponse.class);
                orderResp = (OrderWebResponse) xStream.fromXML(respStr);
                OrderRespHeader header = new OrderRespHeader();
                if (orderResp != null
                        && (header = orderResp.getRespHeader()) != null
                        && header.getRetCode() == Integer.parseInt(BjReturnCode.SUCCESS.getCode())) {
                    //充值成功更新流水关系
                    updateSerialNum(serialNum, orderResp.getRespBody().getRetInfo().getOrderId(),
                            SerialNumGenerator.buildNullBossReqNum("beijing"));
                    BjBossOperationResultImpl result = new BjBossOperationResultImpl(BjReturnCode.SUCCESS);
                    result.setFingerPrint(getFingerPrint());
                    result.setSystemNum(serialNum);
                    result.setEntId(entId);
                    return result;
                }

                if (header != null) {
                    LOGGER.error("Beijing boss channel charge faild,errcode:{}.errmsg:{}", header.getRetCode(), header.getRetDesc());
                } else {
                    LOGGER.error("Beijing boss channel charge faild,errcode:{}.errmsg:{}", "", "");
                }

                //充值失败更新流水关系
                updateSerialNum(serialNum, orderResp == null ? SerialNumGenerator.buildNullBossRespNum("beijing")
                        : orderResp.getRespBody().getRetInfo().getOrderId(), SerialNumGenerator.buildNullBossReqNum("beijing"));
                return new BjBossOperationResultImpl(BjReturnCode.FAILD);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            LOGGER.error("involker WS has throw e:{}", e);
        } catch (Exception e) {
            LOGGER.error("Translate Xml:{} to Object throw e:{}", e, reqStr);
        }
        //充值失败更新流水关系
        updateSerialNum(serialNum, SerialNumGenerator.buildNullBossRespNum("beijing"),
                SerialNumGenerator.buildNullBossReqNum("beijing"));
        return new BjBossOperationResultImpl(BjReturnCode.FAILD);
    }

    @Override
    public String getFingerPrint() {
        return "beijing123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
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

    private String buildOrderXML(OrderReqHeader reqHeader, OrderReqBody reqBody) {
        if (reqHeader == null
                || reqBody == null) {
            return null;
        }
        OrderWebRequest webRequest = new OrderWebRequest();
        webRequest.setReqHeader(reqHeader);
        webRequest.setReqBody(reqBody);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("WebRequest", OrderWebRequest.class);
        return xStream.toXML(webRequest);
    }

    private OrderReqHeader buildOrderHeader() {
        OrderReqHeader reqHeader = new OrderReqHeader();
        reqHeader.setChannelId(TeaUtil.encrypt("EDSMP"));
        reqHeader.setRequstTime(TeaUtil.encrypt(DateUtil.getBjBossTime()));
        return reqHeader;
    }

    private OrderReqBody buildOrderBody(OrderReqPara reqPara) {
        OrderReqBody reqBody = new OrderReqBody();
        reqBody.setReqPara(reqPara);
        return reqBody;
    }

    private OrderReqPara buildORPara(String phone, String pCode) {
        OrderReqPara reqPara = new OrderReqPara();
        reqPara.setAdminName(TeaUtil.encrypt(getAdminName()));
        reqPara.setCa(TeaUtil.encrypt(getCa()));
        reqPara.setPassWord(TeaUtil.encrypt(getPassWord()));
        reqPara.setResName(TeaUtil.encrypt(pCode));
        reqPara.setPhoneList(TeaUtil.encrypt(phone));
        return reqPara;
    }

    private void updateSerialNum(String serialNum, String bossRespNum, String bossReqNum) {
        if (updateRecord(serialNum, bossRespNum, bossReqNum)) {
            LOGGER.info("记录平台流水和BOSS返回流水号的关系成功");
        } else {
            LOGGER.error("记录平台流水和BOSS返回流水号的关系失败");
        }
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private String getAdminName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_ADMIN_NAME.getKey());
    }

    private String getCa() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_CA.getKey());
    }

    private String getPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_PASSWORD.getKey());
    }
}
