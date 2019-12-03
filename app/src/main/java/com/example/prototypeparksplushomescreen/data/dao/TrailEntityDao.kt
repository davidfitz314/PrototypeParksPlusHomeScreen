package com.example.prototypeparksplushomescreen.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.prototypeparksplushomescreen.data.entity.TrailEntity

@Dao
interface TrailEntityDao {
    @Query("SELECT * FROM trailentity")
    fun getAllTrails(): LiveData<List<TrailEntity>>

    @Query("SELECT * FROM trailentity WHERE _trail_entity_id == :id")
    fun getTrailById(id: Int): LiveData<TrailEntity>

    @Query("SELECT * FROM trailentity WHERE trailName == :name")
    fun getTrailByName(name: String) : LiveData<TrailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTrails(trails: List<TrailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrail(trail: TrailEntity)

    @Update
    fun updateTrail(trail: TrailEntity)
}