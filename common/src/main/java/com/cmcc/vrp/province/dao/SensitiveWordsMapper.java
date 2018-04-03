package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SensitiveWords;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月20日 上午11:29:25
*/
public interface SensitiveWordsMapper {
    /**
     * 新增敏感词库
     * 
     * @param sensitiveWords
     * @return
     */
    int insert(SensitiveWords sensitiveWords);
    
    
    /**
     * 批量插入
     * 
     * @param sensitiveWordsList
     * @return
     */
    int batchInsert(@Param("sensitiveWordsList") List<SensitiveWords> sensitiveWordsList);
    
    /**
     * 根据敏感词名称删除敏感词库
     * 
     * @param name
     * @return
     */
    int deleteByName(@Param("name") String name);
    
    /**
     * 根据敏感词id删除敏感词库
     * 
     * @param id
     * @return
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据敏感词名称获取敏感词信息
     * 
     * @param name
     * @return
     */
    SensitiveWords selectByName(@Param("name") String name);
    
    /**
     * 根据id获取敏感词信息
     * 
     * @param id
     * @return
     */
    SensitiveWords selectById(@Param("id") Long id);
    
    /**
     * 更新敏感词信息
     * 
     * @param id
     * @param Name
     * @param creatorId
     * @return
     */
    int updateById(@Param("id") Long id, @Param("name") String name, @Param("creatorId") Long creatorId);


    /**
     * @param map
     * @return
     */
    int showSensitiveWordsForPageResultCount(Map<String, Object> map);


    /**
     * @param map
     * @return
     */
    List<SensitiveWords> showSensitiveWordsForPageResult(Map<String, Object> map);


    /**
     * @return
     */
    List<String> getAllSensitiveWords();


    /**
     * @param map
     * @return
     */
    List<SensitiveWords> selectSensitiveWordsByMap(Map map);
}
