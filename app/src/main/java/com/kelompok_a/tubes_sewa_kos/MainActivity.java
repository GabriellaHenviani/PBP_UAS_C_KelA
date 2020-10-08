package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompok_a.tubes_sewa_kos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static ActivityMainBinding binding;
    public static boolean isLogin;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState()==true){
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);

        Fragment homeFragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, homeFragment)
                .commit();

        isLogin = false;
        loadPreference();

        changeMenu(binding.bottomNavigation);
    }

    private void loadPreference() {
        String name = "profile";
        SharedPreferences preferences;
        int mode = Activity.MODE_PRIVATE;

        preferences = getSharedPreferences(name, mode);
        if (preferences!=null) {
            if(preferences.getString("isLogin", "").equals("true")){
                isLogin = true;
            }
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Fragment homeFragment = new HomeFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, homeFragment)
                        .commit();
                break;

            case R.id.login:
                Fragment loginFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, loginFragment)
                        .commit();
                break;
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
//                Fragment settingFragment = new SettingFragment();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_layout, settingFragment)
//                        .commit();
                break;

            case R.id.profile:
                Fragment profileFragment = new ProfileFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, profileFragment)
                        .commit();
                break;

            case R.id.tambahKos:
                Fragment tambahKosFragment = new TambahKosFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, tambahKosFragment)
                        .commit();
                break;
        }
        return true;
    }

    public static void changeMenu(BottomNavigationView navigationView) {

        if(isLogin == true) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.tambahKos).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.tambahKos).setVisible(false);
        }
    }
}