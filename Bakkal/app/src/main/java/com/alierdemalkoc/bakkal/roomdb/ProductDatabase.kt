package com.alierdemalkoc.bakkal.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alierdemalkoc.bakkal.model.Product

@Database(entities = arrayOf(Product::class), version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDao
}