package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: MdrcCardmakerController
 * @Description: 制卡商管理控制器
 * @author: Rowe
 * @date: 2016年5月20日 上午10:56:59
 */
@Controller
@RequestMapping("/manage/mdrc/cardmaker")
public class MdrcCardmakerController extends BaseController {
    /**
     * key文件名称
     */
    public static final String KEY_FILE_SUFFIX = ".key";
    @Autowired
    MdrcCardmakerService mdrcCardMakerService;
    @Autowired
    AdministerService administerService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    XssService xssService;
    private Logger logger = Logger.getLogger(getClass());

    /**
     * @param modelMap
     * @param id
     * @return
     * @Title: detail
     * @Description: 制卡商详情
     * @return: String
     */
    @RequestMapping("detail")
    public String detail(ModelMap modelMap, long id) {
        MdrcCardmaker record = mdrcCardMakerService.selectByPrimaryKey(id);
        modelMap.addAttribute("record", record);
        return "mdrcCardmaker/detail.ftl";
    }

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: index
     * @Description: 制卡商列表
     * @return: String
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        
        if(queryObject == null){
            queryObject = new QueryObject();
        }
        
        //制卡商名称
        //setQueryParameter("name", queryObject);
        //序列号
        //setQueryParameter("serialNumber", queryObject);

        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        modelMap.addAttribute("year",
            queryObject.getQueryCriterias().get("year"));

        modelMap.addAttribute("currentUserID", getCurrentUser().getId());//当前用户ID
        return "mdrcCardmaker/index.ftl";
    }

    /**
     * @param queryObject
     * @param res
     * @Title: search
     * @Description: 制卡商搜索
     * @return: void
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        //制卡商名称
        setQueryParameter("name", queryObject);
        //序列号
        setQueryParameter("serialNumber", queryObject);

        int count = mdrcCardMakerService.count(queryObject);
        List<MdrcCardmaker> list = mdrcCardMakerService.list(queryObject);

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
     * @param modelMap
     * @return
     * @Title: create
     * @Description: 新建制卡商
     * @return: String
     */
    @RequestMapping("create")
    public String create(ModelMap modelMap) {
        // 查询出未绑定为专员的用户
        List<Administer> admins = mdrcCardMakerService.selectUnboundCardmaker(getAuthCodeMdrcDowload());
        modelMap.addAttribute("admins", admins);
        return "mdrcCardmaker/create.ftl";
    }

    private boolean validName(String name, ModelMap modelMap) {
        // 若校验失败，返回失败信息
        if (StringUtils.isBlank(name)) {
            modelMap.addAttribute("errorMessage", "制卡商名称为空。");
            return false;
        } else if (name.length() > 64) {
            modelMap.addAttribute("errorMessage", "制卡商名称过长。");
            return false;
        } else if (name.contains(" ")) {
            modelMap.addAttribute("errorMessage", "制卡商名称不允许存在空格。");
            return false;
        }

        // 记录已存在
        int exists = mdrcCardMakerService.countByName(name);
        if (exists > 0) {
            modelMap.addAttribute("errorMessage", "制卡商名称已存在。");
            return false;
        }
        return true;
    }

    private boolean validForSave(Administer operator, String name,
                                 int serialNumber, ModelMap modelMap) {
        // 若校验失败，返回失败信息
        if (operator == null || serialNumber >= 100) {
            modelMap.addAttribute("errorMessage", "提交失败。");
            return false;
        }
        return validName(name, modelMap);
    }

    /**
     * @param modelMap
     * @param name
     * @param operatorId
     * @return
     * @Title: save
     * @Description: 保存新建的制卡商
     * @return: String
     */
    @RequestMapping("save")
    public String save(ModelMap modelMap, String name, String operatorId) {
        
        name = xssService.stripQuot(name);
        
        modelMap.addAttribute("name", name);

        // 取得最新的序列号
        int serialNumber = mdrcCardMakerService.getNewSerialNumber();

        // 查询专员用户信息
        long oid = NumberUtils.toLong(operatorId, -1);
        Administer operator = administerService.selectAdministerById(oid);

        // 若校验失败，返回失败信息
        if (!validForSave(operator, name, serialNumber, modelMap)) {
            return create(modelMap);
        }

        // 构造制卡商信息
        MdrcCardmaker cardmaker = new MdrcCardmaker();
        cardmaker.setCreateTime(new Date());
        cardmaker.setName(name);
        cardmaker.setCreatorId(getCurrentUser().getId());
        cardmaker.setOperatorId(oid);

        String sn = String.valueOf(serialNumber);
        if (serialNumber < 10) {
            sn = "0" + sn;
        }
        cardmaker.setSerialNumber(sn);
        cardmaker.setDeleteFlag(0);

        // 保存到数据库
        mdrcCardMakerService.insert(cardmaker);

        return "redirect:/manage/mdrc/cardmaker/index.html";
    }

    /**
     * @param modelMap
     * @param id
     * @param request
     * @param response
     * @throws IOException
     * @Title: downloadKeyFile
     * @Description: 秘钥下载
     * @return: void
     */
    @RequestMapping("dlKeyFile")
    @ResponseBody
    public void downloadKeyFile(ModelMap modelMap, long id,
                                HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        // 根据当前用户标识和记录标识查询制卡商信息
        MdrcCardmaker cardmaker = mdrcCardMakerService.selectWithKeys(id);

        // 若没有查询到结果，返回403
        if (cardmaker == null || StringUtils.isBlank(cardmaker.getPublicKey())) {
            response.sendError(403);
        } else {

            // 获得密钥内容
            String pk = cardmaker.getPublicKey();

            // 获得文件名称
            String fileName = URLEncoder.encode(
                getKeyFileName(cardmaker.getName()), "utf-8");

            // 设置响应报文头
            response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + fileName);
            response.addHeader("Content-Length", "" + pk.length());

            // 输出字符流
            response.getWriter().write(pk);
            response.getWriter().flush();
        }
    }

    /**
     * @param cardmakerName
     * @return
     * @Title: getKeyFileName
     * @Description: 获取密钥文件名称
     * @return: String
     */
    private String getKeyFileName(String cardmakerName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        String date = dateFormat.format(new Date());

        if (StringUtils.isBlank(cardmakerName)) {
            return date + KEY_FILE_SUFFIX;
        }
        return cardmakerName + "_" + date + KEY_FILE_SUFFIX;
    }

    /**
     * @param modelMap
     * @param id
     * @return
     * @Title: edit
     * @Description: 编辑制卡商
     * @return: String
     */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, long id) {
        // 根据当前用户标识和记录标识查询制卡商信息
        MdrcCardmaker cardmaker = mdrcCardMakerService.selectWithKeys(id);

        // 查询出未绑定为专员的用户
        List<Administer> admins = mdrcCardMakerService.selectUnboundCardmaker(getAuthCodeMdrcDowload());

        // 将当前的专员加入候选列表
        Administer currentOperator = administerService.selectAdministerById(cardmaker.getOperatorId());
        admins.add(currentOperator);

        modelMap.addAttribute("admins", admins);
        modelMap.addAttribute("record", cardmaker);

        modelMap.addAttribute("currentOperator", currentOperator);

        return "mdrcCardmaker/edit.ftl";
    }

    /**
     * @param modelMap
     * @param id
     * @param name
     * @param operatorId
     * @return
     * @Title: saveEdit
     * @Description: 保存编辑后的制卡商
     * @return: String
     */
    @RequestMapping("saveEdit")
    public String saveEdit(ModelMap modelMap, long id, String name, String operatorId) {
        name = xssService.stripQuot(name);
        MdrcCardmaker cardmaker = mdrcCardMakerService.selectByPrimaryKey(id);
        if (cardmaker == null) {
            modelMap.addAttribute("errorMessage", "制卡商信息有误！");
            return edit(modelMap, id);
        }

        // 查询专员用户信息
        long oid = NumberUtils.toLong(operatorId, -1);
        Administer operator = administerService.selectAdministerById(oid);

        // 若校验失败，返回失败信息
        if (operator == null ||
            (!cardmaker.getName().equals(name) && !validName(name, modelMap)
            )) {
            return edit(modelMap, id);
        }

        // 构造更新对象
        MdrcCardmaker toBeUpdated = new MdrcCardmaker();
        toBeUpdated.setId(cardmaker.getId());
        toBeUpdated.setName(name);
        toBeUpdated.setOperatorId(oid);

        mdrcCardMakerService.update(toBeUpdated);

        return "redirect:/manage/mdrc/cardmaker/index.html";
    }

    /**
     * @param id
     * @param model
     * @param response
     * @throws IOException
     * @Title: delete
     * @Description: 删除制卡商
     * @return: void
     * edit by qinqinyan on 2017/3/15 将物理删除修改为逻辑删除
     */
    @RequestMapping("delete")
    public String delete(Long id, ModelMap model, HttpServletResponse response) throws IOException {
        //MdrcCardmaker cardmaker = mdrcCardMakerService.selectWithKeys(id);
        if (mdrcCardMakerService.deleteCardmaker(id)) {
            logger.info("删除制卡商成功,id="+id);
        }
        return "redirect:index.html";
    }

    /**
     * @param request
     * @param response
     * @param m
     * @throws IOException
     * @Title: checkName
     * @Description: 唯一性校验：制卡商名称
     * @return: void
     */
    @RequestMapping(value = "checkName")
    public void checkName(HttpServletRequest request,
                          HttpServletResponse response, String name, Long id) throws IOException {
        Boolean flag = true;
        //只校验名称
        MdrcCardmaker temp = new MdrcCardmaker();
        temp.setName(name);
        temp.setId(id);

        if (!mdrcCardMakerService.checkUnique(temp)) {
            flag = false;
        }
        response.getWriter().write(flag.toString());
    }

    public String getAuthCodeMdrcDowload() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_DOWNLOAD_AUTH_CODE.getKey());
    }
}