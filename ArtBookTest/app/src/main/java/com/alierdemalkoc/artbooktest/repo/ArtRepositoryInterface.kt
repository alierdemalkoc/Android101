package com.alierdemalkoc.artbooktest.repo

import androidx.lifecycle.LiveData
import com.alierdemalkoc.artbooktest.model.Art
import com.alierdemalkoc.artbooktest.model.ImageResponse
import com.alierdemalkoc.artbooktest.util.Resource

interface ArtRepositoryInterface {
    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt() : LiveData<List<Art>>

    suspend fun searchImage(imageString: String) : Resource<ImageResponse>
}