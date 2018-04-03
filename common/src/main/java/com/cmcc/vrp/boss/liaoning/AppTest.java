package com.cmcc.vrp.boss.liaoning;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月30日 下午3:38:24
*/
public class AppTest {
    public static void main(String[] args) throws Exception {
        
        System.out.println(Thread.currentThread().getContextClassLoader().getResourceAsStream("aiesb.properties"));
       
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");//此文件放在SRC目录下

        LnBossServiceImpl lnBossService=(LnBossServiceImpl)context.getBean("lnBossService");

        System.out.println(lnBossService.charge(null, null, "18867105766", "xxxxx", null).isSuccess());
    }
    
}