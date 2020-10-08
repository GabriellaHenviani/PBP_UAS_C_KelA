package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok_a.tubes_sewa_kos.databinding.ItemKosBinding;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<Kos> result;

    public RecyclerViewAdapter(){}

    public RecyclerViewAdapter(Context context, List<Kos> result){
        this.context = context;
        this.result = result;
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

        Kos kos = result.get(position);
        holder.bind(kos);
    }

    @Override
    public int getItemCount() {
        return result.size();
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
            //Toast.makeText(view.getContext(), "You touch me?", Toast.LENGTH_SHORT).show();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Kos kos = result.get(getAdapterPosition());
            Bundle data = new Bundle();
            data.putSerializable("kos", kos);
            InfoKosFragment infoKosFragment = new InfoKosFragment();
            infoKosFragment.setArguments(data);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, infoKosFragment)
                    .commit();
        }
    }
}