package com.example.prototypeparksplushomescreen.JsonHolders

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrailAndTrailPointsJSONHolder(
    @SerializedName("name")
    val name: String,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("coordinates")
    val coordinates: List<TrailPointsJSONHolder>
)