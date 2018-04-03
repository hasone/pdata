package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SensitiveWords;
import com.cmcc.vrp.util.QueryObject;

/**
* <p>Title: 敏感词</p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月20日 上午10:44:00
*/
public interface SensitiveWordsService {
	/**
	 * 新增敏感词库
	 * 
	 * @param sensitiveWords
	 * @return
	 */
    public boolean insert(SensitiveWords sensitiveWords);
    
    /**
     *批量插入 
     * 
     * @param sensitiveWordsList
     * @return
     */
    public boolean batchInsert(List<SensitiveWords> sensitiveWordsList);
	
	/**
	 * 根据敏感词名称删除敏感词库
	 * 
	 * @param name
	 * @return
	 */
    public boolean deleteByName(String name);
	
	/**
	 * 根据敏感词id删除敏感词库
	 * 
	 * @param id
	 * @return
	 */
    public boolean deleteById(Long id);
	
	/**
	 * 根据敏感词名称获取敏感词信息
	 * 
	 * @param name
	 * @return
	 */
    public SensitiveWords selectByName(String name);
	
	/**
	 * 根据id获取敏感词信息
	 * 
	 * @param id
	 * @return
	 */
    public SensitiveWords selectById(Long id);
	
	/**
	 * 更新敏感词信息
	 * 
	 * @param id
	 * @param Name
	 * @param creatorId
	 * @return
	 */
    public boolean updateById(Long id, String name, Long creatorId);

    /**
     * @param queryObject
     * @return
     */
    public int showForPageResultCount(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     */
    public List<SensitiveWords> showSensitiveWordsForPageResult(QueryObject queryObject);
    
    /**
     * @return
     */
    public List<String> getAllSensitiveWords();

    /**
     * @param map
     * @return
     */
    public List<SensitiveWords> selectSensitiveWordsByMap(Map map);
}
