package com.yangye.baby.support.bean;

import java.io.Serializable;

/**
 * 收入实体Bean
 *
 * @author ldm
 * @description：
 * @date 2015-7-16 下午5:05:31
 */
public class ExpendsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String year;
    private String month;


    private String day;
    private String time;
    private int shi;
    private int niao;
    private String remark;
    private String total;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getShi() {
        return shi;
    }

    public void setShi(int shi) {
        this.shi = shi;
    }

    public int getNiao() {
        return niao;
    }

    public void setNiao(int niao) {
        this.niao = niao;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getIsFirstDay() {
        return isFirstDay;
    }

    public void setIsFirstDay(int isFirstDay) {
        this.isFirstDay = isFirstDay;
    }

    public int getIsFirstYear() {
        return isFirstYear;
    }

    public void setIsFirstYear(int isFirstYear) {
        this.isFirstYear = isFirstYear;
    }

    private int isFirstDay;// 是否是当月帐单的第一条数据0表示否，1表示是
    private int isFirstYear;// 是否是当年帐单的第一条数据0表示否，1表示是


    @Override
    public String toString() {
        return "ExpendsBean [year=" + year + ", month=" + month + ", time=" + time + ", shi=" + shi + ", niao=" + niao + ",  remark=" + remark + ", total=" + total + ", isFirstDay=" + isFirstDay + ", isFirstYear=" + isFirstYear + "]";
    }

}
