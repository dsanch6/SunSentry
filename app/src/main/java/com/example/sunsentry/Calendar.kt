package com.example.sunsentry

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WeeklyCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val sunButton: Button = findViewById(R.id.sunUsage)
        sunButton.setOnClickListener {
            sunButton.text = "Used"

        }
    }
}
