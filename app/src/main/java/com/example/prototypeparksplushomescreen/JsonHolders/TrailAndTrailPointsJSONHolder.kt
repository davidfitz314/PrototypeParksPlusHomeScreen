package com.example.prototypeparksplushomescreen.JsonHolders

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class TrailAndTrailPointsJSONHolder(
    @Json(name = "name")
    val name: String,
    @Json(name = "filename")
    val filename: String,
    @Json(name = "coordinates")
    val coordinates: List<TrailPointsJSONHolder>
)