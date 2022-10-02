package com.ccx.corecarbon.activities;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.adapters.OfflineListAdapter;
import com.ccx.corecarbon.databinding.ActivityOfflineListBinding;
import com.ccx.corecarbon.room_util.AppDatabase;
import com.ccx.corecarbon.room_util.Form;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.NetworkUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import cz.msebera.android.httpclient.Header;

public class OfflineListActivity extends BaseActivity implements OfflineListAdapter.OfflineListAdapterListener {

    private ProgressDialog progressDialog;
    private AppDatabase appDatabase;
    private ActivityOfflineListBinding binding;
    private TextView title;
    private List<AttachmentModel> attachments;
    private int index = 0;
    private int form_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_offline_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_tv);
//        sync = toolbar.findViewById(R.id.sync_tv);
//        sync.setVisibility(View.VISIBLE);

        final String userName = CoreCarbonSharedPreferences.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            title.setText(getString(R.string.cook_stove_offline_records_count, 0));
        } else {
            title.setText(getString(R.string.user_name_offline_records_count, userName, 0));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        appDatabase = AppDatabase.getInstance(OfflineListActivity.this);
//        sync.setOnClickListener(view -> syncData());
        //
        LoadList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void LoadList() {
        //first showing offline records then online
        AppDatabase appDatabase = AppDatabase.getInstance(OfflineListActivity.this);
        //    private TextView sync;
        List<Form> list = appDatabase.formDao().getOfflineRecordsOrderByOfflineRecords();
//        list.addAll(appDatabase.formDao().getOfflineRecords(true));
        if (list.size() != 0) {
            OfflineListAdapter offlineListAdapter = new OfflineListAdapter(this, list, this);
            binding.recyclerview.setLayoutManager(new GridLayoutManager(OfflineListActivity.this, 2));
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(offlineListAdapter);
            binding.recyclerview.setVisibility(View.VISIBLE);
            binding.nodataTv.setVisibility(View.GONE);
//            sync.setVisibility(View.VISIBLE);

            final String userName = CoreCarbonSharedPreferences.getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                title.setText(getString(R.string.cook_stove_offline_records_count, list.size()));
            } else {
                title.setText(getString(R.string.user_name_offline_records_count, userName, list.size()));
            }
        } else {
            binding.nodataTv.setVisibility(View.VISIBLE);
//            sync.setVisibility(View.GONE);
            binding.recyclerview.setVisibility(View.GONE);
            final String userName = CoreCarbonSharedPreferences.getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                title.setText(getString(R.string.cook_stove_offline_records_count, 0));
            } else {
                title.setText(getString(R.string.user_name_offline_records_count, userName, 0));
            }
        }
    }

    @Override
    public void onClickItem(@Nullable Form form) {
        if (form == null) {
            return;
        }
        Intent intent = new Intent(this, CookStoveFormActivity.class);
        intent.putExtra("edit", true);
        intent.putExtra("name", form.name);
        if (form.isOnlineRecord)
            intent.putExtra("_id", form.onlineFormId);
        intent.putExtra("mobileNumber", form.mobileNumber);
        intent.putExtra("userPhoto", "");
        intent.putExtra("mobileType", form.typeOfMobile);
        intent.putExtra("aadharNumber", form.aadharNumber);
        intent.putExtra("aadharPhoto", "");
//            intent.putExtra("housePhoto", form.housePhoto);
        intent.putExtra("bankAccountNumber", form.bankAccountNumber);
        intent.putExtra("passbookphoto", "");
        intent.putExtra("newtove", "");
        intent.putExtra("tradiphoto", "");
        intent.putExtra("agreement", "");
        intent.putExtra("serialImage", "");
        intent.putExtra("bankBranch", form.bankBranch);
        intent.putExtra("bankIfsc", form.bankIfsc);
        intent.putExtra("bankName", form.bankName);
        intent.putExtra("geoLocation", form.geoLocation);
        intent.putExtra("district", form.district);
        intent.putExtra("houseNumber", form.houseNumber);
        intent.putExtra("kyc", form.kyc);
        intent.putExtra("mandal", form.mandal);
        intent.putExtra("manufacturer", form.manufacturer);
        intent.putExtra("personCount", form.personCount);
        intent.putExtra("pincode", form.pincode);
        intent.putExtra("serialNumber", form.serialNumber);
        intent.putExtra("state", form.state);
        intent.putExtra("typeofStove", form.typeofStove);
        intent.putExtra("village", form.village);
        intent.putExtra("dateOfDistribution", form.dateOfDistribution);
        intent.putExtra("dateofBirth", form.dateofBirth);
        startActivity(intent);
    }

    @Override
    public void onSyncItem(@Nullable Form form) {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        if (form == null) {
            return;
        }
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONObject createdByObject = new JSONObject();
        try {
            createdByObject.put("_id", CoreCarbonSharedPreferences.getUserId());
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
        String url = Constants.PRODUCTION_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        RequestParams requestParams = new RequestParams();
        if (form.isOnlineRecord) {
            requestParams.put("_id", form.onlineFormId);
            url = url + "api/generic/update/CookStove";
        } else {
            url = url + "api/generic/add/CookStove";
        }
        requestParams.put("name", form.name);
        requestParams.put("mobileNumber", form.mobileNumber);
        requestParams.put("typeOfMobile", form.typeOfMobile);
        requestParams.put("dateofBirth", form.dateofBirth);
        requestParams.put("photo", "");
        requestParams.put("housePhoto", "");
        requestParams.put("personCount", form.personCount);
        requestParams.put("geoLocation", form.geoLocation);
        requestParams.put("district", form.district);
        requestParams.put("houseNumber", form.houseNumber);
        requestParams.put("mandal", form.mandal);
        requestParams.put("pincode", form.pincode);
        requestParams.put("state", form.state);
        requestParams.put("village", form.village);
        requestParams.put("aadhar", "");
        requestParams.put("aadharNumber", form.aadharNumber);
        requestParams.put("traditionalStoveImage", "");
        requestParams.put("agreement", "");
        requestParams.put("dateOfDistribution", form.dateOfDistribution);
        requestParams.put("kyc", form.kyc);
        requestParams.put("manufacturer", form.manufacturer);
        requestParams.put("newStoveImage", "");
        requestParams.put("serialNumber", form.serialNumber);
        requestParams.put("serialNumberImage", "");
        requestParams.put("typeofStove", form.typeofStove);
        requestParams.put("bankAccountNumber", form.bankAccountNumber);
        requestParams.put("bankBranch", form.bankBranch);
        requestParams.put("bankDocument", "");
        requestParams.put("bankIfsc", form.bankIfsc);
        requestParams.put("bankName", form.bankName);
        requestParams.put("createdBy", createdByObject);
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("error")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Uploaded successfully...", Toast.LENGTH_SHORT).show();
                    final String id;
                    if (!form.isOnlineRecord) {
                        id = jsonObject.getJSONObject("data").getString("_id");
                    } else {
                        id = form.onlineFormId;
                    }
                    form_id = form.form_id;

                    attachments = new ArrayList<>();

                 /*   attachments.add(new AttachmentModel(id, "photo", form.beneficiaryPhoto));
                    attachments.add(new AttachmentModel(id, "aadhar", form.aadhar));
                    attachments.add(new AttachmentModel(id, "traditionalStoveImage", form.traditionalStoveImage));
                    attachments.add(new AttachmentModel(id, "newStoveImage", form.newStoveImage));
                    attachments.add(new AttachmentModel(id, "agreement", form.agreement));*/

                    attachments.add(new AttachmentModel(id, "photo", getBytesFromBitmap(form.beneficiaryPhotoPath)));
                    deleteFileFromExternalStorage(form.beneficiaryPhotoPath);
                    attachments.add(new AttachmentModel(id, "aadhar", getBytesFromBitmap(form.aadharPath)));
                    deleteFileFromExternalStorage(form.aadharPath);
                    attachments.add(new AttachmentModel(id, "traditionalStoveImage", getBytesFromBitmap(form.traditionalStoveImagePath)));
                    deleteFileFromExternalStorage(form.traditionalStoveImagePath);
                    attachments.add(new AttachmentModel(id, "newStoveImage", getBytesFromBitmap(form.newStovePath)));
                    deleteFileFromExternalStorage(form.newStovePath);
                    attachments.add(new AttachmentModel(id, "agreement", getBytesFromBitmap(form.agreementPath)));
                    deleteFileFromExternalStorage(form.agreementPath);
                    index = 0;
                    nextSync();
//                    UploadImageOne(form.beneficiaryPhoto, id, "photo");
////                        UploadImageOne(form.housePhoto, id, "housePhoto");
//                    UploadImageOne(form.aadhar, id, "aadhar");
//                    UploadImageOne(form.traditionalStoveImage, id, "traditionalStoveImage");
//                    UploadImageOne(form.newStoveImage, id, "newStoveImage");
////                        UploadImageOne(form.serialNumberImage, id, "serialNumberImage");
//                    UploadImageOne(form.agreement, id, "agreement");
////                        UploadImageOne(form.bankDocument, id, "bankDocument");
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextSync() {
        if (index > (attachments.size() - 1)) {
            Toast.makeText(getApplicationContext(), "All images are uploaded", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            appDatabase.formDao().deleteByUserId(form_id);
            LoadList();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return;
        }
        AttachmentModel attachmentModel = attachments.get(index);
        if (attachmentModel == null || attachmentModel.attachment == null || attachmentModel.attachment.length == 0) {
            index++;
            nextSync();
            return;
        }
        Log.i("Image-Sync", attachmentModel.name);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if ("photo".equals(attachmentModel.name)) {
            progressDialog.setMessage("Uploading Image...\nBeneficiary with cook stove at home");
        } else if ("aadhar".equals(attachmentModel.name)) {
            progressDialog.setMessage("Uploading Image...\nID card");
        } else if ("traditionalStoveImage".equals(attachmentModel.name)) {
            progressDialog.setMessage("Uploading Image...\nTraditional stove");
        } else if ("newStoveImage".equals(attachmentModel.name)) {
            progressDialog.setMessage("Uploading...\n Serial No. Cook stove");
        } else if ("agreement".equals(attachmentModel.name)) {
            progressDialog.setMessage("Uploading...\nAgreement");
        } else {
            progressDialog.setMessage("Uploading...");
        }
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(attachmentModel.attachment, 0, attachmentModel.attachment.length);
        ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bao1);
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(bao1.toByteArray());

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        final String imgUploadUrl = Constants.PRODUCTION_URL + "api/cookStove/addImage/" + attachmentModel.id + "/" + attachmentModel.name;
        RequestParams requestParams = new RequestParams();
        requestParams.put("file", byteArrayInputStream1, "IMG_" + System.currentTimeMillis() + ".png");
        client.post(imgUploadUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    if (jsonObject.getBoolean("error")) {
                        final String message = jsonObject.getString("message");
                        attachmentSyncRetry(message);
                        progressDialog.dismiss();
                        return;
                    }
                    index++;
                    nextSync();
                } catch (JSONException e) {
                    e.printStackTrace();
                    attachmentSyncRetry(e.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                final String errorMsr = error.getMessage();
                final String message;
                if (errorMsr == null || errorMsr.length() == 0) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    message = "Unable to connect with server";
                } else if (errorMsr.toLowerCase().startsWith("failed to connect to")) {
                    message = "Server not reachable";
                } else if ("timeout".equalsIgnoreCase(errorMsr)) {
                    message = "Request timeout. please try again";
                } else {
                    message = errorMsr;
                }
                error.printStackTrace();
                attachmentSyncRetry(message);
                progressDialog.dismiss();
            }
        });
    }

    public void deleteFileFromExternalStorage(String fileName) {
        String root;

        if (Build.VERSION_CODES.S_V2 == Build.VERSION.SDK_INT || Build.VERSION_CODES.S == Build.VERSION.SDK_INT || Build.VERSION_CODES.R == Build.VERSION.SDK_INT) {
            root = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()).toString();
        } else {
            root = Environment.getExternalStorageDirectory().getPath().toString();
        }
        try {
            File file = new File(fileName);
            Log.d("deleted location", "" + fileName);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("App", "Exception while deleting file " + e.getMessage());
        }

    }

    private void attachmentSyncRetry(@Nullable String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setCancelable(false)
                .setMessage("Error while syncing the attachment." + (message == null || message.trim().length() == 0 ? "" : "Error details: " + message))
                .setPositiveButton(R.string.retry, (dialog, which) -> nextSync())
                .show();
    }

    public static class AttachmentModel {
        private final String id;
        private final String name;
        private final byte[] attachment;

        public AttachmentModel(String id, String name, byte[] attachment) {
            this.id = id;
            this.name = name;
            this.attachment = attachment;
        }
    }


    public byte[] getBytesFromBitmap(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            if (bitmap != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                return output.toByteArray();
            }
        }

        return new byte[0];

    }
}


//    synchronized void syncData() {
//        if (!NetworkUtil.isOnline(this)) {
//            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (list == null || list.size() == 0) {
//            return;
//        }
//        progressDialog.setMessage("Uploading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        //NewMethodTwo();
//        syncFirstItem();
//    }

//    private void syncFirstItem() {
//        JSONObject createdByObject = new JSONObject();
//        try {
//            createdByObject.put("_id", CoreCarbonSharedPreferences.getUserId());
//            createdByObject.put("status", CoreCarbonSharedPreferences.getStatus());
//            createdByObject.put("isMobileApp", CoreCarbonSharedPreferences.isMobileApp());
//            createdByObject.put("cookstoveAccess", CoreCarbonSharedPreferences.getCookStoveAccess());
//            createdByObject.put("agroAccess", CoreCarbonSharedPreferences.getAgroAccess());
//            createdByObject.put("name", CoreCarbonSharedPreferences.getUserName());
//            createdByObject.put("mobileNumber", CoreCarbonSharedPreferences.getMobile());
//            createdByObject.put("role", CoreCarbonSharedPreferences.getUserRole());
//            createdByObject.put("isAdmin", CoreCarbonSharedPreferences.getisAdmin());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String url = Constants.PRODUCTION_URL;
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(20000);
//        RequestParams requestParams = new RequestParams();
//        final Form form = list.get(0);
//        if (form.isOnlineRecord) {
//            requestParams.put("_id", form.onlineFormId);
//            url = url + "api/generic/update/CookStove";
//        } else {
//            url = url + "api/generic/add/CookStove";
//        }
//        requestParams.put("name", form.name);
//        requestParams.put("mobileNumber", form.mobileNumber);
//        requestParams.put("typeOfMobile", form.typeOfMobile);
//        requestParams.put("dateofBirth", form.dateofBirth);
//        requestParams.put("photo", "");
//        requestParams.put("housePhoto", "");
//        requestParams.put("personCount", form.personCount);
//        requestParams.put("geoLocation", form.geoLocation);
//        requestParams.put("district", form.district);
//        requestParams.put("houseNumber", form.houseNumber);
//        requestParams.put("mandal", form.mandal);
//        requestParams.put("pincode", form.pincode);
//        requestParams.put("state", form.state);
//        requestParams.put("village", form.village);
//        requestParams.put("aadhar", "");
//        requestParams.put("aadharNumber", form.aadharNumber);
//        requestParams.put("traditionalStoveImage", "");
//        requestParams.put("agreement", "");
//        requestParams.put("dateOfDistribution", form.dateOfDistribution);
//        requestParams.put("kyc", form.kyc);
//        requestParams.put("manufacturer", form.manufacturer);
//        requestParams.put("newStoveImage", "");
//        requestParams.put("serialNumber", form.serialNumber);
//        requestParams.put("serialNumberImage", "");
//        requestParams.put("typeofStove", form.typeofStove);
//        requestParams.put("bankAccountNumber", form.bankAccountNumber);
//        requestParams.put("bankBranch", form.bankBranch);
//        requestParams.put("bankDocument", "");
//        requestParams.put("bankIfsc", form.bankIfsc);
//        requestParams.put("bankName", form.bankName);
//        requestParams.put("createdBy", createdByObject);
//        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
//        client.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    String str = new String(responseBody);
//                    JSONObject jsonObject = new JSONObject(str);
//                    if (jsonObject.getBoolean("error")) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Toast.makeText(getApplicationContext(), "Uploaded successfully...", Toast.LENGTH_SHORT).show();
//                    final String id;
//                    if (!form.isOnlineRecord) {
//                        id = jsonObject.getJSONObject("data").getString("_id");
//                    } else {
//                        id = form.onlineFormId;
//                    }
//                    UploadImageOne(form.beneficiaryPhoto, id, "photo");
////                        UploadImageOne(form.housePhoto, id, "housePhoto");
//                    UploadImageOne(form.aadhar, id, "aadhar");
//                    UploadImageOne(form.traditionalStoveImage, id, "traditionalStoveImage");
//                    UploadImageOne(form.newStoveImage, id, "newStoveImage");
////                        UploadImageOne(form.serialNumberImage, id, "serialNumberImage");
//                    UploadImageOne(form.agreement, id, "agreement");
////                        UploadImageOne(form.bankDocument, id, "bankDocument");
//                    appDatabase.formDao().deleteByUserId(form.form_id);
//                    LoadList();
//                    progressDialog.dismiss();
//                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void NewMethodTwo() {
//        for (i = 0; i < 1; i++) {
//            userphoto = list.get(i).beneficiaryPhoto;
////            housephoto = list.get(i).housePhoto;
//            aadharphoto = list.get(i).aadhar;
//            agreephoto = list.get(i).agreement;
//            newstoveimage = list.get(i).newStoveImage;
////            serialnumberimage = list.get(i).serialNumberImage;
//            traditionalstoveimage = list.get(i).traditionalStoveImage;
////            documentphoto = list.get(i).bankDocument;
//            JSONObject createdByObject = new JSONObject();
//            try {
//                createdByObject.put("_id", CoreCarbonSharedPreferences.getUserId());
//                createdByObject.put("status", CoreCarbonSharedPreferences.getStatus());
//                createdByObject.put("isMobileApp", CoreCarbonSharedPreferences.isMobileApp());
//                createdByObject.put("cookstoveAccess", CoreCarbonSharedPreferences.getCookStoveAccess());
//                createdByObject.put("agroAccess", CoreCarbonSharedPreferences.getAgroAccess());
//                createdByObject.put("name", CoreCarbonSharedPreferences.getUserName());
//                createdByObject.put("mobileNumber", CoreCarbonSharedPreferences.getMobile());
//                createdByObject.put("role", CoreCarbonSharedPreferences.getUserRole());
//                createdByObject.put("isAdmin", CoreCarbonSharedPreferences.getisAdmin());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams requestParams = new RequestParams();
//            requestParams.put("name", list.get(i).name);
//            requestParams.put("mobileNumber", list.get(i).mobileNumber);
//            requestParams.put("typeOfMobile", list.get(i).typeOfMobile);
//            requestParams.put("dateofBirth", list.get(i).dateofBirth);
//            requestParams.put("photo", "");
//            requestParams.put("housePhoto", "");
//            requestParams.put("personCount", list.get(i).personCount);
//            requestParams.put("geoLocation", list.get(i).geoLocation);
//            requestParams.put("district", list.get(i).district);
//            requestParams.put("houseNumber", list.get(i).houseNumber);
//            requestParams.put("mandal", list.get(i).mandal);
//            requestParams.put("pincode", list.get(i).pincode);
//            requestParams.put("state", list.get(i).state);
//            requestParams.put("village", list.get(i).village);
//            requestParams.put("aadhar", "");
//            requestParams.put("aadharNumber", list.get(i).aadharNumber);
//            requestParams.put("traditionalStoveImage", "");
//            requestParams.put("agreement", "");
//            requestParams.put("dateOfDistribution", list.get(i).dateOfDistribution);
//            requestParams.put("kyc", list.get(i).kyc);
//            requestParams.put("manufacturer", list.get(i).manufacturer);
//            requestParams.put("newStoveImage", "");
//            requestParams.put("serialNumber", list.get(i).serialNumber);
//            requestParams.put("serialNumberImage", "");
//            requestParams.put("typeofStove", list.get(i).typeofStove);
//            requestParams.put("bankAccountNumber", list.get(i).bankAccountNumber);
//            requestParams.put("bankBranch", list.get(i).bankBranch);
//            requestParams.put("bankDocument", "");
//            requestParams.put("bankIfsc", list.get(i).bankIfsc);
//            requestParams.put("bankName", list.get(i).bankName);
//            requestParams.put("createdBy", createdByObject);
//
//            String token = String.valueOf(requestParams);
//            client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
//            client.post(Constants.PRODUCTION_URL + "api/generic/add/CookStove", requestParams, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    String str = new String(responseBody);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(str);
//                        if (!jsonObject.getBoolean("error")) {
//                            JSONObject dataobject = jsonObject.getJSONObject("data");
//                            String id = dataobject.getString("_id");
//                            progressDialog.dismiss();
//                            for (int j = 0; j < 1; j++) {
//                                UploadImageOne(list.get(j).beneficiaryPhoto, id, "photo");
////                                UploadImageOne(list.get(j).housePhoto, id, "housePhoto");
//                                UploadImageOne(list.get(j).aadhar, id, "aadhar");
//                                UploadImageOne(list.get(j).traditionalStoveImage, id, "traditionalStoveImage");
//                                UploadImageOne(list.get(j).newStoveImage, id, "newStoveImage");
////                                UploadImageOne(list.get(j).serialNumberImage, id, "serialNumberImage");
//                                UploadImageOne(list.get(j).agreement, id, "agreement");
////                                UploadImageOne(list.get(j).bankDocument, id, "bankDocument");
//                            }
//                            appDatabase.formDao().deleteByUserId(list.get(i - 1).form_id);
//                            LoadList();
//                            progressDialog.dismiss();
////                            appDatabase.formDao().ClearData();
//
////                            binding.recyclerview.setVisibility(View.GONE);
////                            Intent intent = new Intent(OfflineListActivity.this, HomeActivity.class);
////                            startActivity(intent);
////                            finish();
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                        } else {
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//
//        }
//
//    }

//    private void NewSubmitFormsTwo(String name, String mobile, String typeofmobile,String dateofbirth, byte[] userphoto, byte[] housephoto, String personcount, String location, String district, String housenumber, String mandal, String pincode, String state, String village, byte[] aadharphoto, String aadharnumber, byte[] agreephoto, String dateofdistribution, String kyc, String manufacturer, byte[] newstoveimage, String serialnumber, byte[] serialnumberimage, byte[] traditionalstoveimage, String typeofstove, String accountnumber, String branch, byte[] documentphoto, String ifsc, String bankname) {
//
//
//
//        JSONObject createdByObject = new JSONObject();
//        try {
//            createdByObject.put("_id", CoreCarbonSharedPreferences.getUserId());
//            createdByObject.put("status", CoreCarbonSharedPreferences.getStatus());
//            createdByObject.put("isMobileApp", CoreCarbonSharedPreferences.isMobileApp());
//            createdByObject.put("cookstoveAccess", CoreCarbonSharedPreferences.getCookStoveAccess());
//            createdByObject.put("agroAccess", CoreCarbonSharedPreferences.getAgroAccess());
//            createdByObject.put("name", CoreCarbonSharedPreferences.getUserName());
//            createdByObject.put("mobileNumber", CoreCarbonSharedPreferences.getMobile());
//            createdByObject.put("role", CoreCarbonSharedPreferences.getUserRole());
//            createdByObject.put("isAdmin", CoreCarbonSharedPreferences.getisAdmin());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//        AsyncHttpClient client =  new AsyncHttpClient();
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("name",name);
//        requestParams.put("mobileNumber",mobile);
//        requestParams.put("typeOfMobile",typeofmobile);
//        requestParams.put("dateofBirth",dateofbirth);
//        requestParams.put("photo","");
//        requestParams.put("housePhoto","");
//        requestParams.put("personCount",personcount);
//        requestParams.put("geoLocation",location);
//        requestParams.put("district",district);
//        requestParams.put("houseNumber",housenumber);
//        requestParams.put("mandal",mandal);
//        requestParams.put("pincode",pincode);
//        requestParams.put("state",state);
//        requestParams.put("village",village);
//        requestParams.put("aadhar","");
//        requestParams.put("aadharNumber",aadharnumber);
//        requestParams.put("traditionalStoveImage","");
//        requestParams.put("agreement","");
//        requestParams.put("dateOfDistribution",dateofdistribution);
//        requestParams.put("kyc",kyc);
//        requestParams.put("manufacturer",manufacturer);
//        requestParams.put("newStoveImage","");
//        requestParams.put("serialNumber",serialnumber);
//        requestParams.put("serialNumberImage","");
//        requestParams.put("typeofStove",typeofstove);
//        requestParams.put("bankAccountNumber",accountnumber);
//        requestParams.put("bankBranch",branch);
//        requestParams.put("bankDocument","");
//        requestParams.put("bankIfsc",ifsc);
//        requestParams.put("bankName",bankname);
//        requestParams.put("createdBy",createdByObject);
//
//        String token = String.valueOf(requestParams);
//        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
//        client.post(Constants.PRODUCTION_URL+"api/generic/add/CookStove", requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String str = new String(responseBody);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    if(jsonObject.getBoolean("error") == false){
//                        JSONObject dataobject = jsonObject.getJSONObject("data");
//                        String id =  dataobject.getString("_id");
//                        String idd =  dataobject.getString("_id");
//
//
//
//
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).beneficiaryPhoto, id, "photo");
////
////                        }
////
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).housePhoto, id, "housePhoto");
////
////                        }
////
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).aadhar, id, "aadhar");
////
////                        }
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).traditionalStoveImage, id, "traditionalStoveImage");
////
////                        }
////
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).newStoveImage, id, "newStoveImage");
////                        }
////
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).serialNumberImage, id, "serialNumberImage");
////                        }
////
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).agreement, id, "agreement");
////                        }
////                        for(int k =0; k < 1; k++){
////                            UploadImageOne(list.get(k).bankDocument, id, "bankDocument");
////                        }
//
////                        for(int j = 0; j < 2; j++){
////                            UploadImageOne(list.get(i).beneficiaryPhoto, id, "photo");
////                            UploadImageOne(list.get(i).housePhoto, id, "housePhoto");
////                            UploadImageOne(list.get(i).aadhar, id, "aadhar");
////                            UploadImageOne(list.get(i).traditionalStoveImage, id, "traditionalStoveImage");
////                            UploadImageOne(list.get(i).newStoveImage, id, "newStoveImage");
////                            UploadImageOne(list.get(i).serialNumberImage, id, "serialNumberImage");
////                            UploadImageOne(list.get(i).agreement, id, "agreement");
////                            UploadImageOne(list.get(i).bankDocument, id, "bankDocument");
////                        }
////                             progressDialog.dismiss();
////                            appDatabase.formDao().ClearData();
////                            LoadList();
////                            binding.recyclerview.setVisibility(View.GONE);
////                            Intent intent = new Intent(OfflineListActivity.this, HomeActivity.class);
////                            startActivity(intent);
////                            finish();
////                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//
//                    }else{
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }
