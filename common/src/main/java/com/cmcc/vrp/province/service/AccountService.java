package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;

/**
 * Created by sunyiwei on 2015/11/13.
 */
public interface AccountService {
    /**
     * 创建企业帐户信息
     *
     * @param entId     企业ID
     * @param infos     帐户详情信息，key为产品ID, value为相应的余额
     * @param serialNum 操作流水号
     * @return
     */
    boolean createEnterAccount(Long entId, Map<Long, Double> infos, String serialNum);

    /**
     * 创建活动帐户，会从企业帐户里扣减相应的余额，并创建相应的企业活动帐户
     *
     * @param activityId  活动ID
     * @param accountType 活动类型
     * @param entId       企业ID
     * @param infos       活动的帐户详情，key为产品ID， value为相应的余额
     * @return 创建活动帐户成功返回true， 否则false
     */
    boolean createActivityAccount(Long activityId, AccountType accountType, Long entId, Map<Long, Double> infos, String serialNum);

    /**
     * 增加指定的帐户余额
     *
     * @param ownerId     帐户使用者的ID
     * @param prdId       产品ID
     * @param accountType 活动类型
     * @param delta       增加产品的个数，注意这里是个数
     * @param serialNum   操作流水号
     * @param desc        描述信息
     * @return 成功返回true, 否则false
     */
    boolean addCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum, String desc);

    /**
     * 扣减指定的帐户余额
     *
     * @param ownerId     帐户使用者的ID
     * @param prdId       产品ID
     * @param accountType 活动类型
     * @param delta       减少产品的个数，注意这是是个数，不是总价
     * @param serialNum   操作流水号
     * @param desc        描述信息
     * @return 成功返回OK
     */
    MinusCountReturnType minusCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum, String desc,boolean isCheckSolde);
    
    /**
     * 扣减指定的帐户余额
     *
     * @param ownerId     帐户使用者的ID
     * @param prdId       产品ID
     * @param accountType 活动类型
     * @param delta       减少产品的个数，注意这是是个数，不是总价
     * @param serialNum   操作流水号
     * @param desc        描述信息
     * @return 成功返回true
     */
    boolean minusCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum, String desc);

    /**
     * 查询企业指定帐户的余额， 这里有可能需要从BOSS同步，取决于相应业务的需要
     *
     * @param ownerId     帐户使用者的ID
     * @param prdId       产品ID
     * @param accountType 活动类型
     * @return 返回相应的企业帐户信息
     */
    //Account get(Long ownerId, Long prdId, AccountType accountType);
    Account get(Long ownerId, Long prdId, Integer accountType);

    /**
     * 根据企业ID获取相应的帐户信息，注意，这里只获取到企业使用的那些帐户， 不包括由企业创建但由活动使用的那些帐户信息
     *
     * @param entId 企业ID
     * @return 企业相应的帐户信息
     */
    List<Account> getByEntId(Long entId);

    /**
     * 根据企业ID和产品ID找到相应的企业的账户
     *
     * @param entId
     * @param prizes
     * @return
     */
    List<Account> getByEntIdAndProId(Long entId, List<PrizeInfo> prizes);

    /**
     * 获取企业当前被冻结在活动中的资金总额
     *
     * @param entId 企业ID
     * @return 活动中被冻结的资金总额
     */
    Double sumEntActivitiesFrozenCount(Long entId);

    /**
     * 设置信用额度
     *
     * @param entId
     * @param minCount
     * @return
     * @date 2016年6月29日
     * @author wujiamin
     */
    boolean setMinCount(Long entId, Double minCount);

    /**
     * 充值失败回退账户
     *
     * @param serialNum    平台流水号
     * @param activityType 活动类型
     * @param dstPrdId     充值的平台产品ID
     * @return
     * @date 2016年6月30日
     * @author wujiamin
     */
    boolean returnFunds(String serialNum, ActivityType activityType, Long dstPrdId, Integer count);

    /**
     * 充值失败回退账户, 调用这个方法的时候， 默认从chargeRecord中获取活动类型及目标产品参数
     *
     * @param serialNum 平台流水号
     * @return
     * @date 2016年6月30日
     * @author sunyiwei
     */
    boolean returnFunds(String serialNum);

    /**
     * 检查账户是否负债
     *
     * @param maps
     * @param enterId
     * @return
     */
    boolean isDebt2Account(List<AccountPrizeMap> maps, Long enterId);
    
    /**
     * 
     * @Title: isEnoughInAccount 
     * @Description: TODO
     * @param prdId
     * @param entId
     * @return
     * @return: boolean
     */
    boolean isEnoughInAccount(List<Product> prds, Long entId);


    /**
     * 获取企业的资金账户
     *
     * @param enterId
     * @return
     */
    Account getCurrencyAccount(Long enterId);


    /**
     * 批量获取企业的现金账户
     * @param entIds 企业ID
     * @return
     */
    List<Account> getCurrencyAccounts(List<Long> entIds);

    /**
     * 账户余额是否充足（校验单个产品在企业账户，流量包，流量池，现金）
     *
     * @param productId
     * @param entId
     * @author qinqinyan
     */
    boolean isEnoughInAccount(Long productId, Long entId);

    /***
     * 校验现金账户
     *
     * @param entId
     * @param productId
     * @return
     * @author qinqinyan
     **/
    boolean verifyCashAccount(Long productId, Long entId);

    /**
     * 检查产品账户是否存在（产品变更时调用）
     */
    boolean checkAccountByEntIdAndProductId(Long entId, Long productId);

    /**
     * 创建产品账户（产品变更时调用）
     */
    boolean createProductAccount(List<Account> records);

    /**
     * 从BOSS侧同步账户余额，并强制更新到平台账户中
     *
     * @param entId 企业ID
     * @param prdId 产品ID
     * @return 同步成功返回true, 否则false
     */
    SyncAccountResult syncFromBoss(Long entId, Long prdId);

    /**
     * 更新企业账户
     *
     * @param id
     * @param delta
     * @return
     */
    boolean updateCount(Long id, Double delta);


    /**
     * 设置预警值、暂停值
     *
     * @param account
     * @return
     */
    boolean updateAlertSelective(Account account);

    /**
     * 恢复预警缓存
     *
     * @param ownerId 企业ID
     */
    void recoverAlert(Long ownerId, Long prdId);


    /**
     * @param entId
     * @param totalPrice
     * @return
     */
    String checkAlertStopValue(Long entId, Double totalPrice);


    boolean isEnough2Debt(Account currency, Double debt);

    /**
     * 
     * @Title: cleanAccountByTpye 
     * @Description: 根据产品类型，清空账户
     * @param productType
     * @return
     * @return: boolean
     */
    boolean cleanAccountByTpye(ProductType productType);
    
    /**
     * 是否为空账户
     * true:是;false:否
     * */
    boolean isEmptyAccount();

    /**
     * 校验随机红包余额
     * @param entId
     * @param totalProductSize
     * @author qinqinyan
     * */
    boolean verifyAccountForRendomPacket(Long entId, Long totalProductSize, Long productId);
    
    /**
     * 得到某企业的预付费账户
     */
    Account getPaypreAccByType(Long entId,ProductType prdType);
    
    /**
     * 
     * @Title: updateAlertCount 
     * @Description: TODO
     * @param id
     * @param newCount
     * @return
     * @return: boolean
     */
    boolean updateAlertCount(Long id, Double newCount);
    
    /**
     * 
     * @Title: updateStopCount 
     * @Description: TODO
     * @param id
     * @param newCount
     * @return
     * @return: boolean
     */
    boolean updateStopCount(Long id, Double newCount);

    /**
     * 
     * @Title: updateMinCount 
     * @Description: TODO
     * @param id
     * @param newCount
     * @return
     * @return: boolean
     */
    boolean updateMinCount(Long id, Double newCount);
}
