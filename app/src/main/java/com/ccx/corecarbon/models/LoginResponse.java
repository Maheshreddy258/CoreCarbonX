package com.ccx.corecarbon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;


    @SerializedName("isAdmin")
    @Expose
    public boolean isAdmin;

    @SerializedName("isMobileApp")
    @Expose
    public String isMobileApp;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("agroAccess")
    @Expose
    public String agroAccess;

    @SerializedName("cookstoveAccess")
    @Expose
    public String cookstoveAccess;

    @SerializedName("role")
    @Expose
    public String role;


    @SerializedName("slNo")
    @Expose
    public int slNo;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("error")
    @Expose
    public boolean error;

    @SerializedName("message")
    @Expose
    public String message;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getIsMobileApp() {
        return isMobileApp;
    }

    public void setIsMobileApp(String isMobileApp) {
        this.isMobileApp = isMobileApp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgroAccess() {
        return agroAccess;
    }

    public void setAgroAccess(String agroAccess) {
        this.agroAccess = agroAccess;
    }

    public String getCookstoveAccess() {
        return cookstoveAccess;
    }

    public void setCookstoveAccess(String cookstoveAccess) {
        this.cookstoveAccess = cookstoveAccess;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
