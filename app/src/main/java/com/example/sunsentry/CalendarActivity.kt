package com.example.sunsentry

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarActivity : AppCompatActivity() {

    private val sunscreenUsage = mutableMapOf<String, MutableList<String>>() // Stores the times sunscreen was applied for each date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val backToUVButton: Button = findViewById(R.id.backToUVPage)

        // Handles calendar date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "${year}-${month + 1}-${dayOfMonth}" // Format the selected date
            showSunscreenDialog(selectedDate) // Show dialog for the selected date
        }

        // Back button to go back to UV info page
        backToUVButton.setOnClickListener {
            finish()
        }
    }

    // Displays a dialog to log sunscreen application times or clear the log
    private fun showSunscreenDialog(date: String) {
        // Get the existing log for the selected date or initialize an empty list
        val currentLog = sunscreenUsage[date] ?: mutableListOf()

        // Build the dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sunscreen Log for $date")

        // Generate the log message
        val logMessage = if (currentLog.isEmpty()) {
            "No applications recorded for this date."
        } else {
            "Applications:\n" + currentLog.joinToString("\n")
        }

        builder.setMessage(logMessage)

        // Button to log the application time
        builder.setPositiveButton("Add Time") { _, _ ->
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = timeFormat.format(Date()) // Get current time
            currentLog.add(currentTime) // Add the time to the log
            sunscreenUsage[date] = currentLog // Save the log for the date
            Toast.makeText(this, "Logged application at $currentTime", Toast.LENGTH_SHORT).show()
        }

        // Button to clear the log for the selected date
        builder.setNegativeButton("Clear Log") { _, _ ->
            sunscreenUsage[date] = mutableListOf() // Clear the log
            Toast.makeText(this, "Cleared all applications for $date", Toast.LENGTH_SHORT).show()
        }

        // Cancel button to dismiss the dialog
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss() // Close the dialog
        }

        builder.show() // Show the dialog
    }
}