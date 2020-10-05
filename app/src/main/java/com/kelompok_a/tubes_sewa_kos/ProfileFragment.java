package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelompok_a.tubes_sewa_kos.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        binding.btnLogout.setOnClickListener(new ButtonListener());
        return view;
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity.isLogin = false;
            savePreferences(MainActivity.isLogin);
            MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
            Fragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, homeFragment)
                    .commit();
        }
    }

    public void savePreferences(boolean isLogin) {
        String name = "profile";
        SharedPreferences preferences;
        int mode = Activity.MODE_PRIVATE;

        preferences = getActivity().getSharedPreferences(name, mode);
        SharedPreferences.Editor editor = preferences.edit();
        if(isLogin) {
            editor.putString("isLogin", "true");
        }
        else {
            editor.putString("isLogin", "false");
        }
        editor.apply();
    }
}