package com.ccx.corecarbon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.models.HomeModel;
import com.ccx.corecarbon.room_util.AppDatabase;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;

/**
 * CreatedBy Vijayakumar_KA On 11-Jun-2022 05:24 PM.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.dark_green));
//        }
    }

    protected void gotoNextActivity(Class<?> nextClass, boolean finish) {
        Intent intent = new Intent(this, nextClass);
        intent.putExtra("token", CoreCarbonSharedPreferences.getToken());
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        if (finish) {
            finish();
        }
    }
}
