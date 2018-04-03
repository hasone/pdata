package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.MdrcActiveDetail;

/**
 * Created by qinqinyan on 2016/11/18.
 */
public interface MdrcActiveDetailService {

    /**
     * 根据主键删除
     * @param id
     * @return
     * @author qinqinyan
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean insert(MdrcActiveDetail record);

    /**
     * 插入
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean insertSelective(MdrcActiveDetail record);

    /**
     * 根据主键id查找
     * @param id
     * @return
     * @author qinqinyan
     * */
    MdrcActiveDetail selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean updateByPrimaryKeySelective(MdrcActiveDetail record);

    /**
     * 更新
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean updateByPrimaryKey(MdrcActiveDetail record);

    /**
     * 根据审批请求id查找
     * @param requestId 审批请求id
     * @return
     * @author qinqinyan
     * */
    MdrcActiveDetail selectByRequestId(Long requestId);

    /**
     * @param record
     * @return
     * @author qinqinyan
     * */
    boolean updateByRequestIdSelective(MdrcActiveDetail record);
    
    /**
     * 
     * @Title: selectByconfigIdAndStatus 
     * @Description: TODO
     * @param configId
     * @param status
     * @return
     * @return: List<MdrcActiveDetail>
     */
    List<MdrcActiveDetail> selectByconfigIdAndStatus(Long configId, Integer status);
    
}
