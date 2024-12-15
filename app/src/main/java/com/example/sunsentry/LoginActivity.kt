package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val continueButton = findViewById<Button>(R.id.loginButton)
        continueButton.setOnClickListener {
            val intent = Intent(this, UVInfoActivity::class.java)
            intent.putExtra("userEmail", emailInput.text.toString())
            startActivity(intent)
            finish()
        }
    }
}