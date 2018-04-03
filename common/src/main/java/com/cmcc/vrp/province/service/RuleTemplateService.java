package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.RuleTemplate;
import com.cmcc.vrp.province.model.json.RedPacketPage;
import com.cmcc.vrp.util.QueryObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * RuleTemplateService.java
 */
public interface RuleTemplateService {

    /** 
     * @Title: deleteByPrimaryKey 
    */
    boolean deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
    */
    boolean insert(RuleTemplate record);

    /** 
     * @Title: insertSelective 
    */
    boolean insertSelective(RuleTemplate record);

    /** 
     * @Title: selectByPrimaryKey 
    */
    RuleTemplate selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
    */
    boolean updateByPrimaryKeySelective(RuleTemplate record);

    /** 
     * @Title: updateByPrimaryKey 
    */
    boolean updateByPrimaryKey(RuleTemplate record);

    /**
     * 查询记录条数
     * <p>
     *
     * @param queryObject
     * @return
     */
    Long count(QueryObject queryObject);

    /**
     * 查询记录列表
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<RuleTemplate> listRuleTemplate(QueryObject queryObject);

    /**
     * 校验模板名称是否在该活动中重名
     */
    String addValidate(RuleTemplate ruleTemplate, QueryObject queryObject);

    /**
     * 获取资源文件列表
     * <p>
     *
     * @param id
     * @return
     */
    List<File> listFiles(long id);

    /**
     * 保存文件
     * <p>
     *
     * @param id
     * @param filename
     * @param data
     * @throws IOException
     */
    int writeFile(long id, String filename, byte[] data) throws IOException;

    /** 
     * @Title: updateResourcesCount 
    */
    int updateResourcesCount(Long id, Integer imageCnt);

    byte[] getFile(long id, String filename) throws IOException;

    /** 
     * @Title: updateFrontAndRearImage 
    */
    int updateFrontAndRearImage(long id, String image);

    /** 
     * @Title: deleteFile 
    */
    void deleteFile(long id, String filename);

    List<RuleTemplate> getRuleTemplateByCreator(Long creatorId);

    List<RuleTemplate> getTemplateToCreateRedpacket(Long creatorId);

    /**
     * 从红包生成页面中获取规则模板对象
     *
     * @param pageParams
     * @return
     */
    RuleTemplate getRedPacketRuleTempalteFromPage(RedPacketPage pageParams);
}
