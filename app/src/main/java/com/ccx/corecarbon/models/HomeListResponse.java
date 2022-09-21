package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeListResponse {

    @SerializedName("error")
    public boolean error;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<HomeDataModel> data;


    public HomeListResponse(boolean error, String message, List<HomeDataModel> data) {
        this.error = error;
        this.message = message;
        this.data = data;
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

    public List<HomeDataModel> getData() {
        return data;
    }

    public void setData(List<HomeDataModel> data) {
        this.data = data;
    }
}
