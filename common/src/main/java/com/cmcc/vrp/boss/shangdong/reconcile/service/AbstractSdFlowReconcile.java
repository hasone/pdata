package com.cmcc.vrp.boss.shangdong.reconcile.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.boss.model.BillDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.BillStatisticModel;
import com.cmcc.vrp.boss.shangdong.boss.model.HuadanStatisticModel;
import com.cmcc.vrp.boss.shangdong.boss.model.RecordDetail;
import com.cmcc.vrp.boss.shangdong.boss.model.ServiceModel;
import com.cmcc.vrp.boss.shangdong.boss.model.UserOrder;
import com.cmcc.vrp.boss.shangdong.reconcile.BillMailService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.model.SdDailystatistic;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SdBossProductService;
import com.cmcc.vrp.province.service.SdDailystatisticService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.FTPUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.UncompressFileGZIP;

/**
 * 对账任务抽象类
 *
 * @author panxin
 */
@Service
public abstract class AbstractSdFlowReconcile implements SdFlowReconcileService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSdFlowReconcile.class);
    protected final String changeStatusFileName = "changeRecord";
    protected final String bossRecordsPlusName = "bossPlus";
    protected final String bossrecordsErrorName = "bossError";
    protected final String changePlusFileName = "changeplus";
    //ftp端口
    private final int ftpPort = 21;
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private SupplierProductMapper supplierProductMapper;
    @Autowired
    private BillMailService billMailService;
    @Autowired
    private SdDailystatisticService sdDailystatisticService;
    @Autowired
    private DiscountRecordService discountService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    SdBossProductService sdBossProductService;

    /**
     * 将content写入文件，直接沿用原山东的代码
     */
    public static void writeContentToFile(File file, String content) {
        // 打开一个随机访问文件流，按读写方式
        RandomAccessFile randomFile = null;
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    logger.error("创建新文件失败");
                }
            }
            randomFile = new RandomAccessFile(file, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                    logger.error(e1.getMessage());
                }
            }
        }
    }

    /**
     * 每日的对账任务，结合了所有的流程
     */
    @Override
    public boolean doDailyJob() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String twoDayBefore = sdf.format(DateUtil.getDateBefore(new Date(), 2));
        String onefayBefore = sdf.format(DateUtil.getDateBefore(new Date(), 1));

        logger.info("开始今天的对账任务" + sdf.format(new Date()));

        //如果允许从ftp下载且失败，则返回false
        if (!verifyUseFtp()) {
            logger.info("测试开关打开，不从FTP服务上下账账单文件，需保证已手动上传至指定目录");
        } else if (!downloadFile(onefayBefore)) {
            logger.error("下载账单失败");
            return false;
        }

        //初始化discountMap,结果为bill.getUserid()+ "_"+bill.getBizid()
        Map<String, List<DiscountRecord>> discountMap = new HashMap<String, List<DiscountRecord>>();

        //1.分析账单文件，得到对象,结果为bill.getMsisdn()+"_"+ bill.getUserid()+ "_"+bill.getBizid()
        Map<String, List<BillDetail>> billMap = analyseBill(onefayBefore.replaceAll("-", ""), twoDayBefore, discountMap);

        //2.从数据库中得到某天所有的充值成功记录,1092、1099产品
        List<ServiceModel> dbModels = getDailyRecordFromDB(twoDayBefore);

        //3.从数据库得到所有的userOrder，主键为userOrder.getUserID()+"_"+userOrder.getBizId()
        Map<String, UserOrder> userOrderMap = getAllUserOrdersToMap();

        //4.将账单与数据库记录进行比较，时间不匹配的打印到文件中，返回需要出话单的对象
        List<RecordDetail> details = null;

        //存放数据库比boss多的记录
        File changeStatusFile = new File(getLocalChangeRecordsPath() + File.separator + getChangeStatusFileName()
                + twoDayBefore.replaceAll("-", "") + ".txt");
        BufferedWriter changeStatusWriter = null;

        //存放数据库比boss多的记录
        File changePlusFile = new File(getLocalChangeRecordsPath() + File.separator + getChangeplusName()
                + twoDayBefore.replaceAll("-", "") + ".txt");
        BufferedWriter changePlusWriter = null;

        //存放boss比数据库多的记录
        File bossRecordsPlusFile = new File(getLocalChangeRecordsPath() + File.separator + getBossRecordsPlusName()
                + twoDayBefore.replaceAll("-", "") + ".txt");
        BufferedWriter bossRecordsPlusWriter = null;

        //存放boss无法生成话单的记录
        File bossRecordsErrorFile = new File(getLocalChangeRecordsPath() + File.separator + getBossrecordsErrorName()
                + twoDayBefore.replaceAll("-", "") + ".txt");
        BufferedWriter bossRecordsErrorWriter = null;

        try {
            changeStatusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(changeStatusFile),
                    "utf-8"));
            changePlusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(changePlusFile), "utf-8"));
            bossRecordsPlusWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(bossRecordsPlusFile), "utf-8"));
            bossRecordsErrorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    bossRecordsErrorFile), "utf-8"));

            Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
            writerMap.put(changeStatusFileName, changeStatusWriter);
            writerMap.put(bossRecordsPlusName, bossRecordsPlusWriter);
            writerMap.put(bossrecordsErrorName, bossRecordsErrorWriter);
            writerMap.put(changePlusFileName, changePlusWriter);

            details = reconcileByIntervalTime(dbModels, billMap, twoDayBefore.replaceAll("-", ""), userOrderMap,
                    discountMap, writerMap);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (changeStatusWriter != null) {
                try {
                    changeStatusWriter.close();
                } catch (IOException e) {
                }
            }
            if (bossRecordsPlusWriter != null) {
                try {
                    bossRecordsPlusWriter.close();
                } catch (IOException e) {
                }
            }
            if (bossRecordsErrorWriter != null) {
                try {
                    bossRecordsErrorWriter.close();
                } catch (IOException e) {
                }
            }
            if (changePlusWriter != null) {
                try {
                    changePlusWriter.close();
                } catch (IOException e) {
                }
            }
        }

        //5.生成话单文件
        generateRecordFile(details, twoDayBefore);

        //6.上传话单文件
        logger.info("处理对账和生成话单完成,开始上传");
        if (!verifyUseFtp()) {
            logger.info("测试开关打开，不从FTP服务上下账账单文件，需保证已手动上传至指定目录");
        } else {
            uploadHuadan(twoDayBefore);
        }

        //7.邮件发送所有文件
        if (isSendEmail()) {
            logger.info("开始发送邮件");
            sendEmail(twoDayBefore.replaceAll("-", ""), true);
        }

        //8.统计当天对账结果总额，插入到数据库中
        SdDailystatistic statictic = getDailystatistic(twoDayBefore, onefayBefore);
        sdDailystatisticService.insert(statictic);

        return true;
    }

    /**
     * 每日的对账任务，结合了所有的流程
     */
    @Override
    public boolean doMonthlyJob() {

        //每月全量文件名称格式：DBOSS_MON_3009_20160702011143.0000066470.gz
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String dateString = dateFormat.format(DateUtil.getCurrentDateOfFewMonths(new Date(), -1));
        logger.info("开始本月的对账任务" + dateFormat.format(new Date()));

        //如果允许从ftp下载且失败，则返回false
        if (!verifyUseFtp()) {
            logger.info("测试开关打开，不从FTP服务上下账账单文件，需保证已手动上传至指定目录");
        } else if (!downloadMonthFile(dateString)) {
            logger.error("下载账单失败");
            return false;
        }

        String sourceFileName = "DBOSS_MON_3009_" + dateString + ".txt";

        File sourceFile = new File(getLocalBillFilePath() + File.separator + sourceFileName);

        //每行记录
        //3、按天拆分文件,到指定目录
        List<File> files = splitFile(sourceFile, getLocalBillFilePath());

        //4.从数据库得到所有的userOrder，主键为userOrder.getUserID()+"_"+userOrder.getBizId()
        Map<String, UserOrder> userOrderMap = getAllUserOrdersToMap();

        //数据库多余的记录
        String changeRecordFileName = getLocalChangeRecordsPath() + File.separator + "changeRecord" + "_MON_"
                + dateString + ".txt";
        BufferedWriter changeStatusWriter = null;

        //boss多余的记录
        String bossRecordsPlusFileName = getLocalChangeRecordsPath() + File.separator + "bossPlus" + "_MON_"
                + dateString + ".txt";
        BufferedWriter bossRecordsPlusWriter = null;

        String dbChangePlusFileName = getLocalChangeRecordsPath() + File.separator + "changePlus" + "_MON_"
                + dateString + ".txt";
        BufferedWriter changePlusWriter = null;

        //boss错误的记录
        String bossRecordsErrorFileName = getLocalChangeRecordsPath() + File.separator + "bossError" + "_MON_"
                + dateString + ".txt";
        BufferedWriter bossRecordsErrorWriter = null;
        try {
            changeStatusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(changeRecordFileName),
                    "UTF-8"));
            bossRecordsPlusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    bossRecordsPlusFileName), "UTF-8"));
            bossRecordsErrorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    bossRecordsErrorFileName), "UTF-8"));
            changePlusWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    dbChangePlusFileName), "UTF-8"));

            Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
            writerMap.put(changeStatusFileName, changeStatusWriter);
            writerMap.put(bossRecordsPlusName, bossRecordsPlusWriter);
            writerMap.put(bossrecordsErrorName, bossRecordsErrorWriter);
            writerMap.put(changePlusFileName, changePlusWriter);

            if (files != null && files.size() > 0) {
                for (File file : files) {

                    //从文件名中获取日期信息,日期格式yyyy-MM-dd
                    String date = file.getName().substring(0, file.getName().length() - 4);

                    //0826 新增的代码，根据账单里的userId和prdCode，查询数据库找到对账当天涉及到的所有的折扣,放在分析账单里增加数据，生成话单时使用该数据
                    //主键为userid_prdcode
                    Map<String, List<DiscountRecord>> discountMap = new HashMap<String, List<DiscountRecord>>();

                    //从昨天的账单中得到整理出map   yyyy-MM-dd.txt  , 第二个参数只要传yyyyMMdd，不需要之前一天的值
                    Map<String, List<BillDetail>> map = analyseBill(date.replaceAll(getDestBillFileName(), ""),
                            date.replaceAll("-", ""), discountMap);

                    //从数据库中找寻到相关记录
                    List<ServiceModel> models = getDailyRecordFromDB(date.replaceAll(getDestBillFileName(), ""));

                    //进行对账，并且生成所有的话单对象。按照一日一笔的处理
                    reconcileByIntervalTime(models, map, date, userOrderMap, discountMap, writerMap);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (changeStatusWriter != null) {
                try {
                    changeStatusWriter.close();
                } catch (IOException e) {
                }
            }
            if (bossRecordsPlusWriter != null) {
                try {
                    bossRecordsPlusWriter.close();
                } catch (IOException e) {
                }
            }
            if (bossRecordsErrorWriter != null) {
                try {
                    bossRecordsErrorWriter.close();
                } catch (IOException e) {
                }
            }
        }

        if (isSendEmail()) {
            sendEmail(dateString, false);
        }

        //删除所有的临时文件
        for (File file : files) {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
        }
        return true;
    }

    /**
     * 判断是否允许下载
     */
    private boolean verifyUseFtp() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.SD_NOUSE_FTP.getKey());
        return (value == null || !"ok".equalsIgnoreCase(value));
    }

    /**
     * 得到需要下载文件的前缀名
     *
     * @return 得到需要下载文件的前缀名
     */
    protected abstract boolean downloadFile(String date);

    /**
     * 下载文件使用,fileNameContain为包含的文件名，isDaily为是否日对账
     */
    protected abstract void sendEmail(String fileNameContain, boolean isDaily);

    /**
     * 通过日期,从ftp服务器上得到文件，保存到指定路径下
     *
     * @param date 格式 2016-11-11 或 20161111
     */
    protected boolean downloadFile(String date, String srcFileName, String destFileName) {

        //将文件名中去掉-
        String dateFile = date.replaceAll("-", "");

        //得到需要下载文件的前缀名
        String buzzyFileName = srcFileName + dateFile;

        logger.info("下载文件名格式为" + buzzyFileName);

        //从ftp上下载指定文件，存放到服务器上的 getLocalBillFilePath()路径下，下载失败返回空
        String gzFileName = FTPUtil.downFileFuzzyName(getFtpUrl(), ftpPort, getFtpLoginName(), getFtpLoginPass(),
                getFtpFilePath(), buzzyFileName, getLocalBillFilePath());

        if (StringUtils.isEmpty(gzFileName)) {
            logger.info("下载文件失败");
            return false;
        }

        logger.info("下载文件名格式为" + buzzyFileName + "成功");

        if (!UncompressFileGZIP.doUncompressFile(getLocalBillFilePath() + File.separator + gzFileName)) {
            logger.error("解压文件" + buzzyFileName + "失败");
            return false;
        }

        //得到话单文件的名称 如DBOSS_3009_20160823011140.0000074259
        String huadanFileName = gzFileName.substring(0, gzFileName.length() - 3);

        //将DBOSS_3009_20160823011140.0000074259 改名为  20160823.txt
        File sourceFile = new File(getLocalBillFilePath() + File.separator + huadanFileName);
        File destFile = new File(getLocalBillFilePath() + File.separator + destFileName + dateFile + ".txt");

        try {
            FileUtils.moveFile(sourceFile, destFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 得到需要下载文件的前缀名
     *
     * @return 得到需要下载文件的前缀名
     */
    public abstract String getSrcBillFileName();

    /**
     * FTP下载后的话单文件名
     *
     * @return FTP下载后的文件名
     */
    public abstract String getDestBillFileName();

    /**
     * 下载成功返回文件名，失败fanhu
     */
    public boolean downloadMonthFile(String dateString) {

        String monthFullFile = "DBOSS_MON_3009_" + dateString;

        //从FTP上下载指定文件到本地服务器指定目录
        String huadanFileName = FTPUtil.downFileFuzzyName(getFtpUrl(), ftpPort, getFtpLoginName(), getFtpLoginPass(),
                getFtpFilePath(), monthFullFile, getLocalBillFilePath());
        if (StringUtils.isEmpty(huadanFileName)) {
            logger.info("下载每月全量文件失败，任务结束！");
            return false;
        }

        //2、解压文件
        if (!UncompressFileGZIP.doUncompressFile(getLocalBillFilePath() + File.separator + huadanFileName)) {
            logger.info("解压每月全量文件失败，任务结束！，fileName=" + huadanFileName);
            return false;
        }

        //3、将DBOSS_3009_20160823011140.0000074259 改名为  "DBOSS_MON_3009_201608.txt
        File sourceFile = new File(getLocalBillFilePath() + File.separator + huadanFileName);
        File destFile = new File(getLocalBillFilePath() + File.separator + "DBOSS_MON_3009_" + dateString + ".txt");

        try {
            FileUtils.moveFile(sourceFile, destFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 上传某日的话单到服务器上 date格式 2016-11-11 或 20161111
     */
    public boolean uploadHuadan(String dateFile) {
        //将文件名中去掉-
        String date = dateFile.replaceAll("-", "");

        logger.info("开始今天的上传话单任务");
        String fileName = getBillPath(date);
        logger.info("本次上传的话单的名称为：" + fileName);

        FileInputStream in = null;
        try {
            File file = new File(fileName);
            in = new FileInputStream(file);

            //ftp将文件上传给开放平台
            boolean flag = FTPUtil.uploadFile(getFtpUrl(), ftpPort, getFtpLoginName(), getFtpLoginPass(),
                    getFtpFilePath(), file.getName(), in);
            if (!flag) {
                logger.error("上传到ftp服务器失败,文件名为: " + file.getName());
                return false;
            } else {
                logger.error("上传到ftp服务器成功,文件名为: " + file.getName());
            }

            //发送给开放平台通知对方上传
            String noticeUrl = getNoticeUrl() + "?filename=" + file.getName() + "&product=liuliang";
            logger.info("开始调用开放平台地址: " + noticeUrl);
            HttpUtils.get(noticeUrl);
            return true;

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

    }

    /**
     * 从账单文件读取文件，处理完成后保存到Map<String, List<BillDetail>>中
     *
     * 0826新增discountMap，从数据库读取dateStr当天所有的折扣变化记录，存放在discountMap中
     *
     * billDate为boss账单的日期 recordDate为账单内部数据的日期，比boss账单的日期少一天
     *
     * TODO 缺少罗祖武的discount类对象，本次上传先不处理
     */
    public Map<String, List<BillDetail>> analyseBill(String billDate, String recordDate,
                                                     Map<String, List<DiscountRecord>> discountMap) {
        //将文件名中去掉-
        String billDateVrai = billDate;
        //String recordDateVrai = recordDate.replaceAll("-", "");

        String billFileName = getLocalBillFilePath() + File.separator + getDestBillFileName() + billDateVrai + ".txt";

        //读文件，将所有内容保存在map
        BufferedReader reader = null;
        try {
            Map<String, List<BillDetail>> recordMap = new HashMap<String, List<BillDetail>>();
            File file = new File(billFileName);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                BillDetail bill = geneBillFromStr(tempString);
                if (bill == null) {
                    continue;
                }

                //String key = bill.getMsisdn() + "_" + bill.getUserid() + "_" + bill.getBizid();
                String key = getBillMapKey(bill);
                if (StringUtils.isEmpty(key)) {
                    continue;
                } else if (recordMap.containsKey(key)) {
                    List<BillDetail> list = recordMap.get(key);
                    list.add(bill);
                } else {
                    List<BillDetail> list = new ArrayList<BillDetail>();
                    list.add(bill);
                    recordMap.put(key, list);
                }

                //0826新增功能，填充discountMap
                if (!discountMap.containsKey(bill.getUserid() + "_" + bill.getBizid())) {
                    List<DiscountRecord> listDiscounts = discountService.getOneDayDiscount(recordDate,
                            bill.getUserid(), bill.getBizid());
                    discountMap.put(bill.getUserid() + "_" + bill.getBizid(), listDiscounts);
                }
            }

            return recordMap;

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return new HashMap<String, List<BillDetail>>();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return new HashMap<String, List<BillDetail>>();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new HashMap<String, List<BillDetail>>();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 得到recordMap的key值
     */
    public String getBillMapKey(BillDetail bill) {
        return bill.getMsisdn() + "_" + bill.getUserid() + "_" + bill.getBizid();
    }

    /**
     * 从数据库得到某一天的1092产品搜友充值成功记录 recordDateMinus的格式为 2016-11-14
     */
    public abstract List<ServiceModel> getDailyRecordFromDB(String recordDateMinus);

    /**
     * 得到所有的订单
     */
    private Map<String, UserOrder> getAllUserOrdersToMap() {
        Map<String, UserOrder> map = new HashMap<String, UserOrder>();
        List<UserOrder> list = supplierProductMapper.sdGetAllUserOrders();

        for (UserOrder userOrder : list) {
            if (userOrder.getUserID() != null && userOrder.getBizId() != null
                    && !map.containsKey(userOrder.getUserID() + "_" + userOrder.getBizId())) {
                map.put(userOrder.getUserID() + "_" + userOrder.getBizId(), userOrder);
            }
        }
        return map;
    }

    /**
     * @param recordList 数据库中当天的所有记录
     * @param billMap    账单中的记录，key为 手机+userid+bizid
     * @param date       对账的日期
     */
    public abstract List<RecordDetail> reconcileByIntervalTime(List<ServiceModel> recordList,
                                                               Map<String, List<BillDetail>> billMap, String date, Map<String, UserOrder> userOrderMap,
                                                               Map<String, List<DiscountRecord>> discountMap, Map<String, BufferedWriter> writersMap) throws IOException;

    /**
     * 将生成的文件话单类，生成到相应的文件中 date的格式为 2016-11-14 或 20161114
     */
    protected abstract boolean generateRecordFile(List<RecordDetail> detailList, String recordDate);

    /**
     * 上传每日的账单 recordDate的格式为 2016-11-14 或 20161114
     */
    public boolean uploadDailyHuadan(String recordDate) {
        logger.info("开始今天的上传话单任务");
        //将文件名中去掉-
        String date = recordDate.replaceAll("-", "");

        //"023000_0003.req"
        String huadanFileName = getLocalBillFileName(date);
        logger.info("本次上传的话单的名称为：" + huadanFileName);

        FileInputStream in = null;
        try {
            File file = new File(huadanFileName);
            in = new FileInputStream(file);

            //ftp将文件上传给开放平台
            boolean flag = FTPUtil.uploadFile(getFtpUrl(), ftpPort, getFtpLoginName(), getFtpLoginPass(),
                    getFtpFilePath(), file.getName(), in);
            if (!flag) {
                logger.error("上传到ftp服务器失败,文件名为: " + file.getName());
                return false;
            } else {
                logger.error("上传到ftp服务器成功,文件名为: " + file.getName());
            }

            //发送给开放平台通知对方上传
            String noticeUrl = getNoticeUrl() + "?filename=" + file.getName() + "&product=liuliang";
            logger.info("开始调用开放平台地址: " + noticeUrl);
            HttpUtils.get(noticeUrl);

            return true;

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

    }

    /**
     * 统计数据库的当天所有充值的价格 models 数据库的记录
     */
    public BillStatisticModel statisticDB(List<ServiceModel> models) {
        logger.info("all size:" + models.size());
        BillStatisticModel result = new BillStatisticModel();
        for (ServiceModel model : models) {

            if (!model.getStatus().equals(ChargeRecordStatus.COMPLETE.getCode())) {
                continue;
            }

            Integer discount = model.getOrderDiscount();
            if (discount == null) {
                discount = 100;
            }

            double originalPrize = NumberUtils.toDouble(String.valueOf(model.getPrice())) / 100;
            double discountPrize = originalPrize * discount / 100;

            result.setSuccessCount(result.getSuccessCount() + 1);
            result.setOriginalPrize(result.getOriginalPrize() + originalPrize);
            result.setDiscountPrize(result.getDiscountPrize() + discountPrize);

        }
        logger.info("all size:" + result.getSuccessCount());
        return result;
    }

    /**
     * 统计数据库的当天生成话单的相关数据 models 数据库的记录
     */
    public HuadanStatisticModel statisticHuadanFile(String date) {
        HuadanStatisticModel result = new HuadanStatisticModel();

        String hudadanFileName = getLocalBillFileName(date);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(getLocalHuadanPath() + File.separator
                    + hudadanFileName), "UTF-8"));
            String tempString = null;

            //2016041301300230694553|1|6328023841961|6328024651000||0|
            //109201|0102001|01|20160412104130|20160501120000|||||2|6.00|4.20|0|
            while ((tempString = reader.readLine()) != null) {
                String[] string = tempString.split("\\|");
                String type = string[8];

                //得到话单的原价
                double originalPrize = NumberUtils.toDouble(string[16]);
                //得到话单的折后价
                double discountPrize = NumberUtils.toDouble(string[17]);

                if ("01".equals(type)) {
                    result.setCount01(result.getCount01() + 1);
                    result.setOriginalPrize01(result.getOriginalPrize01() + originalPrize);
                    result.setDiscountPrize01(result.getDiscountPrize01() + discountPrize);
                } else if ("03".equals(type)) {
                    result.setCount03(result.getCount03() + 1);
                    result.setOriginalPrize03(result.getOriginalPrize03() + originalPrize);
                    result.setDiscountPrize03(result.getDiscountPrize03() + discountPrize);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return result;
    }

    /**
     * 统计当天账单生成账单的相关数据
     */
    public BillStatisticModel statisticDataBill(String date) {
        Map<String, Product> prdMap = getInitProductsShandong();

        Map<String, Integer> userIdMap = new HashMap<String, Integer>();

        BillStatisticModel result = new BillStatisticModel();
        String billFileName = date + ".txt";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(getLocalBillFilePath()
                    + File.separator + getDestBillFileName() + billFileName), "UTF-8"));
            String tempString = null;

            //2016041301300230694553|1|6328023841961|6328024651000||0|109201|0102001|01|20160412104130|20160501120000|||||2|6.00|4.20|0|
            //18763602715|00|20160703080745||5468028706098|109202|20160703080736|20160716120000
            while ((tempString = reader.readLine()) != null) {
                String[] string = tempString.split("\\|");
                if (string.length < 8) {
                    continue;
                }

                String prdCode = string[5];
                String userId = string[4];

                Product product = prdMap.get(prdCode);
                if (product == null) {
                    continue;
                }

                int discount;
                if (userIdMap.containsKey(userId)) {
                    discount = userIdMap.get(userId);
                } else {
                    Map<String, UserOrder> userOrderMap = getAllUserOrdersToMap();
                    UserOrder userOrder = userOrderMap.get(userId + "_" + prdCode);
                    if (userOrder == null) {
                        continue;
                    }
                    discount = NumberUtils.toInt(userOrder.getDiscount());
                    if (discount == 0) {
                        discount = 100;
                    }
                    userIdMap.put(userId, discount);
                }

                Double originalPrize = NumberUtils.toDouble(String.valueOf(product.getPrice())) / 100;
                Double discountPrize = originalPrize * discount / 100;

                result.setSuccessCount(result.getSuccessCount() + 1);
                result.setOriginalPrize(result.getOriginalPrize() + originalPrize);
                result.setDiscountPrize(result.getDiscountPrize() + discountPrize);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

        }

        return result;
    }

    /**
     * 将同一企业，产品的所有话单信息汇总，计算出一个话单，将当天所有的记录插入到list中
     */
    protected List<RecordDetail> getTotalRecord(Map<String, List<RecordDetail>> map, int countHuadan) {
        List<RecordDetail> list = new LinkedList<RecordDetail>();
        for (Entry<String, List<RecordDetail>> entry : map.entrySet()) {
            List<RecordDetail> records = entry.getValue();

            RecordDetail newDetail = calculTotalFromList(records, countHuadan);
            list.add(newDetail);
            list.addAll(records);

            countHuadan++;
        }
        return list;
    }

    /**
     * 将同一企业，产品的所有话单信息汇总，计算出一个话单，该话单计算以上所有话单的总价
     */
    protected abstract RecordDetail calculTotalFromList(List<RecordDetail> list, int serialNum);

    /**
     * 写到文件中
     */
    protected boolean writeContents(BufferedWriter writer, String content) {
        try {
            writer.write(content + "\r\n");
            logger.error("打印到文件：" + content);

            writer.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    protected abstract String getChangeStatusFileName();

    protected abstract String getBossRecordsPlusName();

    protected abstract String getBossrecordsErrorName();

    protected abstract String getChangeplusName();

    private List<File> splitFile(File file, String dirPath) {
        List<File> files = new ArrayList<File>();
        //读取文件
        FileInputStream rf = null;
        BufferedReader rbr = null;
        try {
            if (file.exists()) {
                File dir = new File(dirPath);
                if (!(dir.exists() && dir.isDirectory())) {
                    if (!dir.mkdir()) {
                        logger.error("创建新文件夹失败");
                    }
                }
            } else {
                return files;
            }
            rf = new FileInputStream(file);
            rbr = new BufferedReader(new InputStreamReader(rf, "utf-8"));
            String line = null;
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

            int currentLineNum = 1;
            while ((line = rbr.readLine()) != null) {
                //空行检测
                if (StringUtils.isEmpty(line)) {
                    logger.error("对账读取数据为空行,行数为" + currentLineNum);
                    currentLineNum++;
                    continue;//跳过，读下一行
                }

                /**
                 * record :账单的基本格式
                 * 格式：成员手机号|00|最后一次操作时间||集团用户ID（产品实例id） |产品规格（增值产品标示）|开始时间|结束时间
                 样例：13605339630|00|20160328071529||5338028221371|109206|20160328071805|20160401120000
                 */
                String[] array = line.split("\\|");

                //内容检测 1.必须有8列 2.第三列不为空且长度为14
                if (array.length < 8 || StringUtils.isEmpty(array[2]) || array[2].length() != 14) {
                    logger.error("对账读取数据出错，不进行处理,行数为" + currentLineNum + ",数据为" + line);
                    currentLineNum++;
                    continue;//跳过，读下一行
                }

                //按天划分记录
                String date = array[2].substring(0, 8);
                File tempFile = new File(dirPath + File.separator + getDestBillFileName()
                        + sdf2.format(sdf1.parse(date)) + ".txt");
                if (!tempFile.exists()) {//文件不存在新建文件
                    if (!tempFile.createNewFile()) {
                        logger.error("创建新文件失败");
                    }
                    files.add(tempFile);//改为无论存不存在都添加到list
                }

                //写入记录
                writeContentToFile(tempFile, line);
                currentLineNum++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rbr != null) {
                    rbr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }
    
    /**
     * 得到山东预设的产品，key值为109202
     */
    protected Map<String, Product> getInitProductsShandong() {
        Map<String, Product> map = new HashMap<String, Product>();

        List<SdBossProduct> products = sdBossProductService.getAllProducts();
        if (products != null && products.size() > 0) {
            for (SdBossProduct sdProduct : products) {
                if (sdProduct.getCode().startsWith("1092")) {
                    Product product = new Product();
                    product.setProductCode(sdProduct.getCode());
                    product.setPrice(sdProduct.getPrice().intValue());
                    map.put(sdProduct.getCode(), product);
                }
            }
        }
        return map;
    }

    /**
     * 将话单，账单，数据库记录做统计，插入数据库
     */
    private SdDailystatistic getDailystatistic(String huadanDate, String billDate) {
        SdDailystatistic statistic = new SdDailystatistic();

        String recordDate = huadanDate.replaceAll("-", "");

        String billDateNoMinus = billDate.replaceAll("-", "");

        List<ServiceModel> dbModels = getDailyRecordFromDB(huadanDate);

        //数据库统计模型
        BillStatisticModel dbBillModel = statisticDB(dbModels);

        //账单统计模型
        BillStatisticModel billModel = statisticDataBill(billDateNoMinus);

        //话单统计模型
        HuadanStatisticModel huadanModel = statisticHuadanFile(recordDate);

        //将所有的值设置到统计对象中
        statistic.setDate(recordDate);

        statistic.setBillCount(billModel.getSuccessCount());
        statistic.setBilOriginprice(billModel.getOriginalPrize());
        statistic.setBillDiscountprice(billModel.getDiscountPrize());

        statistic.setDatabaseCount(dbBillModel.getSuccessCount());
        statistic.setDatabaseOriginprice(dbBillModel.getOriginalPrize());
        statistic.setDatabaseDiscountprice(dbBillModel.getDiscountPrize());

        statistic.setHuadanoneCount(huadanModel.getCount01());
        statistic.setHuadanoneOriginprice(huadanModel.getOriginalPrize01());
        statistic.setHuadanoneDiscountprice(huadanModel.getDiscountPrize01());

        statistic.setHuadanthreeCount(huadanModel.getCount03());
        statistic.setHuadanthreeOriginprice(huadanModel.getOriginalPrize03());
        statistic.setHuadanthreeDiscountprice(huadanModel.getDiscountPrize03());

        return statistic;
    }

    /**
     * 得到今天话单的完整文件名称和路径
     */
    private String getBillPath(String date) {

        String outFileName = getLocalBillFileName(date.replaceAll("-", ""));

        return getLocalHuadanPath() + File.separator + outFileName;
    }

    protected String getFtpUrl() {
        //return "127.0.0.1";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_FTP_URL.getKey());
    }

    protected String getFtpLoginName() {
        //return "qihang";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_FTP_LOGINNAME.getKey());
    }

    protected String getFtpLoginPass() {
        //return "xiaoqi160";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_FTP_LOGINPASS.getKey());
    }

    protected String getFtpFilePath() {
        //return "aaa";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_FTP_FILEPATH.getKey());
    }

    /**
     * 得到本地账单的存放路径
     */
    protected abstract String getLocalBillFilePath();

    /**
     * 得到本地账单的存放文件名
     */
    protected abstract String getLocalBillFileName(String date);

    /**
     * 得到本地话单的存放路径
     */
    protected abstract String getLocalHuadanPath();

    /**
     * 得到本地生成对账后文件的路径
     */
    protected abstract String getLocalChangeRecordsPath();

    /**
     * 得到开放平台通知的url路径
     */
    protected String getNoticeUrl() {
        //return "127.0.0.1";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_NOTICE_URL.getKey());
    }

    /**
     * 解析话单
     *
     * @param record 话单记录
     * @return 话单对象
     */
    protected abstract BillDetail geneBillFromStr(String record);

    /**
     * 给出一个账单对象eachDetail，在所有的数据库对象List<ServiceModel>中找出一个充值时间匹配的的记录，并返回，若没有则返回空
     */
    protected abstract ServiceModel getSuitableRecord(BillDetail eachDetail, List<ServiceModel> recordList);

    /**
     * 从账单和数据库找出的记录中，合并生成话单的基本信息 0826新增，使用在账单中的记录，在discountMap表中查询当天符合时间的折扣，若找到则使用该折扣，没有的话使用数据库userorder里的折扣
     * TODO 等待罗祖武的折扣功能
     */
    protected abstract RecordDetail geneFromBillModel(BillDetail bill, Map<String, UserOrder> mapUserOrders,
                                                      Map<String, Product> mapPrdsMap, int serialNumber, Map<String, List<DiscountRecord>> discountMap);

    /**
     * 是否发送邮件,为空，或为on均为发送
     */
    protected boolean isSendEmail() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_EMAIL.getKey());
        return (value == null || "ON".equalsIgnoreCase(value));
    }
}
