/**
 * @Title: BossService.java
 * @Package com.cmcc.vrp.neimenggu.boss.service
 * @author: qihang
 * @date: 2016年2月2日 下午3:49:35
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu;

import com.cmcc.vrp.boss.neimenggu.model.RespInfo;
import com.cmcc.vrp.boss.neimenggu.model.resp.RespInfoObject;
import com.cmcc.vrp.util.RSAUtil;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.security.interfaces.RSAPublicKey;

/**
 * @ClassName: BossService
 * @Description: boss服务通用类, 主要包含所有的加密和解密功能
 * @author: qihang
 * @date: 2016年2月2日 下午3:49:35
 *
 */
public class NMBossUtils {
    private String pubKeyStr;

    public NMBossUtils(String pubKeyStr) {
        this.pubKeyStr = pubKeyStr;
    }

    public RespInfoObject getRespInfo(String respData) {
        RespInfo info = parseRespData(respData);
        if (info == null) {
            return new RespInfoObject();
        }
        return info.getRespInfo();
    }

    /**
     * 直接得到解码后的数据
     */
    public String getReturnMsg(String respData) {
        if (StringUtils.isEmpty(respData)) {
            return "";
        }
        RespInfo info = parseRespData(respData);
        return decrypBoss(info);
    }


    /**
     * 从返回字符串解析出基本结果对象
     */
    public RespInfo parseRespData(String respData) {
        if (StringUtils.isEmpty(respData)) {
            return null;
        }


        GsonBuilder gb = new GsonBuilder();

        com.google.gson.Gson gson = gb.create();

        return gson.fromJson(respData, RespInfo.class);
    }

    /**
     * 将得到的Boss信息使用指定算法解码
     */
    private String decrypBoss(RespInfo respInfo) {
        try {

            if (respInfo == null) {
                return "";
            }

            String respKey = respInfo.getRespData().getResp_key();

            String respMsg = respInfo.getRespData().getResp_msg();

            RSAPublicKey pubKey = RSAUtil.getPubKeyForEncode(pubKeyStr);

            String content = RSAUtil.RSADecryptForClient(respKey, pubKey);

            byte[] reqMsg = RSAUtil.AESDecrypt(

                Base64.decodeBase64(respMsg),

                content.getBytes("UTF-8")

            );

            return new String(reqMsg);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return "";
    }


}
