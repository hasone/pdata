package com.cmcc.vrp.boss.chongqing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.chongqing.enums.BossConnectionType;
import com.cmcc.vrp.boss.chongqing.service.request.impl.EnterOrderQueryRequest;
import com.cmcc.vrp.boss.chongqing.service.request.impl.EnterProQueryRequest;
import com.cmcc.vrp.boss.chongqing.service.request.impl.SingleSendServiceRequest;
import com.cmcc.vrp.boss.chongqing.service.response.impl.CQBOSSChargeResponse;
import com.cmcc.vrp.boss.chongqing.service.response.impl.EnterOrderQueryResponse;
import com.cmcc.vrp.boss.chongqing.service.response.impl.EnterProQueryResponse;
import com.cmcc.vrp.boss.chongqing.service.response.impl.SingleSendServiceResponse;
import com.cmcc.vrp.boss.chongqing.web.BossClient;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 重庆boss实现类，参考原重庆的代码,不做大范围的调整
 *
 * @author qihang
 */
@Component("cqBossService")
public class CQBossServiceImpl implements BossService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CQBossServiceImpl.class);

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    BossClient bossClient;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    AccountService accountService;
    
    @Autowired
    SupplierProductAccountService supplierProductAccountService;
    
    @Autowired
    EntSyncListService entSyncListService;
    
    EventLoopGroup workerGroup;
    
    @Autowired
    ChargeRecordService chargeRecordService;
    
    /**
     * 
     */
    @PostConstruct
    public void init(){
        workerGroup = new NioEventLoopGroup(100);
    }
    
    /**
     * 
     */
    @PreDestroy
    public void destroy(){
        workerGroup.shutdownGracefully();
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile,
                                      String serialNum, Long prdSize) {
        
        LOGGER.info("重庆充值start!");
        //检查参数
        SupplierProduct supplierProduct = null;
        if (splPid == null
            || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
            || (enterprisesService.selectByPrimaryKey(entId)) == null
            || StringUtils.isBlank(mobile)
            || StringUtils.isBlank(serialNum)) {
            LOGGER.info("调重庆BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return new CQBOSSChargeResponse("102", "参数错误");
        }
        
        //0411新增，通过chargeRecord表查找effType
        ChargeRecord chargeRecord =  chargeRecordService.getRecordBySN(serialNum);
        if(chargeRecord == null){
            LOGGER.info("调重庆BOSS充值接口失败：efftype参数错误. serialNum = {}");
            return new CQBOSSChargeResponse("102", "参数错误");
        }
        String isnext;
        Integer effectType = chargeRecord.getEffectType();
        if (effectType == null) {
            isnext = "0";
        } else {
            isnext = String.valueOf(effectType);
            if ("2".equals(isnext)) {
                isnext = "1";
            } else {
                isnext = "0";
            }
        }

        String productCode = supplierProduct.getCode();
        SupplierProductAccount supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(splPid);
        String entProCode = entSyncListService.getById(supplierProductAccount.getEntSyncListId()).getEntProductCode();
//    	if (!minusCount(splPid, 1.0)) {
//    	    return new CQBOSSChargeResponse("102", "扣减子账户失败");
//    	}

        //连接boss，返回结果
        CQBOSSChargeResponse response = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("telnum", entProCode);
            params.put("recnum", mobile);
            params.put("prodid", productCode);
            //0411新增，增加字段isNext，0当月生效，1次月生效
            params.put("isnext", isnext);

            LOGGER.info("start to connect BOSS[FingerPrint = {}.]", getFingerPrint());

            //定义两个对象，分别为boss赠送的发送处理类和接收处理类
            //发送类
            SingleSendServiceResponse singleSendServiceRecv = new SingleSendServiceResponse();//发送类
            //接收类
            SingleSendServiceRequest singleSendServiceSend = new SingleSendServiceRequest();

            bossClient.connect(BossConnectionType.SINGLE_SEND, params, singleSendServiceSend, singleSendServiceRecv,workerGroup);

            response = new CQBOSSChargeResponse(singleSendServiceRecv);

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            response = new CQBOSSChargeResponse("101", "Boss连接失败");

        } catch (Exception e) {//其它异常，如Boss连接失败
            LOGGER.error(e.getMessage());
            response = new CQBOSSChargeResponse("001", "boss连接异常");
        } finally {
            if (response == null) {
                addCount(splPid, 1.0);
            } else if (!response.isSuccess()
                    && !"999336".equals(response.getResultCode())) {
                addCount(splPid, 1.0);
            }
        }

        return response;
    }

    @Override
    public String getFingerPrint() {
        return "chongqing";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {

        return null;
    }

    //从boss同步产品类型和个数，分别插入
    //（1）entProduct (2)account
    /**
     * @param enterId
     * @return
     */
    public boolean syncronizedPrdsByEnterCode(Long enterId) {

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(enterId);
        if (enterprise == null) {
            LOGGER.error("没有找到相关企业ID" + enterId);
            return false;
        }

        String enterCode = enterprise.getCode();

        //从boss寻找产品,返回的是产品编码的list
        List<String> prdsList = getProductsOrderByEnterCode(enterCode);
        if (prdsList == null || prdsList.isEmpty()) {
            LOGGER.info("企业没有产品更新");
            return true;
        }

        //将boss找到的产品更新到数据库中，包括 1.entProduct 2.account
        List<Product> allPrds = new ArrayList<Product>();//记录所有的相关product
        Map<Long, Double> bossNumMap = new HashMap<Long, Double>(); //记录所有Product在boss端对应的个数,key:prdId value:个数

        //遍历所有的product编码,从supplierProduct找到所有的product，添加相应allPrds和bossNumMap
        for (String prd : prdsList) {
            SupplierProduct supplierProduct = supplierProductService.selectByCode(prd);
            if (supplierProduct == null) {
                continue;
            }

            List<Product> productsList = supplierProductMapService.getBySplPid(supplierProduct.getId());
            allPrds.addAll(productsList);


            String currentBossNumber = getEnterPrdSolde(enterCode, prd);
            for (Product product : productsList) {
                bossNumMap.put(product.getId(), NumberUtils.toDouble(currentBossNumber));
            }
        }

        List<String> codeList = getProductcodes(allPrds);//得到所有该企业的products编码
        entProductService.updateEnterpriseProduct(codeList, enterId);

        //创建账户
        LOGGER.info("开始创建企业帐户信息.");

        Map<Long, Double> infos = new HashMap<Long, Double>();
        for (Product product : allPrds) {
            if (accountService.get(enterId, product.getId(), AccountType.ENTERPRISE.getValue()) == null) {
                Double num = bossNumMap.get(product.getId());
                if (num == null) {
                    num = 0.00D;
                }
                infos.put(product.getId(), num);
            }
        }

        if (accountService.createEnterAccount(enterId, infos, DigestUtils.md5Hex(UUID.randomUUID().toString()))) {
            LOGGER.info("创建企业帐户成功.");
        } else {
            LOGGER.error("创建企业帐户失败.");
        }

        return true;
    }


    private List<String> getProductcodes(List<Product> prds) {
        List<String> codes = new ArrayList<String>();
        for (Product prd : prds) {
            if (!codes.contains(prd.getProductCode())) {
                codes.add(prd.getProductCode());
            }
        }
        return codes;
    }

    public List<String> getProductsOrderByEnterCode(String enterCode) {
        /*测试用
		 * List<String> prdsList =new ArrayList<String>();
         * List<String> prdsList =new ArrayList<String>();
		
		if(StringUtils.isEmpty(enterCode)){
			return prdsList;
		}
		
		prdsList.add("gl_mwsq_10M");
		prdsList.add("gl_mwsq_30M");
		prdsList.add("gl_mwsq_70M");
		prdsList.add("gl_mwsq_150M");
		prdsList.add("gl_mwsq_500M");
		prdsList.add("gl_mwsq_1G");
		prdsList.add("gl_mwsq_11G");
		

		return prdsList;*/

        List<String> prdsList = new ArrayList<String>();
        if (StringUtils.isEmpty(enterCode)) {
            return prdsList;
        }
        LOGGER.info("重庆开始查询企业拥有的产品!");
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("subsid", enterCode);


            LOGGER.info("start to connect BOSS[FingerPrint = {}.]", getFingerPrint());

            //定义两个对象，分别为boss赠送的发送处理类和接收处理类
            EnterOrderQueryRequest bossRequest = new EnterOrderQueryRequest();
            EnterOrderQueryResponse bossResponse = new EnterOrderQueryResponse();

            bossClient.connect(BossConnectionType.ENTERORDER_QUERY, params, bossRequest, bossResponse,workerGroup);

            if (bossResponse.getRetCode().equalsIgnoreCase("100")) {
                prdsList = (List<String>) bossResponse.getReturnContent();


            } else {//boss 返回的是错误编码
                LOGGER.info(" 企业编码：" + enterCode +
                    " 200272服务返回 retCode为" + bossResponse.getRetCode() + " retMsg为" + bossResponse.getRetMsg());
            }
            return prdsList;


        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            return prdsList;

        } catch (Exception e) {//其它异常，如Boss连接失败
            LOGGER.error(e.getMessage());
            return prdsList;
        }
    }

    public String getEnterPrdSolde(String enterCode, String prdCode) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("telnum", enterCode);
            params.put("prodid", prdCode);

            EnterProQueryRequest bossRequest = new EnterProQueryRequest();
            EnterProQueryResponse bossResponse = new EnterProQueryResponse();

            bossClient.connect(BossConnectionType.ENTERPRO_QUERY, params, bossRequest, bossResponse,workerGroup);

            if (bossResponse.getRetCode().equalsIgnoreCase("100")) {
                String remain = (String) bossResponse.getReturnContent();
                LOGGER.info("100249服务返回 retCode为" + bossResponse.getRetCode());

                //正确返回的是剩余个数
                return remain;
            } else {
                LOGGER.error("100249服务返回 retCode为" + bossResponse.getRetCode() + " retMsg为" + bossResponse.getRetMsg());

                return "";
            }


        } catch (InterruptedException e) {
            LOGGER.error("failed to connect BOSS, " + e.getMessage());
            return "";

        } catch (Exception e) {//其它异常，如Boss连接失败
            LOGGER.error("failed to connect BOSS, " + e.getMessage());
            return "";
        }
        /*测试用
		 * if(prdCode.equals("gl_mwsq_10M")){
			return "20";
		}
		return "10";*/
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
    /**
     * 扣减子账户余额
     * 
     * @param splPid
     * @param delta
     * @return
     */
    public boolean minusCount(Long splPid, Double delta) {
    	SupplierProductAccount supplierProductAccount = null;
    	if ((supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(splPid)) == null) {
    	    LOGGER.info("扣减子账户余额失败");
            return false;
    	}
    	Long supProAccountId = supplierProductAccount.getId();
    	supplierProductAccountService.updateById(supProAccountId, -delta);            	
    	LOGGER.info("扣减帐户余额信息成功.");
    	return true;
    }
    
    /**
     * 退款子账户余额
     * 
     * @param splPid
     * @param delta
     * @return
     */
    public boolean addCount(Long splPid, Double delta) {
    	SupplierProductAccount supplierProductAccount = null;
    	if ((supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(splPid)) == null) {
    	    LOGGER.info("退款子账户余额失败");
    	    return false;
    	}
    	Long supProAccountId = supplierProductAccount.getId();
    	supplierProductAccountService.updateById(supProAccountId, delta);            	
    	LOGGER.info("退款帐户余额信息成功.");
    	return true;
    }
}
