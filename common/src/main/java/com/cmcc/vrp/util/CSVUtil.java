package com.cmcc.vrp.util;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class CSVUtil {

    /**
     * 把数据转成CSV格式
     *
     * @param list
     * @param title
     * @return
     * @throws
     * @Title:listToString
     * @Description: TODO
     * @author: hexinxu
     */
    public static String listToString(List<String> list, List<String> title) {
        StringBuffer buffer = new StringBuffer();
        String tmp = "";
        // 添加title
        for (String s : title) {
            tmp = tmp + s + ",";
        }
        buffer.append(tmp.substring(0, tmp.length() - 1));
        buffer.append("\r\n");

        for (int i = 0; i < list.size(); i++) {


            if ((i + 1) % title.size() == 0) {
                buffer.append(list.get(i));
                buffer.append("\r\n");
            } else {
                buffer.append(list.get(i) + ",");
            }
        }

        return buffer.toString();
    }

    /**
     * 把String类型转成流
     *
     * @param in
     * @return
     * @throws Exception
     * @throws
     * @Title:StringTOInputStream
     * @Description: TODO
     * @author: hexinxu
     */
    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("gb2312"));
        return is;
    }

    /**
     * 生成为CSV文件
     *
     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map,
                                     String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv",
                new File(outPutPath));
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "UTF-8"), 1024);

            if (csvFileOutputStream != null) {
                // 写入文件头部
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                    .hasNext(); ) {
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                        .next();
//					csvFileOutputStream
//							.write("" + (String) propertyEntry.getValue() != null ? (String) propertyEntry
//									.getValue() : "" + "");
                    csvFileOutputStream.write("" + (String) propertyEntry.getValue() + "");
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                csvFileOutputStream.newLine();
                // 写入文件内容
                for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                    Object row = (Object) iterator.next();
                    for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                        .hasNext(); ) {
                        java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                            .next();
                        csvFileOutputStream.write((String) BeanUtils.getProperty(
                            row, (String) propertyEntry.getKey()));
                        if (propertyIterator.hasNext()) {
                            csvFileOutputStream.write(",");
                        }
                    }
                    if (iterator.hasNext()) {
                        csvFileOutputStream.newLine();
                    }
                }
                csvFileOutputStream.flush();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                if (csvFileOutputStream != null) {
                    csvFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * @throws
     * @Title:exportFile
     * @Description: 下载CSV文件
     * @author: xuwanlin
     */
    public static void exportFile(HttpServletResponse response, String fileName, InputStream fileInputStream) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);

        byte[] b = new byte[100];
        int len;

        try {
            while ((len = fileInputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filePath
     * @throws
     * @Title:deleteFiles
     * @Description: 删除所有文件
     * @author: xuwanlin
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        files[i].delete();
                    }
                }
            }
        }
    }

    /**
     * @param filePath
     * @param fileName
     * @throws
     * @Title:deleteFile
     * @Description: 删除单个文件
     * @author: xuwanlin
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        if (files[i].getName().equals(fileName)) {
                            files[i].delete();
                            return;
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) {

        List<String> title = new ArrayList<String>();
        title.add("营销类型");
        title.add("营销流量");
        title.add("占比");
        List<String> list = new ArrayList<String>();
        list.add("红包");
        list.add("12");
        list.add("13");
        list.add("流量卡");
        list.add("22");
        list.add("23");
        list.add("转赠");
        list.add("32");
        list.add("33");

        System.out.println(CSVUtil.listToString(list, title));
    }
}
