package com.cmcc.vrp.boss;

/**
 * 从BOSS侧同步账户的结果
 * <p>
 * Created by sunyiwei on 2016/10/10.
 */
public class SyncAccountResult {
    final public static SyncAccountResult SUCCESS = new SyncAccountResult("success", true);
    private String result;
    private Boolean success;//是否成功的标识，成功=true

    public SyncAccountResult(String result, Boolean success) {
        this.result = result;
        this.setSuccess(success);
    }

    public static boolean isSuccess(SyncAccountResult sar) {
        if(sar!=null && sar.getSuccess()!=null){
            return sar.getSuccess();
        }else{
            return false;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
