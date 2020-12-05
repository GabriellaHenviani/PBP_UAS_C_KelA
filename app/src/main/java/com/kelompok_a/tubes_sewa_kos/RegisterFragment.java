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

import com.android.volley.DefaultRetryPolicy;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private String namaInput, noHpInput, emailInput, passwordInput;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = binding.getRoot();

        progressDialog = new ProgressDialog(view.getContext());

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
                } else if(noHpInput.length() < 11 || noHpInput.length() > 12) {
                    binding.inputNoHp.setError("Nomor handphone harus 11-12 digit");
                }
                if(emailInput.isEmpty()) {
                    binding.inputEmail.setError("Email tidak boleh kosong");
                } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    binding.inputEmail.setError("Email harus mengandung @");
                }
                if(passwordInput.isEmpty()) {
                    binding.inputPassword.setError("Password tidak boleh kosong");
                } else if(passwordInput.length() < 8) {
                    binding.inputPassword.setError("Password harus minimal 8 karater");
                }
                return false;
            }
            return true;
        }
    }

    //api post untuk register
    /*
    public void RegisterUser(String nama, String noHp, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        showProgress("Registering...");

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
                            System.out.println(obj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonMessage = new JSONObject(responseBody);
                    String message = jsonMessage.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    System.out.println(message);
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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

     */

    private void RegisterUser(String nama, String noHp, String email, String password){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Memproses register");

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    if(obj.getString("status").equals("Success"))
                    {
                        Fragment loginFragment = new LoginFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_layout, loginFragment)
                                .commit();
                    }
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Cek email anda untuk melakukan verifikasi", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonMessage = new JSONObject(responseBody);
                    String message = jsonMessage.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}