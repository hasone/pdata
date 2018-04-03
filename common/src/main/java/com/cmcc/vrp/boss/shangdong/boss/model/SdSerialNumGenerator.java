package com.cmcc.vrp.boss.shangdong.boss.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.cmcc.vrp.province.cache.AbstractCacheSupport;

/**
 * 山东云平台的流水号生成类
 * 
 * 补充规则
 * 流水号格式是否有限制？编码规则－4位平台机构编码+8位业务编码（BIPCode）+14位组包时间YYYYMMDDHH24MISS+6位流水号（定长），序号从000001开始，增量步长为1。
 * 如： LLPTBIPCP00320160929112335000001
 * LLPT           -4位平台机构编码 云平台为（YPT0)此处需要boss侧确认是否可用
 * BIPCP003       -8位业务编码（BIPCode)
 * 20160929112335 -14位组包时间YYYYMMDDHH24MISS
 * 000001         -6位流水号（定长）
 */
@Component
public class SdSerialNumGenerator extends AbstractCacheSupport{
      
    /**
     * 设置redis的key值
     */
    public static final String CacheKey = "ChargeSerialNum";
    
    /**
     * 设置redis的key值存活时间
     */
    public static final Integer CacheExistSecond = 60;
    
    /**
     * 4位平台机构编码 云平台为（YPT0)此处需要boss侧确认是否可用
     */
    public static final String CLOUD_SERIAL_PART_ONE = "LLPT";
    
    /**
     * 8位业务编码（BIPCode)
     */
    public static final String CLOUD_SERIAL_PART_TWO = "BIPCP003";
    
    /**
     * MAX_SEED,云平台定义的后六位为递增
     */
    private final static int MAX_SEED = 1000000;
    
    /**
     * 6位流水号（定长）
     * 从0开始到 999999，超过直接返回0，无限循环
     * 
     * 开始时随机生成0-999999中的某数
     */
    private static long CLOUD_SERIAL_NUM = (long)new Random().nextInt(MAX_SEED - 1) ;
    
    /**
     * 定义的转义类，int左边不足则补零
     */
    public static final DecimalFormat DECIMALFORMAT=new DecimalFormat("000000");
    
    
    /**
     * 得到最新云平台序列号的方法
     * synchronized 保证流水号唯一
     * 
     * 2017.01.05更新 该类只支持单机使用，多个实例会出现重复，仅仅保留用作redis中取不到值时使用的方法
     */
    public static synchronized String getNewSerialNum(){
        
        CLOUD_SERIAL_NUM++;
        CLOUD_SERIAL_NUM = CLOUD_SERIAL_NUM % MAX_SEED;      
        return generateSerialNum(CLOUD_SERIAL_NUM);
    }
    
    /**
     * 生成流水号的规则，CLOUD_SERIAL_NUM为流水号的数字
     */
    public static String generateSerialNum(Long serialNum){
        
        if(serialNum >= MAX_SEED ){
            serialNum = serialNum % MAX_SEED;
        }
        
        /**
         * 14位组包时间YYYYMMDDHH24MISS
         */
        SimpleDateFormat cloudDateFormate = new SimpleDateFormat("YYYYMMddHHmmss");
        
        StringBuffer buffer = new StringBuffer();
        //按顺序添加流水号内容
        buffer.append(CLOUD_SERIAL_PART_ONE);
        buffer.append(CLOUD_SERIAL_PART_TWO);
        buffer.append(cloudDateFormate.format(new Date()));
        buffer.append(DECIMALFORMAT.format(serialNum));
        
        return buffer.toString();
    }
    
    
    /**
     * 2017/01/05 更新，得到流水号规则，后6位递增流水号从缓存得到
     */
    public String getSdSerialNum(){
        Long cacheSerialNum = cacheService.getIncrOrUpdate(CacheKey, CacheExistSecond);
        if(cacheSerialNum == null  || cacheSerialNum <= 0L){ //没有从缓存得到了value值,只能之前使用默认的规则，防止redis出故障生成不成功
            return getNewSerialNum();
        }    
        return generateSerialNum(cacheSerialNum);
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        ExecutorService ex = Executors.newFixedThreadPool(100);
        
        for(int i=1;i<=1;i++){

            Runnable runnable = new Runnable() {
                
                @Override
                public void run() {
                    while(true){
                        System.out.println(SdSerialNumGenerator.getNewSerialNum());
                    }
                }
            };
            
            ex.execute(runnable);
        }
        
        while(ex.awaitTermination(10, TimeUnit.SECONDS)){
            
        }
        
        ex.shutdown();
    }

    @Override
    protected String getPrefix() {
       
        return "sd.";
    }
}
