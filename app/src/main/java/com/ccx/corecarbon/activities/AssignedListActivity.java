package com.ccx.corecarbon.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.adapters.HomeRecyclerAdapter;
import com.ccx.corecarbon.databinding.ActivityAssignedListBinding;
import com.ccx.corecarbon.models.HomeModel;
import com.ccx.corecarbon.room_util.AppDatabase;
import com.ccx.corecarbon.room_util.Form;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.NetworkUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AssignedListActivity extends BaseActivity {
    ActivityAssignedListBinding binding;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private AppDatabase appDatabase;
    private HomeRecyclerAdapter homeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assigned_list);
        Toolbar toolbar2 = findViewById(R.id.tool_bar);
        this.toolbar = toolbar2;
        TextView title = toolbar2.findViewById(R.id.title_tv);
        final String userName = CoreCarbonSharedPreferences.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            title.setText(R.string.cook_stove_assigned_list);
        } else {
            title.setText(getString(R.string.user_name_assigned_list, userName));
        }
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        homeRecyclerAdapter = new HomeRecyclerAdapter(this);
        homeRecyclerAdapter.setAssignedData(true);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(homeRecyclerAdapter);

        progressDialog = new ProgressDialog(this);
        initLocalDb();
    }

    private void initLocalDb() {
        appDatabase = AppDatabase.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check any offline records
//        List<Form> offlineList = appDatabase.formDao().getOfflineRecords(false);
        /*if (offlineList != null && offlineList.size() > 0)
            Toast.makeText(this, "First sync offline record records to see the assigned list", Toast.LENGTH_SHORT).show();
        else {
            appDatabase.formDao().deleteAssignedOnlineList(true);
            getAssignedList();
        }
*/
        appDatabase.formDao().deleteAssignedOnlineList(true);
        getAssignedList();
    }

    private void getAssignedList() {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgress();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("assignedTo", CoreCarbonSharedPreferences.getNewId());
        requestParams.put("serialNumber", "");
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        client.post(Constants.PRODUCTION_URL + "api/generic/search/CookStove?_page=1&_limit=100", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                String str = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (!jsonObject.getBoolean("error")) {
                        List<HomeModel> list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Form form = new Form();
                            form.isOnlineRecord = true;
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HomeModel homeModel = new HomeModel();

                            if (jsonObject1.has("_id")) {
                                form.onlineFormId = jsonObject1.getString("_id");
                                homeModel._id = jsonObject1.getString("_id");
                            }
                            if (jsonObject1.has("name")) {
                                form.name = jsonObject1.getString("name");
                                homeModel.name = jsonObject1.getString("name");
                            }
                            if (jsonObject1.has("typeOfMobile")) {
                                form.typeOfMobile = jsonObject1.getString("typeOfMobile");
                                homeModel.mobileType = jsonObject1.getString("typeOfMobile");
                            }
                            if (jsonObject1.has("mobileNumber")) {
                                form.mobileNumber = jsonObject1.getString("mobileNumber");
                                homeModel.mobile = jsonObject1.getString("mobileNumber");
                            }
                            if (jsonObject1.has("aadharNumber")) {
                                form.aadharNumber = jsonObject1.getString("aadharNumber");
                                homeModel.aadharNumber = jsonObject1.getString("aadharNumber");
                            }
                            if (jsonObject1.has("bankAccountNumber")) {
                                form.bankAccountNumber = jsonObject1.getString("bankAccountNumber");
                                homeModel.bankAccountNumber = jsonObject1.getString("bankAccountNumber");
                            }

                            if (jsonObject1.has("bankBranch")) {
                                form.bankBranch = jsonObject1.getString("bankBranch");
                                homeModel.bankBranch = jsonObject1.getString("bankBranch");
                            }

                            if (jsonObject1.has("bankIfsc")) {
                                form.bankIfsc = jsonObject1.getString("bankIfsc");
                                homeModel.bankIfsc = jsonObject1.getString("bankIfsc");
                            }

                            if (jsonObject1.has("bankName")) {
                                form.bankName = jsonObject1.getString("bankName");
                                homeModel.bankName = jsonObject1.getString("bankName");
                            }

                            if (jsonObject1.has("district")) {
                                form.district = jsonObject1.getString("district");
                                homeModel.district = jsonObject1.getString("district");
                            }

                            if (jsonObject1.has("geoLocation")) {
                                form.geoLocation = jsonObject1.getString("geoLocation");
                                homeModel.geoLocation = jsonObject1.getString("geoLocation");
                            }

                            if (jsonObject1.has("houseNumber")) {
                                form.houseNumber = jsonObject1.getString("houseNumber");
                                homeModel.houseNumber = jsonObject1.getString("houseNumber");
                            }

                            if (jsonObject1.has("kyc")) {
                                form.kyc = jsonObject1.getString("kyc");
                                homeModel.kyc = jsonObject1.getString("kyc");
                            }

                            if (jsonObject1.has("mandal")) {
                                form.mandal = jsonObject1.getString("mandal");
                                homeModel.mandal = jsonObject1.getString("mandal");
                            }

                            if (jsonObject1.has("manufacturer")) {
                                form.manufacturer = jsonObject1.getString("manufacturer");
                                homeModel.manufacturer = jsonObject1.getString("manufacturer");
                            }

                            if (jsonObject1.has("personCount")) {
                                form.personCount = jsonObject1.getString("personCount");
                                homeModel.personCount = jsonObject1.getString("personCount");
                            }

                            if (jsonObject1.has("pincode")) {
                                form.pincode = jsonObject1.getString("pincode");
                                homeModel.pincode = jsonObject1.getString("pincode");
                            }

                            if (jsonObject1.has("serialNumber")) {
                                form.serialNumber = jsonObject1.getString("serialNumber");
                                homeModel.serialNumber = jsonObject1.getString("serialNumber");
                            }

                            if (jsonObject1.has("state")) {
                                form.state = jsonObject1.getString("state");
                                homeModel.state = jsonObject1.getString("state");
                            }

                            if (jsonObject1.has("typeofStove")) {
                                form.typeofStove = jsonObject1.getString("typeofStove");
                                homeModel.typeofStove = jsonObject1.getString("typeofStove");
                            }

                            if (jsonObject1.has("village")) {
                                form.village = jsonObject1.getString("village");
                                homeModel.village = jsonObject1.getString("village");
                            }

                            if (jsonObject1.has("dateOfDistribution")) {
                                form.dateOfDistribution = jsonObject1.getString("dateOfDistribution");
                                homeModel.dateOfDistribution = jsonObject1.getString("dateOfDistribution");
                            }

                            if (jsonObject1.has("dateofBirth")) {
                                form.dateofBirth = jsonObject1.getString("dateofBirth");
                                homeModel.dateofBirth = jsonObject1.getString("dateofBirth");
                            }

                            if (jsonObject1.has("photo")) {
                                homeModel.userPhoto = jsonObject1.getString("photo");
                            }

                            if (jsonObject1.has("housePhoto")) {
                                homeModel.housePhoto = jsonObject1.getString("housePhoto");
                            }

                            if (jsonObject1.has("newStoveImage")) {
                                homeModel.newPhoto = jsonObject1.getString("newStoveImage");
                            }

                            if (jsonObject1.has("traditionalStoveImage")) {
                                homeModel.tradiPhoto = jsonObject1.getString("traditionalStoveImage");
                            }


                            if (jsonObject1.has("aadhar")) {
                                homeModel.idPhoto = jsonObject1.getString("aadhar");
                            }


                            if (jsonObject1.has("serialNumberImage")) {
                                homeModel.serialNumberPhoto = jsonObject1.getString("serialNumberImage");
                            }

                            if (jsonObject1.has("bankDocument")) {
                                homeModel.bankpassbook = jsonObject1.getString("bankDocument");
                            }

                            if (jsonObject1.has("agreement")) {
                                homeModel.agreementPhoto = jsonObject1.getString("agreement");
                            }
                            JSONObject createdByObject = new JSONObject();
                            try {
                                createdByObject.put("_id", CoreCarbonSharedPreferences.getNewId());
                                createdByObject.put("status", CoreCarbonSharedPreferences.getStatus());
                                createdByObject.put("isMobileApp", CoreCarbonSharedPreferences.isMobileApp());
                                createdByObject.put("cookstoveAccess", CoreCarbonSharedPreferences.getCookStoveAccess());
                                createdByObject.put("agroAccess", CoreCarbonSharedPreferences.getAgroAccess());
                                createdByObject.put("name", CoreCarbonSharedPreferences.getUserName());
                                createdByObject.put("mobileNumber", CoreCarbonSharedPreferences.getMobile());
                                createdByObject.put("role", CoreCarbonSharedPreferences.getUserRole());
                                createdByObject.put("isAdmin", CoreCarbonSharedPreferences.getisAdmin());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            appDatabase.formDao().insertForm(form);
                            list.add(homeModel);
                        }
                        if (list.size() == 0) {
                            binding.nodataTv.setVisibility(View.VISIBLE);
                        } else {
                            binding.nodataTv.setVisibility(View.GONE);
                        }
                        homeRecyclerAdapter.setList(list);
                    } else {
                        binding.nodataTv.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}