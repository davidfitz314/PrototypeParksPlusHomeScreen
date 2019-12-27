package com.example.prototypeparksplushomescreen.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrailHead(
    val name: String,
    val folder: String,
    val lat: Double,
    val lng: Double
) {
    @PrimaryKey(autoGenerate = true)
    
    var trailheadId: Int = 0
}