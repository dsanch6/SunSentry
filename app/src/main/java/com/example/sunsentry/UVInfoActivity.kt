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
    private val apiKey = "f9f7e08fea8dddb165042d2bfebe54f0" // OpenWeather API key
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var uvInfoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uv_info)

        uvInfoTextView = findViewById(R.id.uvInfoTextView)
        val openCalendarButton = findViewById<Button>(R.id.openCalendar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissionAndFetchUVIndex()

        openCalendarButton.setOnClickListener {
            val intent = Intent(this, WeeklyCalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLocationPermissionAndFetchUVIndex() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchLocationAndUVIndex()
        }
    }

    private fun fetchLocationAndUVIndex() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        fetchUVIndex(location.latitude, location.longitude) { uvIndex ->
                            runOnUiThread {
                                uvInfoTextView.text = getString(R.string.uv_index_text, uvIndex.toString())
                            }
                        }
                    } else {
                        Log.e("UVInfoActivity", "Location is null")
                        uvInfoTextView.text = getString(R.string.location_error)
                    }
                }.addOnFailureListener {
                    Log.e("UVInfoActivity", "Error getting location", it)
                    uvInfoTextView.text = getString(R.string.location_error)
                }
            } else {
                uvInfoTextView.text = getString(R.string.permission_required)
            }
        } catch (e: SecurityException) {
            Log.e("UVInfoActivity", "SecurityException: Permission not granted", e)
            uvInfoTextView.text = getString(R.string.permission_required)
        }
    }

    private fun fetchUVIndex(latitude: Double, longitude: Double, callback: (Double) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/uvi?lat=$latitude&lon=$longitude&appid=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("UVInfoActivity", "Error fetching UV Index", e)
                runOnUiThread {
                    uvInfoTextView.text = getString(R.string.api_error)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val json = JSONObject(responseBody.string())
                        val uvIndex = json.optDouble("value", 0.0) // "value" contains the UV index
                        callback(uvIndex)
                    }
                } else {
                    Log.e("UVInfoActivity", "API response unsuccessful")
                    runOnUiThread {
                        uvInfoTextView.text = getString(R.string.api_error)
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationAndUVIndex()
            } else {
                uvInfoTextView.text = getString(R.string.permission_required)
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}