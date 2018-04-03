package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
*/
public interface ManagerService {
    /**
     * @param adminId
     * @return
     */
    Manager selectByAdminId(Long adminId);

    /**
     * @param manager
     * @param currentManagerId
     * @return
     */
    boolean createManager(Manager manager, Long currentManagerId);

    /**
     * @param parentId
     * @param queryObject
     * @return
     */
    int selectByParentIdCount(Long parentId, QueryObject queryObject);

    /**
     * @param parentId
     * @param queryObject
     * @return
     */
    List<Manager> selectByParentIdForPage(Long parentId, QueryObject queryObject);

    /**
     * @title: getSonTreeByManangId
     * */
    List<Manager> getSonTreeByManangId(Long manageId);

    /**
     * @param parentId
     * @return
     */
    List<Manager> selectByParentId(Long parentId);

    /**
     * @title: getChildNodeString
     * */
    String getChildNodeString(Long rootId);

    /**
     * @param id
     * @return
     */
    Manager selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(Manager record);

    /**
     * @title: getManagerByAdminId
     * */
    Manager getManagerByAdminId(Long adminId);

    /**
     * @title: getSonTreeIdByManageId
     * */
    List<Long> getSonTreeIdByManageId(Long manageId);

    /**
     * @param parentId
     * @return
     */
    Manager selectHigherFatherNodeByParentId(Long parentId);

    /**
     * @param manageId
     * @return
     */
    List<Long> selectSonIdsByParentId(Long manageId);

    /**
     * 根据当前用户manageId查找该用户所能管理的所有用户
     * @param manageId
     * @return
     * @author qinqinyan
     * */
    List<Administer> getChildAdminByCurrentManageId(Long manageId);

    /**
     * 根据管理员节点Id获得该管理员树除超级管理员外到该节点的全称
     *
     * @param fullname
     * @param managerId
     * @return
     * @date 2016年7月27日
     * @author wujiamin
     */

    String getFullNameByCurrentManagerId(String fullname, Long managerId);

    /**
     * 根据角色ID和名称获取相应的管理员节点信息
     *
     * @param roleId 角色ID
     * @param name   名称
     * @return 管理员节点信息
     */
    Manager get(Long roleId, String name);
    
    /**
     * 根据管理员账号，获取职位节点
     * @param phone
     * @return
     */
    Manager getByPhone(String phone);
    
    /**
     * 
     * @Title: selectEntParentNodeByEnterIdOrRoleId 
     * @Description: 根据企业ID和角色ID查找企业父节点
     * @param enterId
     * @param roleId
     * @return
     * @return: List<Manager>
     */
    List<Manager> selectEntParentNodeByEnterIdOrRoleId(Long enterId, Long roleId);
    /**
     * 查找企业id和角色id查找企业某一节点
     * @param entId
     * @param roleId
     * @return
     */
    Long selectManagerIdByEntIdAndRoleId(Long entManagerId, Long roleId);
    
    /**
     * 更据角色id查找职位id
     * @param roleId
     * @author qinqinyan
     * */
    List<Long> getByRoleId(Long roleId);

    /** 
     * @Title: getChildNode 
     */
    List<Long> getChildNode(Long rootId);
    
    /**
     * 
     * */
    boolean isParentManage(Long sonManageId, Long currentManageId);
    
    /**
     * 判断是企业是否是这个manageId的管辖范围内
     * */
    boolean managedByManageId(Long entId, Long manageId);
    
    /**
     * 
     * @Title: isProOrCityOrMangerOrEnt 
     * @Description: 是否是省级管理员、市级管理员、客户经理、企业关键人
     * @param roleId
     * @return
     * @return: boolean
     */
    boolean isProOrCityOrMangerOrEnt(Long roleId);
    
    /**
     * 自底向上得到父树及本身
     */
    List<Manager> getParentTreeByManangId(Long manageId);
    
    /**
     * 自底向上得到父树中某角色的Manager
     */
    Manager getParentNodeByRoleId(Long manageId,Long roleId);
}
