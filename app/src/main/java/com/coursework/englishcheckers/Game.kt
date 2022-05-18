package com.coursework.englishcheckers

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

class Game {

    private val borderOfBoardX = 30..1040
    private val borderOfBoardY = 450..1460
    private val boardSize = 910

    private var fromX = 0
    private var fromY = 0
    private var prevCellName = ""

    private var isHoldingOnChecker = false

    /*
     * Move checker and do a turn by player.
     */

    @SuppressLint("ClickableViewAccessibility")
    fun makePlayerTurn(container: FrameLayout) {
        var x: Int
        var y: Int

        val origin = (1040 - boardSize) / 2f

        container.setOnTouchListener(
            object: View.OnTouchListener {

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    x = (event!!.x).toInt()
                    y = (event.y).toInt()

                    if (touchOnBoard(x, y)) {
                        when (event.action) {

                            MotionEvent.ACTION_DOWN -> {
                                if (touchOnChecker(x, y)) {
                                    println("DOWN")

                                    isHoldingOnChecker = true
                                    fromX = x
                                    fromY = y
                                    val fromCell = Converter().coordinateToCell(fromX, fromY)

                                    prevCellName = Converter().positionToString(fromCell.first, fromCell.second)

                                }
                            }

                            MotionEvent.ACTION_MOVE -> {
                                if (isHoldingOnChecker) {
                                    println("MOVE")

                                    container.findViewWithTag<ImageView>(prevCellName).translationX = (event.x - origin)
                                    container.findViewWithTag<ImageView>(prevCellName).translationY = (event.y - origin)
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                if (isHoldingOnChecker ) {
                                    println("UP")

                                    val newPosX = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first
                                    val newPosY = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second
                                    val newCellName = Converter().positionToString(newPosX, newPosY)

                                    if (checkEmptyCell(newCellName) && board[newCellName]?.getTurnInfo() == true) {

                                        container.findViewWithTag<ImageView>(prevCellName).translationX =
                                            Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                        container.findViewWithTag<ImageView>(prevCellName).translationY =
                                            Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                        if (newCellName != prevCellName) {

                                            changePosToChecker(container, newCellName, prevCellName)
                                            changeCellColor(newCellName, prevCellName)

                                        }
                                    } else {
                                        container.findViewWithTag<ImageView>(prevCellName).translationX = Converter().coordinateToCell(fromX, fromY).first.toFloat()
                                        container.findViewWithTag<ImageView>(prevCellName).translationY = Converter().coordinateToCell(fromX, fromY).second.toFloat()
                                    }
                                    isHoldingOnChecker = false
                                }
                            }
                        }
                    }
                    return true
                }
            })
    }

    /*
     * Check if cell is empty.
     */

    private fun checkEmptyCell(pos: String): Boolean {
        return (board[pos]?.getColorInfo() == 0)
    }

    /*
     * Check if touch was on checker
     */

    private fun touchOnChecker(x: Int, y: Int): Boolean {
        var intX = 0
        var intY = 0
        var xIsRightPlace = false
        var yIsRightPlace = false
        var clickOnChecker = false


        for ((key, value) in mapCellListX) {
            if (x in key) {
                xIsRightPlace = true
                intX = value

            }
        }
        for ((key, value) in mapCellListY) {
            if (y in key) {
                yIsRightPlace = true
                intY = value

            }
        }
        clickOnChecker = xIsRightPlace && yIsRightPlace && (checkersOnBoard[Converter().positionToString(intX, intY)] != null)
        return clickOnChecker
    }

    /*
     * Check if touch was on board
     */

    private fun touchOnBoard(x: Int, y: Int): Boolean {
        return (x in borderOfBoardX && y in borderOfBoardY)
    }

    /*
     * Update info about checker on cell after move action.
     */

    private fun changeCellColor(newCellName: String, prevCellName: String) {
        checkersOnBoard[newCellName]?.getColorChecker()?.let { board[newCellName]?.setColor(it) }
        board[prevCellName]?.setColor(0)
    }

    /*
     * Update checker position after move action.
     */

    private fun changePosToChecker(container: FrameLayout,newPos: String, prevPos: String) {
        checkersOnBoard[newPos] = checkersOnBoard[prevPos]
        checkersOnBoard[newPos]?.setPosChecker(container, newPos)
        checkersOnBoard.remove(prevPos)

    }
}
