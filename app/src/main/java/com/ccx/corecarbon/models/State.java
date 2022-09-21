package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class State {

    @SerializedName("_id")
    public String _id;

    @SerializedName("stateCode")
    public String stateCode;

    @SerializedName("stateName")
    public String stateName;

    @SerializedName("status")
    public boolean status;


    public State(String _id, String stateCode, String stateName, boolean status) {
        this._id = _id;
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.status = status;
    }

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
