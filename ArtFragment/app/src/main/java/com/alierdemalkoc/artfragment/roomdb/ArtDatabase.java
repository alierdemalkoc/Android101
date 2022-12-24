package com.alierdemalkoc.artfragment.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alierdemalkoc.artfragment.model.ArtModel;

@Database(entities = {ArtModel.class},version = 1)
public abstract class ArtDatabase extends RoomDatabase {
    public abstract ArtDao artDao();
}
