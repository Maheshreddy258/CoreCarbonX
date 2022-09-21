package com.ccx.corecarbon.models.district;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateId {

    @SerializedName("_id")
    @Expose
    private String _id;


    @SerializedName("stateCode")
    @Expose
    private String stateCode;


    @SerializedName("stateName")
    @Expose
    private String stateName;


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

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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
