/**
 * @Title: AdminEnterService.java
 * @Package com.cmcc.flow.service
 * @Description: 企业关键人相应的服务封装类
 * @author: sunyiwei
 * @date: 2015-2-4 下午2:01:41
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.model.Enterprise;

import java.io.Serializable;
import java.util.List;


/**
 * @ClassName: AdminEnterService
 * @Description:企业关键人服务类
 * @author: qihang
 * @date: 2015-3-12 下午16:25:41
 */
public interface AdminEnterService extends Serializable {
    /**
     * @param record
     * @return
     */
    boolean insert(AdminEnter record);

    /**
     * @param adminId
     * @param enterId
     * @return
     */
    boolean insert(Long adminId, Long enterId);


    /**
     * @param adminId
     * @return
     */
    List<String> selectByAdminId(Long adminId);


    /**
     * @param adminId
     * @param enterId
     * @return
     */
    int selectCountByAdminIdEntId(Long adminId, Long enterId);


    /**
     * @param record
     * @return
     */
    int selectCountByQuery(AdminEnter record);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:deleteByAdminId
     * @Description:根据用户ID，删除其对应的所有记录，当删除用户时，如果用户是企业关键人，需要执行这个操作
     */
    boolean deleteByAdminId(Long adminId);

    /**
     * @param enterId
     * @return
     */
    boolean deleteByEnterId(Long enterId);

    /**
     * @param record
     * @return
     */
    boolean deleteAdminRole(AdminEnter record);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:selecteEntIdByAdminId
     * @Description: 根据用户ID获取企业ID
     * @author: sunyiwei
     */
    List<Long> selecteEntIdByAdminId(Long adminId);

    /**
     * @param admingId
     * @return
     * @throws
     * @Title:selecteEntByAdminId
     * @Description: 根据用户ID获取企业对象
     * @author: sunyiwei
     */
    List<Enterprise> selecteEntByAdminId(Long adminId);


}
