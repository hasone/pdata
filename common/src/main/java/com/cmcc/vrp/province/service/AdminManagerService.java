package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;

import java.util.List;

/**
 * 
 * @ClassName: AdminManagerService 
 * @Description: TODO
 */
public interface AdminManagerService {

    /**
     * 
     * @Title: insertAdminManager 
     * @Description: TODO
     * @param adminManager
     * @return
     * @return: boolean
     */
    boolean insertAdminManager(AdminManager adminManager);

    /**
     * 通过企业ID,找到和企业直接关联的人，利用admin_manager和ent_manager
     *
     * @param entId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    List<Long> getAdminIdForEnter(Long entId);

    /**
     * 根据企业ID找到企业所属企业管理员下挂的企业管理员用户id，即根据企业id找到企业管理员的用户
     *
     * @param entId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    List<Administer> getAdminForEnter(Long entId);

    /**
     * 根据企业ID寻找上一级节点的对应的管理员（当前上一级是客户经理）
     *
     * @author qinqinyan
     */
    List<Administer> getCustomerManagerByEntId(Long entId);

    /**
     * 根据manageId获得Administer
     *
     * @author qinqinyan
     */
    List<Administer> getAdminByManageId(Long manageId);

    /**
     * 根据企业ID，将该企业所属的企业管理员下面的具体管理员用户删除（逻辑删除）
     *
     * @param entId
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    boolean deleteAdminByEntId(Long entId);

    /**
     * 
     * @Title: selectAdminIdByManagerId 
     * @Description: TODO
     * @param managerId
     * @return
     * @return: List<Long>
     */
    List<Long> selectAdminIdByManagerId(Long managerId);

    /**
     * 
     * @Title: selectManagerIdByAdminId 
     * @Description: TODO
     * @param adminId
     * @return
     * @return: Long
     */
    Long selectManagerIdByAdminId(Long adminId);

    /**
     * 编辑用户的管理员身份
     *
     * @return
     * @date 2016年7月28日
     * @author wujiamin
     */
    boolean editUserManager(AdminManager adminManager);

    /**
     * 根据adminId删除用户职位
     *
     * @param adminId
     * @return
     */
    boolean deleteManagerByAdmin(Long adminId);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: 根据主键动态更新
     * @param record
     * @return
     * @return: boolean
     */
    boolean updateByPrimaryKeySelective(AdminManager record);

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
     * @Title: getManagerByAdminId 
     * @Description: TODO
     * @param adminId
     * @return
     * @return: Manager
     */
    Manager getManagerByAdminId(Long adminId);
    
    /**
     * 根据manageId获得Administer
     *
     * @author qihang
     */
    List<Administer> getAdminByManageIds(List<Manager> manageIds);

}
