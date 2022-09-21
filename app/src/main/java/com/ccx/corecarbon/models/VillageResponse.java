package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VillageResponse {

    @SerializedName("error")
    public boolean error;

    @SerializedName("count")
    public int count;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<VillageResponse.Data> data;


    public VillageResponse(boolean error, int count, String message, List<Data> data) {
        this.error = error;
        this.count = count;
        this.message = message;
        this.data = data;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{

        @SerializedName("_id")
        public String _id;

        @SerializedName("villageCode")
        public String villageCode;

        @SerializedName("villageName")
        public String villageName;

        public Data(String _id, String villageCode, String villageName) {
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

        public String getVillagetName() {
            return villageName;
        }

        public void setVillagetName(String villagetName) {
            this.villageName = villagetName;
        }
    }

}
