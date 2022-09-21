package com.ccx.corecarbon.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.ccx.corecarbon.R;
import com.ccx.corecarbon.util.Constants;
import com.ccx.corecarbon.util.CoreCarbonSharedPreferences;
import com.ccx.corecarbon.util.GlobalData;
import com.ccx.corecarbon.util.NetworkUtil;
import com.ccx.corecarbon.databinding.ActivityViewImageBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewImageActivity extends BaseActivity {

    ActivityViewImageBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScalefactot = 1.0f;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_image);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        TextView title = toolbar2.findViewById(R.id.title_tv);
        title.setText("Preview");
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        //
        if (getIntent().getBooleanExtra("bitmap", false)) {
            if (GlobalData.bitmap != null) {
                binding.imageview.setImageBitmap(GlobalData.bitmap);
                binding.loadingTv.setVisibility(View.GONE);
                binding.imageview.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
            }
        } else {
            image = getIntent().getStringExtra("photo");
            if (image != null && !image.isEmpty()) {
                DownloadImage(image);
            } else {
                Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
            }
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
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void DownloadImage(String image) {
        if (!NetworkUtil.isOnline(this)) {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.imageview.setVisibility(View.GONE);
        binding.loadingTv.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-access-token", CoreCarbonSharedPreferences.getToken());
        RequestParams params = new RequestParams();
        client.get(Constants.PRODUCTION_URL + "api/document/fetch/" + image, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("error") == false) {
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        JSONObject linkobject = dataobject.getJSONObject("file");
                        String link = linkobject.getString("data");


                        if (link != null) {
                            byte[] encodeByte = Base64.decode(link, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            binding.imageview.setImageBitmap(bitmap);
                            binding.loadingTv.setVisibility(View.GONE);
                            binding.imageview.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.loadingTv.setVisibility(View.GONE);
                        binding.imageview.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No Image found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScalefactot *= scaleGestureDetector.getScaleFactor();
            mScalefactot = Math.max(0.1f, Math.min(mScalefactot, 10.0f));
            binding.imageview.setScaleX(mScalefactot);
            binding.imageview.setScaleY(mScalefactot);
            return true;
        }
    }
}