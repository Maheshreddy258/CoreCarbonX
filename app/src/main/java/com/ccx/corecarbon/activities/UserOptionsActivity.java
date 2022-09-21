package com.ccx.corecarbon.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.databinding.ActivityUserOptionsBinding;

public class UserOptionsActivity extends BaseActivity {

    ActivityUserOptionsBinding binding;
    private Toolbar toolbar;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_options);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_tv);
        title.setText("Select Your Option");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.cookstoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserOptionsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        binding.agroforestryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserOptionsActivity.this, AgroForestryHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}