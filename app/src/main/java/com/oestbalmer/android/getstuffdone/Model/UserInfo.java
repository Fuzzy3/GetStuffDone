package com.oestbalmer.android.getstuffdone.Model;


import java.util.UUID;

import io.realm.RealmModel;

public class UserInfo implements RealmModel{

    private String mId;
    private String mName;
    private int mCollectedPoints;
    public static final String USERID = "userinfo";

    public UserInfo(String name, int points) {
        mName = name;
        mCollectedPoints = points;
        mId = USERID;
    }

    public static UserInfo newUserInfo(String name) {
        return new UserInfo(name, 0);
    }

    public void addPoints(int points) {
        mCollectedPoints += points;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
