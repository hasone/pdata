/**
 *
 */
package com.cmcc.vrp.boss.sichuan;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.Sign;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年4月22日
 */
//@Ignore
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/conf/applicationContext.xml")
public class YueTest {
    
    //@Test
    public void test0() {
        final Logger logger = LoggerFactory.getLogger(YueTest.class);
        HttpURLConnection connection = null;
        try {

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

            String params = "appKey=21000030&login_no=oc0025&msg_flag=0&op_type=1&phone_no=18867103685&prod_prcid=11&timeStamp=" + dateformat.format(new Date()) +
                "&userName=wujiamin";

            String sign = Sign.sign(params, "/etc/pdata/conf/private_key_dev.txt");

            params = params + "&sign=" + sign;

            //URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sPFeeQuery?" + params);
            
            URL url = new URL("HTTP://218.205.252.26:33000/rest/1.0/RedPacket?" + params);
            
            
            logger.info("请求地址：" + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    //@Test
    //@Ignore
    /**
     * 
     */
    public void test() {
        final Logger logger = LoggerFactory.getLogger(YueTest.class);
        HttpURLConnection connection = null;
        try {

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

            String params = "appKey=11000161&login_no=&msg_flag=0&phone_no=20230774561&timeStamp=" + dateformat.format(new Date()) +
                "&userName=sc4ggogo";

//        	String params = "appKey=21000030&login_no=oc0025&msg_flag=1&phone_no=20211380609&timeStamp="+dateformat.format(new Date())+
//    				"&userName=wujiamin";

            String sign = Sign.sign(params, "/etc/pdata/conf/private_key.txt");

            params = params + "&sign=" + sign;

            //URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sPFeeQuery?" + params);
            
            URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sPFeeQuery?" + params);
            
            
            logger.info("请求地址：" + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 
     */
    @Ignore
    @Test
    public void testCancel() {
        final Logger logger = LoggerFactory.getLogger(YueTest.class);
        HttpURLConnection connection = null;
        try {

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

            String params = "appKey=11000161&login_no=oc0025&op_code=1052&phone_no=&timeStamp=" + dateformat.format(new Date()) +
                "&userName=sc4ggogo";


//          String params = "appKey=21000030&login_no=oc0025&msg_flag=1&phone_no=20211380609&timeStamp="+dateformat.format(new Date())+
//                  "&userName=wujiamin";

            String sign = Sign.sign(params, "/etc/pdata/conf/private_key.txt");

            params = params + "&sign=" + sign;

            URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sCancleGrp?" + params);
            logger.info("请求地址：" + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 
     */
    @Ignore
    @Test
    public void testOpen() {
        final Logger logger = LoggerFactory.getLogger(YueTest.class);
        HttpURLConnection connection = null;
        try {

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

            String params = "appKey=11000161&discnt_code=95&login_no=oc0025&op_code=4317&prc_id=ACCZ65711&region_id=11&timeStamp=" 
                    + dateformat.format(new Date()) + "&unit_id=&userName=sc4ggogo";

//          String params = "appKey=21000030&login_no=oc0025&msg_flag=1&phone_no=20211380609&timeStamp="+dateformat.format(new Date())+
//                  "&userName=wujiamin";

            String sign = Sign.sign(params, "/etc/pdata/conf/private_key.txt");

            params = params + "&sign=" + sign;

            URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sLocalLLTFGrpOpen?" + params);
            logger.info("请求地址：" + url);
            logger.info("请求参数：" + params);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

//		SCBalanceServiceImpl scBalanceServiceImpl = new SCBalanceServiceImpl();
//		SCBalanceRequest request = new SCBalanceRequest();
//		request.setAppKey("11000161");
//		request.setLoginNo("oc0025");
//		request.setMsg_flag("0");
//		request.setPhoneNo("20211380609");
//		request.setUserName("oc0025");
//		SCBalanceResponse scBalanceResponse = scBalanceServiceImpl.sendBalanceRequest(request);

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Ignore
    @Test
    public void createYuETest() throws FileNotFoundException, IOException {

        HSSFWorkbook wb = null;
        POIFSFileSystem fs = null;
        //设置要读取的文件路径
        fs = new POIFSFileSystem(new FileInputStream("D:\\enter.xls"));
        //HSSFWorkbook相当于一个excel文件，HSSFWorkbook是解析excel2007之前的版本（xls）
        //之后版本使用XSSFWorkbook（xlsx）
        try {
            wb = new HSSFWorkbook(fs);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //获得sheet工作簿
        HSSFSheet sheet = wb.getSheetAt(0);
        int totalRow = sheet.getLastRowNum() + 1;
        for (int i = 0; i < totalRow; i++) {
            //获得行
            HSSFRow row = sheet.getRow(i);
            //获得行中的列，即单元格
            //企业202（列表中第3列）
            HSSFCell cell = row.getCell(2);
            Double msg = cell.getNumericCellValue();
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            String s = getYue(nf.format(msg));
            Double account = Double.valueOf(s) / 100;
            System.out.println(nf.format(msg) + "余额：" + account);

            //写入
            //第四列为账户余额
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(new HSSFRichTextString(account.toString()));

            //第五列写入时间
            HSSFCell cell4 = row.createCell(4);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cell4.setCellValue(new HSSFRichTextString(format.format(date)));

        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("D:\\enter1.xls");
            wb.write(out);

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            out.close();
            wb.close();
        }

//		
    }

    private String getYue(String userId) {
        final Logger logger = LoggerFactory.getLogger(YueTest.class);
        HttpURLConnection connection = null;
        try {

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

            String params = "appKey=11000161&login_no=oc0025&msg_flag=0&phone_no=" + userId + "&timeStamp=" + dateformat.format(new Date()) +
                "&userName=sc4ggogo";

            String sign = Sign.sign(params, "/etc/pdata/conf/private_key.txt");

            params = params + "&sign=" + sign;

            URL url = new URL("HTTP://218.205.252.26:32000/rest/1.0/sPFeeQuery?" + params);
            logger.info("请求地址：" + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);

            JSONObject json = (JSONObject) JSONObject.parse(result);
            String resCode = json.getString("resCode");
            String resMsg = json.getString("resMsg");

            SCBalanceResponse response = new SCBalanceResponse();
            response.setResCode(resCode);
            response.setResMsg(resMsg);
            if (response.getResCode().equals("0000000")) {
                String outDataStr = json.getString("outData");
                SCBalanceResponseOutData outData = JSONObject.parseObject(outDataStr, SCBalanceResponseOutData.class);
                response.setOutData(outData);
            }
            return response.getOutData().getPREPAY_FEE();


        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;

    }

}