package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;

/**
 * Created by qinqinyan on 2016/12/1.
 */
public interface MdrcMakecardRequestConfigService {

    /**
     * @title:deleteByPrimaryKey
     * @description:
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * @description:
     * */
    boolean insert(MdrcMakecardRequestConfig record);

    /**
     * @title:insertSelective
     * @description:
     * */
    boolean insertSelective(MdrcMakecardRequestConfig record);

    /**
     * @title:selectByPrimaryKey
     * @description:
     * */
    MdrcMakecardRequestConfig selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * @description:
     * */
    boolean updateByPrimaryKeySelective(MdrcMakecardRequestConfig record);

    /**
     * @title:updateByPrimaryKey
     * @description:
     * */
    boolean updateByPrimaryKey(MdrcMakecardRequestConfig record);

    /**
     * @title:selectByRequestId
     * @description:
     * */
    MdrcMakecardRequestConfig selectByRequestId(Long requestId);
    
    /**
     * @title:selectByConfigId
     * @description:
     * */
    MdrcMakecardRequestConfig selectByConfigId(Long configId);
}
