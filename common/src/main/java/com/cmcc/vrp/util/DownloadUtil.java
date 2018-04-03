package com.cmcc.vrp.util;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;


public class DownloadUtil {

    /**
     * 文件下载
     *
     * @param filePath，文件完整路径包括文件名
     * @param response
     * @return
     * @throws IOException
     */
    public static boolean downloadFile(String filePath, HttpServletResponse response) throws IOException {
        // 检查文件是否有效
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
        // 取得文件名。
        String filename = file.getName();
        String encoded = URLEncoder.encode(filename, "utf-8");
        // 清空response
        response.reset();
        // 定义输出类型
        // response.setContentType("application/x-download;charset=UTF-8");
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + encoded);
//        response.addHeader("Content-Disposition", "attachment;filename=" + encoded);
        response.addHeader("Content-Length", "" + file.length());
        // 以流的形式下载文件。
        byte[] fileData = FileUtils.readFileToByteArray(file);

        OutputStream outputStream = response.getOutputStream();

        outputStream.write(fileData);

        outputStream.flush();

        return true;

    }

}
