package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ActivityPaymentInfoMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.wx.PaymentService;
import com.cmcc.vrp.wx.beans.PayParameter;

/**
 * Created by qinqinyan on 2017/1/6.
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityPaymentInfoServiceImplTest {

    @InjectMocks
    ActivityPaymentInfoService activityPaymentInfoService =
        new ActivityPaymentInfoServiceImpl();

    @Mock
    ActivityPaymentInfoMapper activityPaymentInfoMapper;
    
    @Mock
    ActivitiesService activitiesService;

    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    ActivityWinRecordService activityWinRecordService;
    
    @Mock
    PaymentService paymentService;
    
    @Mock
    ActivityPrizeService activityPrizeService;
    
    @Mock
    ProductService productService;

    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(activityPaymentInfoService.deleteByPrimaryKey(null));

        Mockito.when(activityPaymentInfoMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(activityPaymentInfoService.deleteByPrimaryKey(1L));
        Mockito.verify(activityPaymentInfoMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testDeleteBySysSerialNum(){
        assertFalse(activityPaymentInfoService.deleteBySysSerialNum(null));

        Mockito.when(activityPaymentInfoMapper.deleteBySysSerialNum(anyString())).thenReturn(1);
        assertTrue(activityPaymentInfoService.deleteBySysSerialNum("test"));
        Mockito.verify(activityPaymentInfoMapper).deleteBySysSerialNum(anyString());
    }

    @Test
    public void testInsert(){
        assertFalse(activityPaymentInfoService.insert(null));

        Mockito.when(activityPaymentInfoMapper.insert(Mockito.any(ActivityPaymentInfo.class))).thenReturn(1);
        assertTrue(activityPaymentInfoService.insert(new ActivityPaymentInfo()));
        Mockito.verify(activityPaymentInfoMapper).insert(Mockito.any(ActivityPaymentInfo.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(activityPaymentInfoService.insertSelective(null));

        Mockito.when(activityPaymentInfoMapper.insertSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(1);
        assertTrue(activityPaymentInfoService.insertSelective(new ActivityPaymentInfo()));
        Mockito.verify(activityPaymentInfoMapper).insertSelective(Mockito.any(ActivityPaymentInfo.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(activityPaymentInfoService.selectByPrimaryKey(null));

        Mockito.when(activityPaymentInfoMapper.selectByPrimaryKey(anyLong())).thenReturn(new ActivityPaymentInfo());
        assertNotNull(activityPaymentInfoService.selectByPrimaryKey(1L));
        Mockito.verify(activityPaymentInfoMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testSelectBySysSerialNum(){
        assertNull(activityPaymentInfoService.selectBySysSerialNum(null));

        Mockito.when(activityPaymentInfoMapper.selectBySysSerialNum(anyString())).thenReturn(new ActivityPaymentInfo());
        assertNotNull(activityPaymentInfoService.selectBySysSerialNum("test"));
        Mockito.verify(activityPaymentInfoMapper).selectBySysSerialNum(anyString());
    }

    @Test
    public void testUpdateBySysSerialNumSelective(){
        assertFalse(activityPaymentInfoService.updateBySysSerialNumSelective(null));

        Mockito.when(activityPaymentInfoMapper.updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(1);
        assertTrue(activityPaymentInfoService.updateBySysSerialNumSelective(new ActivityPaymentInfo()));
        Mockito.verify(activityPaymentInfoMapper).updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class));
    }

    @Test
    public void testUpdateBySysSerialNum(){
        assertFalse(activityPaymentInfoService.updateBySysSerialNum(null));

        Mockito.when(activityPaymentInfoMapper.updateBySysSerialNum(Mockito.any(ActivityPaymentInfo.class))).thenReturn(1);
        assertTrue(activityPaymentInfoService.updateBySysSerialNum(new ActivityPaymentInfo()));
        Mockito.verify(activityPaymentInfoMapper).updateBySysSerialNum(Mockito.any(ActivityPaymentInfo.class));
    }
    
    @Test
    public void testSelectByWinRecordId() {
        assertNull(activityPaymentInfoService.selectByWinRecordId(null));

        Mockito.when(activityPaymentInfoMapper.selectByWinRecordId(anyString())).thenReturn(new ArrayList<ActivityPaymentInfo>());
        assertNotNull(activityPaymentInfoService.selectByWinRecordId("test"));
        Mockito.verify(activityPaymentInfoMapper).selectByWinRecordId(anyString());
    }
    
    @Test
    public void testSelectByReturnSerialNum() {
        assertNull(activityPaymentInfoService.selectByReturnSerialNum(null));

        Mockito.when(activityPaymentInfoMapper.selectByReturnSerialNum(anyString())).thenReturn(new ActivityPaymentInfo());
        assertNotNull(activityPaymentInfoService.selectByReturnSerialNum("test"));
        Mockito.verify(activityPaymentInfoMapper).selectByReturnSerialNum(anyString());
    }
    
    @Test
    public void testCallForPay(){
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(initActivityWinRecord());
        
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(initActivity());
        Mockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(initEnterprise());
        Mockito.when(activityPrizeService.selectByPrimaryKey(anyLong())).thenReturn(initPrize());
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(initProduct());       
    
        Mockito.when(activityPaymentInfoMapper.insertSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(0);
        assertFalse(activityPaymentInfoService.callForPay("11"));
        
        Mockito.when(activityPaymentInfoMapper.insertSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(1);
        
        Mockito.when(paymentService.combinePayPara(Mockito.any(PayParameter.class))).thenReturn("11");
        Mockito.when(paymentService.sendChargeRequest(Mockito.anyString())).thenReturn(false);
        Mockito.when(activityPaymentInfoMapper.updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(0);
        assertFalse(activityPaymentInfoService.callForPay("11"));
        
        Mockito.when(paymentService.sendChargeRequest(Mockito.anyString())).thenReturn(true);        
        Mockito.when(activityPaymentInfoMapper.updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(0);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        
        assertTrue(activityPaymentInfoService.callForPay("11"));
    }
    
    private ActivityWinRecord initActivityWinRecord(){
        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("11111");
        record.setOwnMobile("18867103685");
        return record;
    }
    
    private Activities initActivity(){
        Activities activity = new Activities();
        activity.setName("活动名称");
        activity.setEntId(1L);
        return activity;
    }
    
    private Enterprise initEnterprise(){
        Enterprise enter = new Enterprise();
        enter.setCode("code1111");
        return enter;
    }
    
    private ActivityPrize initPrize(){
        ActivityPrize prize = new ActivityPrize();
        prize.setProductId(1L);
        prize.setDiscount(100);
        return prize;
    }
    
    private Product initProduct(){
        Product product = new Product();
        product.setPrice(100);
        return product;
    }

}
