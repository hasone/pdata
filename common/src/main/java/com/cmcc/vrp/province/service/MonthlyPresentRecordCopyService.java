package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;


/**   
* @Title: MonthlyPresentRecordCopyService.java 
* @Package com.cmcc.vrp.province.service 
* @Description: 
* @author lgk8023   
* @date 2017年11月14日 下午5:16:52 
* @version V1.0   
*/
public interface MonthlyPresentRecordCopyService {


    /** 
    * @Title: batchInsert 
    * @Description:
    * @param monthlyPresentRecordCopyList
    * @return 
    * @return boolean
    * @throws 
    */
    boolean batchInsert(List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList);

    List<MonthlyPresentRecordCopy> getByRuleId(Long ruleId);

}
