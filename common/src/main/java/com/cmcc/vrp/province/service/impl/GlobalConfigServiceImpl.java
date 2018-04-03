package com.cmcc.vrp.province.service.impl;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.GlobalConfigMapper;
import com.cmcc.vrp.province.model.GlobalConfig;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.google.gson.Gson;

@Service
public class GlobalConfigServiceImpl extends AbstractCacheSupport implements GlobalConfigService {
    private static Logger LOGGER = LoggerFactory.getLogger(GlobalConfigServiceImpl.class);

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Override
    public boolean insert(GlobalConfig record) {
        return validate(record) && globalConfigMapper.insert(record) == 1
                && cacheService.add(getKeys(record), new Gson().toJson(record));

    }

    @Override
    public GlobalConfig get(final Long id) {
        if (id == null) {
            return null;
        }

        return process(new Operator() {
            @Override
            public String getValueFromCache() {
                return cacheService.get(String.valueOf(id));
            }

            @Override
            public GlobalConfig getFromDB() {
                return globalConfigMapper.get(id);
            }

            @Override
            public String getKey() {
                return String.valueOf(id);
            }
        });
    }

    @Override
    public boolean updateByPrimaryKeySelective(GlobalConfig record) {
        return record != null && record.getId() != null && cacheService.delete(getKeys(record))
                && globalConfigMapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public int countGlobalConfig(Map<String, Object> map) {
        return map == null ? 0 : globalConfigMapper.countGlobalConfig(map);
    }

    @Override
    public List<GlobalConfig> selectGlobalConfigPage(Map<String, Object> map) {
        return map == null ? null : globalConfigMapper.selectGlobalConfigForPages(map);
    }

    @Override
    public boolean delete(Long id) {
        GlobalConfig globalConfig = null;

        return id != null && (globalConfig = get(id)) != null && cacheService.delete(getKeys(globalConfig))
                && globalConfigMapper.delete(id) == 1;
    }

    @Override
    public List<GlobalConfig> get() {
        return globalConfigMapper.getAllConfigs();
    }

    @Override
    public String get(final String configKey) {
        GlobalConfig globalConfig = getInternal(configKey);
        if (globalConfig == null) {
            return null;
        }

        return globalConfig.getConfigValue();
    }

    private GlobalConfig getInternal(final String configKey) {
        if (StringUtils.isBlank(configKey)) {
            return null;
        }

        return process(new Operator() {
            @Override
            public String getValueFromCache() {
                return cacheService.get(configKey);
            }

            @Override
            public GlobalConfig getFromDB() {
                return globalConfigMapper.getByConfigKey(configKey);
            }

            @Override
            public String getKey() {
                return configKey;
            }
        });
    }

    /*
     * 是否启用指定标识位， OK代表启用，其它代表不启用
     */
    @Override
    public boolean isEnabled(String key) {
        String value = get(key);
        final String expectedValue = "OK";

        return StringUtils.isNotBlank(value) && expectedValue.equalsIgnoreCase(value);
    }

    @Override
    public boolean updateValue(String key, String newValue, String oldValue) {
        GlobalConfig globalConfig = null;

        if (StringUtils.isBlank(key) || StringUtils.isBlank(newValue) || StringUtils.isBlank(oldValue)) {
            LOGGER.error("无效的全局配置参数, 更新全局配置参数失败. Key={}, NewValue={}, OldValue={}.", key, newValue, oldValue);
            return false;
        }

        //尝试删除缓存中的缓存
        return (globalConfig = getInternal(key)) != null && cacheService.delete(getKeys(globalConfig))
                && globalConfigMapper.updateValue(key, newValue, oldValue) == 1;
    }

    @Override
    public boolean validate(GlobalConfig globalConfig) throws InvalidParameterException {
        if (globalConfig == null) {
            throw new InvalidParameterException("无效的全局配置对象.");
        }

        if (StringUtils.isBlank(globalConfig.getName())) {
            throw new InvalidParameterException("全局配置对象的名称不能为空.");
        }

        if (StringUtils.isBlank(globalConfig.getConfigKey())) {
            throw new InvalidParameterException("全局配置对象的Key不能为空.");
        }

        if (StringUtils.isBlank(globalConfig.getConfigValue())) {
            throw new InvalidParameterException("全局配置对象的Value不能为空.");
        }

        if (StringUtils.isBlank(globalConfig.getDescription())) {
            throw new InvalidParameterException("全局配置对象的描述不能为空.");
        }

        return true;
    }

    @Override
    protected String getPrefix() {
        return "global.config.";
    }

    private GlobalConfig process(Operator operator) {
        GlobalConfig globalConfig = null;

        String value = operator.getValueFromCache();

        try {
            globalConfig = new Gson().fromJson(value, GlobalConfig.class);
        } catch (Exception e) {
            LOGGER.error("解析缓存数据时出错, 缓存数据为{}, 错误信息为{}, 错误堆栈为{}.", value, e.getMessage(), e.getStackTrace());
        }

        //缓存没有命中
        if (globalConfig == null) {
            globalConfig = operator.getFromDB();
            if (globalConfig != null && !cacheService.add(getKeys(globalConfig), new Gson().toJson(globalConfig))) {
                LOGGER.error("设置缓存时出错，Key = {}， Value = {}.", operator.getKey(), new Gson().toJson(globalConfig));
            }
        }

        return globalConfig;
    }

    private List<String> getKeys(GlobalConfig globalConfig) {
        List<String> keys = new LinkedList<String>();

        keys.add(String.valueOf(globalConfig.getId()));
        keys.add(globalConfig.getConfigKey());

        return keys;
    }

    private interface Operator {
        String getValueFromCache();

        GlobalConfig getFromDB();

        String getKey();
    }
}
