package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel {

    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("password")
    private String password;


    @SerializedName("isMobileApp")
    private boolean isMobileApp;


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMobileApp() {
        return isMobileApp;
    }

    public void setMobileApp(boolean mobileApp) {
        isMobileApp = mobileApp;
    }
}
