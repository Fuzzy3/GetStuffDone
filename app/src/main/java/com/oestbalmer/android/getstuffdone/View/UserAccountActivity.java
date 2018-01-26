package com.oestbalmer.android.getstuffdone.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.oestbalmer.android.getstuffdone.R;
import com.oestbalmer.android.getstuffdone.RealmDatabase;
import com.oestbalmer.android.getstuffdone.RealmDatabaseImpl;

public class UserAccountActivity extends AppCompatActivity {

    private RealmDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mDatabase = RealmDatabaseImpl.get(this);

    }



    public static Intent newInstance(Context packageContext) {
        Intent i = new Intent(packageContext, UserAccountActivity.class);
        return i;
    }
}
