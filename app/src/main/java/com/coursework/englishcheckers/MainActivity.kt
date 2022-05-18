package com.coursework.englishcheckers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickManager()
    }

    private fun clickManager() {
        val startButton: Button = findViewById(R.id.startButton)

        fun startButtonClick() {
            startActivity(Intent(this, GameActivity::class.java))
        }

        startButton.setOnClickListener {
            startButtonClick()
        }

    }
}