package com.cmcc.vrp.province.model.json;

import java.util.List;

import com.cmcc.vrp.province.model.ProductConverter;

/**
 * 前台转化到后台使用的json
 * @author Administrator
 *
 */
public class PrdInterditPage {
    List<ProductConverter> convertLists;

    public PrdInterditPage() {
        super();
    }

    public PrdInterditPage(List<ProductConverter> convertLists) {
        this.convertLists = convertLists;
    }

    public List<ProductConverter> getConvertLists() {
        return convertLists;
    }

    public void setConvertLists(List<ProductConverter> convertLists) {
        this.convertLists = convertLists;
    }
}
