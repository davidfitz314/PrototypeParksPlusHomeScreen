package com.example.prototypeparksplushomescreen.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.prototypeparksplushomescreen.data.EntityHelpers.TrailAndTrailPointsEntityHelper
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.dao.TrailCoordinatesDao
import com.example.prototypeparksplushomescreen.data.dao.TrailEntityDao
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.example.prototypeparksplushomescreen.data.entity.TrailEntity
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.runBlocking


class TrailViewModel(application: Application, folderName: String) : AndroidViewModel(application) {
    val database: ZionDatabase = ZionDatabase.getInstance(application.applicationContext)
    val trailDao: TrailAndTrailPointsHelper
    val trailPointsDao: TrailCoordinatesDao
    val trailFolderDao: TrailEntityDao
    val trailsAndPoints: LiveData<List<TrailAndTrailPointsEntityHelper>>
    val featureCollection: LiveData<ArrayList<Feature>>
    val trailsByFolder: LiveData<List<TrailEntity>>


    // Todo test using multiple db queries, query trail and then query trail points

    init {
        trailDao = database.TrailAndTrailPointsDao()
        trailPointsDao = database.trailPointsDao()

        trailsAndPoints = trailDao.getAllTrailsAndTrailPointsByFileName(folderName)
//        featureCollection = Transformations.map(trailsAndPoints, { convertToFeatureList(it) })
        trailFolderDao = database.trailDao()
        trailsByFolder = trailFolderDao.getTrailsByFolder(folderName)
        featureCollection = Transformations.map(trailsByFolder, { convertTrailsToFeature(it) })
    }

    fun convertTrailsToFeature(trailNamesList: List<TrailEntity>): ArrayList<Feature> {
        val trailFeaturesList = ArrayList<Feature>()
        for (each in trailNamesList) {
//            val check = GlobalScope.async {
                runBlocking {
                    trailFeaturesList.add(getFeature(each))
                }

//            }
        }
        return trailFeaturesList
    }

    suspend fun getFeature(each: TrailEntity): Feature {
        val currentTrailPointsList = trailPointsDao.getAllTrailPointsList(each.trailName)
        val pointList = mutableListOf<Point>()
        for (currentTrailPoint in currentTrailPointsList) {
            pointList.add(Point.fromLngLat(currentTrailPoint.lng, currentTrailPoint.lat))
        }
        val feature =
            Feature.fromGeometry(LineString.fromLngLats(pointList.toMutableList()))
        feature.addStringProperty("name", each.trailName)
        pointList.clear()
        return feature
    }

//    fun convertToFeatureList(myTrails: List<TrailAndTrailPointsEntityHelper>): ArrayList<Feature> {
//        var outlist = ArrayList<Feature>()
//        try {
//            val pointsList: MutableList<Point> = mutableListOf()
//            for (each in 0..myTrails.size - 1) {
//                var current = myTrails.get(each).trailPoints.trail_id
////                Log.d("sourceaddedtrail", "###"+myTrails.get(each).trail.fileName)
////                if (each + 1 >= myTrails.size - 1) {
////                    pointsList.add(
////                        Point.fromLngLat(
////                            myTrails.get(each).trailPoints.lng,
////                            myTrails.get(each).trailPoints.lat
////                        )
////                    )
////                    val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList))
////                    Log.d("sourceaddedtrail", feature.properties().toString())
////                    outlist.add(feature)
//////                    viewModel.addFeatureToCollection(feature)
////                    pointsList.clear()
////                    break;
////                }
//                if (each < myTrails.size - 1 && myTrails.get(each + 1).trail.trailName.equals(
//                        current
//                    )
//                ) {
//                    Log.d("sourceaddtrail", "if entered")
//                    pointsList.add(
//                        Point.fromLngLat(
//                            myTrails.get(each).trailPoints.lng,
//                            myTrails.get(each).trailPoints.lat
//                        )
//                    )
//
//                } else {
//                    Log.d("sourceaddtrail", "else entered")
//                    pointsList.add(
//                        Point.fromLngLat(
//                            myTrails.get(each).trailPoints.lng,
//                            myTrails.get(each).trailPoints.lat
//                        )
//                    )
//                    val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList.toMutableList()))
//                    feature.addStringProperty("name", myTrails.get(each).trail.trailName)
//                    Log.d("sourceaddedtrail", feature.toString())
//                    outlist.add(feature)
////                    viewModel.addFeatureToCollection(feature)
//                    pointsList.clear()
//                }
//            }
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
//        Log.d("sourceaddedtrail", outlist.size.toString() + " " + myTrails.size)
//        return outlist
//    }

}

class MyViewModelFactory(private val mApplication: Application, private val mParam: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrailViewModel(mApplication, mParam) as T
    }

}