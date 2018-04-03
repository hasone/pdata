/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EntFlowControlRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:EntFlowControlRecordMapper
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月22日
 */
public interface EntFlowControlRecordMapper {
    int insertEntFlowControlRecord(EntFlowControlRecord efcr);

    int showHistoryForPageResultCount(Map map);

    List<EntFlowControlRecord> showHistoryForPageResultList(Map map);

    List<EntFlowControlRecord> selectEntFlowControlRecordByMap(Map map);
    
    /**
     * 更新记录，为了追加金额时，遇到-1，将日追加金额清0
     * title: updateEntFlowControlRecord
     * desc: 
     * @param efcr
     * @return
     * wuguoping
     * 2017年7月10日
     */
    int updateEntFlowControlRecord(EntFlowControlRecord efcr);
}
