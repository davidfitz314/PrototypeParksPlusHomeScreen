package com.example.prototypeparksplushomescreen.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrailPointsEntity(
    val lat: Double,
    val lng: Double,
    @ColumnInfo(name = "trail_points_trail_name")
    var trail_id: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trail_points_id")
    public var trail_points_id: Int = 0
        get() = field
        set(value) {
            field = value
        }
}