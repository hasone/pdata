/**
 *
 */
package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntFlowControl;
import com.cmcc.vrp.province.model.EntFlowControlRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>
 * Title:EntFlowControlService
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月20日
 */
public interface EntFlowControlService {

    int showForPageResultCount(QueryObject queryObject);

    List<EntFlowControl> showForPageResultList(QueryObject queryObject);

    EntFlowControl getFlowControlRecordByEntId(Long entId);
    /**
     * 
     * title: updateSmsFlagByEntId
     * desc: 
     * @param entId
     * @param fcSmsFlag
     * @return
     * wuguoping
     * 2017年7月12日
     */
    boolean updateSmsFlagByEntId(Long entId, Integer fcSmsFlag);

    /**
     * 
     * 更新数据库的日上限金额
     * title: updateEntFlowControl
     * desc: 尼玛  连个注解也咩有  
     * @param entId
     * @param countUpper
     * @param updatorId
     * @param type
     * @return
     * wuguoping
     * 2017年7月12日
     */
    boolean updateEntFlowControl(Long entId, Long countUpper, Long updatorId, int type);
    /**
     * 
     * title: showHistoryForPageResultCount
     * desc: 
     * @param queryObject
     * @return
     * wuguoping
     * 2017年7月12日
     */
    int showHistoryForPageResultCount(QueryObject queryObject);
    /**
     * 
     * title: showHistoryForPageResultList
     * desc: 
     * @param queryObject
     * @return
     * wuguoping
     * 2017年7月12日
     */
    List<EntFlowControlRecord> showHistoryForPageResultList(
        QueryObject queryObject);

    /**
     * 
     * title: updateEntFlowControlAddition
     * desc: 
     * @param entId
     * @param countAddition
     * @return
     * wuguoping
     * 2017年7月12日
     */
    boolean updateEntFlowControlAddition(Long entId, Long countAddition);
    /**
     * 
     * title: isFlowControl
     * desc: 
     * @param deltaAmount
     * @param entId
     * @param isUpdate
     * @return
     * wuguoping
     * 2017年7月12日
     */
    boolean isFlowControl(Double deltaAmount, Long entId, boolean isUpdate);
}
