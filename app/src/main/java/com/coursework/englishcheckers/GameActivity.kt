package com.coursework.englishcheckers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

    /*
     * ENGLISH CHECKERS RULES:
     * First player make first turn with red checkers.
     * Default checker can move and beat only in one cell diagonally forward
     * Default checker can't move and beat backward
     * Queen checker can move and beat in forward and backward in one cell diagonally
     */


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val container: FrameLayout = findViewById(R.id.container)
        Board().startGame(container)
        Game().makePlayerTurn(container)
    }
}