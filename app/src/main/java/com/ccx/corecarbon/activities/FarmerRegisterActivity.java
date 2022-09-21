package com.ccx.corecarbon.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.DateUtil;
import com.ccx.corecarbon.databinding.ActivityFarmerRegisterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FarmerRegisterActivity extends BaseActivity {

    ActivityFarmerRegisterBinding binding;
    private Toolbar toolbar;
    private TextView title;
    private DateUtil dateUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_farmer_register);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_tv);
        title.setText("Farmer Registeration");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dateUtil = new DateUtil();


        binding.textInputstartdate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar cldr = java.util.Calendar.getInstance();
                cldr.add(java.util.Calendar.YEAR, 0);
                int day = cldr.get(java.util.Calendar.DAY_OF_MONTH);
                int month = cldr.get(java.util.Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog datepicker = new DatePickerDialog(FarmerRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String cdate = formatter.format(dateUtil.getMilliFromDate(dayOfMonth+"/"+monthOfYear+"/"+year));
                        binding.textInputstartdate.getEditText().setText(cdate);

                    }
                }, cldr.get(1), month, day);
                datepicker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                datepicker.show();
            }
        });


        binding.seasontextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeasonAdapter();
                binding.seasonspinner.performClick();
            }
        });


        binding.seasonspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                binding.seasontextview.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.farmingtypetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFarmingAdapter();
                binding.farmingtypespinner.performClick();
            }
        });

        binding.farmingtypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                binding.farmingtypetextview.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        binding.statetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetStates();
                binding.statespinner.performClick();
            }
        });


        binding.statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
//                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
                binding.statetextview.setText(stateName);
//                binding.statespinner.setSelection(i);
//                if(!stateName.contains("Select State")){
//                    GEtDistricts(stateName);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.districttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GEtDistricts(binding.statetextview.getText().toString());
                binding.districtspinner.performClick();
            }
        });

        binding.districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                binding.districttextview.setText(stateName);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                binding.statespinner.setSelection(i);
//                if(!stateName.contains("Select District")){
//                    GetVillages(stateName);
//                    GetMandals(stateName);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.mandaltextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMandals(binding.districttextview.getText().toString());
                binding.mandalspinner.performClick();
            }
        });

        binding.mandalspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                binding.mandaltextview.setText(stateName);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                binding.mandalspinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.villagetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetVillages(binding.districttextview.getText().toString());
                binding.villagespinner.performClick();
            }
        });

        binding.villagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                binding.villagetextview.setText(stateName);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                binding.villagespinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void GetStates() {

        String statesString = CoreCarbonSharedPreferences.getStates();
        List<String> states = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(statesString);
            for(int i=0 ; i < jsonArray.length(); i ++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                states.add(jsonObject1.getString("stateName"));
            }

            states.add(0,"Select State");
            String[] str = states.toArray(new String[states.size()]);

            Log.i("STATES", str.toString());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.simple_spinner_item, str);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.statespinner.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void GEtDistricts(String state) {


        String districtString = CoreCarbonSharedPreferences.getDistricts();
        List<String> dist = new ArrayList<>();
        dist.clear();

        try {
            JSONArray jsonArray = new JSONArray(districtString);
            for(int i=0 ; i < jsonArray.length();i++){
//                        for(int j = 0; j < districtData.size(); j++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject stateIdObject = jsonObject.getJSONObject("stateId");
                String stateName = stateIdObject.getString("stateName");
                if(stateName.contentEquals(state)){
                    String distr = jsonObject.getString("districtName");
                    dist.add(distr);
                }

            }
            dist.add(0,"Select District");
            String[] strrr = dist.toArray(new String[dist.size()]);
            Log.i("DISTRICTS", strrr.toString());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.simple_spinner_item, strrr);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.districtspinner.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GetMandals(String district) {
        String mandalString = CoreCarbonSharedPreferences.getMandals();
        List<String> village = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(mandalString);
            for(int i=0 ; i < jsonArray.length();i++){
//                        for(int j = 0; j < districtData.size(); j++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject stateIdObject = jsonObject.getJSONObject("districtId");
                String districtName = stateIdObject.getString("districtName");
                if(districtName.contentEquals(district)){
                    String distr = jsonObject.getString("mandalName");
                    village.add(distr);
                }

            }
            village.add(0,"Select Mandal");
            String[] strrr = village.toArray(new String[village.size()]);
            Log.i("DISTRICTS", strrr.toString());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.simple_spinner_item, strrr);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.mandalspinner.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GetVillages(String district) {

        String mandalString = CoreCarbonSharedPreferences.getVillages();
        List<String> village = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(mandalString);
            for(int i=0 ; i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject stateIdObject = jsonObject.getJSONObject("mandalId");
                JSONObject districtObject = stateIdObject.getJSONObject("districtId");
                String districtName = districtObject.getString("districtName");
                if(districtName.contentEquals(district)){
                    String distr = jsonObject.getString("villageName");
                    village.add(distr);
                }

            }
            village.add(0,"Select Village");
            String[] strrr = village.toArray(new String[village.size()]);
            Log.i("DISTRICTS", strrr.toString());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.simple_spinner_item, strrr);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.villagespinner.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setSeasonAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.season_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.seasonspinner.setAdapter(adapter);
    }


    private void setFarmingAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FarmerRegisterActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.type_of_farming_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.farmingtypespinner.setAdapter(adapter);
    }

}