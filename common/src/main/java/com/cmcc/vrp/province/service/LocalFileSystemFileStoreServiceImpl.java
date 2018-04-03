package com.cmcc.vrp.province.service;

import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 使用本地的文件系统来存储文件
 *
 * Created by sunyiwei on 2017/4/17.
 */
public class LocalFileSystemFileStoreServiceImpl implements FileStoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemFileStoreServiceImpl.class);


    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 使用指定的key值来保存文件
     *
     * @param key  key值
     * @param file 需要上传的文件
     */
    @Override
    public boolean save(String key, File file) {
        try {
            return StringUtils.isNotBlank(key)
                    && file != null
                    && doSave(key, new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            LOGGER.error("读取文件时抛出异常, 文件名称为{}, 错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
        }

        return false;
    }

    /**
     * 校验某个key的文件是否存在
     *
     * @param key key值
     */
    @Override
    public boolean exist(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }

        File file = getFile(key);
        return file != null && file.isFile() && file.exists();
    }

    /**
     * 获取以指定prefix开始的key值
     */
    @Override
    public List<String> getKeysStartWith(final String prefix) {
        File[] files = prefixFilter(prefix);
        return files != null ? convert(files) : null;
    }

    /**
     * 删除以指定前缀的key值指定的文件
     *
     * @param prefix 前缀
     */
    @Override
    public boolean deleteKeysStartWith(String prefix) {
        File[] files = prefixFilter(prefix);
        return files != null && deleteAll(files);
    }

    //过滤文件
    private File[] prefixFilter(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return null;
        }

        File dir = getFile();
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return null;
        }

        //得到文件夹下的文件
        return dir.listFiles((FilenameFilter) new PrefixFileFilter(prefix));
    }

    private boolean deleteAll(File[] files) {
        boolean flag = true;
        for (File file : files) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                LOGGER.error("删除文件时抛出异常,文件名称为{},错误信息为{}, 错误堆栈为{}", file.getName(), e.getMessage(), e.getStackTrace());
                flag = false;
            }
        }

        return flag;
    }


    private List<String> convert(File[] files) {
        List<String> result = new LinkedList<String>();
        for (File file : files) {
            result.add(file.getName());
        }

        return result;
    }

    /**
     * 保存指定的文件流,并返回相应的key
     *
     * key值即为文件系统中保存的名称
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

    //使用指定的Key来保存InputStream流
    private boolean doSave(String key, InputStream inputStream) {
        if (StringUtils.isBlank(key) || inputStream == null) {
            return false;
        }

        File file = getFile(key);
        if (file == null) {
            return false;
        }

        try {
            StreamUtils.copy(inputStream, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            LOGGER.error("写入文件时[Name = {}]出错, 错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
        }

        return true;
    }


    //生成文件对象
    private File getFile(String fileName) {
        String path = getStoreDir();
        if (StringUtils.isBlank(path)) {
            LOGGER.error("获取文件存储目录时返回空值,请确定全局配置中配置了LOCAL_FILESYSTEM_STORE_PATH选项.");
            return null;
        }

        //保证目录存在
        if (!ensureExist(path)) {
            return null;
        }

        return StringUtils.isBlank(fileName)
                ? new File(path)
                : new File(path, fileName);
    }

    private boolean ensureExist(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            try {
                FileUtils.forceMkdir(file);
            } catch (IOException e) {
                LOGGER.error("生成目录时出错,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
                return false;
            }
        }

        return true;
    }

    private File getFile() {
        return getFile(null);
    }

    /**
     * 保存指定的文件,并返回相应的key
     *
     * @param file 需要上传的文件
     * @return 保存的文件对应的key, 后续可以通过调用get(key)来获取到该文件, 保存失败则返回null
     */
    @Override
    public String save(File file) {
        try {
            return file == null ? null : save(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            LOGGER.error("读取文件时出错, 文件名称为{}, 错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
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
            return null;
        }

        File file = getFile(key);

        //文件存在且为文件格式(不是文件夹)
        if (file != null && file.exists() && file.isFile()) {
            try {
                byte[] result = StreamUtils.copyToByteArray(new BufferedInputStream(new FileInputStream(file)));
                return new BufferedInputStream(new ByteArrayInputStream(result));
            } catch (IOException e) {
                LOGGER.error("读取文件信息时出错, 文件名称为{}, 错误信息为{}, 错误堆栈为{}.", file.getName(), e.getMessage(), e.getStackTrace());
            }
        }

        return null;
    }

    private String getStoreDir() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOCAL_FILESYSTEM_STORE_PATH.getKey());
    }
}
