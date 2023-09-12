package com.libit.wingspayroll.Model;

public class RequestModel {

    String Username;
    String Id ;
    String Status ;
    String SelectDate ;
    String FDate ;
    String Reason ;
    String TDate ;
    String Days;
    String LeaveRequestType;



    public String getImagepath() {
        return Imagepath;
    }

    public void setImagepath(String imagepath) {
        Imagepath = imagepath;
    }

    String Imagepath ;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    String UID ;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSelectDate() {
        return SelectDate;
    }

    public void setSelectDate(String selectDate) {
        SelectDate = selectDate;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getTDate() {
        return TDate;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getLeaveRequestType() {
        return LeaveRequestType;
    }

    public void setLeaveRequestType(String leaveRequestType) {
        LeaveRequestType = leaveRequestType;
    }
}


