package com.cmcc.vrp.gd.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.gd.EntSrvStateReq;
import com.cmcc.vrp.ec.bean.gd.EntSrvStateResp;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "/gd")
public class EntSrvStateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntSrvStateController.class);
    private static XStream xstream;

    static {
        xstream = new XStream();
        xstream.alias("EntSrvStateReq", EntSrvStateReq.class);
        xstream.alias("EntSrvStateResp", EntSrvStateResp.class);
        xstream.autodetectAnnotations(true);
    }
    
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    EntStatusRecordService entStatusRecordService;
    @Autowired
    GlobalConfigService globalConfigService;
    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "entSrvState", method = RequestMethod.POST)
    @ResponseBody
    public void entSrvState(HttpServletRequest request, HttpServletResponse response) {
        Response result = new Response();
        Gson gson = new Gson();
        EntSrvStateResp entSrvStateResp = new EntSrvStateResp();
        EntSrvStateReq entSrvStateReq = parse(request);
        if (entSrvStateReq == null) {
            entSrvStateResp.setResult("1");
            entSrvStateResp.setResultMsg("解析报文出错");
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.setCode("1");
                result.setMsg(xstream.toXML(entSrvStateResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        if (!check(entSrvStateReq)) {
            entSrvStateResp.setResult("2");
            entSrvStateResp.setResultMsg("必要字段缺失或为空");
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.setCode("2");
                result.setMsg(xstream.toXML(entSrvStateResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        try {
            if (!handle(entSrvStateReq)) {
                entSrvStateResp.setResult("3");
                entSrvStateResp.setResultMsg("订购关系变更失败");
                try {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.setCode("3");
                    result.setMsg(xstream.toXML(entSrvStateResp));
                    StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e) {
                    LOGGER.error("响应出错，错误信息为{}.", e.toString());
                }
                return;
            }
        } catch (Exception e) {
            entSrvStateResp.setResult("4");
            entSrvStateResp.setResultMsg(e.getMessage());
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.setCode("4");
                result.setMsg(xstream.toXML(entSrvStateResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e1) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        
        entSrvStateResp.setResult("0");
        entSrvStateResp.setResultMsg("success");
        try {
            result.setCode("0");
            result.setMsg(xstream.toXML(entSrvStateResp));
            StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应出错，错误信息为{}.", e.toString());
        }
        return;

    }
    /**
     * @param entSrvStateReq
     * @return
     */
    @Transactional
    public boolean handle(EntSrvStateReq entSrvStateReq) {
        String entCode = entSrvStateReq.getEntCode();
        String optType = entSrvStateReq.getOptType();
        String prdOrdCode = entSrvStateReq.getPrdOrdCode();
        String opDesc = entSrvStateReq.getMemo();
        String reason = entSrvStateReq.getModiReason();
        List<EnterprisesExtInfo> enterprisesExtInfoList = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(entCode, prdOrdCode);
        if (enterprisesExtInfoList == null
                || enterprisesExtInfoList.size() == 0) {
            LOGGER.error("根据集团编码和集团产品编码获取不到订购关系");
            throw new TransactionException("根据集团编码和集团产品编码获取不到订购关系");
        }
        for(EnterprisesExtInfo enterprisesExtInfo:enterprisesExtInfoList) {
            Long entId = enterprisesExtInfo.getEnterId();
            Enterprise enterpriseOld = enterprisesService.selectById(entId);
            if (enterpriseOld == null) {
                LOGGER.error("根据集团编码和集团产品编码获取不到订购关系");
                throw new TransactionException("根据集团编码和集团产品编码获取不到订购关系");
            }
            Enterprise enterprise = new Enterprise();
            enterprise.setId(entId);
            if (!((enterpriseOld.getDeleteFlag()).equals(EnterpriseStatus.CONFIRM.getCode()))) {
                if ("2".equals(optType)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.OFFLINE.getCode());
                } else if ("3".equals(optType)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                } else if ("4".equals(optType)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
                } else if ("5".equals(optType)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                } else if ("6".equals(optType)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
                }
            }
            if (!enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                LOGGER.error("订购关系变更失败");
                return false;
            }
            EntStatusRecord entStatusRecord = new EntStatusRecord();
            entStatusRecord.setEntId(entId);//企业
            entStatusRecord.setCreateTime(new Date());
            entStatusRecord.setUpdateTime(new Date());
            entStatusRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            entStatusRecord.setPreStatus(enterpriseOld.getDeleteFlag());//企业先前状态，注：enterprise中deleteFlag表企业状态
            entStatusRecord.setNowStatus(enterprise.getDeleteFlag());
            entStatusRecord.setOpType(enterprise.getDeleteFlag());
            entStatusRecord.setReason(reason);
            entStatusRecord.setOpDesc(opDesc);

            if (!entStatusRecordService.insert(entStatusRecord)) {
                LOGGER.info("企业状态变更记录生成失败：" + new Gson().toJson(entStatusRecord));
                return false;
            }
        }
        return true;
    }
    private boolean check(EntSrvStateReq entSrvStateReq) {
        return !StringUtils.isEmpty(entSrvStateReq.getSiCode())
                && !StringUtils.isEmpty(entSrvStateReq.getOptType())
                && !StringUtils.isEmpty(entSrvStateReq.getEntCode())
                && !StringUtils.isEmpty(entSrvStateReq.getPrdOrdCode())
                && !StringUtils.isEmpty(entSrvStateReq.getOptTime())
                && !StringUtils.isEmpty(entSrvStateReq.getExecDate())
                && !StringUtils.isEmpty(entSrvStateReq.getModiReason())
                && !StringUtils.isEmpty(entSrvStateReq.getMemo());
    }
    private EntSrvStateReq parse(HttpServletRequest request) {
        String reqStr = null;
        try {
            reqStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
            LOGGER.info("从BOSS侧接收到订购关系状态变更信息， 信息内容为{}.", reqStr == null ? "空" : reqStr);
            return (EntSrvStateReq) xstream.fromXML(reqStr);
        } catch (Exception e) {
            LOGGER.error("解析回调参数时出错.");
            return null;
        }
    }

}
