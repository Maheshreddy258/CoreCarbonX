package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class Mandal {

    @SerializedName("_id")
    public String _id;

    @SerializedName("mandalCode")
    public String mandalCode;

    @SerializedName("mandalName")
    public String mandalName;

    public Mandal(String _id, String mandalCode, String mandalName) {
        this._id = _id;
        this.mandalCode = mandalCode;
        this.mandalName = mandalName;
    }

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
}
