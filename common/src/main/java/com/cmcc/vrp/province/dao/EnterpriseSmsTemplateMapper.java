package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:20:11
*/
public interface EnterpriseSmsTemplateMapper {

    /**
     * @param record
     * @return
     */
    int insert(EnterpriseSmsTemplate record);

    /**
     * @param enterId
     * @return
     */
    List<Long> selectByEnterId(Long enterId);

    /**
     * @param enterId
     * @return
     */
    int deleteByEnterId(Long enterId);

    /**
     * @param enterId
     * @return
     */
    List<EnterpriseSmsTemplate> selectSmsTemplateByEnterId(Long enterId);

    /**
     * @param enterId
     * @param smsTemplateId
     * @param status
     * @return
     */
    int updateStatus(@Param("enterId") Long enterId, @Param("smsTemplateId") Long smsTemplateId, @Param("status") Integer status);

    List<EnterpriseSmsTemplate> getChoosedSmsTemplate(Long enterId);

}
