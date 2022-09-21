package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class District {


    @SerializedName("_id")
    public String _id;

    @SerializedName("districtCode")
    public String districtCode;

    @SerializedName("districtName")
    public String districtName;

    @SerializedName("stateId")
    public stateId stateId;

    public District(String _id, String districtCode, String districtName, District.stateId stateId) {
        this._id = _id;
        this.districtCode = districtCode;
        this.districtName = districtName;
        this.stateId = stateId;
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

    public District.stateId getStateId() {
        return stateId;
    }

    public void setStateId(District.stateId stateId) {
        this.stateId = stateId;
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
