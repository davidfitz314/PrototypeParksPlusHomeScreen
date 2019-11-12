package com.example.prototypeparksplushomescreen

import android.util.JsonReader
import android.util.Log
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class JsonTrailHandler {

    private var myPoints = ArrayList<Point>()


    fun readJson(inputStream: JsonReader): Feature? {
        inputStream.beginObject()
        while (inputStream.hasNext())
        {
            val name = inputStream.nextName()
//            Log.d("FEATURE", name)
            if (name == "coordinates")
            {
//                readJsonArray(reader)
                readArray(inputStream)
            }
            else
            {
                inputStream.skipValue()
            }
        }
        inputStream.endObject()
        if (myPoints.size > 0) {
            val feature = Feature.fromGeometry(LineString.fromLngLats(myPoints))
            return feature
        } else {
            return null
        }
    }

    private fun readArray(reader: JsonReader){
        reader.beginArray()
        var count = 0;
        while (reader.hasNext())
        {
            count++;
            readObject(reader)
        }
        reader.endArray()
    }

    private fun readObject(reader: JsonReader){
        reader.beginObject()
        var lng: Double = 0.0
        var lat: Double = 0.0

        while (reader.hasNext())
        {
            val name = reader.nextName()
            if (name == "lng")
            {
                lng = reader.nextDouble()
            }
            else if (name == "lat")
            {
                lat = reader.nextDouble()
            } else {
                reader.skipValue()
            }
        }
        if (lng != 0.0 && lat != 0.0)
        {
//            Log.d("FEATURE", "success "+lat+" "+lng)
            myPoints.add(Point.fromLngLat(lng, lat))
        }
        reader.endObject()
    }
}