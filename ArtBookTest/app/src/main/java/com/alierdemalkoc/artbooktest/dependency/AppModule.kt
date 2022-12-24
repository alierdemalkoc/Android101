package com.alierdemalkoc.artbooktest.dependency

import android.content.Context
import androidx.room.Room
import com.alierdemalkoc.artbooktest.R
import com.alierdemalkoc.artbooktest.RetrofitAPI
import com.alierdemalkoc.artbooktest.repo.ArtRepository
import com.alierdemalkoc.artbooktest.repo.ArtRepositoryInterface
import com.alierdemalkoc.artbooktest.roomdb.ArtDao
import com.alierdemalkoc.artbooktest.roomdb.ArtDatabase
import com.alierdemalkoc.artbooktest.util.Util.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun injectRoomDB(@ApplicationContext context: Context) = Room.databaseBuilder(context,ArtDatabase::class.java,"ArtBookDB").build()

    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()

    @Singleton
    @Provides
    fun injectAPI() : RetrofitAPI {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api: RetrofitAPI) = ArtRepository(dao,api) as ArtRepositoryInterface

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )
}