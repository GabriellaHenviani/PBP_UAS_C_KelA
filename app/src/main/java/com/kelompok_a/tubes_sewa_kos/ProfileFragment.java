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
    private SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = new SharedPref(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        binding.btnLogout.setOnClickListener(new ButtonListener());
        return view;
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sharedPref.setIsLogin(false);
            MainActivity.isLogin = false;

            MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
            Fragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, homeFragment)
                    .commit();
        }
    }
}