package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

// Activity for user login and email input
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get references to the email input and login button
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val continueButton = findViewById<Button>(R.id.loginButton)

        // When the continue button is clicked, pass the email to UVInfoActivity
        continueButton.setOnClickListener {
            val intent = Intent(this, UVInfoActivity::class.java)
            intent.putExtra("userEmail", emailInput.text.toString()) // Send the email as an extra
            startActivity(intent) // Start UVInfoActivity
            finish() // Close LoginActivity
        }
    }
}