package com.example.prototypeparksplushomescreen.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrailEntity(

    val trailName: String,
    val fileName: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_trail_entity_id")
    public var id: Int = 0

}