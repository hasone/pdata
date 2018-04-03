package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.PrizeInfo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title:AccountMapper </p> <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月8日
 */

public interface AccountMapper {
    /**
     * @Title: deleteByOwnerId
     * @Description: TODO
     */
    int deleteByOwnerId(@Param("ownerId") Long ownerId, @Param("type") Integer type);

    /**
     * @Title: insert
     * @Description: TODO
     */
    int insert(Account record);

    /**
     * @Title: batchInsert
     * @Description: TODO
     */
    int batchInsert(@Param("records") List<Account> records);

    /**
     * @Title: getByOwner
     * @Description: TODO
     */
    List<Account> getByOwner(@Param("ownerId") Long ownerId, @Param("type") Integer type);

    /**
     * @Title: getByOwnerIdAndPrdId
     * @Description: TODO
     */
    Account getByOwnerIdAndPrdId(@Param("ownerId") Long ownerId, @Param("prdId") Long prdId, @Param("type") Integer type);

    /**
     * @Title: updateCount
     * @Description: TODO
     */
    int updateCount(@Param("id") Long id, @Param("delta") Double delta);

    /**
     * @Title: forceUpdateCount
     * @Description: TODO
     */
    int forceUpdateCount(@Param("id") Long id, @Param("delta") Double delta);

    /**
     * @Title: sumActivitiesFrozenAccount
     * @Description: TODO
     */
    Double sumActivitiesFrozenAccount(@Param("entId") Long entId);

    /**
     * @Title: updateMinCount
     * @Description: TODO
     */
    int updateMinCount(@Param("id") Long id, @Param("newMinCount") Double newMinCount, @Param("oldVersion") int oldVersion);

    /**
     * @Title: getById
     * @Description: TODO
     */
    Account getById(@Param("id") Long id);

    /**
     * @Title: selectByEntIdAndProIds
     * @Description: TODO
     */
    List<Account> selectByEntIdAndProIds(@Param("entId") Long entId, @Param("prizeList") List<PrizeInfo> prizes);

    /**
     * @Title: selectCurrencyAccount
     * @Description: TODO
     */
    Account selectCurrencyAccount(@Param("entId") Long entId);

    /**
     * 批量获取企业的现金账户
     *
     * @param entIds 企业ID
     */
    List<Account> selectCurrencyAccounts(@Param("entIds") List<Long> entIds);

    /**
     * @Title: checkAccountByEntIdAndProductId
     * @Description: TODO
     */
    List<Account> checkAccountByEntIdAndProductId(@Param("entId") Long entId, @Param("productId") Long productId);

    /**
     * 修改账户信息(千万不能包含企业余额)
     */
    int updateAlertSelective(Account record);

    /**
     * @Title: cleanAccountByTpye
     * @Description: 根据产品类型，清空所有账户
     * @return: int
     */
    int cleanAccountByTpye(@Param("productType") Integer productType);
    
    /**
     * 根据类型获取企业预付费产品账户
     */
    List<Account> getPaypreAccByType(@Param("entId") Long entId,@Param("prdType") Integer prdType);
    
    /**
     * 
     * @Title: updateAlertCount 
     * @Description: TODO
     * @param id
     * @param newMinCount
     * @param oldVersion
     * @return
     * @return: int
     */
    int updateAlertCount(@Param("id") Long id, @Param("newCount") Double newCount);
    
    /**
     * 
     * @Title: updateStopCount 
     * @Description: TODO
     * @param id
     * @param newMinCount
     * @param oldVersion
     * @return
     * @return: int
     */
    int updateStopCount(@Param("id") Long id, @Param("newCount") Double newCount);
    
    /**
     * 
     * @Title: updateMinCount2 
     * @Description: TODO
     * @param id
     * @param newCount
     * @return
     * @return: int
     */
    int updateMinCount2(@Param("id") Long id, @Param("newCount") Double newCount);
}
