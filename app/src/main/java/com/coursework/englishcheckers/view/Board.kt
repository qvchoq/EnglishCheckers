package com.coursework.englishcheckers.view

import android.app.Activity
import android.widget.FrameLayout
import android.widget.ImageView
import com.coursework.englishcheckers.R
import com.coursework.englishcheckers.model.BoardCell
import com.coursework.englishcheckers.model.Checker
import com.coursework.englishcheckers.model.Converter

class Board {

    companion object {

        var board = mutableMapOf<String, BoardCell>()
        var checkersOnBoard = mutableMapOf<String, Checker?>()
        val cellToLetter = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')

    }

    /*
     * Draw checkers.
     */

    fun drawCheckers(container: FrameLayout) {
        var x: Int
        var y: Int
        for (player in 0..1) {

            if (player == 0) {
                x = 30
                y = 1360
            } else {
                x = 160
                y = 450
            }

            for (i in 0..2) {

                if (player == 0) {
                    if (x == 1070) {
                        x = 160
                        y = 1230
                    }
                    if (x == 1200) {
                        x = 30
                        y = 1100
                    }
                }

                if (player == 1) {
                    if (x == 1200) {
                        x = 30
                        y = 580
                    }
                    if (x == 1070) {
                        x = 160
                        y = 710
                    }
                }

                for (q in 0..3) {
                    val checker = Checker((player + 1), false, Converter().coordinateToCellName(x, y))
                    (container.context as Activity).runOnUiThread {
                        container.addView(CheckerView().draw(container, x, y, (player + 1), false, Converter().coordinateToCellName(x, y)))
                    }
                    checkersOnBoard[Converter().coordinateToCellName(x, y)] = checker
                    x += 260
                }

            }
        }
    }

    /*
     * Draw a highlight shape.
     */

    fun drawHighlightCell(container: FrameLayout, cellName: String) {
        board[cellName]?.setHighlight(true)

        val cell = ImageView(container.context).apply {
            this.tag = cellName + 'h'
            this.setImageResource(R.drawable.highlight)
            this.layoutParams = FrameLayout.LayoutParams(132, 132)
            this.translationX = Converter().cellNameToCoordinate(cellName).first.toFloat() - 11
            this.translationY = Converter().cellNameToCoordinate(cellName).second.toFloat() - 11
        }

        (container.context as Activity).runOnUiThread {
            container.addView(cell)
        }

    }

    /*
     * Check if cell is empty.
     */

    fun checkEmptyCell(pos: String): Boolean {
        return (board[pos]?.getColor() == 0)
    }

    /*
     * Erase the highlight shape.
     */

    fun removeHighlightCell(container: FrameLayout, cellName: String) {
        board[cellName]?.setHighlight(false)

        (container.context as Activity).runOnUiThread {
            container.removeView(container.findViewWithTag<ImageView>(cellName + 'h'))
        }
    }

    /*
     * Erase the default checker and place there queen.
     */

    fun replaceDefaultCheckerToQueen(container: FrameLayout, checkerName: String, x: Int, y: Int): Boolean {
        val checker = checkersOnBoard[checkerName]
        val color = checker?.getColor()

        if (checker?.getColor() == 1) {
            if (checker.getPos().contains('a') && !checker.getQueenInfo()) {
                checker.setQueen(true)
                (container.context as Activity).runOnUiThread {
                    if (color != null) {
                        container.removeView(container.findViewWithTag(checkerName))
                        container.addView(CheckerView().draw(container, x, y, color.toInt(), true, checkerName))
                    }
                }
                return true
            }
        } else if (checker?.getColor() == 2) {
            if (checker.getPos().contains('h') && !checker.getQueenInfo()) {
                checker.setQueen(true)
                (container.context as Activity).runOnUiThread {
                    if (color != null) {
                        container.removeView(container.findViewWithTag(checkerName))
                        container.addView(CheckerView().draw(container, x, y, color.toInt(), true, checkerName))
                    }
                }
                return true
            }
        }
        return false
    }
}