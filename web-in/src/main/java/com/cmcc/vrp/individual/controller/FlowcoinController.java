package com.cmcc.vrp.individual.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.FlowCoinPurchaseStatus;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.QueryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujiamin
 * @date 2016年10月17日下午4:52:39
 */
@Controller
@RequestMapping("/individual/flowcoin")
public class FlowcoinController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FlowcoinController.class);
    @Autowired
    IndividualProductMapService individualProductMapService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualFlowcoinPurchaseService individualFlowcoinPurchaseService;
    @Autowired
    IndividualFlowcoinExchangeService individualFlowcoinExchangeService;

    /**
     * @param map
     * @return
     */
    @RequestMapping("purchaseIndex")
    public String purchaseFlowcoinIndex(ModelMap map) {
        return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
    }

    /**
     * 根据输入的数量，获取价格
     *
     * @param map
     * @param count
     * @param resp
     * @Title: getMoney
     * @Author: wujiamin
     * @date 2016年9月23日下午1:25:03
     */
    @RequestMapping("getMoney")
    public void getMoney(ModelMap map, Integer count, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }
        if (count == null) {
            logger.info("流量币个数为空");
            return;
        }

        IndividualProduct product = individualProductService.getFlowcoinProduct();
        IndividualProductMap ipm = individualProductMapService.getByAdminIdAndProductId(admin.getId(), product.getId());
        if (ipm != null) {
            Double money = ipm.getPrice() * ipm.getDiscount() / 100d * count / 100d;
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.0#");  
            df.setGroupingUsed(true);
            df.setMaximumFractionDigits(1);
            returnMap.put("result", df.format(money));
        } else {
            returnMap.put("result", "该用户无法购买流量币");
        }

        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存流量币购买
     *
     * @param map
     * @param coins
     * @return
     * @Title: savePurchase
     * @Author: wujiamin
     * @date 2016年9月23日下午4:14:43
     */
    @RequestMapping("savePurchase")
    public String savePurchase(ModelMap map, Integer coins, Boolean payFlag) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            map.put("errorMsg", "无法获取当前用户信息");
            return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
        }
        if (coins == null) {
            map.put("errorMsg", "未填写流量币数量");
            return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
        }

//		IndividualProduct product = individualProductService.getFlowcoinProduct();		
//		IndividualProductMap ipm = individualProductMapService.getByAdminIdAndProductId(admin.getId(), product.getId());

        IndividualFlowcoinPurchase record = new IndividualFlowcoinPurchase();
        record.setAdminId(admin.getId());
        record.setCount(coins);

        if (!individualFlowcoinPurchaseService.saveFlowcoinPurchase(record)) {
            map.put("errorMsg", "购买流量币订单创建失败！");
            return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
        }

        map.put("record", record);

        if (payFlag) {
            return "individual/flowcoinPurchase/flowCoin_purchase-pay.ftl";
        } else {
            return "individual/flowcoinPurchase/flowCoin_purchase-success.ftl";
        }
    }

    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping("historyOrders")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }

        setQueryParameter("startDate", queryObject);
        setQueryParameter("endDate", queryObject);
        setQueryParameter("status", queryObject);

        queryObject.getQueryCriterias().put("adminId", admin.getId());

        // 数据库查找符合查询条件的个数
        int count = individualFlowcoinPurchaseService.countByMap(queryObject.toMap());
        List<IndividualFlowcoinPurchase> list = individualFlowcoinPurchaseService.selectByMap(queryObject.toMap());

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
     * @param map
     * @param systemSerial
     * @return
     */
    @RequestMapping("detail")
    public String detail(ModelMap map, String systemSerial) {
        IndividualFlowcoinPurchase record = individualFlowcoinPurchaseService.selectBySystemSerial(systemSerial);
        if (record != null) {
            map.put("record", record);
            return "individual/flowcoinPurchase/flowCoin_purchase-detail.ftl";
        }
        return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
    }

    /**
     * @param map
     * @param systemSerial
     * @return
     */
    @RequestMapping("showPay")
    public String showPay(ModelMap map, String systemSerial) {
        IndividualFlowcoinPurchase record = individualFlowcoinPurchaseService.selectBySystemSerial(systemSerial);
        if (record != null) {
            map.put("record", record);
            return "individual/flowcoinPurchase/flowCoin_purchase-pay.ftl";
        }
        return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
    }


    /**
     * @param map
     * @param systemSerial
     * @param resp
     */
    @RequestMapping("pay")
    public void pay(ModelMap map, String systemSerial, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }
        if (systemSerial == null) {
            logger.info("流量币购买订单号为空");
            return;
        }
        try {
            if (individualFlowcoinPurchaseService.pay(admin.getId(), systemSerial)) {
                returnMap.put("result", "success");
            } else {
                logger.info("支付失败！");
                returnMap.put("result", "fail");
            }
        } catch (Exception e) {
            logger.info("支付失败！抛异常：" + e.getMessage());
            returnMap.put("result", "fail");
        }

        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map
     * @param systemSerial
     * @return
     */
    @RequestMapping("cancelPurchase")
    public String cancelPurchase(ModelMap map, String systemSerial) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            map.put("errorMsg", "无法获取当前用户信息");
            return "error.ftl";
        }
        try {
            if (!individualFlowcoinPurchaseService.updateStatus(systemSerial, FlowCoinPurchaseStatus.CANCEL.getCode())) {
                logger.info("取消失败！");
            }
            ;
        } catch (Exception e) {
            logger.info("取消失败！" + e.getMessage());
        }
        return "individual/flowcoinPurchase/flowCoin_purchase.ftl";
    }


    /**
     * @param map
     * @return
     */
    @RequestMapping("exchangeIndex")
    public String exchangeIndex(ModelMap map) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            map.put("errorMsg", "无法获取当前用户信息");
            return "error.ftl";
        }
        List<IndividualProduct> products = individualProductService.getProductsByAdminIdAndType(admin.getId(), IndividualProductType.FLOW_PACKAGE.getValue());
        if (products != null && products.size() > 0) {
            map.put("flowcoinCount", getFlowcoinCount(admin.getId(), products.get(0).getId()));
            map.put("productId", products.get(0).getId());
        }

        map.put("products", products);

        return "individual/flowcoinExchange/exchange.ftl";
    }

    /*
     * 根据兑换产品需要的流量币个数
     */
    private Integer getFlowcoinCount(Long adminId, Long productId) {
        IndividualProductMap flowcoinProductMap 
            = individualProductMapService.getByAdminIdAndProductId(adminId, individualProductService.getFlowcoinProduct().getId());
        IndividualProductMap productMap = individualProductMapService.getByAdminIdAndProductId(adminId, productId);

        Integer count = (productMap.getPrice() * productMap.getDiscount())
            / (flowcoinProductMap.getPrice() * flowcoinProductMap.getDiscount());

        return count;
    }


    @RequestMapping("getFlowcoinCount")
    public void getFlowcoinCount(ModelMap map, Long productId, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }
        if (productId == null) {
            logger.info("产品Id为空");
            return;
        }

        Integer count = getFlowcoinCount(admin.getId(), productId);
        returnMap.put("result", count);

        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map
     * @param count
     * @param mobile
     * @param productId
     * @param resp
     */
    @RequestMapping("exchange")
    public void exchange(ModelMap map, Integer count, String mobile, Long productId, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }
        if (productId == null) {
            logger.info("产品Id为空");
            return;
        }

        //保存兑换
        try {
            if (individualFlowcoinExchangeService.createExchange(admin.getId(), productId, count, mobile)) {
                returnMap.put("result", "success");
            } else {
                returnMap.put("result", "fail");
            }
        } catch (Exception e) {
            logger.info("创建流量币兑换记录时抛出异常，错误信息为：" + e.getMessage());
            returnMap.put("result", "fail");
        }

        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping("exchangeHistoryOrders")
    public void searchExchangeHistoryOrders(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }

        setQueryParameter("mobile", queryObject);

        queryObject.getQueryCriterias().put("adminId", admin.getId());

        // 数据库查找符合查询条件的个数
        int count = individualFlowcoinExchangeService.countByMap(queryObject.toMap());
        List<IndividualFlowcoinExchange> list = individualFlowcoinExchangeService.selectByMap(queryObject.toMap());

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
}
