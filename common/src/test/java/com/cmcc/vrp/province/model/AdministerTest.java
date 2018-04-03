package com.cmcc.vrp.province.model;

import org.junit.Test;

import com.cmcc.vrp.exception.SelfCheckException;

/**
 * 
 * @author Administrator
 *
 */
public class AdministerTest {
    /**
     * 
     */
    @Test
    public void testSelfCheck(){
        Administer admin = new Administer();
        admin.setUserName("abc");
        admin.setPassword("abcde123456.");
        admin.setMobilePhone("18867102012");
        admin.setEmail("aaa@chinamobiel.com");
        try {
            admin.selfCheck();
        } catch (SelfCheckException e) {
        }
    }
}
