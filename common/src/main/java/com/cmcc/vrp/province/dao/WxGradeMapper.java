package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.wx.model.WxGrade;

/**
 * WxGradeMapper.java
 * @author wujiamin
 * @date 2017年3月2日
 */
public interface WxGradeMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(WxGrade record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(WxGrade record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxGrade selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(WxGrade record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(WxGrade record);

    /** 
     * @Title: selectByGrade 
     */
    WxGrade selectByGrade(Integer grade);

    /** 
     * @Title: selectAllGrade 
     */
    List<WxGrade> selectAllGrade();
}