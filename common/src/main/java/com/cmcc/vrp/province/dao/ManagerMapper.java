package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Manager;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Title:ManagerMapper
 * @Description:
 */
public interface ManagerMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     */
    int insert(Manager record);

    /**
     * @Title:insertSelective
     * @Description:
     */
    int insertSelective(Manager record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     */
    Manager selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     */
    int updateByPrimaryKeySelective(Manager record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     */
    int updateByPrimaryKey(Manager record);

    /**
     * @Title:selectByAdminId
     * @Description:
     */
    Manager selectByAdminId(Long adminId);

    /**
     * @Title:selectByParentIdForPage
     * @Description:
     */
    List<Manager> selectByParentIdForPage(Map<String, Object> map);

    /**
     * @Title:selectByParentIdCount
     * @Description:
     */
    int selectByParentIdCount(Map<String, Object> map);

    /**
     * @Title:selectByParentId
     * @Description:
     */
    List<Manager> selectByParentId(Long parentId);

    //获取rootIdList节点的直接子节点
    List<Long> getDirectChild(@Param("rootIdList") List<Long> rootIdList);

    //根据managerIds获取相应的管理员对象列表
    List<Manager> getManagers(@Param("managerIds") List<Long> managerIds);

    /**
     * @Title: selectManagerByadminId
     */
    Manager selectManagerByadminId(@Param("adminId") Long adminId);

    /**
     * @Title: selectHigherFatherNodeByParentId
     */
    Manager selectHigherFatherNodeByParentId(@Param("parentId") Long parentId);

    /**
     * @Title:selectSonIdsByParentId
     * @Description:
     */
    List<Long> selectSonIdsByParentId(@Param("parentId") Long manageId);

    /**
     * @Title:getByRoleIdAndName
     * @Description:
     */
    Manager getByRoleIdAndName(@Param("roleId") Long roleId, @Param("name") String name);

    /**
     * @Title:getByPhone
     * @Description:
     */
    Manager getByPhone(@Param("phone") String phone);

    /**
     * @Title: selectEntParentNodeByEnterIdOrRoleId
     * @Description: TODO
     * @return: List<Manager>
     */
    List<Manager> selectEntParentNodeByEnterIdOrRoleId(@Param("enterId") Long enterId, @Param("roleId") Long roleId);

    /**
     * @param entManageId
     * @param roleId
     * @return
     */
    Long selectManagerIdByEntIdAndRoleId(@Param("entManageId") Long entManageId, @Param("roleId") Long roleId);
    
    /**
     * getByRoleId
     * @param roleId
     * @return
     */
    List<Long> getByRoleId(Long roleId);
}