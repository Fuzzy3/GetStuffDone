package com.oestbalmer.android.getstuffdone;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Log.i("APP", "TESTTEST");
    }
}
