package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcTemplate;

import java.util.List;
import java.util.Map;


/**
 * @Title:MdrcTemplateMapper
 * @Description:
 * */
public interface MdrcTemplateMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(MdrcTemplate record);
    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(MdrcTemplate record);
    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    MdrcTemplate selectByPrimaryKey(Long id);
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(MdrcTemplate record);
    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(MdrcTemplate record);
    /**
     * @Title:updateUrlByPrimaryKeySelective
     * @Description:
     * */
    int updateUrlByPrimaryKeySelective(MdrcTemplate record);

    /**
     * @return
     * @throws
     * @Title:selectAll
     * @Description: TODO
     * @author: zhoujianfeng
     */

    List<MdrcTemplate> selectAll();

    /**
     * @param params
     * @return
     * @throws
     * @Title:getMdrcTemplateList
     * @author: zhoujianfeng
     */
    List<MdrcTemplate> list(Map<String, Object> params);

    /**
     * @return
     * @throws
     * @Title:getMdrcTemplateCount
     * @author: zhoujianfeng
     */
    int count(Map<String, Object> queryCriteria);
    /**
     * @Title:isExist
     * @Description:
     * */
    int isExist(Map<String, Object> queryCriteria);


    /**
     * @return
     * @throws
     * @Title:selectAllMdrcTemplates
     * @Description: TODO
     * @author: zhoujianfeng
     */
    List<MdrcTemplate> selectAllMdrcTemplates();
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    List<MdrcTemplate> selectAllMdrcTemplatesByEnterId(Long enterId);

    /**
     * @return
     * @throws
     * @Title:selectByMdrcTemplateName
     * @Description: TODO
     * @author: zhoujianfeng
     */
    MdrcTemplate selectByMdrcTemplateName(String mdrcTemplateName);

    /**
     * @return
     * @throws
     * @Title:selectByMdrcTemplateCode
     * @Description: TODO
     * @author: zhoujianfeng
     */
    MdrcTemplate selectByMdrcTemplateCode(String mdrcTemplateCode);

    /**
     * @Title:selectThemes
     * @Description:
     * */
    List<String> selectThemes();

    /**
     * 查询所有未删除的模板，按照主题排序
     * <p>
     *
     * @return
     */
    List<MdrcTemplate> selectAllTemplateByTheme();

    /**
     * 根据卡序列号查询模板
     * <p>
     *
     * @return
     */
    MdrcTemplate selectByCardnumber(String cardNumber);
    
    /**
     * 根据map查找
     * */
    List<MdrcTemplate> selectByMap(Map map);

}