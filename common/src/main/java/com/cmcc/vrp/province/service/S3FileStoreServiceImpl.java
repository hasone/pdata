package com.cmcc.vrp.province.service;

import com.cmcc.vrp.util.S3Until;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

/**
 * 使用S3来存储文件
 *
 * Created by sunyiwei on 2017/4/17.
 */
public class S3FileStoreServiceImpl implements FileStoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3FileStoreServiceImpl.class);

    private S3Until s3Until = null;

    @Value("#{settings['s3.AWS_ACCESS_KEY']}")
    private String s3AccessKey;

    @Value("#{settings['s3.AWS_SECRET_KEY']}")
    private String s3SecretKey;

    @Value("#{settings['s3.HOST']}")
    private String host;

    @Value("#{settings['s3.bucketName']}")
    private String bucketName;

    @Value("#{settings['s3.useHttps']}")
    private Boolean useHttps = true;

    @PostConstruct
    private void init() {
        //初始化s3对象
        s3Until = new S3Until(s3AccessKey, s3SecretKey, host, bucketName, useHttps);
    }

    /**
     * 保存指定的文件流,并返回相应的key
     *
     * @param inputStream 输入流
     * @return 保存的文件对应的key, 后续可以通过调用get(key)来获取到该文件, 保存失败则返回null
     */
    @Override
    public String save(InputStream inputStream) {
        String key = UUID.randomUUID().toString();
        if (doSave(key, inputStream)) {
            return key;
        }

        return null;
    }

    /**
     * 删除以指定前缀的key值指定的文件
     *
     * @param prefix 前缀
     */
    @Override
    public boolean deleteKeysStartWith(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return false;
        }

        s3Until.deleteObjectsWithPrefix(prefix);
        return true;
    }

    /**
     * 校验某个key的文件是否存在
     *
     * @param key key值
     */
    @Override
    public boolean exist(String key) {
        return StringUtils.isNotBlank(key) && s3Until.checkExist(key);
    }

    /**
     * 获取以指定prefix开始的key值
     */
    @Override
    public List<String> getKeysStartWith(String prefix) {
        return StringUtils.isBlank(prefix) ? null : s3Until.listStartWithKeys(prefix);
    }

    /**
     * 使用指定的key值来保存文件
     *
     * @param key  key值
     * @param file 需要上传的文件
     */
    @Override
    public boolean save(String key, File file) {
        if (StringUtils.isBlank(key) || file == null) {
            return false;
        }

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return doSave(key, is);
        } catch (FileNotFoundException e) {
            LOGGER.error("打开文件对象时出错,文件名称为{},错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入流时时出错,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
                }
            }
        }

        return false;
    }

    //使用指定的key保存inputStream
    private boolean doSave(String key, InputStream inputStream) {
        if (StringUtils.isBlank(key) || inputStream == null) {
            return false;
        }

        try {
            s3Until.uploadFile(key, inputStream);
            return true;
        } catch (Exception e) {
            LOGGER.error("往S3中写入流对象内容时出错,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return false;
    }

    /**
     * 保存指定的文件,并返回相应的key
     *
     * @param file 需要上传的文件
     * @return 保存的文件对应的key, 后续可以通过调用get(key)来获取到该文件, 保存失败则返回null
     */
    @Override
    public String save(File file) {
        if (file == null) {
            return null;
        }

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return save(is);
        } catch (FileNotFoundException e) {
            LOGGER.error("上传文件[Filename={}]时出错,错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入流时时出错,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
                }
            }
        }

        return null;
    }

    /**
     * 从文件存储系统中获取指定的文件
     *
     * @param key 文件对应的key, 该key值由save()方法返回
     * @return 对应的文件流, 空则为null
     */
    @Override
    public InputStream get(String key) {
        if (StringUtils.isBlank(key)) {
            LOGGER.debug("无效的key值.");
            return null;
        }

        return s3Until.readFileFromS3(key);
    }
}
