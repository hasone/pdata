package com.cmcc.vrp.boss.shangdong.boss.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/** 
 * @ClassName:  BillDetail 
 * @Description:  成员账单详情
 * @author: qihang
 * @date:   2016年3月24日 下午7:16:24 
 *  
 */
public class BillDetail {
    private String msisdn;
    
    private String oprType;
    
    private String oprTime;
    
    private String ecid;
    
    private String userid;
    
    private String bizid;
    
    private String beginTime;
    
    private String endTime;
    
    private Long id=0L;
    
    /**
     * 流量大小
     */
    private String flow;
    
    /**
     * 流水号
     */
    private String pkqSeq;
    
    /**
     * 账期，默认为1
     */
    private String count;
    
    /**
     * record :账单的基本格式
     * 格式：成员手机号|00|最后一次操作时间||集团用户ID（产品实例id） |产品规格（增值产品标示）|开始时间|结束时间
        样例：13605339630|00|20160328071529||5338028221371|109206|20160328071805|20160401120000
     */
    public static BillDetail geneBillFromStr(String record){
        String[] params = record.split("\\|");
        
        if(params.length==8){
            BillDetail billDetail =new BillDetail();
            billDetail.setMsisdn(params[0]);
            billDetail.setOprType(params[1]);
            billDetail.setOprTime(params[2]);
            billDetail.setEcid(params[3]);
            billDetail.setUserid(params[4]);
            billDetail.setBizid(params[5]);
            billDetail.setBeginTime(params[6]);
            billDetail.setEndTime(params[7]);
            return billDetail;  
            
        }else if(params.length==9){
            BillDetail billDetail =new BillDetail();
            billDetail.setMsisdn(params[0]);
            billDetail.setOprType(params[1]);
            billDetail.setOprTime(params[2]);
            billDetail.setEcid(params[3]);
            billDetail.setUserid(params[4]);
            billDetail.setBizid(params[5]);
            billDetail.setBeginTime(params[6]);
            billDetail.setEndTime(params[7]);
            billDetail.setPkqSeq(params[8]);
            return billDetail;  
        }else{
            return null;
        }
    }
    
    /**
     * 将对象转换Str输出
     */
    public String toRecord(){
        StringBuffer buffer=new StringBuffer();
        buffer.append(getMsisdn()+"|");
        buffer.append(getOprType()+"|");
        buffer.append(getOprTime()+"|");
        buffer.append(getEcid()+"|");
        buffer.append(getUserid()+"|");
        buffer.append(getBizid()+"|");
        buffer.append(getBeginTime()+"|");
        buffer.append(getEndTime() + "|");
        if(StringUtils.isNotBlank(getPkqSeq())){
            buffer.append(getPkqSeq() + "|");
        }
        if(StringUtils.isNotBlank(getFlow())){
            buffer.append(getFlow()+ "|");
        }
        if(StringUtils.isNotBlank(getCount())){
            buffer.append(getCount());
        }
        return buffer.toString();
    }
    
    
    /**
     *   从账单对象的List中得到map对象
     *   map的key值为  手机号_ecid_spcode 例如：15905314004_5328045134560_100406
     */
    public static Map<String, List<BillDetail>> getBillsMap(List<BillDetail> totalbills){
        Map<String, List<BillDetail>> dataMap =new HashMap<String, List<BillDetail>>();
        for(BillDetail detail : totalbills){
            String key = detail.getMsisdn()+"_"+detail.getUserid()+"_"+detail.getBizid();
            if(dataMap.containsKey(key)){
                List<BillDetail> list = dataMap.get(key);
                list.add(detail);
            }else{
                List<BillDetail> list = new ArrayList<BillDetail>();
                list.add(detail);
                dataMap.put(key, list);
            }
            
        }
        return dataMap;
        
    }
    
    

    public String getMsisdn() {
        return msisdn;
    }

    public String getOprType() {
        return oprType;
    }

    public void setOprType(String oprType) {
        this.oprType = oprType;
    }

    public String getOprTime() {
        return oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public String getEcid() {
        return ecid;
    }

    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getPkqSeq() {
        return pkqSeq;
    }

    public void setPkqSeq(String pkqSeq) {
        this.pkqSeq = pkqSeq;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    
}
