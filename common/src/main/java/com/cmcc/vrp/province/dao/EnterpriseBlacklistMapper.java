/**
 * 
 */
package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.EnterpriseBlacklist;

/**
 *  @desc:
 *  @author: wuguoping 
 *  @data: 2017年7月13日
 */
public interface EnterpriseBlacklistMapper {
    
    /**
     * 插入企业黑名单
     * title: insert
     * desc: 
     * @param enterpriseBlacklist
     * @return
     * wuguoping
     * 2017年7月13日
     */
    int insert(EnterpriseBlacklist enterpriseBlacklist);
   
    /**
     * 通过企业名称和关键词获取
     * title: selectByEntNameKeyWord
     * desc: 
     * @param entName
     * @param keyName
     * @return
     * wuguoping
     * 2017年7月14日
     */
    List<EnterpriseBlacklist> selectByEntNameKeyWord(@Param("entName") String entName, @Param("keyName") String keyName);
    
    /**
     * 通过ID获取该黑名单
     * title: selectById
     * desc: 
     * @param id
     * @return
     * wuguoping
     * 2017年7月17日
     */
    EnterpriseBlacklist selectById(@Param("id") int id);
    
    /**
     * 企业黑名单列表展示
     * title: showEntBlacklistPagResult
     * desc: 
     * @param map
     * @return
     * wuguoping
     * 2017年7月14日
     */
    List<EnterpriseBlacklist> showEntBlacklistPagResult( Map<String, Object> map );
    
    /**
     * 企业黑名单列表总数-用于分页
     * title: showEntBlacklistPagCount
     * desc: 
     * @param map
     * @return
     * wuguoping
     * 2017年7月14日
     */
    Integer showEntBlacklistPagCount( Map<String, Object> map );
    
    /**
     * 更新企业黑名单
     * title: update
     * desc: 
     * @param id
     * @param entName
     * @param keyName
     * @param creatorId
     * @return
     * wuguoping
     * 2017年7月17日
     */
    int updateById(@Param("id") Long id, @Param("entName") String entName, @Param("keyName") String keyName, @Param("creatorId") Long creatorId);
    
    /**
     * 
     * title: deleteById
     * desc: 
     * @param id
     * @return
     * wuguoping
     * 2017年7月17日
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据企业名称模糊搜索
     * title: selectByEntName
     * desc: 
     * @param entName
     * @return
     * wuguoping
     * 2017年7月19日
     */
    List<EnterpriseBlacklist> selectByEntName(@Param("entName") String entName);
    
    /**
     * 获取所有的企业黑名单
     * title: getAllKeywords
     * desc: 
     * @return
     * wuguoping
     * 2017年7月19日
     */
    List<EnterpriseBlacklist> getAllEntBlacklist();
    
    /**
     * 
     * title: getAllKeywordsList
     * desc: 
     * @return
     * wuguoping
     * 2017年7月20日
     */
    Set<String> getAllKeywordsList();
    
    /**
     * 通过关键词列表获取企业名称列表
     * title: selectByKeywordList
     * desc: 
     * @param keywordList
     * @return
     * wuguoping
     * 2017年7月20日
     */
    List<String> selectByKeywordList(Map<String, Object> map);
}
