package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.model.ChargeRecord;

/**
 * EmptyChargeRecordQueryServiceImplTest
 * @author Administrator
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EmptyChargeRecordQueryServiceImplTest {
    @InjectMocks
    EmptyChargeRecordQueryServiceImpl service = new EmptyChargeRecordQueryServiceImpl();
    
    /**
     * testUpdateResultFromBoss
     */
    @Test
    public void testUpdateResultFromBoss(){
        Assert.assertTrue(service.updateResultFromBoss(initChargeRecord(3)));
    }
    
    /**
     * initChargeRecord  初始化
     */
    private ChargeRecord initChargeRecord(int status){
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(1L);
        chargeRecord.setStatus(status);
        chargeRecord.setSystemNum("aaa");
        chargeRecord.setSupplierProductId(1L);
        return chargeRecord;
    }
}
