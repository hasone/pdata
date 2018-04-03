package com.cmcc.vrp.async.controller;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author lgk8023
 *
 */
public class UploadFileControllerTest {
    private static String baseUrl = "http://localhost:8080/web-in/uploadFile.html";
	
    private static String TIME_FOMMAT = "yyyy-MM-dd HH:mms";// 时间格式
    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(baseUrl);
        httpPost.setHeader("User-Agent","SOHUWapRebot");
        httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.5");
        httpPost.setHeader("Accept-Charset","GBK,utf-8;q=0.7,*;q=0.7");
        httpPost.setHeader("Connection","keep-alive");
		 
        MultipartEntity mutiEntity = new MultipartEntity();
        File file = new File("d:/test.txt");
        mutiEntity.addPart("approveImage", new FileBody(file));
        
        
        
        FileBody bin = new FileBody(new File("d:/test.txt"));  
        //StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);  

        HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("approveImage", bin).build();  
		 
		 
        httpPost.setEntity(reqEntity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse  httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity =  httpResponse.getEntity();
        String content = EntityUtils.toString(httpEntity);
        System.out.println(content);
    }
}