package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.MdrcCardInfo;

/**
 * @Title:MdrcCardInfoMapper
 * @Description:
 * */
public interface MdrcCardInfoMapper {

    /**
     * 批量插入记录
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(@Param("year") int year, @Param("records") List<MdrcCardInfo> records);

    /**
     * @param map
     * @return
     * @Title: statistics
     * @Description: 统计各个状态卡数量
     * @return: Map<String,Long>
     */
    Map<String, Long> statistics(Map<String, Object> map);

    /**
     * @Title:statisticsOpStatus
     * @Description:
     * */
    Map<String, Long> statisticsOpStatus(Map<String, Object> map);

    /**
     * @param param
     * @return
     * @Title: queryMdrcCount
     * @Description: 根据卡状态或卡号统计总数
     * @return: Long
     */
    Long queryMdrcCount(Map<String, Object> param);

    /**
     * @param param
     * @return
     * @Title: queryMdrcList
     * @Description: 根据卡状态或卡号查询列表
     * @return: List<MdrcCardInfo>
     */
    List<MdrcCardInfo> queryMdrcList(Map<String, Object> param);

    /**
     * 获取为激活列表
     */
    List<MdrcCardInfo> queryNotActiveList(Map<String, Object> param);

    /**
     * @param year
     * @param cardnumber
     * @return
     * @Title: selectByCardNumber
     * @Description: 根据卡号查询
     * @return: MdrcCardInfo
     */
    MdrcCardInfo selectByCardNumber(@Param("year") int year, @Param("cardnumber") String cardnumber);

    /**
     * @param year
     * @param id
     * @return
     * @Title: selectByPrimaryKey
     * @Description: 根据主键ID查询
     * @return: MdrcCardInfo
     */
    MdrcCardInfo selectByPrimaryKey(@Param("year") int year, @Param("id") long id);

    /**
     * @param year
     * @param configId
     * @return
     * @Title: selectDetailByConfigId
     * @Description: 根据规则ID查询，查询详细信息，包括产品信息
     * @return: List<MdrcCardInfo>
     */
    List<MdrcCardInfo> selectByConfigId(@Param("year") int year, @Param("configId") Long configId);

    /**
     * @param year
     * @param cardNums
     * @param newStatus
     * @param oldStatus
     * @return
     * @Title: batchUpdateStatus
     * @Description: 批量更新状态
     * @return: int
     */
    int batchUpdateStatus(@Param("year") int year, @Param("cardNums") List<String> cardNums,
            @Param("newStatus") int newStatus, @Param("oldStatus") int oldStatus);

    /**
     * @param year
     * @param cardNums
     * @return
     * @Title: batchExpire
     * @Description: 批量过期
     * @return: int
     */
    int batchExpire(@Param("year") int year, @Param("cardNums") List<String> cardNums);

    /**
     * 单张过期
     * @Title:expire
     * @Description:
     * */
    int expire(@Param("year") int year, @Param("cardNum") String cardNum);

    /**
     * 更新单张卡状态
     * 单张过期
     * @Title:updateStatus
     * @Description:
     * */
    int updateStatus(@Param("year") int year, @Param("cardNum") String cardNum, @Param("newStatus") int newStatus,
            @Param("oldStatus") List<Integer> oldStatus);

    /**
     * @param year
     * @param pwd
     * @return
     * @Title: selectByPwd
     * @Description: 由卡密查卡数据
     * @return: MdrcCardInfo
     */
    MdrcCardInfo selectByPwd(@Param("year") int year, @Param("pwd") String pwd);

    /**
     * @param map
     * @return
     * @Title: batchUpdatePassword
     * @Description: 批量更新卡密码
     * @return: int
     */
    int batchUpdatePassword(Map<String, Object> map);

    /**
     * 更新卡的运营状态
     * @Title:updateOpStatus
     * @Description:
     * */
    int updateOpStatus(@Param("year") int year, @Param("cardNum") String cardNum, @Param("newOpStatus") int newOpStatus);

    /**
     * 批量更新卡的运营状态
     * @Title:batchUpdateOpStatus
     * @Description:
     * */
    int batchUpdateOpStatus(@Param("year") int year, @Param("cardNums") List<String> cardNums,
            @Param("newOpStatus") int newOpStatus);

    /**
     * 使用卡
     * @Title:use
     * @Description:
     * */
    int use(@Param("year") int year, @Param("cardNum") String cardNum, @Param("mobile") String mobile,
            @Param("ip") String ip, @Param("serial") String serial);

    /**
     * 充值结果
     * @Title:use
     * @Description:
     * */
    int updateChargeResult(@Param("year") int year, @Param("chargeStatus") int chargeStatus,
            @Param("chargeMsg") String chargeMsg, @Param("cardNum") String cardNum);

    /**
     * 延期卡
     * @Title:use
     * @Description:
     * */
    int extend(@Param("year") int year, @Param("cardNum") String cardNum, @Param("newDeadline") Date newDealine,
            @Param("updateOpStatus") boolean updateOpStatus);

    /**
     * @param year
     * @param cardNums
     * @param entId
     * @param proId
     * @param oldStatus
     * @param newStatus
     * @return
     * @Title: batchActivate
     * @Description: 批量激活，绑定企业和产品
     * @return: int
     */
    int batchActivate(@Param("year") int year, @Param("cardNums") List<String> cardNums, @Param("entId") Long entId,
            @Param("proId") Long proId, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    /**
     * @param year
     * @param configId
     * @param entId
     * @param proId
     * @param oldStatus
     * @param newStuatus
     * @return
     * @Title: activateByConfigId
     * @Description: 根据configId激活
     * @return: int
     */
    int activateByConfigId(@Param("year") int year, @Param("configId") Long configId, @Param("entId") Long entId,
            @Param("proId") Long proId, @Param("oldStatus") int oldStatus, @Param("newStuatus") int newStuatus);

    /**
     * @param year
     * @param beginCardNum
     * @param endCardNum
     * @return
     * @Title: internalActivate
     * @Description: 区间激活
     * @return: int
     */
    int internalActivate(@Param("year") int year, @Param("beginCardNum") String beginCardNum,
            @Param("endCardNum") String endCardNum, @Param("enterId") Long enterId, @Param("proId") Long proId,
            @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    /**
     * @param year
     * @param cardNum
     * @param oldStatus
     * @param newStatus
     * @return
     * @Title: deactivate
     * @Description: 单张去激活
     * @return: int
     */
    int deactivate(@Param("year") int year, @Param("cardNum") String cardNum, @Param("oldStatus") int oldStatus,
            @Param("newStatus") int newStatus);

    /**
     * @param year
     * @param cardNums
     * @param oldStatus
     * @param newStatus
     * @return
     * @Title: batchDeactivate
     * @Description: 批量去激活
     * @return: int
     */
    int batchDeactivate(@Param("year") int year, @Param("cardNums") List<String> cardNums,
            @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    /**
     * @param year
     * @param cardNum
     * @param entId
     * @param oldStatus
     * @param newStatus
     * @return
     * @Title: activate
     * @Description: 卡激活
     * @return: int
     */
    int activate(@Param("year") int year, @Param("cardNum") String cardNum, @Param("entId") Long entId,
            @Param("pltPid") Long pltPid, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    /*MdrcCardInfo selectByConfigAndCardNum(@Param("config")Long config,
    		    @Param("endNum")String endNum,
    		    @Param("cardStatus")Integer cardStatus);*/

    /**
     * @Title:countInvalidCard
     * @Description:
     * */
    long countInvalidCard(@Param("year") int year, @Param("startSerial") String startSerial,
            @Param("endSerial") String endSerial, @Param("cardStatus") Integer cardStatus,
            @Param("configId") Long configId);

    /**
     * @Title:countNotActive
     * @Description:
     * */
    long countNotActive(@Param("year") int year, @Param("configId") Long configId,
            @Param("cardStatus") Integer cardStatus, @Param("startSerial") String startSerial,
            @Param("endSerial") String endSerial);

    /**
     * @param param
     * @return
     * @Description: 根据条件查询卡列表(不分页)
     */
    List<MdrcCardInfo> queryMdrcListByEntId(Map<String, Object> param);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(MdrcCardInfo mdrcCardInfo);

    /**
     * @title:getBycreateTime
     */
    List<MdrcCardInfo> getBycreateTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByStoredTime
     */
    List<MdrcCardInfo> getByStoredTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByBoundTime
     */
    List<MdrcCardInfo> getByBoundTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByActivatedTime
     */
    List<MdrcCardInfo> getByActivatedTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByUsedTime
     */
    List<MdrcCardInfo> getByUsedTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByLockedTime
     */
    List<MdrcCardInfo> getByLockedTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getBySerialNum
     */
    List<MdrcCardInfo> getBySerialNum(@Param("configId") Long configId, @Param("year") int year);

    /**
     * @title:getByDeactivateTime
     */
    List<MdrcCardInfo> getByDeactivateTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:getByUnlockTime
     */
    List<MdrcCardInfo> getByUnlockTime(@Param("begin") Date begin, @Param("end") Date end, @Param("year") int year);

    /**
     * @title:changeStatusByConfig
     */
    int changeStatusByConfigId(@Param("status") Integer status, @Param("configId") Long configId,
            @Param("year") int year);

    /**
     * @title:countMdrcCardsByMap
     */
    Long countMdrcCardsByMap(Map map);

    /**
     * @title:selectMdrcCardInfos
     */
    List<MdrcCardInfo> selectMdrcCardInfos(Map map);

    /**
     * @title:selectMdrcCardInfos
     */
    List<MdrcCardInfo> selectForShowChargeResult(Map map);

    /**
     * @title:countForShowChargeResult
     */
    int countForShowChargeResult(Map map);

    /**
     * 
     * @Title: getCardNumbersByCount 
     * @Description: TODO
     * @param configId
     * @param count
     * @param year
     * @param status
     * @return
     * @return: List<String>
     */
    List<String> getCardNumbersByCount(@Param("configId") Long configId, @Param("count") Long count,
            @Param("year") int year, @Param("status") int status);

    /**
     * 
     * @Title: batchStoreByConfigId 
     * @Description: TODO
     * @param configId
     * @param year
     * @param oldStatus
     * @param newStatus
     * @return
     * @return: int
     */
    int batchUpdateByConfigId(@Param("configId") Long configId, @Param("year") int year,
            @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);
}
