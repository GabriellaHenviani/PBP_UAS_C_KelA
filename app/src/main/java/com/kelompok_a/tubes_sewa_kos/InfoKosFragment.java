package com.kelompok_a.tubes_sewa_kos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.kelompok_a.tubes_sewa_kos.databinding.FragmentInfoKosBinding;

public class InfoKosFragment extends Fragment {

    private Kos kos;
    private FragmentInfoKosBinding binding;
    private SharedPref sharedPref;

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
        sharedPref = new SharedPref(getActivity());
        
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
                        .commit();
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
}