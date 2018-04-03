package com.cmcc.vrp.province.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.module.Membership;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月22日 下午2:23:05
 */
public interface IndividualAccountMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(IndividualAccount record);

    /**
     * @param record
     * @return
     */
    int insertSelective(IndividualAccount record);

    /**
     * @param id
     * @return
     */
    IndividualAccount selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(IndividualAccount record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(IndividualAccount record);

    /**
     * @param records
     * @return
     */
    int batchInsert(@Param("records") List<IndividualAccount> records);

    IndividualAccount getAccountByOwnerIdAndProductId(@Param("ownerId") Long ownerId, @Param("productId") Long productId, @Param("type") Integer type);

    /**
     * @param count 变更值
     */
    int addCount(@Param("id") Long id, @Param("count") BigDecimal count);

    /**
     * @param count 变更值
     */
    int minusCount(@Param("id") Long id, @Param("count") BigDecimal count);

    /** 
     * @Title: getExpireAccount 
     */
    List<IndividualAccount> getExpireAccount(Date date);
    
    /** 
     * @Title: getMembershipList 
     */
    List<Membership> getMembershipList(Map map);

    /** 
     * @Title: countMembershipList 
     */
    Integer countMembershipList(Map map);

    /** 
     * @Title: selectByTypeAndOwnerId 
     */
    List<IndividualAccount> selectByTypeAndOwnerId(@Param("type")Integer type, @Param("ownerId")Long ownerId);

}