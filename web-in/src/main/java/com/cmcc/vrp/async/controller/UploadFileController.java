package com.cmcc.vrp.async.controller;

import com.cmcc.vrp.ec.bean.EntAccountReq;
import com.cmcc.vrp.ec.bean.EntAccountResp;
import com.cmcc.vrp.ec.bean.UploadFileResp;
import com.cmcc.vrp.ec.bean.UploadFileRespData;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.MD5;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lgk8023
 */
@Controller
public class UploadFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);
    @Autowired
    FileStoreService fileStoreService;
    private XStream xStream;

    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", EntAccountReq.class);
        xStream.alias("Response", EntAccountResp.class);
        xStream.autodetectAnnotations(true);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {
        logger.info("调用S3服务上传文件");
        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        // 1、上传文件
        // 迭代处理
        while (itr != null && itr.hasNext()) {
            // 获得文件实例
            multipartFile = request.getFile(itr.next());
            if (multipartFile.getSize() >= 5000000) {
                logger.error("文件大小不能超过5M.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String originalFilename = multipartFile.getOriginalFilename();
            if (!"".equals(originalFilename)) {
                if (multipartFile.getName().equals("licenceImage")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "LicenceImage", response);

                }
                if (multipartFile.getName().equals("authorization")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "Authorization", response);
                }

                if (multipartFile.getName().equals("identification")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "Identification", response);
                }

                if (multipartFile.getName().equals("identificationBack")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "IdentificationBack", response);
                }

                if (multipartFile.getName().equals("customerFile")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "CustomerFile", response);
                }
                if (multipartFile.getName().equals("contract")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "Contract", response);
                }

                if (multipartFile.getName().equals("approveImage")) {
                    String key = MD5.md5(UUID.randomUUID().toString());
                    fileStoreService.save(key, file);
                    buildResp(key, originalFilename, "ApproveImage", response);
                }
            }
        }
        logger.error("文件上传错误, 没有包含相应的文件信息.");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void buildResp(String key, String fileName, String fileType, HttpServletResponse response) {
        UploadFileResp uploadFileResp = new UploadFileResp();
        UploadFileRespData uploadFileRespData = new UploadFileRespData();
        uploadFileRespData.setFileType(fileType);
        uploadFileRespData.setKey(key);
        uploadFileRespData.setFileName(fileName);
        uploadFileResp.setUploadFileRespData(uploadFileRespData);
        uploadFileResp.setResponseTime(DateUtil.getRespTime());
        try {
            StreamUtils.copy(xStream.toXML(uploadFileResp), Charsets.UTF_8, response.getOutputStream());
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
