package com.kelompok_a.tubes_sewa_kos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentInfoKosBinding;

public class InfoKosFragment extends Fragment {

    private Kos kos;
    private FragmentInfoKosBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_kos, container, false);
        View view = binding.getRoot();
        kos = (Kos) getArguments().getSerializable("kos");

        binding.setKos(kos);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
}