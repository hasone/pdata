package com.cmcc.vrp.accounting;

import com.google.gson.Gson;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 对账
 * <p>
 * Created by sunyiwei on 2016/6/8.
 */
public class AccountingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountingTest.class);

    private static JedisPool jedisPool;
    static final String BOSS_PREFIX = "BOSS";
    static final  String SC_PREFIX = "SC";

    //BOSS侧的账单文件
    static final  String BOSS_FILE = "C:\\Users\\Lenovo\\Desktop\\BOSS.txt";

    //平台侧的账单文件
    static final  String SC_FILE = "C:\\Users\\Lenovo\\Desktop\\sc.txt";

    //错误输出文件
    static final  String ERROR_FILE = "C:\\Users\\Lenovo\\Desktop\\error.txt";

    static final  String RESULT_KEY = "compare.result";

    //    @BeforeClass
    public static void setUp() throws Exception {
        jedisPool = new JedisPool(buildConfig(), "192.168.11.157", 6379);

        Jedis jedis = jedisPool.getResource();
        jedis.flushDB();
        jedis.close();
    }

    private static JedisPoolConfig buildConfig() {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(10000);
        jpc.setTestOnBorrow(true);

        return jpc;
    }

    @Ignore
    @Test
    public void testAccount() throws Exception {
        //1. 读取BOSS文件
        LOGGER.info("开始从BOSS文件中读取数据.");
        readFromBossFile(BOSS_FILE);

        //2. 读取平台文件
        LOGGER.info("开始从平台文件中读取数据.");
        readFromPlatformFile(SC_FILE);

        //3. 开始处理
        LOGGER.info("开始处理读取的数据.");
        List<Delta> deltas = process();

        //4. 输出差异文件
        LOGGER.info("开始输出差异文件.");
        writeToErrorFile(ERROR_FILE, deltas);
    }

    @Ignore
    @Test
    public void testWriteFile() throws Exception {
        String resultContent = readResult();
        List<Delta> deltas = new Gson().fromJson(resultContent, List.class);
        writeToErrorFile(ERROR_FILE, deltas);
    }

    private String readResult() {
        Jedis jedis = jedisPool.getResource();
        String content = jedis.get(RESULT_KEY);
        jedis.close();

        return content;
    }

    private void writeToRedis(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        jedis.close();
    }

    private void writeToErrorFile(String filename, List<Delta> deltas) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        for (Delta delta : deltas) {
            formatter.format("%s %d %d %d%n", delta.getKey(), delta.getBossCount(), delta.getScSuccessCount(), delta.getScFailCount());
            formatter.flush();
        }

        FileUtils.writeStringToFile(new File(filename), stringBuilder.toString());
    }

    private Set<String> keys() {
        final Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(BOSS_PREFIX + "*");
        jedis.close();

        return keys;
    }

    private List<Delta> process() {
        final List<Delta> deltas = new LinkedList<Delta>();

        Set<String> keys = keys();
        final List<String> keysList = new LinkedList<String>(keys);

        final int COUNT = 100;
        final int size = keys.size();
        final int subCount = size / COUNT;
        ExecutorService executorService = Executors.newFixedThreadPool(COUNT);

        for (int i = 0; i < COUNT; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Jedis threadJedis = null;

                    try {
                        threadJedis = jedisPool.getResource();
                        int delta = index * subCount;
                        int end = Math.min(delta + subCount, size);
                        for (int j = delta; j < end; j++) {
                            String key = keysList.get(j);
                            process(threadJedis, key, deltas);
                        }
                    } finally {
                        if (threadJedis != null) {
                            threadJedis.close();
                        }
                    }
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return deltas;
    }

    private void readFromPlatformFile(String filename) throws IOException {
        String content = FileUtils.readFileToString(new File(filename), Charsets.UTF_8);

        Scanner scanner = new Scanner(content);
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();

        while (scanner.hasNext()) {
            String lineContent = scanner.nextLine();
            String[] comps = lineContent.split("\\|");

            String code = comps[0];
            String mobile = comps[1];
            String date = parseDate(comps[2]);
            String status = comps[3];
            String size = modify(comps[4]);

            incr(pipeline, buildKey(SC_PREFIX, code, mobile, date, status, size));
        }

        pipeline.sync();
        jedis.close();
    }

    private void readFromBossFile(String filename) throws IOException {
        String content = FileUtils.readFileToString(new File(filename), Charsets.UTF_8);

        Scanner scanner = new Scanner(content);
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();

        while (scanner.hasNext()) {
            String code = scanner.next();
            String mobile = scanner.next();
            String size = modify(scanner.next());
            String date = scanner.next();

            incr(pipeline, buildKey(BOSS_PREFIX, code, mobile, date, "3", size));
        }

        pipeline.sync();
        jedis.close();
    }

    private String parseDate(String date) {
        String tmp = date.substring(0, date.indexOf(" "));
        return tmp.replace("-", "");
    }

    private String modify(String size) {
        String tmp = size.replace("本地流量统付", "");
        return tmp.replace("MB", "M");
    }

    //处理单条记录
    private void process(Jedis jedis, String bossKey, List<Delta> deltas) {
        //分别获取BOSS侧与平台侧的数量，如果数量不一致，则加入错误列表
        int bossCount = NumberUtils.toInt(jedis.get(bossKey));
        int scSuccessCount = NumberUtils.toInt(jedis.get(changePrefix(bossKey)));
        int scFailCount = NumberUtils.toInt(jedis.get(changeStatus(bossKey)));
        int scTotalCount = scSuccessCount + scFailCount;

        if (bossCount != scTotalCount
            || scFailCount != 0) {
            synchronized (deltas) {
                deltas.add(new Delta(removePrefix(bossKey, false), bossCount, scSuccessCount, scFailCount));
            }
        }
    }

    private String removePrefix(String bossKey, boolean reserveLine) {
        return bossKey.substring(bossKey.indexOf("_") + (reserveLine ? 0 : 1));
    }

    private String changePrefix(String bossKey) {
        return SC_PREFIX + removePrefix(bossKey, true);
    }

    private String changeStatus(String bossKey) {
        String changePrefix = changePrefix(bossKey);
        return changePrefix.replace("_3_", "_4_");
    }

    private void incr(Pipeline pipeline, String key) {
        pipeline.incr(key);
    }

    private String buildKey(String prefix, String code, String mobile, String date, String status, String size) {
        return new StringBuilder()
            .append(prefix)
            .append("_")
            .append(code)
            .append("_")
            .append(mobile)
            .append("_")
            .append(date)
            .append("_")
            .append(status)
            .append("_")
            .append(size).toString();
    }

    private class Delta {
        private String key;
        private int bossCount;
        private int scSuccessCount;
        private int scFailCount;

        public Delta(String key, int bossCount, int scSuccessCount, int scFailCount) {
            this.key = key;
            this.bossCount = bossCount;
            this.scSuccessCount = scSuccessCount;
            this.scFailCount = scFailCount;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getBossCount() {
            return bossCount;
        }

        public void setBossCount(int bossCount) {
            this.bossCount = bossCount;
        }

        public int getScSuccessCount() {
            return scSuccessCount;
        }

        public void setScSuccessCount(int scSuccessCount) {
            this.scSuccessCount = scSuccessCount;
        }

        public int getScFailCount() {
            return scFailCount;
        }

        public void setScFailCount(int scFailCount) {
            this.scFailCount = scFailCount;
        }
    }
}
