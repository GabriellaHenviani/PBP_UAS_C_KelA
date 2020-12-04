package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok_a.tubes_sewa_kos.API.KostAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class DaftarKosFragment extends Fragment {

    private ArrayList<Kos> listKos;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;
    private View view;
    private FloatingActionButton add;

    public DaftarKosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daftar_kos, container, false);
        sharedPref = new SharedPref(getActivity());

        add = view.findViewById(R.id.fab_add);

        progressDialog = new ProgressDialog(view.getContext());
        listKos = new ArrayList<Kos>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getActivity(), listKos);
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new TambahKosFragment());
            }
        });

        getDaftarKos();
        return view;
    }

    public void getDaftarKos() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Menampilkan daftar kost");

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, KostAPI.URL_SELECT_KOST_USER
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listKos.isEmpty())
                        listKos.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String nama = jsonObject.optString("nama");
                        String tipe = jsonObject.optString("tipe");
                        String alamat = jsonObject.optString("alamat");
                        String foto = jsonObject.optString("foto");
                        int harga = jsonObject.optInt("harga");
                        Double longitude = jsonObject.optDouble("longitude");
                        Double latitude = jsonObject.optDouble("latitude");

                        //Membuat objek buku
                        Kos kos = new Kos(nama, tipe, alamat, harga, foto, longitude, latitude);

                        //Menambahkan objek user tadi ke list user
                        listKos.add(kos);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
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

    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
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
}