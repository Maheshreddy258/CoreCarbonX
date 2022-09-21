package com.ccx.corecarbon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.databinding.ActivityAgroForestryHomeBinding;

public class AgroForestryHomeActivity extends AppCompatActivity {

    ActivityAgroForestryHomeBinding binding;
    private Toolbar toolbar;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_agro_forestry_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_tv);
        title.setText("Agro Forestry");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgroForestryHomeActivity.this, FarmerRegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}