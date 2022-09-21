package com.ccx.corecarbon.models.village;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistrictId {

    @SerializedName("_id")
    @Expose
    private String _id;


    @SerializedName("districtCode")
    @Expose
    private String districtCode;


    @SerializedName("districtName")
    @Expose
    private String districtName;


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("label")
    @Expose
    private String label;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
