package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.queue.pojo.MdrcMakeCardPojo;
import com.cmcc.vrp.util.Constants;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by qinqinyan on 2017/08/08.
 * 营销卡制卡
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MdrcMakeCardWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(MdrcMakeCardWorker.class);

    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    
    MdrcCardInfoService mdrcCardInfoService;

    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        MdrcMakeCardPojo makeCardPojo;
        if (!validate(makeCardPojo = parse(taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        MdrcCardmakeDetail mdrcCardmakeDetail = 
                mdrcCardmakeDetailService.selectByRequestId(makeCardPojo.getRequestId());
        if(mdrcCardmakeDetail==null){
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }
        if(mdrcCardmakeDetail.getCardmakeStatus().toString().equals(Constants.MAKE_CARD_STATUS.NOT_MAKE_CARD.getResult())){
            //2. 生成卡数据
            logger.error("开始生成卡数据 requestId = {} ...", makeCardPojo.getRequestId());
            if(mdrcCardmakeDetailService.makecard(makeCardPojo.getRequestId(), makeCardPojo.getAdminId())){
                logger.info("生成卡数据成功。requestId = {}", makeCardPojo.getRequestId());
            }else{
                logger.info("生成卡数据失败。requestId = {}", makeCardPojo.getRequestId());
            }
        }else{
            logger.info("该请求  requestId = {} 已经生成卡数据，不允许重复生成.", makeCardPojo.getRequestId());
        }
        
        MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService.selectByRequestId(makeCardPojo.getRequestId());
        MdrcCardmakeDetail newMdrcCardmakeDetail = 
                mdrcCardmakeDetailService.selectByRequestId(makeCardPojo.getRequestId());
        if(requestConfigMap!=null && newMdrcCardmakeDetail!=null 
                && newMdrcCardmakeDetail.getCardmakeStatus().toString().equals(Constants.MAKE_CARD_STATUS.MAKE_CARD.getResult())){
            
            //3、准备待下载文件
            logger.info("数据已经生成好，开始准备数据提供下载  requestId = {} ...", makeCardPojo.getRequestId());
            boolean result = mdrcBatchConfigService.listFile(requestConfigMap.getConfigId(), null);
            if(!result){
                logger.info("生成文件失败。requestConfigMap = {}", JSON.toJSONString(requestConfigMap));
            }else{
                logger.info("生成文件成功。requestConfigMap = {}", JSON.toJSONString(requestConfigMap));
               
            }
        }else{
            logger.info("准备数据失败。requestConfigMap = {}", JSON.toJSONString(requestConfigMap));
        }
    }

    //解析充值对象
    private MdrcMakeCardPojo parse(String taskString) {
        try {
            return JSON.parseObject(taskString, MdrcMakeCardPojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.toString(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(MdrcMakeCardPojo taskPojo) {
        if (taskPojo == null
            || taskPojo.getAdminId()==null
            || taskPojo.getRequestId()==null) {
            logger.error("无效的充值请求参数，MdrcMakeCardPojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }
        return true;
    }
}
