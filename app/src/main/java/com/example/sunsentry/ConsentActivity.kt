package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// Activity to request user consent for location access
class ConsentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)

        // Button to grant location permission
        val grantPermissionButton = findViewById<Button>(R.id.grantPermissionButton)
        grantPermissionButton.setOnClickListener {
            checkLocationPermission() // Check if permission is granted
        }
    }

    // Checks if the app has permission to access location
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            navigateToUVInfo() // Navigate to UVInfoActivity if permission is granted
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    // Handles the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            navigateToUVInfo() // Navigate if permission is granted
        } else {
            // Show a toast message if permission is denied
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Starts UVInfoActivity when permission is granted
    private fun navigateToUVInfo() {
        val intent = Intent(this, UVInfoActivity::class.java)
        startActivity(intent)
    }
}