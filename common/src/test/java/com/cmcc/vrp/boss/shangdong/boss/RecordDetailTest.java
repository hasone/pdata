
package com.cmcc.vrp.boss.shangdong.boss;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cmcc.vrp.boss.shangdong.boss.model.RecordDetail;

public class RecordDetailTest {
    @Test
    @Ignore
    public void testToBillRecord(){
        RecordDetail detail = new RecordDetail();
        detail.setSerialNumber("serialnum");
        detail.setCustomerType(1);
        detail.setCustomerID("customerID");
        detail.setUserID("userid");
        detail.setMemberPhone("mobile");
        detail.setServiceID("prdcode");
        detail.setMeasureMode("100104");
        detail.setChargingType("03");
        detail.setOrderDate("begintime");
        detail.setExpireDate("endtime");
        detail.setBeginTime("");
        detail.setEndTime("");
        detail.setDuration("");
        detail.setUsageAmount("");
        detail.setOrderQuantity("");
        
        System.out.println(detail.toBillRecord());
        Assert.assertEquals(detail.toBillRecord(), 
                "serialnum|1|customerID|userid|mobile|0|prdcode|100104|03|begintime|endtime||||||null|null|0|");
    }
}
