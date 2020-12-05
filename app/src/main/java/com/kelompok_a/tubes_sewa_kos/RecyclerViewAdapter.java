package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok_a.tubes_sewa_kos.databinding.ItemKosBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Kos> dataList, filteredDataList;

    public RecyclerViewAdapter(){}

    public RecyclerViewAdapter(Context context, List<Kos> dataList){
        this.context = context;
        this.dataList = dataList;
        this.filteredDataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemKosBinding binding = ItemKosBinding.inflate(layoutInflater, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Kos kos = filteredDataList.get(position);
        holder.bind(kos);
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                if(charSequenceString.isEmpty()) {
                    filteredDataList = dataList;
                }
                else {
                    List<Kos> filteredList = new ArrayList<>();
                    for (Kos kos : dataList) {
                        if(kos.getNama().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(kos);
                        }
                        filteredDataList = filteredList;
                    }
                }
                FilterResults dataLists = new FilterResults();
                dataLists.values = filteredDataList;
                return dataLists;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<Kos>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ItemKosBinding binding;

        public MyViewHolder(@NonNull ItemKosBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Kos kos) {
            binding.setKos(kos);
            binding.setClickListener(this);
            binding.executePendingBindings();
        }

        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Kos kos = filteredDataList.get(getAdapterPosition());
            Bundle data = new Bundle();
            data.putSerializable("kos", kos);
            InfoKosFragment infoKosFragment = new InfoKosFragment();
            infoKosFragment.setArguments(data);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, infoKosFragment)
                    .commit();
        }
    }
}