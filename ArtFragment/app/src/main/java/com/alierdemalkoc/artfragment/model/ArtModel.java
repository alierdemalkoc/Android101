package com.alierdemalkoc.artfragment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.rxjava3.annotations.Nullable;

@Entity(tableName = "Arts")
public class ArtModel {
    @ColumnInfo(name = "name")
    public  String name;

    @PrimaryKey(autoGenerate = true)
    public  int id;

    @Nullable
    @ColumnInfo(name = "oz")
    public String ozName;

    @Nullable
    @ColumnInfo(name = "bol")
    public String bolName;

    @Nullable
    @ColumnInfo(name = "image")
    public byte[] image;

    public ArtModel(String name,@Nullable String ozName,@Nullable String bolName, @Nullable byte[] image) {
        this.name = name;
        this.ozName = ozName;
        this.bolName = bolName;
        this.image = image;
    }
}
