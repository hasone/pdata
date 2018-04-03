package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.module.UserStatisticModule;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: AdministerService 
 * @Description: TODO
 */

public interface AdministerService {
    /**
     * 
     * @Title: selectAdministerById 
     * @Description: TODO
     * @param id
     * @return
     * @return: Administer
     */
    Administer selectAdministerById(Long id);

    /**
     * 
     * @Title: selectByUserName 
     * @Description: TODO
     * @param userName
     * @return
     * @return: List<Administer>
     */
    List<Administer> selectByUserName(String userName);

    /**
     * 
     * @Title: insertAdminister 
     * @Description: TODO
     * @param administer
     * @param roleId
     * @param enterpriseId
     * @return
     * @return: boolean
     */
    boolean insertAdminister(Administer administer, Long roleId,
                             Long enterpriseId);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param administer
     * @return
     * @return: boolean
     */
    boolean insertSelective(Administer administer);

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param mobile
     * @return
     * @return: boolean
     */
    boolean insert(String mobile);//手机号首次登陆时创建用户

    /**
     * 
     * @Title: updateSelective 
     * @Description: TODO
     * @param administer
     * @return
     * @return: boolean
     */
    boolean updateSelective(Administer administer);

    /**
     * 
     * @Title: updateAdminister 
     * @Description: TODO
     * @param administer
     * @param newRoleId
     * @param enterpriseId
     * @return
     * @return: boolean
     */
    boolean updateAdminister(Administer administer, Long newRoleId,
                             Long enterpriseId);

    /**
     * 
     * @Title: updateAdministerPassword 
     * @Description: TODO
     * @param userId
     * @param szNewPassword
     * @return
     * @return: boolean
     */
    boolean updateAdministerPassword(Long userId, String szNewPassword);

    /**
     * 
     * @Title: deleteById 
     * @Description: TODO
     * @param id
     * @return
     * @return: boolean
     */
    boolean deleteById(Long id);

    /**
     * 
     * @Title: selectByMobilePhone 
     * @Description: TODO
     * @param phone
     * @return
     * @return: Administer
     */
    Administer selectByMobilePhone(String phone);

    /**
     * 
     * @Title: queryPaginationAdminCount 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: int
     */
    int queryPaginationAdminCount(QueryObject queryObject);

    /**
     * 
     * @Title: queryPaginationAdminList 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<Administer>
     */
    List<Administer> queryPaginationAdminList(QueryObject queryObject);

    /**
     * @return
     * @throws
     * @Title:selectAllAdministers
     * @Description: 选择当前数据库存储的所有用户信息
     * @author: sunyiwei
     */
    List<Administer> selectAllAdministers();

    /**
     * @param userName
     * @return
     * @throws
     * @Title:queryUserAuthories
     * @Description: 根据用户手机获取相应的权限
     * @author: sunyiwei
     */
    List<Authority> queryUserAuthoriesByMobile(String mobilePhone);

    /**
     * @return
     * @throws
     * @Title: uniqueCheck
     * @Description: 校验新建或编辑的姓名称或手机号码是否已存在
     */
    boolean checkUnique(Administer administer);

    /**
     * 
     * @Title: checkNameUnique 
     * @Description: TODO
     * @param id
     * @param name
     * @return
     * @return: boolean
     */
    boolean checkNameUnique(Long id, String name);

    /**
     * 检查密码
     */
    Boolean checkPassword(String password, String mobilePhone);

    /**
     * 
     * @Title: queryCMByDistrictId 
     * @Description: TODO
     * @param districtId
     * @param roleId
     * @return
     * @return: List<Administer>
     */
    List<Administer> queryCMByDistrictId(Long districtId, Long roleId);

    /**
     * 
     * @Title: selectEMByEnterpriseId 
     * @Description: TODO
     * @param enterId
     * @return
     * @return: List<Administer>
     */
    List<Administer> selectEMByEnterpriseId(Long enterId);

    /**
     * 创建用户，同时为用户设置管理员身份
     *
     * @param managerId
     * @param administer
     * @param administerDB 数据库中的Administer对象
     * @return
     * @date 2016年7月19日
     * @author wujiamin
     */
    boolean createAdminister(Long managerId, Administer administer, Administer administerDB, Long currentUserId);

    /**
     * 通过管理员ID统计管理员树上所有角色的数量
     *
     * @param managerId
     * @return
     */
    int statisticRoleCountByManangerId(Long managerId);

    /**
     * 通过管理员ID统计管理员树上每类角色对应信息
     *
     * @param managerId
     * @return
     */
    List<UserStatisticModule> statisticRoleByManagerId(Long managerId, QueryObject queryObject);


    /**
     * 通过管理员ID统计管理员树上某一角色的数量
     *
     * @param roleId
     * @param managerId
     * @return
     */
    int statisticOneRoleCountByManangerId(Long roleId, Long managerId);


    /**
     * 通过管理员ID统计管理员树上某一类角色对应信息
     *
     * @param roleId
     * @param managerId
     * @param queryObject
     * @return
     */
    List<UserStatisticModule> statisticOneRoleByManagerId(Long roleId, Long managerId, QueryObject queryObject);

    /**
     * 四川集中化平台的用户创建
     *
     * @param mobile
     * @return
     * @Title: insertForScJizhong
     * @Author: wujiamin
     * @date 2016年9月23日上午9:03:46
     */
    boolean insertForScJizhong(String mobile);
    
    /**
     * 校验用户密码有效期
     * @Title: checkPasswordUpdateTime 
     * @param mobile
     * @return
     * @Author: wujiamin
     * @date 2016年10月14日下午3:21:18
     */
    boolean checkPasswordUpdateTime(String mobile);

    /**
    * @Title: queryAllUsersByAuthName
    * @Description: 根据权限名查找所有用户
    */ 
    List<Administer> queryAllUsersByAuthName(String authName);
    
    /**
     * 
     * @Title: createAdminister 
     * @Description: 新增用户
     * @param administer
     * @return
     * @return: boolean
     */
    boolean createAdminister(Administer administer);
    
    /**
     * 
     * @Title: updateAdminister 
     * @Description: 更新用户
     * @param administer
     * @return
     * @return: boolean
     */
    boolean updateAdminister(Administer administer);

    /**
     * 根据manageIds获取用户
     * @param manageIds
     * @return
     * @author qinqinyan
     * */
    List<Administer> getByManageIds(List<Long> manageIds);
    
    /** 
     * 微信插入用户，同时构建账户
     * @Title: insertForWx 
     */
    boolean insertForWx(String mobile, String openid);
    
    /**
     * v1.10.1功能
     * 更新手机号
     * 这个功能现在仅限于广东众筹平台
     * @author qinqinyan
     * */
    boolean updateAdminster(String oldMobile, String newMobile);

    /** 
     * 重置密码信息验证
     * @Title: verifyResetPsd 
     */
    boolean verifyResetPsd(Integer type, String entName, String customerManagerMobile, String mobile, String userName);
    
    /**
     * 
     * @Title: isOverAuth 
     * @Description: 校验是否横向越权：adminId所在节点是currentUserId所在节点的子节点时，不越权，返回false;否则返回true
     * @param adminId
     * @param currentUserId
     * @return
     * @return: boolean
     */
    boolean isOverAuth(Long adminId, Long currentUserId);

    /**
     * 
     * @Title: blurAdministerInfo 
     * @Description: 用户信息模糊处理
     * @param administer
     * @return: void
     */
    void blurAdministerInfo(Administer administer);
}
