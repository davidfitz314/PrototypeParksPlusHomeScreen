package com.example.prototypeparksplushomescreen.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO update filename to be called folder name
@Entity
data class TrailPointsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trail_points_id")
    var trail_points_id: Int = 0,
    val lat: Double,
    val lng: Double,
    @ColumnInfo(name = "trail_points_trail_name")
    var trail_id: String,
    @ColumnInfo(name = "file_name")
    var fileName: String
) {

}