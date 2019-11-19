package com.example.prototypeparksplushomescreen

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrailPoints(val lat: Double, val lng: Double, val elevation: Double) {
}