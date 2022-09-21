package com.ccx.corecarbon.models.mandal;

import com.ccx.corecarbon.models.village.DistrictId;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MandalData {


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

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
