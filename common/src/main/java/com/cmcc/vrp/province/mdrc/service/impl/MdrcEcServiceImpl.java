package com.cmcc.vrp.province.mdrc.service.impl;

import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.exception.PreconditionRequiredException;
import com.cmcc.vrp.exception.UnprocessableEntityException;
import com.cmcc.vrp.province.mdrc.enums.MdrcEcOperationStatusEnum;
import com.cmcc.vrp.province.mdrc.model.MdrcEcCardQueryInfo;
import com.cmcc.vrp.province.mdrc.model.MdrcEcRequestData;
import com.cmcc.vrp.province.mdrc.model.MdrcEcResponseData;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcEcService;
import com.cmcc.vrp.province.mdrc.utils.CardNumCodec;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.webservice.constants.Constant.Common;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 流量卡接口服务
 * <p>
 * Created by sunyiwei on 2016/5/31.
 */
@Service("mdrcEcService")
public class MdrcEcServiceImpl implements MdrcEcService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MdrcEcServiceImpl.class);

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    ProductService productService;

    @Override
    public MdrcEcResponseData operate(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        MdrcEcResponseData mdrcEcResponse = null;
        if (mdrcEcRequest == null || mdrcEcRequest.getOperateType() == null) {
            LOGGER.error("无效的状态变更请求参数. MdrcEcRequestData = {}.", new Gson().toJson(mdrcEcRequest));
            throw new UnprocessableEntityException("invalid status change");
        }

        if (mdrcEcRequest.getOriginCardInfo() == null || StringUtils.isBlank(mdrcEcRequest.getOriginCardInfo())
            || mdrcEcRequest.getOriginCardInfo().length() > Common.MAX_REMARKS_LENGTH) {
            LOGGER.error("无效的状态变更请求参数. MdrcEcRequestData = {}.", new Gson().toJson(mdrcEcRequest));
            throw new UnprocessableEntityException("invalid card numbers");
        }

        //根据不同的变更类型进行操作
        switch (mdrcEcRequest.getOperateType()) {
            case STOCKIN:
                mdrcEcResponse = stockIn(mdrcEcRequest);
                break;
            case ACTIVATE:
                mdrcEcResponse = activate(mdrcEcRequest);
                break;
            case DEACTIVATE:
                mdrcEcResponse = deactivate(mdrcEcRequest);
                break;
            case DELETE:
                mdrcEcResponse = delete(mdrcEcRequest);
                break;
            case LOCK:
                mdrcEcResponse = lock(mdrcEcRequest);
                break;
            case UNLOCK:
                mdrcEcResponse = unlock(mdrcEcRequest);
                break;
            case EXTEND:
                mdrcEcResponse = extend(mdrcEcRequest);
                break;
            default:
                break;
        }

        return mdrcEcResponse;
    }

    @Override
    public MdrcEcCardQueryInfo queryCardInfo(String cardNumber) {
        return null;
    }

    @Override
    public boolean useCard(String cardNum, String password, String mobile, String ip, String serialNum) {
        if (taskProducer.produceMdrcChargeMsg(build(cardNum, password, mobile, ip, serialNum))) {
            LOGGER.info("发布流量卡充值请求到队列成功.");
            return true;
        } else {
            LOGGER.info("发布流量卡充值请求到队列失败.");
            return false;
        }
    }

    //入库操作
    private MdrcEcResponseData stockIn(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                return mdrcCardInfoService.batchStore(cardNums);
            }
        });
    }

    //激活
    private MdrcEcResponseData activate(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {

        if (mdrcEcRequest.getGroupId() == null || StringUtils.isBlank(mdrcEcRequest.getGroupId()) ||
            !Pattern.matches("^[0-9a-zA-Z]{1,64}", mdrcEcRequest.getGroupId())) {
            LOGGER.error("无效的状态变更请求参数. MdrcEcRequestData = {}.", new Gson().toJson(mdrcEcRequest));
            throw new UnprocessableEntityException("invalid groupId");
        }

        List<String> cardNums = new ArrayList<String>();
        cardNums.add(mdrcEcRequest.getOriginCardInfo());

        //海南营销卡激活不需要产品编码
        if (!mdrcCardInfoService.isHaiNanProvince(cardNums)
            && ((mdrcEcRequest.getProductCode() == null
            || StringUtils.isBlank(mdrcEcRequest.getProductCode()) ||
            !Pattern.matches("^[0-9a-zA-Z]{1,64}", mdrcEcRequest.getProductCode())))) {
            LOGGER.error("无效的状态变更请求参数. MdrcEcRequestData = {}.", new Gson().toJson(mdrcEcRequest));
            throw new UnprocessableEntityException("invalid productCode");
        }

        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                if (mdrcCardInfoService.isHaiNanProvince(cardNums)) {//海南营销卡激活单独处理，海南营销卡激活操作没有企业产品信息
                    return mdrcCardInfoService.batchActivate(cardNums, request.getGroupId(), null);
                } else {
                    Product product = productService.selectByProductCode(request.getProductCode());
                    if (product == null
                        || product.getStatus().equals(ProductStatus.OFF.getCode())) {
                        LOGGER.error("无效的产品编码, 产品不存在或者产品已下架. Product={}.", product == null ? "" : new Gson().toJson(product));
                        return false;
                    }

                    return mdrcCardInfoService.batchActivate(cardNums, request.getGroupId(), product.getId());
                }
            }
        });
    }

    //去激活
    private MdrcEcResponseData deactivate(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                return mdrcCardInfoService.batchDeactivate(cardNums);
            }
        });
    }

    //注销
    private MdrcEcResponseData delete(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                return mdrcCardInfoService.batchDelete(cardNums);
            }
        });
    }

    //锁定
    private MdrcEcResponseData lock(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                return mdrcCardInfoService.batchLock(cardNums);
            }
        });
    }

    //解锁
    private MdrcEcResponseData unlock(final MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws PreconditionRequiredException {
                return mdrcCardInfoService.batchUnlock(cardNums);
            }
        });
    }

    //延期
    private MdrcEcResponseData extend(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException {
        return abstractOperate(mdrcEcRequest, new Operator() {
            @Override
            public boolean operate(List<String> cardNums, MdrcEcRequestData request) throws UnprocessableEntityException, PreconditionRequiredException {
                String date = request.getExpireDate();

                Date newDeadLine = null;
                try {
                    newDeadLine = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    newDeadLine = modifyDate(newDeadLine);
                } catch (Exception e) {
                    LOGGER.error("无效的过期时间. 过期时间为{}.", date);
                    throw new UnprocessableEntityException("invalid extend date");
                }

                return mdrcCardInfoService.batchExtend(cardNums, newDeadLine);
            }
        });
    }

    private MdrcEcResponseData buildResp(MdrcEcOperationStatusEnum statusEnum) {
        MdrcEcResponseData response = new MdrcEcResponseData();

        response.setRspCode(statusEnum.getCode());
        response.setRspDesc(statusEnum.getMessage());

        return response;
    }


    private MdrcEcResponseData abstractOperate(MdrcEcRequestData mdrcEcRequest, Operator operator)
        throws UnprocessableEntityException, PreconditionRequiredException {

        List<String> cardNums = CardNumCodec.decode(mdrcEcRequest.getOriginCardInfo());
        if (cardNums == null || cardNums.isEmpty()) {
            return buildResp(MdrcEcOperationStatusEnum.FAILED);
        }

        String operateName = mdrcEcRequest.getOperateType().getOperMessage();
        if (!operator.operate(cardNums, mdrcEcRequest)) {
            LOGGER.error("{}操作失败, 卡号为{}.", operateName, cardNums);
            return buildResp(MdrcEcOperationStatusEnum.FAILED);
        }

        return buildResp(MdrcEcOperationStatusEnum.SUCCESS);
    }

    private MdrcChargePojo build(String cardNum, String password, String mobile, String ip, String serialNum) {
        MdrcChargePojo mdrcChargePojo = new MdrcChargePojo();

        mdrcChargePojo.setCardNum(cardNum);
        mdrcChargePojo.setPassword(password);
        mdrcChargePojo.setMobile(mobile);
        mdrcChargePojo.setIp(ip);
        mdrcChargePojo.setSerialNum(serialNum);

        return mdrcChargePojo;
    }

    private Date modifyDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    private interface Operator {
        boolean operate(List<String> cardNums, MdrcEcRequestData request) throws UnprocessableEntityException, PreconditionRequiredException;
    }

}
