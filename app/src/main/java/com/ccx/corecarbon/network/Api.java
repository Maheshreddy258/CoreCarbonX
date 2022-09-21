package com.ccx.corecarbon.network;

import com.ccx.corecarbon.models.CookStoveFormRequestBody;
import com.ccx.corecarbon.models.district.DistrictList;
import com.ccx.corecarbon.models.HomeListResponse;
import com.ccx.corecarbon.models.ImageUploadResponse;
import com.ccx.corecarbon.models.LoginRequestModel;
import com.ccx.corecarbon.models.LoginResponse;
import com.ccx.corecarbon.models.mandal.MandalList;
import com.ccx.corecarbon.models.StatesResponse;
import com.ccx.corecarbon.models.village.VillageList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


//    public static final String PRODUCTION_URL = "https://corecarbon.in/";
//    public static final String PRODUCTION_URL = "http://172.105.56.143:4500/";


//    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> Login(@Body LoginRequestModel loginRequestModel);

    @GET("api/generic/all/States?sort=yes")
    Call<StatesResponse> getStates();

    @GET("api/generic/all/Districts?sort=yes")
    Call<DistrictList> getDistricts();

    @GET("api/generic/all/Mandals?sort=yes")
    Call<MandalList> getMandals();

    @GET("api/generic/all/Villages?sort=yes")
    Call<VillageList> getVillages();

    @GET("api/generic/all/CookStove")
    Call<HomeListResponse> getHomeList();

    @Multipart
    @POST("api/document/upload")
    Call<ImageUploadResponse> uploadUserphoto(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @POST("api/generic/add/CookStove")
    Call<ImageUploadResponse> SubmitForm(@Body CookStoveFormRequestBody cookStoveFormRequestBody);
}
