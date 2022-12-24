package com.alierdemalkoc.artfragment.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alierdemalkoc.artfragment.model.ArtModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ArtDao {
    @Query("SELECT name, id FROM Arts")
    Flowable<List<ArtModel>> getArtWithNameAndId();

    @Query("SELECT * FROM Arts WHERE id = :id")
    Flowable<ArtModel> getArtById(int id);

    @Insert
    Completable insert(ArtModel artModel);

    @Delete
    Completable delete(ArtModel artModel);


}
