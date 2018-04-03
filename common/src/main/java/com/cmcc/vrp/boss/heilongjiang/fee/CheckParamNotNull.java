package com.cmcc.vrp.boss.heilongjiang.fee;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:33:58
*/
public class CheckParamNotNull {

    /**
     * The input array could not be null and any member of this array could not
     * be null.
     * 
     * @param objects
     */
    public static void check(Object... objects) {
        if (objects == null) {
            throw new RuntimeException("object cannot be null");
        }

        for (Object o : objects) {
            if (o == null) {
                throw new RuntimeException("object cannot be null");
            }
        }
    }
}
