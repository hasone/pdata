package com.cmcc.vrp.util;

import java.util.Random;

/**
 * 使用反射测试私有方法的覆盖率
 *
 * Created by sunyiwei on 2016/10/15.
 */
public class PrivateMethodClass {
    /**
     * 公有方法
     */
    public void publicMethod() {
        Random random = new Random();
        privateMethod(random.nextBoolean());
    }


    /**
     * 私有方法
     *
     * @param flag 布尔参数
     */
    private void privateMethod(Boolean flag) {
        if (flag != null && !flag) {
            System.out.println("TRUE");
        } else {
            System.out.println("FALSE");
        }
    }
}
