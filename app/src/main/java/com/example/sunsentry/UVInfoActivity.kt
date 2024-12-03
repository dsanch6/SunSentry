package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UVInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uv_info)

        // Set UV information text
        val uvInfoTextView = findViewById<TextView>(R.id.uvInfoTextView)
        uvInfoTextView.text = "UV Index: 6"

        // Button to open WeeklyCalendarActivity
        val openCalendarButton = findViewById<Button>(R.id.openCalendar)
        openCalendarButton.setOnClickListener {
            val intent = Intent(this, WeeklyCalendarActivity::class.java)
            startActivity(intent)
        }
    }
}