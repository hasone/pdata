package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;

/**
 * Created by qinqinyan on 2016/11/30.
 * 制卡审核服务类
 */
public interface MdrcCardmakeDetailService {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:todo
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:todo
     * */
    boolean insert(MdrcCardmakeDetail record);

    /**
     * @Title:insertSelective
     * @Description:todo
     * */
    boolean insertSelective(MdrcCardmakeDetail record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:todo
     * */
    MdrcCardmakeDetail selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:todo
     * */
    boolean updateByPrimaryKeySelective(MdrcCardmakeDetail record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:todo
     * */
    boolean updateByPrimaryKey(MdrcCardmakeDetail record);

    /**
     * 根据请求id查找
     * @param requestId
     * @return
     * @author qinqinyan
     * */
    MdrcCardmakeDetail selectByRequestId(Long requestId);

    /**
     * 生成卡数据
     * @param requestId
     * @return adminId
     * @author qinqinyan
     * */
    boolean makecard(Long requestId, Long adminId);
    
    /**
     * 更新快递单号
     * @param mdrcCardmakeDetail
     * @param mdrcBatchConfig
     * @author qinqinyan
     * @date 2017/08/10
     * */
    boolean updateTrackingNumber(MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfig mdrcBatchConfig);
}
