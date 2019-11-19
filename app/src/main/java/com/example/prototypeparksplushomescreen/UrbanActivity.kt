package com.example.prototypeparksplushomescreen

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.JsonReader
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UrbanActivity : AppCompatActivity(), OnMapReadyCallback
{
	var mapboxMap: MapboxMap? = null
	var mapView: MapView? = null
	var featureCollection = MutableLiveData<ArrayList<Feature>>()
	var trailNameList: ArrayList<String> = ArrayList<String>()

	val moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()
	val adapter: JsonAdapter<MyTrail> = moshi.adapter(MyTrail::class.java)

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		Mapbox.getInstance(
			this,
			"pk.eyJ1IjoiZGF2aWRmaXR6MzE0IiwiYSI6ImNqeTdvZHlxYjAxa3YzbW9sZnlnZzJzMWsifQ.J1-VZUeqnR5oYH9514frYQ"
		)
		setContentView(R.layout.activity_urban)
		mapView = findViewById(R.id.mapUrbanFragmentMapView)
		mapView?.onCreate(savedInstanceState)
		mapView?.getMapAsync(this)
		this.title = "Urban Area"
		addTrailNamesToList()
		for (i in trailNameList) {
			GlobalScope.launch {
				withContext(Dispatchers.Main){
					try {
						applicationContext.assets.open("urbanjson/" + i).use {
							val reader = it.bufferedReader().use { it.readText() }
							val item = adapter.fromJson(reader)
							item?.let {myTrail ->
								val feature = Feature.fromGeometry(LineString.fromLngLats(myTrail.generateMapPointList()))
								feature.addStringProperty("name", myTrail.name)
								addFeatureToCollection(feature)
							}
//							JsonReader(it.reader()).use { reader ->
//								val myFeature = JsonTrailHandler().readJson(reader)
//								addFeatureToCollection(myFeature)
//							}
						}
					} catch (e: Exception){
						e.printStackTrace()
					}
				}
			}
		}
	}

	private fun addTrailNamesToList(){
		trailNameList.add("babylon_arch_trail_feet.json")
		trailNameList.add("barrel_roll_trail_feet.json")
		trailNameList.add("bearclaw_poppy_trail_59_feet.json")
		trailNameList.add("beck_hill_trail_81_feet.json")
		trailNameList.add("brackens_loop_trail_feet.json")
		trailNameList.add("butterfly_trail-1_feet.json")
		trailNameList.add("chuckwalla_trail_feet.json")
		trailNameList.add("cub_scout_trail_feet.json")
		trailNameList.add("elephant_arch_trail-3_feet.json")
		trailNameList.add("gap_trail_82_feet.json")
		trailNameList.add("hidden_pinyon_trail_113_feet.json")
		trailNameList.add("jennys_canyon_trail-1_feet.json")
		trailNameList.add("johnson_canyon_trail-1_feet.json")
		trailNameList.add("lava_flow_trail-1_feet.json")
		trailNameList.add("padre_canyon_trail_feet.json")
		trailNameList.add("paradise_rim_trail_feet.json")
		trailNameList.add("petrified_dunes_trail-1_feet.json")
		trailNameList.add("pioneer_names_trail_feet.json")
		trailNameList.add("precipice_trail_feet.json")
		trailNameList.add("prospector_trail_feet.json")
		trailNameList.add("red_mountain_trail_feet.json")
		trailNameList.add("red_reef_trail-2_feet.json")
		trailNameList.add("rim_ramble_trail_feet.json")
		trailNameList.add("rim_reaper_trail_146_feet.json")
		trailNameList.add("rim_rock_trail_feet.json")
		trailNameList.add("rim_runner_trail_feet.json")
		trailNameList.add("santa_clara_river_trail_347-1_feet.json")
		trailNameList.add("scout_cave_trail-1_feet.json")
		trailNameList.add("sidewinder_trail_feet.json")
		trailNameList.add("snow_canyon_overlook-reversed.json")
		trailNameList.add("stucki_springs_trail_feet.json")
		trailNameList.add("suicidal_tendencies_trail_feet.json")
		trailNameList.add("toe_trail_feet.json")
		trailNameList.add("turtle_wall_trail_feet.json")
		trailNameList.add("virgin_river_south_trail-2_feet.json")
		trailNameList.add("vortex-reversed.json")
		trailNameList.add("west_canyon_trail-1_feet.json")
		trailNameList.add("whiptail_trail-1_feet.json")
		trailNameList.add("white_rocks_trail_fixed_feet.json")
		trailNameList.add("zen_trail_feet.json")

	}

	override fun onMapReady(mapboxMap: MapboxMap)
	{
		this.mapboxMap = mapboxMap
		mapboxMap.setStyle(Style.OUTDOORS) {
			addTrailSource(it)
		}
	}

	private fun addFeatureToCollection(feature: Feature?){
		feature?.let { localfeature ->
			if (featureCollection.value == null){
				featureCollection.value = ArrayList<Feature>()
			}
			featureCollection.value?.add(localfeature)
			return
		}
	}

	private fun addTrailSource(style: Style){
		featureCollection.observe(this, Observer {
			if (it != null){
				if (style.getSource("default-source") != null)
				{
					removeTrailLayer(style)
					style.removeSource("default-source")
				}

				if (style.getSource("default-source") == null)
				{
					style.addSource(GeoJsonSource("default-source", FeatureCollection.fromFeatures(it)))
					addTrailLayer(style)
				}
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
				PropertyFactory.lineColor(Color.parseColor("#654321")),
				PropertyFactory.lineWidth(2f),
				PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
				PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
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
