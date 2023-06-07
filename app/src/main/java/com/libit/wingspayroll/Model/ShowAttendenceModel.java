package com.libit.wingspayroll.Model;

public class ShowAttendenceModel {
    String Date,Intime,Outtime,TotalWorkingHours;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIntime() {
        return Intime;
    }

    public void setIntime(String intime) {
        Intime = intime;
    }

    public String getOuttime() {
        return Outtime;
    }

    public void setOuttime(String outtime) {
        Outtime = outtime;
    }

    public String getTotalWorkingHours() {
        return TotalWorkingHours;
    }

    public void setTotalWorkingHours(String totalWorkingHours) {
        TotalWorkingHours = totalWorkingHours;
    }
}
