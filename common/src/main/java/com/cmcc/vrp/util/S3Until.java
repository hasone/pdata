package com.cmcc.vrp.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class S3Until {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Until.class);

    private final String AWS_ACCESS_KEY;
    private final String AWS_SECRET_KEY;
    private final String HOST;
    private final String bucketName;
    private final boolean useHttps;

    AmazonS3 s3 = null;
    TransferManager tx;

    public S3Until(String bucketName, String HOST, String AWS_SECRET_KEY, String AWS_ACCESS_KEY) {
        this(AWS_ACCESS_KEY, AWS_SECRET_KEY, HOST, bucketName, true);
    }

    public S3Until(String AWS_ACCESS_KEY, String AWS_SECRET_KEY, String HOST, String bucketName, boolean useHttps) {
        this.AWS_ACCESS_KEY = AWS_ACCESS_KEY;
        this.AWS_SECRET_KEY = AWS_SECRET_KEY;
        this.HOST = HOST;
        this.bucketName = bucketName;
        this.useHttps = useHttps;


        try {
            init_with_key();
        } catch (Exception e) {
            LOGGER.error("初始化S3时抛出异常,异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    public void init_with_key() throws Exception {
        AWSCredentials credentials = null;
        credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        ClientConfiguration clientConfig = new ClientConfiguration();

        //默认使用的是https
        if (!useHttps) {
            clientConfig.setProtocol(Protocol.HTTP);
        }

        S3ClientOptions s3ClientOptions = new S3ClientOptions();
        s3ClientOptions.setPathStyleAccess(true);
        s3 = new AmazonS3Client(credentials, clientConfig);
        s3.setEndpoint(getHOST());
        s3.setS3ClientOptions(s3ClientOptions);
        tx = new TransferManager(s3);
        createBucket();
    }

    public void createBucket() {
        if (s3.doesBucketExist(bucketName)) {
            LOGGER.info(bucketName + " already exist!");
            return;
        }
        LOGGER.info("creating " + bucketName + " ...");
        s3.createBucket(bucketName);
        LOGGER.info(bucketName + " has been created!");
    }

    public List<String> listStartWithKeys(String bucketName, String key) {
        List<String> list = new LinkedList<String>();
        LOGGER.info("Listing objects of " + bucketName);
        ObjectListing objectListing = s3.listObjects(bucketName, key);
        int objectNum = 0;
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            LOGGER.info(" - " + objectSummary.getKey());
            String existKey = objectSummary.getKey();
            if (existKey.startsWith(key)) {
                list.add(existKey);
            }

            objectNum++;
        }
        LOGGER.info("total " + objectNum + " object(s).");
        return list;
    }

    /**
     * 罗列默认的bucketName中的文件对象
     *
     * @date 2016年6月7日
     * @author wujiamin
     */
    public List<String> listStartWithKeys(String key) {
        List<String> list = new LinkedList<String>();
        LOGGER.info("Listing objects of " + bucketName);
        ObjectListing objectListing = s3.listObjects(bucketName, key);
        int objectNum = 0;
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            LOGGER.info(" - " + objectSummary.getKey());
            String existKey = objectSummary.getKey();
            if (existKey.startsWith(key)) {
                list.add(existKey);
            }

            objectNum++;
        }
        LOGGER.info("total " + objectNum + " object(s).");
        return list;
    }


    /**
     * 上传文件
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    public void uploadFile(String bucketName, String key, File file) {
        s3.putObject(new PutObjectRequest(bucketName, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    /**
     * 不带bucketName的文件上传
     *
     * @date 2016年6月7日
     * @author wujiamin
     */
    public void uploadFile(String key, File file) {
        s3.putObject(new PutObjectRequest(bucketName, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void uploadFile(String key, InputStream inputStream) {
        s3.putObject(new PutObjectRequest(bucketName, key, inputStream, null).withCannedAcl(CannedAccessControlList.PublicRead));
    }


    /**
     * ------------------- 上传新的文件 -------------------
     *
     * @Author liuzengzeng
     * @Date 16/3/10 下午2:34
     */
    public void uploadFile(String bucketName, String tag, String path, File file) {
        s3.putObject(new PutObjectRequest(bucketName, tag + path, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }


    /**
     * 不带bucketName的删除
     *
     * @date 2016年6月7日
     * @author wujiamin
     */
    public void deleteObjectsWithPrefix(String prefix) {
        if (!s3.doesBucketExist(bucketName)) {
            LOGGER.info(bucketName + " does not exists!");
            return;
        }
        LOGGER.info("deleting " + prefix + "* in " + bucketName + " ...");
        int pre_len = prefix.length();
        ObjectListing objectListing = s3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            int len = key.length();
            if (len < pre_len) {
                continue;
            }
            int i;
            for (i = 0; i < pre_len; i++) {
                if (key.charAt(i) != prefix.charAt(i)) {
                    break;
                }
            }
            if (i < pre_len) {
                continue;
            }
            s3.deleteObject(bucketName, key);
        }
        LOGGER.info("All " + prefix + "* deleted!");
    }


    /**
     * 不带key的读取文件转化为文件流输出
     *
     * @date 2016年6月7日
     * @author wujiamin
     */
    public BufferedInputStream readFileFromS3(String key) {
        S3Object s3object = s3.getObject(new GetObjectRequest(
                bucketName, key));

        InputStream iin = s3object.getObjectContent();
        BufferedInputStream out = new BufferedInputStream(iin);
        return out;
    }

    /**
     * 不带bucketName的查找是否存在文件
     *
     * @date 2016年6月7日
     * @author wujiamin
     */
    public boolean checkExist(String startKeys) {
        List<String> list = listStartWithKeys(bucketName, startKeys);
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }


    public String getAWS_ACCESS_KEY() {
        return AWS_ACCESS_KEY;
    }

    public String getAWS_SECRET_KEY() {
        return AWS_SECRET_KEY;
    }

    public String getHOST() {
        return HOST;
    }

    public String getBucketName() {
        return bucketName;
    }

    public boolean isUseHttps() {
        return useHttps;
    }
}
