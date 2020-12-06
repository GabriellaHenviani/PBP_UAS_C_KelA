package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.kelompok_a.tubes_sewa_kos.API.KostAPI;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentInfoKosBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class InfoKosFragment extends Fragment {

    private Kos kos;
    private String nama, noHp;
    private FragmentInfoKosBinding binding;
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_kos, container, false);
        View view = binding.getRoot();
        sharedPref = new SharedPref(getActivity());
        progressDialog = new ProgressDialog(view.getContext());

        nama = "";
        noHp = "";

        kos = (Kos) getArguments().getSerializable("kos");

        binding.setKos(kos);
        getOwner();


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                back ke main activity (lebih lambat tapi muncul bottom nav)
                //Intent intent = new Intent(getContext(), MainActivity.class);
                //startActivity(intent);
                getFragmentManager().popBackStack();

//                bawah kembali ke homefragment tapi tanpa bottom nav
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });

        if(kos.getLongitude() == 0 || kos.getLatitude() == 0) {
            binding.mapBtn.setEnabled(false);
        } else
            binding.mapBtn.setEnabled(true);

        binding.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lng", kos.getLongitude());
                bundle.putDouble("lat", kos.getLatitude());
                intent.putExtra("coord", bundle);
                startActivity(intent);
            }
        });
        binding.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle data = new Bundle();
                data.putSerializable("kos", kos);
                data.putString("status", "edit");
                TambahKosFragment tambahKosFragment = new TambahKosFragment();
                tambahKosFragment.setArguments(data);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, tambahKosFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.hapusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Yakin ingin menghapus kos ini?");
                dialog.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete function here
                        deleteKos();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        if (kos.getIdUser() != sharedPref.getIdUser()) {
            binding.editBtn.setVisibility(View.GONE);
            binding.hapusBtn.setVisibility(View.GONE);
        } else {
            binding.editBtn.setVisibility(View.VISIBLE);
            binding.hapusBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void deleteKos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        showProgress("Menghapus Kost");

        StringRequest stringRequest = new StringRequest(DELETE, KostAPI.URL_DELETE + kos.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("Success")) {
                                //pindah ke fragment login/home / suruh verif
                                Fragment daftarKosFragment = new DaftarKosFragment();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_layout, daftarKosFragment)
                                        .commit();
                            }
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
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
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPref.getToken());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getOwner() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Menampilkan data kost");

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_OWNER + kos.getIdUser()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    if(response.optString("status").equalsIgnoreCase("Success")) {
                        JSONObject dataUser = response.getJSONObject("data");

                        nama = dataUser.optString("nama");
                        noHp = dataUser.optString("noHp");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.textNamaNoHp.setText(nama + " - " + noHp);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(),
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

    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}