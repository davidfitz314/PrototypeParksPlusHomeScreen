package com.example.prototypeparksplushomescreen.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

@Database(entities = [], version = 1, exportSchema = false)
abstract class ZionDatabase : RoomDatabase() {

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