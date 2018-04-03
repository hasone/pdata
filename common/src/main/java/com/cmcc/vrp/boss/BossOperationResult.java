package com.cmcc.vrp.boss;

/**
 * Boss操作结果的接口类
 * <p>
 * Created by sunyiwei on 2016/4/6.
 */
public interface BossOperationResult {
    /**
     * 获取操作结果的代码
     *
     * @return 返回操作结果代码，代码的含义由各渠道自行定义
     */
    String getResultCode();

    /**
     * 判断当前操作结果是否成功
     *
     * @return true为成功，false为失败
     */
    boolean isSuccess();

    /**
     * 是否为异步充值结果
     *
     * @return true表示当前结果为异步结果，否则为同步结果
     */
    boolean isAsync();

    /**
     * 获取操作结果的描述
     *
     * @return 返回操作结果的描述，描述信息由各BOSS渠道自行定义
     */
    String getResultDesc();

    /**
     * 获取操作结果
     *
     * @return 返回操作结果，结果的内容和格式由各BOSS定义
     */
    Object getOperationResult();

    /**
     * 是否需要入队列去查询
     *
     * @return
     */
    boolean isNeedQuery();

    /**
     * 获取指纹
     *
     * @return
     */
    public String getFingerPrint();


    /**
     * 获取流量平台的流水号
     *
     * @return
     */
    public String getSystemNum();

    /**
     * 获取企业ID
     *
     * @return
     */
    public Long getEntId();

}
