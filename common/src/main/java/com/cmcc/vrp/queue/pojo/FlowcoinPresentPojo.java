/**
 *
 */
package com.cmcc.vrp.queue.pojo;

import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualAccount;

/**
 * <p>Title:FlowcoinPresentPojo </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年9月30日
 */
public class FlowcoinPresentPojo {
    private IndividualAccount IndividualAccount;

    private ActivityWinRecord activityWinRecord;

    /**
     * @return the individualAccount
     */
    public IndividualAccount getIndividualAccount() {
        return IndividualAccount;
    }

    /**
     * @param individualAccount the individualAccount to set
     */
    public void setIndividualAccount(IndividualAccount individualAccount) {
        IndividualAccount = individualAccount;
    }

    /**
     * @return the activityWinRecord
     */
    public ActivityWinRecord getActivityWinRecord() {
        return activityWinRecord;
    }

    /**
     * @param activityWinRecord the activityWinRecord to set
     */
    public void setActivityWinRecord(ActivityWinRecord activityWinRecord) {
        this.activityWinRecord = activityWinRecord;
    }

}
