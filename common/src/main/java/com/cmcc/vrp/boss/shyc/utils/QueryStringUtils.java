package com.cmcc.vrp.boss.shyc.utils;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignAlias;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名生成器
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class QueryStringUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryStringUtils.class);

    /**
     * 为对象生成相应的请求字符串,包括生成签名等内容
     *
     * @param obj 要生成请求字符串的对象
     * @return 返回格式为?key1=value1&key2=value2的字符串
     */
    public static String queryString(Object obj, String apiKey) {
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //1. 首先填充sign字段
        if (!fillSign(obj, apiKey)) {
            LOGGER.error("为{}类对象生成签名时返回false, 无法生成请求字符串.", clazz.getName());
            return null;
        }

        //2. 生成queryString
        Map<String, Object> map = toMap(fields, obj);
        if (map == null) {
            return null;
        }

        return "?" + concatInternal(map);
    }


    /**
     * 为对象生成相应的签名
     *
     * @param obj    要生成签名的对象
     * @param apiKey 签名使用的apikey
     * @return 如果生成签名成功, 则返回true, 否则false
     */
    private static boolean fillSign(Object obj, String apiKey) {
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Field[] needSignFields = filterFields(fields, NeedSign.class);
        Field[] signTargetFields = filterFields(fields, SignTarget.class);
        if (needSignFields.length == 0 || signTargetFields.length != 1) { //只能有一个signTarget属性,且必须要有needSign属性
            LOGGER.error("不存在需要签名的属性,或不存在签名值的目标属性.请查看相应的类定义. ClassName = {}", clazz.getName());
            return false;
        }

        //按字母顺序排序
        Map<String, Object> map = toMap(needSignFields, obj);
        if (map == null) {
            return false;
        }

        //计算签名值
        String concatString = concatInternal(map) + "&key=" + apiKey;
        String signString = DigestUtils.md5Hex(concatString).toLowerCase();

        try {
            signTargetFields[0].setAccessible(true);
            signTargetFields[0].set(obj, signString);
        } catch (IllegalAccessException e) {
            LOGGER.error("设置{}类的{}属性时抛出异常,异常信息为{}, 异常堆栈为{}.", clazz.getName(), signTargetFields[0].getName(), e.getMessage(), e.getStackTrace());
            return false;
        }

        return true;
    }

    //将field数组组织成map
    private static Map<String, Object> toMap(Field[] fields, Object obj) {
        Class clazz = obj.getClass();

        //按字母顺序排序
        Map<String, Object> map = new TreeMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);

            try {
                map.put(getSignName(field), field.get(obj));
            } catch (IllegalAccessException e) {
                LOGGER.error("获取{}类的{}属性时抛出异常,异常信息为{}, 异常信息为{}.", clazz.getName(), field.getName(), e.getMessage(), e.getStackTrace());
                return null;
            }
        }

        return map;
    }

    /**
     * 将Map对象以key1=value&key2=value的形式返回
     */
    private static String concatInternal(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append("&").append(key).append("=").append(map.get(key));
        }

        return stringBuilder.substring(1);
    }

    //获取属性参与签名的名称,默认为属性名称,如果设置了SignAlias注解,则使用这个注解
    private static String getSignName(Field field) {
        SignAlias annotation = field.getAnnotation(SignAlias.class);

        String result = "";
        if (annotation == null || StringUtils.isBlank(annotation.value())) {
            result = field.getName();
        } else {
            result = annotation.value();
        }

        return result.toLowerCase();
    }

    //过滤有needSign标签的字段
    private static Field[] filterFields(Field[] fields, Class targetAnnotationClazz) {
        List<Field> fs = new LinkedList<Field>();
        for (Field field : fields) {
            if (field.getAnnotation(targetAnnotationClazz) != null) {
                fs.add(field);
            }
        }

        return convert(fs);
    }

    private static Field[] convert(List<Field> fs) {
        Field[] fsArray = new Field[fs.size()];

        int index = 0;
        for (Field f : fs) {
            fsArray[index++] = f;
        }

        return fsArray;
    }
}
