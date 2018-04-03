/**
 * @Title: BasicRequest.java
 * @Package com.cmcc.vrp.chongqing.boss.service.request
 * @author: qihang
 * @date: 2015年4月28日 上午9:36:48
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.service.request.impl;

import java.util.Map;

/**
 * @ClassName: BasicRequest
 * @Description: Boss连接的基本request接口，组装成报文
 * @author: qihang
 * @date: 2015年4月28日 上午9:36:48
 *
 */
public interface BasicRequest {
    //设置包头及包体xml中的<HEAD>
    public void setHeader();

    //设置该服务需要的参数
    public void setParams(Map<String, Object> params);

    /**
     * 参数检查，检查所有数据是否合法
     * @return  结果
     */
    public boolean frontCheck();

    //得到报文的byte[]格式
    public byte[] getRequestDatas();
}
