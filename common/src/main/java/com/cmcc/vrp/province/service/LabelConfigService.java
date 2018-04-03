package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.LabelConfig;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public interface LabelConfigService {

    boolean insert(LabelConfig record);

    LabelConfig get(Long id);

    boolean updateByPrimaryKeySelective(LabelConfig record);

    int countLabelConfig(Map<String, Object> map);
    
    List<LabelConfig> selectLabelConfigPage(Map<String, Object> map);
    
    boolean delete(Long id);
    
    List<LabelConfig> get();
    
    Map<String, String> getLabelConfigMap(String keyword);

    boolean validate(LabelConfig globalConfig) throws InvalidParameterException;
}
