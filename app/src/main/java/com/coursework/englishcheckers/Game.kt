package com.coursework.englishcheckers

import  android.annotation.SuppressLint
import android.app.Activity
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
    private var multiplyBeat = false
    private var needToBeatNow = false
    private var needToBeatMap = mutableMapOf<String, String>()

    private var possibleMoves = mutableMapOf<String ,List<String>>()

    private var neededToMoveList = mutableListOf<String>()


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

                                    isHoldingOnChecker = true
                                    fromX = x
                                    fromY = y

                                    val fromCell = Converter().coordinateToCell(fromX, fromY)

                                    prevCellName = Converter().coordinateToCellName(fromCell.first, fromCell.second)

                                    //We check the need to beat.
                                    defaultCheckerNeedToBeat(checkersOnBoard[prevCellName]?.getColor())

                                    multiplyBeat = needToBeatMap[prevCellName] != null

                                    //We highlight the cells of the moves of the player who walks.
                                    if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                        if (needToBeatMap.isNotEmpty()) {

                                            for (key in needToBeatMap.keys) {


                                                //Highlight the players move.
                                                if (checkersOnBoard[key]?.getColor() == playerTurn) {

                                                    //We highlight the necessary cells for beating.
                                                    if (needToBeatMap[key] != null) {

                                                        //If the selected cell is not already highlighted.
                                                        if (board[needToBeatMap[key]]?.getHighlightInfo() == false) {

                                                            needToBeatMap[key]?.let { Board().setHighlightCell(container, it) }

                                                        }

                                                    }

                                                }

                                            }

                                        } else {
                                            possibleMoves[prevCellName] = possibleMoveForDefaultChecker(prevCellName, playerTurn)
                                            for ((_, moveList) in possibleMoves) {
                                                for (move in moveList) {
                                                    //We highlight the cells of a possible move, if you do not need to beat.
                                                    Board().setHighlightCell(container, move)
                                                }
                                            }

                                        }

                                    }

                                }
                            }

                            MotionEvent.ACTION_MOVE -> {
                                if (isHoldingOnChecker) {

                                    if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                        container.findViewWithTag<ImageView>(prevCellName).translationX =
                                            (event.x - origin)
                                        container.findViewWithTag<ImageView>(prevCellName).translationY =
                                            (event.y - origin)

                                    }
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                if (isHoldingOnChecker ) {

                                    val newPosX = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first
                                    val newPosY = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second
                                    val newCellName = Converter().coordinateToCellName(newPosX, newPosY)

                                    var nextStepCondition = false

                                    //If the players move (1 - red, 2 - white) matches the color of the selected checker.
                                    if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                        //If there are checkers of the walking player in the collection "needToBeat", then it is necessary to make a move of them.
                                        for (key in needToBeatMap.keys) {
                                            if (checkersOnBoard.contains(key)) {
                                                needToBeatNow = true
                                                if (key == prevCellName) {

                                                    nextStepCondition = true

                                                }
                                                //The cell to move to.
                                                needToBeatMap[key]?.let { neededToMoveList.add(it) }
                                            }
                                         }

                                        if (nextStepCondition) {

                                            //If the cell where you need to move to beat matches the cell where we put, then move.
                                            if (neededToMoveList.contains(newCellName) && needToBeatMap[prevCellName] == newCellName) {

                                                val beatenChecker = getCellBetweenTwo(prevCellName, newCellName)

                                                container.findViewWithTag<ImageView>(prevCellName).translationX =
                                                    Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                                container.findViewWithTag<ImageView>(prevCellName).translationY =
                                                    Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                                hasBeat = true
                                                needToBeatMap = mutableMapOf()


                                                //We delete the beaten checker and update the information.
                                                container.removeView(container.findViewWithTag(beatenChecker))
                                                board[beatenChecker]?.setColor(0)
                                                checkersOnBoard.remove(beatenChecker)


                                                changePosToChecker(container, newCellName, prevCellName)
                                                changeCellColor(newCellName, prevCellName)

                                                hasMoved = true

                                            } else {

                                                container.findViewWithTag<ImageView>(prevCellName).translationX =
                                                    Converter().coordinateToCell(fromX, fromY).first.toFloat()
                                                container.findViewWithTag<ImageView>(prevCellName).translationY =
                                                    Converter().coordinateToCell(fromX, fromY).second.toFloat()

                                            }

                                        //If there is no need to beat.
                                        } else if (board[newCellName]?.getHighlightInfo() == true && !needToBeatNow && possibleMoves.containsKey(prevCellName)) {
                                            container.findViewWithTag<ImageView>(prevCellName).translationX =
                                                Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                            container.findViewWithTag<ImageView>(prevCellName).translationY =
                                                Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                            hasBeat = false

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

                                        //After beat the checker, detect new possible beat moves.
                                        if (multiplyBeat && hasBeat) {

                                            defaultCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColor())

                                            if (!needToBeatMap.containsKey(newCellName)) {
                                                needToBeatNow = false
                                            }

                                        }

                                        if (!needToBeatNow) {

                                            if (hasMoved) {

                                                playerTurn = if (playerTurn == 1) 2 else 1

                                                determineWhoseTurn(container)

                                            }
                                        }


                                        hasMoved = false
                                        isHoldingOnChecker = false
                                    }

                                    if (needToBeatNow || hasBeat) {

                                        //If you beat the checker, then we erase the highlight by the special collections.
                                        for (move in neededToMoveList) {

                                            //Remove cell highlight.
                                            Board().removeHighlightCell(container, move)
                                        }
                                    } else {
                                        for ((_, moveList) in possibleMoves) {
                                            for (move in moveList) {
                                                Board().removeHighlightCell(container, move)
                                            }
                                        }
                                        possibleMoves = mutableMapOf()
                                    }
                                    hasBeat = false
                                    needToBeatNow = false
                                    neededToMoveList = mutableListOf()
                                }
                            }
                        }
                    }
                    return true
                }
            })
    }

    /*
     * Check potential further move for default checker.
     */

    private fun potentialFurtherMoveForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): Pair<String, String> {

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
     * Check possible moves for default checker.
     */

    private fun possibleMoveForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): List<String> {
        val result = mutableListOf<String>()
        val possibleMoves = potentialFurtherMoveForDefaultChecker(chosenCheckerCellName, colorChecker)

        if (checkEmptyCell(possibleMoves.first)) result.add(possibleMoves.first)
        if (checkEmptyCell(possibleMoves.second)) result.add(possibleMoves.second)

        return result.toList()
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
            if (checkersOnBoard[checkerOnBoardName]?.getColor() == colorChecker) {

                possibleMoves = potentialFurtherMoveForDefaultChecker(checkerOnBoardName, colorChecker)

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
                                    needToBeatMap[checkerOnBoardName] = move
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
                                    needToBeatMap[checkerOnBoardName] = move
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
                                    needToBeatMap[checkerOnBoardName] = move
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
                                    needToBeatMap[checkerOnBoardName] = move
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
     * Get the cell between two cell names diagonally.
     */

    private fun getCellBetweenTwo(firstCell: String, secondCell: String): String {
        var resultLetter = ' '
        var resultInteger = 0

        val firstLetter = Converter().cellNameSeparate(firstCell).first
        val firstInteger = Converter().cellNameSeparate(firstCell).second

        val secondLetter = Converter().cellNameSeparate(secondCell).first
        val secondInteger = Converter().cellNameSeparate(secondCell).second

        when {

            (firstInteger > secondInteger && (cellToLetter.indexOf(firstLetter) < cellToLetter.indexOf(secondLetter))) -> {
                resultLetter = cellToLetter[cellToLetter.indexOf(firstLetter) + 1]
                resultInteger = firstInteger - 1
            }

            (firstInteger < secondInteger && (cellToLetter.indexOf(firstLetter) < cellToLetter.indexOf(secondLetter))) -> {
                resultLetter = cellToLetter[cellToLetter.indexOf(firstLetter) + 1]
                resultInteger = firstInteger + 1
            }

            (firstInteger > secondInteger && (cellToLetter.indexOf(firstLetter) > cellToLetter.indexOf(secondLetter))) -> {
                resultLetter = cellToLetter[cellToLetter.indexOf(firstLetter) - 1]
                resultInteger = firstInteger - 1
            }

            (firstInteger < secondInteger && (cellToLetter.indexOf(firstLetter) > cellToLetter.indexOf(secondLetter))) -> {
                resultLetter = cellToLetter[cellToLetter.indexOf(firstLetter) - 1]
                resultInteger = firstInteger + 1
            }

        }
        return "$resultLetter$resultInteger"
    }

    /*
     * Check if there was a touch on the checker.
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
        return xIsRightPlace && yIsRightPlace && (checkersOnBoard[Converter().coordinateToCellName(intX, intY)] != null)
    }

    /*
     * Check if there was a touch on board.
     */

    private fun touchOnBoard(x: Int, y: Int): Boolean {
        return (x in borderOfBoardX && y in borderOfBoardY)
    }

    /*
     * Update the checker information in the cell after the move action.
     */

    private fun changeCellColor(newCellName: String, prevCellName: String) {
        checkersOnBoard[newCellName]?.getColor()?.let { board[newCellName]?.setColor(it) }
        board[prevCellName]?.setColor(0)
    }

    /*
     * Update checker position after move action.
     */

    private fun changePosToChecker(container: FrameLayout, newPos: String, prevPos: String) {
        checkersOnBoard[newPos] = checkersOnBoard[prevPos]
        checkersOnBoard[newPos]?.setPos(container, newPos)
        checkersOnBoard.remove(prevPos)

    }

    /*
     * Make turn.
     */

    private fun determineWhoseTurn(container: FrameLayout) {
        val text: TextView = container.findViewById(R.id.turnText)
        if (playerTurn == 1) {
            text.text = (container.context as Activity).getText(R.string.playerTurnOne)
        }

        if (playerTurn == 2) {
            text.text = (container.context as Activity).getText(R.string.playerTurnTwo)
        }

    }
}
