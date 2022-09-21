package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class Village {

    @SerializedName("_id")
    public String _id;

    @SerializedName("villageCode")
    public String villageCode;

    @SerializedName("villageName")
    public String villageName;


    public Village(String _id, String villageCode, String villageName) {
        this._id = _id;
        this.villageCode = villageCode;
        this.villageName = villageName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
