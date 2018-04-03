/**
 * @Title: 	ServiceBasicParam.java 
 * @Package com.cmcc.xinjiang.boss.model 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:47:57 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.request;

/** 
 * @ClassName:	ServiceBasicParam 
 * @Description:  基本接口，包含成成参数的方法
 * @author:	qihang
 * @date:	2016年3月29日 下午10:47:57 
 *  
 */
public interface ServiceBasicParam {

    /**
     * 转化为packet
     */
    public String toPacket();// 生成xml中body部分的报文
}
