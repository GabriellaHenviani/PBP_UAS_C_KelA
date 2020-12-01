package com.kelompok_a.tubes_sewa_kos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.kelompok_a.tubes_sewa_kos.Model.User;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.android.volley.Request.Method.POST;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedPref sharedPref;
    private String emailInput;
    private String passwordInput;
    public static Array user;

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
                doLogin(emailInput,passwordInput);
            }
        }

        public boolean validateLogin() {

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
                Toast.makeText(getActivity(), "Email harus mengandung @", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(passwordInput.length() < 8) {
                Toast.makeText(getActivity(), "Password harus lebih dari 8 karakter", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }

    private void doLogin(final String strEmail,final String strPass){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    if(obj.getString("message").equals("Authenticated"))
                    {
                        sharedPref.setIsLogin(true);
                        MainActivity.isLogin = true;
                        MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
                        Fragment homeFragment = new HomeFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_layout, homeFragment)
                                .commit();
                        Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error : " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

//    public void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (Build.VERSION.SDK_INT >= 26) {
//            fragmentTransaction.setReorderingAllowed(false);
//        }
//        MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
//        fragmentTransaction.replace(R.id.fragment_layout, fragment)
//                .detach(this)
//                .attach(this)
//                .commit();
//    }
}