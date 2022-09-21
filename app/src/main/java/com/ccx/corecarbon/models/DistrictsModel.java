package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictsModel {


    @SerializedName("error")
    public boolean error;

    @SerializedName("count")
    public int count;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<DistrictsModel.Data> data;


    public DistrictsModel(boolean error, int count, String message, List<Data> data) {
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

        @SerializedName("districtCode")
        public String districtCode;

        @SerializedName("districtName")
        public String districtName;

        @SerializedName("getstateId")
        public DistrictsModel.stateId getstateId;


        public Data(String _id, String districtCode, String districtName, stateId getstateId) {
            this._id = _id;
            this.districtCode = districtCode;
            this.districtName = districtName;
            this.getstateId = getstateId;
        }

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

        public stateId getGetstateId() {
            return getstateId;
        }

        public void setGetstateId(stateId getstateId) {
            this.getstateId = getstateId;
        }
    }


    public class stateId{



        @SerializedName("stateCode")
        public String stateCode;

        @SerializedName("stateName")
        public String stateName;

        public stateId(String stateCode, String stateName) {
            this.stateCode = stateCode;
            this.stateName = stateName;
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
    }



}
