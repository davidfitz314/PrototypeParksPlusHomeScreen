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
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MesaActivity : AppCompatActivity(), OnMapReadyCallback
{
	var mapboxMap: MapboxMap? = null
	var mapView: MapView? = null
	var featureCollection = MutableLiveData<ArrayList<Feature>>()
	var trailNameList: ArrayList<String> = ArrayList<String>()


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		Mapbox.getInstance(
			this,
			"pk.eyJ1IjoiZGF2aWRmaXR6MzE0IiwiYSI6ImNqeTdvZHlxYjAxa3YzbW9sZnlnZzJzMWsifQ.J1-VZUeqnR5oYH9514frYQ"
		)
		setContentView(R.layout.activity_mesa)
		mapView = findViewById(R.id.mapMesaFragmentMapView)
		mapView?.onCreate(savedInstanceState)
		mapView?.getMapAsync(this)
		this.title = "Mesa Area"
		addTrailNamesToList()
		for (i in trailNameList) {
			GlobalScope.launch {
				withContext(Dispatchers.Main){
					try {
						applicationContext.assets.open("mesajson/" + i).use {
							JsonReader(it.reader()).use { reader ->
								val myFeature = JsonTrailHandler().readJson(reader)
								addFeatureToCollection(myFeature)
							}
						}
					} catch (e: Exception){
						e.printStackTrace()
					}
				}
			}
		}
	}

	private fun addTrailNamesToList(){
		trailNameList.add("cryptobionic_trail_feet.json")
		trailNameList.add("dead_ringer_trail_feet.json")
		trailNameList.add("eagle_crags_trail_feet.json")
		trailNameList.add("goosebumps_trail_feet.json")
		trailNameList.add("goulds_rim_trail_feet.json")
		trailNameList.add("goulds_trail_feet.json")
		trailNameList.add("hidden_canyon_trail_12_feet.json")
		trailNameList.add("jem_trail_1_feet.json")
		trailNameList.add("little_creek_feet_3.json")
		trailNameList.add("more_cowbell_trail_204_feet.json")
		trailNameList.add("north_rim_trail-1_feet.json")
		trailNameList.add("practice_trail_feet.json")
		trailNameList.add("sand_mountain_1-1_feet.json")
		trailNameList.add("south_rim_trail_feet.json")
		trailNameList.add("water_canyon_trail-reversed.json")
		trailNameList.add("whiptail_trail-1_feet.json")
		trailNameList.add("white_trail_feet.json")
		trailNameList.add("windmill_trail_feet.json")
		trailNameList.add("yellow_trail_feet.json")

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
