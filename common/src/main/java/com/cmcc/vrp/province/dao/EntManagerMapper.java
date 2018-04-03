package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Manager;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:11:49
*/
public interface EntManagerMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(EntManager record);

    /**
     * @param record
     * @return
     */
    int insertSelective(EntManager record);

    /**
     * @param id
     * @return
     */
    EntManager selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(EntManager record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(EntManager record);

    /** 
     * @Title: getManagerIdForEnter 
     */
    Long getManagerIdForEnter(Long enterId);

    /** 
     * @Title: getManagerForEnter 
     */
    Manager getManagerForEnter(Long enterId);
    
    /** 
     * @Title: getManagerForEnterCode 
     */
    Manager getManagerForEnterCode(String enterCode);

    /** 
     * @Title: selectByManagerId 
     */
    List<EntManager> selectByManagerId(Long managerId);
}