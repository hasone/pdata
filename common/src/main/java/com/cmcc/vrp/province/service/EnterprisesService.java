package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.module.EnterpriseBenefitModule;
import com.cmcc.vrp.province.module.EnterpriseStatisticModule;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>Title:EnterprisesService </p> <p>Description: </p>
 *
 * @author xujue
 * @date 2016年10月24日
 */
public interface EnterprisesService {

    /**
     * @Title: insertSelective
     * @Description: TODO
     */
    boolean insertSelective(Enterprise record);

    /**
     * 根据主键查找状态正常的企业
     *
     * @author qinqinyan
     */
    Enterprise selectByPrimaryKey(Long id);

    /**
     * 根据主键查找企业（不考虑企业状态）
     *
     * @author qinqinyan
     */
    Enterprise selectByPrimaryKeyForActivity(Long id);

    /**
     * @Title: selectByCode
     * @Description: TODO
     * @return: Enterprise
     */
    Enterprise selectByCode(String code);

    /**
     * @Title: updateByPrimaryKeySelective
     * @Description: TODO
     * @return: boolean
     */
    boolean updateByPrimaryKeySelective(Enterprise record);

    /**
     * @author
     */
    List<Enterprise> showEnterprisesForPageResult(QueryObject queryObject);

    /**
     * @author
     */
    List<Enterprise> getEnterpriseList();

    /**
     * @author
     */
    int showForPageResultCount(QueryObject queryObject);

    /**
     * @Title:getEnterpriseListByAdminId
     * @Description: 根据企业管理员筛选出企业
     * @author: sunyiwei
     */
    List<Enterprise> getEnterpriseListByAdminId(Administer admin);

    /**
     * @Title:getEnterpriseIdByAdminId
     * @Description: 根据企业管理员筛选出企业Id
     * @author: sunyiwei
     */
    List<Long> getEnterpriseIdByAdminId(Administer admin);

    /**
     * 根据企业管理员筛选出正常状态的企业
     *
     * @date 2016年7月6日
     * @author wujiamin
     */
    List<Enterprise> getNormalEnterpriseListByAdminId(Administer admin);

    /**
     * 获取与admin相关的所有企业
     */
    List<Enterprise> getAllEnterpriseListByAdminId(Administer admin);

    /**
     * @Title:deleteByPrimaryKey
     * @Description: 根据企业的Id删除相关企业，只有在该企业由该登陆人创建，且无企业关键人绑定时才可以删除。删除成功返回null，错误返回相应信息 。
     * @author: qihang
     */
    // String deleteByPrimaryKey(Long enterId , Long adminId);

    Enterprise queryEnterpriseByCode(String code);

    /**
     * 根据字段值，查询是否有已存在的记录 <p>
     *
     * @param exceptId 可选。当参数值不为NULL时，查询会排除此标识的记录；当参数值为NULL时，不做为查询条件。
     * @param name     三个字段至少有一个。
     * @param code     三个字段至少有一个。
     * @param phone    三个字段至少有一个。
     */
    boolean countExists(Long exceptId, String name, String code, String phone);

    /**
     * 根据各种条件搜索企业，不分页
     */
    List<Enterprise> selectEnterpriseByMap(Map map);

    /**
     * @Title: statistictEnterpriseByCreateDay
     * @Description: TODO
     * @return: List<EnterpriseStatisticModule>
     */
    List<EnterpriseStatisticModule> statistictEnterpriseByCreateDay(
            Long managerId, QueryObject queryObject);

    /**
     * @Title: statistictBenefitForEnterprise
     * @Description: TODO
     * @return: List<EnterpriseBenefitModule>
     */
    List<EnterpriseBenefitModule> statistictBenefitForEnterprise(
            QueryObject queryObject);

    /**
     * @param appKey
     * @return
     */
    Enterprise selectByAppKey(String appKey);

    /**
     * 构建企业合同到期的定时任务
     */
    boolean createEnterpriseExpireSchedule(Enterprise enterprise);

    /**
     * @Title: changeEnterManager
     * @Description: TODO
     * @return: boolean
     */
    boolean changeEnterManager(Administer newAdmin, Long enterId,
                               Long currentUserId);

    /**
     * 认证企业保存信息
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    boolean saveQualification(Long currentUserId, Enterprise enterprise,
                              MultipartHttpServletRequest request);

    /**
     * @Title: saveEditQualification
     * @Description: TODO
     * @return: boolean
     */
    boolean saveEditQualification(Long currentUserId, Enterprise enterprise,
                                  MultipartHttpServletRequest request);

    /**
     * @Title: saveEditEntInfo
     * @Description: TODO
     * @return: boolean
     */
    boolean saveEditEntInfo(Enterprise enterprise, Long currentUserId,
                            MultipartHttpServletRequest request);

    /**
     * 企业保存认证合作信息
     *
     * @date 2016年7月28日
     * @author xujue
     */
    boolean saveQualificationCooperation(Long currentUserId,
                                         Enterprise enterprise, MultipartHttpServletRequest request);

    /**
     * 创建企业营业执照到期回退至体验企业定时任务
     *
     * @date 2016年6月1日
     * @author JamieWu
     */
    boolean createEnterpriseLicenceExpireSchedule(Enterprise enterprise);

    /**
     * 保存企业合作信息
     *
     * @date 2016年6月1日
     * @author wujiamin
     */
    boolean saveCooperation(Enterprise enterprise, Long currentUserId,
                            MultipartHttpServletRequest request);

    /**
     * @param enterprise
     * @param currentUserId
     * @param request
     * @return
     */
    boolean saveEditCooperation(Enterprise enterprise, Long currentUserId,
                                MultipartHttpServletRequest request);

    /**
     * 潜在客户查询
     */
    List<Enterprise> queryPotentialEnterList(QueryObject queryObject,
                                             Long currentManagerId);

    /**
     * @param currentManagerId
     * @return
     */
    Long countPotentialEnterList(QueryObject queryObject, Long currentManagerId);

    /**
     * 找到某节点下的所有企业
     *
     * @date 2016年7月20日
     * @author wujiamin
     */
    List<Enterprise> getEnterByManagerId(Long managerId);
    
    /**
     * 找到某节点下的所有企业
     *
     * @date 2016年7月20日
     * @author wujiamin
     */
    List<Enterprise> getEnterByManagerIdEnterName(Long managerId,String entName);

    /**
     * 删选出所有正常状态下的企业delete_flag=0 and status=3
     *
     * @date 2016年7月21日
     * @author wujiamin
     */
    List<Enterprise> getNormalEnterByManagerId(Long managerId);

    List<Enterprise> getAllEnterByManagerId(Long managerId);

    /**
     * 通过管理员ID检索它下挂的企业ID
     */
    List<Long> getEnterIdByManagerId(Long managerId);

    /**
     * @Title: statistictEnterpriseByManagerTree
     * @Description: TODO
     * @return: List<EnterpriseStatisticModule>
     */
    List<EnterpriseStatisticModule> statistictEnterpriseByManagerTree(
            QueryObject queryObject);

    /**
     * @Title: statistictEnterpriseCountByManagerTree
     * @Description: TODO
     * @return: int
     */
    int statistictEnterpriseCountByManagerTree(QueryObject queryObject);

    /**
     * 企业信息填些权限控制
     *
     * @author qinqinyan
     */
    Boolean hasAuthToFillInQuafilication(Long roleId);

    /**
     * @Title: hasAuthToFillInCooperation
     * @Description: TODO
     * @return: Boolean
     */
    Boolean hasAuthToFillInCooperation(Long roleId);

    /**
     * @Title: hasAuthToFillInForProvince
     * @Description: TODO
     * @return: Boolean
     */
    Boolean hasAuthToFillInForProvince(Long roleId);

    /**
     * @Title: getNormalEnterpriseList
     * @Description: TODO
     * @return: List<Enterprise>
     */
    List<Enterprise> getNormalEnterpriseList();

    /**
     * @Title: hasAuthToEdit
     * @Description: TODO
     * @return: Boolean
     */
    Boolean hasAuthToEdit(Long roleId);

    /**
     * 根据managerId获取潜在客户企业
     *
     * @author qinqinyan
     */
    List<Enterprise> getPotentialEnterByManagerId(Long managerId);

    /**
     * 判断企业状态
     *
     * @return success, 企业状态正常
     * @author qinqinyan
     */
    String judgeEnterprise(Long entId);

    /**
     * 取消企业合同到期定时任务
     *
     * @return boolean
     * @author xujue
     */
    boolean undoEnterpriseExireSchedule(Enterprise enter);

    /**
     * 取消企业营业执照到期定时任务
     *
     * @return boolean
     * @author xujue
     */
    boolean undoEnterpriseLicenceExpireSchedule(Enterprise enter);

    /**
     * 根据id查找Enterprise
     *
     * @author qinqinyan
     */
    Enterprise selectById(Long entId);

    /**
     * 根据企业名称查询企业列表， 支持模糊查询
     *
     * @param entName 企业名称
     * @return 符合相应企业名称的企业列表
     */
    List<Enterprise> queryByEntName(String entName);

    /**
     * 更新appkey及appsecret
     *
     * @Title: changeAppkey
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    boolean changeAppkey(Long id);

    /**
     * 创建appkey及appsecret
     *
     * @Title: changeAppkey
     */
    boolean createAppkey(Long id);

    /**
     * @Title: createNewEnterprise
     * @Description: 创建新的企业
     * @return: boolean
     */
    boolean createNewEnterprise(Enterprise enterprise);

    /**
     * @Title: createOrUpdateEnterprise
     * @Description: 新增或更新企业信息：企业不存在则新增；存在则更新
     * @return: boolean
     */
    boolean createOrUpdateEnterprise(Enterprise enterprise);

    /**
     * 根据节点查找正常状态企业
     *
     * @author qinqinyan
     */
    List<Enterprise> getNomalEnterByManagerId(Long managerId);

    /**
     *
     */
    List<Enterprise> showEnterprisesAccountsForPageResult(QueryObject queryObject);

    /**
     *
     */
    int showEnterprisesAccountsCount(QueryObject queryObject);

    /**
     * 批量获取企业的余额信息
     *
     * @param ids 企业ID
     */
    Map<Long, Double> queryAccounts(List<Long> ids);

    /**当前用户的职位是否是企业的上级 
     * @Title: isParentManage 
     */
    boolean isParentManage(Long entId, Long currentManageId);
    
    /**
     * 
     * @Title: selectByPhone 
     * @Description: TODO
     * @param phone
     * @return
     * @return: Enterprise
     */
    Enterprise selectByPhone(String phone);
    
    /**
     * 
     * @Title: blurEnterpriseInfo 
     * @Description: 企业信息模糊处理
     * @param enterprise
     * @return: void
     */
    void blurEnterpriseInfo(Enterprise enterprise);
}
