package com.kelompok_a.tubes_sewa_kos;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.kelompok_a.tubes_sewa_kos.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Kos> listKos;
    private RecyclerViewAdapter adapter;

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

        adapter = new RecyclerViewAdapter(getActivity(), listKos);
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(new RefreshListener());
        binding.searchView.setOnQueryTextListener(new SearchListener());

        return view;
    }

    public void buatListKos() {
        Kos kos1 = new Kos("Kos ABC", "Putri", "Jl Abc no.90", 800000, "https://cdn.styleblueprint.com/wp-content/uploads/2015/12/SB-ATL-ZookHome-9-e1538165814448.jpg", 110.367432, -7.783030);
        Kos kos2 = new Kos("Kos Damai", "Campuran", "Jl Damai no.12", 500000, "", 110.367695,-7.780134);
        Kos kos3 = new Kos("Kos Surya", "Putra", "Jl Surya no.55", 600000, "", 110.363089, -7.783149);
        listKos.add(kos1);
        listKos.add(kos2);
        listKos.add(kos3);
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    public class SearchListener implements SearchView.OnQueryTextListener {
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
    }
}