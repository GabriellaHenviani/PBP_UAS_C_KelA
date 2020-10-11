package com.kelompok_a.tubes_sewa_kos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.hardware.Camera;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kelompok_a.tubes_sewa_kos.databinding.ActivityMainBinding;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentLoginBinding;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentTambahKosBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.transform.Result;

public class TambahKosFragment extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private FragmentTambahKosBinding binding;
    public ArrayList<Kos> KOS;
    private Camera kCamera = null;
    private CameraView kCameraView = null;


    public TambahKosFragment() {
        KOS = new ArrayList<>();
        KOS.add(DPARAGON);
        KOS.add(OEMAH);
        KOS.add(ACASIA);
        KOS.add(EVAN);
        KOS.add(DIAMOND);
    }

    public static final Kos DPARAGON = new Kos("DPARAGON Seturan 1", "Kost Putra",
            "Jl. Kehutanan Jl. Seturan Raya No.6, Kledokan, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281",
            2000000, "https://origin.pegipegi.com/jalan/images/pict1L/Y3/Y964313/Y964313005.jpg",110.410986,
            -7.766323);

    public static final Kos OEMAH = new Kos("Oemah Kost Exclusive", "Kost Putri",
            "Jalan Gotong Royong TR II No. 275 A, Karangwaru, Tegalrejo, Karangwaru, Kec. Tegalrejo, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55241",
            1500000, "https://www.gudeg.net/cni-content/uploads/modules/direktori/logo/20161019041652.jpg", 110.36201,
            -7.771593);

    public static final Kos ACASIA = new Kos("Kost Putri Acasia", "Kost Putri",
            "Jl. Acasia Jl. Seturan Baru No.4, Kledokan, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281",
            1800000, "https://lh5.googleusercontent.com/p/AF1QipOEw4P3gV8gs1HMEQGfK5MnKL3dmovep4vQOii1=w284-h160-k-no", 110.408918,
            -7.76756);

    public static final Kos EVAN = new Kos("Kost Evan", "Kost Putra",
            "Beteng, Tridadi, Kec. Sleman, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55511",
            800000, "https://3.bp.blogspot.com/-g00KirmkaIU/U2md59uN6NI/AAAAAAAAAYw/_AbEdxDgw9w/s450-a/AmanNyamanBersih_.jpg", 110.357383,
            -7.714447);

    public static final Kos DIAMOND = new Kos("Kost Putra Diamond", "Kost Putra",
            "Perumahan APH, Blk. B No.36, Jl. Raya Kledokan, Kledokan, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281",
            1500000, "https://d99i6ad9lbm5v.cloudfront.net/uploads/ckeditor/pictures/40/content_Kost-pilihan-di-jogja-daerah-Pogung-Raya.jpg",110.405861 ,
            -7.766858);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            kCamera = Camera.open();
        }catch (Exception e){
            Log.d("Error", "Failed to get Camera" +e.getMessage());
        }
        if(kCamera !=null){
            kCameraView = new CameraView( getActivity(), kCamera);
            FrameLayout camera_view = (FrameLayout) kCameraView.findViewById(R.id.FLCamera);
           camera_view.addView(kCameraView);
        }

        ImageButton imageClose = (ImageButton) kCameraView.findViewById(R.id.imgClose);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_tambah_kos, container, false);
        View view = binding.getRoot();
        return view;
    }


}