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

class CanyonActivity : AppCompatActivity(), OnMapReadyCallback
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
		setContentView(R.layout.activity_canyon)
		mapView = findViewById(R.id.mapCanyonFragmentMapView)
		mapView?.onCreate(savedInstanceState)
		mapView?.getMapAsync(this)
		this.title = "Canyon Area"
		addTrailNamesToList()
		for (i in trailNameList) {
			GlobalScope.launch {
				withContext(Dispatchers.Main){
					try {
						applicationContext.assets.open("canyonjson/" + i).use {
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
		trailNameList.add("angels_landing_trail.json")
		trailNameList.add("archeology_trail_reversed.json")
		trailNameList.add("canyon_overlook_trail_292_feet.json")
		trailNameList.add("chinle_trail_feet.json")
		trailNameList.add("east_rim_trail_feet.json")
		trailNameList.add("guacamole_trail_feet-4-reversed.json")
		trailNameList.add("hidden_canyon_trail_feet.json")
		trailNameList.add("kayenta_trail_feet.json")
		trailNameList.add("lower_emerald_pools_trail_feet.json")
		trailNameList.add("middle_emerald_pools_trail_208_feet.json")
		trailNameList.add("middle_taylor_creek_trail_243_feet.json")
		trailNameList.add("northgate_peaks_trail_feet.json")
		trailNameList.add("observation_point_trail_291_feet.json")
		trailNameList.add("parus_trail_feet.json")
		trailNameList.add("rim_trail_feet.json")
		trailNameList.add("riverside_walk_trail_259_feet.json")
		trailNameList.add("sand_bench_trail-1_feet.json")
		trailNameList.add("the_grotto_trail_241_feet.json")
		trailNameList.add("upper_emerald_pools_trail_210_feet.json")
		trailNameList.add("watchman_trail_feet.json")
		trailNameList.add("weeping_rock_trail_216_feet.json")
		trailNameList.add("west_rim_trail_feet.json")
		trailNameList.add("wildcat_canyon_trail_229_feet.json")
		trailNameList.add("zion_view_trail_308_feet.json")
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
