package com.cmcc.vrp.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  @desc: 敏感词工具类
 *  @author: wuguoping 
 *  @data: 2017年7月20日
 */
public class SensitiveWordFilterUtil {
    private static final int MIN_MATCH_TYPE = 1;      //最小匹配规则
    
    /**
     * 获取文本中所包含的敏感词
     * title: getSensitiveWord
     * desc: 
     * @param txt
     * @param sensitiveWords
     * @param matchType
     * @return
     * wuguoping
     * 2017年7月20日
     */
    public static Set<String> getSensitiveWord(String txt, Map sensitiveWords, int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();
        for (int i = 0 ; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, sensitiveWords, matchType);    //判断是否包含敏感字符
            if (length > 0 ){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    
            }
        }
        return sensitiveWordList;
    }
    
    /**
     * 检查文字中是否包含敏感字符
     * title: checkSensitiveWord
     * desc: 
     * @param txt
     * @param beginIndex
     * @param sensitiveWords
     * @param matchType
     * @return
     * wuguoping
     * 2017年7月20日
     */
    private static int checkSensitiveWord(String txt, int beginIndex, Map sensitiveWords, int matchType){
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
                    if(MIN_MATCH_TYPE == matchType){    //最小规则，直接返回,最大规则还需继续查找
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
    
    /**
     * 初始化敏感詞容器
     * title: initKeyWordMap
     * desc: 
     * @param sensitiveWordsList
     * @return
     * wuguoping
     * 2017年7月20日
     */
    public static Map initKeyWordMap(Set<String> sensitiveWordsList){
//        Set<String> keyWordSet = new HashSet<String>(sensitiveWordsList);
        return addSensitiveWordToHashMap(sensitiveWordsList);       
    }
    
    /**
     * 思想：把关键词集合转化为“字典数”
     * title: addSensitiveWordToHashMap
     * desc: 
     * @param keyWordSet
     * @return
     * wuguoping
     * 2017年7月20日
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    private static Map addSensitiveWordToHashMap(Set<String> keyWordSet) {  
        Map sensitiveWordMap = new HashMap(keyWordSet.size()); 
        String key = null;    //当前关键词中的某个字符
        Map nowMap = null;     //
        Map<String, String> newWorMap = null;  
        Iterator<String> iterator = keyWordSet.iterator();  
        
        while(iterator.hasNext()){  
            key = iterator.next();    //关键字  
            nowMap = sensitiveWordMap;  
            for(int i = 0 ; i < key.length() ; i++){  
                char keyChar = key.charAt(i);       //转换成char型  ，取其中的一个字
                Object wordMap = nowMap.get(keyChar);       //获取该  keyChar 的 Entry
                  
                if(wordMap != null){        //如果key存在 ，说明以该key开头的tree存在，直接赋值就好
                    nowMap = (Map) wordMap;  
                } else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个  
                    newWorMap = new HashMap<String,String>();  
                    newWorMap.put("isEnd", "0");    //定义isEnd是否为这个敏感词的最后一个字     0:否，1：是  
                    nowMap.put(keyChar, newWorMap);  
                    nowMap = newWorMap;  
                }  
                  
                if(i == key.length() - 1){  
                    nowMap.put("isEnd", "1");    //最后一个  
                }  
            }  
        }  
        return sensitiveWordMap;
    }  
  
    /**
     * 去除特殊字符
     * title: stringFilter
     * desc: 
     * @param str
     * @return
     * wuguoping
     * 2017年7月20日
     */
    public static String stringFilter(String str){
        String regEx = "[^\u4E00-\u9FA5A-Za-z0-9]";
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    
    
    
}
