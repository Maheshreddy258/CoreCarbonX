package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MandalResponse {

    @SerializedName("error")
    public boolean error;

    @SerializedName("count")
    public int count;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<MandalResponse.Data> data;


    public MandalResponse(boolean error, int count, String message, List<Data> data) {
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

        @SerializedName("mandalCode")
        public String mandalCode;

        @SerializedName("mandalName")
        public String mandalName;


        public Data(String _id, String mandalCode, String mandalName) {
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

}
