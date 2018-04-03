package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.GlobalConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:22:18
*/
public interface GlobalConfigMapper {

    /**
     * @param record
     * @return
     */
    int insert(GlobalConfig record);

    /**
     * @param id
     * @return
     */
    GlobalConfig get(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(GlobalConfig record);

    /**
     * @param map
     * @return
     */
    int countGlobalConfig(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<GlobalConfig> selectGlobalConfigForPages(Map<String, Object> map);

    /**
     * @param id
     * @return
     */
    int delete(Long id);

    List<GlobalConfig> getAllConfigs();

    GlobalConfig getByConfigKey(String configKey);

    //更新全局配置的值
    /**
     * @param cfgKey
     * @param cfgNewValue
     * @param cfgOldValue
     * @return
     */
    int updateValue(@Param("key") String cfgKey, @Param("newValue") String cfgNewValue,
                    @Param("oldValue") String cfgOldValue);
}