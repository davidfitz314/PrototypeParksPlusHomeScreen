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

		this.title = "Alpine Area"

	}

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
