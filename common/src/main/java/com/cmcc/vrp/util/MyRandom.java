package com.cmcc.vrp.util;

import java.util.Random;

/**
 * Created by leelyn on 2016/5/23.
 */
public class MyRandom {

    /**
     * 产生随机数
     *
     * @return
     */
    public static long produceRandom() {
        Random rand = new Random();
        int rInt = rand.nextInt();
        return rInt;
    }
}
