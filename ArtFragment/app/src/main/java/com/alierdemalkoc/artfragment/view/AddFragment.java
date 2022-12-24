package com.alierdemalkoc.artfragment.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alierdemalkoc.artfragment.R;
import com.alierdemalkoc.artfragment.databinding.FragmentAddBinding;
import com.alierdemalkoc.artfragment.model.ArtModel;
import com.alierdemalkoc.artfragment.roomdb.ArtDao;
import com.alierdemalkoc.artfragment.roomdb.ArtDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;



public class AddFragment extends Fragment {
    private FragmentAddBinding binding;
    SQLiteDatabase database;
    Bitmap selectedImage;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    ArtDatabase artDatabase;
    ArtDao artDao;
    ArtModel artFromMain;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    String info = "";


    public AddFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLauncher();

        artDatabase = Room.databaseBuilder(requireContext(),
                ArtDatabase.class, "Arts")
                .build();

        artDao = artDatabase.artDao();

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE,null);

        if(getArguments() != null) {
            info = AddFragmentArgs.fromBundle(getArguments()).getInfo();
        } else {
            info = "new";
        }


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }
        });


        if (info.equals("new")) {
            binding.nameText.setText("");
            binding.bolText.setText("");
            binding.ozText.setText("");
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.GONE);

            binding.imageView.setImageResource(R.drawable.selectimage);

        } else {
            int artId = AddFragmentArgs.fromBundle(getArguments()).getArtId();
            binding.saveButton.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.VISIBLE);

            mDisposable.add(artDao.getArtById(artId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(AddFragment.this::handleResponseWithOldArt));
        }


    }

    public void save(View view){
        String name = binding.nameText.getText().toString();
        String ozName = binding.ozText.getText().toString();
        String bolName = binding.bolText.getText().toString();

        Bitmap smallImage = makeSmallerImage(selectedImage,300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        ArtModel artModel = new ArtModel(name,ozName,bolName,byteArray);
        mDisposable.add(artDao.insert(artModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AddFragment.this::handleResponse));

    }
    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }
    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
    public void delete(View view) {
        mDisposable.add(artDao.delete(artFromMain)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AddFragment.this::handleResponse));}
    private void handleResponse() {
        NavDirections action = AddFragmentDirections.actionAddFragmentToArtFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }
    private void handleResponseWithOldArt(ArtModel art) {
        artFromMain = art;
        binding.nameText.setText(art.name);
        binding.bolText.setText(art.bolName);
        binding.ozText.setText(art.ozName);

        Bitmap bitmap = BitmapFactory.decodeByteArray(art.image,0,art.image.length);
        binding.imageView.setImageBitmap(bitmap);}

    public void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null){
                        Uri imageData = intentFromResult.getData();
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(),imageData);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);

                            } else {
                                selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);

                } else {
                    Toast.makeText(requireActivity(),"Permission Needed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        mDisposable.clear();
    }
}