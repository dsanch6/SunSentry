package com.example.sunsentry

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UVInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uv_info)

        val uvInfoTextView = findViewById<TextView>(R.id.uvInfoTextView)
        uvInfoTextView.text = "UV Index: 6"
    }
}