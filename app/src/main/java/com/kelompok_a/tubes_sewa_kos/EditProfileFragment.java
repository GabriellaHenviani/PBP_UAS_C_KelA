package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.PUT;

public class EditProfileFragment extends Fragment {
    private SharedPref sharedPref;
    private User user;
    private Button batal, edit;
    private TextInputEditText etNama, etEmail, etNoHp;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        sharedPref = new SharedPref(getActivity());

        progressDialog = new ProgressDialog(view.getContext());

        etEmail = view.findViewById(R.id.input_email);
        etNama = view.findViewById(R.id.input_nama);
        etNoHp = view.findViewById(R.id.input_no_hp);
        batal = view.findViewById(R.id.btn_cancel);
        edit = view.findViewById(R.id.btn_edit);

        init();

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ProfileFragment());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser();
            }
        });

        return view;
    }

    public void init()  {
        user = (User) getArguments().getSerializable("user");

        etNama.setText(user.getNama());
        etNoHp.setText(user.getNoHp());
        etEmail.setText(user.getEmail());


    }

    public void editUser(){

        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Mengedit user");

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    if(obj.getString("message").equals("Update User Success")) {
                        MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
                        loadFragment(new ProfileFragment());
                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", etNama.getText().toString());
                params.put("noHp", etNoHp.getText().toString());
//                params.put("email", email);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPref.getToken());
                params.put("Content-Type", "application/x-www-form-urlencoded");
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