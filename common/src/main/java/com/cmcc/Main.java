/**
 * 
 *//*
package com.cmcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

*//**
 * @desc:
 * @author: wuguoping
 * @data: 2017年6月27日
 *//*
@Component
public class Main {

    private static List<String> telNum = new ArrayList<String>();

    *//**
     * 
     * title: readTxtFile desc:
     * 
     * @param filePath
     * @param encoding
     *            wuguoping 2017年6月27日
     *//*
    public static void readTxtFile(String filePath, String encoding) {

        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    telNum.add(lineTxt);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }

    public static Jedis getRedis(String ip, int port) {
        Jedis jedis = new Jedis(ip, port);
        return jedis;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\wuguoping\\Desktop\\EC平台\\短信0627.txt";
        readTxtFile(path, "GBK");
        
        List<String> a = new ArrayList<String>();
        
        try {
            Jedis j = (Jedis) getRedis("172.22.26.120", 6379);
//            j.auth("");
            j.ping();
            if(j.isConnected()){
                System.out.println("connected");
            }
            Main m = new Main();
            ToSend s= m.new ToSend();
            s.setContent("尊敬的用户，中国移动数字家庭创客马拉松大赛已为您成功充值移动数据流量，当月有效，感谢您的支持。更多赛事信息请登录 http://open.home.10086.cn/hack。");
//            s.setContent("中文能发送么");
            
            for(int i=0 ; i< telNum.size(); i++){ 
                s.setMobile(telNum.get(i).toString());
                String sq = JSON.toJSONString(s);
                long l =j.lpush("dsSms", sq);
                System.out.println("标志位:"+l+"----"+ telNum.get(i)); 
            }
            System.out.println("发送完了！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    *//**
     * 
     *  @desc:
     *  @author: wuguoping 
     *  @data: 2017年7月4日
     *//*
    private class ToSend{
        private String content;
        private String mobile;
        *//**
         * @return the content
         *//*
        public String getContent() {
            return content;
        }
        *//**
         * @param content the content to set
         *//*
        public void setContent(String content) {
            this.content = content;
        }
        *//**
         * @return the mobile
         *//*
        public String getMobile() {
            return mobile;
        }
        *//**
         * @param mobile the mobile to set
         *//*
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
*/