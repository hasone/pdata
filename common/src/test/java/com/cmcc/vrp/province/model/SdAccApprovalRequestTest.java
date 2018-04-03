package com.cmcc.vrp.province.model;

import org.junit.Test;

/**
 * 
 * @author Administrator
 *
 */
public class SdAccApprovalRequestTest {
    @Test
    public void testConvert(){
        SdAccApprovalRequest request = new SdAccApprovalRequest();
        request.convertToApprovalRequest();
    }
}
