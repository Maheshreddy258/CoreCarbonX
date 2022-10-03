package com.ccx.corecarbon.activities;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.databinding.ActivityCookStoveFormBinding;
import com.ccx.corecarbon.dialogs.AddressDropDownDialog;
import com.ccx.corecarbon.room_util.AppDatabase;
import com.ccx.corecarbon.room_util.Form;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.DateUtil;
import com.ccx.corecarbon.util.FetchLocationService;
import com.ccx.corecarbon.util.GlobalData;
import com.ccx.corecarbon.util.NetworkUtil;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class CookStoveFormActivity extends BaseActivity implements LocationListener {

    private static final int PERMISSION_FINE_LOCATION_LOCATION = 333;
    private static final int READ_PERMISSION = 000;
    private static final int WRITE_PERMISSION = 999;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int BENEFICIARY_PHOTO_CODE = 100;
    //    private static final int HOUSE_PHOTO = 200;
    private static final int ID_IMAGE_CODE = 300;
    private static final int TRADITIONAL_STOVE_CODE = 400;
    private static final int NEW_STOVE_CODE = 500;
    //    private static final int SERIAL_NUMBERIMAGE_CODE = 600;
//    private static final int BANK_PASSBOOK_CODE = 700;
    private static final int AGREEMENT_CODE = 800;
    ActivityCookStoveFormBinding binding;
    Toolbar toolbar;
    int selected_image_id = 0;
    Handler handler = new Handler();
    Thread thread;
    String _id;
    String preState, PreDistrict, PreMandal, PreVillage;
    TextWatcher textWatcher;
    private ProgressDialog progressDialog;
    private DateUtil dateUtil;
    private boolean edit, isProcessing;
    private ByteArrayInputStream byteArrayInputStream1, //            byteArrayInputStream2,
            byteArrayInputStream3, byteArrayInputStream4, byteArrayInputStream5, //            byteArrayInputStream6,
    //            byteArrayInputStream7,
    byteArrayInputStream8;
    private Bitmap beneficiaryPhoto_bitmap2, /*housePhoto_birmap2,*/
            idimage_bitmap2, traditionalstove_bitmap2, newstove_bitmap2, /*serialnumber_bitmap2,*/ /*passbook_bitmap2,*/
            agreement_bitmap2;
    private boolean isBeneficiaryPhotoPending, isIdImagePending, isTraditionalStovePending, isNewStovePending, isAgreementPending;

    private String userPhoto,/*housePhoto,*/
            aadharPhoto, /*passbookphoto,*/
            newtove, tradiphoto, agreement/*,serialnumberphoto*/;


    public static byte[] bitmapAsByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            return output.toByteArray();
          /*  int bytes = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            bitmap.copyPixelsToBuffer(buffer);
            return buffer.array();*/
           /* ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(byteBuffer);
            byteBuffer.rewind();
            return byteBuffer.array();*/
        }
        return new byte[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cook_stove_form);
        Toolbar toolbar2 = findViewById(R.id.tool_bar);
        this.toolbar = toolbar2;
        TextView title = toolbar2.findViewById(R.id.title_tv);
        final String userName = CoreCarbonSharedPreferences.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            title.setText(R.string.cook_stove);
        } else {
            title.setText(userName);
        }
        setSupportActionBar(toolbar2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        dateUtil = new DateUtil();
//        Objects.requireNonNull(binding.bankIFSCEditText).setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
//        fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this);

        edit = getIntent().getBooleanExtra("edit", false);
        if (edit) {
            GetData();
            binding.updateBtn.setVisibility(View.VISIBLE);
            binding.submitBtn.setVisibility(View.GONE);
        }

        if (!edit) {
            preState = CoreCarbonSharedPreferences.getState();
            binding.statetextview.setText(preState);
            PreDistrict = CoreCarbonSharedPreferences.getDistrict();
            binding.districttextview.setText(PreDistrict);
            PreVillage = CoreCarbonSharedPreferences.getVillage();
            binding.villagetextview.setText(PreVillage);
            PreMandal = CoreCarbonSharedPreferences.getMandal();
            binding.mandaltextview.setText(PreMandal);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }

        getCurrentLocation();

        binding.beneficiaryDOBTextView.setOnClickListener(view -> {

            final Calendar calendar = Calendar.getInstance();
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(CookStoveFormActivity.this, new MonthPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int selectedMonth, int selectedYear) {
                    binding.beneficiaryDOBTextView.setText(String.valueOf(selectedYear));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));

            builder.setActivatedMonth(Calendar.JULY).setMinYear(1940).setActivatedYear(calendar.get(Calendar.YEAR) - 14).setMaxYear(calendar.get(Calendar.YEAR) - 14).setTitle("Select Year of Birth").showYearOnly().build().show();

        });

        binding.dateOfDistributionTextView.setOnClickListener(view -> {
            Calendar cldr = Calendar.getInstance();
            cldr.add(Calendar.YEAR, 0);
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog datepickerDialog = new DatePickerDialog(CookStoveFormActivity.this, (view1, year1, monthOfYear, dayOfMonth) -> {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String cdate = formatter.format(dateUtil.getMilliFromDate(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1));
                binding.dateOfDistributionTextView.setText(cdate);

            }, cldr.get(1), month, day);
            datepickerDialog.getDatePicker().setMaxDate(cldr.getTimeInMillis());
            datepickerDialog.show();
        });


        binding.userPhotoIv.setOnClickListener(view -> {
            selected_image_id = BENEFICIARY_PHOTO_CODE;
            OpenCamera(selected_image_id);
        });

//        binding.housePhotoIv.setOnClickListener(view -> {
//            selected_image_id = HOUSE_PHOTO;
//            OpenCamera(selected_image_id);
//        });

        binding.idImageView.setOnClickListener(view -> {
            selected_image_id = ID_IMAGE_CODE;
            OpenCamera(selected_image_id);
        });

        binding.traditionalStoveIv.setOnClickListener(view -> {
            selected_image_id = TRADITIONAL_STOVE_CODE;
            OpenCamera(selected_image_id);
        });

        binding.newStoveIv.setOnClickListener(view -> {
            selected_image_id = NEW_STOVE_CODE;
            OpenCamera(selected_image_id);
        });

        /*binding.serialNumberIv.setOnClickListener(view -> {
            selected_image_id = SERIAL_NUMBERIMAGE_CODE;
            OpenCamera(selected_image_id);
        });
*/
        /*binding.passbookPhotoIv.setOnClickListener(view -> {
            selected_image_id = BANK_PASSBOOK_CODE;
            OpenCamera(selected_image_id);
        });*/

        binding.agreementIv.setOnClickListener(view -> {
            selected_image_id = AGREEMENT_CODE;
            OpenCamera(selected_image_id);
        });

        binding.textInputlocation.getEditText().setOnClickListener(view -> {
            AskP();
//                GetLtLg();
        });


        binding.userPhotoUploadBtn.setOnClickListener(view -> {
//                NewUploadMethod();
            if (byteArrayInputStream1 != null) {
                binding.userPhotoUploadBtn.setText(R.string.uploading);
//                    UploadUserPhoto(UserFile, "userPhoto");
                uploadAttachment(byteArrayInputStream1, "userPhoto");
            } else {
                NoPhotoToast();
            }
        });


//        binding.housePhotoUploadBtn.setOnClickListener(view -> {
//            if (byteArrayInputStream2 != null) {
//                binding.housePhotoUploadBtn.setText(R.string.uploading);
////                    UploadUserPhoto(HouseFile, "housePhoto");
//                UploadNewMethod(byteArrayInputStream2, "housePhoto");
//            } else {
//                NoPhotoToast();
//            }
//        });


        binding.idPhotoUploadBtn.setOnClickListener(view -> {
            if (byteArrayInputStream3 != null) {
                binding.idPhotoUploadBtn.setText(R.string.uploading);
//                    UploadUserPhoto(IdFile, "idPhoto");
                uploadAttachment(byteArrayInputStream3, "idPhoto");
            } else {
                NoPhotoToast();
            }
        });

        binding.traditionalPhotoUploadBtn.setOnClickListener(view -> {
            if (byteArrayInputStream4 != null) {
                binding.traditionalPhotoUploadBtn.setText(R.string.uploading);
//                    UploadUserPhoto(TraditionalFile, "tradiPhoto");
                uploadAttachment(byteArrayInputStream4, "tradiPhoto");
            } else {
                NoPhotoToast();
            }
        });

        binding.newStovePhotoUploadBtn.setOnClickListener(view -> {
            if (byteArrayInputStream5 != null) {
                binding.newStovePhotoUploadBtn.setText(R.string.uploading);
//                    UploadUserPhoto(NewStoveFile, "newPhoto");
                uploadAttachment(byteArrayInputStream5, "newPhoto");
            } else {
                NoPhotoToast();
            }
        });

//        binding.serialNumberPhotoUploadBtn.setOnClickListener(view -> {
//            if (byteArrayInputStream6 != null) {
//                binding.serialNumberPhotoUploadBtn.setText(R.string.uploading);
////                    UploadUserPhoto(SerialNumberFile, "serialPhoto");
//                UploadNewMethod(byteArrayInputStream6, "serialPhoto");
//            } else {
//                NoPhotoToast();
//            }
//        });

//        binding.passbookPhotoUploadBtn.setOnClickListener(view -> {
//            if (byteArrayInputStream7 != null) {
//                binding.passbookPhotoUploadBtn.setText(R.string.uploading);
////                    UploadUserPhoto(BankPassbookFile, "passPhoto");
//                UploadNewMethod(byteArrayInputStream7, "passPhoto");
//            } else {
//                NoPhotoToast();
//            }
//        });

        binding.agreementPhotoUploadBtn.setOnClickListener(view -> {
//                Toast.makeText(getApplicationContext(),"Agree Clicked", Toast.LENGTH_SHORT).show();
            if (byteArrayInputStream8 != null) {
                binding.agreementPhotoUploadBtn.setText(R.string.uploading);
//                    UploadUserPhoto(AgreementFile, "agreePhoto");
                uploadAttachment(byteArrayInputStream8, "agreePhoto");
            } else {
                NoPhotoToast();
            }
        });


        binding.mobiletypetextview.setOnClickListener(view -> {
            setMobileTypeAdapter();
            binding.mobiletypespinner.performClick();

        });


        binding.mobiletypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                ((TextView) adapterView.getChildAt(0)).setVisibility(View.INVISIBLE);
                binding.mobiletypetextview.setText(stateName);
//                binding.mobiletypespinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.statetextview.setOnClickListener(view -> {
            new AddressDropDownDialog(this, AddressDropDownDialog.AddressType.STATE, "", name -> binding.statetextview.setText(name)).show();
//            GetStates();
//            binding.statespinner.performClick();
        });


//        binding.statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String stateName = adapterView.getItemAtPosition(i).toString();
//                binding.statetextview.setText(stateName);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        binding.districttextview.setOnClickListener(view -> {
            new AddressDropDownDialog(this, AddressDropDownDialog.AddressType.DISTRICT, binding.statetextview.getText().toString(), name -> binding.districttextview.setText(name)).show();
//            GEtDistricts(binding.statetextview.getText().toString());
//            binding.districtspinner.performClick();
        });

//        binding.districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String stateName = adapterView.getItemAtPosition(i).toString();
//                binding.districttextview.setText(stateName);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        binding.mandaltextview.setOnClickListener(view -> {
            new AddressDropDownDialog(this, AddressDropDownDialog.AddressType.MANDAL, binding.districttextview.getText().toString(), name -> binding.mandaltextview.setText(name)).show();
//            GetMandals(binding.districttextview.getText().toString());
//            binding.mandalspinner.performClick();
        });

//        binding.mandalspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String stateName = adapterView.getItemAtPosition(i).toString();
//                binding.mandaltextview.setText(stateName);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
////                binding.mandalspinner.setSelection(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        binding.villagetextview.setOnClickListener(view -> {
            new AddressDropDownDialog(this, AddressDropDownDialog.AddressType.VILLAGE, binding.mandaltextview.getText().toString(), name -> binding.villagetextview.setText(name)).show();
//                Toast.makeText(getApplicationContext(),"Clicked", Toast.LENGTH_SHORT).show();
//            GetVillages(binding.mandaltextview.getText().toString());
//            binding.villagespinner.performClick();
        });

//        binding.villagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String stateName = adapterView.getItemAtPosition(i).toString();
//                binding.villagetextview.setText(stateName);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
////                binding.villagespinner.setSelection(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        binding.idtypetextview.setOnClickListener(view -> {
            setIdTypeAdapter();
            binding.idtypespinner.performClick();
        });

        binding.idtypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
                String idtype = adapterView.getItemAtPosition(i).toString();
                binding.idtypetextview.setText(idtype);
                if (idtype.contains("AADHAR")) {
                    binding.idNumberEditText.getText().clear();
                    binding.idNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    binding.idNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (idtype.contains("PAN")) {
                    binding.idNumberEditText.getText().clear();
                    binding.idNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    binding.idNumberEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                } else {
                    binding.idNumberEditText.getText().clear();
                    binding.idNumberEditText.removeTextChangedListener(textWatcher);
                    binding.idNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.manufacturertextview.setOnClickListener(view -> {
            setManufacturerAdapter();
            binding.manufacturespinner.performClick();
        });

        binding.manufacturespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
                binding.manufacturertextview.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.typeofstovetextview.setOnClickListener(view -> {
            setStoveTypeAdapter();
            binding.stovetypespinner.performClick();
        });

        binding.stovetypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
                binding.typeofstovetextview.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.userphotoViewIv.setOnClickListener(view -> GotoImageView("user"));

//        binding.housephotoViewIv.setOnClickListener(view -> GotoImageView("house"));

        binding.idphotoViewIv.setOnClickListener(view -> GotoImageView("id"));

        binding.tradiphotoViewIv.setOnClickListener(view -> GotoImageView("tradi"));

        binding.newdiphotoViewIv.setOnClickListener(view -> GotoImageView("new"));

//        binding.serialphotoViewIv.setOnClickListener(view -> GotoImageView("serial"));

        binding.agreementphotoViewIv.setOnClickListener(view -> GotoImageView("agree"));

//        binding.passbookphotoViewIv.setOnClickListener(view -> GotoImageView("pass"));

        binding.updateBtn.setOnClickListener(view -> {
            String beneficiaryname = binding.beneficiaryNameEditText.getText().toString();
            String mobilenumber = binding.beneficiaryMobileEditText.getText().toString();
            String mobiletype = binding.mobiletypetextview.getText().toString();
            String dob = binding.beneficiaryDOBTextView.getText().toString();
            String numberofpersons = binding.beneficiaryPersonsEditText.getText().toString();
            String location = binding.textInputlocation.getEditText().getText().toString();
            if (location.equalsIgnoreCase("Click here to get your location")) location = "";
            String housenumber = binding.houseDetailsEditText.getText().toString();
            String state = binding.statetextview.getText().toString();
            String district = binding.districttextview.getText().toString();
            String mandal = binding.mandaltextview.getText().toString();
            String village = binding.villagetextview.getText().toString();
            String pincode = binding.pinCodeEditText.getText().toString();
            String idtype = binding.idtypetextview.getText().toString();
            String idnumber = binding.idNumberEditText.getText().toString();
            String dateofdistribution = binding.dateOfDistributionTextView.getText().toString();
            String serialnumberonstove = binding.serialNumberEditText.getText().toString();
            String manufacturername = binding.manufacturertextview.getText().toString();
            String stovetype = binding.typeofstovetextview.getText().toString();
            String bankname = binding.bankNameEditText.getText().toString();
            String accountnumber = binding.bankAcNumberEditText.getText().toString();
            String branch = binding.bankBranchEditText.getText().toString();
            String ifsc = binding.bankIFSCEditText.getText().toString();
            if (dateofdistribution.toLowerCase(Locale.ROOT).contains("dd/mm/yyyy")) {
                dateofdistribution = "";
            }
            if (manufacturername.contains("Select Manufacturer Name") || manufacturername.equalsIgnoreCase("Select manufacturer")) {
                manufacturername = "";
            }
            if (stovetype.contains("Select Stove Type") || stovetype.equalsIgnoreCase("Select Type of Stove")) {
                stovetype = "";
            }
            if (beneficiaryname.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter beneficiary name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mobilenumber.isEmpty() || mobilenumber.length() < 10) {
                Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mobiletype.toLowerCase(Locale.ROOT).contains("select")) {
                Toast.makeText(getApplicationContext(), "Please select Mobile Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dob.isEmpty() || dob.contains("Select Year")) {
                Toast.makeText(getApplicationContext(), "Please select birth year", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPhoto == null) {
                Toast.makeText(getApplicationContext(), R.string.please_capture_beneficiary_with_cookstove_at_home, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isBeneficiaryPhotoPending) {
                Toast.makeText(getApplicationContext(), R.string.please_upload_beneficiary_with_cookstove_at_home, Toast.LENGTH_SHORT).show();
                return;
            }
            /*
             if (housePhoto == null) {
                Toast.makeText(getApplicationContext(), "Please capture House photo", Toast.LENGTH_SHORT).show();
            return;
            }*/
            if (tradiphoto == null) {
                Toast.makeText(getApplicationContext(), R.string.please_capture_traditional_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isTraditionalStovePending) {
                Toast.makeText(getApplicationContext(), R.string.please_upload_traditional_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (numberofpersons.isEmpty() || Integer.valueOf(numberofpersons) < 0 || Integer.valueOf(numberofpersons) > 50) {
                Toast.makeText(getApplicationContext(), "Please enter number of persons", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(numberofpersons) <= 0 || Integer.parseInt(numberofpersons) > 50) {
                Toast.makeText(getApplicationContext(), "Number of persons must be 1 or within 50", Toast.LENGTH_SHORT).show();
                return;
            }
            if (location.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please get your location", Toast.LENGTH_SHORT).show();
                return;
            }
            if (state.isEmpty() || state.contains("Select State")) {
                Toast.makeText(getApplicationContext(), "Please select state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (district.isEmpty() || district.contains("Select District")) {
                Toast.makeText(getApplicationContext(), "Please select district", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mandal.isEmpty() || mandal.contains("Select Mandal")) {
                Toast.makeText(getApplicationContext(), "Please select mandal", Toast.LENGTH_SHORT).show();
                return;
            }
            if (village.isEmpty() || village.contains("Select Village")) {
                Toast.makeText(getApplicationContext(), "Please select village", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pincode.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter pincode", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idtype.isEmpty() || state.contains("Select ID Type")) {
                Toast.makeText(getApplicationContext(), "Please select ID Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idnumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter valid ID Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (aadharPhoto == null) {
                Toast.makeText(getApplicationContext(), R.string.please_capture_id_card, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isIdImagePending) {
                Toast.makeText(getApplicationContext(), R.string.please_upload_id_card, Toast.LENGTH_SHORT).show();
                return;
            }
            if (dateofdistribution.isEmpty() || dateofdistribution.contains("dd/mm/yyyy")) {
                Toast.makeText(getApplicationContext(), "Please enter date of distribution", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newtove == null) {
                Toast.makeText(getApplicationContext(), R.string.please_capture_serial_no_cook_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isNewStovePending) {
                Toast.makeText(getApplicationContext(), R.string.please_upload_serial_no_cook_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (serialnumberonstove.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter serial number on stove", Toast.LENGTH_SHORT).show();
                return;
            }
            /* if (serialnumberphoto == null) {
                Toast.makeText(getApplicationContext(), "Please capture Serial Number photo on Stove", Toast.LENGTH_SHORT).show();
            return;
            } */
            if (manufacturername.isEmpty() || manufacturername.contains("Select Manufacturer Name")) {
                Toast.makeText(getApplicationContext(), "Please select manufacturer name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (stovetype.isEmpty() || stovetype.contains("Select Stove Type")) {
                Toast.makeText(getApplicationContext(), "Please select stove type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (agreement == null) {
                Toast.makeText(getApplicationContext(), R.string.please_capture_agreement, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isAgreementPending) {
                Toast.makeText(getApplicationContext(), R.string.please_upload_agreement, Toast.LENGTH_SHORT).show();
                return;
            }
            UpdateRecord(beneficiaryname, mobilenumber, numberofpersons, location, housenumber, state, district, mandal, village, pincode, idtype, idnumber, serialnumberonstove, manufacturername, stovetype, bankname, accountnumber, ifsc, branch, mobiletype);
        });


        binding.submitBtn.setOnClickListener(view -> {
            //for creating the dummy values in offline
           /* if (!NetworkUtil.isOnline(this)) {
                createDummyOfflineData();
                return;
            }*/
            if (isProcessing)
                return;
            isProcessing = true;
            String beneficiaryname = binding.beneficiaryNameEditText.getText().toString();
            String mobilenumber = binding.beneficiaryMobileEditText.getText().toString();
            String mobiletype = binding.mobiletypetextview.getText().toString();
            String dob = binding.beneficiaryDOBTextView.getText().toString();
            String numberofpersons = binding.beneficiaryPersonsEditText.getText().toString();
            String location = binding.textInputlocation.getEditText().getText().toString();
            if (location.equalsIgnoreCase("Click here to get your location")) location = "";
            String housenumber = binding.houseDetailsEditText.getText().toString();
            String state = binding.statetextview.getText().toString();
            String district = binding.districttextview.getText().toString();
            String mandal = binding.mandaltextview.getText().toString();
            String village = binding.villagetextview.getText().toString();
            String pincode = binding.pinCodeEditText.getText().toString();
            String idtype = "";
            if (binding.idtypespinner.getSelectedItem() != null)
                idtype = binding.idtypespinner.getSelectedItem().toString();
            String idnumber = binding.idNumberEditText.getText().toString();
            String dateofdistribution = binding.dateOfDistributionTextView.getText().toString();
            if (dateofdistribution.toLowerCase(Locale.ROOT).contains("dd/mm/yyyy")) {
                dateofdistribution = "";
            }
            String serialnumberonstove = binding.serialNumberEditText.getText().toString();
            String manufacturername = binding.manufacturertextview.getText().toString();
            if (manufacturername.contains("Select Manufacturer Name") || manufacturername.equalsIgnoreCase("Select manufacturer")) {
                manufacturername = "";
            }
            String stovetype = binding.typeofstovetextview.getText().toString();
            if (stovetype.contains("Select Stove Type") || stovetype.equalsIgnoreCase("Select Type of Stove")) {
                stovetype = "";
            }
            String bankname = binding.bankNameEditText.getText().toString();
            String accountnumber = binding.bankAcNumberEditText.getText().toString();
            String branch = binding.bankBranchEditText.getText().toString();
            String ifsc = binding.bankIFSCEditText.getText().toString();

            if (beneficiaryname.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter beneficiary name", Toast.LENGTH_SHORT).show();
                isProcessing = false;
                return;
            }
            if (mobilenumber.isEmpty() || mobilenumber.length() < 10) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mobiletype.toLowerCase(Locale.ROOT).contains("select")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select Mobile Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dob.isEmpty() || dob.contains("Select")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select birth year", Toast.LENGTH_SHORT).show();
                return;
            }
            if (beneficiaryPhoto_bitmap2 == null) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_capture_beneficiary_with_cookstove_at_home, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isBeneficiaryPhotoPending && NetworkUtil.isOnline(this)) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_upload_beneficiary_with_cookstove_at_home, Toast.LENGTH_SHORT).show();
                return;
            }
            /*
            if (housePhoto_birmap2 == null) {
                Toast.makeText(getApplicationContext(), "Please capture House photo", Toast.LENGTH_SHORT).show();
            return;
            }
            */
            if (traditionalstove_bitmap2 == null) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_capture_traditional_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isTraditionalStovePending && NetworkUtil.isOnline(this)) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_upload_traditional_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (numberofpersons.isEmpty() || Integer.valueOf(numberofpersons) < 0 || Integer.valueOf(numberofpersons) > 50) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter number of persons", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(numberofpersons) <= 0 || Integer.parseInt(numberofpersons) > 50) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Number of persons must be 1 or within 50", Toast.LENGTH_SHORT).show();
                return;
            }
            if (location.isEmpty()) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please get your location", Toast.LENGTH_SHORT).show();
                return;
            }
            if (state.isEmpty() || state.contains("Select State")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (district.isEmpty() || district.contains("Select")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select district", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mandal.isEmpty() || mandal.contains("Select")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select mandal", Toast.LENGTH_SHORT).show();
                return;
            }
            if (village.isEmpty() || village.contains("Select")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select village", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pincode.isEmpty()) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter pincode", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idtype.isEmpty() || state.contains("Select ID Type")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select ID Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idnumber.isEmpty()) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter valid ID Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idimage_bitmap2 == null) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_capture_id_card, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isIdImagePending && NetworkUtil.isOnline(this)) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_upload_id_card, Toast.LENGTH_SHORT).show();
                return;
            }
            if (dateofdistribution.isEmpty() || dateofdistribution.contains("dd/mm/yyyy")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter date of distribution", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newstove_bitmap2 == null) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_capture_serial_no_cook_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isNewStovePending && NetworkUtil.isOnline(this)) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_upload_serial_no_cook_stove, Toast.LENGTH_SHORT).show();
                return;
            }
            /*
              if (serialnumber_bitmap2 == null) {
                Toast.makeText(getApplicationContext(), "Please capture Serial Number photo on Stove", Toast.LENGTH_SHORT).show();
           return;
           }
           */
            if (serialnumberonstove.isEmpty()) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please enter serial number on stove", Toast.LENGTH_SHORT).show();
                return;
            }
            if (manufacturername.isEmpty() || manufacturername.contains("Select Manufacturer Name")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select manufacturer name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (stovetype.isEmpty() || stovetype.contains("Select Stove Type")) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), "Please select stove type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (agreement_bitmap2 == null) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_capture_agreement, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isAgreementPending && NetworkUtil.isOnline(this)) {
                isProcessing = false;
                Toast.makeText(getApplicationContext(), R.string.please_upload_agreement, Toast.LENGTH_SHORT).show();
                return;
            }
            SubmitForm(beneficiaryname, mobilenumber, numberofpersons, location, housenumber, state, district, mandal, village, pincode, idtype, idnumber, serialnumberonstove, manufacturername, stovetype, bankname, accountnumber, ifsc, branch, mobiletype);
        });
        String status = getIntent().getStringExtra("status");
        if (status != null && status.equalsIgnoreCase("verified")) {
            isProcessing = true;
            disableEnableButtons(false);
        }
    }

    private void disableEnableButtons(boolean isEnable) {
        binding.updateBtn.setClickable(isEnable);
        binding.submitBtn.setClickable(isEnable);
        binding.updateBtn.setEnabled(isEnable);
        binding.submitBtn.setEnabled(isEnable);
    }

    public void OpenCamera(int id) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_DENIED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, id);
//                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), id);
            } else {
                requestPermissions(new String[]{"android.permission.CAMERA"}, CAMERA_PERMISSION_CODE);
            }
        }*/
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);
        intent.putExtra(ImageSelectActivity.FLAG_CROP, false);
        startActivityForResult(intent, id);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetLtLg();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;

            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), this.selected_image_id);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{"android.permission.CAMERA"}, CAMERA_PERMISSION_CODE);
                    }
                }
                break;
            case READ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }

                break;

            case WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                return;

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = "";
        if (data != null) filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
        if (requestCode == BENEFICIARY_PHOTO_CODE && resultCode == -1) {
            binding.userPhotoWarningView.setVisibility(View.GONE);
            isBeneficiaryPhotoPending = true;
            Bitmap beneficiaryPhoto_bitmap = BitmapFactory.decodeFile(filePath);
            beneficiaryPhoto_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.userPhotoIv.setImageBitmap(beneficiaryPhoto_bitmap);
            binding.userphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.userPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.userPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
            beneficiaryPhoto_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao1);
            byteArrayInputStream1 = new ByteArrayInputStream(bao1.toByteArray());
//            UserFile = new File (String.valueOf((InputStream) byteArrayInputStream6), "IMG_" + System.currentTimeMillis() + ".JPEG");
        } /*else if (requestCode == HOUSE_PHOTO && resultCode == -1) {
            housePhoto_birmap = BitmapFactory.decodeFile(filePath);
            housePhoto_birmap2 = BitmapFactory.decodeFile(filePath);
            binding.housePhotoIv.setImageBitmap(housePhoto_birmap);
            binding.housephotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.housePhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.housePhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
            housePhoto_birmap.compress(Bitmap.CompressFormat.PNG, 100, bao2);
            byteArrayInputStream2 = new ByteArrayInputStream(bao2.toByteArray());

        }*/ else if (requestCode == ID_IMAGE_CODE && resultCode == -1) {
            binding.idImageWarningView.setVisibility(View.GONE);
            isIdImagePending = true;
            Bitmap idimage_bitmap = BitmapFactory.decodeFile(filePath);
            idimage_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.idImageView.setImageBitmap(idimage_bitmap);
            binding.idphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.idPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.idPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao3 = new ByteArrayOutputStream();
            idimage_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao3);
            byteArrayInputStream3 = new ByteArrayInputStream(bao3.toByteArray());
        } else if (requestCode == TRADITIONAL_STOVE_CODE && resultCode == -1) {
            binding.traditionalStoveWarningView.setVisibility(View.GONE);
            isTraditionalStovePending = true;
            Bitmap traditionalstove_bitmap = BitmapFactory.decodeFile(filePath);
            traditionalstove_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.traditionalStoveIv.setImageBitmap(traditionalstove_bitmap);
            binding.tradiphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.traditionalPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.traditionalPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao4 = new ByteArrayOutputStream();
            traditionalstove_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao4);
            byteArrayInputStream4 = new ByteArrayInputStream(bao4.toByteArray());

        } else if (requestCode == NEW_STOVE_CODE && resultCode == -1) {
            binding.newStoveWarningView.setVisibility(View.GONE);
            isNewStovePending = true;
            Bitmap newstove_bitmap = BitmapFactory.decodeFile(filePath);
            newstove_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.newStoveIv.setImageBitmap(newstove_bitmap);
            binding.newdiphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.newStovePhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.newStovePhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao5 = new ByteArrayOutputStream();
            newstove_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao5);
            byteArrayInputStream5 = new ByteArrayInputStream(bao5.toByteArray());

        } /*else if (requestCode == SERIAL_NUMBERIMAGE_CODE && resultCode == -1) {
            serialnumber_bitmap = BitmapFactory.decodeFile(filePath);
            serialnumber_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.serialNumberIv.setImageBitmap(serialnumber_bitmap);
            binding.serialphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.serialNumberPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.serialNumberPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao6 = new ByteArrayOutputStream();
            serialnumber_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao6);
            byteArrayInputStream6 = new ByteArrayInputStream(bao6.toByteArray());

        } else if (requestCode == BANK_PASSBOOK_CODE && resultCode == -1) {
            passbook_bitmap = BitmapFactory.decodeFile(filePath);
            passbook_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.passbookPhotoIv.setImageBitmap(passbook_bitmap);
            binding.passbookphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.passbookPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.passbookPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao7 = new ByteArrayOutputStream();
            passbook_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao7);
            byteArrayInputStream7 = new ByteArrayInputStream(bao7.toByteArray());

        }*/ else if (requestCode == AGREEMENT_CODE && resultCode == -1) {
            binding.agreementPhotoWarningView.setVisibility(View.GONE);
            isAgreementPending = true;
            Bitmap agreement_bitmap = BitmapFactory.decodeFile(filePath);
            agreement_bitmap2 = BitmapFactory.decodeFile(filePath);
            binding.agreementIv.setImageBitmap(agreement_bitmap);
            binding.agreementphotoViewIv.setVisibility(View.VISIBLE);
            if (NetworkUtil.isOnline(this)) {
                binding.agreementPhotoUploadBtn.setVisibility(View.VISIBLE);
            } else {
                binding.agreementPhotoUploadBtn.setVisibility(View.GONE);
            }
            ByteArrayOutputStream bao8 = new ByteArrayOutputStream();
            agreement_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao8);
            byteArrayInputStream8 = new ByteArrayInputStream(bao8.toByteArray());
        }
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (edit) {
            return;
        }
        try {
            binding.textInputlocation.getEditText().setText(location.getLatitude() + "," + location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

//    private void GetStates() {
//        String statesString = CoreCarbonSharedPreferences.getStates();
//        List<String> states = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(statesString);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                states.add(jsonObject1.getString("stateName"));
//            }
//
//            states.add(0, "Select State");
//            String[] str = states.toArray(new String[states.size()]);
//
//            Log.i("STATES", str.toString());
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.simple_spinner_item, str);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.statespinner.setAdapter(dataAdapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void GEtDistricts(String state) {
//
//
//        String districtString = CoreCarbonSharedPreferences.getDistricts();
//        List<String> dist = new ArrayList<>();
//        dist.clear();
//
//        try {
//            JSONArray jsonArray = new JSONArray(districtString);
//            for (int i = 0; i < jsonArray.length(); i++) {
////                        for(int j = 0; j < districtData.size(); j++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                JSONObject stateIdObject = jsonObject.getJSONObject("stateId");
//                String stateName = stateIdObject.getString("stateName");
//                if (stateName.contentEquals(state)) {
//                    String distr = jsonObject.getString("districtName");
//                    dist.add(distr);
//                }
//
//            }
//            dist.add(0, "Select District");
//            String[] strrr = dist.toArray(new String[dist.size()]);
//            Log.i("DISTRICTS", strrr.toString());
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.simple_spinner_item, strrr);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.districtspinner.setAdapter(dataAdapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void GetMandals(String district) {
//        String mandalString = CoreCarbonSharedPreferences.getMandals();
//        List<String> village = new ArrayList<>();
//
//
//        try {
//            JSONArray jsonArray = new JSONArray(mandalString);
//            for (int i = 0; i < jsonArray.length(); i++) {
////                        for(int j = 0; j < districtData.size(); j++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                JSONObject stateIdObject = jsonObject.getJSONObject("districtId");
//                String districtName = stateIdObject.getString("districtName");
//                if (districtName.contentEquals(district)) {
//                    String distr = jsonObject.getString("mandalName");
//                    village.add(distr);
//                }
//
//            }
//            village.add(0, "Select Mandal");
//            String[] strrr = village.toArray(new String[village.size()]);
//            Log.i("DISTRICTS", strrr.toString());
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.simple_spinner_item, strrr);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.mandalspinner.setAdapter(dataAdapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void GetVillages(String mandal) {
//
//        String mandalString = CoreCarbonSharedPreferences.getVillages();
//        List<String> village = new ArrayList<>();
//
//        try {
//            JSONArray jsonArray = new JSONArray(mandalString);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                JSONObject mandalObject = jsonObject.getJSONObject("mandalId");
////                JSONObject districtObject = stateIdObject.getJSONObject("districtId");
//                String mandalName = mandalObject.getString("mandalName");
//                if (mandalName.contentEquals(mandal)) {
//                    village.add(jsonObject.getString("villageName"));
//                }
//            }
//            village.add(0, "Select Village");
//            String[] strrr = village.toArray(new String[village.size()]);
//            Log.i("DISTRICTS", strrr.toString());
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.simple_spinner_item, strrr);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.villagespinner.setAdapter(dataAdapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void setMobileTypeAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.type_of_mobile_number_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.mobiletypespinner.setAdapter(adapter);
    }

    private void setManufacturerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.manufacturer_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.manufacturespinner.setAdapter(adapter);
    }

    private void setStoveTypeAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.stove_type_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.stovetypespinner.setAdapter(adapter);
    }

    private void setIdTypeAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CookStoveFormActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.id_type_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.idtypespinner.setAdapter(adapter);
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 0, this);

        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void stopFetching() {
        handler.removeCallbacksAndMessages(null);
        stopService(new Intent(this, FetchLocationService.class));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
//        GetLtLg();
        getCurrentLocation();
        super.onResume();
        GlobalData.bitmap = null;
    }

    private void AskP() {

        if (ContextCompat.checkSelfPermission(CookStoveFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CookStoveFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION_LOCATION);
        } else {
            GetLtLg();
        }

    }

    @SuppressLint("MissingPermission")
    private void GetLtLg() {
        if (edit) {
            return;
        }
        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(GPS_PROVIDER, 3000, 1, CookStoveFormActivity.this);
            Location currentLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (currentLocation != null) {
                binding.textInputlocation.getEditText().setText(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

//    private void UploadUserPhoto(File file, String photo) {
//        thread = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                if (file != null) {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                            && ContextCompat.checkSelfPermission(CookStoveFormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(CookStoveFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
//                    }
//
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//
//                    Call<ImageUploadResponse> call = RetrofitClient.getInstance().getMyApi().uploadUserphoto(fileToUpload, filename);
//                    call.enqueue(new Callback<ImageUploadResponse>() {
//                        @Override
//                        public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
//                            if (!response.body().error) {
//
//                                switch (photo) {
//                                    case "userPhoto":
//                                        CoreCarbonSharedPreferences.SetUserImageIdFromResponse(response.body().getData());
//                                        binding.userPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.userPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;
//
//                                   /* case "housePhoto":
//                                        CoreCarbonSharedPreferences.SetHouseImageIdFromResponse(response.body().getData());
//                                        binding.housePhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.housePhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;*/
//
//                                    case "idPhoto":
//                                        CoreCarbonSharedPreferences.SetIDImageIdFromResponse(response.body().getData());
//                                        binding.idPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.idPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;
//
//                                    case "tradiPhoto":
//                                        CoreCarbonSharedPreferences.SetTradiImageIdFromResponse(response.body().getData());
//                                        binding.traditionalPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.traditionalPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;
//
//                                    case "newPhoto":
//                                        CoreCarbonSharedPreferences.SetNewImageIdFromResponse(response.body().getData());
//                                        binding.newStovePhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.newStovePhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;
//
//                                  /*  case "serialPhoto":
//                                        CoreCarbonSharedPreferences.SetSerialImageIdFromResponse(response.body().getData());
//                                        binding.serialNumberPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.serialNumberPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;*/
//
//                                    /*case "passPhoto":
//                                        CoreCarbonSharedPreferences.SetPassImageIdFromResponse(response.body().getData());
//                                        binding.passbookPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.passbookPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//
//                                        thread.stop();
//                                        break;*/
//
//                                    case "agreePhoto":
//                                        CoreCarbonSharedPreferences.SetAgreeImageIdFromResponse(response.body().getData());
//                                        binding.agreementPhotoUploadBtn.setText(R.string.uploaded);
//                                        binding.agreementPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
//                                        thread.stop();
//                                        break;
//
//                                    default:
//                                        return;
//
//                                }
//                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                thread.stop();
//                            } else {
//                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                thread.stop();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
//                            call.cancel();
//                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                            thread.stop();
//                        }
//                    });
//                }
//            }
//        };
//
//        thread.start();
//
//
//    }

    private void NoPhotoToast() {
        Toast.makeText(getApplicationContext(), "Please capture photo first!", Toast.LENGTH_SHORT).show();
    }

    private void GotoImageView(String user) {
        GlobalData.bitmap = null;
        Intent intent = new Intent(CookStoveFormActivity.this, ViewImageActivity.class);

        switch (user) {
            case "user":
                if (edit) {
                    intent.putExtra("photo", userPhoto);
                } else {
                    GlobalData.bitmap = beneficiaryPhoto_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", beneficiaryPhoto_bitmap2);
                }
//                intent.putExtra("file", destination);
                startActivity(intent);
                break;
            /*case "house":
                if (edit) {
                    intent.putExtra("photo", housePhoto);
                } else {
                    GlobalData.bitmap = housePhoto_birmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", housePhoto_birmap2);
                }
//                intent.putExtra("file", destination);
                startActivity(intent);
                break;*/
            case "id":
                if (edit) {
                    intent.putExtra("photo", aadharPhoto);
                } else {
                    GlobalData.bitmap = idimage_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", idimage_bitmap2);
                }
                startActivity(intent);
                break;
            case "tradi":
                if (edit) {
                    intent.putExtra("photo", tradiphoto);
                } else {
                    GlobalData.bitmap = traditionalstove_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", traditionalstove_bitmap2);
                }
                startActivity(intent);
                break;
            case "new":
                if (edit) {
                    intent.putExtra("photo", newtove);
                } else {
                    GlobalData.bitmap = newstove_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", newstove_bitmap2);
                }
                startActivity(intent);
                break;
            /*case "serial":
                if (edit) {
                    intent.putExtra("photo", serialnumberphoto);
                } else {
                    GlobalData.bitmap = serialnumber_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", serialnumber_bitmap2);
                }
                startActivity(intent);
                break;*/
            case "agree":
                if (edit) {
                    intent.putExtra("photo", agreement);
                } else {
                    GlobalData.bitmap = agreement_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", agreement_bitmap2);
                }
                startActivity(intent);
                break;
           /* case "pass":
                if (edit) {
                    intent.putExtra("photo", passbookphoto);
                } else {
                    GlobalData.bitmap = passbook_bitmap2;
                    intent.putExtra("bitmap", true);
//                    intent.putExtra("bitmapphoto", passbook_bitmap2);
                }
                startActivity(intent);
                break;*/
            default:
                break;
        }
    }

    private void GetData() {
        final Intent intent = getIntent();
//        housePhoto = intent.getStringExtra("housePhoto");
//        binding.housephotoViewIv.setVisibility(View.VISIBLE);
//        passbookphoto = intent.getStringExtra("passbookphoto");
//        binding.passbookphotoViewIv.setVisibility(View.VISIBLE);
//        serialnumberphoto = intent.getStringExtra("serialImage");
//        binding.serialphotoViewIv.setVisibility(View.VISIBLE);
        _id = intent.getStringExtra("_id");
        //
        //
        //BENEFICIARY DETAILS
        final String name = intent.getStringExtra("name");
        final String mobileNumber = intent.getStringExtra("mobileNumber");
        final String mobileType = intent.getStringExtra("mobileType");
        final String dateofBirth = intent.getStringExtra("dateofBirth");
        final String personCount = intent.getStringExtra("personCount");
        final String location = intent.getStringExtra("geoLocation");
        //
        userPhoto = intent.getStringExtra("userPhoto");
        tradiphoto = intent.getStringExtra("tradiphoto");
        //
        if (isEmpty(name)) {
            binding.beneficiaryNameEditText.setError("Empty");
        } else {
            binding.beneficiaryNameEditText.setText(name);
        }
        if (isEmpty(mobileNumber)) {
            binding.beneficiaryMobileEditText.setError("Empty");
        } else {
            binding.beneficiaryMobileEditText.setText(mobileNumber);
        }
        if (isEmpty(mobileType)) {
            binding.mobiletypetextview.setError("Empty");
        } else {
            binding.mobiletypetextview.setText(mobileType);
        }
        if (isEmpty(dateofBirth)) {
            binding.beneficiaryDOBTextView.setError("Empty");
        } else {
            binding.beneficiaryDOBTextView.setText(dateofBirth);
        }
        if (isEmpty(personCount)) {
            binding.beneficiaryPersonsEditText.setError("Empty");
        } else {
            binding.beneficiaryPersonsEditText.setText(personCount);
        }
        if (isEmpty(dateofBirth)) {
            binding.textInputlocation.setError("Empty");
        } else {
            binding.textInputlocation.getEditText().setText(location);
        }
        if (isEmpty(userPhoto)) {
            binding.userPhotoWarningView.setVisibility(View.VISIBLE);
            binding.userphotoViewIv.setVisibility(View.GONE);
        } else {
            binding.userphotoViewIv.setVisibility(View.VISIBLE);
            binding.userPhotoWarningView.setVisibility(View.GONE);
        }
        if (isEmpty(tradiphoto)) {
            binding.traditionalStoveWarningView.setVisibility(View.VISIBLE);
            binding.tradiphotoViewIv.setVisibility(View.GONE);
        } else {
            binding.traditionalStoveWarningView.setVisibility(View.GONE);
            binding.tradiphotoViewIv.setVisibility(View.VISIBLE);
        }
        //
        //
        //ADDRESS
        final String houseNumber = intent.getStringExtra("houseNumber");
        final String state = intent.getStringExtra("state");
        final String district = intent.getStringExtra("district");
        final String mandal = intent.getStringExtra("mandal");
        final String village = intent.getStringExtra("village");
        final String pincode = intent.getStringExtra("pincode");
        if (isEmpty(houseNumber)) {
            binding.houseDetailsEditText.setError("Empty");
        } else {
            binding.houseDetailsEditText.setText(houseNumber);
        }
        if (isEmpty(state)) {
            binding.statetextview.setError("Empty");
        } else {
            binding.statetextview.setText(state);
        }
        if (isEmpty(district)) {
            binding.districttextview.setError("Empty");
        } else {
            binding.districttextview.setText(district);
        }
        if (isEmpty(mandal)) {
            binding.mandaltextview.setError("Empty");
        } else {
            binding.mandaltextview.setText(mandal);
        }
        if (isEmpty(village)) {
            binding.villagetextview.setError("Empty");
        } else {
            binding.villagetextview.setText(village);
        }
        if (isEmpty(pincode)) {
            binding.pinCodeEditText.setError("Empty");
        } else {
            binding.pinCodeEditText.setText(pincode);
        }
        //
        //
        //ID PROOF
        final String kyc = intent.getStringExtra("kyc");
        final String aadharNumber = intent.getStringExtra("aadharNumber");
        aadharPhoto = intent.getStringExtra("aadharPhoto");
        if (isEmpty(kyc)) {
            binding.idtypetextview.setError("Empty");
        } else {
            binding.idtypetextview.setText(kyc);
        }
        if (isEmpty(aadharNumber)) {
            binding.idNumberEditText.setError("Empty");
        } else {
            binding.idNumberEditText.setText(aadharNumber);
        }
        if (isEmpty(aadharPhoto)) {
            binding.idImageWarningView.setVisibility(View.VISIBLE);
            binding.idphotoViewIv.setVisibility(View.GONE);
        } else {
            binding.idImageWarningView.setVisibility(View.GONE);
            binding.idphotoViewIv.setVisibility(View.VISIBLE);
        }
        //
        //
        //COOK STOVE
        final String dateOfDistribution = intent.getStringExtra("dateOfDistribution");
        final String serialNumber = intent.getStringExtra("serialNumber");
        final String manufacturer = intent.getStringExtra("manufacturer");
        final String typeofStove = intent.getStringExtra("typeofStove");
        newtove = intent.getStringExtra("newtove");
        agreement = intent.getStringExtra("agreement");
        //
        if (isEmpty(dateOfDistribution)) {
            binding.dateOfDistributionTextView.setError("Empty");
        } else {
            binding.dateOfDistributionTextView.setText(dateOfDistribution);
        }
        if (isEmpty(serialNumber)) {
            binding.serialNumberEditText.setError("Empty");
        } else {
            binding.serialNumberEditText.setText(serialNumber);
        }
        if (isEmpty(manufacturer)) {
            binding.manufacturertextview.setError("Empty");
        } else {
            binding.manufacturertextview.setText(manufacturer);
        }
        if (isEmpty(typeofStove)) {
            binding.typeofstovetextview.setError("Empty");
        } else {
            binding.typeofstovetextview.setText(typeofStove);
        }
        if (isEmpty(newtove)) {
            binding.newStoveWarningView.setVisibility(View.VISIBLE);
            binding.newdiphotoViewIv.setVisibility(View.GONE);
        } else {
            binding.newStoveWarningView.setVisibility(View.GONE);
            binding.newdiphotoViewIv.setVisibility(View.VISIBLE);
        }
        if (isEmpty(agreement)) {
            binding.agreementPhotoWarningView.setVisibility(View.VISIBLE);
            binding.agreementphotoViewIv.setVisibility(View.GONE);
        } else {
            binding.agreementPhotoWarningView.setVisibility(View.GONE);
            binding.agreementphotoViewIv.setVisibility(View.VISIBLE);
        }
        //
        //
        //BANK
        final String bankName = intent.getStringExtra("bankName");
        final String bankAccountNumber = intent.getStringExtra("bankAccountNumber");
        final String bankBranch = intent.getStringExtra("bankBranch");
        final String bankIfsc = intent.getStringExtra("bankIfsc");
        if (!isEmpty(bankName)) {
//            binding.bankNameEditText.setError("Empty");
//        } else {
            binding.bankNameEditText.setText(bankName);
        }
        if (!isEmpty(bankAccountNumber)) {
//            binding.bankAcNumberEditText.setError("Empty");
//        } else {
            binding.bankAcNumberEditText.setText(bankAccountNumber);
        }
        if (!isEmpty(bankBranch)) {
//            binding.bankBranchEditText.setError("Empty");
//        } else {
            binding.bankBranchEditText.setText(bankBranch);
        }
        if (!isEmpty(bankIfsc)) {
//            binding.bankIFSCEditText.setError("Empty");
//        } else {
            binding.bankIFSCEditText.setText(bankIfsc);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private void SubmitForm(String beneficiaryname, String mobilenumber, String numberofpersons, String location, String housenumber, String state, String district, String mandal, String village, String pincode, String idtype, String idnumber, String serialnumberonstove, String manufacturername, String stovetype, String bankname, String accountnumber, String ifsc, String branch, String mobiletype) {
        disableEnableButtons(false);
        isProcessing = true;
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
            isProcessing = false;
        }
        if (NetworkUtil.isOnline(this)) {
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();

            requestParams.put("name", beneficiaryname);
            requestParams.put("mobileNumber", mobilenumber);
            requestParams.put("typeOfMobile", mobiletype);
            requestParams.put("dateofBirth", binding.beneficiaryDOBTextView.getText().toString());
            requestParams.put("photo", userPhoto);
//            requestParams.put("housePhoto", housePhoto);
            requestParams.put("personCount", numberofpersons);
            requestParams.put("geoLocation", location);
            requestParams.put("district", district);
            requestParams.put("houseNumber", housenumber);
            requestParams.put("mandal", mandal);
            requestParams.put("pincode", pincode);
            requestParams.put("state", state);
            requestParams.put("village", village);
            requestParams.put("aadhar", aadharPhoto);
            requestParams.put("aadharNumber", idnumber);
            requestParams.put("agreement", agreement);
            requestParams.put("dateOfDistribution", binding.dateOfDistributionTextView.getText().toString());
            requestParams.put("kyc", idtype);
            requestParams.put("manufacturer", manufacturername);
            requestParams.put("newStoveImage", newtove);
            requestParams.put("serialNumber", serialnumberonstove);
//            requestParams.put("serialNumberImage", serialnumberphoto);
            requestParams.put("traditionalStoveImage", tradiphoto);
            requestParams.put("typeofStove", stovetype);
            requestParams.put("bankAccountNumber", accountnumber);
            requestParams.put("bankBranch", branch);
//            requestParams.put("bankDocument", passbookphoto);
            requestParams.put("bankIfsc", ifsc);
            requestParams.put("bankName", bankname);
            requestParams.put("createdBy", createdByObject);

            client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
            client.post(Constants.PRODUCTION_URL + "api/generic/add/CookStove", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String str = new String(responseBody);
                    progressDialog.dismiss();
                    disableEnableButtons(true);
                    isProcessing = false;
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getBoolean("error") == false) {

                            CoreCarbonSharedPreferences.setState(state);
                            CoreCarbonSharedPreferences.setDistrict(district);
                            CoreCarbonSharedPreferences.setMandal(mandal);
                            CoreCarbonSharedPreferences.setVillage(village);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CookStoveFormActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    disableEnableButtons(true);
                    progressDialog.dismiss();
                    isProcessing = false;
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        progressDialog.show();
        storeImageInRoomDb(beneficiaryname, mobilenumber, mobiletype, numberofpersons, location, district, housenumber, mandal, pincode, state, village, idnumber, idtype, manufacturername, serialnumberonstove, stovetype, accountnumber, branch, ifsc, createdByObject);
    }

    private void storeImageInRoomDb(String beneficiaryname, String mobilenumber, String mobiletype, String numberofpersons, String location, String district, String housenumber, String mandal, String pincode, String state, String village, String idnumber, String idtype, String manufacturername, String serialnumberonstove, String stovetype, String accountnumber, String branch, String ifsc, JSONObject createdByObject) {
        disableEnableButtons(false);
        isProcessing = true;
        AppDatabase appDatabase = AppDatabase.getInstance(CookStoveFormActivity.this.getApplicationContext());
        long totalCount = appDatabase.formDao().getOfflineRecordCount();
        if (totalCount >= 30) {
            Toast.makeText(this, "You have reached maximum offline records 30! Sync the offline records then add new one!!", Toast.LENGTH_LONG).show();
            return;
        }
        String str_beneficiaryPhoto = storeImageInDirectory(beneficiaryPhoto_bitmap2);
        byte[] beneficiaryphoto = bitmapAsByteArray(beneficiaryPhoto_bitmap2);

//            byte[] housephoto = bitmapAsByteArray(housePhoto_birmap2);
        byte[] aadharphoto = bitmapAsByteArray(idimage_bitmap2);
        String str_aadharphoto = storeImageInDirectory(idimage_bitmap2);
        byte[] agreementphoto = bitmapAsByteArray(agreement_bitmap2);
        String str_agreementphoto = storeImageInDirectory(agreement_bitmap2);
        byte[] newstovephoto = bitmapAsByteArray(newstove_bitmap2);
        String str_newstovephoto = storeImageInDirectory(newstove_bitmap2);
//            byte[] serialnumberphoto = bitmapAsByteArray(serialnumber_bitmap2);
        byte[] traditionalphoto = bitmapAsByteArray(traditionalstove_bitmap2);
        String str_traditionalphoto = storeImageInDirectory(traditionalstove_bitmap2);
//            Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.corecarbon_logo);
//            byte[] passbookphoto = bitmapAsByteArray(icon);

        Form form = new Form();
        form.name = beneficiaryname;
        form.mobileNumber = mobilenumber;
        form.typeOfMobile = mobiletype;
        form.dateofBirth = binding.beneficiaryDOBTextView.getText().toString();
        form.beneficiaryPhoto = beneficiaryphoto;
        form.beneficiaryPhotoPath = str_beneficiaryPhoto;
//            form.housePhoto = housephoto;
        form.personCount = numberofpersons;
        form.geoLocation = location;
        form.district = district;
        form.houseNumber = housenumber;
        form.mandal = mandal;
        form.pincode = pincode;
        form.state = state;
        form.village = village;
        form.aadhar = aadharphoto;
        form.aadharPath = str_aadharphoto;
        form.aadharNumber = idnumber;
        form.agreement = agreementphoto;
        form.agreementPath = str_agreementphoto;
        String date_of_distribution = binding.dateOfDistributionTextView.getText().toString();
        if (date_of_distribution != null && !date_of_distribution.isEmpty())
            form.dateOfDistribution = binding.dateOfDistributionTextView.getText().toString();
        else form.dateOfDistribution = "";
        form.kyc = idtype;
        form.manufacturer = manufacturername;
        if (newstovephoto != null) form.newStoveImage = newstovephoto;
        form.newStovePath = str_newstovephoto;
        form.serialNumber = serialnumberonstove;
//            form.serialNumberImage = serialnumberphoto;
        form.traditionalStoveImage = traditionalphoto;
        form.traditionalStoveImagePath = str_traditionalphoto;
        form.typeofStove = stovetype;
        form.bankAccountNumber = accountnumber;
        form.bankBranch = branch;
//            form.bankDocument = passbookphoto;
        form.bankIfsc = ifsc;
        form.createdBy = createdByObject.toString();
//            for (int i = 5; i < 50; i++) {
//                form.name = "TEST " + (i + 1);
//                long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
//                form.mobileNumber = String.valueOf(number);
        appDatabase.formDao().insertForm(form);
//            }
        CoreCarbonSharedPreferences.setState(state);
        CoreCarbonSharedPreferences.setDistrict(district);
        CoreCarbonSharedPreferences.setMandal(mandal);
        CoreCarbonSharedPreferences.setVillage(village);
        CoreCarbonSharedPreferences.ClearIds();
        progressDialog.dismiss();
        disableEnableButtons(true);
        Toast.makeText(getApplicationContext(), "Offline record stored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CookStoveFormActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        isProcessing = false;

    }

    private void createDummyOfflineData() {
        AppDatabase appDatabase = AppDatabase.getInstance(CookStoveFormActivity.this.getApplicationContext());
        Form form = new Form();
        form.aadharNumber = "653646434646";
        form.aadharPath = "/storage/emulated/0/Documents/Core Carbon X/1664689040054.jpg";
        form.agreementPath = "/storage/emulated/0/Documents/Core Carbon X/1664689040640.jpg";
        form.bankAccountNumber = "";
        form.bankBranch = "";
        form.bankIfsc = "";
        form.bankName = "";
        form.beneficiaryPhotoPath = "/storage/emulated/0/Documents/Core Carbon X/1664689038965.jpg";
        form.createdBy = "{\"_id\":\"6254d16f46628dcf426ed5ce\",\"status\":\"Active\",\"isMobileApp\":\"Yes\",\"cookstoveAccess\":\"Yes\",\"agroAccess\":\"Yes\",\"name\":\"Ajith\",\"mobileNumber\":\"8143389020\",\"role\":\"[\\\"Others\\\"]\",\"isAdmin\":false}";
        form.dateOfDistribution = "01-10-2022";
        form.dateofBirth = "2004";
        form.district = "Cuttack";
        form.form_id = 0;
        form.geoLocation = "16.6247103,81.7388746";
        form.houseNumber = "";
        form.isOnlineRecord = false;
        form.kyc = "AADHAR";
        form.mandal = "Banki";
        form.mobileNumber = "6535956535";
        form.manufacturer = "Swami Samarth Electronics Pvt Ltd";
        form.name = "Swami";
        form.newStovePath = "/storage/emulated/0/Documents/Core Carbon X/1664689041294.jpg";
        form.onlineFormId = "";
        form.personCount = "2";
        form.pincode = "244333";
        form.serialNumber = "244333";
        form.state = "Odisha";
        form.traditionalStoveImagePath = "/storage/emulated/0/Documents/Core Carbon X/1664689041707.jpg";
        form.typeOfMobile = "Self";
        form.typeofStove = "Agneekaa Eco Mini";
        form.village = "Village1";
        appDatabase.formDao().insertForm(form);
        Toast.makeText(getApplicationContext(), "Offline record stored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CookStoveFormActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private String storeImageInDirectory(Bitmap imageToSave) {
        String root;

        if (Build.VERSION_CODES.S_V2 == Build.VERSION.SDK_INT || Build.VERSION_CODES.S == Build.VERSION.SDK_INT || Build.VERSION_CODES.R == Build.VERSION.SDK_INT) {
            root = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()).toString();
        } else {
            root = Environment.getExternalStorageDirectory().getPath().toString();
        }

        File direct = new File(root + "/" + getString(R.string.app_name));
        if (!direct.exists()) {
            direct.mkdirs();
        }
        String fname = System.currentTimeMillis() + ".jpg";
        File file = new File(direct, fname);
        Log.d("Stored location", "" + direct + fname);

        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);

        return file.getPath();
    }

    private void UpdateRecord(String beneficiaryname, String mobilenumber, String numberofpersons, String location, String housenumber, String state, String district, String mandal, String village, String pincode, String idtype, String idnumber, String serialnumberonstove, String manufacturername, String stovetype, String bankname, String accountnumber, String ifsc, String branch, String mobiletype) {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        disableEnableButtons(false);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("_id", _id);
        requestParams.put("name", beneficiaryname);
        requestParams.put("mobileNumber", mobilenumber);
        requestParams.put("typeOfMobile", mobiletype);
        requestParams.put("dateofBirth", binding.beneficiaryDOBTextView.getText().toString());
        requestParams.put("photo", userPhoto);
//        requestParams.put("housePhoto", housePhoto);
        requestParams.put("personCount", numberofpersons);
        requestParams.put("geoLocation", location);
        requestParams.put("district", district);
        requestParams.put("houseNumber", housenumber);
        requestParams.put("mandal", mandal);
        requestParams.put("pincode", pincode);
        requestParams.put("state", state);
        requestParams.put("village", village);
        requestParams.put("aadhar", aadharPhoto);
        requestParams.put("aadharNumber", idnumber);
        requestParams.put("agreement", agreement);
        requestParams.put("dateOfDistribution", binding.dateOfDistributionTextView.getText().toString());
        requestParams.put("kyc", idtype);
        requestParams.put("manufacturer", manufacturername);
        requestParams.put("newStoveImage", newtove);
        requestParams.put("serialNumber", serialnumberonstove);
//        requestParams.put("serialNumberImage", serialnumberphoto);
        requestParams.put("traditionalStoveImage", tradiphoto);
        requestParams.put("typeofStove", stovetype);
        requestParams.put("bankAccountNumber", accountnumber);
        requestParams.put("bankBranch", branch);
//        requestParams.put("bankDocument", passbookphoto);
        requestParams.put("bankIfsc", ifsc);
        requestParams.put("bankName", bankname);


        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        client.post(Constants.PRODUCTION_URL + "api/generic/update/CookStove", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                progressDialog.dismiss();
                disableEnableButtons(true);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("error") == false) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CookStoveFormActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                disableEnableButtons(true);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAttachment(ByteArrayInputStream bytearray, String photo) {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        RequestParams requestParams = new RequestParams();
        requestParams.put("file", bytearray, "IMG_" + System.currentTimeMillis() + ".jpg");
        client.post(Constants.PRODUCTION_URL + "api/document/upload", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody == null) {
                    Toast.makeText(CookStoveFormActivity.this, "Error while uploading", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String str = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    String id = jsonObject.getString("data");
                    switch (photo) {
                        case "userPhoto":
                            CoreCarbonSharedPreferences.SetUserImageIdFromResponse(id);
                            userPhoto = id;
                            binding.userPhotoUploadBtn.setText(R.string.uploaded);
                            binding.userPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                            isBeneficiaryPhotoPending = false;
                            break;
                           /* case "housePhoto":
                                CoreCarbonSharedPreferences.SetHouseImageIdFromResponse(id);
                                housePhoto = id;
                                binding.housePhotoUploadBtn.setText(R.string.uploaded);
                                binding.housePhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                                break;*/
                        case "idPhoto":
                            CoreCarbonSharedPreferences.SetIDImageIdFromResponse(id);
                            aadharPhoto = id;
                            binding.idPhotoUploadBtn.setText(R.string.uploaded);
                            binding.idPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                            isIdImagePending = false;
                            break;
                        case "tradiPhoto":
                            CoreCarbonSharedPreferences.SetTradiImageIdFromResponse(id);
                            tradiphoto = id;
                            binding.traditionalPhotoUploadBtn.setText(R.string.uploaded);
                            binding.traditionalPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                            isTraditionalStovePending = false;
                            break;
                        case "newPhoto":
                            CoreCarbonSharedPreferences.SetNewImageIdFromResponse(id);
                            newtove = id;
                            binding.newStovePhotoUploadBtn.setText(R.string.uploaded);
                            binding.newStovePhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                            isNewStovePending = false;
                            break;
                            /*case "serialPhoto":
                                CoreCarbonSharedPreferences.SetSerialImageIdFromResponse(id);
                                serialnumberphoto = id;
                                binding.serialNumberPhotoUploadBtn.setText(R.string.uploaded);
                                binding.serialNumberPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                                break;*/

                            /*case "passPhoto":
                                CoreCarbonSharedPreferences.SetPassImageIdFromResponse(id);
                                passbookphoto = id;
                                binding.passbookPhotoUploadBtn.setText(R.string.uploaded);
                                binding.passbookPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                                break;*/
                        case "agreePhoto":
                            CoreCarbonSharedPreferences.SetAgreeImageIdFromResponse(id);
                            agreement = id;
                            binding.agreementPhotoUploadBtn.setText(R.string.uploaded);
                            binding.agreementPhotoUploadBtn.setBackgroundResource(R.drawable.curved_submit_button_bg);
                            isAgreementPending = false;
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                switch (photo) {
                    case "userPhoto":
                        binding.userPhotoUploadBtn.setText(R.string.upload);
                        break;
                    case "idPhoto":
                        binding.idPhotoUploadBtn.setText(R.string.upload);
                        break;
                    case "tradiPhoto":
                        binding.traditionalPhotoUploadBtn.setText(R.string.upload);
                        break;
                    case "newPhoto":
                        binding.newStovePhotoUploadBtn.setText(R.string.upload);
                        break;
                    case "agreePhoto":
                        binding.agreementPhotoUploadBtn.setText(R.string.upload);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}