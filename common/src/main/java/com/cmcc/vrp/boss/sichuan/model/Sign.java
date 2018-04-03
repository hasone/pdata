/**
 *
 */
package com.cmcc.vrp.boss.sichuan.model;

import com.cmcc.vrp.util.MyRSA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;

/**
 * @author JamieWu
 *         BOSS能力开放平台签名类
 */
public class Sign {

    /** 
     * @Title: sign 
     * @param str
     * @param keyfile
     * @return
     * @throws Exception
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    private static Logger LOG = LoggerFactory.getLogger(Sign.class);
	
    /**
     * @param str
     * @param keyfile
     * @return
     * @throws Exception
     */
    public static String sign(String str, String keyfile) throws Exception {
    	LOG.debug("签名字符串{}", str);

    	//1、字典排序
        //2、请求参数做urlEncode
        str = URLEncoder.encode(str, "UTF-8");
        LOG.debug("一次URL编码后{}", str);
        
        //3、对字符串做MD5加密
        String md5Str = DigestUtils.md5DigestAsHex(str.getBytes());
        LOG.debug("M5得到{}", md5Str);
        
        //4、私钥加密
        //String sign = MyRSA.encryptWithFile(md5Str, keyfile);

        String sign = MyRSA.signWithFile(md5Str, keyfile);
        LOG.debug("私钥签名后{}", sign);
        
        String result = URLEncoder.encode(sign, "UTF-8");
        LOG.debug("二次URL编码后{}", result);
        return result;
    }
}
