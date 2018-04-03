package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;

import java.util.List;

/**
 * Created by qinqinyan on 2016/12/1.
 */
public interface MdrcActiveRequestConfigService {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    boolean insert(MdrcActiveRequestConfig record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    boolean insertSelective(MdrcActiveRequestConfig record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    MdrcActiveRequestConfig selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    boolean updateByPrimaryKeySelective(MdrcActiveRequestConfig record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    boolean updateByPrimaryKey(MdrcActiveRequestConfig record);

    /**
     * 激活操作
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean active(MdrcActiveRequestConfig record);

    /**
     * 根据requestId查询
     * @param requestId
     * @author qinqinyan
     * */
    List<MdrcActiveRequestConfig> selectByRequestId(Long requestId);
}
