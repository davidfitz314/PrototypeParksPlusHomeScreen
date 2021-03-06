package com.example.prototypeparksplushomescreen.data.HelperDaos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.prototypeparksplushomescreen.data.EntityHelpers.TrailAndTrailPointsEntityHelper

//TODO update filename to be called folder name
@Dao
interface TrailAndTrailPointsHelper {
    @Query("SELECT TrailEntity.*, TrailPointsEntity.* FROM TrailEntity INNER JOIN TrailPointsEntity on trailName == trail_points_trail_name ORDER BY trail_points_trail_name ASC, trail_points_id ASC")
    fun getAllTrailsAndTrailPoints(): LiveData<List<TrailAndTrailPointsEntityHelper>>

    @Query("SELECT TrailEntity.*, TrailPointsEntity.* FROM TrailEntity INNER JOIN TrailPointsEntity ON trailName == :name AND trail_points_trail_name == :name")
    fun getTrailAndPointsByName(name: String): LiveData<List<TrailAndTrailPointsEntityHelper>>

    @Query("SELECT TrailEntity.*, TrailPointsEntity.* FROM TrailEntity INNER JOIN TrailPointsEntity ON trailName == trail_points_trail_name AND fileName like :fileName AND file_name like :fileName ORDER BY trail_points_trail_name ASC, trail_points_id ASC")
    fun getAllTrailsAndTrailPointsByFileName(fileName: String): LiveData<List<TrailAndTrailPointsEntityHelper>>

}