package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.S3Service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qinqinyan on 2016/10/12.
 */
@Service("s3Service")
public class S3ServiceImpl implements S3Service {
    private static final Logger logger = LoggerFactory
            .getLogger(S3Service.class);
    @Autowired
    FileStoreService fileStoreService;

    @Override
    public void downloadFromS3(HttpServletResponse response, String key, String fileName, HttpServletRequest request) {

        InputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        try {
            // 客户使用保存文件的对话框：
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");

            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (userAgent != null && userAgent.indexOf("firefox") >= 0) {
                response.setHeader("Content-disposition", "attachment; filename="
                        + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            } else {
                response.setHeader("Content-disposition", "attachment; filename="
                        + java.net.URLEncoder.encode(fileName, "UTF-8"));
            }
            // 通知客户文件的MIME类型：
            bis = fileStoreService.get(key);
            bos = new java.io.BufferedOutputStream(response.getOutputStream());
            StreamUtils.copy(bis, bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
        }
    }
    @Override
    public void getImageFromS3(HttpServletResponse response, String key, HttpServletRequest request) {
        InputStream inputStream = fileStoreService.get(key);
        if (inputStream != null) {
            try {
                StreamUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                logger.error("输出结果流时抛出异常,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            }
        }
    }

    @Override
    public void getImageFromS3(HttpServletResponse response, String key) {
        InputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        try {
            // 通知客户文件的MIME类型：
            bis = fileStoreService.get(key);
            bos = new java.io.BufferedOutputStream(response.getOutputStream());
            StreamUtils.copy(bis, bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
        }
    }

    @Override
    public boolean checkFile(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();

        MultipartFile multipartFile = null;

        // 迭代处理
        while (itr != null && itr.hasNext()) {

            // 获得文件实例
            multipartFile = request.getFile(itr.next());

            if (multipartFile.getName().equals("image") || multipartFile.getName().equals("licenceImage")
                    || multipartFile.getName().equals("logo") || multipartFile.getName().equals("banner")
                    || multipartFile.getName().equals("qrcode") || multipartFile.getName().equals("certificateFront")
                    || multipartFile.getName().equals("certificateBack") || multipartFile.getName().equals("qrCode")
                    || multipartFile.getName().equals("certificate") || multipartFile.getName().equals("templateFront")
                    || multipartFile.getName().equals("templateBack") || multipartFile.getName().equals("mdrcCommonTemplateFront")
                    || multipartFile.getName().equals("mdrcCommonTemplateBack")
                    || multipartFile.getName().equals("signVoucher")) {

                String originalFilename = multipartFile.getOriginalFilename();
                int index = originalFilename.indexOf(".");
                String fileType = originalFilename.substring(index + 1);
                if (!StringUtils.isBlank(fileType)
                        && !("jpg".equalsIgnoreCase(fileType))
                        && !("jpeg".equalsIgnoreCase(fileType))
                        && !("png".equalsIgnoreCase(fileType))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkFileSize(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());
            Long size = multipartFile.getSize();
            if (multipartFile.getName().equals("logo")
                    || multipartFile.getName().equals("banner")) {
                //广东众筹的logo和banner不能大于100KB
                if (size > 1024 * 100) {
                    return true;
                }
            } else if (multipartFile.getName().equals("qrCode")
                    || multipartFile.getName().equals("mdrcCommonTemplateFront")
                    || multipartFile.getName().equals("mdrcCommonTemplateBack")
                    || multipartFile.getName().equals("certificate")
                    || multipartFile.getName().equals("templateFront")
                    || multipartFile.getName().equals("templateBack")){
                //流量卡申请制卡上传的二维码qrcode、自定义正反面（templateFront、templateBack），缴费凭证（certificate）不能超过1M
                //营销模板通用正反面（mdrcCommonTemplateFront、mdrcCommonTemplateBack）
                if(size > 1024*1024){
                    return true;
                }
            }else {
                if (size > 1024 * 1024 * 5) {
                    return true;
                }
            }
        }
        return false;
    }
}
