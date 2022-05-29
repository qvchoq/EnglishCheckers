package com.coursework.englishcheckers.view

import android.app.Activity
import android.widget.FrameLayout
import android.widget.ImageView
import com.coursework.englishcheckers.R
import com.coursework.englishcheckers.controller.screenHeight
import com.coursework.englishcheckers.model.BoardCell
import com.coursework.englishcheckers.model.Checker
import com.coursework.englishcheckers.model.Converter

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

    fun prepareViewsForGame(container: FrameLayout) {
        drawCheckersPlayerOne(container)
        drawCheckersPlayerTwo(container)
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
     * Draw a highlight shape.
     */

    fun setHighlightCell(container: FrameLayout, cellName: String) {
        board[cellName]?.setHighlight(true)

        val cell = ImageView(container.context).apply {
            this.tag = cellName+'h'
            this.setImageResource(R.drawable.highlight)
            this.layoutParams = FrameLayout.LayoutParams(130,130)
            this.translationX = Converter().cellNameToCoordinate(cellName).first.toFloat() - 11
            this.translationY = Converter().cellNameToCoordinate(cellName).second.toFloat() - 11
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

    /*
     * Erase the default checker and place there queen checker.
     */

    fun replaceDefaultCheckerToQueen(container: FrameLayout, checkerName: String, x: Int, y: Int) {
        val checker = checkersOnBoard[checkerName]

        (container.context as Activity).runOnUiThread {
            if (checker != null) {
                container.removeView(container.findViewWithTag(checkerName))
                container.addView(checker.draw(container, x, y))
            }
        }


    }
}