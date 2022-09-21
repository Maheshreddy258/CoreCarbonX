package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class HomeDataModel {

    @SerializedName("_id")
    public String _id;

    @SerializedName("geoLocation")
    public String geoLocation;

    @SerializedName("name")
    public String name;

    @SerializedName("mobileNumber")
    public String mobileNumber;

    @SerializedName("dateofBirth")
    public String dateofBirth;

    @SerializedName("photo")
    public String photo;

    @SerializedName("housePhoto")
    public String housePhoto;

    @SerializedName("personCount")
    public String personCount;

    @SerializedName("state")
    public State state;

    @SerializedName("district")
    public District district;

    @SerializedName("mandal")
    public Mandal mandal;

    @SerializedName("village")
    public Village village;

    @SerializedName("pincode")
    public String pincode;

    @SerializedName("houseNumber")
    public String houseNumber;

    @SerializedName("kyc")
    public String kyc;

    @SerializedName("aadharNumber")
    public String aadharNumber;

    public HomeDataModel(String _id, String geoLocation, String name, String mobileNumber, String dateofBirth, String photo, String housePhoto, String personCount, State state, District district, Mandal mandal, Village village, String pincode, String houseNumber, String kyc, String aadharNumber) {
        this._id = _id;
        this.geoLocation = geoLocation;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.dateofBirth = dateofBirth;
        this.photo = photo;
        this.housePhoto = housePhoto;
        this.personCount = personCount;
        this.state = state;
        this.district = district;
        this.mandal = mandal;
        this.village = village;
        this.pincode = pincode;
        this.houseNumber = houseNumber;
        this.kyc = kyc;
        this.aadharNumber = aadharNumber;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHousePhoto() {
        return housePhoto;
    }

    public void setHousePhoto(String housePhoto) {
        this.housePhoto = housePhoto;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Mandal getMandal() {
        return mandal;
    }

    public void setMandal(Mandal mandal) {
        this.mandal = mandal;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }
}
