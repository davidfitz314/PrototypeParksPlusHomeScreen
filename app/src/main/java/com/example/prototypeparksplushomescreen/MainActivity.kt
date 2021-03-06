package com.example.prototypeparksplushomescreen

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.prototypeparksplushomescreen.viewmodel.MainActivityViewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener
{


	var mapboxMap: MapboxMap? = null
	var mapView: MapView? = null
	private val GREY_COLOR = Color.parseColor("#c2c2c2")
	private val RED_COLOR = Color.parseColor("#BF544C")
	private val FILL_OPACITY = .7f
	private val LINE_WIDTH = 2f
//	var alpinePointsList = MutableLiveData<List<Point>>()
	var alpineFeatureCollection = MutableLiveData<FeatureCollection>()
	var canyonFeatureCollection = MutableLiveData<FeatureCollection>()
	var urbanFeatureCollection = MutableLiveData<FeatureCollection>()
	var mesaFeatureCollection = MutableLiveData<FeatureCollection>()
	var desertFeatureCollection = MutableLiveData<FeatureCollection>()

	private val viewModel: MainActivityViewModel by lazy {
		ViewModelProvider(this).get(MainActivityViewModel::class.java)
	}
//
//	var canyonPointsList = MutableLiveData<List<Point>>()
//	var desertPointsList = MutableLiveData<List<Point>>()
//	var mesaPointsList = MutableLiveData<List<Point>>()
//	var urbanPointsList = MutableLiveData<List<Point>>()
	var alpineFeature = Feature.fromGeometry(Point.fromLngLat(-113.560123, 37.450705))
	var desertFeature = Feature.fromGeometry(Point.fromLngLat(-113.895337, 37.208261))
	var mesaFeature = Feature.fromGeometry(Point.fromLngLat(-113.115297, 37.095368))
	var urbanFeature = Feature.fromGeometry(Point.fromLngLat(-113.570852, 37.080415))
	var canyonFeature = Feature.fromGeometry(Point.fromLngLat(-113.031394, 37.330810))
	var isPopUpEnabled: Boolean = true
	var prefs: SharedPreferences? = null
	val popupBoolean = "mainscreen_popup_key"


	var textFeatureList: List<Feature>? = null

	val canyonSource = "canyon-source-id"
	val canyonLayer = "canyon-layer-id"
	val canyonLayerBorder = "canyon-layer-border-id"
	val alpineSource = "alpine-source-id"
	val alpineLayer = "alpine-layer-id"
	val alpineLayerBorder = "alpine-layer-border-id"
	val desertSource = "desert-source-id"
	val desertLayer = "desert-layer-id"
	val desertLayerBorder = "desert-layer-border-id"
	val mesaSource = "mesa-source-id"
	val mesaLayer = "mesa-layer-id"
	val mesaLayerBorder = "mesa-layer-border-id"
	val urbanSource = "urban-source-id"
	val urbanLayer = "urban-layer-id"
	val urbanLayerBorder = "urban-layer-border-id"


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		Mapbox.getInstance(
			this,
			"pk.eyJ1IjoiZGF2aWRmaXR6MzE0IiwiYSI6ImNqeTdvZHlxYjAxa3YzbW9sZnlnZzJzMWsifQ.J1-VZUeqnR5oYH9514frYQ"
		)
		setContentView(R.layout.activity_main)
		mapView = findViewById(R.id.mapFragmentMapView)
		mapView?.onCreate(savedInstanceState)
		mapView?.getMapAsync(this)
		alpineFeature.addStringProperty("name", "ALPINE")
		mesaFeature.addStringProperty("name", "MESA")
		urbanFeature.addStringProperty("name", "URBAN")
		canyonFeature.addStringProperty("name", "CANYON")
		desertFeature.addStringProperty("name", "DESERT")
		textFeatureList =
			listOf(alpineFeature, mesaFeature, urbanFeature, canyonFeature, desertFeature)
		GlobalScope.launch {
			loadTrailFeatures()
		}



		//get the shared prefs object
		prefs = PreferenceManager.getDefaultSharedPreferences(this)
		isPopUpEnabled = prefs!!.getBoolean(popupBoolean, true)

		if (!isPopUpEnabled) {
			val closedLayout: ConstraintLayout = findViewById(R.id.homescreenLayoverLayout)
			closedLayout.visibility = View.GONE
		}


		title = "Greater Zion Regions"
		//		fetchFromAssets()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater: MenuInflater = menuInflater
		inflater.inflate(R.menu.main_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId){
			R.id.SettingsButton -> {
				goToOptionsPage()
				return true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onMapReady(mapboxMap: MapboxMap)
	{
		this.mapboxMap = mapboxMap
		mapboxMap.setStyle(Style.OUTDOORS) {
			it.addImage("trailheaddefault", BitmapFactory.decodeResource(resources, R.drawable.hikingsm))
			addSources(it)
			val textFeatureList2 = textFeatureList
			if (textFeatureList2 != null)
			{
				it.addSource(
					GeoJsonSource(
						"text-items-source",
						FeatureCollection.fromFeatures(textFeatureList2)
					)
				)
				it.addLayer(
					SymbolLayer("text-layer-id", "text-items-source")
						.withProperties(
							PropertyFactory.textField("{name}"),
							PropertyFactory.textAllowOverlap(true),
							PropertyFactory.textColor(Color.parseColor("#FFFFFF")),
							PropertyFactory.textOffset(arrayOf(-0.25f, -.75f))

						)
				)
			}
			mapboxMap.addOnMapClickListener(this)
		}
	}

	override fun onMapClick(point: LatLng): Boolean
	{
		return handleClickIcon(mapboxMap?.getProjection()?.toScreenLocation(point))
	}

	private fun handleClickIcon(point: PointF?): Boolean
	{
		if (point == null)
		{
			return false
		}

		var rectF: RectF = RectF(point.x - 10, point.y - 10, point.x + 10, point.y + 10);
		//trail handler clicks
		val thfeatures: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, "trailheadslayer")
		if (thfeatures != null && thfeatures.size > 0)
		{
			Toast.makeText(this, "clicked " + "trail head Layer" + thfeatures.get(0).properties(), Toast.LENGTH_SHORT).show()
			val trailheadIntent = Intent(this, TrailHeadActivity::class.java)
			trailheadIntent.putExtra("trail_head_info", thfeatures.get(0).getStringProperty("name"))
			startActivity(trailheadIntent)
			return true
		}
		//regions clicks

		val features: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, alpineLayer)
		if (features != null && features.size > 0)
		{
//			Toast.makeText(this, "clicked " + "Alpine Layer", Toast.LENGTH_SHORT).show()
			val alpineIntent = Intent(this, AlpineActivity::class.java)
			alpineIntent.putExtra("folder_name", "alpinejson")
			startActivity(alpineIntent)
			return true
		}
		val cfeatures: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, canyonLayer)
		if (cfeatures != null && cfeatures.size > 0)
		{
			//			val name: String = features.get(0).getStringProperty(FEATURE_TITLE_LABEL);
//			Toast.makeText(this, "clicked " + "Canyon Layer", Toast.LENGTH_SHORT).show()
			val canyonIntent = Intent(this, CanyonActivity::class.java)
			canyonIntent.putExtra("folder_name", "canyonjson")
			startActivity(canyonIntent)
			return true
		}
		val dfeatures: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, desertLayer)
		if (dfeatures != null && dfeatures.size > 0)
		{
			//			val name: String = features.get(0).getStringProperty(FEATURE_TITLE_LABEL);
//            viewModel.onNavigateToFeatureScreen(features.get(0))
//			Toast.makeText(this, "clicked " + "Desert Layer", Toast.LENGTH_SHORT).show()
			val desertIntent = Intent(this, DesertActivity::class.java)
			desertIntent.putExtra("folder_name", "desertjson")
			startActivity(desertIntent)
			return true
		}
		val mfeatures: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, mesaLayer)
		if (mfeatures != null && mfeatures.size > 0)
		{
			//			val name: String = features.get(0).getStringProperty(FEATURE_TITLE_LABEL);
//            viewModel.onNavigateToFeatureScreen(features.get(0))
			val mesaIntent = Intent(this, MesaActivity::class.java)
			mesaIntent.putExtra("folder_name", "mesajson")
			startActivity(mesaIntent)
//			Toast.makeText(this, "clicked " + "Mesa Layer", Toast.LENGTH_SHORT).show()
			return true
		}
		val ufeatures: List<Feature>? = mapboxMap?.queryRenderedFeatures(rectF, urbanLayer)
		if (ufeatures != null && ufeatures.size > 0)
		{
			//			val name: String = features.get(0).getStringProperty(FEATURE_TITLE_LABEL);
//            viewModel.onNavigateToFeatureScreen(features.get(0))
//			Toast.makeText(this, "clicked " + "Urban Layer", Toast.LENGTH_SHORT).show()
			val urbanIntent = Intent(this, UrbanActivity::class.java)
			urbanIntent.putExtra("folder_name", "urbanjson")
			startActivity(urbanIntent)
			return true
		}
		return false
	}

	private fun addSources(style: Style)
	{
		alpineFeatureCollection.observe(this, androidx.lifecycle.Observer {
			if (it != null)
			{
				if (style.getSource(alpineSource) != null)
				{
					removeAlpineMapLayer(style)
					style.removeSource(alpineSource)
				}

				if (style.getSource(alpineSource) == null)
				{
					val innerList : FeatureCollection = it
//					val innerLineString = LineString.fromLngLats(it)
//					innerList.add(innerLineString)
					style.addSource(GeoJsonSource(alpineSource, it))
					addAlpineLayer(style)

				}
			}
		})

		canyonFeatureCollection.observe(this, androidx.lifecycle.Observer {
			if (it != null)
			{
				if (style.getSource(canyonSource) != null)
				{
					removeCanyonMapLayer(style)
					style.removeSource(canyonSource)
				}

				if (style.getSource(canyonSource) == null)
				{
//					val innerList = ArrayList<LineString>()
//					val innerLineString = LineString.fromLngLats(it)
//					innerList.add(innerLineString)
					style.addSource(GeoJsonSource(canyonSource, it))
					addCanyonLayer(style)
				}
			}
		})

		desertFeatureCollection.observe(this, androidx.lifecycle.Observer {
			if (it != null)
			{
				if (style.getSource(desertSource) != null)
				{
					removeDesertMapLayer(style)
					style.removeSource(desertSource)
				}

				if (style.getSource(desertSource) == null)
				{
//					val innerList = ArrayList<LineString>()
//					val innerLineString = LineString.fromLngLats(it)
//					innerList.add(innerLineString)
					style.addSource(GeoJsonSource(desertSource, it))
					addDesertLayer(style)
				}
			}
		})

		mesaFeatureCollection.observe(this, androidx.lifecycle.Observer {
			if (it != null)
			{
				if (style.getSource(mesaSource) != null)
				{
					removeMesaMapLayer(style)
					style.removeSource(mesaSource)
				}

				if (style.getSource(mesaSource) == null)
				{
//					val innerList = ArrayList<LineString>()
//					val innerLineString = LineString.fromLngLats(it)
//					innerList.add(innerLineString)
					style.addSource(GeoJsonSource(mesaSource, it))
					addMesaLayer(style)
				}
			}
		})

		urbanFeatureCollection.observe(this, androidx.lifecycle.Observer {
			if (it != null)
			{
				if (style.getSource(urbanSource) != null)
				{
					removeUrbanMapLayer(style)
					style.removeSource(urbanSource)
				}

				if (style.getSource(urbanSource) == null)
				{
//					val innerList = ArrayList<LineString>()
//					val innerLineString = LineString.fromLngLats(it)
//					innerList.add(innerLineString)
					style.addSource(GeoJsonSource(urbanSource, it))
					addUrbanLayer(style)
				}
			}
		})

		viewModel.trailHeads.observe(this, androidx.lifecycle.Observer {
			if (it != null){
//				Log.d("MainActivityObserver", it.size.toString())
				if (style.getSource("trailheadssource") != null){
					removeTrailHeadsLayer(style)
					style.removeSource("trailheadssource")
				}

				if (style.getSource("trailheadssource") == null){
					style.addSource(GeoJsonSource("trailheadssource", FeatureCollection.fromFeatures(it)))
					addTrailHeadLayer(style)
				}
			}
		})
	}

	private fun removeTrailHeadsLayer(style: Style){
		if (style.getLayer("trailheadslayer") != null){
			style.removeLayer("trailheadslayer")
		}
	}

	private fun addTrailHeadLayer(style: Style){
		if (style.getLayer("trailheadslayer") == null){
			style.addLayer(
				SymbolLayer("trailheadslayer", "trailheadssource")
					.withProperties(
						PropertyFactory.textField("{name}"),
						PropertyFactory.iconImage("{image}"),
						PropertyFactory.iconAllowOverlap(true),
						PropertyFactory.iconOffset(arrayOf(0f, -9f))
					)
			)

		}
	}

	private fun removeAlpineMapLayer(@NonNull style: Style)
	{
		if (style.getLayer(alpineLayer) != null)
		{
			style.removeLayer(alpineLayer)
			Log.d("FEATURE", "layer removed")
		}
		if (style.getLayer(alpineLayerBorder) != null)
		{
			style.removeLayer(alpineLayerBorder)
			Log.d("FEATURE", "layer removed")
		}
	}

	private fun removeCanyonMapLayer(@NonNull style: Style)
	{
		if (style.getLayer(canyonLayer) != null)
		{
			style.removeLayer(canyonLayer)
			Log.d("FEATURE", "layer removed")
		}
		if (style.getLayer(canyonLayerBorder) != null)
		{
			style.removeLayer(canyonLayerBorder)
			Log.d("FEATURE", "layer removed")
		}
	}

	private fun removeDesertMapLayer(@NonNull style: Style)
	{
		if (style.getLayer(desertLayer) != null)
		{
			style.removeLayer(desertLayer)
			Log.d("FEATURE", "layer removed")
		}
		if (style.getLayer(desertLayerBorder) != null)
		{
			style.removeLayer(desertLayerBorder)
			Log.d("FEATURE", "layer removed")
		}
	}

	private fun removeMesaMapLayer(@NonNull style: Style)
	{
		if (style.getLayer(mesaLayer) != null)
		{
			style.removeLayer(mesaLayer)
			Log.d("FEATURE", "layer removed")
		}
		if (style.getLayer(mesaLayerBorder) != null)
		{
			style.removeLayer(mesaLayerBorder)
			Log.d("FEATURE", "layer removed")
		}
	}

	private fun removeUrbanMapLayer(@NonNull style: Style)
	{
		if (style.getLayer(urbanLayer) != null)
		{
			style.removeLayer(urbanLayer)
			Log.d("FEATURE", "layer removed")
		}
		if (style.getLayer(urbanLayerBorder) != null)
		{
			style.removeLayer(urbanLayerBorder)
			Log.d("FEATURE", "layer removed")
		}
	}

	private fun addAlpineLayer(style: Style)
	{
		style.addLayer(
			FillLayer(alpineLayer, alpineSource).withProperties(
				lineColor(RED_COLOR),
				lineWidth(LINE_WIDTH),
				fillColor(Color.parseColor("#8072916A")),
				textField("alpine")
			)
		);
		style.addLayer(
			LineLayer(alpineLayerBorder, alpineSource).withProperties(
				lineColor(Color.parseColor("#466940")),
				lineWidth(LINE_WIDTH),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		);
	}

	private fun addCanyonLayer(style: Style)
	{
		style.addLayer(
			FillLayer(canyonLayer, canyonSource).withProperties(
				lineColor(RED_COLOR),
				lineWidth(LINE_WIDTH),
				fillColor(Color.parseColor("#80461EB5")),
				textField("canyon")
			)
		);
		style.addLayer(
			LineLayer(canyonLayerBorder, canyonSource).withProperties(
				lineColor(Color.parseColor("#54496D")),
				lineWidth(LINE_WIDTH),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		);
	}

	private fun addDesertLayer(style: Style)
	{
		style.addLayer(
			FillLayer(desertLayer, desertSource).withProperties(
				lineColor(RED_COLOR),
				lineWidth(LINE_WIDTH),
				fillColor(Color.parseColor("#80FFB800")),
				textField("desert")
			)
		);
		style.addLayer(
			LineLayer(desertLayerBorder, desertSource).withProperties(
				lineColor(Color.parseColor("#FFB800")),
				lineWidth(LINE_WIDTH),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		);
	}


	private fun addMesaLayer(style: Style)
	{
		style.addLayer(
			FillLayer(mesaLayer, mesaSource).withProperties(
				lineColor(RED_COLOR),
				lineWidth(LINE_WIDTH),
				fillColor(Color.parseColor("#80E72508")),
				textField("mesa")
			)
		);
		style.addLayer(
			LineLayer(mesaLayerBorder, mesaSource).withProperties(
				lineColor(Color.parseColor("#F64848")),
				lineWidth(LINE_WIDTH),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		);
	}

	private fun addUrbanLayer(style: Style)
	{
		style.addLayer(
			FillLayer(urbanLayer, urbanSource).withProperties(
				lineColor(RED_COLOR),
				lineWidth(LINE_WIDTH),
				fillColor(Color.parseColor("#80529FF2"))
			)
		);
		style.addLayer(
			LineLayer(urbanLayerBorder, urbanSource).withProperties(
				lineColor(Color.parseColor("#529FF2")),
				lineWidth(LINE_WIDTH),
				lineCap(Property.LINE_CAP_ROUND),
				lineJoin(Property.LINE_JOIN_ROUND)
			)
		);
	}


	//
	// Gets the trails geojson from the assets file
	// and loads it in a background thread to not effect main thread performance.
	//
	private suspend fun loadTrailFeatures()
	{
		withContext(Dispatchers.Main) {
			applicationContext.assets.open("Alpine.geojson").use {
				alpineFeatureCollection.value = FeatureCollection.fromJson(convertStreamToString(it))
//				JsonReader(it.reader()).use { reader ->
//
////					alpinePointsList.value = ParseJson().build(reader)
////                    Log.d("FEATURE", output.size.toString())
//				}
			}

			applicationContext.assets.open("Canyon.geojson").use {
				canyonFeatureCollection.value = FeatureCollection.fromJson(convertStreamToString(it))
//				JsonReader(it.reader()).use { reader ->
//					canyonPointsList.value = ParseJson().build(reader)
//				}
			}

			applicationContext.assets.open("Desert.geojson").use {
				desertFeatureCollection.value = FeatureCollection.fromJson(convertStreamToString(it))
//				JsonReader(it.reader()).use { reader ->
//					desertPointsList.value = ParseJson().build(reader)
//				}
			}

			applicationContext.assets.open("Mesa.geojson").use {
				mesaFeatureCollection.value = FeatureCollection.fromJson(convertStreamToString(it))
//				JsonReader(it.reader()).use { reader ->
//					mesaPointsList.value = ParseJson().build(reader)
//				}
			}

			applicationContext.assets.open("Urban.geojson").use {
				urbanFeatureCollection.value = FeatureCollection.fromJson(convertStreamToString(it))
//				JsonReader(it.reader()).use { reader ->
//					urbanPointsList.value = ParseJson().build(reader)
//				}
			}
			Log.d("FEATURES", "LOADED")
		}
	}

	companion object {
		fun convertStreamToString(itIs: InputStream): String{
			var scanner: Scanner = Scanner(itIs).useDelimiter("\\A");
			if (scanner.hasNext()){
				return scanner.next()
			}
			return ""
		}
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

	fun closeLayoverLayout(view: View)
	{
		if (isPopUpEnabled) {
			val closedLayout: ConstraintLayout = findViewById(R.id.homescreenLayoverLayout)
			closedLayout.visibility = View.GONE
			val editor = prefs!!.edit()
			editor.putBoolean(popupBoolean, false)
			editor.apply()
		}
	}

	fun goToOptionsPage(){
		val intent = Intent(this, SettingsActivity::class.java)
		startActivity(intent)
	}
}
