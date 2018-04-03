/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EntFlowControl;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:EntFlowControlMapper
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月20日
 */
public interface EntFlowControlMapper {

    /**
     * @param map
     * @return
     */
    int showForPageResultCount(Map map);

    /**
     * @param map
     * @return
     */
    List<EntFlowControl> showForPageResultList(Map map);

    /**
     * @param entId
     * @return
     */
    EntFlowControl getFlowControlRecordByEntId(Long entId);

    /**
     * @param map
     * @return
     */
    int updateSmsFlagByEntId(Map<String, Object> map);

    /**
     * 获取日上限额的记录
     * title: getFlowControlUpperByEntId
     * desc: 
     * @param entId
     * @return
     * wuguoping
     * 2017年7月10日
     */
    EntFlowControl getFlowControlUpperByEntId(Long entId);

    /**
     * @param efc
     * @return
     */
    int insertEntFlowControlUpper(EntFlowControl efc);

    /**
     * @param efc
     * @return
     */
    int updateEntFlowControlUpper(EntFlowControl efc);

}
