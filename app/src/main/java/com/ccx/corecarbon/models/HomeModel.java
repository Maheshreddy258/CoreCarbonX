package com.ccx.corecarbon.models;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeModel {
    public String _id;
    public String name;
    public String mobile;
    public String mobileType;
    public String aadharNumber;
    public String housePhoto;
    public String idPhoto;
    public String agreementPhoto;
    public String tradiPhoto;
    public String newPhoto;
    public String serialNumberPhoto;
    public String bankpassbook;
    public String userPhoto;
    public String bankAccountNumber;
    public String bankBranch;
    public String bankIfsc;
    public String bankName;
    public String district;
    public String geoLocation;
    public String houseNumber;
    public String kyc;
    public String mandal;
    public String manufacturer;
    public String personCount;
    public String pincode;
    public String serialNumber;
    public String state;
    public String typeofStove;
    public String village;
    public String dateOfDistribution;
    public String dateofBirth;

    public HomeModel() {
    }

    public HomeModel(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("_id")) {
//                            form.onlineFormId = jsonObject1.getString("_id");
            _id = jsonObject.getString("_id");
        }
        if (jsonObject.has("name")) {
//                            form.name = jsonObject1.getString("name");
            name = jsonObject.getString("name");
        }
        if (jsonObject.has("typeOfMobile")) {
//                            form.typeOfMobile = jsonObject1.getString("typeOfMobile");
            mobileType = jsonObject.getString("typeOfMobile");
        }
        if (jsonObject.has("mobileNumber")) {
//                            form.mobileNumber = jsonObject1.getString("mobileNumber");
            mobile = jsonObject.getString("mobileNumber");
        }
        if (jsonObject.has("aadharNumber")) {
//                            form.aadharNumber = jsonObject1.getString("aadharNumber");
            aadharNumber = jsonObject.getString("aadharNumber");
        }
        if (jsonObject.has("bankAccountNumber")) {
//                            form.bankAccountNumber = jsonObject1.getString("bankAccountNumber");
            bankAccountNumber = jsonObject.getString("bankAccountNumber");
        }

        if (jsonObject.has("bankBranch")) {
//                            form.bankBranch = jsonObject1.getString("bankBranch");
            bankBranch = jsonObject.getString("bankBranch");
        }

        if (jsonObject.has("bankIfsc")) {
//                            form.bankIfsc = jsonObject1.getString("bankIfsc");
            bankIfsc = jsonObject.getString("bankIfsc");
        }

        if (jsonObject.has("bankName")) {
//                            form.bankName = jsonObject1.getString("bankName");
            bankName = jsonObject.getString("bankName");
        }

        if (jsonObject.has("district")) {
//                            form.district = jsonObject1.getString("district");
            district = jsonObject.getString("district");
        }

        if (jsonObject.has("geoLocation")) {
//                            form.geoLocation = jsonObject1.getString("geoLocation");
            geoLocation = jsonObject.getString("geoLocation");
        }

        if (jsonObject.has("houseNumber")) {
//                            form.houseNumber = jsonObject1.getString("houseNumber");
            houseNumber = jsonObject.getString("houseNumber");
        }

        if (jsonObject.has("kyc")) {
//                            form.kyc = jsonObject1.getString("kyc");
            kyc = jsonObject.getString("kyc");
        }

        if (jsonObject.has("mandal")) {
//                            form.mandal = jsonObject1.getString("mandal");
            mandal = jsonObject.getString("mandal");
        }

        if (jsonObject.has("manufacturer")) {
//                            form.manufacturer = jsonObject1.getString("manufacturer");
            manufacturer = jsonObject.getString("manufacturer");
        }

        if (jsonObject.has("personCount")) {
//                            form.personCount = jsonObject1.getString("personCount");
            personCount = jsonObject.getString("personCount");
        }

        if (jsonObject.has("pincode")) {
//                            form.pincode = jsonObject1.getString("pincode");
            pincode = jsonObject.getString("pincode");
        }

        if (jsonObject.has("serialNumber")) {
//                            form.serialNumber = jsonObject1.getString("serialNumber");
            serialNumber = jsonObject.getString("serialNumber");
        }

        if (jsonObject.has("state")) {
//                            form.state = jsonObject1.getString("state");
            state = jsonObject.getString("state");
        }

        if (jsonObject.has("typeofStove")) {
//                            form.typeofStove = jsonObject1.getString("typeofStove");
            typeofStove = jsonObject.getString("typeofStove");
        }

        if (jsonObject.has("village")) {
//                            form.village = jsonObject1.getString("village");
            village = jsonObject.getString("village");
        }

        if (jsonObject.has("dateOfDistribution")) {
//                            form.dateOfDistribution = jsonObject1.getString("dateOfDistribution");
            dateOfDistribution = jsonObject.getString("dateOfDistribution");
        }

        if (jsonObject.has("dateofBirth")) {
//                            form.dateofBirth = jsonObject1.getString("dateofBirth");
            dateofBirth = jsonObject.getString("dateofBirth");
        }

        if (jsonObject.has("photo")) {
            userPhoto = jsonObject.getString("photo");
        }

        if (jsonObject.has("housePhoto")) {
            housePhoto = jsonObject.getString("housePhoto");
        }

        if (jsonObject.has("newStoveImage")) {
            newPhoto = jsonObject.getString("newStoveImage");
        }

        if (jsonObject.has("traditionalStoveImage")) {
            tradiPhoto = jsonObject.getString("traditionalStoveImage");
        }


        if (jsonObject.has("aadhar")) {
            idPhoto = jsonObject.getString("aadhar");
        }


        if (jsonObject.has("serialNumberImage")) {
            serialNumberPhoto = jsonObject.getString("serialNumberImage");
        }

        if (jsonObject.has("bankDocument")) {
            bankpassbook = jsonObject.getString("bankDocument");
        }

        if (jsonObject.has("agreement")) {
            agreementPhoto = jsonObject.getString("agreement");
        }
//                            JSONObject createdByObject = new JSONObject();
//                            try {
//                                createdByObject.put("_id", CoreCarbonSharedPreferences.getNewId());
//                                createdByObject.put("status", CoreCarbonSharedPreferences.getStatus());
//                                createdByObject.put("isMobileApp", CoreCarbonSharedPreferences.isMobileApp());
//                                createdByObject.put("cookstoveAccess", CoreCarbonSharedPreferences.getCookStoveAccess());
//                                createdByObject.put("agroAccess", CoreCarbonSharedPreferences.getAgroAccess());
//                                createdByObject.put("name", CoreCarbonSharedPreferences.getUserName());
//                                createdByObject.put("mobileNumber", CoreCarbonSharedPreferences.getMobile());
//                                createdByObject.put("role", CoreCarbonSharedPreferences.getUserRole());
//                                createdByObject.put("isAdmin", CoreCarbonSharedPreferences.getisAdmin());
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
    }

}
