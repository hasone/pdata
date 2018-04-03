package com.cmcc.vrp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @ClassName:  FTPUtil 
 * @Description:  ftp基本类
 * @author: qihang
 * @date:   2016年3月26日 下午7:24:47 
 *  
 */
public class FTPUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
            
    /**
     * Description: 向FTP服务器上传文件
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url,int port,
            String username, String password, String path, 
            String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(url, port);//连接FTP服务器
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);//登录
            
            ftp.getReplyCode();
            
           /* if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }*/
            
            ftp.changeWorkingDirectory(path);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //ftp.enterLocalPassiveMode();
            
            ftp.storeFile(filename, input);        
            
            input.close();
            ftp.logout();
            success = true;      
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
  
    /**
     * 下载模糊文件名,成功返回文件名，失败返回""
     */
    public static String downFileFuzzyName(String url, int port,
            String username, String password, 
            String remotePath,String fileName,String localPath) { 
        String concreteFileName="";
        FTPClient ftp = new FTPClient(); 
        
        OutputStream os =null;
        try { 
            int reply; 
            ftp.connect(url, port); 
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器 
            ftp.login(username, password);//登录 
            reply = ftp.getReplyCode(); 
            if (!FTPReply.isPositiveCompletion(reply)) { 
                ftp.disconnect(); 
                return ""; 
            } 
            ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //ftp.enterLocalPassiveMode();
            
            FTPFile[] fs = ftp.listFiles(); 
            for(FTPFile ff:fs){ 
                if(ff.getName().indexOf(fileName)!=-1){ 
                    File localFile = new File(localPath+"/"+ff.getName()); 
                     
                    os = new FileOutputStream(localFile);  
                    ftp.retrieveFile(ff.getName(), os); 
                    concreteFileName=ff.getName();
                    
                    logger.info("downloadFile:" + concreteFileName);

                } 
            } 
             
            ftp.logout(); 
            
        } catch (IOException e) { 
            logger.error(e.getMessage());
        } finally { 
            if (ftp.isConnected()) { 
                try { 
                    ftp.disconnect(); 
                } catch (IOException ioe) { 
                    logger.error(ioe.getMessage());
                } 
            } 
            
            if(os != null){
                try { 
                    os.close();
                } catch (IOException ioe) { 
                    logger.error(ioe.getMessage());
                } 
            }
        } 
        return concreteFileName; 
    }
    
    /**
     * downFileFuzzyName
     */
    public static String downFileFuzzyName(FtpProperties ftpProperties,String fileName){
        
        return ftpProperties==null ? "": downFileFuzzyName(ftpProperties.getUrl(),ftpProperties.getPort(),
                ftpProperties.getUsername(),ftpProperties.getPassword(),ftpProperties.getRemotePath(),
                fileName,ftpProperties.getLocalPath());
    }
}
