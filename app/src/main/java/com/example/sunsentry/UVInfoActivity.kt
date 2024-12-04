package com.example.sunsentry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class UVInfoActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val uvApiKey = "f9f7e08fea8dddb165042d2bfebe54f0"
    private val geocodingApiKey = "AIzaSyDj60wCZPrAZRMQp_PLs-SZ_inqLeAb9OM"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var uvInfoTextView: TextView
    private lateinit var locationNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uv_info)

        uvInfoTextView = findViewById(R.id.uvInfoTextView)
        locationNameTextView = findViewById(R.id.locationNameTextView)
        val openCalendarButton = findViewById<Button>(R.id.openCalendar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissionAndFetchData()

        openCalendarButton.setOnClickListener {
            val intent = Intent(this, WeeklyCalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLocationPermissionAndFetchData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchLocationData()
        }
    }

    private fun fetchLocationData() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        fetchUVIndex(location.latitude, location.longitude) { uvIndex ->
                            runOnUiThread {
                                uvInfoTextView.text = "UV Index: $uvIndex"
                            }
                        }
                        fetchLocationName(location.latitude, location.longitude) { locationName ->
                            runOnUiThread {
                                locationNameTextView.text = filterPlusCode(locationName)
                            }
                        }
                    } else {
                        uvInfoTextView.text = "Error: Location is null"
                    }
                }
            }
        } catch (e: SecurityException) {
            uvInfoTextView.text = "Error: Permission denied"
        }
    }

    private fun fetchUVIndex(latitude: Double, longitude: Double, callback: (Double) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/uvi?lat=$latitude&lon=$longitude&appid=$uvApiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("UVInfoActivity", "UV API error", e)
                callback(0.0)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = JSONObject(it.string())
                        val uvIndex = json.optDouble("value", 0.0)
                        callback(uvIndex)
                    }
                } else {
                    callback(0.0)
                }
            }
        })
    }

    private fun fetchLocationName(latitude: Double, longitude: Double, callback: (String) -> Unit) {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=$geocodingApiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("UVInfoActivity", "Geocoding API error", e)
                callback("Unknown")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let {
                        val json = JSONObject(it.string())
                        val results = json.optJSONArray("results")
                        val locationName = results?.optJSONObject(0)?.optString("formatted_address") ?: "Unknown"
                        callback(locationName)
                    }
                } else {
                    callback("Unknown")
                }
            }
        })
    }

    private fun filterPlusCode(locationName: String): String {
        val regex = Regex("^[A-Z0-9+]+\\s+")
        return regex.replace(locationName, "").trim()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}