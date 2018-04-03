/**
 * 
 */
package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntWhiteList;

/**
 * <p>Title:EntWhiteListService </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月17日
 */
public interface EntWhiteListService {
    
    boolean isIpInEntWhiteList(String ip, Long entId);
   
    /** 
     * @Title: selectByEntId 
     * @param entId
     * @return
     * @Author: wujiamin
     * @date 2016年10月18日
    */
    List<EntWhiteList> selectByEntId(Long entId);
    
    /**
    * @Title: deleteByEntId
    * @Description: 根据企业id逻辑删除ip白名单
    */ 
    boolean deleteByEntId(Long entId);
    
    /**
    * @Title: batchInsert
    * @Description: 批量插入
    */ 
    boolean batchInsert (List<EntWhiteList> entWhiteLists);
    

    /**
     * 插入IP
     * @Title: insertIps 
     * @return
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    boolean insertIps (List<String> ips, Long entId);

}
