package com.cmcc.vrp.queue;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultipleAddressResolverTest {
    Method parse = null;
    Method build = null;
    
    @InjectMocks
    private MultipleAddressResolver multipleAddressResolver = new MultipleAddressResolver();
    
    @org.junit.Before
    public void setUp() throws Exception {
        //设置私有方法可见
        parse = MultipleAddressResolver.class.getDeclaredMethod("parse");
        parse.setAccessible(true);
        
        build = MultipleAddressResolver.class.getDeclaredMethod("build", String.class);
        build.setAccessible(true);
        
    }

    @Test
    public void testParse() throws Exception {
        MultipleAddressResolver mar = new MultipleAddressResolver();
        parse.invoke(mar);
        
        String s1 = "127.0.0.1:6379";
        String s2 = "127.0.0.1";
        assertNotNull(build.invoke(mar, s1));
        assertNotNull(build.invoke(mar, s2));
    }
    
    @Test
    public void testBuild() throws Exception {
        MultipleAddressResolver mar = new MultipleAddressResolver();
        String s1 = "127.0.0.1:6379";
        String s2 = "127.0.0.1";
        assertNotNull(build.invoke(mar, s1));
        assertNotNull(build.invoke(mar, s2));
    }
    
    @Test
    public void testGetAddresses(){
        try {
            multipleAddressResolver.getAddresses();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
