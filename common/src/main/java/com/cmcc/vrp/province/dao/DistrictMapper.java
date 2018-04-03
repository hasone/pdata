/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.District;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author JamieWu
 */
public interface DistrictMapper {
    /** 
     * 根据各种条件查找
     * @Title: selectByMap 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日
    */
    List<District> selectByMap(Map map);

    /** 
     * @Title: selectById 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日
    */
    District selectById(Long id);

    /** 
     * 查询某地区及其子地区id
     * @Title: selectNodeById 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日
    */
    List<Long> selectNodeById(Long id);

    /**
     * @param adminDistrictds
     * @return
     */
    List<Long> selectNodeByIds(@Param("adminDistrictds") List<AdminDistrict> adminDistrictds);


    /**
     * 根据Id查该地区的完整名称
     */
    String selectFullDistrictNameById(Long id);

    /**
     * @return
     */
    List<District> list();


    /**
     * 通过父节点找子节点地区
     *
     * @param parentId
     * @return
     */
    List<District> selectDisByParentId(@Param("parentId") Long parentId);

    /**
     * @param sonId
     * @return
     */
    District selectDisBySonId(@Param("id") Long sonId);

    /**
     * @param name
     * @return
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
