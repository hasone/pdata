package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierPayRecord;
import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierPayRecordService;
import com.cmcc.vrp.province.service.SupplierSuccessTotalUseService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商财务记录控制器
 * @author qinqinyan
 * */
@Controller
@RequestMapping("/manage/supplierFinance")
public class SupplierFinanceRecordController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(SupplierFinanceRecordController.class);
    
    @Autowired
    SupplierFinanceRecordService supplierFinanceRecordService;
    @Autowired
    SupplierPayRecordService supplierPayRecordService;
    @Autowired
    SupplierSuccessTotalUseService supplierSuccessTotalUseService;
    
    /**
     * @Title: index
     * @author qinqinyan
     */
    @RequestMapping("index")
    public String index(QueryObject queryObject, ModelMap modelMap){
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "supplierFinance/financeRecord.ftl";
    }
    
    /**
     * 查找供应商财务记录
     * @author qinqinyan
     * */
    @RequestMapping("search")
    public void search(HttpServletResponse response, QueryObject queryObject){
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("supplierName", queryObject);

        int count = supplierFinanceRecordService.countSupplierFinanceRecords(queryObject.toMap());
        List<SupplierFinanceRecord> list = supplierFinanceRecordService.querySupplierFinanceRecords(queryObject.toMap());

        List<SupplierFinanceRecord> updateRecords = new ArrayList<SupplierFinanceRecord>();
        for(SupplierFinanceRecord item : list){
            //供应商统成功充值金额由脚本统计
            List<SupplierSuccessTotalUse> supplierSuccessTotalUse = supplierSuccessTotalUseService.selectBySupplierId(item.getSupplierId());
            if(supplierSuccessTotalUse!=null && supplierSuccessTotalUse.size()>0){
                SupplierFinanceRecord updateRecord = new SupplierFinanceRecord();
                
                updateRecord.setSupplierId(item.getSupplierId());
                updateRecord.setTotalMoney(item.getTotalMoney());
                updateRecord.setUsedMoney(supplierSuccessTotalUse.get(0).getTotalUseMoney());
                if(item.getTotalMoney()==null){
                    updateRecord.setBalance(0-supplierSuccessTotalUse.get(0).getTotalUseMoney().doubleValue());
                }else{
                    updateRecord.setBalance(item.getTotalMoney().doubleValue()-supplierSuccessTotalUse.get(0).getTotalUseMoney().doubleValue());
                }
                updateRecord.setUpdateTime(new Date());
                
                updateRecords.add(updateRecord);
                
                //更新要显示的字段
                item.setUsedMoney(updateRecord.getUsedMoney());
                item.setBalance(updateRecord.getBalance());
            }
        }
        
        //跟新供应商财务记录
        if(updateRecords!=null && updateRecords.size()>0){
            try{
                if(!supplierFinanceRecordService.batchUpdate(updateRecords)){
                    logger.info("批量更新财务记录失败。"+JSON.toJSONString(updateRecords));
                }
            }catch(Exception e){
                logger.info("批量更新财务记录失败。"+JSON.toJSONString(updateRecords)+";失败原因"+e.getMessage());
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 供应商付款明细
     * @author qinqinyan
     * */
    @RequestMapping("payRecord")
    public String payRecord(ModelMap modelMap, Long supplierId){
        if(supplierId!=null){
            SupplierFinanceRecord supplierFinanceRecord = supplierFinanceRecordService.selectBySupplierId(supplierId);
            modelMap.addAttribute("supplierFinanceRecord", supplierFinanceRecord);
            modelMap.addAttribute("supplierId", supplierId);
            return "supplierFinance/payRecord.ftl";
        }
        modelMap.addAttribute("errorMsg", "未查找到该供应商相关付款记录");
        return "error.ftl";
    }
    
    /**
     * 删除付款记录
     * @param id 付款记录id
     * @author qinqinyan
     * @throws IOException 
     * */
    @RequestMapping("delPayRecord")
    public void delPayRecord(HttpServletResponse response, Long id) throws IOException{
        Map returnMap = new HashMap<String, String>();
        Boolean result = false; 
        String msg = "删除该条付款记录失败";
        Administer administer = getCurrentUser();
        if(id!=null){
            try{
                if(supplierPayRecordService.deleteSupplierPayRecord(id)){
                    logger.info("用户 adminId = {} 成功删除了付款记录 supplierPayRecordId = {}", administer.getId(), id);
                    result = true; 
                }else{
                    logger.info("用户 adminId = {} 删除付款记录 supplierPayRecordId = {} 失败", administer.getId(), id);
                }
            }catch(RuntimeException e){
                logger.info("用户 adminId = {} 删除付款记录 supplierPayRecordId = {} 失败", administer.getId(), id);
            }
        }
        returnMap.put("result", result.toString());
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }
    
    /**
     * 获取供应商付款记录
     * @author qinqinyan
     * */
    @RequestMapping("getPayRecords")
    public void getPayRecords(HttpServletResponse response, QueryObject queryObject,
            Long supplierId){
        
        setQueryParameter("supplierId", queryObject);
        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        setQueryParameter("lessMoney", queryObject);
        setQueryParameter("moreMoney", queryObject);
        
        if (queryObject.getQueryCriterias().get("endTime") != null) {
            queryObject.getQueryCriterias().put("startTime",
                    queryObject.getQueryCriterias().get("startTime").toString() + " 00:00:00");
            queryObject.getQueryCriterias().put("endTime",
                    queryObject.getQueryCriterias().get("endTime").toString() + " 23:59:59");
        }
        
        if(queryObject.getQueryCriterias().get("lessMoney") != null){
            String lessMoneyStr = queryObject.getQueryCriterias().get("lessMoney").toString();
            Double lessMoney = Double.valueOf(lessMoneyStr);
            queryObject.getQueryCriterias().remove("lessMoney");
            queryObject.getQueryCriterias().put("lessMoney", lessMoney.doubleValue()==0?0:lessMoney.doubleValue()*1000000);
        }
        if(queryObject.getQueryCriterias().get("moreMoney") != null){
            String moreMoneyStr = queryObject.getQueryCriterias().get("moreMoney").toString();
            Double moreMoney = Double.valueOf(moreMoneyStr);
            queryObject.getQueryCriterias().remove("moreMoney");
            queryObject.getQueryCriterias().put("moreMoney", moreMoney.doubleValue()==0?0:moreMoney.doubleValue()*1000000);
        }
        //Map map = new HashMap<String, String>();
        //map.put("supplierId", supplierId);
        queryObject.setPageSize(-1);
        int count = supplierPayRecordService.countSupplierPayRecords(queryObject.toMap());
        List<SupplierPayRecord> supplierPayRecords = supplierPayRecordService.querySupplierPayRecords(queryObject.toMap());

        JSONObject json = new JSONObject();
        //json.put("supplierPayRecords", supplierPayRecords);
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", supplierPayRecords);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 保存新增付款记录
     * @throws IOException 
     * @author qinqinyan
     * */
    @RequestMapping("saveAjax")
    public void saveAjax(HttpServletResponse response, HttpServletRequest request) throws IOException{
        Map returnMap = new HashMap<String, String>();
        Boolean result = false; 
        String msg = "参数异常,保存失败";
        Administer administer = getCurrentUser();
        
        String supplierPayRecordStr = request.getParameter("supplierPayRecord");
        
        if(administer!=null && !StringUtils.isEmpty(supplierPayRecordStr)){
            SupplierPayRecord supplierPayRecord = JSON.parseObject(supplierPayRecordStr, SupplierPayRecord.class);
            if(validate(supplierPayRecord)){
                init(supplierPayRecord, administer.getId());
                try{
                    if(supplierPayRecordService.saveSupplierPayRecord(supplierPayRecord)){
                        result = true;
                        msg = "保存成功";
                    }else{
                        msg = "保存失败";
                    }
                }catch(RuntimeException e){
                    msg = "保存失败";
                }
            }
        }
        returnMap.put("result", result.toString());
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }
    
    private boolean validate(SupplierPayRecord supplierPayRecord){
        if(supplierPayRecord!=null && supplierPayRecord.getPayMoney()!=null
                && supplierPayRecord.getPayTime()!=null && supplierPayRecord.getSupplierId()!=null
                && !StringUtils.isEmpty(supplierPayRecord.getNote())){
            return true;
        }
        return false;
    }
    
    private SupplierPayRecord init(SupplierPayRecord supplierPayRecord, Long adminId){
        supplierPayRecord.setCreateTime(new Date());
        supplierPayRecord.setUpdateTime(new Date());
        supplierPayRecord.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        supplierPayRecord.setOperatorId(adminId);
        return supplierPayRecord;
    }
    

}
