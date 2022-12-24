package com.alierdemalkoc.waif.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Waif (
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "comment")
    var comment: String,

    @ColumnInfo(name = "prize")
    var prize: Int,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}