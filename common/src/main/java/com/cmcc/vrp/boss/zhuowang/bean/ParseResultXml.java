package com.cmcc.vrp.boss.zhuowang.bean;

import com.cmcc.vrp.boss.zhuowang.utils.ParseXml;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 解析全国接口响应报文
 *
 * @author qinpo
 */
public class ParseResultXml {

    private static final Logger logger = LoggerFactory.getLogger(ParseResultXml.class);

    /**
     * 解析回调报文体
     *
     * @param xml
     * @return
     */
    public static OrderHandleResult parseHandleResultXml(String xml) {
        OrderHandleResult result = new OrderHandleResult();

        try {
            Map map = ParseXml.xml2Map(xml);
            String svcCont = (String) map.get("SvcCont");
            if (svcCont != null) {
                Map svcContMap = ParseXml.xml2Map(svcCont);
                result.setOperSeq((String) svcContMap.get("OperSeq"));
                result.setSuccNum(Integer.valueOf((String) svcContMap.get("SuccNum")));
                result.setFailNum(Integer.valueOf((String) svcContMap.get("FailNum")));

                //成功号码
                Object succInfoObj = svcContMap.get("SuccInfo");
                if (succInfoObj != null) {
                    List<String> succTels = new ArrayList<String>();
                    if (succInfoObj instanceof Map) {
                        succTels.add((String) ((Map) succInfoObj).get("SuccTel"));
                    } else if (succInfoObj instanceof List) {
                        List<Map> succInfoList = (List<Map>) succInfoObj;
                        for (Map succInfoMap : succInfoList) {
                            succTels.add((String) succInfoMap.get("SuccTel"));
                        }
                    }
                    result.setSuccMobNum(succTels);
                }

                //失败号码
                Object failInfoObj = svcContMap.get("FailInfo");
                if (failInfoObj != null) {
                    List<OrderFailInfo> list = new ArrayList<OrderFailInfo>();
                    if (failInfoObj instanceof Map) {
                        OrderFailInfo failInfo = new OrderFailInfo();
                        failInfo.setErrorCode((String) ((Map) failInfoObj).get("Rsp"));
                        failInfo.setErrorDesc((String) ((Map) failInfoObj).get("RspDesc"));
                        failInfo.setMobNum((String) ((Map) failInfoObj).get("MobNum"));

                        list.add(failInfo);
                    } else if (failInfoObj instanceof List) {
                        List<Map> failInfoList = (List<Map>) failInfoObj;
                        for (Map failInfoMap : failInfoList) {
                            OrderFailInfo failInfo = new OrderFailInfo();
                            failInfo.setErrorCode((String) failInfoMap.get("Rsp"));
                            failInfo.setErrorDesc((String) failInfoMap.get("RspDesc"));
                            failInfo.setMobNum((String) failInfoMap.get("MobNum"));

                            list.add(failInfo);
                        }
                    }
                    result.setFailInfos(list);
                }

            }
        } catch (DocumentException e) {
            logger.error("解析回调报文出错：" + xml);
        }

        return result;
    }

}
