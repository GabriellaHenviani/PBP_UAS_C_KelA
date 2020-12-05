package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

public class ActivityUtil {
    private Context activity;
    public ActivityUtil(Activity activity) {
        this.activity = activity;
    }

    public void startMainActivity(FragmentManager fragmentManager) {
        ProfileFragment homeFragment = new ProfileFragment();

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_layout, homeFragment)
                .commit();
    }
}
