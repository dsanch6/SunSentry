package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WeeklyCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Button to mark sun usage
        val sunButton: Button = findViewById(R.id.sunUsage)
        sunButton.setOnClickListener {
            sunButton.text = "Used"
        }

        // Button to navigate back to UVInfoActivity
        val backToUVButton: Button = findViewById(R.id.backToUVPage)
        backToUVButton.setOnClickListener {
            val intent = Intent(this, UVInfoActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close current activity
        }
    }
}