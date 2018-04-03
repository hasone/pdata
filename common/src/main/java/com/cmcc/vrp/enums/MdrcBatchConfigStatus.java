package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: MdrcBatchConfigStatus
 * @Description: 规则状态，1未下载 2已下载，3已入库，4已激活
 * @author: luozuwu
 * @date: 2015年5月19日 上午10:41:55
 * 
 * edit by qinqinyan on 2017/08/02 for v1.21.1
 * 1：新制卡== 1：未下载
 * 2：制卡中==2：已下载
 * 3：已签收==3：已入库
 *       == 4：已激活（原有状态，已经无用）
 * 5：已邮寄（新增状态）
 * 6：已失效（新增状态）      下载失败
 *           l————————————>已失效(6) 
 * 怎个流程是  新制卡(1)——>制卡中(2)——>已邮寄(5)——>已签收(3)
 */
public enum MdrcBatchConfigStatus {
    /*NOT_DOWNLOAD(1, "未下载"),
    DOWNLOADED(2, "已下载"),
    STORED(3, "已入库"),
    ACTIVATED(4, "已激活");*/
    
    NOT_DOWNLOAD(1, "新制卡"),
    DOWNLOADED(2, "制卡中"),
    STORED(3, "已签收"),
    ACTIVATED(4, "已激活"),
    POST(5, "已邮寄"),
    USELESS(6, "已失效");
    //EXPIRED(7, "已过期");

    private Integer code;

    private String message;

    MdrcBatchConfigStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcBatchConfigStatus item : MdrcBatchConfigStatus.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
