package com.kelompok_a.tubes_sewa_kos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok_a.tubes_sewa_kos.API.BookmarkAPI;
import com.kelompok_a.tubes_sewa_kos.API.KostAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class DaftarBookmarkFragment extends Fragment {

    private ArrayList<Kos> listKos;
    private RecyclerViewAdapter adapter;
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;

    public DaftarBookmarkFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPref = new SharedPref(getActivity());

        TextView judul = view.findViewById(R.id.judulFragment);
        judul.setText(R.string.daftar_bookmark_saya);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDaftarBookmark();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        SearchView search = view.findViewById(R.id.search_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    adapter.getFilter().filter(s);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    adapter.getFilter().filter(s);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }
        });

        progressDialog = new ProgressDialog(view.getContext());
        listKos = new ArrayList<Kos>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getActivity(), listKos);
        recyclerView.setAdapter(adapter);

        getDaftarBookmark();
        return view;
    }

    public void getDaftarBookmark() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        showProgress("Menampilkan daftar bookmark");

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BookmarkAPI.URL_SELECT
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

                        int id = jsonObject.optInt("id");
                        String nama = jsonObject.optString("nama");
                        String tipe = jsonObject.optString("tipe");
                        String alamat = jsonObject.optString("alamat");
                        String foto = jsonObject.optString("foto");
                        int harga = jsonObject.optInt("harga");
                        Double longitude = jsonObject.optDouble("longitude");
                        Double latitude = jsonObject.optDouble("latitude");
                        int idUser = jsonObject.optInt("idUser");

                        //Membuat objek buku
                        Kos kos = new Kos(id, nama, tipe, alamat, harga, foto, longitude, latitude, idUser);

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
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonMessage = new JSONObject(responseBody);
                    String message = jsonMessage.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Disini bagian jika response jaringan terdapat ganguan/error
//                Toast.makeText(getContext(), error.getMessage(),
//                        Toast.LENGTH_SHORT).show();
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
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}