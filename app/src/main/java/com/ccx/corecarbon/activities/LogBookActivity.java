package com.ccx.corecarbon.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.util.DateUtil;
import com.ccx.corecarbon.databinding.ActivityLogBookBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogBookActivity extends BaseActivity {

    ActivityLogBookBinding binding;
    private Toolbar toolbar;
    private TextView title;
    private DateUtil dateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_book);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_tv);
        title.setText("Log Book");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dateUtil = new DateUtil();


        binding.textInputdate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar cldr = java.util.Calendar.getInstance();
                cldr.add(java.util.Calendar.YEAR, 0);
                int day = cldr.get(java.util.Calendar.DAY_OF_MONTH);
                int month = cldr.get(java.util.Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog datepicker = new DatePickerDialog(LogBookActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String cdate = formatter.format(dateUtil.getMilliFromDate(dayOfMonth+"/"+monthOfYear+"/"+year));
                        binding.textInputdate.getEditText().setText(cdate);

                    }
                }, cldr.get(1), month, day);
                datepicker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                datepicker.show();
            }
        });


        binding.activitytextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivityAdapter();
                binding.activityspinner.performClick();
            }
        });

        binding.activityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                binding.activitytextview.setText(stateName);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                binding.mandalspinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.activitymeasuretextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeasureAdapter();
                binding.measurespinner.performClick();
            }
        });

        binding.measurespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateName = adapterView.getItemAtPosition(i).toString();
                binding.activitymeasuretextview.setText(stateName);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                binding.mandalspinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    private void setActivityAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LogBookActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.activity_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityspinner.setAdapter(adapter);
    }


    private void setMeasureAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LogBookActivity.this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.measure_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.measurespinner.setAdapter(adapter);
    }
}