/**
 * @Title: EntProQueryController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: qihang
 * @date: 2015年5月27日 下午4:49:45
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: EntProQueryController
 * @Description: 企业流量包查询Controller
 * @author: qihang
 * @date: 2015年5月27日 下午4:49:45
 */
@Controller
@RequestMapping("manage/entproquery")
public class EntProQueryController extends BaseController {
    private static final Logger logger = Logger
        .getLogger(EntProQueryController.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AdminEnterService adminEnterService;

    @Autowired
    private AdminRoleService adminRoleService;


    /**
     * @param modelMap
     * @param entId    非空则查指定企业，空则查所有企业，并在页面上显示第一个企业的所有产品信息，
     *                 entId只能查属于自己的企业，否则跳转到错误页面
     * @return
     * @throws
     * @Title:indexPlus
     * @Description: 实时流量包查询的主页
     * @author: qihang
     */
    @RequestMapping("indexPlus")
    public String indexPlus(ModelMap modelMap, QueryObject queryObject, Long entId) {

        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        // 显示当前登录用户的企业及所有产品
        Administer currentUser = getCurrentUser();
        if (currentUser == null) {
            return getLoginAddress();
        }

        Long roleId = adminRoleService.getRoleIdByAdminId(currentUser.getId());
        if (roleId == null || roleId.toString().equals(getSuperAdminRoleId())) {
            modelMap.addAttribute("errorMsg", "当前用户没有权限!");
            return "error.ftl";
        }

        List<Enterprise> enterprises = adminEnterService
            .selecteEntByAdminId(currentUser.getId());

        List<Product> products = null;
        Enterprise currentEnterprise = null;


        //通过前台是否传来entid得到相应的对象，前台不存在默认选择第一个
        if (enterprises.size() == 0) {
            products = new ArrayList<Product>();
        } else if (entId == null) {
            currentEnterprise = enterprises.get(0);
        } else {
            for (Enterprise enter : enterprises) {
                if (enter.getId().equals(entId)) {
                    currentEnterprise = enter;
                }
            }

            if (currentEnterprise == null) {//用于防止登陆用户访问不属于自己的企业
                modelMap.addAttribute("errorMsg", "对不起，您没有权限访问该企业");
                return "error.ftl";
            }
        }


        if (currentEnterprise != null) {
            products = productService.selectAllProductsByEnterId(currentEnterprise.getId());
        }

        /**
         * 获取企业
         */

        modelMap.addAttribute("enterprises", enterprises);

        modelMap.addAttribute("enter", currentEnterprise);

        /**
         * 获取当前用户角色
         */
        if (roleId.toString().equals(getENTERPRISE_CONTACTOR())) {
            modelMap.addAttribute("role", "ENTERPRISE_CONTACTOR");
        } else {
            modelMap.addAttribute("role", "CUSTOM_MANAGER");
        }

        /**
         * 所有产品
         */
        modelMap.addAttribute("products", products);

        return "enterProQuery/enterProQueryPlus.ftl";
    }

    /**
     * 确认查找的集团号和产品号是否是登陆人所有的，是则返回"",否返回错误信息
     */
    private String confirmValid(Administer administer, String enterpriseCode, Product product) {
        Long roleId = adminRoleService.getRoleIdByAdminId(administer.getId());
        if (roleId == null || roleId.toString().equals(getSuperAdminRoleId())) {
            return "对不起，超级管理员不能做相关操作";
        }

        List<Enterprise> enterprises = adminEnterService
            .selecteEntByAdminId(administer.getId());

        if (enterprises == null || enterprises.size() == 0) {
            return "对不起,您没有相关企业";
        }

        for (Enterprise enter : enterprises) {
            if (enter.getCode().equals(enterpriseCode)) {//找到了用户提供的企业编码
                List<Product> products = productService.selectAllProductsByEnterId(enter.getId());
                if (ListContainProduct(products, product)) {
                    return "";
                }
            }
        }

        return "对不起，您无权查看您提供的产品的存量";

    }

    /**
     * 确定某一product是否在List<Product>当中，是则返回true
     */
    private boolean ListContainProduct(List<Product> products, Product product) {
        if (products == null || products.size() == 0) {
            return false;
        }
        for (Product pro : products) {
            if (pro.getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }
}
