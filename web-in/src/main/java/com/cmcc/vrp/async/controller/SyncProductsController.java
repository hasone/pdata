package com.cmcc.vrp.async.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.SyncProReq;
import com.cmcc.vrp.ec.bean.SyncProReqData;
import com.cmcc.vrp.ec.bean.SyncProResp;
import com.cmcc.vrp.ec.bean.SyncProRespData;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.DateUtil;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
public class SyncProductsController {
    private static final Logger logger = LoggerFactory.getLogger(SyncProductsController.class);
	
    private XStream xStream;
	
    @Autowired
	EnterprisesService enterprisesService;
	
    @Autowired
	ProductService productService;
    
    @Autowired
    EntSyncListService entSyncListService;
    

	
    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", SyncProReq.class);
        xStream.alias("Response", SyncProResp.class);
        xStream.autodetectAnnotations(true);
    }
	
	/**
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "syncProducts", method = RequestMethod.POST)
	@ResponseBody
	public void syncProducts(HttpServletRequest request, HttpServletResponse response) {
        String appKey = null;
        SyncProReq req = null;
        Enterprise enterprise = null;;
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
        		|| (req = retrieveChargeReq(request)) == null
        		|| !validate(req)
        		|| (enterprise = enterprisesService.selectByAppKey(appKey)) == null
        		|| enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
            logger.error("无效的同步请求参数, AppKey = {}, Enterprise = {}.", appKey, enterprise == null ? "" : JSONObject.toJSONString(enterprise));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        SyncProReqData syncProReqData = req.getSyncProReqData();
        if (syncProReqData.getEnterpriseId().length() > 255
        		|| syncProReqData.getEntProCode().length() > 64) {
            response.setStatus(422);
            return;
        }
        
        Long entId = NumberUtils.toLong(syncProReqData.getEnterpriseId());
        enterprise = enterprisesService.selectById(entId);
        String enterProCode = syncProReqData.getEntProCode();
        if (enterprise == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
    	try {
    	    EntSyncList entSyncList = null;
    	    if ((entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, enterProCode)) == null) {
    	        entSyncList = new EntSyncList();
    	        entSyncList.setEntId(entId);
    	        entSyncList.setEntProductCode(enterProCode);
    	        entSyncList.setStatus(1);
    	        entSyncList.setSyncInfo("同步成功");
    	        entSyncList.setDeleteFlag(0);
    	        entSyncListService.insert(entSyncList);
    	    } else {
    	    	Long endSyncLsitId = entSyncList.getId();
    	    	EntSyncList updateEntSyncList = new EntSyncList();
    	    	updateEntSyncList.setId(endSyncLsitId);
    	    	updateEntSyncList.setStatus(1);
    	    	updateEntSyncList.setSyncInfo("同步成功");
    	    	entSyncListService.updateSelective(updateEntSyncList);
    	    }   		
    	    productService.synPrdsWithSupplierPro(enterprise.getCode(), enterProCode);
    	    entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, enterProCode);
            SyncProResp syncProResp = new SyncProResp();
            SyncProRespData syncProRespData = new SyncProRespData();
            syncProRespData.setSyncCode("0000");
            syncProRespData.setSyncInfo(entSyncList.getSyncInfo());
            syncProResp.setResponseTime(DateUtil.getRespTime());
            syncProResp.setSyncProRespData(syncProRespData);
            try {
                StreamUtils.copy(xStream.toXML(syncProResp), Charsets.UTF_8, response.getOutputStream());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
    	} catch (TransactionException transactionException) {
    	    EntSyncList entSyncList = null;
    	    if ((entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, enterProCode)) != null) {
    	        Long endSyncLsitId = entSyncList.getId();
    	        EntSyncList updateEntSyncList = new EntSyncList();
    	        updateEntSyncList.setId(endSyncLsitId);
    	        updateEntSyncList.setStatus(0);
    	        updateEntSyncList.setSyncInfo(transactionException.getMessage());
    	        entSyncListService.updateSelective(updateEntSyncList);
    	    } else {
    	    	entSyncList = new EntSyncList();
    	    	entSyncList.setEntId(entId);
    	    	entSyncList.setEntProductCode(enterProCode);
    	    	entSyncList.setStatus(0);
    	    	entSyncList.setSyncInfo(transactionException.getMessage());
    	    	entSyncList.setDeleteFlag(0);
    	    	entSyncListService.insert(entSyncList);
    	    }
    	    SyncProResp syncProResp = new SyncProResp();
            SyncProRespData syncProRespData = new SyncProRespData();
            syncProRespData.setSyncCode("1000");
            syncProRespData.setSyncInfo(entSyncList.getSyncInfo());
            syncProResp.setResponseTime(new DateTime().toString());
            syncProResp.setSyncProRespData(syncProRespData);
            try {
            	StreamUtils.copy(xStream.toXML(syncProResp), Charsets.UTF_8, response.getOutputStream());
            	return;
            } catch (IOException e) {
            	e.printStackTrace();
            }
    	}  
        		 
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//同步失败
    }
	
    private SyncProReq retrieveChargeReq(HttpServletRequest request) {
    	try {
            String syncProReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (SyncProReq) xStream.fromXML(syncProReqStr);
    	} catch (Exception e) {
    	    return null;
    	}
    }
    
    private boolean validate(SyncProReq req) {
    	SyncProReqData reqData;

        return req != null
            && (reqData = req.getSyncProReqData()) != null
            && StringUtils.isNotBlank(reqData.getEnterpriseId())
            && StringUtils.isNotBlank(reqData.getEntProCode());
    }  

}
