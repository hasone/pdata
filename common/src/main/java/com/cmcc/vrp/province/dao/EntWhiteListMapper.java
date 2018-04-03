package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.EntWhiteList;

/**
 * <p>Title:EntWhiteListMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月17日
*/
public interface EntWhiteListMapper {
    
    /**
    * @Title: deleteByPrimaryKey
    * @Description: 根据id删除
    */ 
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: 插入记录
    */ 
    int insert(EntWhiteList record);

    /**
    * @Title: insertSelective
    * @Description: 插入记录
    */ 
    int insertSelective(EntWhiteList record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: 根据id查找
    */ 
    EntWhiteList selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: 更新
    */ 
    int updateByPrimaryKeySelective(EntWhiteList record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: 更新
    */ 
    int updateByPrimaryKey(EntWhiteList record);
    
    /**
    * @Title: selectByEntId
    * @Description: 根据企业id查找
    */ 
    List<EntWhiteList> selectByEntId(Long entId);
    
    /**
    * @Title: deleteByEntId
    * @Description: 根据企业id逻辑删除ip白名单
    */ 
    int deleteByEntId(Long entId);
    
    /**
    * @Title: batchInsert
    * @Description: 批量插入
    */ 
    int batchInsert (@Param("records")List<EntWhiteList> records);

    /**
     * 插入ip
     * @Title: insertIps 
     * @param ips
     * @param entId
     * @return
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    int insertIps(@Param("ips")List<String> ips, @Param("entId")Long entId);
}