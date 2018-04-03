package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SensitiveWordsMapper;
import com.cmcc.vrp.province.model.SensitiveWords;
import com.cmcc.vrp.province.service.SensitiveWordsService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月20日 上午10:59:33
*/
@Service
public class SensitiveWordsServiceImpl implements SensitiveWordsService{
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordsServiceImpl.class);
    
    @Autowired
    SensitiveWordsMapper sensitiveWordsMapper;

    @Override
	public boolean insert(SensitiveWords sensitiveWords) {
        if (sensitiveWords == null
                || StringUtils.isEmpty(sensitiveWords.getName())
                || sensitiveWords.getCreatorId() == null) {
            LOGGER.error("无效的请求参数sensitiveWords={}", sensitiveWords);
            return false;
        }
        
        if (selectByName(sensitiveWords.getName()) != null) {
            LOGGER.error("该敏感词已存在sensitiveWords={}", sensitiveWords);
            return false;
        }
        
        return sensitiveWordsMapper.insert(sensitiveWords) > 0;
    }
    
    @Override
    public boolean batchInsert(List<SensitiveWords> sensitiveWordsList) {
        
        if (sensitiveWordsList == null
                || sensitiveWordsList.isEmpty()) {
            return false;
        }
        
        return sensitiveWordsMapper.batchInsert(sensitiveWordsList) > 0;
        
    }

    @Override
	public boolean deleteByName(String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.error("无效的请求参数name={}", name);
            return false;
        }
        return sensitiveWordsMapper.deleteByName(name) > 0;
    }

    @Override
	public boolean deleteById(Long id) {
        return sensitiveWordsMapper.deleteById(id) > 0;
    }

    @Override
	public SensitiveWords selectByName(String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.error("无效的请求参数name={}", name);
            return null;
        }
        
        return sensitiveWordsMapper.selectByName(name);
    }

    @Override
	public SensitiveWords selectById(Long id) {
        return sensitiveWordsMapper.selectById(id);
    }

    @Override
	public boolean updateById(Long id, String name, Long creatorId) {
        if (id == null
                || StringUtils.isEmpty(name)
                || creatorId == null) {
            LOGGER.error("无效的请求参数id={}, name={}, creatorId={}", id, name, creatorId);
            return false;
        }
        return sensitiveWordsMapper.updateById(id, name, creatorId) > 0;
    }
    
    @Override
    public int showForPageResultCount(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        Map<String, Object> map = queryObject.toMap();
        return sensitiveWordsMapper.showSensitiveWordsForPageResultCount(map);
        
    }
    @Override
    public List<SensitiveWords> showSensitiveWordsForPageResult(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        Map<String, Object> map = queryObject.toMap();
        return sensitiveWordsMapper.showSensitiveWordsForPageResult(map);
        
    }
    @Override
    public List<String> getAllSensitiveWords() {
        return sensitiveWordsMapper.getAllSensitiveWords();
    }

    @Override
    public List<SensitiveWords> selectSensitiveWordsByMap(Map map) {
        return sensitiveWordsMapper.selectSensitiveWordsByMap(map);
    }

}
