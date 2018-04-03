package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.module.UserStatisticModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * AdministerMapper.java
 * @author wujiamin
 * @date 2016年11月15日
 */
public interface AdministerMapper {
    /** 
     * @Title: deleteByPrimaryKey 
    */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
    */
    int insert(Administer record);

    /** 
     * @Title: insertSelective 
    */
    int insertSelective(Administer record);

    /** 
     * @Title: selectByPrimaryKey 
    */
    Administer selectByPrimaryKey(Long id);

    /**
     * 根据手机号码查询用户 用户用手机登录 或者修改密码时需要用到
     *
     * @param phone
     * @return
     */
    Administer selectByMobilePhone(String phone);

    /**
     * @param userName
     * @return
     * @throws
     * @Title:selectByUserName
     * @Description: 由姓名来查找用户对象
     * @author: sunyiwei
     */
    List<Administer> selectByUserName(String userName);

    /** 
     * @Title: updateByPrimaryKeySelective 
    */
    int updateByPrimaryKeySelective(Administer record);

    /** 
     * @Title: updateByPrimaryKey 
    */
    int updateByPrimaryKey(Administer record);


    /** 
     * @Title: updatePasswordByKey 
    */
    int updatePasswordByKey(@Param("id") Long id, @Param("passwordNew") String passwordNew, @Param("salt") String salt);

    /** 
     * @Title: selectAllAdministers 
    */
    List<Administer> selectAllAdministers();

    /**
     * @描述：管理员分页查询（得到总记录数量 ），按管理员名称模糊查询
     */
    int queryPaginationAdminCount(Map<String, Object> map);

    /**
     * @描述：管理员分页查询（得到分页数据 ），按管理员名称模糊查询
     */
    List<Administer> queryPaginationAdminList(Map<String, Object> map);


    /**
     * @param mobilePhone
     * @return
     * @throws
     * @Title:queryUserAuthoriesByMobile
     * @Description: 通过手机号查找该用户拥有的权限
     * @author: sunyiwei
     */
    List<Authority> queryUserAuthoriesByMobile(String mobilePhone);

    /**
     * 查询出未设定为制卡专员的用户
     * <p>
     *
     * @return
     */
    List<Administer> selectUnboundCardmaker(String authCode);

    /** 
     * @Title: selectCustomerByDistrictId 
    */
    List<Administer> selectCustomerByDistrictId(@Param("districtId") Long districtId, @Param("roleId") Long roleId);

    /** 
     * @Title: selectEMByEnterpriseId 
    */
    List<Administer> selectEMByEnterpriseId(@Param("enterId") Long enterId);

    /** 
     * @Title: updateUserNameBymobile 
    */
    int updateUserNameBymobile(@Param("userName") String userName, @Param("mobile") String mobile);

    /** 
     * @Title: selectRoleByManagerId 
    */
    List<UserStatisticModule> selectRoleByManagerId(Map map);

    /** 
     * @Title: selectRoleCountByManagerId 
    */
    int selectRoleCountByManagerId(Map map);

    /** 
     * @Title: selectOneRoleCountByManangerId 
    */
    int selectOneRoleCountByManangerId(Map map);

    /** 
     * @Title: selectOneRoleByManagerId 
    */
    List<UserStatisticModule> selectOneRoleByManagerId(Map map);
    
    /**
    * @Title: queryAllUsersByAuthName
    * @Description: 根据权限名查找所有用户
    */ 
    List<Administer> queryAllUsersByAuthName(String authName);

    /**
     * @Title:getByMap
     * @Description:根据manageIds查找
     * */
    List<Administer> getByMap(Map map);
}