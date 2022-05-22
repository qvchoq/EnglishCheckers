package com.coursework.englishcheckers

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class Game {

    private val borderOfBoardX = 30..1040
    private val borderOfBoardY = 450..1460
    private val boardSize = 910

    private var fromX = 0
    private var fromY = 0
    private var prevCellName = ""

    private var isHoldingOnChecker = false

    private var playerTurn = 1

    private var hasBeat = false
    private var hasMoved = false

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

                                    //println(possibleFurtherMoveForDefaultChecker(prevCellName, checkersOnBoard[prevCellName]?.getColorChecker()))

                                    defaultCheckerNeedToBeat(checkersOnBoard[prevCellName]?.getColorChecker())

                                }
                            }

                            MotionEvent.ACTION_MOVE -> {
                                if (isHoldingOnChecker) {

                                    if (playerTurn == checkersOnBoard[prevCellName]?.getColorChecker()) {

                                        container.findViewWithTag<ImageView>(prevCellName).translationX =
                                            (event.x - origin)
                                        container.findViewWithTag<ImageView>(prevCellName).translationY =
                                            (event.y - origin)

                                    }
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                if (isHoldingOnChecker ) {
                                    println("UP")

                                    val newPosX = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first
                                    val newPosY = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second
                                    val newCellName = Converter().positionToString(newPosX, newPosY)

                                    if (playerTurn == checkersOnBoard[prevCellName]?.getColorChecker()) {

                                        if (checkEmptyCell(newCellName) && board[newCellName]?.getTurnInfo() == true) {

                                            container.findViewWithTag<ImageView>(prevCellName).translationX =
                                                Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                            container.findViewWithTag<ImageView>(prevCellName).translationY =
                                                Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                            if (newCellName != prevCellName) {

                                                changePosToChecker(container, newCellName, prevCellName)
                                                changeCellColor(newCellName, prevCellName)
                                                hasMoved = true
                                            }

                                        } else {
                                            container.findViewWithTag<ImageView>(prevCellName).translationX =
                                                Converter().coordinateToCell(fromX, fromY).first.toFloat()
                                            container.findViewWithTag<ImageView>(prevCellName).translationY =
                                                Converter().coordinateToCell(fromX, fromY).second.toFloat()
                                        }

                                        defaultCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColorChecker())
                                        println("hasBeat : $hasBeat")
                                        if (!hasBeat && hasMoved) {
                                            if (playerTurn == 1) playerTurn = 2 else playerTurn = 1
                                            determineWhoseTurn(container)
                                        }

                                        println("TURN PLAYER : $playerTurn")
                                        hasMoved = false
                                        isHoldingOnChecker = false
                                    }
                                }
                            }
                        }
                    }
                    return true
                }
            })
    }

    /*
     * Check possible further move for default checker.
     */

    private fun possibleFurtherMoveForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): Pair<String, String> {

        val letter = Converter().cellNameSeparate(chosenCheckerCellName).first
        val integer = Converter().cellNameSeparate(chosenCheckerCellName).second

        var posLetter = ' '
        var posIntRight = 0
        var posIntLeft = 0

        when (colorChecker) {

            1 -> {
                if (letter != 'a') {
                    posLetter = cellToLetter[(cellToLetter.indexOf(letter) - 1)]
                    when (integer) {
                        in 2..7 -> {
                            posIntRight = integer + 1
                            posIntLeft = integer - 1
                        }
                        1 -> {
                            posIntRight = integer + 1
                        }
                        8 -> {
                            posIntLeft = integer - 1
                        }
                    }
                }
            }

            2 -> {
                if (letter != 'h') {
                    posLetter = cellToLetter[(cellToLetter.indexOf(letter) + 1)]
                    when (integer) {
                        in 2..7 -> {
                            posIntRight = integer + 1
                            posIntLeft = integer - 1
                        }
                        1 -> {
                            posIntRight = integer + 1
                        }
                        8 -> {
                            posIntLeft = integer - 1
                        }
                    }
                }
            }
        }
        //println("$posLetter$posIntLeft" to "$posLetter$posIntRight")
        return "$posLetter$posIntLeft" to "$posLetter$posIntRight"
    }

    /*
     * Check default checker need to beat.
     */

    private fun defaultCheckerNeedToBeat(colorChecker: Int?) {

        var possibleMoves: Pair<String, String>

        var possibleMoveLetter: Char
        var possibleMoveInteger: Int

        var checkLetter: Char
        var checkInteger: Int

        var move: String

        for (checkerOnBoardName in checkersOnBoard.keys) {

            //For checking checker only who making turn.
            if (checkersOnBoard[checkerOnBoardName]?.getColorChecker() == colorChecker) {

                possibleMoves = possibleFurtherMoveForDefaultChecker(checkerOnBoardName, colorChecker)

                if (colorChecker == 1) {

                    if (!checkerOnBoardName.contains('1') && !checkerOnBoardName.contains('2') && !checkerOnBoardName.contains('a')) {

                        if (board[possibleMoves.first]?.getColorInfo() == 2) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second

                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 1) {

                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"

                                if (board[move]?.getColorInfo() == 0) {
                                    println("(${board[possibleMoves.first]?.getColorInfo()}) NEED TO BEAT left from $checkerOnBoardName to $move")
                                }

                            }
                        }
                    }

                    if (!checkerOnBoardName.contains('7') && !checkerOnBoardName.contains('8') && !checkerOnBoardName.contains('a')) {

                        if (board[possibleMoves.second]?.getColorInfo() == 2) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second

                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 8) {

                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"

                                if (board[move]?.getColorInfo() == 0) {
                                    println("(${board[possibleMoves.second]?.getColorInfo()}) NEED TO BEAT right from $checkerOnBoardName to $move")
                                }

                            }
                        }
                    }
                }

                if (colorChecker == 2) {

                    if (!checkerOnBoardName.contains('1') && !checkerOnBoardName.contains('2') && !checkerOnBoardName.contains('h')) {

                        if (board[possibleMoves.first]?.getColorInfo() == 1) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second

                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 8) {

                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"

                                if (board[move]?.getColorInfo() == 0) {
                                    println("(${board[possibleMoves.first]?.getColorInfo()}) NEED TO BEAT left from $checkerOnBoardName to $move")
                                }

                            }
                        }
                    }

                    if (!checkerOnBoardName.contains('7') && !checkerOnBoardName.contains('8') && !checkerOnBoardName.contains('h')) {

                        if (board[possibleMoves.second]?.getColorInfo() == 1) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second

                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 1) {

                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"

                                if (board[move]?.getColorInfo() == 0) {
                                    println("(${board[possibleMoves.second]?.getColorInfo()}) NEED TO BEAT right from $checkerOnBoardName to $move")
                                }

                            }
                        }
                    }
                }
            }
        }

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
        return xIsRightPlace && yIsRightPlace && (checkersOnBoard[Converter().positionToString(intX, intY)] != null)
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

    private fun changePosToChecker(container: FrameLayout, newPos: String, prevPos: String) {
        checkersOnBoard[newPos] = checkersOnBoard[prevPos]
        checkersOnBoard[newPos]?.setPosChecker(container, newPos)
        checkersOnBoard.remove(prevPos)

    }

    /*
     * Make turn.
     */

    private fun determineWhoseTurn(container: FrameLayout) {
        val text: TextView = container.findViewById(R.id.turnText)
        if (playerTurn == 1) {
            text.text = "Turn Player 1"
        }

        if (playerTurn == 2) {
            text.text = "Turn Player 2"
        }

    }
}
