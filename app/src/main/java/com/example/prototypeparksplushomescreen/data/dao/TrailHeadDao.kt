package com.example.prototypeparksplushomescreen.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.prototypeparksplushomescreen.data.entity.TrailHead

@Dao
interface TrailHeadDao {
    @Query("SELECT * FROM trailhead")
    fun getAllTrailHeads(): LiveData<List<TrailHead>>

    @Query("SELECT * FROM trailhead WHERE folder == :folder")
    fun getAllTrailHeadsForFolderGroup(folder: String): LiveData<List<TrailHead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailHead(trailHead: TrailHead)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTrailHeads(trailheads: List<TrailHead>)

}