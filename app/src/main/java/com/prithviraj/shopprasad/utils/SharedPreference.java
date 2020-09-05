package com.prithviraj.shopprasad.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {

    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
    }

    public void setUserId(int number) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userId", MODE_PRIVATE).edit();
        editor.putInt("userId", number);
        editor.apply();
    }

    public int getUserId() {
        SharedPreferences prefs = context.getSharedPreferences("userId", MODE_PRIVATE);
        return prefs.getInt("userId", 0);
    }

    public void setUserType(String userType) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userType", MODE_PRIVATE).edit();
        editor.putString("userType", userType);
        editor.apply();
    }

    public String getUserType() {
        SharedPreferences prefs = context.getSharedPreferences("userType", MODE_PRIVATE);
        return prefs.getString("userType", "0");
    }

    public void setName(String userName) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userName", MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public String getUserName() {
        SharedPreferences prefs = context.getSharedPreferences("userName", MODE_PRIVATE);
        return prefs.getString("userName", "");
    }

    public void setUserToken(String userToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userToken", MODE_PRIVATE).edit();
        editor.putString("userToken", userToken);
        editor.apply();
    }

    public String getUserToken() {
        SharedPreferences prefs = context.getSharedPreferences("userToken", MODE_PRIVATE);
        return prefs.getString("userToken", "");
    }


}
