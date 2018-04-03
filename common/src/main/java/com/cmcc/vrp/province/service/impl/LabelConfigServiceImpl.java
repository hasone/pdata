package com.cmcc.vrp.province.service.impl;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmcc.vrp.province.dao.LabelConfigMapper;
import com.cmcc.vrp.province.model.LabelConfig;
import com.cmcc.vrp.province.service.LabelConfigService;

@Service
public class LabelConfigServiceImpl implements LabelConfigService {
    @Autowired
    private LabelConfigMapper labelConfigMapper;

    @Override
    public boolean insert(LabelConfig record) {
        return validate(record)
                && labelConfigMapper.insert(record) == 1;
    }

    @Override
    public LabelConfig get(Long id) {
        return id == null ? null : labelConfigMapper.get(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(LabelConfig record) {
        return record != null
                && record.getId() != null
                && labelConfigMapper.updateByPrimaryKeySelective(record) == 1;
    }


    @Override
    public int countLabelConfig(Map<String, Object> map) {
        return map == null ? 0 : labelConfigMapper.countLabelConfig(map);
    }

    @Override
    public List<LabelConfig> selectLabelConfigPage(Map<String, Object> map) {
        return map == null ? null : labelConfigMapper.selectLabelConfigForPages(map);
    }

    @Override
    public boolean delete(Long id) {
        return id != null && labelConfigMapper.delete(id) == 1;
    }

    @Override
    public List<LabelConfig> get() {
        return labelConfigMapper.getAllConfigs();
    }

    @Override
    public Map<String, String> getLabelConfigMap(String keyword) {
    	List<LabelConfig> labelConfigList = labelConfigMapper.getByName(keyword);
    	Map<String, String> labelConfigMap = new LinkedHashMap<String, String>();
    	if (labelConfigList == null || labelConfigList.isEmpty()) {
    	    return labelConfigMap;
    	}
    	
    	for (LabelConfig labelConfig : labelConfigList) {
    	    labelConfigMap.put(labelConfig.getName(), labelConfig.getDefaultValue());
    	}
    	return labelConfigMap;
    }

    @Override
    public boolean validate(LabelConfig labelConfig) throws InvalidParameterException {
        if (labelConfig == null) {
            throw new InvalidParameterException("无效的标签配置对象.");
        }

        if (StringUtils.isBlank(labelConfig.getName())) {
            throw new InvalidParameterException("标签配置对象的Key值名称不能为空.");
        }

        if (StringUtils.isBlank(labelConfig.getDefaultValue())) {
            throw new InvalidParameterException("标签配置对象的默认值不能为空.");
        }
        
        if (StringUtils.isBlank(labelConfig.getDescription())) {
            throw new InvalidParameterException("标签配置对象的描述不能为空.");
        }

        return true;
    }
}
