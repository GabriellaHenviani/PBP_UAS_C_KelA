package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
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

        createFirebaseNotification();

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

    private void createFirebaseNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Channel 1";
            CharSequence name = "Channel 1";
            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("berita")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String mag = "Successful";
                        if(!task.isSuccessful()) {
                            mag = "Failed";
                        }
                    }
                });
    }
}