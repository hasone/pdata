package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.GlobalConfig;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
*/
public interface GlobalConfigService {

    /**
     * @param record
     * @return
     */
    boolean insert(GlobalConfig record);

    /**
     * @param id
     * @return
     */
    GlobalConfig get(Long id);

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(GlobalConfig record);

    /**
     * @param map
     * @return
     */
    int countGlobalConfig(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<GlobalConfig> selectGlobalConfigPage(Map<String, Object> map);

    /**
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * @return
     */
    List<GlobalConfig> get();

    /**
     * @param configKey
     * @return
     */
    String get(String configKey);

    boolean isEnabled(String key);

    /**
     * @param key
     * @param newValue
     * @param oldValue
     * @return
     */
    boolean updateValue(String key, String newValue, String oldValue);

    /**
     * @param globalConfig
     * @return
     * @throws InvalidParameterException
     */
    boolean validate(GlobalConfig globalConfig) throws InvalidParameterException;
}
