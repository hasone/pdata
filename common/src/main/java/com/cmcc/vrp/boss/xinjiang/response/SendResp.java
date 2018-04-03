/**
 * @Title: 	SendResp.java 
 * @Package com.cmcc.xinjiang.boss.model.response 
 * @author:	qihang
 * @date:	2016年3月30日 下午7:02:17 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.response;

import java.util.Map;

/**
 * @ClassName: SendResp
 * @Description: 转增返回报文
 * @author: qihang
 * @date: 2016年3月30日 下午7:02:17
 * 
 */

public class SendResp {
    private String resultCode;// 状态码

    private String resultInfo;// 状态信息

    private String tradeId = "";// 返回的交易流水号，只有在交易成功时才会返回

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    /**
     * 分析结果
     * @param bodyPacket
     * @return
     */
    public static SendResp analyseRespBodyPacket(String bodyPacket) {
        Map<String, String> paramMap = GroupInfoResp.getResponseMap(bodyPacket);
        SendResp resp = new SendResp();

        resp.setResultCode(paramMap.get("X_RESULTCODE"));

        resp.setResultInfo(paramMap.get("X_RESULTINFO"));

        if (resp.getResultCode().equals("0")) {// result_code为0时是代表成功

            resp.setTradeId(paramMap.get("TRADE_ID"));

        }

        return resp;
    }
    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     * <SvcInfo><Header><System><COMMUNICATE value="02" index="0" /><TRANSFER
     * value="01" index="0" /><ORGCHANNELID value="A001" index="0"
     * /><HOMECHANNELID /></System><Inparam><CHANNEL_TRADE_ID
     * value="E00320090401165020000000000000" index="0"
     * /></Inparam><Outparam><RESULT_CODE value="0" /><RESULT_INFO value="OK!"
     * /></Outparam><TESTFLAG value="0" index="0" /><ACTIONCODE value="1"
     * index="0" /></Header> <Body><SVC_CONTENT>{X_RESULTINFO=["Trade OK!"],
     * X_LAST_RESULTINFO=["Trade OK!"], TRADE_ID=["9116033064603408"],
     * X_RESULTCODE=["0"], X_RECORDNUM=["1"]}</SVC_CONTENT></Body> </SvcInfo>
     */

}
