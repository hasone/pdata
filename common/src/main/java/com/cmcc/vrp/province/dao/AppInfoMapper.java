package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AppInfo;

import java.util.List;
import java.util.Map;

/**
 * AppInfoMapper.java
 */
public interface AppInfoMapper {
    /** 
     * @Title: selectByEnterpriseId 
     */
    AppInfo selectByEnterpriseId(Long appInfoId);

    /** 
     * @Title: selectByQuery 
     */
    List<AppInfo> selectByQuery(Map<String, Object> param);

    /** 
     * @Title: deleteByEnterpriseId 
     */
    int deleteByEnterpriseId(Long enterpriseId);

    /** 
     * @Title: insert 
     */
    int insert(AppInfo record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(AppInfo record);

    /** 
     * @Title: selectByAppKey 
     */
    AppInfo selectByAppKey(String appkey);

    /** 
     * @Title: selectByAppId 
     */
    AppInfo selectByAppId(int id);

    /** 
     * @Title: selectByEnterpriseCode 
     */
    AppInfo selectByEnterpriseCode(String code);

    /** 
     * @Title: deleteAppByEnterpriseCode 
     */
    int deleteAppByEnterpriseCode(String code);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(AppInfo record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(AppInfo record);

    /** 
     * @Title: updateByAppKey 
     */
    int updateByAppKey(AppInfo record);

    /** 
     * @Title: updateByAppId 
     */
    int updateByAppId(AppInfo record);


    /** 
     * @Title: showAppInfoForPageResult 
     */
    List<AppInfo> showAppInfoForPageResult(Map<String, Object> map);

    /** 
     * @Title: showAppInfoForPageResultCount 
     */
    int showAppInfoForPageResultCount(Map<String, Object> map);


    /** 
     * @Title: showAppInfoForPage 
     */
    List<AppInfo> showAppInfoForPage(AppInfo appInfo);

    /** 
     * @Title: selectAll 
     */
    List<AppInfo> selectAll();

    /** 
     * @Title: QueryAppInfoPages 
     */
    List<?> QueryAppInfoPages(Map<String, Object> map);

    /** 
     * @Title: queryAppInfoCount 
     */
    int queryAppInfoCount(Map<String, Object> map);


    /** 
     * @Title: queryUserAuthsByUserName 
     */
    List<AppInfo> queryUserAuthsByUserName(String userName);

    /**
     * 根据企业id删除企业app
     *
     * @param enterpriseId
     * @return
     */
    int updateAppDeletFlag(long enterpriseId);
}
