package com.alierdemalkoc.bakkal.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alierdemalkoc.bakkal.model.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAll() : Flowable<List<Product>>

    @Query("SELECT * FROM Product WHERE id = :id")
    fun getProductById(id: Int): Flowable<Product>

    @Insert
    fun insert(product : Product) : Completable

    @Delete
    fun delete(product: Product) : Completable

}