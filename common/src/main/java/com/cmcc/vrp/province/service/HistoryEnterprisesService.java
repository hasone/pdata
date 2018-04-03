package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.HistoryEnterprises;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by qinqinyan on 2016/10/12.
 */
public interface HistoryEnterprisesService {

    /**
    * @Title: insert
    * @Description: TODO
    */ 
    boolean insert(HistoryEnterprises record);

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    boolean insertSelective(HistoryEnterprises record);

    /**
     * 根据请求审核id查找
     * @param requestId
     * @return
     * @author qinqinyan
     * */
    HistoryEnterprises selectByRequestId(Long requestId);
    
    /**
    * @Title: updateStatusByPrimaryKey
    * @Description: TODO
    */ 
    boolean updateStatusByPrimaryKey(HistoryEnterprises record);

    /**
     * 保存企业信息变更
     * @param historyEnterprises
     * @param adminId
     * @param request
     * @author qinqinyan
     * */
    boolean saveEdit(HistoryEnterprises historyEnterprises, Long adminId,
                     MultipartHttpServletRequest request);
    
    /**
    * @Title: selectHistoryEnterpriseByRequestId
    * @Description: 根据requestId查找记录
    */ 
    HistoryEnterprises selectHistoryEnterpriseByRequestId(Long requestId);

    /**
     * 判断是否有待审核的企业记录
     * @param entId
     * @author qinqinyan
     * */
    Boolean hasApprovalRecord(Long entId);

    /**
     * 根据企业id和企业状态（delete_flag）筛选企业
     * @param entId
     * @param status
     * @author qinqinyan
     * */
    List<HistoryEnterprises> selectByEntIdAndStatus(Long entId, Integer status);

}
