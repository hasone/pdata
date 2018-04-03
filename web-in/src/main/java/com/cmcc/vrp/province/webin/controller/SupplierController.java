package com.cmcc.vrp.province.webin.controller;
/**
 * <p>Title:SupplierContorller </p>
 * <p>Description: 供应商</p>
 *
 * @author xujue
 * @date 2016年6月15日
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SupplierController.java
 */
@Controller
@RequestMapping("/manage/supplier")
public class SupplierController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    SupplierService supplierService;
    @Autowired
    GlobalConfigService getGlobalConfigService;
    @Autowired 
    SupplierProductService supplierProductService;
    @Autowired 
    SupplierProductAccountService supplierProductAccountService;
    @Autowired
    XssService xssService;

    /**
     * @Title: index
     * @author wujiamin
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "supplier/index.ftl";
    }

    /**
     * @Title: search
     * @author wujiamin
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("supplierName", queryObject);

        int count = supplierService.queryPaginationSupplierCount(queryObject);
        List<Supplier> list = supplierService.queryPaginationSupplier(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: showDetail
     * @author wujiamin
     * 
     * edit by qinqinyan 增加供應商產品列表和企業餘額
     */
    @RequestMapping("showDetail")
    public String showDetail(Long id, ModelMap map) {

        Supplier supplier = supplierService.get(id);
        if (supplier == null) {
            map.put("errorMsg", "不存在该供应商id：" + id);
            return "error.ftl";
        }

        map.put("supplier", supplier);
        
        //根據供應商id獲取供應商產品
        List<SupplierProduct> supplierProducts = supplierProductService.selectBySupplierId(id);
        for(SupplierProduct item : supplierProducts){
            SupplierProductAccount account = supplierProductAccountService.getInfoBySupplierProductId(item.getId());
            if(account!=null){
                item.setLeftCount(account.getCount());
            }
        }
        map.put("supplierProducts", supplierProducts);
        return "supplier/supplierDetail.ftl";
    }

    /**
     * @Title: suspendSupplier
     * @author wujiamin
     */
    @RequestMapping("suspendSupplier")
    public void suspendSupplier(Long id, HttpServletResponse res) {

        Map map = new HashMap();

        try {
            if (supplierService.suspendSupplier(id)) {
                map.put("result", "true");
            } else {
                map.put("result", "false");
            }
        } catch (RuntimeException e) {
            map.put("result", "false");
        }

        try {
            res.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: recoverSupplier
     * @author wujiamin
     */
    @RequestMapping("recoverSupplier")
    public void recoverSupplier(Long id, HttpServletResponse res) {

        Map map = new HashMap();

        try {
            if (supplierService.recoverSupplier(id)) {
                map.put("result", "true");
            } else {
                map.put("result", "false");
            }
        } catch (RuntimeException e) {
            map.put("result", "false");
        }

        try {
            res.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建供应商
     * 
     * @param map
     * @author qinqinyan
     */
    @RequestMapping("create")
    public String create(ModelMap map) {
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            return getLoginAddress();
        }
        return "supplier/create.ftl";
    }

    /**
     * 保存供应商
     * 
     * @param modelMap
     * @param supplier
     * @author qinqinyan
     */
    @RequestMapping("save")
    public String save(ModelMap modelMap, Supplier supplier) {
        if (getCurrentUserManager() == null) {
            return getLoginAddress();
        }

        try {
            supplier.setName(xssService.stripQuot(supplier.getName()));
            supplier.setFingerprint(xssService.stripQuot(supplier.getFingerprint()));
            if (supplierService.create(supplier)) {
                logger.info("成功插入供应商信息");
                return "redirect:index.html";
            }
        } catch (Exception e) {
            logger.error("插入供应商失败,失败原因:" + e.getMessage());
            modelMap.addAttribute("errorMsg", "保存失败");
        }
        return create(modelMap);
    }

    /**
     * 编辑供应商
     * 
     * @param map
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("edit")
    public String edit(ModelMap map, Long id) {
        if (getCurrentUserManager() == null) {
            return getLoginAddress();
        }

        if (id != null) {
            Supplier supplier = supplierService.get(id);
            if (supplier != null) {
                map.addAttribute("supplier", supplier);
                return "supplier/edit.ftl";
            }
        }
        map.addAttribute("errorMsg", "未查找到该条供应商详情！");
        return "error.ftl";
    }

    /**
     * 保存编辑信息
     * 
     * @param modelMap
     * @param supplier
     * @author qinqinyan
     */
    @RequestMapping("saveEdit")
    public String saveEdit(ModelMap modelMap, Supplier supplier) {
        if (getCurrentUserManager() == null) {
            return getLoginAddress();
        }

        if (supplier != null) {
            supplier.setUpdateTime(new Date());

            try {
                supplier.setName(xssService.stripQuot(supplier.getName()));
                supplier.setFingerprint(xssService.stripQuot(supplier.getFingerprint()));
                if (supplierService.updateByPrimaryKeySelective(supplier)) {
                    logger.info("保存成功, ID - {}", supplier.getId());
                    return "redirect:index.html";
                }
            } catch (Exception e) {
                logger.error("保存编辑信息失败， ID - {}, 失败原因：{}");
                modelMap.addAttribute("errorMsg", "保存失败");
            }
            return edit(modelMap, supplier.getId());
        } else {
            modelMap.addAttribute("errorMsg", "参数缺失,保存失败");
            return edit(modelMap, null);
        }
    }

    /**
     * 上下架
     * 
     * @param response
     * @param id
     * @param status
     * @author qinqinyan
     */
    @RequestMapping("changeSupplierStatus")
    public void changeSupplierStatus(HttpServletResponse response, Long id, Integer status) throws IOException {
        Map returnMap = new HashMap<String, String>();

        if (getCurrentUserManager() == null) {
            logger.info("获取用户信息失败。id = {}, status = {}", id, status);
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
        if (id == null || status == null) {
            logger.info("存在参数为空。id = {}, status = {}", id, status);
            returnMap.put("msg", "参数错误");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        String msg = "上架";
        if (status.intValue() == SupplierStatus.OFF.getCode()) {
            msg = "下架";
        }

        Supplier record = new Supplier();
        record.setId(id);
        record.setStatus(status);
        record.setUpdateTime(new Date());
        if (supplierService.updateByPrimaryKeySelective(record)) {
            logger.info(msg + "成功。id = {}, status = {}", id, status);
            msg = msg + "成功！";
        } else {
            logger.info(msg + "失败。id = {}, status = {}", id, status);
            msg = msg + "失败！";
        }
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 删除供应商
     * 
     * @param response
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("delSupplier")
    public void delSupplier(HttpServletResponse response, Long id) throws IOException {
        Map returnMap = new HashMap<String, String>();
        if (getCurrentUserManager() == null) {
            logger.info("获取用户信息失败。id = {}", id);
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
        String msg = "";
        if (id != null) {
            try {
                if (supplierService.delete(id)) {
                    logger.info("删除供应商成功, id = {}", id);
                    msg = "成功删除供应商！";
                }
            } catch (RuntimeException e) {
                logger.info("删除供应商失败, id = {}, 失败原因  : {}", id, e.getMessage());
                msg = "删除供应商失败！";
            }
        } else {
            msg = "参数异常,删除供应商失败！";
        }
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 下载供应商
     * 
     * @param response
     * @param name
     * @author qinqinyan
     */
    @RequestMapping("downloadSuppliers")
    public void downloadSuppliers(HttpServletResponse response, String name) {
        Map map = new HashMap();
        /**
         * 查询参数: 供应商名称
         */
        map.put("name", name);
        List<Supplier> suppliers = supplierService.selectByMap(map);

        String timeType = "yyyy-MM-dd HH:mm:ss";

        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("供应商");
        titles.add("供应商指纹");
        titles.add("状态");
        titles.add("创建时间");
        titles.add("修改时间");

        List<String> rowList = new ArrayList<String>();
        for(int i = 1; i <= suppliers.size(); i++){
            rowList.add("" + i);
            rowList.add(suppliers.get(i-1).getName());
            rowList.add(suppliers.get(i-1).getFingerprint());
            if(suppliers.get(i-1).getStatus().intValue()==SupplierStatus.ON.getCode()){
                rowList.add("上架");
            }else{
                rowList.add("下架");
            }
            rowList.add(DateUtil.dateToString(suppliers.get(i-1).getCreateTime(), timeType));
            rowList.add(DateUtil.dateToString(suppliers.get(i-1).getUpdateTime(), timeType));
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "suppliers.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 校验供应商名称唯一性
     * @throws IOException
     * @param response
     * @param name
     * @param supplierId
     * @author qinqinyan
     * @Date 2017/5/9
     * */
    @RequestMapping("checkNameUnique")
    public void checkProductCodeUnique(HttpServletResponse response, String name, Long supplierId) throws IOException {
        //Map<String, String> returnMap = new HashMap<String, String>();
        Boolean result = true;
        List<Supplier> list = supplierService.selectByName(name);
        if(list!=null && list.size()>0){
            if(supplierId!=null){
                //编辑
                for(Supplier item : list){
                    if(!item.getId().toString().equals(supplierId.toString())){
                        result = false;
                        break;
                    }
                }
            }else{
                //新建
                result = false;
            }
        }
        //returnMap.put("result", result.toString());
        response.getWriter().write(result.toString());
    }

    /**
     * 校验供应商指纹唯一性
     * @throws IOException
     * @param response
     * @param name
     * @param supplierId
     * @author qinqinyan
     * @Date 2017/5/11
     * */
    @RequestMapping("checkFingerprintUnique")
    public void checkFingerprintUnique(HttpServletResponse response, String fingerprint, Long supplierId) throws IOException {
        Boolean result = true;
        Supplier supplier = supplierService.getByFingerPrint(fingerprint);
        if(supplier!=null){
            if(supplierId!=null){
                if(!supplier.getId().toString().equals(supplierId.toString())){
                    result = false;
                }
            }else{
                //新建
                result = false;
            }
        }
        response.getWriter().write(result.toString());
    }
}
