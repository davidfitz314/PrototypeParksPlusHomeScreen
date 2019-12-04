package com.example.prototypeparksplushomescreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.prototypeparksplushomescreen.data.EntityHelpers.TrailAndTrailPointsEntityHelper
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.dao.TrailEntityDao
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase

class TrailViewModel(application: Application): AndroidViewModel(application){
    val database: ZionDatabase = ZionDatabase.getInstance(application.applicationContext)
    val trailDao: TrailAndTrailPointsHelper
    val trailsAndPoints: LiveData<List<TrailAndTrailPointsEntityHelper>>
    init {
        trailDao = database.TrailAndTrailPointsDao()
        trailsAndPoints = trailDao.getAllTrailsAndTrailPoints()
    }
}