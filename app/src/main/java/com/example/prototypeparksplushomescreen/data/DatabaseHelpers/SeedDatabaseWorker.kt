package com.example.prototypeparksplushomescreen.data.DatabaseHelpers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prototypeparksplushomescreen.JsonHolders.TrailAndTrailPointsJSONHolder
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
    var desertTrailNameList: ArrayList<String> = ArrayList<String>()
    var mesaTrailNameList: ArrayList<String> = ArrayList<String>()
    var urbanTrailNameList: ArrayList<String> = ArrayList<String>()
    var canyonTrailNameList: ArrayList<String> = ArrayList<String>()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val trailAdapter: JsonAdapter<TrailAndTrailPointsJSONHolder> = moshi.adapter(TrailAndTrailPointsJSONHolder::class.java)

    override suspend fun doWork(): Result = coroutineScope {
        try {
            addAlpineTrailNamesToList()
            addCanyonTrailNamesToList()
            addDesertTrailNamesToList()
            addMesaTrailNamesToList()
            addUrbanTrailNamesToList()

            val database = ZionDatabase.getInstance(context)
            val trailDao = database.trailDao()
            val trailPointsDao = database.trailPointsDao()

            //TODO update filename to be called folder name
            for (i in alpineTrailNameList){
                applicationContext.assets.open("alpinejson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = trailAdapter.fromJson(reader)
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

            for (i in canyonTrailNameList){
                applicationContext.assets.open("canyonjson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = trailAdapter.fromJson(reader)
                    item?.let {
                        val pointsList: List<TrailPointsEntity> = it.coordinates.map { coordinate ->
                            TrailPointsEntity(0, coordinate.lat, coordinate.lng, it.name, "canyonjson")
                        }
                        val trail: TrailEntity = TrailEntity(it.name, "canyonjson")
                        trailDao.insertTrail(trail)
                        trailPointsDao.insertTrailPoints(pointsList)
                    }
                }
            }

            for (i in desertTrailNameList){
                applicationContext.assets.open("desertjson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = trailAdapter.fromJson(reader)
                    item?.let {
                        val pointsList: List<TrailPointsEntity> = it.coordinates.map { coordinate ->
                            TrailPointsEntity(0, coordinate.lat, coordinate.lng, it.name, "desertjson")
                        }
                        val trail: TrailEntity = TrailEntity(it.name, "desertjson")
                        trailDao.insertTrail(trail)
                        trailPointsDao.insertTrailPoints(pointsList)
                    }
                }
            }

            for (i in mesaTrailNameList){
                applicationContext.assets.open("mesajson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = trailAdapter.fromJson(reader)
                    item?.let {
                        val pointsList: List<TrailPointsEntity> = it.coordinates.map { coordinate ->
                            TrailPointsEntity(0, coordinate.lat, coordinate.lng, it.name, "mesajson")
                        }
                        val trail: TrailEntity = TrailEntity(it.name, "mesajson")
                        trailDao.insertTrail(trail)
                        trailPointsDao.insertTrailPoints(pointsList)
                    }
                }
            }

            for (i in urbanTrailNameList){
                applicationContext.assets.open("urbanjson/" + i).use {
                    val reader = it.bufferedReader().use { it.readText() }
                    val item = trailAdapter.fromJson(reader)
                    item?.let {
                        val pointsList: List<TrailPointsEntity> = it.coordinates.map { coordinate ->
                            TrailPointsEntity(0, coordinate.lat, coordinate.lng, it.name, "urbanjson")
                        }
                        val trail: TrailEntity = TrailEntity(it.name, "urbanjson")
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

    private fun addCanyonTrailNamesToList(){
        canyonTrailNameList.add("angels_landing_trail.json")
        canyonTrailNameList.add("archeology_trail_reversed.json")
        canyonTrailNameList.add("canyon_overlook_trail_292_feet.json")
        canyonTrailNameList.add("chinle_trail_feet.json")
        canyonTrailNameList.add("east_rim_trail_feet.json")
        canyonTrailNameList.add("guacamole_trail_feet-4-reversed.json")
        canyonTrailNameList.add("hidden_canyon_trail_feet.json")
        canyonTrailNameList.add("kayenta_trail_feet.json")
        canyonTrailNameList.add("lower_emerald_pools_trail_feet.json")
        canyonTrailNameList.add("middle_emerald_pools_trail_208_feet.json")
        canyonTrailNameList.add("middle_taylor_creek_trail_243_feet.json")
        canyonTrailNameList.add("northgate_peaks_trail_feet.json")
        canyonTrailNameList.add("observation_point_trail_291_feet.json")
        canyonTrailNameList.add("parus_trail_feet.json")
        canyonTrailNameList.add("rim_trail_feet.json")
        canyonTrailNameList.add("riverside_walk_trail_259_feet.json")
        canyonTrailNameList.add("sand_bench_trail-1_feet.json")
        canyonTrailNameList.add("the_grotto_trail_241_feet.json")
        canyonTrailNameList.add("upper_emerald_pools_trail_210_feet.json")
        canyonTrailNameList.add("watchman_trail_feet.json")
        canyonTrailNameList.add("weeping_rock_trail_216_feet.json")
        canyonTrailNameList.add("west_rim_trail_feet.json")
        canyonTrailNameList.add("wildcat_canyon_trail_229_feet.json")
        canyonTrailNameList.add("zion_view_trail_308_feet.json")
    }
    private fun addDesertTrailNamesToList(){
        desertTrailNameList.add("joshua_forest_fixed_feet.json")
    }
    private fun addMesaTrailNamesToList(){
        mesaTrailNameList.add("cryptobionic_trail_feet.json")
        mesaTrailNameList.add("dead_ringer_trail_feet.json")
        mesaTrailNameList.add("eagle_crags_trail_feet.json")
        mesaTrailNameList.add("goosebumps_trail_feet.json")
        mesaTrailNameList.add("goulds_rim_trail_feet.json")
        mesaTrailNameList.add("goulds_trail_feet.json")
        mesaTrailNameList.add("hidden_canyon_trail_12_feet.json")
        mesaTrailNameList.add("jem_trail_1_feet.json")
        mesaTrailNameList.add("little_creek_feet_3.json")
        mesaTrailNameList.add("more_cowbell_trail_204_feet.json")
        mesaTrailNameList.add("north_rim_trail-1_feet.json")
        mesaTrailNameList.add("practice_trail_feet.json")
        mesaTrailNameList.add("sand_mountain_1-1_feet.json")
        mesaTrailNameList.add("south_rim_trail_feet.json")
        mesaTrailNameList.add("water_canyon_trail-reversed.json")
        mesaTrailNameList.add("whiptail_trail-1_feet.json")
        mesaTrailNameList.add("white_trail_feet.json")
        mesaTrailNameList.add("windmill_trail_feet.json")
        mesaTrailNameList.add("yellow_trail_feet.json")
    }
    private fun addUrbanTrailNamesToList(){
        urbanTrailNameList.add("babylon_arch_trail_feet.json")
        urbanTrailNameList.add("barrel_roll_trail_feet.json")
        urbanTrailNameList.add("bearclaw_poppy_trail_59_feet.json")
        urbanTrailNameList.add("beck_hill_trail_81_feet.json")
        urbanTrailNameList.add("brackens_loop_trail_feet.json")
        urbanTrailNameList.add("butterfly_trail-1_feet.json")
        urbanTrailNameList.add("chuckwalla_trail_feet.json")
        urbanTrailNameList.add("cub_scout_trail_feet.json")
        urbanTrailNameList.add("elephant_arch_trail-3_feet.json")
        urbanTrailNameList.add("gap_trail_82_feet.json")
        urbanTrailNameList.add("hidden_pinyon_trail_113_feet.json")
        urbanTrailNameList.add("jennys_canyon_trail-1_feet.json")
        urbanTrailNameList.add("johnson_canyon_trail-1_feet.json")
        urbanTrailNameList.add("lava_flow_trail-1_feet.json")
        urbanTrailNameList.add("padre_canyon_trail_feet.json")
        urbanTrailNameList.add("paradise_rim_trail_feet.json")
        urbanTrailNameList.add("petrified_dunes_trail-1_feet.json")
        urbanTrailNameList.add("pioneer_names_trail_feet.json")
        urbanTrailNameList.add("precipice_trail_feet.json")
        urbanTrailNameList.add("prospector_trail_feet.json")
        urbanTrailNameList.add("red_mountain_trail_feet.json")
        urbanTrailNameList.add("red_reef_trail-2_feet.json")
        urbanTrailNameList.add("rim_ramble_trail_feet.json")
        urbanTrailNameList.add("rim_reaper_trail_146_feet.json")
        urbanTrailNameList.add("rim_rock_trail_feet.json")
        urbanTrailNameList.add("rim_runner_trail_feet.json")
        urbanTrailNameList.add("santa_clara_river_trail_347-1_feet.json")
        urbanTrailNameList.add("scout_cave_trail-1_feet.json")
        urbanTrailNameList.add("sidewinder_trail_feet.json")
        urbanTrailNameList.add("snow_canyon_overlook-reversed.json")
        urbanTrailNameList.add("stucki_springs_trail_feet.json")
        urbanTrailNameList.add("suicidal_tendencies_trail_feet.json")
        urbanTrailNameList.add("toe_trail_feet.json")
        urbanTrailNameList.add("turtle_wall_trail_feet.json")
        urbanTrailNameList.add("virgin_river_south_trail-2_feet.json")
        urbanTrailNameList.add("vortex-reversed.json")
        urbanTrailNameList.add("west_canyon_trail-1_feet.json")
        urbanTrailNameList.add("whiptail_trail-1_feet.json")
        urbanTrailNameList.add("white_rocks_trail_fixed_feet.json")
        urbanTrailNameList.add("zen_trail_feet.json")

    }

}