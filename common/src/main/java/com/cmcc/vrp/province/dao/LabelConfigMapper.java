package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.LabelConfig;

import java.util.List;
import java.util.Map;
/**
 * @Title:LabelConfigMapper
 * @Description:
 * */
public interface LabelConfigMapper {
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(LabelConfig record);
    /**
     * @Title:get
     * @Description:
     * */
    LabelConfig get(Long id);
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(LabelConfig record);
    /**
     * @Title:countLabelConfig
     * @Description:
     * */
    int countLabelConfig(Map<String, Object>map);
    /**
     * @Title:selectLabelConfigForPages
     * @Description:
     * */
    List<LabelConfig> selectLabelConfigForPages(Map<String, Object>map);
    /**
     * @Title:delete
     * @Description:
     * */
    int delete(Long id);
    /**
     * @Title:getAllConfigs
     * @Description:
     * */
    List<LabelConfig> getAllConfigs();
    /**
     * @Title:getByName
     * @Description:
     * */
    List<LabelConfig> getByName(String name);
}