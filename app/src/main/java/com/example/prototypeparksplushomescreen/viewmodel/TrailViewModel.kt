package com.example.prototypeparksplushomescreen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.prototypeparksplushomescreen.data.EntityHelpers.TrailAndTrailPointsEntityHelper
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.dao.TrailEntityDao
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.GlobalScope
import java.lang.Exception

class TrailViewModel(application: Application) : AndroidViewModel(application) {
    val database: ZionDatabase = ZionDatabase.getInstance(application.applicationContext)
    val trailDao: TrailAndTrailPointsHelper
    val trailsAndPoints: LiveData<List<TrailAndTrailPointsEntityHelper>>
    val featureCollection: LiveData<ArrayList<Feature>>



    init {
        trailDao = database.TrailAndTrailPointsDao()
        trailsAndPoints = trailDao.getAllTrailsAndTrailPointsByFileName("alpinejson")
        featureCollection = Transformations.map(trailsAndPoints, { convertToFeatureList(it) })
    }

    fun convertToFeatureList(myTrails: List<TrailAndTrailPointsEntityHelper>): ArrayList<Feature> {
        var outlist = ArrayList<Feature>()
        try {
            val pointsList: MutableList<Point> = mutableListOf()
            for (each in 0..myTrails.size - 1) {
                var current = myTrails.get(each).trailPoints.trail_id
//                Log.d("sourceaddedtrail", "###"+myTrails.get(each).trail.fileName)
//                if (each + 1 >= myTrails.size - 1) {
//                    pointsList.add(
//                        Point.fromLngLat(
//                            myTrails.get(each).trailPoints.lng,
//                            myTrails.get(each).trailPoints.lat
//                        )
//                    )
//                    val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList))
//                    Log.d("sourceaddedtrail", feature.properties().toString())
//                    outlist.add(feature)
////                    viewModel.addFeatureToCollection(feature)
//                    pointsList.clear()
//                    break;
//                }
                if (each < myTrails.size - 1 && myTrails.get(each + 1).trail.trailName.equals(
                        current
                    )
                ) {
                    Log.d("sourceaddtrail", "if entered")
                    pointsList.add(
                        Point.fromLngLat(
                            myTrails.get(each).trailPoints.lng,
                            myTrails.get(each).trailPoints.lat
                        )
                    )

                } else {
                    Log.d("sourceaddtrail", "else entered")
                    pointsList.add(
                        Point.fromLngLat(
                            myTrails.get(each).trailPoints.lng,
                            myTrails.get(each).trailPoints.lat
                        )
                    )
                    val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList.toMutableList()))
                    feature.addStringProperty("name", myTrails.get(each).trail.trailName)
                    Log.d("sourceaddedtrail", feature.toString())
                    outlist.add(feature)
//                    viewModel.addFeatureToCollection(feature)
                    pointsList.clear()
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        Log.d("sourceaddedtrail", outlist.size.toString() + " " + myTrails.size)
        return outlist
    }

}