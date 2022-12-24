package com.example.flowers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.flowers.databinding.ActivityArtBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class ArtActivity extends AppCompatActivity {
    private ActivityArtBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;
    SQLiteDatabase database;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher();
        database = this.openOrCreateDatabase("Flowers",MODE_PRIVATE,null);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info.equals("new")){
           // binding.nameText.setText("");
            //binding.bolText.setText("");
            //binding.ozText.setText("");
            binding.button.setVisibility(View.VISIBLE);
            binding.imageView.setImageResource(R.drawable.cicek);

        }else {
            int artId = intent.getIntExtra("artId",0);
            binding.button.setVisibility(View.INVISIBLE);
            try {
                Cursor cursor = database.rawQuery("SELECT * FROM flowers WHERE id = ?",new String[] {String.valueOf(artId)});
                int artnameIx = cursor.getColumnIndex("flowerName");
                int oznameIx = cursor.getColumnIndex("ozName");
                int bolnameIx = cursor.getColumnIndex("bolName");
                int imageIx = cursor.getColumnIndex("image");
                while (cursor.moveToNext()){
                    binding.nameText.setText(cursor.getString(artnameIx));
                    binding.ozText.setText(cursor.getString(oznameIx));
                    binding.bolText.setText(cursor.getString(bolnameIx));
                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                    binding.nameText.setFocusable(false);
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public void save(View view){
        String name = binding.nameText.getText().toString();
        String oz = binding.ozText.getText().toString();
        String bol = binding.bolText.getText().toString();
        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte [] byteArray = byteArrayOutputStream.toByteArray();

        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS flowers(id INTEGER PRIMARY KEY, flowerName VARCHAR, ozName VARCHAR, bolName VARCHAR, image BLOB)");
            String sqlString = "INSERT INTO flowers (flowerName, ozName, bolName, image) VALUES(?,?,?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,name);
            sqLiteStatement.bindString(2,oz);
            sqLiteStatement.bindString(3,bol);
            sqLiteStatement.bindBlob(4,byteArray);
            sqLiteStatement.execute();

        } catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(ArtActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width/bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height/bitmapRatio);
        }
    return image.createScaledBitmap(image,width,height,true);

    }
    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Çiçekleri koleksiyona ekleyebilmemiz için izniniz gerekiyor.",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }


    }
    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                       Uri imageData =  intentFromResult.getData();
                       //binding.imageView.setImageURI(imageData);
                        try { if (Build.VERSION.SDK_INT  >=28){
                            ImageDecoder.Source source = ImageDecoder.createSource(ArtActivity.this.getContentResolver(),imageData);
                            selectedImage = ImageDecoder.decodeBitmap(source);
                            binding.imageView.setImageBitmap(selectedImage);} else {
                            selectedImage = MediaStore.Images.Media.getBitmap(ArtActivity.this.getContentResolver(),imageData);
                            binding.imageView.setImageBitmap(selectedImage);
                        }

                        } catch (Exception e) {
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
                        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncher.launch(intentToGallery);



                    } else {
                        Toast.makeText(ArtActivity.this,"İzin Vermeniz Gerekiyor!",Toast.LENGTH_LONG).show();
                    }

                }
            });
    }
}