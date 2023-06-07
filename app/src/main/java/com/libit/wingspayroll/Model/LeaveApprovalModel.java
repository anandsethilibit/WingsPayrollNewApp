package com.libit.wingspayroll.Model;

public class LeaveApprovalModel {
    String ID ;
    String Type ;
    String Date;
    String EStatus ;
    String fdate ;
    String tdate ;
    String reason ;
    String UserName ;
    String RequestDate;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEStatus() {
        return EStatus;
    }

    public void setEStatus(String EStatus) {
        this.EStatus = EStatus;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }
}
