/**
 * @Title: MdrcEcService.java
 * @Package com.cmcc.vrp.province.mdrc.service
 * @author: qihang
 * @date: 2016年5月26日 下午3:44:02
 * @version V1.0
 */
package com.cmcc.vrp.province.mdrc.service;


import com.cmcc.vrp.exception.PreconditionRequiredException;
import com.cmcc.vrp.exception.UnprocessableEntityException;
import com.cmcc.vrp.province.mdrc.model.MdrcEcCardQueryInfo;
import com.cmcc.vrp.province.mdrc.model.MdrcEcRequestData;
import com.cmcc.vrp.province.mdrc.model.MdrcEcResponseData;

/**
 * @ClassName: MdrcEcService
 * @Description: 营销卡EC接口的交互类
 * @author: qihang
 * @date: 2016年5月26日 下午3:44:02
 *
 */
public interface MdrcEcService {


    /**
     * 接受ec接口数据，转交后台处理类
     * @throws UnprocessableEntityException
     * @throws PreconditionRequiredException
     */
    MdrcEcResponseData operate(MdrcEcRequestData mdrcEcRequest) throws UnprocessableEntityException, PreconditionRequiredException;


    /**
     * 查询卡信息,需要用boss返回的结果
     *
     */
    MdrcEcCardQueryInfo queryCardInfo(String cardNumber);


    /**
     * 使用卡，给指定手机充值某张卡
     * 返回类型暂定为boolean
     */
    boolean useCard(String cardNum, String password, String mobile, String ip, String serialNum);


}
