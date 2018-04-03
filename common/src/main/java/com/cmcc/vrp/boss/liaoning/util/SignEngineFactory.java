
package com.cmcc.vrp.boss.liaoning.util;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.openplatform.sign.ISignEngine;
import com.asiainfo.openplatform.sign.impl.RSASignEngineImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月18日 下午8:15:34
*/
public class SignEngineFactory{
    private static Map<String, ISignEngine> signEngineMap = new HashMap();

    public static ISignEngine getSignEngine() {
        String signMethod = "SHA";
        ISignEngine signEngine = (ISignEngine)signEngineMap.get(signMethod);
        if (signEngine != null) {
            return signEngine;
        }

        if ("RSA".equalsIgnoreCase(signMethod)) {
            signEngine = RSASignEngineImpl.getSingleton();
        } else if ("SHA".equalsIgnoreCase(signMethod)) {
            signEngine = SHASignEngineImpl.getSingleton();
        }
        signEngineMap.put(signMethod, signEngine);
        return signEngine;
    }
}
