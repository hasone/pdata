package com.cmcc.vrp.boss.xiangshang;

import com.cmcc.vrp.boss.xiangshang.pojo.RequestPojo;
import com.cmcc.vrp.boss.xiangshang.pojo.ResponsePojo;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
 * 向上公司的签名服务
 * <p>
 * Created by sunyiwei on 2016/12/9.
 */
@Service
public class XsSignService {
    private final static Logger LOGGER = LoggerFactory.getLogger(XsSignService.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 校验响应的签名
     *
     * @param responsePojo 响应对象
     * @return 校验通过返回true, 否则false
     */
    public boolean validate(ResponsePojo responsePojo) {
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("id", responsePojo.getId());
        map.put("orderid", responsePojo.getOrderid());
        map.put("deno", String.valueOf(responsePojo.getDeno()));
        map.put("successdeno", String.valueOf(responsePojo.getSuccessdeno()));
        map.put("errcode", responsePojo.getErrcode());
        map.put("errinfo", responsePojo.getErrinfo());

        try {
            return RSACoder.verify(join(map).getBytes(Charsets.UTF_8), getPublicKey(), responsePojo.getSign());
        } catch (Exception e) {
            LOGGER.error("使用公钥校验签名时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return false;
    }

    /**
     * 增加签名内容
     *
     * @param requestPojo 请求对象
     * @return
     */
    public String sign(RequestPojo requestPojo) {
        TreeMap<String, String> map = new TreeMap<String, String>();

        map.put("phone", requestPojo.getPhone());
        map.put("deno", String.valueOf(requestPojo.getDeno()));
        map.put("orderid", requestPojo.getOrderId());
        map.put("arsid", requestPojo.getArsid());
        map.put("time", requestPojo.getTime());
        map.put("macid", requestPojo.getMacid());

        try {
            return RSACoder.sign(join(map).getBytes(Charsets.UTF_8), getPrivateKey());
        } catch (Exception e) {
            LOGGER.error("使用私钥签名时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    private String join(TreeMap<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (String key : map.keySet()) {
            sb.append(key).append(map.get(key));
        }

        return sb.toString();
    }

    //获取平台侧往向上公司请求时的私钥
    private String getPrivateKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XS_PRIVATE_KEY.getKey());
    }

    //获取向上公司的公钥
    private String getPublicKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XS_PUBLIC_KEY.getKey());
    }
}
