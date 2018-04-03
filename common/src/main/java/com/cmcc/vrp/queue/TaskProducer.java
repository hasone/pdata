package com.cmcc.vrp.queue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.ActivitySendMessagePojo;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.vrp.queue.pojo.MdrcMakeCardPojo;
import com.cmcc.vrp.queue.pojo.MdrcSmsChargePojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.queue.pojo.YqxChargePojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.queue.pojo.ZwChargePojo;
import com.cmcc.vrp.wx.beans.ExchangeChargePojo;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 所有任务队列的生产者
 * <p>
 * Created by sunyiwei on 2016/4/8.
 */
@Component
public class TaskProducer extends QueueEndPoint {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(TaskProducer.class);

    @Value("#{settings['batch.present.queue.name']}")
    private String batchPresentQueueName;

    @Value("#{settings['deliver.sms.verify.queue.name']}")
    private String deliverVerifySmsQueueName;

    @Value("#{settings['deliver.sms.notice.queue.name']}")
    private String deliverNoticeSmsQueueName;

    @Value("#{settings['flow.card.queue.name']}")
    private String flowCardQueueName;

    @Value("#{settings['lottery.queue.name']}")
    private String lotteryQueueName;

    @Value("#{settings['monthly.present.queue.name']}")
    private String monthlyPresentQueueName;

    @Value("#{settings['redpacket.queue.name']}")
    private String redpacketQueueName;

    @Value("#{settings['goldenball.queue.name']}")
    private String goldenballQueueName;

    @Value("#{settings['ec.async.charge.queue.name']}")
    private String deliverChargeQueueName;

    @Value("#{settings['mdrc.async.charge.queue.name']}")
    private String mdrcAsyncChargeQueueName;

    @Value("#{settings['boss.async.charge.query.queue.name']}")
    private String asyncChargeQueryQueueName;

    @Value("#{settings['platform.callback.queue.name']}")
    private String platformCallbackQueueName;

    @Value("#{settings['activities.win.queue.name']}")
    private String activitiesWinQueueName;

    @Value("#{settings['product.online.queue.name']}")
    private String productOnlineQueueName;

    @Value("#{settings['flowcoin.exchange.queue.name']}")
    private String flowcoinExchangeQueueName;

    @Value("#{settings['flowcoin.present.queue.name']}")
    private String flowcoinPresentQueueName;

    @Value("#{settings['redis.cmpp.upStream.queueName']}")
    private String usSmsQueueName;

    @Value("#{settings['zw.charge.queue.name']}")
    private String zwChargeQueueName;
    
    @Value("#{settings['individual.activities.win.queue.name']}")
    private String individualActivitiesWinQueueName;
    
    @Value("#{settings['wx.score.exchange.queue.name']}")
    private String wxScoreExchangeQueueName;
    
    @Value("#{settings['wx.send.template.queue.name']}")
    private String wxSendTemplateQueueName;
    
    @Value("#{settings['yqx.async.charge.queue.name']}")
    private String yqxAsyncChargeQueueName;
    
    @Value("#{settings['activities.send.message.queue.name']}")
    private String activitiesSendMessageQueueName;

    @Value("#{settings['mdrc.make.card.queue.name']}")
    private String mdrcMakeCardQueueName;

    @Value("#{settings['boss.async.deliver.query.queue.name']}")
    private String asynDeliverQueryQueueName;

    /**
     * 初始化mq
     */
    @PostConstruct
    public void initMq() {
        // init queues
        String[] queues = new String[]{batchPresentQueueName,
            deliverVerifySmsQueueName, deliverNoticeSmsQueueName,
            flowCardQueueName, lotteryQueueName, monthlyPresentQueueName,
            redpacketQueueName, goldenballQueueName,
            deliverChargeQueueName, mdrcAsyncChargeQueueName,
            asyncChargeQueryQueueName, platformCallbackQueueName,
            activitiesWinQueueName, productOnlineQueueName,
            flowcoinExchangeQueueName, flowcoinPresentQueueName,
            zwChargeQueueName, individualActivitiesWinQueueName,
            wxScoreExchangeQueueName, wxSendTemplateQueueName,yqxAsyncChargeQueueName,
            activitiesSendMessageQueueName, mdrcMakeCardQueueName, asynDeliverQueryQueueName};

        initQueues(queues);
    }

    /**
     * 在线同步渠道侧产品
     *
     * @param pojo pojo
     * @return 结果
     */
    public boolean productProductOnlineMsg(ProductOnlinePojo pojo) {
        return produce(productOnlineQueueName, pojo);
    }

    /**
     * 平台回调EC
     *
     * @param pojo pojo
     * @return 结果
     */
    public boolean productPlatformCallbackMsg(CallbackPojo pojo) {
        return produce(platformCallbackQueueName, pojo);
    }

    /**
     * 异步查询
     *
     * @param pojo pojo
     * @return 结果
     */
    public boolean produceAsynChargeQueryMsg(ChargeQueryPojo pojo) {
        return produce(asyncChargeQueryQueueName, pojo);
    }

    /**
     * 批量赠送
     *
     * @param pojos
     * @return
     */
    public boolean produceBatchPresentMsg(List<BlockPresentPojo> pojos) {
        return batchProduce(batchPresentQueueName, pojos);
    }

    /**
     * 即时性高短信任务
     *
     * @param smsPojo smsPojo
     * @return 结果
     */
    public boolean produceDeliverVerifySmsMsg(SmsPojo smsPojo) {
        return produce(deliverVerifySmsQueueName, smsPojo);
    }

    /**
     * 即时性高批量短信任务
     *
     * @param smsPojos smsPojos
     * @return 结果
     */
    public boolean produceDeliverVerifySmsMsg(List<SmsPojo> smsPojos) {
        return batchProduce(deliverVerifySmsQueueName, smsPojos);
    }

    /**
     * 即时性低短信任务
     *
     * @param
     * @return 结果
     */
    public boolean produceDeliverNoticeSmsMsg(SmsPojo smsPojo) {
        return produce(deliverNoticeSmsQueueName, smsPojo);
    }

    /**
     * 即时性低批量短信任务
     *
     * @param smsPojos smsPojos
     * @return 结果
     */
    public boolean produceDeliverNoticeSmsMsg(List<SmsPojo> smsPojos) {
        return batchProduce(deliverNoticeSmsQueueName, smsPojos);
    }

    /**
     * 流量卡任务
     *
     * @param presentPojo presentPojo
     * @return 结果
     */
    public boolean produceFlowcardMsg(PresentPojo presentPojo) {
        return produce(flowCardQueueName, presentPojo);
    }

    /**
     * 流量卡批量任务
     *
     * @param presentPojos presentPojos
     * @return 结果
     */
    public boolean produceFlowcardMsg(List<PresentPojo> presentPojos) {
        return batchProduce(flowCardQueueName, presentPojos);
    }
    
    /**
     * 包月赠送
     *
     * @param presentPojos presentPojos
     * @return 结果
     */
    public boolean produceMonthPresentMsg(List<BlockPresentPojo> pojos) {
        return batchProduce(monthlyPresentQueueName, pojos);
    }

    /**
     * 红包任务
     *
     * @param presentPojo
     * @return 结果
     */
    public boolean produceRedpacketMsg(PresentPojo presentPojo) {
        return produce(redpacketQueueName, presentPojo);
    }

    /**
     * 红包批量任务
     *
     * @param presentPojos presentPojos
     * @return 结果
     */
    public boolean produceRedpacketMsg(List<PresentPojo> presentPojos) {
        return batchProduce(redpacketQueueName, presentPojos);
    }

    /**
     * 充值任务
     *
     * @param chargePojo chargePojo
     * @return 结果
     */
    public boolean produceChargeMsg(ChargePojo chargePojo) {
        return produce(deliverChargeQueueName, chargePojo);
    }

    /**
     * 流量卡异步充值任务
     *
     * @param mdrcChargePojo mdrcChargePojo
     * @return 结果
     */
    public boolean produceMdrcChargeMsg(MdrcChargePojo mdrcChargePojo) {
        return produce(mdrcAsyncChargeQueueName, mdrcChargePojo);
    }
    /**
     * 流量卡生成卡数据及准备下载数据队列
     * @author qinqinyan
     * */
    public boolean produceMdrcMakeCard(MdrcMakeCardPojo mdrcMakeCardPojo){
        return produce(mdrcMakeCardQueueName, mdrcMakeCardPojo);
    }

    /**
     * 营销活动充值任务（整合后的营销活动采用这个队列）
     *
     * @param activitiesWinPojo activitiesWinPojo
     * @return 结果
     */
    public boolean produceActivityWinMsg(ActivitiesWinPojo activitiesWinPojo) {
        return produce(activitiesWinQueueName, activitiesWinPojo);
    }

    /** 
     * 个人集中化平台营销活动队列
     * @Title: produceIndividualActivityWinMsg 
     */
    public boolean produceIndividualActivityWinMsg(ActivitiesWinPojo activitiesWinPojo) {
        return produce(individualActivitiesWinQueueName, activitiesWinPojo);
    }

    /**
     * 批量活动(二维码、流量券)充值任务
     *
     * @param activitiesWinPojos activitiesWinPojos
     * @return 结果
     */
    public boolean produceBatchActivityWinMsg(
        List<ActivitiesWinPojo> activitiesWinPojos) {
        return batchProduce(activitiesWinQueueName, activitiesWinPojos);
    }

    /**
     * 生成消息到卓望充值队列中
     *
     * @return 生成消息成功返回true, 否则false
     */
    public boolean produceZwChargeMsg(ZwChargePojo zwChargePojo) {
        return produce(zwChargeQueueName, zwChargePojo);
    }

    /**
     * 流量币兑换任务
     *
     * @param flowcoinExchangePojo 流量币兑换
     * @return 投递成功返回true, 否则false
     */
    // 流量币兑换任务
    public boolean produceFlowcoinExchangeMsg(FlowcoinExchangePojo flowcoinExchangePojo) {
        return produce(wxScoreExchangeQueueName, flowcoinExchangePojo);
    }

    /** 
     * @Title: produceZwPackage 
     */
    public boolean produceZwPackage(ZwBossPojo zwBossPojo) {
        return produce(zwChargeQueueName, zwBossPojo);
    }

    /**
     * 流量币赠送任务
     *
     * @param flowcoinPresentPojo 流量币赠送对象
     * @return 投递成功返回true, 否则false
     */
    public boolean produceFlowcoinPresentMsg(FlowcoinPresentPojo flowcoinPresentPojo) {
        return produce(flowcoinPresentQueueName, flowcoinPresentPojo);
    }

    /**
     * 流量卡短信充值任务
     *
     * @param mdrcSmsChargePojo 流量卡短信充值
     * @return 投递成功返回true, 否则false
     */
    //流量卡短信充值任务
    public boolean produceMdrcChargeMsg(MdrcSmsChargePojo mdrcSmsChargePojo) {
        return produce(usSmsQueueName, mdrcSmsChargePojo);
    }
    
    /** 
     * 微信积分兑换任务
     * @Title: produceWxScoreExchangeMsg 
     */
    public boolean produceWxScoreExchangeMsg(ExchangeChargePojo exchangeChargePojo) {
        return produce(wxScoreExchangeQueueName, exchangeChargePojo);
    }
    
    /** 
     * 向微信发送模板消息
     * @Title: produceWxSendTemplateMsg 
     */
    public boolean produceWxSendTemplateMsg(TemplateMsgPojo templateMsgPojo) {
        return produce(wxSendTemplateQueueName, templateMsgPojo);
    }
    
    /** 
     * 向微信发送模板消息(批量)
     * @Title: produceBatchWxSendTemplateMsg 
     */
    public boolean produceBatchWxSendTemplateMsg(List<TemplateMsgPojo> templateMsgPojos) {
        return batchProduce(wxSendTemplateQueueName, templateMsgPojos);
    }
    
    
    /**
     * 云企信充值任务

     */
    public boolean produceYqxChargeMsg(YqxChargePojo chargePojo) {
        return produce(yqxAsyncChargeQueueName, chargePojo);
    }
    
    /**
     * 活动短信分发队列
     * 目前只有流量券有批量短信通知
     * @author qinqinyan
     * @date 20170711
     * */
    public boolean produceActivitySendMessage(ActivitySendMessagePojo pojo) {
        return produce(activitiesSendMessageQueueName, pojo);
    }
    
    /**
     * 异步查询队列，查询机制，分时间段查询
     * @param pojo
     * @return
     */
    public boolean produceAsynDeliverQueryMsg(ChargeQueryPojo pojo) {
        return produce(asynDeliverQueryQueueName, pojo);
    }

    /***
     * 单消息
     *
     * @param queueName
     * @param obj
     * @returng
     */
    private boolean produce(final String queueName, final Object obj) {
        if (StringUtils.isBlank(queueName) || obj == null) {
            return false;
        }

        Channel channel = null;
        try {
            channel = rmqChannelPool.borrowObject();
            if (channel == null) {
                LOGGER.error("创建channel出错，生产消息返回false.");
                return false;
            }

            String message = JSONObject.toJSONString(obj);
            channel.basicPublish("", queueName,
                MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            return true;
        } catch (IOException e) {
            LOGGER.error("生产消息{}到{}抛出异常，异常信息为{}.",
                JSONObject.toJSONString(obj), queueName, e.getMessage());
        } catch (TimeoutException e) {
            LOGGER.error("生产消息{}到{}超时, 超时信息为{}.", JSONObject.toJSONString(obj),
                queueName, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("无法从RMQ连接池中获取RMQ连接.");
        } finally {
            if (channel != null) {
                rmqChannelPool.returnObject(channel);
            }
        }

        return false;
    }

    /**
     * 批量消息
     *
     * @param queueName
     * @param objects
     * @return
     */
    private boolean batchProduce(String queueName, List objects) {
        if (StringUtils.isBlank(queueName) || objects.isEmpty()) {
            return false;
        }

        Channel channel = null;
        try {
            channel = rmqChannelPool.borrowObject();
            if (channel == null) {
                LOGGER.error("创建channel出错，生产消息返回false.");
                return false;
            }

            for (Object object : objects) {
                String message = JSONObject.toJSONString(object);
                channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            }

            LOGGER.info("生产消息{}到{}成功.", JSONObject.toJSONString(objects),
                queueName);
            return true;
        } catch (IOException e) {
            LOGGER.error("生产消息{}到{}抛出异常，异常信息为{}.",
                JSONObject.toJSONString(objects), queueName, e.getMessage());
        } catch (TimeoutException e) {
            LOGGER.error("生产消息{}到{}超时, 超时信息为{}.",
                JSONObject.toJSONString(objects), queueName, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("无法从RMQ连接池中获取RMQ连接,异常信息为{}", e);
        } finally {
            if (channel != null) {
                rmqChannelPool.returnObject(channel);
            }
        }

        return false;
    }
}
