package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:AdminManagerMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
*/
public interface AdminManagerMapper {
    /**
    * @Title: deleteByPrimaryKey
    * @Description: TODO
    */
    int deleteByPrimaryKey(Long id);

    /**
    * @Title: insert
    * @Description: TODO
    */
    int insert(AdminManager record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */
    int insertSelective(AdminManager record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */
    AdminManager selectByPrimaryKey(Long id);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */
    int updateByPrimaryKeySelective(AdminManager record);

    /**
    * @Title: updateByPrimaryKey
    * @Description: TODO
    */
    int updateByPrimaryKey(AdminManager record);

    /**
    * @Title: deleteAdminByEntId
    * @Description: TODO
    */
    int deleteAdminByEntId(Long entId);

    /**
    * @Title: selectAdminIdByManagerId
    * @Description: TODO
    */
    List<Long> selectAdminIdByManagerId(Long managerId);

    /**
     * 根据企业id查找企业管理员节点下挂着的企业管理员用户
     *
     * @param entId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    List<Long> getAdminIdForEnter(Long entId);

    /**
    * @Title: getAdminForEnter
    * @Description: TODO
    */
    List<Administer> getAdminForEnter(Long entId);

    /**
    * @Title: selectManagerIdByAdminId
    * @Description: TODO
    */
    Long selectManagerIdByAdminId(Long adminId);

    /**
    * @Title: getAdminByManageId
    * @Description: TODO
    */
    List<Administer> getAdminByManageId(@Param("managerId") Long manageId);

    /**
    * @Title: deleteByAdminId
    * @Description: TODO
    */
    int deleteByAdminId(Long adminId);

    /**
     * 
     * @Title: selectByManagerId 
     * @Description: TODO
     * @param managerId
     * @return
     * @return: List
     */
    AdminManager selectByManagerId(Long managerId);

    /**
     * 
     * @Title: selectByAdminId 
     * @Description: TODO
     * @param adminId
     * @return
     * @return: AdminManager
     */
    AdminManager selectByAdminId(Long adminId);
    
    /**
     * 
     * @Title: selectByAdminId 
     * @Description: TODO
     * @param adminId
     * @return
     * @return: AdminManager
     */
    List<Manager> getManagerForAdminId(Long adminId);
    
    List<Administer> getAdminByManageIds(Map<String, Object> map);
}
