package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcCardmaker;

import java.util.List;
import java.util.Map;


/**
 * @Title:MdrcCardmakerMapper
 * @Description:
 * */
public interface MdrcCardmakerMapper {


    /**
     * @Title:selectAllCardmaker
     * @Description:
     * */
    List<MdrcCardmaker> selectAllCardmaker();


    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * 物理删除
     * */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 逻辑删除
     * @param id
     * @author qinqinyan
     * */
    int deleteCardmaker(Long id);


    /**
     * @Title:insert
     * @Description:
     * */
    int insert(MdrcCardmaker record);


    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(MdrcCardmaker record);


    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * 根据id获取未删除的记录
     * @param id
     * edit by qinqinyan
     * */
    MdrcCardmaker selectByPrimaryKey(Long id);
    
    /**
     * 根据id获取记录，用于显示
     * @param id
     * @author qinqinyan
     * */
    MdrcCardmaker selectByPrimaryKeyForshow(Long id);


    /**
     * @Title:selectWithKeys
     * @Description:
     * */
    MdrcCardmaker selectWithKeys(Map<String, Object> map);


    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(MdrcCardmaker record);


    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(MdrcCardmaker record);


    /**
     * @Title:select
     * @Description:
     * */
    List<MdrcCardmaker> select(Map<String, Object> map);


    /**
     * @Title:count
     * @Description:
     * */
    int count(Map<String, Object> map);


    /**
     * @Title:selectMaxSerialNumber
     * @Description:
     * */
    Integer selectMaxSerialNumber();


    /**
     * @Title:countByName
     * @Description:
     * */
    int countByName(String name);


    /**
     * @Title:selectByOperatorId
     * @Description:
     * */
    MdrcCardmaker selectByOperatorId(Long operatorId);

    //唯一性校验：名称或编号

    /**
     * @Title:checkUnique
     * @Description:
     * */
    List<MdrcCardmaker> checkUnique(MdrcCardmaker m);
}