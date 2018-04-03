/**
 * 
 */
package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Set;

import com.cmcc.vrp.province.model.EnterpriseBlacklist;
import com.cmcc.vrp.util.QueryObject;

/**
 *  @desc: 企业黑名单服务
 *  @author: wuguoping 
 *  @data: 2017年7月13日
 */
public interface EnterpriseBlacklistService {
    /**
     * 判断该企业名称下是否含有相同的关键词
     * title: isContainKeyword
     * desc: 
     * @param entName
     * @param keyword
     * @return
     * wuguoping
     * 2017年7月14日
     */
    public boolean isContainKeyword(String entName , String keyword);
    
    /**
     * 
     * title: selectEntBlacklistById
     * desc: 
     * @param id
     * @return
     * wuguoping
     * 2017年7月17日
     */
    public EnterpriseBlacklist selectEntBlacklistById(Integer id);
    
    /**
     * 插入企业黑名单
     * title: insert
     * desc: 
     * @param enterpriseBlacklist
     * @return
     * wuguoping
     * 2017年7月14日
     */
    public boolean insert(EnterpriseBlacklist enterpriseBlacklist);
    
    /**
     * 获取企业黑名单列表
     * title: getList
     * desc: 
     * @param queryObject
     * @return
     * wuguoping
     * 2017年7月14日
     */
    public List<EnterpriseBlacklist> getEntBlacklistForPage(QueryObject queryObject);
    
    /**
     * 获取企业黑名单列表数量
     * title: getEntBlacklistCount
     * desc: 
     * @param queryObject
     * @return
     * wuguoping
     * 2017年7月14日
     */
    public Integer getEntBlacklistCount(QueryObject queryObject);
    
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
    public boolean update(Long id, String entName, String keyName, Long creatorId);
    
    /**
     * 删除
     * title: delete
     * desc: 
     * @param id
     * @return
     * wuguoping
     * 2017年7月17日
     */
    public boolean delete(Long id);
    
    /**
     * 根据企业名称获取相似企业黑名单名称（企业开户时）
     * title: selectByEntname
     * desc: 
     * @param entName
     * @return
     * wuguoping
     * 2017年7月18日
     */
    public List<String> getBlacklistEntNameList(String entName);
    
    /**
     * 根据企业名称迷糊核查获取企业黑名单
     * title: indistinctCheckByEntName
     * desc: 
     * @param entName
     * @return
     * wuguoping
     * 2017年7月19日
     */
    public Set<String> indistinctCheckByEntName(String entName);
    
}
