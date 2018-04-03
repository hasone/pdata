package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.PresentSerialNum;

import java.util.List;

/**
 * 批量赠送按块操作时的流水号对应
 * <p>
 * Created by sunyiwei on 2016/11/1.
 */
public interface PresentSerialNumService {
    /**
     * 批量插入流水号对应关系
     *
     * @param psns 流水号对应关系
     * @return 插入成功返回true, 否则false
     */
    boolean batchInsert(List<PresentSerialNum> psns);

    /**
     * 批量插入流水号对应关系
     *
     * @param blockSerialNum 块流水号
     * @param pltSns         平台流水号列表
     * @return 插入成功返回true, 否则false
     */
    boolean batchInsert(String blockSerialNum, List<String> pltSns);

    /**
     * 根据平台流水号查找相应的记录
     *
     * @param pltSn 平台流水号
     * @return 批量赠送流水号对应关系
     */
    PresentSerialNum selectByPltSn(String pltSn);
}
