package com.example.prototypeparksplushomescreen.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prototypeparksplushomescreen.data.DatabaseHelpers.SeedDatabaseWorker
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.dao.TrailCoordinatesDao
import com.example.prototypeparksplushomescreen.data.dao.TrailEntityDao
import com.example.prototypeparksplushomescreen.data.dao.TrailHeadDao
import com.example.prototypeparksplushomescreen.data.entity.TrailEntity
import com.example.prototypeparksplushomescreen.data.entity.TrailHead
import com.example.prototypeparksplushomescreen.data.entity.TrailPointsEntity

@Database(entities = [TrailEntity::class, TrailPointsEntity::class, TrailHead::class], version = 1, exportSchema = false)
abstract class ZionDatabase : RoomDatabase() {
    abstract fun trailDao(): TrailEntityDao
    abstract fun trailPointsDao(): TrailCoordinatesDao
    abstract fun TrailAndTrailPointsDao(): TrailAndTrailPointsHelper
    abstract fun trailHeadDao(): TrailHeadDao

    companion object {
        @Volatile
        private var instance: ZionDatabase? = null

        fun getInstance(context: Context): ZionDatabase {
            return instance
                ?: synchronized(this)
                {
                    instance
                        ?: buildDatabase(
                            context
                        ).also { instance = it }
                }

        }

        private fun buildDatabase(context: Context): ZionDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ZionDatabase::class.java,
                "json_database"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}