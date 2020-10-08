package com.kelompok_a.tubes_sewa_kos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kelompok_a.tubes_sewa_kos.databinding.ActivityMainBinding;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentInfoKosBinding;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class InfoKosFragment extends Fragment {

    Kos kos;
    private FragmentInfoKosBinding binding;
    Button back_btn, map_btn;
    TextView text_nama_kos, text_tipe_kos, text_alamat_kos, text_harga_kos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_info_kos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_kos, container, false);
        View view = binding.getRoot();
        kos = (Kos) getArguments().getSerializable("kos");

        back_btn = view.findViewById(R.id.back_btn);
        map_btn = view.findViewById(R.id.map_btn);
        text_nama_kos = view.findViewById(R.id.text_nama_kos);
        text_tipe_kos = view.findViewById(R.id.text_tipe_kos);
        text_alamat_kos = view.findViewById(R.id.text_alamat_kos);
        text_harga_kos = view.findViewById(R.id.text_harga_kos);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_nama_kos.setText(kos.getNama());
        text_tipe_kos.setText(kos.getTipe());
        text_alamat_kos.setText(kos.getAlamat());
        text_harga_kos.setText(String.valueOf(kos.getHarga()));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                back ke main activity (lebih lambat tapi muncul bottom nav)
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

//                bawah kembali ke homefragment tapi tanpa bottom nav
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
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
    }
}