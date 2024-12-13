package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val continueButton = findViewById<Button>(R.id.loginButton)
        continueButton.setOnClickListener {
            // Navigate to ConsentActivity
            val intent = Intent(this, ConsentActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close LoginActivity so it can't be revisited with Back button
        }
    }
}
