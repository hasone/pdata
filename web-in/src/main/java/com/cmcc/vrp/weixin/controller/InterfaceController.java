package com.cmcc.vrp.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.wx.InviteService;
import com.cmcc.vrp.wx.beans.BindMsgPojo;

@Controller
@RequestMapping("/wx/api")
public class InterfaceController extends BaseController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonController.class);

    @Autowired
    WxAdministerService wxAdministerService;
    
    @Autowired
    InviteService inviteService;
    
    @Autowired
    IndividualAccountService individualAccountService;

    /** 
     * 接收微信侧发送过来的邀请信息
     * @Title: getInviteMsg 
     */
    @RequestMapping("getInviteMsg")
    public void getInviteMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("getInviteMsg");
        String result = "fail";
        BufferedReader bufferReader = request.getReader();//获取头部参数信息
        StringBuffer buffer = new StringBuffer();
        String line = " ";
        while ((line = bufferReader.readLine()) != null) {
            buffer.append(line);
        }
        String param = buffer.toString();
        LOGGER.info("getInviteMsg，request={}", param);
        //处理微信发送过来的邀请信息
        if(inviteService.processInvited(param)){
            result = "success";           
        }
        LOGGER.info("getInviteMsg，response={}", result);
        
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 接收微信侧发送过来的绑定信息
     * @Title: getInviteMsg 
     */
    @RequestMapping("getBindMsg")
    public void getBindMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("getBindMsg");
        String result = "fail";
        BufferedReader bufferReader = request.getReader();//获取头部参数信息
        StringBuffer buffer = new StringBuffer();
        String line = " ";
        while ((line = bufferReader.readLine()) != null) {
            buffer.append(line);
        }
        String param = buffer.toString();
        LOGGER.info("getBindMsg，request={}", param);
        //处理微信发送过来的邀请信息
        try{
            BindMsgPojo pojo = JSONObject.parseObject(param, BindMsgPojo.class);   
            //1、判断用户是否已经存在
            WxAdminister admin = wxAdministerService.selectByMobilePhone(pojo.getMobile());
            //2、插入用户
            if(admin == null){
                if(wxAdministerService.insertForWx(pojo.getMobile(), pojo.getOpenid())){
                    result = "success"; 
                }else{
                    LOGGER.info("插入用户失败，mobile={}, openid={}", pojo.getMobile(), pojo.getOpenid());
                }                
            }else{
                LOGGER.info("该用户已存在，mobile={}", pojo.getMobile());
                //已存在的用户要检查是否存在个人账户
                if(individualAccountService.checkAndInsertAccountForWx(admin.getId(), pojo.getOpenid())){
                    result = "success"; 
                }else{
                    LOGGER.info("用户检查并创建个人账户失败，mobile={}", pojo.getMobile());
                }
            }
        }catch(Exception e){
            LOGGER.error("出现异常={}", e.getMessage());
        }

        LOGGER.info("getBindMsg，response={}", result);
        
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
