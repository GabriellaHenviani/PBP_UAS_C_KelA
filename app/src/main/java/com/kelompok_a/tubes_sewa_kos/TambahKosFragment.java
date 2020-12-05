package com.kelompok_a.tubes_sewa_kos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kelompok_a.tubes_sewa_kos.API.KostAPI;
import com.kelompok_a.tubes_sewa_kos.API.UserAPI;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentTambahKosBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.POST;

public class TambahKosFragment extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private FragmentTambahKosBinding binding;
    double lng = 0, lat = 0;
    String encodedImage; //string untuk upload image
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;

    private String status;
    private Kos kos;

    public TambahKosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_tambah_kos, container, false);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(view.getContext());
        sharedPref = new SharedPref(getActivity());

        binding.btnCamera.setOnClickListener(new CameraButtonListener());
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment homeFragment = new HomeFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_layout, homeFragment)
                        .commit();
            }
        });
        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapSelectActivity.class);
                intent.putExtra("lng", 0);
                intent.putExtra("lat", 0);
                startActivityForResult(intent, 77);
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add
                if (validateTambahKos()){
                    tambahKos(binding.inputNamaKos.getText().toString(), binding.tipeKostDropdown.getText().toString(),
                            binding.inputAlamatKos.getText().toString(), binding.inputHargaKos.getText().toString(),
                            lng, lat);
                }
            }

            public boolean validateTambahKos() {
                if (binding.inputNamaKos.getText().toString().equals(""))
                    return false;
                if (binding.tipeKostDropdown.getText().toString().equals(""))
                    return false;
                if (binding.inputAlamatKos.getText().toString().equals(""))
                    return false;
                if (binding.inputHargaKos.getText().toString().equals(""))
                    return false;
                return true;
            };
        });

        populateDropdown(view);

        status = getArguments().getString("status");
        if(status.equals("edit")) {
            binding.judulFragment.setText(R.string.edit_kost);
            kos = (Kos) getArguments().getSerializable("kos");
            binding.setKos(kos);
            Glide.with(view.getContext())
                    .load(KostAPI.URL_IMAGES +kos.getImgURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageView);
        }
        return view;
    }

    //TODO masih minus gambar
    public void tambahKos(String nama, String tipe, String alamat, String harga, double lng, double lat) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        showProgress("Menambahkan Kos...");



        StringRequest stringRequest = new StringRequest(POST, KostAPI.URL_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
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
                //TODO nambahin token
                params.put("Authorization", "Bearer " + sharedPref.getToken());
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("tipe", tipe);
                params.put("alamat", alamat);
                params.put("harga", harga);
                params.put("foto", encodedImage); //ubah ke bentuk jpeg
                params.put("longitude", String.valueOf(lng));
                params.put("latitude", String.valueOf(lat));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void showProgress(String title) {
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void populateDropdown(View view) {
        String[] kost = new String[] {"Putra", "Putri", "Campuran"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.tipe_kost_dropdown_menu,
                        kost);

        binding.tipeKostDropdown.setAdapter(adapter);
    }

    public class CameraButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            askCameraPermission();
        }
    }

    public void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
            else {
                Toast.makeText(getActivity(), "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            binding.imageView.setImageBitmap(image);

            image = getResizedBitmap(image, 512);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        if(requestCode == 77) {
            if (resultCode == RESULT_OK) {
                lng = data.getDoubleExtra("lng", 0);
                lat = data.getDoubleExtra("lat", 0);
                //ambil result
            }
            if (resultCode == RESULT_CANCELED) {
                //cancelled
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}