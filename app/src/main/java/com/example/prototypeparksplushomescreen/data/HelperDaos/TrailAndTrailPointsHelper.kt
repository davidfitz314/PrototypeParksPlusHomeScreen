package com.example.prototypeparksplushomescreen.data.HelperDaos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TrailAndTrailPointsHelper {
    @Query("SELECT TrailEntity.*, TrailPointsEntity.* FROM TrailEntity INNER JOIN TrailPointsEntity on trailName == trail_points_trail_name")
    fun getAllTrailsAndTrailPoints(): LiveData<List<TrailAndTrailPointsHelper>>

    @Query("SELECT TrailEntity.*, TrailPointsEntity.* FROM TrailEntity INNER JOIN TrailPointsEntity ON trailName == :name AND trail_points_trail_name == :name")
    fun getTrailAndPointsByName(name: String): LiveData<List<TrailAndTrailPointsHelper>>
}