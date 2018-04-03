package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.enums.OrderOperationEnum;
import com.cmcc.vrp.enums.OrderOperationStatus;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SdBossProductService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;

/**
 * 
 */
@Controller
@RequestMapping(value = "orders")
public class OrderInterfaceController {

    private static final Logger logger = LoggerFactory.getLogger(OrderInterfaceController.class);

    private static final int MAX_LENGTH_64 = 64;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    SupplierService supplierService;

    private static String FINGER_PRINT = "shandongcloud";//山东移动
    private static String ISP = "M";
    private static String OWNERSHIP_REGION = "全国";
    private static String ROAMING_REGION = "全国";
    private static final String SD_CLOUD_CODE = "99999";//山东云平台企业编码

    @Autowired
    private AdministerService administerService;
    
    @Autowired
    SdBossProductService sdBossProductService;

    /**
     * 
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void orderOperation(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        String appKey = null;//企业APPKEY
        String systemSerialNum = null;//系统流水号

        //校验认证返回的参数,返回appKey和 systemSerialNum,否则认为认证失败，返回403
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
                || StringUtils.isBlank(systemSerialNum = (String) request.getAttribute(Constants.SYSTEM_NUM_ATTR))) {
            logger.error("认证未通过, AppKey = {}, SystemSerialNum = {}.", appKey, systemSerialNum);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //校验是否是山东云平台发送的请求
        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
        if (enterprise == null || DELETE_FLAG.UNDELETED.getValue() != enterprise.getDeleteFlag().intValue()
                || !SD_CLOUD_CODE.equalsIgnoreCase(enterprise.getCode())) {
            logger.error("非山东云平台推送消息,约定只能使用山东云平台【code = " + SD_CLOUD_CODE + "】推送消息.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String json = (String) request.getAttribute(Constants.BODY_XML_ATTR);//参数获取，JSON格式字符串
        logger.info("订单接口收到报文：" + json);
        int httpCode = HttpServletResponse.SC_BAD_REQUEST;//默认操作失败，参数缺失或者非法，返回400
        try {

            //TODO：订单接口业务处理
            if (StringUtils.isNotBlank(json) && handleOrders(json)) {//操作成功，返回http状态码200
                httpCode = HttpServletResponse.SC_OK;
            }
        } catch (Exception e) {
            logger.error("参数错误或操作失败:" + json);
            e.printStackTrace();
        }
        response.setStatus(httpCode);
        logger.info("订单接口响应状态码：" + httpCode);
        return;
    }

    /**
     * 
     * @Title: handleOrders 
     * @Description: 订单信息处理
     * @param json
     * @return
     * @return: boolean
     */
    private boolean handleOrders(String json) {
        //报文解析：订单信息转换为平台产品信息
        List<SupplierProduct> allSupplierProducts = jsonToSupplierProducts(json);

        //订单归类：分为新增订单和更新订单（暂停、恢复、更新、删除操作都归类为更新订单）
        JSONObject jo = JSONObject.parseObject(json);
        String opr = jo.getString("opr");//接口类型：订单接口指定为order
        String type = jo.getString("type");
        String infoString = jo.getString("info");
        JSONObject info = JSONObject.parseObject(infoString);

        //需要更新的信息
        String seq = info.getString("seq");//订购编号
        String productID = info.getString("productID");//产品编号
        String customerType = info.getString("customerType");//客户类型
        String customerID = info.getString("customerID");//客户ID
        String oprCode = info.getString("oprCode");//操作码
        String customerName = info.getString("customerName");//客户名称
        String enterpriseId = info.getString("enterpriseId");//企业编号
        String discount = info.getString("discount");//0323新增的折扣字段

        //折扣不合法或是为空，都认为没折扣
        if (StringUtils.isBlank(discount) || !StringUtils.isNumeric(discount)) {
            discount = "10";//默认为10折
        }

        StringBuffer featrue = new StringBuffer();
        featrue.append("{\"opr\":").append(opr).append(",\"type\":\"").append(type).append("\"").append(",\"seq\":\"")
                .append(seq).append("\"").append(",\"productID\":\"").append(productID).append("\"")
                .append(",\"customerType\":\"").append(customerType).append("\"").append(",\"customerID\":\"")
                .append(customerID).append("\"").append(",\"oprCode\":\"").append(oprCode).append("\"")
                .append(",\"customerName\":\"").append(customerName).append("\"").append(",\"enterpriseId\":\"")
                .append(enterpriseId).append("\"").append(",\"discount\":\"").append(discount).append("\"");

        //包装需要更新的信息
        SupplierProduct newInfo = new SupplierProduct();
        newInfo.setCode(productID);
        newInfo.setDiscount(Integer.valueOf(discount) * 10);
        newInfo.setEnterpriseCode(enterpriseId);
        newInfo.setFeature(featrue.toString());

        OrderOperationEnum operation = OrderOperationEnum.getOperation(oprCode, type);
        List<SupplierProduct> newProducts = new ArrayList<SupplierProduct>();
        List<SupplierProduct> updateProducts = new ArrayList<SupplierProduct>();
        classifyOrders(allSupplierProducts, newProducts, updateProducts, operation);

        //根据不同操作设置不同属性
        setDefaultValue(newProducts, updateProducts, operation, newInfo);

        //执行操作
        return supplierProductService.createOrUpdateSupplierProduct(enterpriseId, newProducts, updateProducts);
    }

    private void classifyOrders(List<SupplierProduct> allProducts, List<SupplierProduct> newProducts,
            List<SupplierProduct> updateProducts, OrderOperationEnum operation) {
        //参数校验
        if (allProducts == null || allProducts.size() <= 0 || newProducts == null || updateProducts == null
                || operation == null) {
            return;
        }

        //变更操作需要更新同一USERID下的所有订单
        if (OrderOperationEnum.CHNAGE.equals(operation)) {
            //获取USERID相同的所有订单
            List<SupplierProduct> normalOrders = supplierProductService.selectByNameWithOutDeleteFlag(allProducts
                    .get(0).getName());
            for (SupplierProduct sp : normalOrders) {
                //删除状态的订单不更新：op_status = 1
                if (OrderOperationStatus.DELETE.getOpStatus().equals(sp.getOpStatus())) {
                    continue;
                } else {
                    updateProducts.add(sp);
                }
            }
        }

        //订单分类
        for (SupplierProduct supplierProduct : allProducts) {
            //新建、更新操作时，校验企业信息和产品信息			
            if (OrderOperationEnum.ORDER.equals(operation) || OrderOperationEnum.CHNAGE.equals(operation)) {//新增和更新操作
                //校验企业信息
                Enterprise enterprise = enterprisesService.selectByCode(supplierProduct.getEnterpriseCode());
                if (enterprise == null || enterprise.getDeleteFlag().intValue() != DELETE_FLAG.UNDELETED.getValue()) {
                    logger.error("订购和变更操作校验订单信息时失败，企业信息不存在：enterpriseId = " + supplierProduct.getEnterpriseCode());
                    newProducts.clear();
                    updateProducts.clear();
                    return;
                }

                //校验产品信息
                SdBossProduct sdBossProduct = sdBossProductService.selectByCode(supplierProduct.getCode());
                if (sdBossProduct == null) {
                    logger.error("订购和变更操作校验订单信息时失败，产品信息不存在：bizId = " + supplierProduct.getCode());
                    newProducts.clear();
                    updateProducts.clear();
                    return;
                }
            }

            //根据userID和bizId判断订单是否存在:正常状态
            List<SupplierProduct> normalSupplierProducts = supplierProductService.getByNameOrCodeOrOpStatus(
                    supplierProduct.getName(), supplierProduct.getCode(), OrderOperationStatus.NORMAL.getOpStatus());

            //根据userID和bizId判断订单是否存在：暂停状态
            List<SupplierProduct> pausedSupplierProducts = supplierProductService.getByNameOrCodeOrOpStatus(
                    supplierProduct.getName(), supplierProduct.getCode(), OrderOperationStatus.PAUSE.getOpStatus());

            if (OrderOperationEnum.ORDER.equals(operation)) {
                if ((normalSupplierProducts != null && normalSupplierProducts.size() > 0)
                        || (pausedSupplierProducts != null && pausedSupplierProducts.size() > 0)) {//订单存在(包括正常状态和暂停状态)则更新
                    logger.error("订购操作校验订单信息时失败，订单信息已存在：userId = " + supplierProduct.getName() + ", bizId = "
                            + supplierProduct.getCode());
                    newProducts.clear();
                    updateProducts.clear();
                    return;
                } else {//订单不存在，新增
                    newProducts.add(supplierProduct);
                }
            } else if (OrderOperationEnum.DELETE.equals(operation)) {
                if (normalSupplierProducts != null && normalSupplierProducts.size() > 0) {//订单存在(包括正常状态和暂停状态)则删除
                    updateProducts.addAll(normalSupplierProducts);
                }

                if (pausedSupplierProducts != null && pausedSupplierProducts.size() > 0) {//订单存在(包括正常状态和暂停状态)则删除
                    updateProducts.addAll(pausedSupplierProducts);
                }

                if ((normalSupplierProducts == null || normalSupplierProducts.size() <= 0)
                        && (pausedSupplierProducts == null || pausedSupplierProducts.size() <= 0)) {
                    logger.error("删除操作校验订单信息时失败，订单信息不存在：userId = " + supplierProduct.getName() + ", bizId = "
                            + supplierProduct.getCode());
                    newProducts.clear();
                    updateProducts.clear();
                    return;
                }
            } else if (OrderOperationEnum.CHNAGE.equals(operation)) {//变更操作：存在则更新，不存在则新增
                if (!((normalSupplierProducts != null && normalSupplierProducts.size() > 0) || (pausedSupplierProducts != null && pausedSupplierProducts
                        .size() > 0))) {//既不存在正常状态的订单、也不存在暂停状态的订单则新增订单
                    newProducts.add(supplierProduct);
                }
            } else if (OrderOperationEnum.PAUSE.equals(operation) || OrderOperationEnum.RECOVER.equals(operation)) {//暂停、恢复操作
                if (!OrderOperationStatus.DELETE.getOpStatus().equals(supplierProduct.getOpStatus())) {//已做删除操作的订单不更新
                    updateProducts.add(supplierProduct);
                } else {
                    logger.error("暂停或恢复操作时，订单信息已被删除：userId = " + supplierProduct.getName() + ", bizId = "
                            + supplierProduct.getCode());
                }
            }
        }
    }

    private void setDefaultValue(List<SupplierProduct> newProducts, List<SupplierProduct> updateProducts,
            OrderOperationEnum operation, SupplierProduct newInfo) {
        if (newProducts == null || updateProducts == null) {
            return;
        }

        Supplier supplier = supplierService.getByFingerPrint(FINGER_PRINT);

        //新增订单设置默认值
        for (SupplierProduct supplierProduct : newProducts) {
            supplierProduct.setIsp(ISP);
            supplierProduct.setOwnershipRegion(OWNERSHIP_REGION);
            supplierProduct.setRoamingRegion(ROAMING_REGION);
            if (supplier != null) {//供应商ID
                supplierProduct.setSupplierId(supplier.getId());
            }
            supplierProduct.setStatus(ProductStatus.NORMAL.getCode());//默认上架	
            supplierProduct.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());//默认未删除

            supplierProduct.setOpType(OrderOperationEnum.ORDER.getOprCode());//操作类型：opType=01(订购)
            supplierProduct.setOpStatus(OrderOperationStatus.NORMAL.getOpStatus());//操作状态：opStatus=0（正常）      
        }

        if (OrderOperationEnum.DELETE.equals(operation)) {//删除操作
            for (SupplierProduct sp : updateProducts) {
                //删除
                sp.setDiscount(newInfo.getDiscount());
                sp.setDeleteFlag(DELETE_FLAG.DELETED.getValue());

                sp.setOpType(OrderOperationEnum.DELETE.getOprCode());
                sp.setOpStatus(OrderOperationStatus.DELETE.getOpStatus());
            }
        } else if (OrderOperationEnum.CHNAGE.equals(operation)) {//变更操作  		
            for (SupplierProduct sp : updateProducts) {
                //设置折扣信息
                sp.setDiscount(newInfo.getDiscount());
                sp.setUpdateTime(new Date());

                sp.setOpType(OrderOperationEnum.CHNAGE.getOprCode());
            }

        } else if (OrderOperationEnum.PAUSE.equals(operation)) {//暂停操作
            for (SupplierProduct sp : updateProducts) {
                //设置折扣信息
                sp.setDiscount(newInfo.getDiscount());
                //暂停
                sp.setDeleteFlag(DELETE_FLAG.DELETED.getValue());
                sp.setUpdateTime(new Date());

                sp.setOpType(OrderOperationEnum.PAUSE.getOprCode());
                sp.setOpStatus(OrderOperationStatus.PAUSE.getOpStatus());
            }
        } else if (OrderOperationEnum.RECOVER.equals(operation)) {//恢复操作
            for (SupplierProduct sp : updateProducts) {
                //设置通用信息
                sp.setDiscount(newInfo.getDiscount());
                //恢复操作  
                sp.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
                sp.setUpdateTime(new Date());

                sp.setOpType(OrderOperationEnum.RECOVER.getOprCode());
                sp.setOpStatus(OrderOperationStatus.NORMAL.getOpStatus());
            }
        }
    }

    private List<SupplierProduct> jsonToSupplierProducts(String json) {
        List<SupplierProduct> supplierProducts = new ArrayList<SupplierProduct>();//供应商产品信息
        JSONObject jo = JSONObject.parseObject(json);
        String opr = jo.getString("opr");//接口类型：订单接口指定为order
        if (StringUtils.isBlank(opr) || !"order".equals(opr)) {
            logger.error("订单信息校验不通过：opr约定为order,实际为：" + opr);
            return null;
        }

        String type = jo.getString("type");
        String infoString = jo.getString("info");
        JSONObject info = JSONObject.parseObject(infoString);

        //需要更新的信息
        String seq = info.getString("seq");//订购编号
        String productID = info.getString("productID");//产品编号
        String customerType = info.getString("customerType");//客户类型
        String customerID = info.getString("customerID");//客户ID
        String oprCode = info.getString("oprCode");//操作码
        String customerName = info.getString("customerName");//客户名称
        String enterpriseId = info.getString("enterpriseId");//企业编号
        String discount = info.getString("discount");//0323新增的折扣字段
        String userID = info.getString("userID");//订单USERID		

        //折扣不合法或是为空，都认为没折扣
        if (StringUtils.isBlank(discount) || !StringUtils.isNumeric(discount)) {
            discount = "10";//默认为10折
        }

        OrderOperationEnum operation = OrderOperationEnum.getOperation(oprCode, type);
        if (operation == null) {
            logger.error("操作类型错误，期望的操作类型为：" + OrderOperationEnum.toMap().toString() + ",实际操作类型：oprCode =" + oprCode
                    + ",type = " + type);
            return null;
        }

        StringBuffer featrueOuter = new StringBuffer();
        featrueOuter.append("{\"opr\":\"").append(opr).append("\"").append(",\"type\":\"").append(type).append("\"")
                .append(",\"seq\":\"").append(seq).append("\"").append(",\"productID\":\"").append(productID)
                .append("\"").append(",\"customerType\":\"").append(customerType).append("\"")
                .append(",\"customerID\":\"").append(customerID).append("\"").append(",\"oprCode\":\"").append(oprCode)
                .append("\"").append(",\"customerName\":\"").append(customerName).append("\"")
                .append(",\"enterpriseId\":\"").append(enterpriseId).append("\"").append(",\"discount\":\"")
                .append(discount).append("\"");

        JSONArray array = info.getJSONArray("bizReq");

        if (OrderOperationEnum.PAUSE.equals(operation) || OrderOperationEnum.RECOVER.equals(operation)) {//暂停、恢复操作是批量更新(仅通过userId进行操作)，不涉及具体的订单
            supplierProducts = supplierProductService.selectByNameWithOutDeleteFlag(userID);
            if (supplierProducts == null || supplierProducts.size() <= 0) {
                logger.error("暂停或恢复操作失败：userID = " + userID + "不存在");
                return null;
            }
        } else {//订购、变更、删除操作涉及具体的订单信息（userId和bizId）
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    SupplierProduct supplierProduct = new SupplierProduct();
                    System.out.println("array:" + array.get(i));
                    String userIDInner = array.getJSONObject(i).getString("userID");//订单编号，主要订单变更关联变更信息的订单编号
                    String oldBizId = array.getJSONObject(i).getString("oldBizId");//规格编号			
                    String oldSpId = array.getJSONObject(i).getString("oldSpId");//企业编号
                    String bizId = array.getJSONObject(i).getString("bizId");//规格编号
                    String spId = array.getJSONObject(i).getString("spId");//企业编号
                    String chargingType = array.getJSONObject(i).getString("chargingType"); //计费类型

                    SdBossProduct sdBossProduct = sdBossProductService.selectByCode(bizId);
                    if (sdBossProduct == null) {
                        logger.error("产品编码不存在：bizId = " + bizId);
                        return null;
                    }

                    if (StringUtils.isNotBlank(userIDInner)) {
                        userID = userIDInner;
                    }

                    StringBuffer featrueInner = new StringBuffer();
                    featrueInner.append(",\"userID\":\"").append(userID).append("\"").append(",\"oldBizId\":\"")
                            .append(oldBizId).append("\"").append(",\"oldSpId\":\"").append(oldSpId).append("\"")
                            .append(",\"bizId\":\"").append(bizId).append("\"").append(",\"spId\":\"").append(spId)
                            .append("\"").append(",\"chargingType\":\"").append(chargingType).append("\"").append("}");

                    supplierProduct.setName(userID);
                    supplierProduct.setCode(bizId);
                    supplierProduct.setSize(sdBossProduct.getSize());
                    supplierProduct.setPrice(sdBossProduct.getPrice().intValue());
                    supplierProduct.setFeature(featrueOuter.toString() + featrueInner.toString());
                    supplierProduct.setCreateTime(new Date());
                    supplierProduct.setUpdateTime(new Date());
                    supplierProduct.setDiscount(Integer.valueOf(discount) * 10);
                    supplierProduct.setEnterpriseCode(enterpriseId);

                    if (!checkSupplierProduct(supplierProduct)) {
                        return null;
                    } else {
                        supplierProducts.add(supplierProduct);
                    }
                }
            } else {
                logger.error("bizReq字段为空：约定变更操作(UPDATE=03)、删除操作(DELETE=02)、订购操作(NEW=01)bizReq不能为空！");
            }
        }
        return supplierProducts;
    }

    private boolean checkSupplierProduct(SupplierProduct supplierProduct) {
        if (supplierProduct == null) {
            return false;
        } else if (StringUtils.isBlank(supplierProduct.getCode())) {
            logger.error("订单信息校验失败：产品编码bizId不能为空。");
            return false;
        } else if (StringUtils.isBlank(supplierProduct.getEnterpriseCode())) {
            logger.error("订单信息校验失败：企业编码enterpriseId不能为空。");
            return false;
        } else if (StringUtils.isBlank(supplierProduct.getName())) {
            logger.error("订单信息校验失败：userId不能为空。");
            return false;
        } else if (supplierProduct.getName().length() > MAX_LENGTH_64) {
            logger.error("订单信息校验失败：userId字段约定长度为：" + MAX_LENGTH_64 + ",实际为：" + supplierProduct.getName().length());
            return false;
        }
        return true;
    }

}
