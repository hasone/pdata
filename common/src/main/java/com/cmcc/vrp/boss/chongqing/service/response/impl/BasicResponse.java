/**
 * @Title: BasicResponse.java
 * @Package com.cmcc.vrp.chongqing.boss.service.response
 * @author: qihang
 * @date: 2015年4月28日 上午11:08:51
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.service.response.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @ClassName: BasicResponse
 * @Description: Boss连接的基本response接口，分析取得的数据
 * @author: qihang
 * @date: 2015年4月28日 上午11:08:51
 *
 */
public interface BasicResponse {

    /**
     * 处理从服务器端收到的数据
     * @param recvDatas   接受的数据
     * @throws UnsupportedEncodingException   异常
     */
    public void recvData(byte[] recvDatas) throws UnsupportedEncodingException;

    /**
     * 响应处理类
     */
    public void responseHandler();

    /**
     * 用于测试用，观看结果，之后删除
     */
    public void printAll();

    /**
     *
     * @Title:setParams
     * @Description: 设置参数
     * @param params
     * @throws
     * @author: sunyiwei
     */
    public void setParams(Map<String, Object> params);

    public String getRetCode();

    public String getRetMsg();

    public Object getReturnContent();

}
