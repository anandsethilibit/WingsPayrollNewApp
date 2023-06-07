package com.libit.wingspayroll.Model;

public class MonthlyAttendanceModel {
    String EMPNAME;
    String THUMBNO ;
    String Userid ;
    String Date;
    String Day;
    String Intime;
    String OutTime;
    String Duration;
    String LateIn;
    String LateOut;
    String OverTime;
    String Status;

    public String getEMPNAME() {
        return EMPNAME;
    }

    public void setEMPNAME(String EMPNAME) {
        this.EMPNAME = EMPNAME;
    }

    public String getTHUMBNO() {
        return THUMBNO;
    }

    public void setTHUMBNO(String THUMBNO) {
        this.THUMBNO = THUMBNO;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getIntime() {
        return Intime;
    }

    public void setIntime(String intime) {
        Intime = intime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getLateIn() {
        return LateIn;
    }

    public void setLateIn(String lateIn) {
        LateIn = lateIn;
    }

    public String getLateOut() {
        return LateOut;
    }

    public void setLateOut(String lateOut) {
        LateOut = lateOut;
    }

    public String getOverTime() {
        return OverTime;
    }

    public void setOverTime(String overTime) {
        OverTime = overTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
