package com.cmcc.vrp.queue.pojo;

import java.util.List;

/**
 * 批量赠送的块对象
 * <p>
 * Created by sunyiwei on 2016/8/29.
 */
public class BlockPresentPojo {
    private List<PresentPojo> pojos;
    private String serialNum;

    public List<PresentPojo> getPojos() {
        return pojos;
    }

    public void setPojos(List<PresentPojo> pojos) {
        this.pojos = pojos;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
