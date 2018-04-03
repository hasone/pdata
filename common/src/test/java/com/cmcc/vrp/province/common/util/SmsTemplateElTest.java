package com.cmcc.vrp.province.common.util;

import com.cmcc.vrp.util.SmsTemplateEl;
import org.easymock.EasyMockRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(EasyMockRunner.class)
public class SmsTemplateElTest {

    @Test
    public void Test() {
        List list = new ArrayList();
        list.add("kok");
        String msg = "您好， ${0}";
        Assert.assertEquals(SmsTemplateEl.virifySmsTemplate(msg), "true");
        Assert.assertEquals(SmsTemplateEl.fillingElExpression(msg, list), "您好， kok");
        Assert.assertEquals(SmsTemplateEl.getTemplateLength(msg), 1);

    }
}
