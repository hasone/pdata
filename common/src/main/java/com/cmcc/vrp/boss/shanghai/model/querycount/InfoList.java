package com.cmcc.vrp.boss.shanghai.model.querycount;

/**
 * @author lgk8023
 *
 */
public class InfoList {
    
    private String itemId;
    
    private String itemName;
    
    private String notes;
    
    private String sts;
    
    private String susableFlag;
    
    private String totalFee;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getSusableFlag() {
        return susableFlag;
    }

    public void setSusableFlag(String susableFlag) {
        this.susableFlag = susableFlag;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

}
