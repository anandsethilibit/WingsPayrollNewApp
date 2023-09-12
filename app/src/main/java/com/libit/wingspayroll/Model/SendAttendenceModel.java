package com.libit.wingspayroll.Model;

public class SendAttendenceModel {

    String
            EmpCode,
            atten_image,
            InAddress,
            InLatitude,
            InLongitude,
            EmpId,
            UnitId;

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
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

    public String getInLatitude() {
        return InLatitude;
    }

    public void setInLatitude(String inLatitude) {
        InLatitude = inLatitude;
    }

    public String getInLongitude() {
        return InLongitude;
    }

    public void setInLongitude(String inLongitude) {
        InLongitude = inLongitude;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }
}


