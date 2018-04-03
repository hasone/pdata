package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.module.EnterpriseBenefitModule;
import com.cmcc.vrp.province.module.EnterpriseStatisticModule;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:14:42
*/
public interface EnterpriseMapper {

    List<Enterprise> getEnterpriseListByProSize(String proSize);

    /**
     * 根据ID删除记录
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 添加数据
     *
     * @param record
     * @return
     */
    int insertSelective(Enterprise record);

    /**
     * 根据ID查询数据
     *
     * @param id
     * @return 状态正常的企业
     */
    Enterprise selectByPrimaryKey(Long id);

    /**
     * 根据ID查询数据
     *
     * @param id
     * @return 返回企业（不考虑企业状态）
     */
    Enterprise selectByPrimaryKeyForActivity(Long id);

    /**
     * 根据企业编码查询数据
     *
     * @param id
     * @return
     */
    List<Enterprise> selectEnterprisesByCode(String code);

    /**
     * 根据企业名称查询数据
     *
     * @param id
     * @return
     */
    List<Enterprise> selectEnterprisesByName(String name);

    /**
     * 根据条件更新记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Enterprise record);

    /**
     * 根据条件和分页信息查询
     *
     * @param map
     * @return
     */
    List<Enterprise> showEnterprisesForPageResult(Map<String, Object> map);

    /**
     * 根据条件和分页信息查询未使用产品模板的记录
     * @author qinqinyan
     * @param map
     * @return
     */
    List<Enterprise> showEnterprisesButNotUsedProdTemplate(Map<String, Object> map);

    /**
     * 根据条件和分页信息查询记录数
     *
     * @param map
     * @return
     */
    int showEnterprisesForPageResultCount(Map<String, Object> map);

    /**
     * 根据条件和分页信息查询不使用产品模板记录数
     *
     * @param map
     * @author qinqinyan
     * @return
     */
    int countEnterprisesButNotUsedProdTemplate(Map<String, Object> map);


    int getCodeCount(String code);


    /**
     * 根据条件查询
     *
     * @param map
     * @return
     */
    List<Enterprise> queryAllEnterpriseList();

    /**
     * @param enterName
     * @return
     */
    Long selectByEnterName(String enterName);

    /**
     * @param code
     * @return
     */
    Enterprise queryEnterpriseByCode(String code);

    /**
     * 查询符合条件的记录条数
     * <p>
     *
     * @param exceptId 可选。当参数值不为NULL时，查询会排除此标识的记录；当参数值为NULL时，不做为查询条件。
     * @param name     三个字段至少有一个。
     * @param code     三个字段至少有一个。
     * @param phone    三个字段至少有一个。
     * @return
     */
    int countExists(@Param("exceptId") Long exceptId, @Param("name") String name,
                    @Param("code") String code, @Param("phone") String phone);

    /**
     * 根据各种条件查询企业，不分页
     */
    public List<Enterprise> selectEnterpriseByMap(Map map);

    /**
     * 根据条件统计
     *
     * @param map
     * @return
     */
    public List<EnterpriseStatisticModule> statistictEnterpriseByDistrict(Map map);


    /**
     * @param map
     * @return
     */
    public List<EnterpriseStatisticModule> statistictEnterpriseByDistrictProvince(Map map);


    /**
     * @param map
     * @return
     */
    int statistictEnterpriseByDistrictCount(Map map);

    /**
     * @param map
     * @return
     */
    public List<EnterpriseStatisticModule> statistictEnterpriseByCreateDay(Map map);


    /**
     * 统计各企业的效益值
     *
     * @return
     */
    List<EnterpriseBenefitModule> statistictBenefitForEnterprise(Map map);

    /**
     * @param id
     * @param key
     * @param secret
     * @param creatorId
     * @return
     */
    int updateKeyAndSecretAndCreatorId(@Param("id") Long id,
                                       @Param("key") String key,
                                       @Param("secret") String secret,
                                       @Param("creatorId") Long creatorId);

    /**
     * @param appKey
     * @return
     */
    Enterprise selectByAppKey(String appKey);

    /**
     * @param map
     * @return
     */
    int queryPotentialForPageResultCount(Map<String, Object> map);

    /**
     * 潜在客户显示查询
     */
    List<Enterprise> queryPotentialEnt(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    long countPotentialEnt(Map<String, Object> map);

    /**
     * 审核驳回时，清空企业认证信息
     */
    boolean deleteEnterpriseCode(Long entId);

    /**
     * @param entId
     * @return
     */
    boolean deleteEnterpriseVerifyInfo(Long entId);

    /**
     * 找到节点的所有企业
     *
     * @param managerIds
     * @return
     * @date 2016年7月20日
     * @author wujiamin
     */
    List<Enterprise> getEnterByManagerId(@Param("managerIds") String managerIds);
    
    /**
     * 
     * @param managerIds
     * @param entName
     * @return
     */
    List<Enterprise> getEnterByManagerIdEnterName(@Param("managerIds") String managerIds,@Param("entName")String entName);

    /**
     * 找到节点的状态正常企业
     *
     * @param managerIds
     * @return
     * @author qinqinyan
     */
    List<Enterprise> getNomalEnterByManagerId(@Param("managerIds") String managerIds);

    /**
     * 删选出所有正常状态下的企业delete_flag=0 and status=3
     *
     * @param managerId
     * @return
     * @date 2016年7月21日
     * @author wujiamin
     */
    List<Enterprise> getNormalEnterByManagerId(@Param("managerIds") String managerIds);

    /**
     * 删选出所有状态下的企业
     *
     * @param managerId
     * @return
     * @author qinqinyan
     */
    List<Enterprise> getAllEnterByManagerId(@Param("managerIds") String managerIds);

    /**
     * 审核相关
     * 获取潜在客户企业列表
     */
    List<Enterprise> getPotentialEntList(Map<String, Object> map);


    /**
     * @param map
     * @return
     */
    int selectEnterpriseCountByManagerTree(Map map);

    /**
     * @param map
     * @return
     */
    List<EnterpriseStatisticModule> selectEnterpriseByManagerTree(Map map);

    List<Enterprise> getNormalEnter();

    List<Enterprise> getPotentialEnterByManagerId(@Param("managerIds") String managerIds);

    /**
     * 列出所有企业
     *
     * @return
     * @Title: list
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月22日下午1:55:51
     */
    List<Enterprise> list();


    /**
     * 列出所有企业非常状态企业
     *
     * @return
     * @Title: getEnterprisesByStatus
     * @Description: TODO
     * @Author: qinqinyan
     */
    List<Enterprise> getEnterprisesByStatus();

    /**
     * @param id
     * @return
     */
    Enterprise selectById(Long id);

    /**
     * 根据企业名称查询企业列表信息
     *
     * @param entName 企业名称
     * @return 企业列表信息
     */
    List<Enterprise> queryByEntName(String entName);
    
    /**
     * showEnterprisesAccountsForPageResult
     * @param map
     */
    List<Enterprise> showEnterprisesAccountsForPageResult(Map<String, Object> map);
    
    /**
     * showEnterprisesAccountsCount
     * @param map
     */
    int showEnterprisesAccountsCount(Map<String, Object> map);
    
    /**
     * 查找关联产品模板的企业
     * selectRelatedEnterprises
     * @param map
     * @author qinqinyan
     */
    List<Enterprise> selectRelatedEnterprises(Map map);
    
    /**
     * 查找关联产品模板的企业
     * countRelatedEnterprises
     * @param map
     * @author qinqinyan
     */
    int countRelatedEnterprises(Map map);

    /**
     * @param map
     * @return
     */
    List<Enterprise> showGdEnterprisesForPageResult(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    int showGdEnterprisesForPageResultCount(Map<String, Object> map);
    
    /**
     * 
     * @Title: selectByPhone 
     * @Description: TODO
     * @param phone
     * @return
     * @return: Enterprise
     */
    Enterprise selectByPhone(String phone);

}
