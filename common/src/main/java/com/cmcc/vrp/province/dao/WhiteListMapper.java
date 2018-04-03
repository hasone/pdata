package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.WhiteList;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:33:41
*/
public interface WhiteListMapper {
    /**
     * 利用手机号获取对象
     */
    WhiteList get(String mobile);

    /**
     * 插入白名单对象
     */
    int insert(WhiteList whiteList);

    /**
     * 删除白名单对象
     */
    int delete(String mobile);

    /**
     * 根据ID删除白名单对象
     */
    int deleteById(Long id);

    /**
     * 按搜索条件查询符合的条数
     */
    Long count(Map<String, Object> params);

    /**
     * 返回符合条件的白名单
     */
    List<WhiteList> query(Map<String, Object> params);

    /**
     * 批量插入
     */
    int insertBatch(List<String> phones);
}