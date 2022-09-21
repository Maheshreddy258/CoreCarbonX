package com.ccx.corecarbon.models.village;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VillageData {



    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("villageCode")
    @Expose
    private String villageCode;

    @SerializedName("villageName")
    @Expose
    private String villageName;

    @SerializedName("mandalId")
    @Expose
    private MandalId mandalId;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

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

    public MandalId getMandalId() {
        return mandalId;
    }

    public void setMandalId(MandalId mandalId) {
        this.mandalId = mandalId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
