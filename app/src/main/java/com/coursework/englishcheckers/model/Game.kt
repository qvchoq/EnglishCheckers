package com.coursework.englishcheckers.model

import android.app.Activity
import android.widget.FrameLayout
import android.widget.TextView
import com.coursework.englishcheckers.*
import com.coursework.englishcheckers.model.Converter.Companion.mapCellListX
import com.coursework.englishcheckers.model.Converter.Companion.mapCellListY
import com.coursework.englishcheckers.view.Board
import com.coursework.englishcheckers.view.Board.Companion.board
import com.coursework.englishcheckers.view.Board.Companion.cellToLetter
import com.coursework.englishcheckers.view.Board.Companion.checkersOnBoard

class Game {

    companion object {

        var winner = 0
        var playerTurn = 1
        var needToBeatMap = mutableMapOf<String, MutableList<String>>()

    }

    private val borderOfBoardX = 30..1040
    private val borderOfBoardY = 450..1460

    /*
     * Check potential further move for default checker.
     */

    fun getPotentialFurtherMovesForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): Pair<String, String> {

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
        return "$posLetter$posIntLeft" to "$posLetter$posIntRight"
    }

    /*
     * Check possible moves for default checker.
     */

    fun getPossibleMovesForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): List<String> {
        val result = mutableListOf<String>()
        val potentialMoves = getPotentialFurtherMovesForDefaultChecker(chosenCheckerCellName, colorChecker)

        if (Board().checkEmptyCell(potentialMoves.first)) result.add(potentialMoves.first)
        if (Board().checkEmptyCell(potentialMoves.second)) result.add(potentialMoves.second)

        return result
    }

    /*
     * Check default checker need to beat.
     */

    fun checkDefaultCheckerNeedToBeat(colorChecker: Int?) {

        var possibleMoves: Pair<String, String>

        var possibleMoveLetter: Char
        var possibleMoveInteger: Int

        var checkLetter: Char
        var checkInteger: Int

        var move: String

        for (checkerOnBoardName in checkersOnBoard.keys) {

            //For checker only who making turn.
            if (checkersOnBoard[checkerOnBoardName]?.getColor() == colorChecker) {

                possibleMoves = getPotentialFurtherMovesForDefaultChecker(checkerOnBoardName, colorChecker)

                    if (colorChecker == 1) {
                        //If on a possible move, in front of player 1 checker, there is player 2 checker.
                        if (board[possibleMoves.first]?.getColor() == 2) {
                            //possibleMoves.first - left move.
                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second
                            //If the checker is not on the leftmost cell, in the top row.
                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 1) {
                                //Calculate the possible move to beat
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"
                                //If the calculated cell is empty.
                                addToNeedToBeatMap(move, checkerOnBoardName)
                            }
                        }
                        if (board[possibleMoves.second]?.getColor() == 2) {
                            //possibleMoves.second - right move.
                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second
                            //If the checker is not on the rightmost cell, in the top row.
                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 8) {
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"
                                addToNeedToBeatMap(move, checkerOnBoardName)
                            }
                        }
                    } else {
                        if (board[possibleMoves.first]?.getColor() == 1) {
                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second
                            //If the checker is not on the leftmost cell, in the bottom row.
                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 1) {
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"
                                addToNeedToBeatMap(move, checkerOnBoardName)
                            }
                        }
                        if (board[possibleMoves.second]?.getColor() == 1) {
                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second
                            //If the checker is not on the rightmost cell, in the bottom row.
                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 8) {
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"
                                addToNeedToBeatMap(move, checkerOnBoardName)
                            }
                        }
                    }
                }
        }
    }

    /*
     * Check potential further move for default checker.
     */

    fun getPotentialFurtherMovesForQueen(chosenQueenCheckerCellName: String): List<String> {

        val potentialMoves = mutableListOf<String>()
        val result = mutableListOf<String>()

        val letter = Converter().cellNameSeparate(chosenQueenCheckerCellName).first
        val integer = Converter().cellNameSeparate(chosenQueenCheckerCellName).second

        var posLetter: Char
        val posIntRight = integer + 1
        val posIntLeft = integer - 1

        if (letter != 'a') {
            posLetter = cellToLetter[(cellToLetter.indexOf(letter) - 1)]

            potentialMoves.add("$posLetter$posIntLeft")
            potentialMoves.add("$posLetter$posIntRight")
        }

        if (letter != 'h') {
            posLetter = cellToLetter[(cellToLetter.indexOf(letter) + 1)]

            potentialMoves.add("$posLetter$posIntLeft")
            potentialMoves.add("$posLetter$posIntRight")
        }

        for (move in potentialMoves) {
            if (board[move] != null) {
                result.add(move)
            }
        }
        return result
    }

    /*
     * Check possible moves for queen checker.
     */

    fun getPossibleMovesForQueen(chosenCheckerCellName: String): List<String> {

        val result = mutableListOf<String>()
        val potentialMoves = getPotentialFurtherMovesForQueen(chosenCheckerCellName)

        for (move in potentialMoves) {
            if (Board().checkEmptyCell(move)) {
                result.add(move)
            }
        }

        return result
    }

    /*
     * Check queen checker need to beat.
     */

    fun checkQueenNeedToBeat(colorChecker: Int?) {

        //Player queens on board
        val queensOnBoard = mutableListOf<String>()

        var queenLetter: Char
        var queenInteger: Int

        var potentialMoves: List<String>

        var possibleMoveLetter: Char
        var possibleMoveInteger: Int

        var checkLetter: Char
        var checkInteger: Int

        var move: String

        for (checkerName in checkersOnBoard.keys) {
            if (checkersOnBoard[checkerName]?.getQueenInfo() == true && checkersOnBoard[checkerName]?.getColor() == colorChecker) {
                queensOnBoard.add(checkerName)
            }
        }

        for (queenName in queensOnBoard) {

                potentialMoves = getPotentialFurtherMovesForQueen(queenName)

                queenLetter = Converter().cellNameSeparate(queenName).first
                queenInteger = Converter().cellNameSeparate(queenName).second

                        //If on a possible move, in front of player checker, there is enemy checker
                        for (potMove in potentialMoves) {
                            //If potential move is enemy checker
                            if (board[potMove]?.getColor() != colorChecker && board[potMove]?.getColor() != 0) {

                                possibleMoveLetter = Converter().cellNameSeparate(potMove).first
                                possibleMoveInteger = Converter().cellNameSeparate(potMove).second
                                //If you need to beat left up
                                if (possibleMoveLetter != 'a' && possibleMoveInteger != 1) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) < cellToLetter.indexOf(queenLetter) && possibleMoveInteger < queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                        checkInteger = possibleMoveInteger - 1
                                        move = "$checkLetter$checkInteger"
                                        addToNeedToBeatMap(move, queenName)
                                    }
                                }
                                //If you need to beat right up
                                if (possibleMoveLetter != 'a' && possibleMoveInteger != 8) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) < cellToLetter.indexOf(queenLetter) && possibleMoveInteger > queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                        checkInteger = possibleMoveInteger + 1
                                        move = "$checkLetter$checkInteger"
                                        addToNeedToBeatMap(move, queenName)
                                    }
                                }
                                //If you need to beat left down
                                if (possibleMoveLetter != 'h' && possibleMoveInteger != 1) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) > cellToLetter.indexOf(queenLetter) && possibleMoveInteger < queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                        checkInteger = possibleMoveInteger - 1
                                        move = "$checkLetter$checkInteger"
                                        addToNeedToBeatMap(move, queenName)
                                    }
                                }
                                //If you need to beat right down
                                if (possibleMoveLetter != 'h' && possibleMoveInteger != 8) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) > cellToLetter.indexOf(queenLetter) && possibleMoveInteger > queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                        checkInteger = possibleMoveInteger + 1
                                        move = "$checkLetter$checkInteger"
                                        addToNeedToBeatMap(move, queenName)
                                    }
                                }
                            }
                        }
                }

    }

    /*
     * Add need to beat move in map.
     */

    private fun addToNeedToBeatMap(move: String, checker: String) {
        if (board[move]?.getColor() == 0) {
            if (needToBeatMap[checker].isNullOrEmpty()) {
                needToBeatMap[checker] = mutableListOf(move)
            } else if (needToBeatMap[checker]?.contains(move) == false) {
                needToBeatMap[checker]!!.add(move)
            }
        }
    }

    /*
     * Get the cell between two cell names diagonally.
     */

    fun getCellBetweenTwo(firstCell: String, secondCell: String): String {
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
     * Check if there was a touch on the player turn checker.
     */

    fun touchOnChecker(x: Int, y: Int): Boolean {

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

        val resultName: String = Converter().coordinateToCellName(intX, intY)

        return xIsRightPlace &&
                yIsRightPlace &&
                (checkersOnBoard[resultName] != null) &&
                (checkersOnBoard[resultName]?.getColor() == playerTurn)
    }

    /*
     * Check if there was a touch on board.
     */

    fun touchOnBoard(x: Int, y: Int): Boolean {
        return (x in borderOfBoardX && y in borderOfBoardY)
    }

    /*
     * Update the checker information in the cell after the move action.
     */

    fun changeCellColor(newCellName: String, prevCellName: String) {
        checkersOnBoard[newCellName]?.getColor()?.let { board[newCellName]?.setColor(it) }
        board[prevCellName]?.setColor(0)
    }

    /*
     * Update checker position after move action.
     */

    fun changePosToChecker(container: FrameLayout, newPos: String, prevPos: String) {
        checkersOnBoard[newPos] = checkersOnBoard[prevPos]
        checkersOnBoard[newPos]?.setPos(container, newPos)
        checkersOnBoard.remove(prevPos)

    }

    /*
     * Make turn.
     */

    fun determineWhoseTurn(container: FrameLayout) {
        val text: TextView = container.findViewById(R.id.turnText)
        if (playerTurn == 1) {
            text.text = (container.context as Activity).getText(R.string.playerTurnOne)
        }

        if (playerTurn == 2) {
            text.text = (container.context as Activity).getText(R.string.playerTurnTwo)
        }

    }

    /*
    * Create cells with information on board once.
    */

    fun placeCellsOnBoard() {
        var nameCell: String
        var colorChecker: Int
        for (x in 0..7) {
            for (y in 0..7) {
                nameCell = "${cellToLetter[y]}${x + 1}"

                if (checkersOnBoard[nameCell]?.getPos()  == nameCell) {
                    colorChecker = checkersOnBoard[nameCell]!!.getColor()
                } else {
                    colorChecker = 0
                }

                board[nameCell] = BoardCell(colorChecker, false)

            }
        }
    }

    /*
     * Making game over with winner by beating all enemy checkers.
     */

    fun endGameWithWinnerByBeatingAllCheckers(): Boolean {

        var winPlayerOne = false
        var winPlayerTwo = false

        for (checker in checkersOnBoard) {
            if (checker.value?.getColor() == 1) {
                winPlayerOne = true
            }
            if (checker.value?.getColor() == 2) {
                winPlayerTwo = true
            }
        }

        if (winPlayerOne && !winPlayerTwo || !winPlayerOne && winPlayerTwo) {

            if (winPlayerOne) {
                winner = 1
            }
            if (winPlayerTwo) {
                winner = 2
            }
        }

        return (winPlayerOne && !winPlayerTwo || !winPlayerOne && winPlayerTwo)
    }

    /*
     * Making game over by no possible moves for one player.
     */

    fun endGameWithWinnerByNoPossibleMoves(checkPlayer: Int): Boolean {

        val allPossibleMovesForPlayer = mutableListOf<String>()

        val player = if (playerTurn == 1) 2 else 1

        for (checkerName in checkersOnBoard.keys) {
            if (checkersOnBoard[checkerName]?.getColor() == checkPlayer) {
                if (checkersOnBoard[checkerName]?.getQueenInfo() == true) {
                    allPossibleMovesForPlayer += getPossibleMovesForQueen(checkerName)
                } else {
                    allPossibleMovesForPlayer += getPossibleMovesForDefaultChecker(checkerName, checkPlayer)
                }
            }
        }

        if (allPossibleMovesForPlayer.isEmpty()) {
            winner = player
        }

        return (allPossibleMovesForPlayer.isEmpty())
    }

    /*
     * Reset all variables.
     */

    fun clearAllData() {
        winner = 0
        playerTurn = 1
        needToBeatMap = mutableMapOf()
        board = mutableMapOf()
        checkersOnBoard = mutableMapOf()
    }

}
