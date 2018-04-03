package com.cmcc.vrp.province.model;
/**
 * 营销卡统计数量对象
 * create by qinqinyan on 2017/08/11
 * 
 * newCount: "新制卡"
 * storedCount: "已签收 "
 * activatedCount: "已激活"
 * usedCount: "已使用 "
 * expiredCount: "已过期 "
 * lockedCount: "已锁定 "
 * deleteCount: "已销卡 "
 * totalCount: "总数 "
 * */
public class MdrcStatisticsCount {
    private Long newCount; 
    
    private Long storedCount;
    
    private Long activatedCount;
    
    private Long usedCount;
    
    private Long expiredCount;
    
    private Long lockedCount;
    
    private Long deleteCount;
    
    private Long totalCount;
    
    public MdrcStatisticsCount(){
        this.newCount = 0l;
        this.storedCount = 0l;
        this.activatedCount = 0l;
        this.usedCount = 0l;
        this.expiredCount = 0l;
        this.lockedCount = 0l;
        this.deleteCount = 0l;
        this.totalCount = 0l;
    }

    public Long getNewCount() {
        return newCount;
    }

    public void setNewCount(Long newCount) {
        this.newCount = newCount;
    }

    public Long getStoredCount() {
        return storedCount;
    }

    public void setStoredCount(Long storedCount) {
        this.storedCount = storedCount;
    }

    public Long getActivatedCount() {
        return activatedCount;
    }

    public void setActivatedCount(Long activatedCount) {
        this.activatedCount = activatedCount;
    }

    public Long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Long usedCount) {
        this.usedCount = usedCount;
    }

    public Long getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(Long expiredCount) {
        this.expiredCount = expiredCount;
    }

    public Long getLockedCount() {
        return lockedCount;
    }

    public void setLockedCount(Long lockedCount) {
        this.lockedCount = lockedCount;
    }

    public Long getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(Long deleteCount) {
        this.deleteCount = deleteCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
