package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by leelyn on 2016/11/2.
 */
public class ProductOnlineServiceTest extends BaseServiceTest {

    @InjectMocks
    ProductOnlineServiceImpl productOnlineService = new ProductOnlineServiceImpl();

    @Mock
    TaskProducer producer;

    @Before
    public void initMocks() {

    }

    @Test
    public void testIsOnlineProduct() {
        PowerMockito.when(producer.productProductOnlineMsg(any(ProductOnlinePojo.class))).thenReturn(false);
        boolean flag1 = productOnlineService.isOnlineProduct(12345l);
        Assert.assertFalse(flag1);
        PowerMockito.when(producer.productProductOnlineMsg(any(ProductOnlinePojo.class))).thenReturn(true);
        boolean flag2 = productOnlineService.isOnlineProduct(12345l);
        Assert.assertTrue(flag2);
        verify(producer, times(2)).productProductOnlineMsg(any(ProductOnlinePojo.class));
    }
}
