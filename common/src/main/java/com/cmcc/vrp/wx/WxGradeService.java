package com.cmcc.vrp.wx;

import java.util.List;

import com.cmcc.vrp.wx.model.WxGrade;

/**
 * WxGradeService.java
 * @author wujiamin
 * @date 2017年3月2日
 */
public interface WxGradeService {

    /** 
     * @Title: selectByGrade 
     */
    WxGrade selectByGrade(Integer grade);

    /** 
     * @Title: selectAllGrade 
     */
    List<WxGrade> selectAllGrade();

}
