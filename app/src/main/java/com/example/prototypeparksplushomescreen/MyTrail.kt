package com.example.prototypeparksplushomescreen

import com.mapbox.geojson.Point
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MyTrail(val name: String, val filename: String, val coordinates: List<TrailPoints>) {
    val pointList: MutableList<Point> = mutableListOf<Point>()

    fun generateMapPointList(): MutableList<Point> {
        for (i in coordinates){
            pointList.add(Point.fromLngLat(i.lng, i.lat))
        }
        return pointList
    }
}