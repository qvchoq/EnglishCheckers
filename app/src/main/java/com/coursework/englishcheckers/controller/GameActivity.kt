package com.coursework.englishcheckers.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.coursework.englishcheckers.R
import com.coursework.englishcheckers.model.Game
import com.coursework.englishcheckers.view.Board

class GameActivity : AppCompatActivity() {

    /*
     * Entry point in GameActivity.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val container: FrameLayout = findViewById(R.id.container)
        Board().prepareViewsForGame(container)
        Game().placeCellsOnBoard()
        Game().mainMoveLogic(container)
    }

}