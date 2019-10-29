package com.example.prototypeparksplushomescreen

import android.util.JsonReader
import android.util.Log
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point

class ParseJson()
{
	private var myPoints = ArrayList<Point>()

	fun build(reader: JsonReader): ArrayList<Point>
	{
		reader.beginObject()
		while (reader.hasNext())
		{
			val name = reader.nextName()
//            Log.d("FEATURE", name)
			if (name == "features")
			{
//                readJsonArray(reader)
				readArray(reader)
			}
			else
			{
				reader.skipValue()
			}
		}
		reader.endObject()
		return myPoints
	}

	private fun readArray(reader: JsonReader)
	{
		reader.beginArray()
		var count = 0;
		while (reader.hasNext())
		{
			count++;
			readObject(reader)
		}
		reader.endArray()
//        Log.d("FEATURE", "found features "+count)
	}

	private fun readObject(reader: JsonReader)
	{
		reader.beginObject()
		while (reader.hasNext())
		{
			val tag = reader.nextName()
//            Log.d("FEATURE", "*" + tag)

			if (tag == "geometry")
			{
//                readGeometry(reader)
				getPoints(reader)
//                Log.d("FEATURE","Mess with geometry later")
			}
			else
			{
				reader.skipValue()
			}
//            reader.skipValue()
		}
		reader.endObject()
	}

	private fun getPoints(reader: JsonReader)
	{
		reader.beginObject()
		var lng: Double = 0.0
		var lat: Double = 0.0

		while (reader.hasNext())
		{
			val name = reader.nextName()
			if (name == "x")
			{
				lng = reader.nextDouble()
			}
			else if (name == "y")
			{
				lat = reader.nextDouble()
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