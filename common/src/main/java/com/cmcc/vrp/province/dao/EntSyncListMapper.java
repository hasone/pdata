package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.EntSyncList;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月30日 上午8:33:25
*/
public interface EntSyncListMapper {
	
	/**
	 * 创建同步信息
	 * 
	 * @param entSyncList
	 * @return
	 */
    int insert(EntSyncList entSyncList);
	
    List<EntSyncList> getByEntProCode(@Param("entProCode") String entProCode);
	
    List<EntSyncList> getByEntId(@Param("entId") Long entId);

    EntSyncList getByEntIdAndEntProCode(@Param("entId") Long entId, @Param("entProCode") String entProCode);

	/**
	 * 更新同步信息
	 * 
	 * @param id
	 * @param status
	 * @param syncInfo
	 * @return
	 */
    int updateSelective(@Param("id") Long id, @Param("status") Integer status, @Param("syncInfo") String syncInfo);

    EntSyncList getById(@Param("id") Long id);
}
