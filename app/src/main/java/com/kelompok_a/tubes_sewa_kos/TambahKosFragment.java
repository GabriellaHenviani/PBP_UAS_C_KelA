package com.kelompok_a.tubes_sewa_kos;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentTambahKosBinding;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TambahKosFragment extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private FragmentTambahKosBinding binding;

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

        populateDropdown(view);
        return view;
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
        }
        if(requestCode == 77) {
            if (resultCode == RESULT_OK) {
                double lng = data.getDoubleExtra("lng", 0);
                double lat = data.getDoubleExtra("lat", 0);
                //ambil result
                Toast.makeText(getContext(), String.valueOf(lng) + "\n" + String.valueOf(lat), Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //cancelled
            }
        }
    }
}