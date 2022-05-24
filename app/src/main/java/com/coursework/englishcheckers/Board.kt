package com.coursework.englishcheckers

import android.app.Activity
import android.widget.FrameLayout
import android.widget.ImageView

var board = mutableMapOf<String, BoardCell>()
var checkersOnBoard = mutableMapOf<String, Checker?>()

val cellToLetter = listOf(
    'a',
    'b',
    'c',
    'd',
    'e',
    'f',
    'g',
    'h'
)

class Board {

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
                val checker = Checker(1, false, Converter().coordinateToCellName(x, y))
                (container.context as Activity).runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                }
                checkersOnBoard[Converter().coordinateToCellName(x,y)] = checker
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
                val checker = Checker(2, false, Converter().coordinateToCellName(x, y))
                (container.context as Activity).runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                }
                checkersOnBoard[Converter().coordinateToCellName(x,y)] = checker
                x += 260
            }
        }
    }

    /*
     * Create cells with information on board once.
     */

    private fun placeCellsOnBoard() {
        var nameCell: String
        var turnCell = false
        var colorChecker: Int
        for (x in 0..7) {
            for (y in 0..7) {
                nameCell = "${cellToLetter[y]}${x + 1}"

                if (checkersOnBoard[nameCell]?.getPos()  == nameCell) {
                    colorChecker = checkersOnBoard[nameCell]!!.getColor()
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

    /*
     * Draw a highlight shape.
     */

    fun setHighlightCell(container: FrameLayout, cellName: String) {
        board[cellName]?.setHighlight(true)

        val cell = ImageView(container.context).apply {
            this.tag = cellName+'h'
            this.setImageResource(R.drawable.highlight)
            this.layoutParams = FrameLayout.LayoutParams(130,130)
            this.translationX = Converter().cellNameToCoordinate(cellName).first.toFloat() - 12
            this.translationY = Converter().cellNameToCoordinate(cellName).second.toFloat() - 12
        }

        (container.context as Activity).runOnUiThread {
            container.addView(cell)
        }

    }

    /*
     * Erase the highlight shape.
     */

    fun removeHighlightCell(container: FrameLayout, cellName: String) {
        board[cellName]?.setHighlight(false)

        (container.context as Activity).runOnUiThread {
            container.removeView(container.findViewWithTag<ImageView>(cellName+'h'))
        }
    }
}