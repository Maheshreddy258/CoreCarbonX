package com.ccx.corecarbon.models.district;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictList {


    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("data")
    @Expose
    private List<DistrictData> data = null;

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

    public List<DistrictData> getData() {
        return data;
    }

    public void setData(List<DistrictData> data) {
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
