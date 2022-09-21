package com.ccx.corecarbon.models.district;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistrictData {

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("districtCode")
    @Expose
    private String districtCode;

    @SerializedName("districtName")
    @Expose
    private String districtName;

    @SerializedName("stateId")
    @Expose
    private StateId stateId;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;


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

    public StateId getStateId() {
        return stateId;
    }

    public void setStateId(StateId stateId) {
        this.stateId = stateId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
