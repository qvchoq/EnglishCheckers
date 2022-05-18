package com.coursework.englishcheckers

import android.app.Activity
import android.widget.FrameLayout

var board = mutableMapOf<String, BoardCell>()
var checkersOnBoard = mutableMapOf<String, Checker?>()

class Board {

    private val cellToLetter = listOf(
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h'
    )

    /*
     * Call all functions to start the game.
     */

    fun startGame(container: FrameLayout) {
        drawCheckersPlayerOne(container)
        drawCheckersPlayerTwo(container)
        placeCellsOnBoard()
    }

    /*
     * Draw first player checkers.
     */

    private fun drawCheckersPlayerOne(container: FrameLayout) {
        var x = 30
        var y = 1360
        for (i in 0..2) {

            if (x == 1070) {
                x = 160
                y = 1230
            }
            if (x == 1200) {
                x = 30
                y = 1100
            }
            for (q in 0..3) {
                val checker = Checker(1, false, Converter().positionToString(x, y))
                (container.context as Activity).runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                }
                checkersOnBoard[Converter().positionToString(x,y)] = checker
                x += 260
            }
        }
    }

    /*
     * Draw second player checkers.
     */

    private fun drawCheckersPlayerTwo(container: FrameLayout) {

        var x = 160
        var y = 450
        for (i in 0..2) {
            if (x == 1200) {
                x = 30
                y = 580
            }
            if (x == 1070) {
                x = 160
                y = 710
            }
            for (q in 0..3) {
                val checker = Checker(2, false, Converter().positionToString(x, y))
                (container.context as Activity).runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                }
                checkersOnBoard[Converter().positionToString(x,y)] = checker
                x += 260
            }
        }
    }

    /*
     * Create cells with info on board once.
     */

    private fun placeCellsOnBoard() {
        var nameCell: String
        var turnCell = false
        var colorChecker: Int
        for (x in 0..7) {
            for (y in 0..7) {
                nameCell = "${cellToLetter[y]}${x + 1}"

                if (checkersOnBoard[nameCell]?.getPosChecker()  == nameCell) {
                    colorChecker = checkersOnBoard[nameCell]!!.getColorChecker()
                } else {
                    colorChecker = 0
                }

                if ((x + 1) % 2 == 0) {
                    turnCell = (y + 1) % 2 != 0
                }

                if ((x + 1) % 2 != 0) {
                    turnCell = (y + 1) % 2 == 0
                }

                board[nameCell] = BoardCell(nameCell, turnCell, colorChecker,false, false)

            }
        }
    }
}