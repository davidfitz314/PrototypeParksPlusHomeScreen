package com.example.prototypeparksplushomescreen.JsonHolders

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrailPointsJSONHolder(
    val lat: Double,
    val lng: Double
)