package com.ccx.corecarbon.room_util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Form {


    @PrimaryKey(autoGenerate = true)
    public int form_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "mobileNumber")
    public String mobileNumber;

    @ColumnInfo(name = "typeOfMobile")
    public String typeOfMobile;

    @ColumnInfo(name = "dateofBirth")
    public String dateofBirth;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] beneficiaryPhoto;

    @ColumnInfo(name = "beneficiaryPhotoPath")
    public String beneficiaryPhotoPath;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] traditionalStoveImage;

    @ColumnInfo(name = "traditionalStoveImagePath")
    public String traditionalStoveImagePath;

    @ColumnInfo(name = "personCount")
    public String personCount;

    @ColumnInfo(name = "geoLocation")
    public String geoLocation;


    @ColumnInfo(name = "houseNumber")
    public String houseNumber;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "district")
    public String district;

    @ColumnInfo(name = "mandal")
    public String mandal;

    @ColumnInfo(name = "village")
    public String village;

    @ColumnInfo(name = "pincode")
    public String pincode;


    @ColumnInfo(name = "kyc")
    public String kyc;

    @ColumnInfo(name = "aadharNumber")
    public String aadharNumber;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] aadhar;

    @ColumnInfo(name = "aadharPath")
    public String aadharPath;


    @ColumnInfo(name = "dateOfDistribution")
    public String dateOfDistribution;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] newStoveImage;

    @ColumnInfo(name = "newStovePath")
    public String newStovePath;

    @ColumnInfo(name = "serialNumber")
    public String serialNumber;

    @ColumnInfo(name = "manufacturer")
    public String manufacturer;

    @ColumnInfo(name = "typeofStove")
    public String typeofStove;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] agreement;

    @ColumnInfo(name = "agreementPath")
    public String agreementPath;


    @ColumnInfo(name = "bankName")
    public String bankName;

    @ColumnInfo(name = "bankAccountNumber")
    public String bankAccountNumber;

    @ColumnInfo(name = "bankBranch")
    public String bankBranch;

    @ColumnInfo(name = "bankIfsc")
    public String bankIfsc;


    @ColumnInfo(name = "createdBy")
    public String createdBy;

    @ColumnInfo(name = "isOnlineRecord")
    public boolean isOnlineRecord;

    @ColumnInfo(name = "onlineFormId")
    public String onlineFormId;
}
