package com.example.prototypeparksplushomescreen.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.prototypeparksplushomescreen.data.entity.TrailPointsEntity

@Dao
interface TrailCoordinatesDao {
    @Query("SELECT * FROM trailpointsentity ORDER BY trail_points_trail_name ASC")
    fun getAllTrailsPoints(): LiveData<List<TrailPointsEntity>>

    @Query("SELECT * FROM trailpointsentity where trail_points_trail_name == :name")
    fun getAllTrailPointsByTrailName(name: String): LiveData<List<TrailPointsEntity>>

    @Query("SELECT * FROM trailpointsentity where trail_points_trail_name == :name ORDER BY trail_points_id ASC")
    suspend fun getAllTrailPointsList(name: String): List<TrailPointsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailPoints(trailPoints: List<TrailPointsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailPoint(trailPoint: TrailPointsEntity)

    @Update
    fun updateTrailPoint(trailPoint: TrailPointsEntity)
}