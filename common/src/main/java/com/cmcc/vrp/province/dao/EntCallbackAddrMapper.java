package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EntCallbackAddr;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Title:EntCallbackAddrMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
*/
public interface EntCallbackAddrMapper {
    /**
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * @param record
     * @return
     */
    int insert(EntCallbackAddr record);

    /**
     * @param id
     * @return
     */
    EntCallbackAddr get(Long id);

    /**
     * @param entId
     * @return
     */
    EntCallbackAddr getByEntId(Long entId);

    /**
     * @param entId
     * @param newCallbackAddr
     * @return
     */
    int update(@Param("entId") Long entId, @Param("newCallbackAddr") String newCallbackAddr);
    
    /**
     * @param entId
     * @return
     */
    int deleteByEntId(Long entId);
}