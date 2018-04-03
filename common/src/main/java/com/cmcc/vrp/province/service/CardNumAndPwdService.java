package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;

import java.util.List;

import org.joda.time.DateTime;

/**
 * 营销卡的卡号卡密服务
 * <p>
 * Created by sunyiwei on 2016/11/29.
 */
public interface CardNumAndPwdService {
    /**
     * 校验卡号卡密是否匹配
     *
     * @param cardNum  卡号
     * @param password 卡密
     * @return 校验通过返回true, 否则false
     */
    boolean validate(String cardNum, String password);

    /**
     * 根据卡号信息获取卡信息
     *
     * @param cardNum 卡号
     * @return
     */
    MdrcCardInfo get(String cardNum);

    /**
     * 生成一定数量的营销卡
     *
     * @param mdrcBatchConfig 营销卡批次信息
     * @return
     */
    List<String> generatCardNums(MdrcBatchConfig mdrcBatchConfig);

    /**
     * 批量生成营销卡的卡密
     *
     * @param list 卡号序列
     */
    void generatePasswords(List<MdrcCardInfo> list);

    /**
     * 根据卡号和数量信息，计算终止卡号
     *
     * @param beginCardNum 起始卡号
     * @param delta        数量
     * @return 终止卡号，如果起始卡号+数量已经超过了该批次的最大卡号，返回空
     */
    String cal(String beginCardNum, int delta);
	
    List<MdrcCardInfo> getBycreateTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByStoredTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByBoundTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByActivatedTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByUsedTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByLockedTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getBySerialNum(Long serialId, String year);
    
    List<MdrcCardInfo> getByDeactivateTime(DateTime start, DateTime end);
    
    List<MdrcCardInfo> getByUnlockTime(DateTime start, DateTime end);

    List<MdrcCardInfo> getBill(DateTime start, DateTime end);
}
