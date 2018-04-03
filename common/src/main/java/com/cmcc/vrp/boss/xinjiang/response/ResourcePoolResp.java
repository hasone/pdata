/**
 * @Title: 	ResourcePoolResp.java 
 * @Package com.cmcc.xinjiang.boss.model.response 
 * @author:	qihang
 * @date:	2016年3月30日 下午5:54:56 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ResourcePoolResp
 * @Description: 5.6. 集团用户产品流量池信息查询
 * @author: qihang
 * @date: 2016年3月30日 下午5:54:56
 * 
 * 
 *        一个企业可能会有多个流量池，转增时需要选择其中一个池，拿到userId进行充值
 */
public class ResourcePoolResp {

    private String resultCode;// 状态码,为0时才有流量池信息

    private String resultInfo;// 状态信息

    private String recordNum;// 流量池的个数

    private List<ResourcePool> pool;// 所有流量池的信息

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public List<ResourcePool> getPool() {
        return pool;
    }

    public void setPool(List<ResourcePool> pool) {
        this.pool = pool;
    }

    /**
     * 分析返回结果
     * 
     * @param bodyPacket
     * @return
     */
    public static ResourcePoolResp analyseRespBodyPacket(String bodyPacket) {
        Map<String, String> paramMap = GroupInfoResp.getResponseMap(bodyPacket);
        ResourcePoolResp resp = new ResourcePoolResp();

        resp.setResultCode(paramMap.get("X_RESULTCODE"));

        resp.setResultInfo(paramMap.get("X_RESULTINFO"));

        if (!resp.getResultCode().equals("0")) {
            resp.setRecordNum("0");
            resp.setPool(new ArrayList<ResourcePool>());
        } else {
            resp.setRecordNum(paramMap.get("X_RECORDNUM"));

            String[] allInitValues = paramMap.get("ALL_INIT_VALUE").split(",",
                    -1);
            String[] startDates = paramMap.get("START_DATE").split(",", -1);
            String[] userIds = paramMap.get("USER_ID").split(",", -1);
            String[] allValues = paramMap.get("ALL_VALUE").split(",", -1);
            String[] endDates = paramMap.get("END_DATE").split(",", -1);
            String[] resIds = paramMap.get("RES_ID").split(",", -1);

            List<ResourcePool> pools = new ArrayList<ResourcePool>();
            for (int i = 0; i < allInitValues.length; i++) {
                ResourcePool pool = new ResourcePool();
                pool.setAllInitValue(allInitValues[i]);
                pool.setStartDate(startDates[i]);
                pool.setUserId(userIds[i]);
                pool.setAllValue(allValues[i]);
                pool.setEndDate(endDates[i]);
                pool.setResId(resIds[i]);
                pools.add(pool);
            }

            resp.setPool(pools);

        }
        return resp;
    }

}

/*
 * <SvcInfo><Header><System><COMMUNICATE value="02" index="0" /><TRANSFER
 * value="01" index="0" /><ORGCHANNELID value="A001" index="0" /> <HOMECHANNELID
 * /></System><Inparam><CHANNEL_TRADE_ID value="E00320090401165020000000000000"
 * index="0" /></Inparam><Outparam><RESULT_CODE value="0" /> <RESULT_INFO
 * value="OK!" /></Outparam><TESTFLAG value="0" index="0" /><ACTIONCODE
 * value="1" index="0" /></Header><Body>
 * 
 * <SVC_CONTENT>{ X_RESULTINFO=["OK"], ALL_INIT_VALUE=["169984.0", "0.0",
 * "204800.0"], START_DATE=["20160125000000", "20160105000000",
 * "20160328000000"], USER_ID=["9016012501900494", "9016010501900015",
 * "9016032801901306"], ALL_VALUE=["169934.0", "0.0", "204800.0"],
 * END_DATE=["20170131235959", "20170131235959", "20170331235959"],
 * RES_ID=["210000000220400", "210000000178723", "210000000238618"],
 * X_RESULTCODE=["0"], X_RECORDNUM=["3"]}
 * 
 * </SVC_CONTENT></Body> </SvcInfo>
 */