package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkPermissionButton = findViewById<Button>(R.id.checkPermissionButton)
        checkPermissionButton.setOnClickListener {
            val intent = Intent(this, ConsentActivity::class.java)
            startActivity(intent)
        }
    }
}