package com.ccx.corecarbon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.ccx.corecarbon.models.district.DistrictList;
import com.ccx.corecarbon.models.mandal.MandalList;
import com.ccx.corecarbon.models.StatesResponse;
import com.ccx.corecarbon.models.village.VillageList;
import com.ccx.corecarbon.network.RetrofitClient;
import com.ccx.corecarbon.R;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.NetworkUtil;
import com.ccx.corecarbon.databinding.ActivityLoginBinding;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

//        EnableLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.loginLayout.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp).duration(700).repeat(0).playOn(binding.loginLayout);
            }
        }, 500);

        binding.loginBtn.setOnClickListener(view -> {
            String mobile = binding.mobileEd.getText().toString();
            String password = binding.passwordEd.getText().toString();
            if (mobile.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter passwaord", Toast.LENGTH_SHORT).show();
            } else {
                Login(mobile, password);
            }
        });
    }

    private void Login(String mobile, String password) {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("mobileNumber", mobile);
        params.put("password", password);
        params.put("isMobileApp", true);

        client.post(Constants.PRODUCTION_URL + "auth/login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String message = jsonObject.getString("message");
                    if (jsonObject.getBoolean("error") == false) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String id = jsonObject1.getString("_id");
                        CoreCarbonSharedPreferences.setNewUserId(id);
                        CoreCarbonSharedPreferences.setUserData(
                                jsonObject1.getString("_id"),
                                jsonObject1.getString("name"),
                                jsonObject1.getString("mobileNumber"),
                                jsonObject1.getString("token"),
                                true,
                                jsonObject1.getBoolean("isAdmin"),
                                jsonObject1.getString("cookstoveAccess"),
                                jsonObject1.getString("agroAccess"),
                                jsonObject1.getString("role"),
                                jsonObject1.getString("status"),
                                jsonObject1.getString("isMobileApp"));

                        GetStates();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetStates() {
        if (!NetworkUtil.isOnline(this)) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().getMyApi().getStates().enqueue(new Callback<StatesResponse>() {
            @Override
            public void onResponse(Call<StatesResponse> call, Response<StatesResponse> response) {
                if (response.body().isError()) {
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                CoreCarbonSharedPreferences.storeStatesArray(new Gson().toJson(response.body().getData()));
                GEtDistricts();
            }

            @Override
            public void onFailure(Call<StatesResponse> call, Throwable t) {
                progressDialog.dismiss();
                call.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void GEtDistricts() {
        if (!NetworkUtil.isOnline(this)) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().getMyApi().getDistricts().enqueue(new Callback<DistrictList>() {
            @Override
            public void onResponse(Call<DistrictList> call, Response<DistrictList> response) {
                if (response.body().isError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                CoreCarbonSharedPreferences.storeDistrictsArray(new Gson().toJson(response.body().getData()));
                GetVillages();
            }

            @Override
            public void onFailure(Call<DistrictList> call, Throwable t) {
                progressDialog.dismiss();
                call.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetVillages() {
        if (!NetworkUtil.isOnline(this)) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().getMyApi().getVillages().enqueue(new Callback<VillageList>() {
            @Override
            public void onResponse(Call<VillageList> call, Response<VillageList> response) {
                if (response.body().isError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                CoreCarbonSharedPreferences.storeVillagesArray(new Gson().toJson(response.body().getData()));
                GetMandals();
            }

            @Override
            public void onFailure(Call<VillageList> call, Throwable t) {
                progressDialog.dismiss();
                call.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetMandals() {
        if (!NetworkUtil.isOnline(this)) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().getMyApi().getMandals().enqueue(new Callback<MandalList>() {
            @Override
            public void onResponse(Call<MandalList> call, Response<MandalList> response) {
                if (response.body().isError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                progressDialog.dismiss();
                CoreCarbonSharedPreferences.storeMandalsArray(new Gson().toJson(response.body().getData()));
                //
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }

            @Override
            public void onFailure(Call<MandalList> call, Throwable t) {
                progressDialog.dismiss();
                call.cancel();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}