package com.alierdemalkoc.artfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alierdemalkoc.artfragment.adapter.ArtAdapter;
import com.alierdemalkoc.artfragment.model.ArtModel;
import com.alierdemalkoc.artfragment.databinding.FragmentArtBinding;
import com.alierdemalkoc.artfragment.roomdb.ArtDao;
import com.alierdemalkoc.artfragment.roomdb.ArtDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtFragment extends Fragment {
    ArtAdapter artAdapter;
    private FragmentArtBinding binding;
    ArtDatabase artDatabase;
    ArtDao artDao;
    private final CompositeDisposable mDisposable = new CompositeDisposable();




    public ArtFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artDatabase = Room.databaseBuilder(requireContext(),ArtDatabase.class,"Arts").build();
        artDao = artDatabase.artDao();

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentArtBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mDisposable.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        getData();

    }

    public void goToAdd(View view){
        //NavDirections action = ArtFragmentDirections.actionArtFragmentToAddFragment();

        //Navigation.findNavController(view).navigate(action);

    }
    public void getData(){
        mDisposable.add(artDao.getArtWithNameAndId()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(ArtFragment.this::handleResponse));

    }
    private void handleResponse(List<ArtModel> artList){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        artAdapter = new ArtAdapter(artList);
        binding.recyclerView.setAdapter(artAdapter);
    }

}