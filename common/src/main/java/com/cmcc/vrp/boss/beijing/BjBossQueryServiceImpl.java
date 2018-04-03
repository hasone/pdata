package com.cmcc.vrp.boss.beijing;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.beijing.model.OrderReqHeader;
import com.cmcc.vrp.boss.beijing.model.PhoneList;
import com.cmcc.vrp.boss.beijing.model.QueryReqBody;
import com.cmcc.vrp.boss.beijing.model.QueryReqPara;
import com.cmcc.vrp.boss.beijing.model.QueryStatus;
import com.cmcc.vrp.boss.beijing.model.QueryWebRequest;
import com.cmcc.vrp.boss.beijing.model.QueryWebResponse;
import com.cmcc.vrp.boss.beijing.util.TeaUtil;
import com.cmcc.vrp.boss.beijing.webservice.TfllbServiceServiceLocator;
import com.cmcc.vrp.boss.beijing.webservice.TfllbService_PortType;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
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
 * Created by leelyn on 2016/8/11.
 */
@Service
public class BjBossQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjBossQueryServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("北京渠道查询充值状态开始了,systemNum:{}", systemNum);
        String respStr;
        String orderId;
        SerialNum serialNum;
        //QueryWebResponse queryWebResponse = new QueryWebResponse();
        QueryWebResponse queryWebResponse;
        if (StringUtils.isBlank(systemNum)
            || (recordService.getRecordBySN(systemNum)) == null
            || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
            || StringUtils.isBlank(orderId = serialNum.getBossRespSerialNum())) {
            LOGGER.error("北京渠道查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        QueryWebRequest request = buildRequest(orderId);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("WebRequest", QueryWebRequest.class);
        String reqStr = xStream.toXML(request);
        LOGGER.info("北京渠道查询充值状态的请求包体:{}", reqStr);
        try {
            TfllbServiceServiceLocator serviceLocator = new TfllbServiceServiceLocator();
            TfllbService_PortType portType = serviceLocator.getTfllbService();
            xStream.alias("WebResponse", QueryWebResponse.class);
            PhoneList list;
            if (StringUtils.isNotBlank(respStr = portType.tf_ddmxcx(reqStr))
                && (queryWebResponse = (QueryWebResponse) xStream.fromXML(respStr)) != null
                && ((list = queryWebResponse.getRespBody().getQueryRetInfo().getPhoneList())) != null) {
                String returnCode = list.getState();
                if (returnCode.equals(QueryStatus.SUCCESS_DEAL.getCode())) {
                    return BossQueryResult.SUCCESS;
                } else if (returnCode.equals(QueryStatus.FAILD_DEAL.getCode())) {
                    LOGGER.error("查询北京充值渠道返回失败,原因:{},平台流水:{},BOSS返回流水:{}", list.getDetail(), systemNum, orderId);
                    return BossQueryResult.FAILD;
                } else if (returnCode.equals(QueryStatus.HAS_SUBMIT.getCode())) {
                    LOGGER.info("查询北京充值渠道返回已提交");
                    return BossQueryResult.PROCESSING;
                } else if (returnCode.equals(QueryStatus.NO_DEAL.getCode())) {
                    LOGGER.info("查询北京充值渠道返回未受理");
                    return BossQueryResult.PROCESSING;
                }
            }
        } catch (ServiceException e) {
            LOGGER.error("北京渠道查询充值状态抛出异常:{}", e);
            return BossQueryResult.EXCEPTION;
        } catch (RemoteException e) {
            LOGGER.error("北京渠道查询充值状态抛出异常:{}", e);
            return BossQueryResult.EXCEPTION;
        } catch (Exception e) {
            LOGGER.error("北京渠道查询充值状态抛出异常:{}", e);
            return BossQueryResult.EXCEPTION;
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
        return "beijing123456789";
    }

    private QueryWebRequest buildRequest(String orderId) {
        QueryWebRequest webRequest = new QueryWebRequest();
        webRequest.setHeader(buildHeader());
        webRequest.setWebBody(buildBody(orderId));
        return webRequest;
    }

    private OrderReqHeader buildHeader() {
        OrderReqHeader reqHeader = new OrderReqHeader();
        reqHeader.setChannelId(TeaUtil.encrypt("EDSMP"));
        reqHeader.setRequstTime(TeaUtil.encrypt(DateUtil.getBjBossTime()));
        return reqHeader;
    }

    private QueryReqBody buildBody(String orderId) {
        QueryReqBody reqBody = new QueryReqBody();
        reqBody.setPara(buildPara(orderId));
        return reqBody;
    }

    private QueryReqPara buildPara(String orderId) {
        QueryReqPara param = new QueryReqPara();
        param.setPassword(TeaUtil.encrypt(getPassWord()));
        param.setCa(TeaUtil.encrypt(getCa()));
        param.setCurrentPage(TeaUtil.encrypt("1"));
        param.setAdminName(TeaUtil.encrypt(getAdminName()));
        param.setNumber(TeaUtil.encrypt("5"));
        param.setOrderId(TeaUtil.encrypt(orderId));
        return param;
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

