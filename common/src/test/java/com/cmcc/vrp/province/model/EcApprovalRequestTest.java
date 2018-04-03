package com.cmcc.vrp.province.model;

import org.junit.Test;

/**
 * 
 * @author Administrator
 *
 */
public class EcApprovalRequestTest {
    @Test
    public void init(){
        ApprovalRequest approvalRequest = new ApprovalRequest();
        EcApprovalRequest.init(approvalRequest);
    }
}
