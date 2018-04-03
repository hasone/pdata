package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntSyncList;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月30日 上午8:48:15
*/
public interface EntSyncListService {
	
	/**
	 * 插入记录
	 * @param entSyncList
	 */
    public boolean insert(EntSyncList entSyncList);
	
	/**
	 * 根据企业产品编码获取产品同步列表
	 * @param entProCode  集团产品号码
	 * @return
	 */
    public List<EntSyncList> getByEntProCode(String entProCode);
	
	/**
	 * 根据企业编码获取产品同步列表
	 * @param entId 企业id
	 * @return
	 */

    List<EntSyncList> getByEntId(Long entId);
	
	/**
	 * 根据企业编码和集团产品编码获取获取产品同步列表
	 * @param entId 企业id
	 * @return
	 */
    EntSyncList getByEntIdAndEntProCode(Long entId, String entProCode);

    /**
     * 更新同步信息
     * 
     * @param entSyncList
     * @return
     */
    int updateSelective(EntSyncList entSyncList);
    
    EntSyncList getById(Long id);	
}
