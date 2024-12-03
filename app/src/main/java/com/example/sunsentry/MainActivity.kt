package com.example.sunsentry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Automatically navigate to ConsentActivity
        val intent = Intent(this, ConsentActivity::class.java)
        startActivity(intent)

        // Finish MainActivity so it's removed from the back stack
        finish()
    }
}