/**
 *
 */
package com.cmcc.vrp.boss.sichuan;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月9日
 */
@Ignore
public class AccountRecordTest {

    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;
    @Autowired
    ProductService productService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    com.cmcc.vrp.boss.sichuan.service.SCBalanceService scBalanceService;

    /**
     * @return
     */
    public List<String> readTxtFile() {
        List<String> lists = new ArrayList<String>();
        try {
            String encoding = "GBK";
            File file = new File("D:\\user_id.txt");
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    lists.add(lineTxt);
                }
                read.close();

            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return lists;

    }

    /**
     * 账户记录测试
     */
    @Test
    @Ignore
    public void test() {
        final Logger logger = LoggerFactory.getLogger(AccountRecordTest.class);

        FileWriter writer = null;
        try {
            writer = new FileWriter("d:\\data.txt");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (writer == null) {
            return;
        }

        List<String> lists = readTxtFile();
        for (String userId : lists) {

            HttpURLConnection connection = null;
            String result = null;
            try {

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");


                String params = "appKey=11000161&login_no=oc0025&msg_flag=1&phone_no="
                    + userId + "&timeStamp=" + dateformat.format(new Date()) +
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
                result = buffer.toString();
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

            JSONObject json = (JSONObject) JSONObject.parse(result);
            String resCode = json.getString("resCode");

            if ("0000000".equals(resCode)) {
                String outDataStr = json.getString("outData");
                SCBalanceResponseOutData outData = JSONObject.parseObject(outDataStr, SCBalanceResponseOutData.class);
                String prepayFee = outData.getPREPAY_FEE();

//				String sql = "insert into account(enter_id, owner_id, product_id,"
//						+ "type, count, min_count, create_time, update_time,"
//						+ "version) values(" + enterprise.getId() + "," + enterprise.getId() + ","
//						+ product.getId() 
//						+ ", -1, " + Float.parseFloat(prepay_fee)/100 + ", 0, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP (), 0" + ");";
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
                String sql = "202:" + userId + "余额：" + Float.parseFloat(prepayFee) / 100 + "元，查询时间：" + dateformat.format(new Date());

                logger.info(sql);


                try {

                    writer.write(sql);
                    writer.write("\r\n ");


//					FileWriter fw = new FileWriter("d:\\data.txt", true);
//		            BufferedWriter bw = new BufferedWriter(fw);
//		            //bw.append("在已有的基础上添加字符串");
//		            bw.write("abc\r\n ");// 往已有的文件上添加字符串
//		            bw.write("def\r\n ");
//		            bw.write("hijk ");
//		            bw.close();
//		            fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
