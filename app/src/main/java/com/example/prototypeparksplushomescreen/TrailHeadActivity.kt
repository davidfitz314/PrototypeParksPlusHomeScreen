package com.example.prototypeparksplushomescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.prototypeparksplushomescreen.data.dao.TrailHeadDao
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.example.prototypeparksplushomescreen.data.entity.TrailHead
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrailHeadActivity : AppCompatActivity() {
    lateinit var trailHeadText: TextView
    lateinit var database: ZionDatabase
    var mutableTrailHead = MutableLiveData<TrailHead>()
    lateinit var trailHead: LiveData<TrailHead>
    lateinit var trailHeadDao: TrailHeadDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_head)
        trailHeadText = findViewById(R.id.trailheadninfo)
        database = ZionDatabase.getInstance(this)
        trailHeadDao = database.trailHeadDao()
        var trailheadProperties: String? = intent.getStringExtra("trail_head_info")
        if (trailheadProperties != null) {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    getSetUpTrails(trailheadProperties)
                }
            }
//            trailHeadText.text = trailheadProperties
        }
    }

    fun getSetUpTrails(name: String){
        trailHead = trailHeadDao.getTrailHeadByTrailName(name)
        trailHead.observe(this, Observer {
            trailHeadText.text = it.toString()
        })
    }
}
