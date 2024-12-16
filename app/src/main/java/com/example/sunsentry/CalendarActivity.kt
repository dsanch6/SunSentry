package com.example.sunsentry

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appname.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarActivity : BaseActivity() {

    private val sunscreenUsage = mutableMapOf<String, MutableList<String>>() // storing times you applied

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val backToUVButton: Button = findViewById(R.id.backToUVPage)

        // handle date clicks
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "${year}-${month + 1}-${dayOfMonth}" // formatting date
            showSunscreenDialog(selectedDate)
        }

        // back button
        backToUVButton.setOnClickListener {
            finish()
        }
    }

    private fun showSunscreenDialog(date: String) {
        // get the log for the selected date or default to an empty list
        val currentLog = sunscreenUsage[date] ?: mutableListOf()

        // build the dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sunscreen Log for $date")

        // generate log message
        val logMessage = if (currentLog.isEmpty()) {
            "No applications recorded for this date."
        } else {
            "Applications:\n" + currentLog.joinToString("\n")
        }

        builder.setMessage(logMessage)

        // button to log the application time
        builder.setPositiveButton("Add Time") { _, _ ->
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = timeFormat.format(Date())
            currentLog.add(currentTime)
            sunscreenUsage[date] = currentLog
            Toast.makeText(this, "Logged application at $currentTime", Toast.LENGTH_SHORT).show()
        }

        //  button to clear the log for the date
        builder.setNegativeButton("Clear Log") { _, _ ->
            sunscreenUsage[date] = mutableListOf()
            Toast.makeText(this, "Cleared all applications for $date", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss() // Close the dialog
        }

        builder.show()
    }
}
