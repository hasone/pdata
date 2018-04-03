package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinRecord;

import java.util.List;
import java.util.Map;

public interface IndividualFlowcoinRecordService {

    boolean insert(IndividualFlowcoinRecord record);

    boolean createRecord(IndividualAccountRecord record);

    List<IndividualFlowcoinRecord> selectByMap(Map map);

    int countByMap(Map map);

}
