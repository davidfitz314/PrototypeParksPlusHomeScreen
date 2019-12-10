package com.example.prototypeparksplushomescreen.data.DatabaseHelpers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prototypeparksplushomescreen.JsonHolders.TrailAndTrailPointsJSONHolder
import com.example.prototypeparksplushomescreen.MyTrail
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.example.prototypeparksplushomescreen.data.entity.TrailEntity
import com.example.prototypeparksplushomescreen.data.entity.TrailPointsEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(val context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    private val TAG by lazy { SeedDatabaseWorker::class.java.simpleName }
    var alpineTrailNameList: ArrayList<String> = ArrayList<String>()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val alpineAdapter: JsonAdapter<TrailAndTrailPointsJSONHolder> = moshi.adapter(TrailAndTrailPointsJSONHolder::class.java)

    override suspend fun doWork(): Result = coroutineScope {
        try {
            addAlpineTrailNamesToList()
            val database = ZionDatabase.getInstance(context)
            val trailDao = database.trailDao()
            val trailPointsDao = database.trailPointsDao()

            for (i in alpineTrailNameList){
                applicationContext.assets.open("alpinejson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = alpineAdapter.fromJson(reader)
                    item?.let {
                        val pointsList: List<TrailPointsEntity> = it.coordinates.map { coordinate ->
                            TrailPointsEntity(0, coordinate.lat, coordinate.lng, it.name, "alpinejson")
                        }
                        val trail: TrailEntity = TrailEntity(it.name, "alpinejson")
                        trailDao.insertTrail(trail)
                        trailPointsDao.insertTrailPoints(pointsList)
                    }
                }
            }

        }catch (e:Exception){
            Result.failure()
        }

        Result.success()
    }

    private fun addAlpineTrailNamesToList(){
        alpineTrailNameList.add("anderson_valley_trail.json")
        alpineTrailNameList.add("blake-gubler_trail_feet.json")
        alpineTrailNameList.add("browns_point_trail_281_feet.json")
        alpineTrailNameList.add("bull_valley_atv_trail_feet.json")
        alpineTrailNameList.add("canal_trail_feet.json")
        alpineTrailNameList.add("comanche_trail_feet.json")
        alpineTrailNameList.add("gardner_peak_trail_feet.json")
        alpineTrailNameList.add("goldstrike_fixed_feet.json")
        alpineTrailNameList.add("grass_valley_atv_trail_feet.json")
        alpineTrailNameList.add("oak-grove-road-ohv_feet.json")
        alpineTrailNameList.add("oak-grove-trail-hiking_feet.json")
        alpineTrailNameList.add("ox_valley_atv_trail_feet.json")
        alpineTrailNameList.add("silver_rim_trail_278_feet.json")
        alpineTrailNameList.add("stud_horse_draw_feet.json")
        alpineTrailNameList.add("summit_trail_feet.json")
        alpineTrailNameList.add("syler_spring_trail_271_feet.json")
        alpineTrailNameList.add("upper_grants_ranch_trail_feet.json")
        alpineTrailNameList.add("yant_flat-1_feet.json")
    }
}