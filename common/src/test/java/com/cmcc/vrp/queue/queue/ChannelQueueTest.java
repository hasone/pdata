/**
 *
 */
package com.cmcc.vrp.queue.queue;

import com.cmcc.vrp.queue.ChannelObjectPool;
import com.cmcc.vrp.queue.queue.busi.ActivityQueue;
import com.cmcc.vrp.queue.queue.busi.AsyncChargeQueryQueue;
import com.cmcc.vrp.queue.queue.busi.AsyncChargeQueue;
import com.cmcc.vrp.queue.queue.busi.BatchPresentQueue;
import com.cmcc.vrp.queue.queue.busi.CallbackEcQueue;
import com.cmcc.vrp.queue.queue.busi.DeadLetterQueue;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import com.cmcc.vrp.queue.queue.busi.DeliverNoticeSmsQueue;
import com.cmcc.vrp.queue.queue.busi.DeliverVerifySmsQueue;
import com.cmcc.vrp.queue.queue.busi.FlowCardQueue;
import com.cmcc.vrp.queue.queue.busi.FlowCoinExchangeQueue;
import com.cmcc.vrp.queue.queue.busi.FlowCoinPresentQueue;
import com.cmcc.vrp.queue.queue.busi.IndividualActivityQueue;
import com.cmcc.vrp.queue.queue.busi.MdrcChargeQueue;
import com.cmcc.vrp.queue.queue.busi.MdrcSmsChargeQueue;
import com.cmcc.vrp.queue.queue.busi.MonthlyPresentQueue;
import com.cmcc.vrp.queue.queue.busi.ProductOnlineQueue;
import com.cmcc.vrp.queue.queue.busi.WxScoreExchangeQueue;
import com.cmcc.vrp.queue.queue.busi.WxSendTemplateQueue;
import com.cmcc.vrp.queue.queue.busi.ZwChargeQueue;
import com.cmcc.vrp.queue.queue.channel.ChanneVirtualChargeQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelBeijingQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelBjymQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelChongqingNewQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelChongqingPlatformQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelChongqingQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelCrowdfundingQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelFujianQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGansuQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGdCardQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGdPoolQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGdWheelQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGuangdongQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelGuangxiQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHainanQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHebeiQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHeilongjiangQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHenanPlatformQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHenanQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHljFeddQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHunanFreeQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelHunanQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelJiLinQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelJiangSuQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelJiangxiNewQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelJiangxiQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelJsofQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelLiaoningQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelNeiMengGuPlatformQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelNeimengQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShandongPlatformQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShandongQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShangHaiNewQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShanghaiQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShanxiFeeQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShanxiQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelShycQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelSichuanQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelTianjinPlatformQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelTianjinQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelWangsuQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelXiangShangQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelXinjiangQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelZhejiangQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelZhuowangQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title:ChannelQueueTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月17日
 */
@RunWith(MockitoJUnitRunner.class)
public class ChannelQueueTest {

    @Mock
    ChannelObjectPool channelObjectPool;

    /**
     * @throws Exception
     */
    @Test
    public final void test() throws Exception {
        List<Info> infos = buildClazzs();
        for (Info info : infos) {
            testConcreteQueue(info);
        }
    }

    private void testConcreteQueue(Info info) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
            NoSuchMethodException, SecurityException {
//
        AbstractQueue abstractQueue = info.getAbstractQueue();
        System.out.println(abstractQueue.getQueueName());
        System.out.println(abstractQueue.getWorkerClass());
        System.out.println(abstractQueue.getFingerPrint());
        abstractQueue.getQueueName();
        abstractQueue.getWorkerClass();
        abstractQueue.getFingerPrint();

    }

    private List<Info> buildClazzs() throws IOException {
        List<Info> infos = new LinkedList<Info>();
        infos.add(new Info(new ChannelBeijingQueue()));
        infos.add(new Info(new ChannelChongqingQueue()));
        infos.add(new Info(new ChannelFujianQueue()));
        infos.add(new Info(new ChannelGansuQueue()));
        infos.add(new Info(new ChannelGuangdongQueue()));
        infos.add(new Info(new ChannelGdCardQueue()));
        infos.add(new Info(new ChannelGuangxiQueue()));
        infos.add(new Info(new ChannelHainanQueue()));
        infos.add(new Info(new ChannelHebeiQueue()));
        infos.add(new Info(new ChannelHeilongjiangQueue()));
        infos.add(new Info(new ChannelHunanQueue()));
        infos.add(new Info(new ChannelJiangxiQueue()));
        infos.add(new Info(new ChannelNeimengQueue()));
        infos.add(new Info(new ChannelShanghaiQueue()));
        infos.add(new Info(new ChannelShanxiQueue()));
        infos.add(new Info(new ChannelSichuanQueue()));
        infos.add(new Info(new ChannelZhejiangQueue()));
        infos.add(new Info(new ChannelZhuowangQueue()));
        infos.add(new Info(new ChannelHenanQueue()));
        infos.add(new Info(new ChannelHenanPlatformQueue()));
        infos.add(new Info(new ChannelHunanFreeQueue()));
        infos.add(new Info(new ChannelWangsuQueue()));
        infos.add(new Info(new ChannelShandongQueue()));
        infos.add(new Info(new ChannelShandongPlatformQueue()));
        infos.add(new Info(new ChannelChongqingPlatformQueue()));
        infos.add(new Info(new ChannelXinjiangQueue()));
        infos.add(new Info(new ChannelXiangShangQueue()));
        infos.add(new Info(new ActivityQueue()));
        infos.add(new Info(new AsyncChargeQueryQueue()));
        infos.add(new Info(new AsyncChargeQueue()));
        infos.add(new Info(new BatchPresentQueue()));
        infos.add(new Info(new CallbackEcQueue()));
        infos.add(new Info(new DeadLetterQueue()));
        infos.add(new Info(new DeliverByBossQueue()));
        infos.add(new Info(new DeliverNoticeSmsQueue()));
        infos.add(new Info(new DeliverVerifySmsQueue()));
        infos.add(new Info(new FlowCardQueue()));
        infos.add(new Info(new FlowCoinExchangeQueue()));
        infos.add(new Info(new FlowCoinPresentQueue()));
        infos.add(new Info(new MdrcChargeQueue()));
        infos.add(new Info(new MdrcSmsChargeQueue()));
        infos.add(new Info(new MonthlyPresentQueue()));
        infos.add(new Info(new ProductOnlineQueue()));
        infos.add(new Info(new ZwChargeQueue()));
        infos.add(new Info(new ChannelShanxiFeeQueue()));
        infos.add(new Info(new ChannelCrowdfundingQueue()));
        infos.add(new Info(new IndividualActivityQueue()));
        infos.add(new Info(new WxScoreExchangeQueue()));
        infos.add(new Info(new WxSendTemplateQueue()));
        infos.add(new Info(new ChannelGdWheelQueue()));        
        infos.add(new Info(new ChannelHljFeddQueue()));
        infos.add(new Info(new ChannelJiLinQueue()));
        infos.add(new Info(new ChannelLiaoningQueue()));
        infos.add(new Info(new ChannelNeiMengGuPlatformQueue()));
        infos.add(new Info(new ChannelShycQueue()));
        infos.add(new Info(new ChannelTianjinQueue()));
        
        infos.add(new Info(new ChanneVirtualChargeQueue()));
        infos.add(new Info(new ChannelBjymQueue()));
        infos.add(new Info(new ChannelChongqingNewQueue()));
        infos.add(new Info(new ChannelGdPoolQueue()));
        infos.add(new Info(new ChannelJiangSuQueue()));
        infos.add(new Info(new ChannelJiangxiNewQueue()));
        infos.add(new Info(new ChannelJsofQueue()));
        infos.add(new Info(new ChannelShangHaiNewQueue()));
        infos.add(new Info(new ChannelTianjinPlatformQueue()));
        return infos;
    }

    private class Info {
        private AbstractQueue abstractQueue;
        private String fingerprint;
        private String queuename;

        public Info(AbstractQueue abstractQueue) {
            this.abstractQueue = abstractQueue;
            this.fingerprint = abstractQueue.getFingerPrint();
            this.queuename = abstractQueue.getQueueName();
        }

        public AbstractQueue getAbstractQueue() {
            return abstractQueue;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public String getQueuename() {
            return queuename;
        }
    }

}
