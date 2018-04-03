package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.ec.bean.individual.OrderResponse;
import com.cmcc.vrp.province.model.IndividualFlowOrder;

/**
 * IndividualFlowOrderService.java
 * @author wujiamin
 * @date 2017年1月12日
 */
public interface IndividualFlowOrderService {
    /** 
     * @Title: insert 
     */
    boolean insert(IndividualFlowOrder record);

    /** 
     * @Title: insertSelective 
     */
    boolean insertSelective(IndividualFlowOrder record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    IndividualFlowOrder selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(IndividualFlowOrder record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    boolean updateByPrimaryKey(IndividualFlowOrder record);

    /** 
     * @Title: orderFlow 
     */
    OrderResponse orderFlow(String mobile, String prdCode, String ecSerialNum);

    /** 
     * 页面侧的流量订购
     * @Title: orderFlowForPage 
     */
    boolean orderFlowForPage(String mobile, String prdCode);

    /** 
     * @Title: selectBySystemNum 
     */
    IndividualFlowOrder selectBySystemNum(String systemNum);

    /** 
     * @Title: countByDate 
     */
    Integer countByDate(Date startTime, Date endTime);

    /** 
     * 判断订购次数是否超过限制
     * @Title: validateLimit 
     */
    void validateLimit(Long adminId, Map resultMap);
    
    /** 
     * @Title: selectByMap 
     */
    List<IndividualFlowOrder> selectByMap(Map map);

    /** 
     * @Title: countByMap 
     */
    Integer countByMap(Map map);
    
}
