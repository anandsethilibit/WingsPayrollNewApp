package com.libit.wingspayroll.Model;

public class MobileAttendanceModel {
    String Id;
    String Name;
    String EmpCode;
    String Date;
    String Days;
    String InTime;
    String OutTime;
    String atten_image;
    String InAddress;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getAtten_image() {
        return atten_image;
    }

    public void setAtten_image(String atten_image) {
        this.atten_image = atten_image;
    }

    public String getInAddress() {
        return InAddress;
    }

    public void setInAddress(String inAddress) {
        InAddress = inAddress;
    }
}

