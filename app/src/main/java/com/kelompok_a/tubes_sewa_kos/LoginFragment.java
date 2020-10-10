package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelompok_a.tubes_sewa_kos.databinding.FragmentLoginBinding;

import java.math.BigInteger;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = new SharedPref(getActivity());
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login, container, false);
        View view = binding.getRoot();

        binding.linkSignup.setOnClickListener(new TextListener());
        binding.btnLogin.setOnClickListener(new ButtonListener());
        return view;
    }

    public class TextListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Fragment registerFragment = new RegisterFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, registerFragment)
                    .commit();
        }
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(validateLogin()) {
                sharedPref.setIsLogin(true);
                MainActivity.isLogin = true;
                MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
                Fragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, homeFragment)
                        .commit();
            }
        }

        public boolean validateLogin() {
            String emailInput;
            String passwordInput;

            emailInput = binding.inputEmail.getText().toString();
            passwordInput = binding.inputPassword.getText().toString();

            if(emailInput.isEmpty()) {
                Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(passwordInput.isEmpty()) {
                Toast.makeText(getActivity(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!emailInput.contains("@")) {
                Toast.makeText(getActivity(), "Email invalid", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(passwordInput.length() < 8) {
                Toast.makeText(getActivity(), "Password invalid", Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(getActivity(), "Login berhasil", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}