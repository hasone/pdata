package com.cmcc.vrp.boss.chongqing.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * <https调用测试用例> <功能详细描述> <处理流程>
 * 
 * @author xWX227233
 * @version [版本号, May 16, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HttpsUtil{
    private static class TrustAnyTrustManager implements X509TrustManager{
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException{
        }
        
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{
        }
        
        public X509Certificate[] getAcceptedIssuers(){
            return new X509Certificate[] {};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier{
        public boolean verify(String hostname, SSLSession session){
            return true;
        }
    }
    
    /**
     * @param url
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws NoSuchProviderException
     */
    public static String post(String url, String content) throws NoSuchAlgorithmException, KeyManagementException,
            IOException, NoSuchProviderException{
        PrintWriter out = null;
        HttpsURLConnection conn = null;
        BufferedReader in = null;
        
        try{
            TrustManager[] tm = {new TrustAnyTrustManager()};
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, tm, new java.security.SecureRandom());
            URL console = new URL(url);
            StringBuffer sbBuf = new StringBuffer();
            
            conn = (HttpsURLConnection)console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=gbk");
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.connect();
            
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "gbk"));
            out.println(content);
            
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
            
            String sLine = "";
            while ((sLine = in.readLine()) != null){
                sbBuf.append(sLine).append("\r\n");
            }
            return sbBuf.toString();
        }catch (KeyManagementException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (NoSuchProviderException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (null != in){
                in.close();
                in = null;
            }
            if (null != out){
                out.close();
                out = null;
            }
            if (null != conn){
                conn.disconnect();
                conn = null;
            }
        }
        
        return null;
    }
    
    /**
     * <生成token> <功能详细描述>
     * 
     * @param url
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws NoSuchProviderException
     * @see [类、类#方法、类#成员]
     */
    public static String getToken(String url, String content) throws NoSuchAlgorithmException, KeyManagementException,
            IOException, NoSuchProviderException{
        PrintWriter out = null;
        HttpsURLConnection conn = null;
        BufferedReader in = null;
        
        try{
            TrustManager[] tm = {new TrustAnyTrustManager()};
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, tm, new java.security.SecureRandom());
            URL console = new URL(url);
            StringBuffer sbBuf = new StringBuffer();
            
            conn = (HttpsURLConnection)console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "");// "application/json; charset=gbk"
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.connect();
            
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "gbk"));
            out.print(content);
            
            out.flush();
            
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
            
            String sLine = "";
            while ((sLine = in.readLine()) != null){
                sbBuf.append(sLine).append("\r\n");
            }
            return sbBuf.toString();
        }catch (KeyManagementException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchProviderException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (null != in){
                in.close();
                in = null;
            }
            if (null != out) {
                out.close();
                out = null;
            }
            try{
                if (null != conn){
                    conn.disconnect();
                    conn = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    public static void main(String[] args) throws IOException{
        // 生成token
        try{
            String token = getToken("https://10.171.69.220:7002/OAuth/restOauth2Server/access_token",
                    "grant_type=client_credentials&client_id=20140526000002047&client_secret=d41d8cd98f00b204e9800998ecf8427e");
            System.out.println(token);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        // 根据token拼接url后，调用接口
        try{
            String post = post("https://10.171.69.220:7002/OpenEbus/httpService/UserService?"
                    + "access_token=7dbeb5827686eb0f5277d097809073f8b8ff4e51&method=qryUserInfo&format=json",
                    "{\"TELNUM\":\"13563414048\"}");
            
            System.out.println(post);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
