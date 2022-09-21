package com.ccx.corecarbon.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CoreCarbonSharedPreferences {


    private static final String SHARED_PREF = "shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ID = "id";
    private static final String LOGGED_IN = "logged_in";
    private static final String USER_IMAGE_ID = "user";
    private static final String HOUSE_IMAGE_ID = "house";
    private static final String ID_IMAGE_ID = "id";
    private static final String TRADI_IMAGE_ID = "tradi";
    private static final String NEW_IMAGE_ID = "new";
    private static final String SERIAL_IMAGE_ID = "serial";
    private static final String AGREE_IMAGE_ID = "agree";
    private static final String PASS_IMAGE_ID = "pass";
    private static final String IS_MOBILE_APP = "ismobileapp";
    private static final String COOKSTOVE_ACCESS = "cookstoveaccess";
    private static final String AGRO_ACCESS = "agroaccess";
    private static final String USER_ROLE = "role";
    private static final String IS_ADMIN = "isadmin";
    private static final String STATUS = "status";
    private static final String STATES_LIST = "states";
    private static final String DISTRICTS_LIST = "districts";
    private static final String VILLAGES_LIST = "villages";
    private static final String MANDALS_LIST = "mandals";
    private static final String NEW_TOKEN = "newtoken";
    private static final String NEW_ID = "newid";
    private static final String STATE = "state";
    private static final String DISTRICT = "district";
    private static final String MANDAL = "mandal";
    private static final String VILLAGE = "village";


//    public static void setUserData(String id,String name, String Mobile, String token, boolean value, boolean isAdmin,
//   String stoveaccess, String agroaccess, String role, String status, String isMobileApp
//    ){
//        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_NAME, name);
//        editor.putString(KEY_MOBILE, Mobile);
//        editor.putString(KEY_TOKEN, token);
//        editor.putString(KEY_ID, id);
//        editor.putBoolean(IS_ADMIN, isAdmin);
//        editor.putString(IS_MOBILE_APP, isMobileApp);
//        editor.putString(COOKSTOVE_ACCESS, stoveaccess);
//        editor.putString(AGRO_ACCESS, agroaccess);
//        editor.putString(USER_ROLE, role);
//        editor.putString(STATUS, status);
//        editor.putBoolean(LOGGED_IN, value);
//        editor.apply();
//
//    }

    public static void SetUserImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(USER_IMAGE_ID, id).apply();
    }


    public static void SetHouseImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(HOUSE_IMAGE_ID, id).apply();
    }

    public static void SetIDImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ID_IMAGE_ID, id).apply();
    }

    public static void SetTradiImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(TRADI_IMAGE_ID, id).apply();
    }

    public static void SetNewImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(NEW_IMAGE_ID, id).apply();
    }

    public static void SetSerialImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SERIAL_IMAGE_ID, id).apply();
    }

    public static void SetAgreeImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AGREE_IMAGE_ID, id).apply();
    }

    public static void SetPassImageIdFromResponse(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PASS_IMAGE_ID, id).apply();
    }

    public static String getUserImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_IMAGE_ID, "");
    }

    public static String getHouseImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(HOUSE_IMAGE_ID, "");
    }

    public static String getIDImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_IMAGE_ID, "");
    }

    public static String getTradiImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TRADI_IMAGE_ID, "");
    }

    public static String getNewImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NEW_IMAGE_ID, "");
    }

    public static String getSerialImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SERIAL_IMAGE_ID, "");
    }

    public static String getAgreeImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AGREE_IMAGE_ID, "");
    }

    public static String getPassImageId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASS_IMAGE_ID, "");
    }

    public static void ClearData() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static String getUserName() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public static String getMobile() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE, "");
    }


    public static String getToken() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public static String getUserId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, "");
    }

    public static Boolean isLoggedIn() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public static void setUserData(String id, String name, String mobileNumber, String token, boolean b, boolean admin, String cookstoveAccess, String agroAccess, String role, String status, String mobileapp) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_MOBILE, mobileNumber)
                .putString(KEY_TOKEN, token)
                .putString(KEY_ID, id)
                .putBoolean(IS_ADMIN, admin)
                .putString(IS_MOBILE_APP, mobileapp)
                .putString(COOKSTOVE_ACCESS, cookstoveAccess)
                .putString(AGRO_ACCESS, agroAccess)
                .putString(USER_ROLE, role)
                .putString(STATUS, status)
                .putBoolean(LOGGED_IN, b)
                .apply();
    }

    public static boolean getisAdmin() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_ADMIN, false);
    }

    public static String isMobileApp() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(IS_MOBILE_APP, "");
    }

    public static String getCookStoveAccess() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COOKSTOVE_ACCESS, "");
    }

    public static String getAgroAccess() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AGRO_ACCESS, "");
    }

    public static String getUserRole() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ROLE, "");
    }

    public static String getStatus() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(STATUS, "");
    }

    public static void storeStatesArray(String list) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(STATES_LIST, list).apply();
    }

    public static void storeDistrictsArray(String list) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(DISTRICTS_LIST, String.valueOf(list)).apply();
    }

    public static void storeVillagesArray(String list) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(VILLAGES_LIST, String.valueOf(list)).apply();
    }

    public static void storeMandalsArray(String list) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(MANDALS_LIST, String.valueOf(list)).apply();
    }

    public static String getStates() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(STATES_LIST, "");
    }

    public static String getDistricts() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DISTRICTS_LIST, "");
    }

    public static String getVillages() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(VILLAGES_LIST, "");
    }

    public static String getMandals() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MANDALS_LIST, "");
    }

    public static void SetNewToken(String token) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(NEW_TOKEN, token).apply();
    }

    public static String getNewToken() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NEW_TOKEN, "");
    }

    public static void setNewUserId(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(NEW_ID, id).apply();
    }

    public static String getNewId() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NEW_ID, "");
    }

    public static String getState() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(STATE, "Select State");
    }

    public static void setState(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(STATE, id).apply();
    }

    public static String getDistrict() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DISTRICT, "Select District");
    }

    public static void setDistrict(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(DISTRICT, id).apply();
    }

    public static String getMandal() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MANDAL, "Select Mandal");
    }

    public static void setMandal(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(MANDAL, id).apply();
    }

    public static String getVillage() {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(VILLAGE, "Select Village");
    }

    public static void setVillage(String id) {
        SharedPreferences sharedPreferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(VILLAGE, id).apply();
    }

    public static void ClearIds() {
        SharedPreferences preferences = CoreCarbonApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        preferences.edit()
                .remove(USER_IMAGE_ID)
                .remove(HOUSE_IMAGE_ID)
                .remove(ID_IMAGE_ID)
                .remove(NEW_IMAGE_ID)
                .remove(TRADI_IMAGE_ID)
                .remove(SERIAL_IMAGE_ID)
                .remove(AGREE_IMAGE_ID)
                .remove(PASS_IMAGE_ID)
                .apply();
    }
}
