package com.alierdemalkoc.artbooktest.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alierdemalkoc.artbooktest.model.Art

@Dao
interface ArtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)

    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>
}