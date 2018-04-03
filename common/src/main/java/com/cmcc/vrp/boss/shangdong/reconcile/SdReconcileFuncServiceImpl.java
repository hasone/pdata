package com.cmcc.vrp.boss.shangdong.reconcile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shangdong.reconcile.service.SdFlowCellBossReconcile;
import com.cmcc.vrp.boss.shangdong.reconcile.service.SdFlowPackageReconcile;
import com.cmcc.vrp.boss.shangdong.reconcile.service.SdFlowPackageSeqReconcile;
import com.cmcc.vrp.boss.shangdong.reconcile.service.SdFlowReconcileService;
import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.quartz.jobs.SdDailyReconcileJob;
import com.cmcc.vrp.province.quartz.jobs.SdMonthlyCleanAccountJob;
import com.cmcc.vrp.province.quartz.jobs.SdMonthlyReconcileJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SdDailystatisticService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 山东对账使用到的服务类，方便汇总时直接使用mock写UT
 *
 */
@Service
public class SdReconcileFuncServiceImpl implements SdReconcileFuncService{
    
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
    
    //@Autowired
    //@Qualifier("sdFlowPackageReconcile")
    //@Qualifier("sdFlowPackageSeqReconcile")
    private SdFlowReconcileService flowPackageReconcile;
    
    @Autowired
    @Qualifier("sdFlowCellReconcile")
    private SdFlowReconcileService flowCellReconcile;
    
    @Autowired
    @Qualifier("sdFlowCellBossReconcile")
    private SdFlowCellBossReconcile flowCellBossReconcile;
    
    @Autowired
    private ApplicationContext applicationContext;
     
    /**
     * 初始化函数，
     * 1.如果是PROVINCE_FLAG为sd，则配置日对账和月对账任务，否则取消且不配置任务
     * 2.判断是否为测试组的测试环境，如果是则设定为5分钟，否则是每天4点半执行，每月2日6点执行
     */
    @PostConstruct
    public void init(){
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
         
        String dailyCronExp = "";
        String monthlyCronExp = "";
        String monthlyCleanFlowAccountCronExp = "";
		
		//取消掉原任务，若不存在直接返回
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "SdDailyReconcileJob");
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "SdMonthlyReconcileJob");
        scheduleService.undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), "SdMonthlyCleanAccountJob");
        
        if(!"sd".equals(provinceFlag)){
            //非山东环境，不设置任何对账的任务
            return;            
        }else if(isReconcileForTest()){
            //测试组测试对账，设定日对账和月对账间隔时间为5分钟
            dailyCronExp = "0 0/5 * * * ?";
            monthlyCronExp = "0 0/5 * * * ?";
            //每小时启动清空流量池账户任务
            monthlyCleanFlowAccountCronExp = "0 0 0/1 * * ?";
        }else{
            //线上每日对账，每日4点半执行    
            dailyCronExp = "0 30 4 * * ? *";
            //线上每月对账，每月2日6点执行
            monthlyCronExp = "0 0 6 2 * ?";
            //每月倒数第三天23:59:59启动清空流量池账户任务
            monthlyCleanFlowAccountCronExp = "59 59 23 L-3 * ?";
        }
        
        
        //设定定时任务
        scheduleService.createCronTrigger(SdDailyReconcileJob.class, dailyCronExp, 
                "SdDailyReconcileJob", SchedulerGroup.DEFAULT.getCode());
        scheduleService.createCronTrigger(SdMonthlyReconcileJob.class, monthlyCronExp, 
                "SdMonthlyReconcileJob", SchedulerGroup.DEFAULT.getCode());
        scheduleService.createCronTrigger(SdMonthlyCleanAccountJob.class, monthlyCleanFlowAccountCronExp, 
                "SdMonthlyCleanAccountJob", SchedulerGroup.DEFAULT.getCode());
        
    }
    
    /**
     * 每日的对账任务，结合了所有的流程
     * @return
     */
    @Override
    public boolean doDailyJob() {
        if(isSeqReconcile()){
            flowPackageReconcile =  applicationContext.getBean("sdFlowPackageSeqReconcile", SdFlowPackageSeqReconcile.class);
        }else{
            flowPackageReconcile =  applicationContext.getBean("sdFlowPackageReconcile", SdFlowPackageReconcile.class);
        }
        
        //包对账
        flowPackageReconcile.doDailyJob();
        //池对账，boss,不再使用
        //flowCellBossReconcile.doDailyJob();
        //池对账，出话单,不再使用
        //flowCellReconcile.doDailyJob();
        
        
        return true;
    }
    
    
    @Override
    public boolean doMonthlyJob() {
        flowPackageReconcile =  applicationContext.getBean("sdFlowPackageReconcile", SdFlowPackageReconcile.class);
        
        flowPackageReconcile.doMonthlyJob();
        
        return true;
    }
    
    /**
     * 判断是否给测试组测试，只有为on是给测试组测试的
     */
    private boolean isReconcileForTest(){
        String value = globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_FOR_TEST.getKey());
        return (value!=null && "ON".equalsIgnoreCase(value));
    }
    
    /**
     * 判断是否使用流水号对账，只有on是使用流水号对账
     */
    private boolean isSeqReconcile(){
        String value = globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_USE_SEQ.getKey());
        return (value!=null && "ON".equalsIgnoreCase(value));
    }
}
