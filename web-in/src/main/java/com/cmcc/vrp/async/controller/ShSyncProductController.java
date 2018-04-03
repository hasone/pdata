package com.cmcc.vrp.async.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductOnlineService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年3月24日 下午1:34:24
*/
@Controller
public class ShSyncProductController {
    private static final Logger logger = LoggerFactory.getLogger(ShSyncProductController.class);
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ProductOnlineService productOnlineService;
    
    /** 
    * @Title: syncProduct 
    * @Description:
    * @param request
    * @param response
    * @param entCode 
    * @throws 
    */
    @RequestMapping(value = "/shSyncProduct", method = RequestMethod.GET)
    public void syncProduct(HttpServletRequest request, HttpServletResponse response,
            String entCode) {
        Enterprise enterprise;
        if ((enterprise = enterprisesService.selectByCode(entCode)) == null) {
            logger.error("企业不存在entcode{}", entCode);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!productOnlineService.isOnlineProduct(enterprise.getId())) {
            logger.error("入队列失败，entcode{}", entCode);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}
