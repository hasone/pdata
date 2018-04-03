package com.cmcc.vrp.boss.beijing.util;

import com.bmcc.service.pub.util.Tea;

/**
 * Created by leelyn on 2016/7/26.
 */
public class TeaUtil {

    /**
     * 北京渠道字段加密
     *
     * @param data
     * @return
     */
    public static String encrypt(String data) {
        Tea tea = new Tea();
        return tea.encryptByTea(data);
    }
}
