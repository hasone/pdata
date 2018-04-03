package com.cmcc.vrp.boss.zhuowang.service.impl;

import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;
import com.cmcc.vrp.boss.zhuowang.bean.UserData;
import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.province.service.impl.BaseTest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 卓望充值接口测试
 *
 * Created by sunyiwei on 2017/3/1.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:conf/applicationContext.xml")
public class FlowPackageOrderServiceImplTest extends BaseTest {
    @Autowired
    FlowPackageOrderService flowPackageOrderService;

    /**
     * 测试流量包订购
     */
    @Test
    @Ignore
    public void testSendRequest() throws Exception {
        List<UserData> userDatas = new ArrayList<UserData>();
        UserData u = new UserData();
        u.setMobNum("18867102100");
        u.setUserPackage("10"); //卓望3元10M包
        userDatas.add(u);

        OrderRequestResult orr = flowPackageOrderService.sendRequest(userDatas, randStr(10));
        assert (orr.getStatus().equals("00"));
    }
}