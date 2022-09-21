package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class ImageUploadResponse {


    @SerializedName("error")
    public boolean error;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public String data;


    public ImageUploadResponse(boolean error, String message, String data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
