package com.cmcc.vrp.boss.chongqing;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author lgk8023
 * @date 2016年12月7日 上午8:35:02
 */
@Ignore
public class TestInterface {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     *
     */
    @Test
    public void testInterface() {
        CQBossServiceImpl cqBossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
        System.out.println(cqBossService.getEnterPrdSolde("2308091054885", "gl_mwsq_11G"));
        System.out.println(cqBossService.getProductsOrderByEnterCode("2308091054885"));
    }

}
 