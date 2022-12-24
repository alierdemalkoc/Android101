package com.alierdemalkoc.artfragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alierdemalkoc.artfragment.databinding.RecyclerRowBinding;
import com.alierdemalkoc.artfragment.model.ArtModel;
import com.alierdemalkoc.artfragment.view.ArtFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {
    List<ArtModel> artList;

    public ArtAdapter(List<ArtModel> artList){
        this.artList = artList;
    }
    class ArtHolder extends RecyclerView.ViewHolder {

        private RecyclerRowBinding binding;

        public ArtHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;}}

    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ArtHolder(recyclerRowBinding);

    }

    @Override
    public void onBindViewHolder(ArtAdapter.ArtHolder holder,int position) {
            holder.binding.recyclerViewTextView.setText(artList.get(position).name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArtFragmentDirections.ActionArtFragmentToAddFragment action = ArtFragmentDirections.actionArtFragmentToAddFragment("old");
                    action.setArtId(artList.get(position).id);
                    action.setInfo("old");
                    Navigation.findNavController(view).navigate(action);
                }
            });
    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    }

