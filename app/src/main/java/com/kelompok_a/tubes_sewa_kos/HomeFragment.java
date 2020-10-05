package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelompok_a.tubes_sewa_kos.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Kos> listKos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);

        listKos = new ArrayList<>();
        buatListKos();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), listKos);
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(new RefreshListener());

        return view;
    }

    public void buatListKos() {
        Kos kos1 = new Kos("Kos ABC", "Putri", "Jl Abc no.90", 800000);
        Kos kos2 = new Kos("Kos Damai", "Campuran", "Jl Damai no.12", 500000);
        Kos kos3 = new Kos("Kos Surya", "Putra", "Jl Surya no.55", 600000);
        listKos.add(kos1);
        listKos.add(kos2);
        listKos.add(kos3);
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            // TODO: tambah fungsi load data

            binding.swipeRefresh.setRefreshing(false);
        }
    }
}