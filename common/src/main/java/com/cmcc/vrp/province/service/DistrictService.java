package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.District;

import java.util.List;

/**
 * Created by leelyn on 2016/6/1.
 */
public interface DistrictService {

    /**
     * @param parentId
     * @return
     */
    List<District> queryByParentId(Long parentId);

    /**
     * @param sonId
     * @return
     */
    District queryBySonId(Long sonId);

    /**
     * @param id
     * @return
     */
    District queryById(Long id);

    /**
     * @param id
     * @return
     */
    String selectFullDistrictNameById(Long id);

    /*
     * 查询某地区及其子地区id
     */
    /**
     * @param id
     * @return
     */
    List<Long> selectNodeById(Long id);

    /**
     * 根据地区名字查找子地区
     *
     * @param name
     * @return
     * @date 2016年7月28日
     * @author wujiamin
     */
    List<District> selectChildByName(String name);


    /**
     * @param name
     * @return
     */
    List<District> selectByName(String name);

    /**
     * @param parentId
     * @return
     */
    List<District> selectByParentId(Long parentId);
}
