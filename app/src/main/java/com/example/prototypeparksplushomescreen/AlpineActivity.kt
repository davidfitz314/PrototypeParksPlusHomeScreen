package com.example.prototypeparksplushomescreen

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.JsonReader
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.prototypeparksplushomescreen.data.EntityHelpers.TrailAndTrailPointsEntityHelper
import com.example.prototypeparksplushomescreen.data.HelperDaos.TrailAndTrailPointsHelper
import com.example.prototypeparksplushomescreen.data.database.ZionDatabase
import com.example.prototypeparksplushomescreen.viewmodel.TrailViewModel
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Line
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import java.lang.Exception

class AlpineActivity : AppCompatActivity(), OnMapReadyCallback
{

	var mapboxMap: MapboxMap? = null
	var mapView: MapView? = null
	var mapTrails = MutableLiveData<ArrayList<Point>>()
	var trailNameList: ArrayList<String> = ArrayList<String>()

	val moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()
	val adapter: JsonAdapter<MyTrail> = moshi.adapter(MyTrail::class.java)

	lateinit var viewModel: TrailViewModel

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		Mapbox.getInstance(
			this,
			"pk.eyJ1IjoiZGF2aWRmaXR6MzE0IiwiYSI6ImNqeTdvZHlxYjAxa3YzbW9sZnlnZzJzMWsifQ.J1-VZUeqnR5oYH9514frYQ"
		)
		setContentView(R.layout.activity_alpine)
		mapView = findViewById(R.id.mapAlpineFragmentMapView)
		mapView?.onCreate(savedInstanceState)
		mapView?.getMapAsync(this)
		viewModel = ViewModelProvider(this).get(TrailViewModel::class.java)

//		viewModel.trailsAndPoints.observe(this, Observer {
//			it?.let {
//				if (it.size > 0) {
//					var current: String = it.get(0).trail.trailName
//					val pointsList: MutableList<Point> = mutableListOf()
//					for (each in 0..it.size - 1) {
//						current = it.get(each).trailPoints.trail_id
//						if (each+1 >= it.size - 1){
//							pointsList.add(Point.fromLngLat(it.get(each).trailPoints.lng, it.get(each).trailPoints.lat))
//							val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList))
//							viewModel.addFeatureToCollection(feature)
//							pointsList.clear()
//							break;
//						}
//						if (each < it.size - 1 && it.get(each+1).trail.trailName.equals(current)){
//							pointsList.add(Point.fromLngLat(it.get(each).trailPoints.lng, it.get(each).trailPoints.lat))
//						} else {
//							val feature = Feature.fromGeometry(LineString.fromLngLats(pointsList))
//							feature.addStringProperty("name", it.get(each).trail.trailName)
//							viewModel.addFeatureToCollection(feature)
//							pointsList.clear()
//						}
//					}
//				}
//
//			}
//		})
		this.title = "Alpine Area"
//		addTrailNamesToList()
//		for (i in trailNameList) {
//			GlobalScope.launch {
//				withContext(Dispatchers.Main){
//					try {
//						applicationContext.assets.open("alpinejson/" + i).use {
//							val reader = it.bufferedReader().use { it.readText() }
//							val item = adapter.fromJson(reader)
//							item?.let { myTrail ->
//								val feature =
//									Feature.fromGeometry(LineString.fromLngLats(myTrail.generateMapPointList()))
//								feature.addStringProperty("name", myTrail.name)
//								addFeatureToCollection(feature)
//							}
//						}
//					}catch (e: Exception){
//						e.printStackTrace()
//					}
//				}
//			}
//		}

	}

//	private fun addTrailNamesToList(){
//		trailNameList.add("anderson_valley_trail.json")
//		trailNameList.add("blake-gubler_trail_feet.json")
//		trailNameList.add("browns_point_trail_281_feet.json")
//		trailNameList.add("bull_valley_atv_trail_feet.json")
//		trailNameList.add("canal_trail_feet.json")
//		trailNameList.add("comanche_trail_feet.json")
//		trailNameList.add("gardner_peak_trail_feet.json")
//		trailNameList.add("goldstrike_fixed_feet.json")
//		trailNameList.add("grass_valley_atv_trail_feet.json")
//		trailNameList.add("oak-grove-road-ohv_feet.json")
//		trailNameList.add("oak-grove-trail-hiking_feet.json")
//		trailNameList.add("ox_valley_atv_trail_feet.json")
//		trailNameList.add("silver_rim_trail_278_feet.json")
//		trailNameList.add("stud_horse_draw_feet.json")
//		trailNameList.add("summit_trail_feet.json")
//		trailNameList.add("syler_spring_trail_271_feet.json")
//		trailNameList.add("upper_grants_ranch_trail_feet.json")
//		trailNameList.add("yant_flat-1_feet.json")
//	}

	override fun onMapReady(mapboxMap: MapboxMap)
	{
		this.mapboxMap = mapboxMap
		mapboxMap.setStyle(Style.OUTDOORS) {
			addTrailSource(it)
		}
	}

	private fun addTrailSource(@NonNull style: Style){
		viewModel.featureCollection.observe(this, Observer {
			try {
				Log.d("sourceaddedtrail", "size "+it.size)
				if (style.getSource("default-source") != null)
				{
					Log.d("sourceaddedtrail", "source removed")
					removeTrailLayer(style)
					style.removeSource("default-source")
				}

				if (style.getSource("default-source") == null)
				{
					val featureOne = it.get(0)
					Log.d("sourceaddedtrail", featureOne.toString())

					style.addSource(GeoJsonSource("default-source", FeatureCollection.fromFeatures(it)))
					addTrailLayer(style)
				}
			} catch (e: Exception){
				e.printStackTrace()
			}
		})
	}

	private fun removeTrailLayer(@NonNull style: Style)
	{
		if (style.getLayer("trail") != null)
		{
			style.removeLayer("trail")
		}
	}

	private fun addTrailLayer(style: Style){
		style.addLayer(
			LineLayer("trail", "default-source").withProperties(
				lineColor(Color.parseColor("#654321")),
				lineWidth(2f),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		)
	}

	override fun onPause()
	{
		super.onPause()
		mapView?.onPause()
	}

	override fun onStop()
	{
		super.onStop()
		mapView?.onStop()
	}

	override fun onResume()
	{
		super.onResume()
		mapView?.onResume()
	}

	override fun onDestroy()
	{
		super.onDestroy()
		mapView?.onDestroy()
	}

	override fun onStart()
	{
		super.onStart()
		mapView?.onStart()
	}

	override fun onLowMemory()
	{
		super.onLowMemory()
		mapView?.onLowMemory()
	}

	override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle)
	{
		super.onSaveInstanceState(outState, outPersistentState)
		mapView?.onSaveInstanceState(outState)
	}
}
