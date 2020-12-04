package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    String namaInput, noHpInput, emailInput, passwordInput;

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

    //ke fragment login via klik link
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
                //register user pake volley RegisterUser()
                RegisterUser(namaInput, noHpInput, emailInput, passwordInput);
            }
        }

        //pengecekan input sebelum dikirim ke api
        public boolean validateRegister() {
            namaInput = binding.inputNama.getText().toString();
            noHpInput = binding.inputNoHp.getText().toString();
            emailInput = binding.inputEmail.getText().toString();
            passwordInput = binding.inputPassword.getText().toString();

            if(namaInput.isEmpty() || noHpInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() ||
                    noHpInput.length() < 11 || noHpInput.length() > 12 || !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() ||
                    passwordInput.length() < 8) {
                if(namaInput.isEmpty()) {
                    binding.inputNama.setError("Nama lengkap tidak boleh kosong");
                }
                if(noHpInput.isEmpty()) {
                    binding.inputNoHp.setError("Nomor handphone tidak boleh kosong");
                }
                if(emailInput.isEmpty()) {
                    binding.inputEmail.setError("Email tidak boleh kosong");
                }
                if(passwordInput.isEmpty()) {
                    binding.inputPassword.setError("Password tidak boleh kosong");
                }
                if(noHpInput.length() < 11 || noHpInput.length() > 12) {
                    binding.inputNoHp.setError("Nomor handphone harus 11-12 digit");
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    binding.inputEmail.setError("Email harus mengandung @");
                }
                if(passwordInput.length() < 8) {
                    binding.inputPassword.setError("Password harus minimal 8 karater");
                }
                return false;
            }
            return true;
        }
    }

    //api post untuk register
    public void RegisterUser(String nama, String noHp, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Registering User");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("Success")) {
                                //pindah ke fragment login/home / suruh verif
                                Fragment loginFragment = new LoginFragment();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_layout, loginFragment)
                                        .commit();
                            }
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), nama + noHp + email + password, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("noHp", noHp);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}