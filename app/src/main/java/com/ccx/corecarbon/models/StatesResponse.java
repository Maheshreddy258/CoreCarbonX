package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatesResponse {

    @SerializedName("error")
    public boolean error;

    @SerializedName("count")
    public int count;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<Data> data;


    public StatesResponse(boolean error, int count, String message, List<Data> data) {
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

        @SerializedName("stateCode")
        public String stateCode;

        @SerializedName("stateName")
        public String stateName;

        @SerializedName("status")
        public boolean status;


        public Data(String _id, String stateCode, String stateName, boolean status) {
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


}
