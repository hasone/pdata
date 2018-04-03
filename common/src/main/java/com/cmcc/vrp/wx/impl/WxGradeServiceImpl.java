package com.cmcc.vrp.wx.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxGradeMapper;
import com.cmcc.vrp.wx.WxGradeService;
import com.cmcc.vrp.wx.model.WxGrade;

/**
 * WxGradeServiceImpl.java
 * @author wujiamin
 * @date 2017年3月2日
 */
@Service
public class WxGradeServiceImpl implements WxGradeService {
    @Autowired
    WxGradeMapper mapper;
    
    @Override
    public WxGrade selectByGrade(Integer grade){
        return mapper.selectByGrade(grade);
    }
    
    @Override
    public List<WxGrade> selectAllGrade(){
        return mapper.selectAllGrade();
    }
}
