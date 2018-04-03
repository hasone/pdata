/**
 * 
 */
package com.cmcc.vrp.ec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: wuguoping
 * @data: 2017年5月9日
 */
@Controller
public class ECPlatform {

    /**
     * 
     * title: ecValidate
     * desc: 
     * @param modelMap
     * @return
     * wuguoping
     * 2017年5月31日
     */
    @RequestMapping("/validate")
    public String ecValidate(ModelMap modelMap) {
        return "/ec/ECTest.ftl";
    }
}
