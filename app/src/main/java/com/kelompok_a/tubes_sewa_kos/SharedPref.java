package com.kelompok_a.tubes_sewa_kos;

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
        return sharedPreferences.getBoolean("NightMode",false);
    }

    public void setIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }

    public boolean loadIsLogin() {
         return sharedPreferences.getBoolean("isLogin", false);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean("IsFirstTimeLaunch", true);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setIdUser(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUser", id);
        editor.apply();
    }

    public int getIdUser() {
        return sharedPreferences.getInt("idUser", 0);
    }
}
