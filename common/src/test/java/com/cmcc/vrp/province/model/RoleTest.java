package com.cmcc.vrp.province.model;

import org.junit.Test;

import com.cmcc.vrp.exception.SelfCheckException;

/**
 * 
 * @author Administrator
 *
 */
public class RoleTest {
    @Test
    public void testSelfCheck(){
        Role role = new Role();
        role.setName("abcde");
        role.setCode("123");
        try {
            role.selfCheck();
        } catch (SelfCheckException e) {
        }
    }
}
