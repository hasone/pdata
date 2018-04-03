package com.cmcc.vrp.province.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qinqinyan on 2016/10/12.
 */
public interface S3Service {

    /**
     * 从S3中下载
     * @param response
     * @param key
     * @param fileName
     */
    void downloadFromS3(HttpServletResponse response, String key, String fileName,  HttpServletRequest request);

    /**
     * 从S3中下载
     * @param response
     * @param key
     */
    void getImageFromS3(HttpServletResponse response, String key);
    
    /**
     * 从S3中下载
     * @param response
     * @param key
     */
    void getImageFromS3(HttpServletResponse response, String key, HttpServletRequest request);

    /**
     * 检查文件类型（检查图片格式是否符合要求）
     * @param request
     * */
    boolean checkFile(MultipartHttpServletRequest request);

    /**
     * 检查文件大小
     * @param request
     * */
    boolean checkFileSize(MultipartHttpServletRequest request);
}
