package com.cmcc.vrp.province.webin.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.SensitiveWords;
import com.cmcc.vrp.province.service.SensitiveWordsService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月20日 下午2:28:00
*/
@Controller
@RequestMapping("/manage/sensitiveWords")
public class SensitiveWordsController extends BaseController{
    public static final int MAX_FILE_SIZE = 1;//单位MB
    private Logger logger = Logger.getLogger(SensitiveWordsController.class);

    @Autowired
    SensitiveWordsService sensitiveWordsService;
    
    /**
     * @return
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "sensitiveWords/index.ftl";
    }
    
    
    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping("sensitiveWordsList")
    public void sensitiveWordsList(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        String managerIdStr = getRequest().getParameter("managerId");
        if (!StringUtils.isEmpty(managerIdStr)) {
            Long managerId = NumberUtils.toLong(managerIdStr);
            if (!isParentOf(managerId)) { //当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
                res.setStatus(HttpStatus.SC_FORBIDDEN);
                return;
            }
        }
        
        setQueryParameter("name", queryObject);
        setQueryParameter("creatorName", queryObject);
        int sensitiveWordsCount = sensitiveWordsService.showForPageResultCount(queryObject);
        List<SensitiveWords> sensitiveWordsList = sensitiveWordsService.showSensitiveWordsForPageResult(queryObject);
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", sensitiveWordsList);
        json.put("total", sensitiveWordsCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * @param sensitiveWords
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "insertSensitiveWords")
    public void insertSensitiveWords(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {      
        //设置响应给前台内容的数据格式

        response.setContentType("text/plain; charset=UTF-8");
        List<String> list = new ArrayList<String>();
        InputStream is = null;
        if (file == null || file.getSize() == 0) {
            try {
                PrintWriter out = response.getWriter();

                String returnMsg = "上传文件不能为空";
                out.print("1;" + returnMsg);
                out.flush();
                return;

            } catch (IOException e) {
                logger.error("文件解析错误", e);
            }
        } else if (file.getSize() > MAX_FILE_SIZE * 1024 * 1024) {
            try {
                PrintWriter out = response.getWriter();

                String returnMsg = "敏感词文档过大";
                out.print("1;" + returnMsg);
                out.flush();
                return;

            } catch (IOException e) {
                logger.error("文件解析错误", e);
            }

        } else {
            try {

                PrintWriter out = response.getWriter();
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix.split("\\.").length - 1];
                }
                File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),
                        fileSuffix);
                file.transferTo(tempFile);

                is = new FileInputStream(tempFile);
                String type = codeString(tempFile);
                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    readText(list, is, type);
                } else {
                    //只支持文件
                    String returnMsg = "请上传正确格式文档";
                    is.close();
                    FileUtils.deleteQuietly(tempFile);
                    out.print("1;" + returnMsg);
                    out.flush();
                    return;
                }
                
                if (list.size() ==0) {
                    String returnMsg = "上传文件为空或所有敏感词已存在";
                    out.print("1;" + returnMsg);
                    out.flush();
                    return;
                }
                FileUtils.deleteQuietly(tempFile);
                Administer administer = getCurrentUser();
                List<SensitiveWords> sensitiveWordsList = new ArrayList<SensitiveWords>();
                for (String sensitiveWord:list) {
                    SensitiveWords sensitiveWords = new SensitiveWords();
                    sensitiveWords.setName(sensitiveWord);
                    sensitiveWords.setCreatorId(administer.getId());
                    sensitiveWordsList.add(sensitiveWords);
                }
                sensitiveWordsService.batchInsert(sensitiveWordsList);
            } catch (IOException e) {
                logger.error("上传文件异常", e);
            } catch (Exception e) {
                logger.error("上传文件格式异常", e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * @param response
     * @param request
     * @param modelMap
     */
    @RequestMapping("delete")
    public void delete(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
        Long id = NumberUtils.toLong(request.getParameter("id"));
        sensitiveWordsService.deleteById(id);
        try {
            modelMap.addAttribute("success", "删除成功!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            response.getWriter().flush();
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }
    /**
     * @param response
     * @param request
     * @param modelMap
     */
    @RequestMapping("edit")
    public String edit(HttpServletRequest request, ModelMap modelMap) {
        Long id = NumberUtils.toLong(request.getParameter("id"));
        String name = sensitiveWordsService.selectById(id).getName();
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("name", name);
        return "sensitiveWords/edit.ftl";
    }
    /**
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("update")
    public void update (HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
        Long id = NumberUtils.toLong(request.getParameter("id"));
        String newName = request.getParameter("name");
        Administer administer = getCurrentUser();  
        List<String> sensitiveWordsList = sensitiveWordsService.getAllSensitiveWords();
        try {
            if (!judge(newName, sensitiveWordsList)) {
                modelMap.addAttribute("failure", "敏感词已经存在");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            }
            if (sensitiveWordsService.updateById(id, newName, administer.getId())) {
                modelMap.addAttribute("success", "更新成功");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } else {
                modelMap.addAttribute("failure", "更新失败");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            }
        } catch (IOException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;  
        
    }
    
    /**
     * @param request
     * @param response
     * @param name
     * @param code
     * @param managerId
     * @param startTime
     * @param endTime
     */
    @RequestMapping("/creatCSVfile")
    public void creatPackageCSVfile(HttpServletRequest request, HttpServletResponse response, String name, String creatorName) {
        Map map = new HashMap();
        map.put("name", name);
        map.put("creatorName", creatorName);
        List<SensitiveWords> list = sensitiveWordsService.selectSensitiveWordsByMap(map);
        logger.info("导出信息：" + list);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> title = new ArrayList<String>();
        title.add("敏感词");
        title.add("创建人");
        title.add("创建时间");
        title.add("更新时间");
        List<String> rowList = new ArrayList<String>();
        for (SensitiveWords module : list) {
            rowList.add(module.getName());

            if (module.getCreatorName() != null) {
                rowList.add(module.getCreatorName());
            } else {
                rowList.add("");
            }

            if (module.getCreateTime() != null) {
                rowList.add(sdf.format(module.getCreateTime()));
            } else {
                rowList.add("");
            }
            if (module.getUpdateTime() != null) {
                rowList.add(sdf.format(module.getUpdateTime()));
            } else {
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "sensitiveWords.csv");
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
  * @param request
  * @param reponse
 * @throws IOException 
  */
    @RequestMapping("/checkSensitiveWords")
    public void checkSensitiveWords(HttpServletRequest request, HttpServletResponse reponse, ModelMap modelMap) throws IOException {
        Map<String, String[]> map = request.getParameterMap();      
        Iterator<String> iter = map.keySet().iterator();      
        List<String> sensitiveWordsList = sensitiveWordsService.getAllSensitiveWords();
        Map words = initKeyWord(sensitiveWordsList);
        
        boolean has = false;
        while(iter.hasNext()){
            String[] values = map.get(iter.next());
            for(String value : values){
                if(isContaintSensitiveWord(value, words, 1)){
                    has = true;
                }
            }
        }
        
        if(has){
            reponse.getWriter().write("false");
        }else{
            reponse.getWriter().write("true");
        }
        
    }
    
    /**
     * @param fileName
     * @return
     * @throws Exception
     */
    public String codeString(File tempFile) throws Exception{  
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(tempFile));  
        int p = (bin.read() << 8) + bin.read();  
        String code = null;  
          
        switch (p) {  
            case 0xefbb:  
                code = "UTF-8";  
                break;  
            case 0xfffe:  
                code = "Unicode";  
                break;  
            case 0xfeff:  
                code = "UTF-16BE";  
                break;  
            default:  
                code = "GBK";  
        }  
        bin.close();
        return code;  
    }  
    
    /**
     * 解析Txt文件,返回成功总行数
     * 
     * @param list
     * @param inputStream
     * @param type 
     * @return
     * @throws IOException
     */
    public int readText(List<String> list, InputStream inputStream, String type) throws IOException {
        List<String> sensitiveWordsList = sensitiveWordsService.getAllSensitiveWords();
        BufferedReader buReader = new BufferedReader(new InputStreamReader(inputStream, type));
        String in = null;
        int rowNums = 0;
        while ((in = buReader.readLine()) != null) {
            logger.info("敏感词:" + in);
            in = in.trim();
            rowNums++;
            if (judge(in, sensitiveWordsList)) {
                list.add(in);
            }
        }
        HashSet<String > h = new HashSet<String>(list);   
        list.clear();   
        list.addAll(h);   
        return rowNums;
    }
    private boolean judge(String in, List<String> sensitiveWordsList) {
        if (StringUtils.isEmpty(in)) {
            return false;
        }
        Map words = initKeyWord(sensitiveWordsList);
        return (in.length() <= 50) && !isContaintSensitiveWord(in, words, 1);
    }
    
    
    /**
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map initKeyWord(List<String> sensitiveWordsList){
        Set<String> keyWordSet = new HashSet<String>(sensitiveWordsList);
        return addSensitiveWordToHashMap(keyWordSet);       

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map addSensitiveWordToHashMap(Set<String> keyWordSet) {
        Map sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;  
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0 ; i < key.length() ; i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取
                
                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }                
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
        return sensitiveWordMap;
    }
    
    /**
     * 判断文字是否包含敏感字符
     * @param txt
     * @param matchType
     * @return
     */
    public boolean isContaintSensitiveWord(String txt, Map sensitiveWords, int matchType){
        boolean flag = false;
        for (int i = 0 ; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, sensitiveWords, matchType); //判断是否包含敏感字符
            if (matchFlag > 0 ){    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }
    

    /**
     * 获取文字中的敏感词
     * 
     * @param txt
     * @param matchType
     * @return
     */
    public Set<String> getSensitiveWord(String txt, Map sensitiveWords, int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();
        
        for (int i = 0 ; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, sensitiveWords, matchType);    //判断是否包含敏感字符
            if (length > 0 ){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        
        return sensitiveWordList;
    }
    

    /**
     * 替换敏感字字符
     * 
     * @param txt
     * @param matchType
     * @param replaceChar
     * @return
     */
    public String replaceSensitiveWord(String txt, Map sensitiveWords, int matchType, String replaceChar){
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, sensitiveWords, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        
        return resultTxt;
    }
    
    /**
     * 获取替换字符串
     * 
     * @param replaceChar
     * @param length
     * @return
     */
    private String getReplaceChars(String replaceChar, int length){
        String resultReplace = replaceChar;
        for(int i = 1 ; i < length ; i++){
            resultReplace += replaceChar;
        }
        
        return resultReplace;
    }
    
    /**
     * 检查文字中是否包含敏感字符
     * 
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return
     */
    @SuppressWarnings({ "rawtypes"})
    public int checkSensitiveWord(String txt, int beginIndex, Map sensitiveWords, int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWords;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1 
                if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true   
                    if(1 == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }else{     //不存在，直接返回
                break;
            }
        }
        if(!flag){        //长度必须大于等于1，为词 
            matchFlag = 0;
        }
        return matchFlag;
    }
}
