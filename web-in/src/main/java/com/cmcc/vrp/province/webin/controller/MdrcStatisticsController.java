/**
 *
 */
package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcStatisticsCount;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:MdrcStatisticsController </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月19日
 */

@Controller
@RequestMapping("/manage/mdrc/statistics")
public class MdrcStatisticsController extends BaseController {

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    ManagerService managerService;

    /** 
     * @Title: index 
    */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap, QueryObject queryObject) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 

        //卡号
        setQueryParameter("cardNumber", queryObject);
        //卡状态
        setQueryParameter("status", queryObject);

        modelMap.addAttribute("cardNumber",
            queryObject.getQueryCriterias().get("cardNumber"));
        modelMap.addAttribute("status",
            queryObject.getQueryCriterias().get("status"));
        modelMap.addAttribute("cardStatus", MdrcCardStatus.toEntStatusMap());//营销卡状态

        return "mdrcStatistics/index.ftl";
    }


    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: search
     * @Description: 卡数据列表
     * @return: String
     */
    @RequestMapping("/search")
    public void search(HttpServletRequest request, QueryObject queryObject, HttpServletResponse res) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        List<Enterprise> enterpriseList = enterprisesService.getEnterpriseListByAdminId(getCurrentUser());
        Enterprise enterprise = enterpriseList.get(0);

        setQueryParameter("cardNumber", queryObject);

        setQueryParameter("status", queryObject);

        queryObject.getQueryCriterias().put("entId", enterprise.getId());


        Map<String, Object> map = mdrcCardInfoService.getAllCardInfoByEnt(queryObject);
        Long count = (Long) map.get("count");
        List<MdrcCardInfo> list = (List<MdrcCardInfo>) map.get("list");

        int fromIndex = (queryObject.getPageNum() - 1) * queryObject.getPageSize();
        int toIndex = queryObject.getPageNum() * queryObject.getPageSize();
        if (toIndex > count) {
            toIndex = count.intValue();
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list.subList(fromIndex, toIndex));
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 营销卡状态统计圆饼图
     * @author qinqinyan
     * @date 2017/08/11
     * */
    @RequestMapping("mdrcPieStatistics")
    public String mdrcPieStatistics(ModelMap modelMap, QueryObject queryObject) {
        Manager manager = getCurrentUserManager();
        modelMap.addAttribute("managerId", manager.getId());
        return "mdrcStatistics/pieStatistics.ftl";
    }
    
    /**
     * 
     * */
    @RequestMapping("getData")
    public void getData(HttpServletRequest request, HttpServletResponse response,
            QueryObject queryObject) throws IOException{
        Map<String, String> returnMap = new HashMap<String, String>();
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        MdrcStatisticsCount mdrcStatisticsCount = new MdrcStatisticsCount();
        
        setQueryParameter("eName", queryObject);
        setQueryParameter("managerId", queryObject);
        
        Manager manager = getCurrentUserManager();
        
        Map map = queryObject.toMap();
        String managerId = (String)map.get("managerId");
        if(manager!=null && !StringUtils.isEmpty(managerId)){
            boolean authFlag = true; //当前用户有查看数据的权限
            Long sonManangeId = null;
            Long currentManageId = manager.getId();
            //判断当前登录的用户manageId 和 前端传过来的 manageId 是否有有关联关系
            if(!managerId.equals(currentManageId.toString())){
                //前端传入的managerId和登录用户currentManageId不相等，说明传入的是子节点
                try{
                    sonManangeId = Long.valueOf(managerId);
                    if(!managerService.isParentManage(sonManangeId, currentManageId)){
                        //说明传入的不是子节点
                        authFlag = false;
                    }
                }catch(Exception e){
                    authFlag = false;
                }
            }
            
            if(authFlag){
                List<Enterprise> enterprises = enterprisesService.getNormalEnterByManagerId(Long.valueOf(managerId));
                if(enterprises!=null && enterprises.size()>0){
                    map.put("enterprises", enterprises);
                    
                    List<MdrcCardInfo> records = mdrcCardInfoService.selectMdrcCardInfos(map);
                    if(records!=null && records.size()>0){
                        
                        long newCount = 0l;
                        long storedCount = 0l;
                        long activatedCount = 0l;
                        long usedCount = 0l;
                        long expiredCount = 0l;
                        long lockedCount = 0l;
                        long deleteCount = 0l;
                        
                        for(MdrcCardInfo item : records){
                            if(item.getStatus().toString().equals(MdrcCardStatus.NEW.getCode().toString())){
                                newCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.STORED.getCode().toString())){
                                storedCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.ACTIVATED.getCode().toString())){
                                activatedCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.USED.getCode().toString())){
                                usedCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.EXPIRED.getCode().toString())){
                                expiredCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.LOCKED.getCode().toString())){
                                lockedCount += 1;
                            }else if(item.getStatus().toString().equals(MdrcCardStatus.DELETE.getCode().toString())){
                                deleteCount += 1;
                            }
                        }
                        mdrcStatisticsCount.setNewCount(newCount);
                        mdrcStatisticsCount.setStoredCount(storedCount);
                        mdrcStatisticsCount.setActivatedCount(activatedCount);
                        mdrcStatisticsCount.setUsedCount(usedCount);
                        mdrcStatisticsCount.setExpiredCount(expiredCount);
                        mdrcStatisticsCount.setLockedCount(lockedCount);
                        mdrcStatisticsCount.setDeleteCount(deleteCount);
                        mdrcStatisticsCount.setTotalCount((long)records.size());
                    }
                }
            }
        }
        /**
         ** newCount: "新制卡"
         * storedCount: "已签收 "
         * activatedCount: "已激活"
         * usedCount: "已使用 "
         * expiredCount: "已过期 "
         * lockedCount: "已锁定 "
         * deleteCount: "已销卡 "
         * totalCount: "总数 "
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("新制卡", mdrcStatisticsCount.getNewCount().toString());
        jsonObject.put("已签收", mdrcStatisticsCount.getStoredCount().toString());
        jsonObject.put("已激活", mdrcStatisticsCount.getActivatedCount().toString());
        jsonObject.put("已使用", mdrcStatisticsCount.getUsedCount().toString());
        jsonObject.put("已过期 ", mdrcStatisticsCount.getExpiredCount().toString());
        jsonObject.put("已锁定 ", mdrcStatisticsCount.getLockedCount().toString());
        jsonObject.put("已销卡 ", mdrcStatisticsCount.getDeleteCount().toString());
        
        returnMap.put("mdrcStatisticsCount", JSON.toJSONString(mdrcStatisticsCount));
        //returnMap.put("pie", jsonObject.toJSONString());
        
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }
    

    /**
     * @param modelMap
     * @param configId
     * @param year
     * @return
     * @Title: pieStatistics
     * @Description: 饼状统计图
     * @return: String
     */
    @RequestMapping("/pieStatistics")
    public String pieStatistics(ModelMap modelMap) {

        Map<String, Long> result = new HashMap<String, Long>();
        List<Enterprise> enterpriseList = enterprisesService.getEnterpriseListByAdminId(getCurrentUser());
        Enterprise enterprise = enterpriseList.get(0);

        List<MdrcBatchConfig> mdrcBatchConfigList = mdrcBatchConfigService.selectAllConfig();

        if (mdrcBatchConfigList != null && mdrcBatchConfigList.size() > 0) {
            for (MdrcBatchConfig mdrcBatchConfig : mdrcBatchConfigList) {
                Map<String, Long> map = mdrcCardInfoService.pieStatisticsByEntId(Integer.parseInt(mdrcBatchConfig.getThisYear().substring(2)),
                    mdrcBatchConfig.getId(), enterprise.getId());
                result = addAll(map, result);
            }

        }

        String[] rdata = toData(result, "pie");
        toModel(modelMap, result);
        modelMap.addAttribute("nameData", rdata[0]);
        modelMap.addAttribute("valueData", rdata[1]);

        return "mdrcStatistics/entPieStatistics.ftl";
    }

    private String[] toData(Map<String, Long> map, String type) {

        // 需要保证按一定顺序排列
        Map<String, String> rmap = new HashMap<String, String>();
        for (String key : map.keySet()) {
            String value = MdrcCardStatus.fromValue(Integer.valueOf(key)).getMessage();
            rmap.put(key, value + "," + map.get(key).toString());
        }
        if ("pie".equals(type)) {
            return pieToData(rmap);
        } else {
            return barToDate(rmap);
        }

    }

    // 转换成饼图需要的格式
    private String[] pieToData(Map<String, String> rmap) {
        String[] rdata = new String[2];
        StringBuffer nameData = new StringBuffer();
        StringBuffer valueData = new StringBuffer();
        nameData.append("data:[");
        valueData.append("data:[");
        for (String key : rmap.keySet()) {
            String temp = rmap.get(key);
            if (temp == null) {
                continue;
            }
            String[] data = temp.split(",");
            nameData.append("'" + data[0] + "',");
            valueData.append("{value:" + data[1] + ",name:'" + data[0] + "'},");

        }
        rdata[0] = nameData.toString().substring(0,
            nameData.toString().length() - 1)
            + "]";
        rdata[1] = valueData.toString().substring(0,
            valueData.toString().length() - 1)
            + "]";

        return rdata;
    }

    private String[] barToDate(Map<String, String> rmap) {

        String[] rdata = new String[2];
        StringBuffer nameData = new StringBuffer();
        StringBuffer valueData = new StringBuffer();
        nameData.append("[");
        valueData.append("[");
        for (String key : rmap.keySet()) {
            String[] data = rmap.get(key).replaceAll("_", "").split(",");
            if (data[0].length() > 6) {
                nameData.append("'"
                    + data[0].substring(data[0].length() - 2,
                    data[0].length()) + "',");
            } else {
                nameData.append("'" + data[0] + "',");
            }

            valueData.append(rmap.get(key).split(",")[1] + ",");

        }
        rdata[0] = nameData.toString().substring(0,
            nameData.toString().length() - 1)
            + "]";
        rdata[1] = valueData.toString().substring(0,
            valueData.toString().length() - 1)
            + "]";

        return rdata;
    }

    private void toModel(ModelMap modelMap, Map<String, Long> map) {

        Long count = 0L;
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                count += map.get(key);
            }
        }
        modelMap.put("cardStatus", MdrcCardStatus.toMap());
        modelMap.put("statusStatistics", map);
        modelMap.addAttribute("count", count);
    }

    private Map<String, Long> addAll(Map<String, Long> map, Map<String, Long> result) {

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (result.containsKey(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue() + result.get(entry.getKey()));
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;

    }

}
