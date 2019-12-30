package com.example.prototypeparksplushomescreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.example.prototypeparksplushomescreen.data.entity.TrailHead
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    val database: ZionDatabase = ZionDatabase.getInstance(application.applicationContext)
    val trailHeads: LiveData<List<Feature>>

    init {
        trailHeads = Transformations.map(database.trailHeadDao().getAllTrailHeads(), { convertTrailHeadsToFeature(it) })
    }

    private fun convertTrailHeadsToFeature(it: List<TrailHead>): List<Feature> {
        val featureList: ArrayList<Feature> = ArrayList()
        for (each in it){
            val thFeature = Feature.fromGeometry(Point.fromLngLat(each.lng, each.lat))
            thFeature.addStringProperty("name", each.name)
            thFeature.addStringProperty("image", "trailheaddefault")
            featureList.add(thFeature)
        }
        return featureList.toList()
    }
}