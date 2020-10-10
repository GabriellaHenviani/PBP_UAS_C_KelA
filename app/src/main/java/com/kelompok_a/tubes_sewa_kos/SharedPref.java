package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    public boolean loadNightModeState(){
        Boolean state = sharedPreferences.getBoolean("NightMode",false);
        return state;
    }

    public void setIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }

    public boolean loadIsLogin() {
         Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
         return isLogin;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        Boolean isFirstTime = sharedPreferences.getBoolean("IsFirstTimeLaunch", true);
        return isFirstTime;
    }
}
