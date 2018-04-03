/**
 *
 */
package com.cmcc.vrp.queue.task;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.queue.channel.ChannelBeijingQueue;
import com.cmcc.vrp.queue.rule.DeliverRule;
import com.google.gson.Gson;

/**
 * <p>Title:AbstractDeliverWorkerTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月24日
 */
@Ignore
@RunWith(PowerMockRunner.class)
public class AbstractDeliverWorkerTest {

    @InjectMocks
    DeliverByBossWorker worker = new DeliverByBossWorker();

    @Mock
    protected QueueRegistryCenter queueRegistryCenter;

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    PresentRecordService presentRecordService;

    @Mock
    ActivityWinRecordService activityWinRecordService;

    @Mock
    InterfaceRecordService interfaceRecordService;

    @Mock
    AccountService accountService;

    @Mock
    ProductService productService;

    @Mock
    EntProductService entProductService;

    @Mock
    DeliverRule deliverRule;

    @Test
    public void testExec() {
        worker.setTaskString("");
        worker.exec();
    }

    //没找到分发渠道
    @Test
    public void testExec1() {

        when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(buildChargeRecord());
        when(deliverRule.deliver(Mockito.any(ChargeDeliverPojo.class))).thenReturn(null);
        when(chargeRecordService.updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(false).thenReturn(true);
        when(presentRecordService.updatePresentStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(interfaceRecordService.updateChargeStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false).thenReturn(true);
        when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyInt())).thenReturn(false).thenReturn(true);

        worker.setTaskString(buildChargePojo(ActivityType.GIVE, ChargeType.PRESENT_TASK.getCode()));
        worker.exec();
        worker.exec();

        worker.setTaskString(buildChargePojo(ActivityType.INTERFACE, ChargeType.EC_TASK.getCode()));
        worker.exec();
        worker.setTaskString(buildChargePojo(ActivityType.REDPACKET, ChargeType.REDPACKET_TASK.getCode()));
        worker.exec();

        Mockito.verify(chargeRecordService, Mockito.times(4)).getRecordBySN(Mockito.anyString());
        Mockito.verify(deliverRule, Mockito.times(4)).deliver(Mockito.any(ChargeDeliverPojo.class));
        Mockito.verify(chargeRecordService, Mockito.times(4)).updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class));
        Mockito.verify(presentRecordService, Mockito.times(2)).updatePresentStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString());
        Mockito.verify(interfaceRecordService, Mockito.times(1)).updateChargeStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString());
        Mockito.verify(activityWinRecordService, Mockito.times(1)).updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class));
        Mockito.verify(accountService, Mockito.times(4)).returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyInt());
    }


    //塞入队列失败
    @Test
    @PrepareForTest(ChannelBeijingQueue.class)
    public void testExec3() {
        AbstractQueue queue = PowerMockito.mock(ChannelBeijingQueue.class);

        when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(buildChargeRecord());
        when(deliverRule.deliver(Mockito.any(ChargeDeliverPojo.class))).thenReturn(queue);
        when(productService.get(Mockito.anyLong())).thenReturn(buildProduct());
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(buildEntProduct());
        when(chargeRecordService.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);

        when(queue.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(false);
        when(chargeRecordService.updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(false).thenReturn(true);
        when(presentRecordService.updatePresentStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(interfaceRecordService.updateChargeStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false).thenReturn(true);
        when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyInt())).thenReturn(false).thenReturn(true);

        worker.setTaskString(buildChargePojo(ActivityType.GIVE, ChargeType.PRESENT_TASK.getCode()));
        worker.exec();
        worker.exec();
        worker.exec();

        worker.setTaskString(buildChargePojo(ActivityType.INTERFACE, ChargeType.EC_TASK.getCode()));
        worker.exec();
        worker.setTaskString(buildChargePojo(ActivityType.REDPACKET, ChargeType.REDPACKET_TASK.getCode()));
        worker.exec();

//        Mockito.verify(chargeRecordService, Mockito.times(5)).getRecordBySN(Mockito.anyString());
//        Mockito.verify(deliverRule, Mockito.times(5)).deliver(Mockito.any(ChargeDeliverPojo.class));
//        Mockito.verify(productService, Mockito.times(5)).get(Mockito.anyLong());
//        Mockito.verify(entProductService, Mockito.times(5)).selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong());
//        Mockito.verify(chargeRecordService, Mockito.times(5)).updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class));
//
//        Mockito.verify(queue, Mockito.times(4)).publish(Mockito.any(ChargeDeliverPojo.class));
//        Mockito.verify(chargeRecordService, Mockito.times(4)).updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString());
//        Mockito.verify(presentRecordService, Mockito.times(2)).updatePresentStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString());
//        Mockito.verify(interfaceRecordService, Mockito.times(1)).updateChargeStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString());
//        Mockito.verify(activityWinRecordService, Mockito.times(1)).updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class));
//        Mockito.verify(accountService, Mockito.times(4)).returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    @PrepareForTest(ChannelBeijingQueue.class)
    public void testExec4() {
        AbstractQueue queue = PowerMockito.mock(ChannelBeijingQueue.class);

        when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(buildChargeRecord());
        when(deliverRule.deliver(Mockito.any(ChargeDeliverPojo.class))).thenReturn(queue);
        when(productService.get(Mockito.anyLong())).thenReturn(buildProduct());
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(buildEntProduct());
        when(chargeRecordService.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(true);

        when(queue.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(true);
        when(chargeRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(presentRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(interfaceRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(activityWinRecordService.updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);

        worker.setTaskString(buildChargePojo(ActivityType.GIVE, ChargeType.PRESENT_TASK.getCode()));
        worker.exec();
        worker.exec();

        worker.setTaskString(buildChargePojo(ActivityType.INTERFACE, ChargeType.EC_TASK.getCode()));
        worker.exec();
        worker.exec();
        worker.setTaskString(buildChargePojo(ActivityType.REDPACKET, ChargeType.REDPACKET_TASK.getCode()));
        worker.exec();
        worker.exec();

//        Mockito.verify(chargeRecordService, Mockito.times(6)).getRecordBySN(Mockito.anyString());
//        Mockito.verify(deliverRule, Mockito.times(6)).deliver(Mockito.any(ChargeDeliverPojo.class));
//        Mockito.verify(productService, Mockito.times(6)).get(Mockito.anyLong());
//        Mockito.verify(entProductService, Mockito.times(6)).selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong());
//        Mockito.verify(chargeRecordService, Mockito.times(6)).updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class));
//
//        Mockito.verify(queue, Mockito.times(6)).publish(Mockito.any(ChargeDeliverPojo.class));
//        Mockito.verify(chargeRecordService, Mockito.times(6)).updateStatusCode(Mockito.anyLong(), Mockito.anyString());
//        Mockito.verify(presentRecordService, Mockito.times(2)).updateStatusCode(Mockito.anyLong(), Mockito.anyString());
//        Mockito.verify(interfaceRecordService, Mockito.times(2)).updateStatusCode(Mockito.anyLong(), Mockito.anyString());
//        Mockito.verify(activityWinRecordService, Mockito.times(2)).updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString());
    }

    private String buildChargePojo(ActivityType activityType, String type) {
        PhoneRegion pr = new PhoneRegion();
        pr.setCity("杭州");
        pr.setMobile("18867101111");
        pr.setProvince("浙江");
        pr.setSupplier("M");
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setActivityName("aaa");
        pojo.setActivityType(activityType);
        pojo.setEntId(1L);
        pojo.setMobile("18867101111");
        pojo.setPrdId(1L);
        pojo.setRecordId(1L);
        pojo.setSerialNum("w3rq21");
        pojo.setSplPrdId(1L);
        pojo.setType(type);
        pojo.setPhoneRegion(pr);
        return new Gson().toJson(pojo);
    }

    private ChargeRecord buildChargeRecord() {
        ChargeRecord cr = new ChargeRecord();
        cr.setaName("aaa");
        cr.setEnterId(2L);
        cr.setId(1L);
        return cr;
    }

    private Product buildProduct() {
        Product p = new Product();
        p.setId(1L);
        p.setPrice(10);
        p.setType((int)ProductType.FLOW_PACKAGE.getValue());
        p.setFlowAccountFlag(1);
        return p;
    }

    private EntProduct buildEntProduct() {
        EntProduct ep = new EntProduct();
        ep.setEnterprizeId(1L);
        ep.setProductId(1L);
        ep.setDiscount(100);
        return ep;
    }
}
