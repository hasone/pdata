/**
 * @Title: MdrcEcCardInfo.java
 * @Package com.cmcc.vrp.province.mdrc.model
 * @author: qihang
 * @date: 2016年5月26日 下午4:28:10
 * @version V1.0
 */
package com.cmcc.vrp.province.mdrc.model;

import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;

import java.util.Date;

/**
 * @ClassName: MdrcEcCardInfo
 * @Description: 查询卡状态返回类，boss接口返回
 * @author: qihang
 * @date: 2016年5月26日 下午4:28:10
 *
 */
public class MdrcEcCardQueryInfo {
    String cardNum;

    /**
     * 卡状态
     */
    MdrcCardStatus status;

    /**
     * 过期时间,待定
     */
    Date expireTime;

    /**
     * 使用时间，待定
     */
    Date usedTime;

    /**
     * 绑定企业Code,待定
     */
    Long bindEntCode;

    /**
     * 绑定产品Code,待定
     */
    Long bindPrdCode;

    /**
     * 绑定产品Size,待定
     */
    String bindPrdSize;
}
