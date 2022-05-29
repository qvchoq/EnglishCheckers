package com.coursework.englishcheckers.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.coursework.englishcheckers.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGameActivity()
    }

    private fun startGameActivity() {
        val startButton: Button = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

    }
}