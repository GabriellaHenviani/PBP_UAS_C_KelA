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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPref sharedPref;
    private View view;
    private Button editBtn;
    private User user;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = new SharedPref(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        view = binding.getRoot();

        progressDialog = new ProgressDialog(view.getContext());

        editBtn = view.findViewById(R.id.btn_update);

        getUser();

        binding.btnLogout.setOnClickListener(new ButtonListener());
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putSerializable("user", user);
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                editProfileFragment.setArguments(data);
                loadFragment(editProfileFragment);
            }
        });
        return view;
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            logout();
        }
    }

    public void getUser() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        showProgress("Menampilkan profil");

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String nama;
                String email;
                String noHp;
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    JSONObject dataUser = response.getJSONObject("data");

                    nama = dataUser.optString("nama");
                    email = dataUser.optString("email");
                    noHp = dataUser.optString("noHp");

                    user = new User(nama, noHp, email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
                binding.setUser(user);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPref.getToken());
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void logout(){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Memproses logout");

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(GET, UserAPI.URL_LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {
                        sharedPref.setIsLogin(false);
                        sharedPref.setToken("");
                        sharedPref.setIdUser(-1);
                        MainActivity.isLogin = false;
                        MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
                        loadFragment(new HomeFragment());
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
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPref.getToken());
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