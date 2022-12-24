package com.alierdemalkoc.bakkal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Product (

    @ColumnInfo(name = "groceryName")
    var name : String,

    @ColumnInfo(name = "stock")
    var stock : Int,

    @ColumnInfo(name = "image")
    var image : ByteArray?

    ) : Serializable {
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0

}