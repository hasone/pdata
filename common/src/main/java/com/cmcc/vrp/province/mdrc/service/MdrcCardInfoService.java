package com.cmcc.vrp.province.mdrc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.exception.PreconditionRequiredException;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.util.QueryObject;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:38:07
*/
public interface MdrcCardInfoService {

    /**
     *批量插入卡数据
     * @return
     * @throws
     * @Title:batchInsert
     * @Description: TODO
     * @author:
     */
    int batchInsert(int year, List<MdrcCardInfo> records);

    /**
     * @return
     * @throws
     * @Title:selectByPrimaryKey
     * @Description: TODO
     * @author:
     */
    MdrcCardInfo selectByPrimaryKey(int year, Long id);

    /**
     * 根据条件查询营销卡总数
     *
     * @return
     * @throws
     * @Title:queryMdrcList
     * @Description: TODO
     * @author: hexinxu
     */
    Long queryMdrcCount(QueryObject queryObject);

    /**
     * 根据条件查询营销卡列表
     *
     * @return
     * @throws
     * @Title:queryMdrcList
     * @Description: TODO
     * @author: hexinxu
     */
    List<MdrcCardInfo> queryMdrcList(QueryObject queryObject);

    /**
     * @return
     * @throws
     * @Title:pieStatistics
     * @Description: TODO
     * @author:
     */
    Map<String, Long> pieStatistics(int year, Long configId);

    /**
     * 批量过期
     * <p>
     *
     * @param year
     * @param cardNums
     * @throws
     */
    int batchExpire(int year, List<String> cardNums);

    /**
     * 单张卡过期
     * @return
     * @throws
     * @Title:expire
     * @Description: TODO
     * @author:
     */
    boolean expire(String cardNum);

    /**
     * 查询营销卡信息
     * <p>
     *
     * @param cardNum
     * @return
     */
    MdrcCardInfo get(String cardNum);

    /**
     * 根据配置规则获得记录
     * <p>
     *
     * @param config
     * @return
     */
    List<MdrcCardInfo> listByConfig(MdrcBatchConfig config);

    /**
     * @return
     * @throws
     * @Title:batchUpdatePassword
     * @Description: TODO
     * @author:
     */
    int batchUpdatePassword(List<MdrcCardInfo> cards);

    /**
     * 卡入库操作， 当前卡状态必须为新建卡
     * @return
     * @throws
     * @Title:store
     * @Description: TODO
     * @author:
     */
    boolean store(String cardNum) throws PreconditionRequiredException;

    /**
     * 批量入库
     * @return
     * @throws
     * @Title:batchStore
     * @Description: TODO
     * @author:
     */
    boolean batchStore(List<String> cardNums) throws PreconditionRequiredException;

    /**
     * 激活
     * @return
     * @throws
     * @Title:activate
     * @Description: TODO
     * @author:
     */
    boolean activate(String cardNum, String code, Long pltPid) throws PreconditionRequiredException;

    /**
     * 批量激活
     * @return
     * @throws
     * @Title:batchActivate
     * @Description: TODO
     * @author:
     */
    boolean batchActivate(List<String> cardNums, String code, Long pltPid) throws PreconditionRequiredException;

    /**
     * 去激活操作
     * @return
     * @throws
     * @Title:deactive
     * @Description: TODO
     * @author:
     */
    boolean deactive(String cardNum) throws PreconditionRequiredException;

    /**
     * 使用卡
     * @return
     * @throws
     * @Title:use
     * @Description: TODO
     * @author:
     */
    boolean use(String cardNumber, String password, String mobile, String ip, String serialNum);

    /**
     * 延期
     * @return
     * @throws
     * @Title:extend
     * @Description: TODO
     * @author:
     */
    boolean extend(String cardNum, Date newExpireDate) throws PreconditionRequiredException;

    /**
     * 批量延期
     * @return
     * @throws
     * @Title:batchExtend
     * @Description: TODO
     * @author:
     */
    boolean batchExtend(List<String> cardNums, Date newExpireDate) throws PreconditionRequiredException;

    /**
     * 锁定单张卡， 当前卡状态可以是新制卡，已入库或者已激活
     * <p>
     *
     * @param cardNum 卡号
     * @return 锁定成功返回true, 失败返回false
     * @throws PreconditionRequiredException
     */
    boolean lock(String cardNum) throws PreconditionRequiredException;

    /**
     * 批量锁卡
     */
    boolean batchLock(List<String> cardNums) throws PreconditionRequiredException;

    /**
     * 解锁， 当前卡状态只能是锁定状态
     */
    boolean unlock(String cardNum) throws PreconditionRequiredException;

    /**
     * 批量解锁
     */
    boolean batchUnlock(List<String> cardNums) throws PreconditionRequiredException;

    /**
     * 销卡，当前卡状态可以是新制卡，已入库或者已激活
     */
    boolean delete(String cardNum) throws PreconditionRequiredException;

    /**
     * 批量销卡
     */
    boolean batchDelete(List<String> cardNums) throws PreconditionRequiredException;

    /**
     * 使用卡之前校验卡号卡密及卡状态
     */
    boolean validateBeforeUse(String cardNum, String password, String mobile, String ip);

    //重新充值
    /**
     * @param cardNum
     * @return
     */
    boolean recharge(String cardNum);

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * @param configId
     * @param oldStatus
     * @param newStatus
     * @return
     * @Title: batchUpdateStatus
     * @Description: 批量更新状态
     * @return: Boolean
     */
    Boolean batchUpdateStatus(Long configId, MdrcCardStatus oldStatus, MdrcCardStatus newStatus);

    /**
     * @param configId
     * @return
     * @Title: listByConfigIdAndStatus
     * @Description: 根据规则ID和卡状态查询
     * @return: List<MdrcCardInfo>
     */
    List<MdrcCardInfo> listByConfigIdAndStatus(Long configId, MdrcCardStatus cardStatus);

    /**
     * @param configId
     * @param cardStatus
     * @return
     * @Title: countByConfigIdAndStatus
     * @Description: 根据规则ID和卡状态统计数量
     * @return: Long
     */
    Long countByConfigIdAndStatus(Long configId, MdrcCardStatus cardStatus);

    /**
     * @param configId
     * @param beginCardNum
     * @param endCardNum
     * @param entId
     * @param prodId
     * @return
     * @Title: internalActivate
     * @Description: TODO
     * @return: boolean
     */
    boolean internalActivate(Long configId, String beginCardNum, String endCardNum, Long entId, Long prodId);

    /**
     * @param cardNums
     * @return
     * @throws PreconditionRequiredException
     * @Title: batchDeactivate
     * @Description: 批量去激活
     * @return: int 返回去激活成功的卡数量
     */
    boolean batchDeactivate(List<String> cardNums) throws PreconditionRequiredException;

    /**
     * 区间激活，检查卡是否合法
     */
    boolean checkCardNum(Long config, String startSerial, String endSerial);

    /**
     * @return
     * @throws
     * @Title:countInvalidCard
     * @Description: TODO
     * @author:
     */
    long countInvalidCard(String startSerial, String endSerial, Integer cardStatus, Long config);

    /**
     * 区间激活
     */
    boolean activeRange(Long config, String startSerial, String endSerial, Long entId, Long proId);

    /**
     * @return
     * @throws
     * @Title:countNotActive
     * @Description: TODO
     * @author:
     */
    Long countNotActive(Long configId, Integer cardStatus, String startSerial, String endSerial);

    /**
     * @return
     * @throws
     * @Title:getByMdrcBatchConfigAndStatus
     * @Description: TODO
     * @author:
     */
    List<MdrcCardInfo> getByMdrcBatchConfigAndStatus(MdrcBatchConfig mdrcBatchConfig, Integer mdrcCardStatus);

    /**
     * @param queryCriterias
     * @return
     * @Description: 根据queryCriterias的参数查询所有营销卡信息
     */
    List<MdrcCardInfo> queryMdrcListByEntId(Map<String, Object> queryCriterias);

    /**
     * @param year
     * @param configId
     * @param entId
     * @return
     * @Description: 对企业的营销卡进行统计
     */
    Map<String, Long> pieStatisticsByEntId(int year, Long configId, Long entId);

    /**
     * @param queryObject
     * @return
     * @Description: 根据企业获取营销卡信息
     */
    Map<String, Object> getAllCardInfoByEnt(QueryObject queryObject);

    /**
     * @param cardNumbers
     * @return
     * @Title: isHaiNanProvince
     * @Description: 根据卡号判断是不是海南省份
     * @return: boolean
     */
    boolean isHaiNanProvince(List<String> cardNumbers);

    /**
     * 根据configId改变卡状态
     * @author qinqinyan
     * @date 2017/08/10
     * */
    boolean changeStatusByConfigId(Integer status, Long configId);

    /**
     * 
     * */
    Long countMdrcCardsByMap(Map map);

    /**
     * 
     * */
    List<MdrcCardInfo> selectMdrcCardInfos(Map map);

    /***
     * 爲了顯示充值記錄列表
     * @author qinqinyan
     * @date 2017/08/14
     * */
    List<MdrcCardInfo> selectForShowChargeResult(Map map);

    /***
     * 爲了顯示充值記錄列表
     * @author qinqinyan
     * @date 2017/08/14
     * */
    Integer countForShowChargeResult(Map map);

    /**
     * 
     * @Title: getCardNumbersByCount 
     * @Description: 获取指定数量的卡号
     * @param configId
     * @param count
     * @param status
     * @return
     * @return: List<String>
     */
    List<String> getCardNumbersByCount(Long configId, Long count, Integer status);

    /**
     * 
     * @Title: validate 
     * @Description: 卡号密码校验
     * @param cardNum
     * @param password
     * @return
     * @return: String
     */
    String validate(String cardNum, String password);

    /**
     * 
     * @Title: batchUpdateByConfigId 
     * @Description: 批量更新
     * @param configId
     * @param oldStatus
     * @param newStatus
     * @return
     * @return: boolean
     */
    boolean batchUpdateByConfigId(Long configId, int oldStatus, int newStatus);
    
    /**
     * 
     * @Title: updateChargeResult 
     * @Description: 更新营销卡充值状态
     * @param mdrcCardInfo
     * @param chargeStatus
     * @param chargeResult
     * @return
     * @return: boolean
     */
    boolean updateChargeResult(MdrcCardInfo mdrcCardInfo, int chargeStatus, String chargeResult);
    
    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param year
     * @param mdrcCardInfo
     * @return
     * @return: boolean
     */
    boolean updateByPrimaryKeySelective(int year, MdrcCardInfo mdrcCardInfo);
}
