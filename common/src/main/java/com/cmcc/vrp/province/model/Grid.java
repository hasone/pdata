package com.cmcc.vrp.province.model;

import java.util.List;

/**
 * ajax返回实体类,包含所有的返回消息
 *
 * @author gzl
 * @date 2014年11月25日 下午4:44:45
 */
public class Grid {
    private List<Object> list;//返回的列表
    private String infoMsg;//返回的提示信息
    private String errorMsg;//返回的错误信息
    private Object obj;//返回的单个对象
    private int type;//返回的标识

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 
     */
    public void init() {
        this.list = null;
        this.infoMsg = null;
        this.errorMsg = null;
        this.obj = null;
        this.type = 0;
    }
}
