package com.cmcc.vrp.province.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 文件存储服务
 *
 * Created by sunyiwei on 2017/4/17.
 */
public interface FileStoreService {
    /**
     * 保存指定的文件流,并返回相应的key
     *
     * @param inputStream 输入流
     * @return 保存的文件对应的key, 后续可以通过调用get(key)来获取到该文件, 保存失败则返回null
     */
    String save(InputStream inputStream);

    /**
     * 保存指定的文件,并返回相应的key
     *
     * @param file 需要上传的文件
     * @return 保存的文件对应的key, 后续可以通过调用get(key)来获取到该文件, 保存失败则返回null
     */
    String save(File file);

    /**
     * 使用指定的key值来保存文件
     *
     * @param key  key值
     * @param file 需要上传的文件
     */
    boolean save(String key, File file);

    /**
     * 校验某个key的文件是否存在
     *
     * @param key key值
     */
    boolean exist(String key);

    /**
     * 获取以指定prefix开始的key值
     */
    List<String> getKeysStartWith(String prefix);

    /**
     * 删除以指定前缀的key值指定的文件
     *
     * @param prefix 前缀
     */
    boolean deleteKeysStartWith(String prefix);


    /**
     * 从文件存储系统中获取指定的文件
     *
     * @param key 文件对应的key, 该key值由save()方法返回
     * @return 对应的文件流, 空则为null
     */
    InputStream get(String key);
}
