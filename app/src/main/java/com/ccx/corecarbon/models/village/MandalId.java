package com.ccx.corecarbon.models.village;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MandalId {


    @SerializedName("_id")
    @Expose
    private String _id;


    @SerializedName("mandalCode")
    @Expose
    private String mandalCode;


    @SerializedName("mandalName")
    @Expose
    private String mandalName;

    @SerializedName("districtId")
    @Expose
    private DistrictId districtId;


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

    public String getMandalCode() {
        return mandalCode;
    }

    public void setMandalCode(String mandalCode) {
        this.mandalCode = mandalCode;
    }

    public String getMandalName() {
        return mandalName;
    }

    public void setMandalName(String mandalName) {
        this.mandalName = mandalName;
    }

    public DistrictId getDistrictId() {
        return districtId;
    }

    public void setDistrictId(DistrictId districtId) {
        this.districtId = districtId;
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
