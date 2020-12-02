package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private String namaInput;
    private String noHpInput;
    private String emailInput;
    private String passwordInput;

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
                doRegister(emailInput, passwordInput, namaInput, noHpInput);
            }
        }

        public boolean validateRegister() {

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
                Toast.makeText(getActivity(), "Password harus lebih dari 8 karakter", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;

        }
    }

    private void doRegister(final String strEmail, final String strPass, final String strNama, final String strNoHp){
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
                        loadFragment(new LoginFragment());
                    }
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", strEmail);
                params.put("password", strPass);
                params.put("nama", strNama);
                params.put("noHp", strNoHp);
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }

        fragmentTransaction.replace(R.id.fragment_layout, fragment)
                .detach(this)
                .attach(this)
                .commit();
    }

    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}