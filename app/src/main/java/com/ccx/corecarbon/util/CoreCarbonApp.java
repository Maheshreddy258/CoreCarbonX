package com.ccx.corecarbon.util;

import android.app.Application;
import android.content.Context;

public class CoreCarbonApp extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(instance == null) {
            instance = this;
        }
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }
}
