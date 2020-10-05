package com.kelompok_a.tubes_sewa_kos;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelompok_a.tubes_sewa_kos.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = binding.getRoot();

        binding.linkLogin.setOnClickListener(new TextListener());
        binding.btnRegister.setOnClickListener(new ButtonListener());
        return view;
    }

    public class TextListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Fragment loginFragment = new LoginFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, loginFragment)
                    .commit();
        }
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(validateRegister()) {
                Fragment loginFragment = new LoginFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, loginFragment)
                        .commit();
            }
            else {

            }
        }

        public boolean validateRegister() {
            String namaInput;
            String noHpInput;
            String emailInput;
            String passwordInput;

            namaInput = binding.inputNama.getText().toString();
            noHpInput = binding.inputNoHp.getText().toString();
            emailInput = binding.inputEmail.getText().toString();
            passwordInput = binding.inputPassword.getText().toString();

            if(namaInput.isEmpty()) {
                Toast.makeText(getActivity(), "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(noHpInput.isEmpty()) {
                Toast.makeText(getActivity(), "Nomor handphone tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(emailInput.isEmpty()) {
                Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(passwordInput.isEmpty()) {
                Toast.makeText(getActivity(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(noHpInput.length() < 11 || noHpInput.length() > 12) {
                Toast.makeText(getActivity(), "Nomor handphone harus 11-12 digit", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Register berhasil", Toast.LENGTH_SHORT).show();
            return true;

        }
    }
}