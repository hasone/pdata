package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.mdrc.model.MdrcFileInfo;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.util.QueryObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * MdrcTemplateService.java
 */
public interface MdrcTemplateService {
    /** 
     * @Title: selectByPrimaryKey 
     */
    MdrcTemplate selectByPrimaryKey(Long id);

    /**
     * 查询符合条件的记录数
     * <p>
     *
     * @param queryObject
     * @return
     */
    int count(QueryObject queryObject);
    
    /**
     * @title 
     * */
    int isExist(QueryObject queryObject);

    /**
     * 查询符合条件的记录
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<MdrcTemplate> list(QueryObject queryObject);

    /**
     * 更新资源文件个数
     * <p>
     *
     * @param id
     * @param resourcesCount
     * @return
     */
    int updateResourcesCount(Long id, Integer resourcesCount);

    /**
     * 新增一条记录
     * <p>
     *
     * @param mdrcTemplate
     * @return
     */
    boolean insert(MdrcTemplate mdrcTemplate);
    
    /**
     * 新增一条记录
     * <p>
     * @param mdrcTemplate
     * @return
     */
    boolean insertSelective(MdrcTemplate mdrcTemplate);

    /**
     * 查询已存在的主题
     * <p>
     *
     * @return
     */
    List<String> listThemes();

    /**
     * 获取资源文件列表
     * <p>
     *
     * @param id
     * @return
     */
    List<File> listFiles(long id);

    /** 
     * @Title: listFilesS3 
     */
    List<MdrcFileInfo> listFilesS3(long id);

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
     * 删除模板文件
     * <p>
     *
     * @param id
     * @param filename
     */
    void deleteFile(long id, String filename);


    /** 
     * @Title: minusResourceCount 
     */
    void minusResourceCount(long id);

    /**
     * 更新正面和反面效果图
     * <p>
     *
     * @param id
     * @param frontImage
     * @param rearImage
     * @return
     */
    int updateFrontAndRearImage(long id, String frontImage, String rearImage);

    /**
     * 压缩打包资源文件
     * <p>
     *
     * @param id
     * @param outputFolder
     */
    void compress(long id, String outputFolder);

    /**
     * 获得可用模板列表，按照主题名称分组
     * <p>
     *
     * @return
     */
    Map<String, List<MdrcTemplate>> themeTemplates();
    
    /**
     * 根据产品大小获得模板主题
     * @author qinqinyan
     * @date 2017/07/31 
     * * */
    Map<String, List<MdrcTemplate>> getThemeTemplatesByProdSize(Long prodSize);

    /**
     * @title:getFile
     * */
    byte[] getFile(long id, String filename) throws IOException;

    /**
     * @param name
     * @return
     * @throws
     * @Title:checkUnique
     * @Description: 查找数据库里面名称是否重复
     * @author: xuwanlin
     */
    Boolean checkUnique(String name);
    
    /**
     * 
     * @Title: listSort 
     * @Description: TODO
     * @param list
     * @return
     * @return: List<File>
     */
    List<File> listSort(List<File> list);
    
    /**
     * 更新
     * */
    boolean updateByIdSeletive(MdrcTemplate mdrcTemplate);

}
