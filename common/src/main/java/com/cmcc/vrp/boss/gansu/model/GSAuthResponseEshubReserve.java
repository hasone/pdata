/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年6月2日
 */
public class GSAuthResponseEshubReserve {

    private String eshubTransactionId;

    private String processTime;

    private String cutOffDay;

    public String getEshubTransactionId() {
        return eshubTransactionId;
    }

    public void setEshubTransactionId(String eshubTransactionId) {
        this.eshubTransactionId = eshubTransactionId;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getCutOffDay() {
        return cutOffDay;
    }

    public void setCutOffDay(String cutOffDay) {
        this.cutOffDay = cutOffDay;
    }


}
