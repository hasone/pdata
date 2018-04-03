/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EnterpriseBlacklistMapper;
import com.cmcc.vrp.province.model.EnterpriseBlacklist;
import com.cmcc.vrp.province.service.EnterpriseBlacklistService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SensitiveWordFilterUtil;
import com.cmcc.vrp.util.StringUtils;

/**
 *  @desc:
 *  @author: wuguoping 
 *  @data: 2017年7月13日
 */
@Service
public class EnterpriseBlacklistServiceImpl implements EnterpriseBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseBlacklistServiceImpl.class);
    private static final int MIN_MATCH_TYPE = 1; 
    
    @Autowired
    EnterpriseBlacklistMapper enterpriseBlacklistMapper;

    @Override
    public boolean isContainKeyword(String entName , String keyWord){
        List<EnterpriseBlacklist> enterpriseBlacklist  =  enterpriseBlacklistMapper.selectByEntNameKeyWord(entName, keyWord);
        if(enterpriseBlacklist == null
                || enterpriseBlacklist.size() == 0){
            return false;
        }
        return true;
    }
    
    @Override
    public EnterpriseBlacklist selectEntBlacklistById(Integer id){
        if(id == null || id < 0){
            logger.error("无效的请求参数 id ");
            return null;
        }
        return enterpriseBlacklistMapper.selectById(id);
    }

    @Override
    public boolean insert(EnterpriseBlacklist enterpriseBlacklist) {
        if(enterpriseBlacklist == null){
            logger.error("无效的请求参数 enterpriseBlacklist = { NULL}");
        }
        return enterpriseBlacklistMapper.insert(enterpriseBlacklist) >0;
    }

    @Override
    public List<EnterpriseBlacklist> getEntBlacklistForPage(QueryObject queryObject) {
        if(queryObject == null){
            logger.error("无效的请求参数  queryObject = NULL");
            return null;
        }
        Map<String, Object> map = queryObject.toMap();
        return enterpriseBlacklistMapper.showEntBlacklistPagResult(map);
    }

    @Override
    public Integer getEntBlacklistCount(QueryObject queryObject) {
        if(queryObject == null){
            logger.error("无效的请求参数  queryObject = NULL");
            return null;
        }
        Map<String, Object> map = queryObject.toMap();
        return enterpriseBlacklistMapper.showEntBlacklistPagCount(map);
    }

    @Override
    public boolean update(Long id, String entName, String keyName, Long creatorId) {
        if(StringUtils.isEmpty(entName) 
                || StringUtils.isEmpty(keyName)
                || StringUtils.isEmpty(String.valueOf(id))
                || StringUtils.isEmpty(String.valueOf(creatorId))){
            logger.error("无效的请求参数 entName/keyword/id = { NULL or ''}");
        }
        return enterpriseBlacklistMapper.updateById(id, entName, keyName, creatorId) > 0;
    }

    @Override
    public boolean delete(Long id) {
        if(StringUtils.isEmpty(String.valueOf(id))){
            logger.error("无效的请求参数 id = { NULL or ''}");
        }
        
        return enterpriseBlacklistMapper.deleteById(id) > 0;
    }

    @Override
    public List<String> getBlacklistEntNameList(String entName) {
        if(StringUtils.isEmpty(entName)){
            logger.error("无效的请求参数 entName = { NULL or ''}");
            return null;
        }
        
        Set<String>  keywordsList = enterpriseBlacklistMapper.getAllKeywordsList();
        if(keywordsList == null
                || keywordsList.size() == 0){
            logger.info("还未建立黑名单。");
            return null;
        }
        
        Map keywordsMap = SensitiveWordFilterUtil.initKeyWordMap(keywordsList);
        String newEntName = SensitiveWordFilterUtil.stringFilter(entName);
        Set<String> blacklistKeywords = SensitiveWordFilterUtil.getSensitiveWord(newEntName, keywordsMap, MIN_MATCH_TYPE);
        
        if(blacklistKeywords == null 
                || blacklistKeywords.size() ==0 ){
            logger.info(entName + "  不含有关键词。");
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keywordSet", blacklistKeywords);
        
        return enterpriseBlacklistMapper.selectByKeywordList(map);
    }

    @Override
    public Set<String> indistinctCheckByEntName(String entName) {
        if(StringUtils.isEmpty(entName)){
            logger.error("无效的请求参数 entName = { NULL or ''}");
            return null;
        }
        
        List<EnterpriseBlacklist>  enterpriseBlacklists = enterpriseBlacklistMapper.selectByEntName(entName);
        if(enterpriseBlacklists == null
                || enterpriseBlacklists.size() == 0){
            logger.info("该企业不在黑名单中。");
            return null;
        }
        Set<String> entNames = new HashSet<String>();
        for(int i = 0; i < enterpriseBlacklists.size(); i++){
            if(!entNames.contains(enterpriseBlacklists.get(i).getEnterpriseName())){
                entNames.add(enterpriseBlacklists.get(i).getEnterpriseName());
            }
        }
        return entNames;
    }
    
}
