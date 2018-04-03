package com.cmcc.vrp.boss.shangdong.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cmcc.vrp.boss.shangdong.boss.model.BillDetail;

/**
 * BillDetailTest
 * @author qihang
 *
 */
@Ignore
public class BillDetailTest {

    /**
     * 测试GeneBillFromStr
     */
    @Test
    public void testGeneBillFromStr() {
       String correctBill = 
               "17854057616|00|20161020143122||5338029470434|109206|20161020143117|20161110000000";
       
       String errorBill = 
               "17854057616|00|20161020143122||5338029470434|109206|20161020143117";
       
       Assert.assertNotNull(BillDetail.geneBillFromStr(correctBill));
       Assert.assertNull(BillDetail.geneBillFromStr(errorBill));
    }
    
    /**
     * 测试GeneBillFromStr
     */
    @Test
    public void testToRecord(){
        String correctBill = 
                "17854057616|00|20161020143122||5338029470434|109206|20161020143117|20161110000000";
        
        BillDetail billDetail = BillDetail.geneBillFromStr(correctBill);
        
        Assert.assertEquals(billDetail.toRecord(), correctBill);
    }

    /**
     * 测试GetBillsMap
     */
    @Test
    public void testGetBillsMap(){
        List<BillDetail> totalbills = new ArrayList<BillDetail>();
        
        String correctBill1 = 
                "17854057610|00|20161020143122||5338029470434|109206|20161020143117|20161110000000";
        
        String correctBill2 = 
                "17854057610|00|20161020143123||5338029470434|109206|20161020143117|20161110000000";
        
        String correctBill3 = 
                "17854057610|00|20161020143124||5338029470434|109206|20161020143117|20161110000000";
        
        totalbills.add(BillDetail.geneBillFromStr(correctBill1));
        totalbills.add(BillDetail.geneBillFromStr(correctBill2));
        totalbills.add(BillDetail.geneBillFromStr(correctBill3));
        
        Map<String, List<BillDetail>> map = BillDetail.getBillsMap(totalbills);
        Assert.assertTrue(map.containsKey("17854057610_5338029470434_109206"));
    }
}
