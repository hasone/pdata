package com.cmcc.vrp.province.webin.controller.aop;

import com.cmcc.vrp.province.service.LabelConfigService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by panxin on 2016/9/13.
 */
@Aspect
@Component
public class ControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private LabelConfigService labelConfigService;

    /**
     * 
     * @Title: hainanBossOrder 
     * @Description: 海南BOSS订单
     * @return: void
     */
    @Pointcut("execution(* com.cmcc.vrp.province.webin.controller.PotentialCustomerController.indexPotential(..))")
    public void hainanBossOrder() {
    }

    /**
     * 
     * @Title: lotteryActivity 
     * @Description: TODO
     * @return: void
     */
   /* @Pointcut("execution(* com.cmcc.vrp.province.webin.controller.LotteryActivityController.createLottery(..))")
    public void lotteryActivity() {
    }

    *//**
    * @Title: redpacketActivity
    * @Description: 
    *//*
    @Pointcut("execution(* com.cmcc.vrp.province.webin.controller.EntRedpacketController.create(..))")
    public void redpacketActivity() {
    }

    *//**
    * @Title: goldenBallActivity
    * @Description: 
    *//*
    @Pointcut("execution(* com.cmcc.vrp.province.webin.controller.GoldenBallActivityController.createLottery(..))")
    public void goldenBallActivity() {
    }*/

    /**
    * @Title: around
    * @Description: 
    */
    @Around("hainanBossOrder()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Object obj = pjp.proceed();

            BaseController baseController = (BaseController) Class.forName(pjp.getTarget().getClass().getName())
                    .newInstance();
            if (StringUtils.isNotEmpty(baseController.getLocalLabel())) {
                HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
                req.setAttribute("localLabelConfig",
                        labelConfigService.getLabelConfigMap(baseController.getLocalLabel()));
                req.setAttribute("wholeLabelConfig", labelConfigService.getLabelConfigMap("WHOLE"));
            }

            return obj;
        } catch (Throwable throwable) {
            logger.error("invoke interface failed...", throwable);
        }
        return null;
    }
}
