package com.ccx.corecarbon.models.village;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VillageList {


    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("data")
    @Expose
    private List<VillageData> data = null;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<VillageData> getData() {
        return data;
    }

    public void setData(List<VillageData> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
