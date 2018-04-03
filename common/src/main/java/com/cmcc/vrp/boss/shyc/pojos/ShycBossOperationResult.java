package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.shyc.enums.ErrorCode;

/**
 * 上海月呈的充值结果
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class ShycBossOperationResult extends AbstractBossOperationResultImpl {
    private String code;
    private String message;

    public ShycBossOperationResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ShycBossOperationResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * 判断当前操作结果是否成功
     *
     * @return true为成功，false为失败
     */
    @Override
    public boolean isSuccess() {
        return ErrorCode.SUCCESS.getCode().equals(code);
    }

    @Override
    public String getResultCode() {
        return code;
    }

    @Override
    public String getResultDesc() {
        return message;
    }

    /**
     * 是否为异步充值结果
     *
     * @return true表示当前结果为异步结果，否则为同步结果
     */
    @Override
    public boolean isAsync() {
        return true;
    }

    /**
     * 获取操作结果
     *
     * @return 返回操作结果，结果的内容和格式由各BOSS定义
     */
    @Override
    public Object getOperationResult() {
        return message;
    }

    /**
     * 是否需要入队列去查询
     */
    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
