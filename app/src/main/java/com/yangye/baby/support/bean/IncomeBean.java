package com.yangye.baby.support.bean;

import java.io.Serializable;

/**
 * 收入实体Bean
 * @description：
 * @author ldm
 * @date 2015-7-16 下午5:05:31
 */
public class IncomeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String year;
	private String month;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String day;
	private String time;
	private String mamilk;
	private String milk;
    private String other;
    private String total;
    private int isFirstDay;//是否是当月帐单的第一条数据0表示否，1表示是
    private int isFirstYear;// 是否是当年帐单的第一条数据0表示否，1表示是

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMamilk() {
        return mamilk;
    }

    public void setMamilk(String mamilk) {
        this.mamilk = mamilk;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
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




	@Override
	public String toString() {
		return "IncomeBean [year=" + year + ", month=" + month + ", time=" + time + ", mamilk=" + mamilk + ", milk=" + milk + ", other=" + other + ", total=" + total + ", isFirstDay=" + isFirstDay + "]";
	}

}
