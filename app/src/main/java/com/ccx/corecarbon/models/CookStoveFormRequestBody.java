package com.ccx.corecarbon.models;

import com.google.gson.annotations.SerializedName;

public class CookStoveFormRequestBody {

    @SerializedName("name")
    public String name;

    @SerializedName("mobileNumber")
    public String mobileNumber;

    @SerializedName("dateofBirth")
    public long dateofBirth;

    @SerializedName("photo")
    public String photo;

    @SerializedName("housePhoto")
    public String housePhoto;

    @SerializedName("personCount")
    public String personCount;

    @SerializedName("geoLocation")
    public String geoLocation;

    @SerializedName("district")
    public String district;

    @SerializedName("houseNumber")
    public String houseNumber;

    @SerializedName("mandal")
    public String mandal;

    @SerializedName("pincode")
    public String pincode;

    @SerializedName("state")
    public String state;

    @SerializedName("village")
    public String village;

    @SerializedName("aadhar")
    public String aadhar;

    @SerializedName("aadharNumber")
    public String aadharNumber;

    @SerializedName("agreement")
    public String agreement;

    @SerializedName("dateOfDistribution")
    public Long dateOfDistribution;

    @SerializedName("kyc")
    public String kyc;


    @SerializedName("manufacturer")
    public String manufacturer;


    @SerializedName("newStoveImage")
    public String newStoveImage;


    @SerializedName("serialNumber")
    public String serialNumber;


    @SerializedName("serialNumberImage")
    public String serialNumberImage;


    @SerializedName("traditionalStoveImage")
    public String traditionalStoveImage;

    @SerializedName("typeofStove")
    public String typeofStove;

    @SerializedName("bankAccountNumber")
    public String bankAccountNumber;

    @SerializedName("bankBranch")
    public String bankBranch;

    @SerializedName("bankDocument")
    public String bankDocument;

    @SerializedName("bankIfsc")
    public String bankIfsc;

    @SerializedName("bankName")
    public String bankName;

    public CookStoveFormRequestBody(String name, String mobileNumber, Long dateofBirth, String photo, String housePhoto, String personCount, String geoLocation, String district, String houseNumber, String mandal, String pincode, String state, String village, String aadhar, String aadharNumber, String agreement, Long dateOfDistribution, String kyc, String manufacturer, String newStoveImage, String serialNumber, String serialNumberImage, String traditionalStoveImage, String typeofStove, String bankAccountNumber, String bankBranch, String bankDocument, String bankIfsc, String bankName) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.dateofBirth = dateofBirth;
        this.photo = photo;
        this.housePhoto = housePhoto;
        this.personCount = personCount;
        this.geoLocation = geoLocation;
        this.district = district;
        this.houseNumber = houseNumber;
        this.mandal = mandal;
        this.pincode = pincode;
        this.state = state;
        this.village = village;
        this.aadhar = aadhar;
        this.aadharNumber = aadharNumber;
        this.agreement = agreement;
        this.dateOfDistribution = dateOfDistribution;
        this.kyc = kyc;
        this.manufacturer = manufacturer;
        this.newStoveImage = newStoveImage;
        this.serialNumber = serialNumber;
        this.serialNumberImage = serialNumberImage;
        this.traditionalStoveImage = traditionalStoveImage;
        this.typeofStove = typeofStove;
        this.bankAccountNumber = bankAccountNumber;
        this.bankBranch = bankBranch;
        this.bankDocument = bankDocument;
        this.bankIfsc = bankIfsc;
        this.bankName = bankName;
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

    public Long getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(Long dateofBirth) {
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

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getMandal() {
        return mandal;
    }

    public void setMandal(String mandal) {
        this.mandal = mandal;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public Long getDateOfDistribution() {
        return dateOfDistribution;
    }

    public void setDateOfDistribution(Long dateOfDistribution) {
        this.dateOfDistribution = dateOfDistribution;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getNewStoveImage() {
        return newStoveImage;
    }

    public void setNewStoveImage(String newStoveImage) {
        this.newStoveImage = newStoveImage;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumberImage() {
        return serialNumberImage;
    }

    public void setSerialNumberImage(String serialNumberImage) {
        this.serialNumberImage = serialNumberImage;
    }

    public String getTraditionalStoveImage() {
        return traditionalStoveImage;
    }

    public void setTraditionalStoveImage(String traditionalStoveImage) {
        this.traditionalStoveImage = traditionalStoveImage;
    }

    public String getTypeofStove() {
        return typeofStove;
    }

    public void setTypeofStove(String typeofStove) {
        this.typeofStove = typeofStove;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankDocument() {
        return bankDocument;
    }

    public void setBankDocument(String bankDocument) {
        this.bankDocument = bankDocument;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
