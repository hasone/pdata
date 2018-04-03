package com.cmcc.vrp.boss.zhuowang.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:28:05
*/
public class ToolsUtils {

    public static String getNoLineYYYYMMDD(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        return dateformat.format(date);
    }

    public static String getNoLineYYYYMMDDHHMMSS(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateformat.format(date);
    }


    public static String getSha256(String source) {
        return DigestUtils.sha256Hex(source);
    }

}
