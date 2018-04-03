/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.QRCodeExtendParam;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.QRCodeService;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MatrixToImageWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年8月27日
 */
@Service("qRCodeServiceService")
public class QRCodeServiceImpl implements QRCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeServiceImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivityInfoService activityInfoService;

    @Override
    public boolean qRCodeCommon(ActivityInfo activityInfo, String fileName,
                                HttpServletRequest request, HttpServletResponse response) {

        if (!activityWinRecordService.batchInsertForQRcode(activityInfo.getActivityId(), activityInfo)) {
            LOGGER.error("批量插入qrcode-activityWinRecord失败");
            return false;
        }
        List<ActivityWinRecord> activityWinRecords = activityWinRecordService.selectByActivityId(activityInfo.getActivityId());

        // 设置文件存放路径
        String fileFolder = getQRCodeDownloadPath() + File.separator + activityInfo.getId();
        File folder = new File(fileFolder);
//	    List<File> files = null;
        try {
            if (folder.exists()) {
                // 移除文件夹下已存在的文件
                FileUtils.cleanDirectory(new File(fileFolder));
            } else {
                folder.mkdirs();
            }

            LOGGER.info("decode,fileName:" + fileName);
            LOGGER.info("开始生成二维码");
            doGenQRCodes(activityInfo, activityWinRecords, fileFolder);
            //压缩文件包
            if (!createZIPfile(fileFolder, fileFolder, fileName, activityInfo)) {
                LOGGER.info("生成二维码压缩文件失败");
                return false;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }

        File file = new File(fileFolder + File.separator + fileName + ".zip");
        try {
            flushData(file, response);
        } catch (Exception e) {
            LOGGER.error("二维码下载失败,失败原因:" + e.getMessage());
            return false;
        }
        //将活动信息activityInfo记录设置为已下载
        activityInfo.setDownload(1);
        if (!activityInfoService.updateByPrimaryKeySelective(activityInfo)) {
            LOGGER.error("activityInf更新失败:activityInfo={}", JSONObject.toJSONString(activityInfo));
        }
        return true;
    }

    private void doGenQRCodes(ActivityInfo activityInfo, List<ActivityWinRecord> activityWinRecords, String fileFolder) {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        //内容所使用编码  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        int size = activityInfo.getQrcodeSize() * Integer.parseInt(QRCodeExtendParam.QRCODE_SIZE_BASIC.getKey());
        int i = 1;
        if (activityWinRecords != null && activityWinRecords.size() > 0) {
            for (ActivityWinRecord activityWinRecord : activityWinRecords) {
                String recordId = activityWinRecord.getRecordId();
                String token = new StringBuffer(activityInfo.getActivityId()).append(";" + recordId).toString();
                String serectToken = DatatypeConverter.printHexBinary(AES.encrypt(token.getBytes(), getActivityUrlKey().getBytes()));
                String content = new StringBuffer(getLotteryQRCodeUrl()).append(serectToken).toString();

                try {
                    //生成二维码
                    BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);

                    File outputFile = new File(fileFolder, i + ".jpg");

                    MatrixToImageWriter.writeToFile(bitMatrix, "jpg", outputFile);
                } catch (WriterException e) {
                    LOGGER.info("生成二维码失败");
                    LOGGER.info(e.getMessage());
                } catch (Exception e) {
                    LOGGER.info("生成二维码失败");
                    LOGGER.info(e.getMessage());
                }
                i++;
            }
        }
    }

    private boolean createZIPfile(String sourceFilePath, String zipFilePath, String fileName, ActivityInfo activityInfo) {
        boolean flag = true;

        if (!saveZIPfile(sourceFilePath, zipFilePath, fileName)) {
            LOGGER.info(zipFilePath + "目录下创建" + fileName + ".zip" + "打包文件,失败！");
            flag = false;
        }

        //删除二维码文件
        for (int i = 1; i <= activityInfo.getPrizeCount(); i++) {
            LOGGER.info(getQRCodeDownloadPath() + File.separator + activityInfo.getActivityId()
                + File.separator + i + ".jpg");
            if (!FileUtils.deleteQuietly(new File(getQRCodeDownloadPath() + File.separator + activityInfo.getId()
                + File.separator + i + ".jpg"))) {
                LOGGER.info("删除" + i + ".jpg失败");
                flag = false;
            }

        }

        return flag;
    }

    private boolean saveZIPfile(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
        } else {
            try {
                File zipFile = new File(zipFilePath + File.separator + fileName + ".zip");
                if (zipFile.exists()) {
                    LOGGER.info(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.删除打包文件");
                    if (FileUtils.deleteQuietly(zipFile)) {
                        LOGGER.info(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.删除成功");
                    }
                    ;
                }
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    LOGGER.info("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                } else {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    zos.setEncoding("GBK");
                    byte[] bufs = new byte[1024 * 10];
                    for (int i = 0; i < sourceFiles.length; i++) {
                        //创建ZIP实体，并添加进压缩包  
                        LOGGER.info("ZIP实体文件名:" + sourceFiles[i].getName());
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里  
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                        fis.close();
                    }

                    flag = true;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流  
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                    if (null != fis) {
                        fis.close();
                    }
                    if (null != fos) {
                        fos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

    /**
     * 传输文件数据
     * <p>
     *
     * @param file
     * @param response
     * @throws IOException
     */
    private void flushData(File file, HttpServletResponse response) throws IOException {
        // 取得文件名。
        String filename = file.getName();
        String encoded = URLEncoder.encode(filename, "utf-8");
        // 清空response
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + encoded);
        response.addHeader("Content-Length", "" + file.length());
        // 以流的形式下载文件。
        byte[] fileData = FileUtils.readFileToByteArray(file);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(fileData);
        outputStream.flush();
    }

    public String getActivityUrlKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
    }

    public String getQRCodeDownloadPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.QRCODE_DOWNLOAD_PATH.getKey());
    }

    public String getLotteryQRCodeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOTTERY_QRCODE_URL.getKey());
    }

}
