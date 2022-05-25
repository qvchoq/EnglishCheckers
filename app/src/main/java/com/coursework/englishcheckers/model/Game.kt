package com.coursework.englishcheckers.model

import  android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.coursework.englishcheckers.*
import com.coursework.englishcheckers.view.Board
import com.coursework.englishcheckers.view.board
import com.coursework.englishcheckers.view.cellToLetter
import com.coursework.englishcheckers.view.checkersOnBoard

class Game {

    private val borderOfBoardX = 30..1040
    private val borderOfBoardY = 450..1460
    private val boardSize = 910

    private var fromX = 0
    private var fromY = 0
    private var prevCellName = ""

    private var isHoldingOnChecker = false

    private var playerTurn = 1

    private var turnsWithoutBeating = 0

    private var hasBeat = false
    private var hasMoved = false
    private var needToBeatNow = false
    private var replacedToQueen = false

    private var needToBeatMap = mutableMapOf<String, String>()

    private var possibleMoves = mutableMapOf<String ,List<String>>()

    private var mustToMoveList = mutableListOf<String>()


    /*
     * Move checker and do a turn by player.
     */

    @SuppressLint("ClickableViewAccessibility")
    fun mainMoveLogic(container: FrameLayout) {

        var x: Int
        var y: Int

        val origin = (1040 - boardSize) / 2f

        container.setOnTouchListener { _, event ->
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
                            queenCheckerNeedToBeat(checkersOnBoard[prevCellName]?.getColor())

                            //We highlight the cells of the moves of the player whose turn is.
                            if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                if (needToBeatMap.isNotEmpty()) {

                                    for (key in needToBeatMap.keys) {

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
                                    if (checkersOnBoard[prevCellName]?.getQueenInfo() == false) {
                                        possibleMoves[prevCellName] =
                                            possibleMovesForDefaultChecker(prevCellName, playerTurn)
                                    } else {
                                        possibleMoves[prevCellName] =
                                            possibleMovesForQueenChecker(prevCellName)
                                    }
                                    for ((_, moveList) in possibleMoves) {
                                        for (move in moveList) {
                                            //We highlight the cells of a possible move, if you do not need to beat.
                                            if (checkersOnBoard[prevCellName]?.getColor() == playerTurn) {
                                                Board().setHighlightCell(container, move)
                                            }
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
                        if (isHoldingOnChecker) {

                            val newPosX = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first
                            val newPosY = Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second
                            val newCellName = Converter().coordinateToCellName(newPosX, newPosY)

                            var beatCondition = false

                            //If the players move (1 - red, 2 - white) matches the color of the selected checker.
                            if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                //If there are checkers of the walking player in the collection "needToBeat", then it is necessary to make a move of them.
                                for (key in needToBeatMap.keys) {
                                    if (checkersOnBoard.contains(key)) {
                                        needToBeatNow = true
                                        if (key == prevCellName) {

                                            beatCondition = true

                                        }
                                        //The cell to move to.
                                        needToBeatMap[key]?.let { mustToMoveList.add(it) }
                                    }
                                }

                                if (beatCondition) {

                                    //If the cell where you need to move to beat matches the cell where we put, then move.
                                    if (mustToMoveList.contains(newCellName) && needToBeatMap[prevCellName] == newCellName) {

                                        val beatenChecker = getCellBetweenTwo(prevCellName, newCellName)

                                        container.findViewWithTag<ImageView>(prevCellName).translationX =
                                            Converter().coordinateToCell(
                                                event.x.toInt(),
                                                event.y.toInt()
                                            ).first.toFloat()
                                        container.findViewWithTag<ImageView>(prevCellName).translationY =
                                            Converter().coordinateToCell(
                                                event.x.toInt(),
                                                event.y.toInt()
                                            ).second.toFloat()

                                        hasBeat = true
                                        turnsWithoutBeating = 0
                                        needToBeatMap = mutableMapOf()


                                        //We delete the beaten checker and update the information.
                                        container.removeView(container.findViewWithTag(beatenChecker))
                                        board[beatenChecker]?.setColor(0)
                                        checkersOnBoard.remove(beatenChecker)


                                        changePosToChecker(container, newCellName, prevCellName)
                                        changeCellColor(newCellName, prevCellName)

                                        //Replace default checker to queen.
                                        if (checkersOnBoard[newCellName]?.getColor() == 1) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('a') == true) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true

                                            }
                                        }

                                        if (checkersOnBoard[newCellName]?.getColor() == 2) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('h') == true) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true
                                            }
                                        }


                                        hasMoved = true

                                    } else {

                                        container.findViewWithTag<ImageView>(prevCellName).translationX =
                                            Converter().coordinateToCell(fromX, fromY).first.toFloat()
                                        container.findViewWithTag<ImageView>(prevCellName).translationY =
                                            Converter().coordinateToCell(fromX, fromY).second.toFloat()

                                    }

                                    //If there is no need to beat.
                                } else if (board[newCellName]?.getHighlightInfo() == true && !needToBeatNow && possibleMoves.containsKey(
                                        prevCellName
                                    )
                                ) {
                                    container.findViewWithTag<ImageView>(prevCellName).translationX =
                                        Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                    container.findViewWithTag<ImageView>(prevCellName).translationY =
                                        Converter().coordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                    hasBeat = false
                                    turnsWithoutBeating++

                                    if (newCellName != prevCellName) {

                                        changePosToChecker(container, newCellName, prevCellName)
                                        changeCellColor(newCellName, prevCellName)

                                        //Replace default checker to queen.
                                        if (checkersOnBoard[newCellName]?.getColor() == 1) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('a') == true) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true
                                            }
                                        }

                                        if (checkersOnBoard[newCellName]?.getColor() == 2) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('h') == true) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true
                                            }
                                        }

                                        hasMoved = true

                                    }

                                } else {

                                    container.findViewWithTag<ImageView>(prevCellName).translationX =
                                        Converter().coordinateToCell(fromX, fromY).first.toFloat()
                                    container.findViewWithTag<ImageView>(prevCellName).translationY =
                                        Converter().coordinateToCell(fromX, fromY).second.toFloat()
                                }

                                //After beat the checker, detect new possible beat moves.
                                if (hasBeat && !replacedToQueen) {

                                    if (checkersOnBoard[newCellName]?.getQueenInfo() == false) {
                                        defaultCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColor())
                                    } else {
                                        queenCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColor())
                                    }

                                    if (!needToBeatMap.containsKey(newCellName) || replacedToQueen) {
                                        needToBeatNow = false
                                    }

                                }

                                if (!needToBeatNow || replacedToQueen) {
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
                                for (move in mustToMoveList) {

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
                            replacedToQueen = false
                            mustToMoveList = mutableListOf()

                            //Calculate game end.

                            endGameWithWinnerByBeatingAllCheckers()

                            if (turnsWithoutBeating >= 30) {
                                endGameWithDraw()
                            }

                        }
                    }
                }
            }
            true
        }
    }

    /*
     * Check potential further move for default checker.
     */

    private fun potentialFurtherMovesForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): Pair<String, String> {

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

    private fun possibleMovesForDefaultChecker(chosenCheckerCellName: String, colorChecker: Int?): List<String> {
        val result = mutableListOf<String>()
        val possibleMoves = potentialFurtherMovesForDefaultChecker(chosenCheckerCellName, colorChecker)

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

            //For checker only who making turn.
            if (checkersOnBoard[checkerOnBoardName]?.getColor() == colorChecker) {

                possibleMoves = potentialFurtherMovesForDefaultChecker(checkerOnBoardName, colorChecker)

                if (colorChecker == 1) {
                    //If the checker is on the two leftmost cells, in the top row
                    if (!checkerOnBoardName.contains('1') && !checkerOnBoardName.contains('2') && !checkerOnBoardName.contains('a')) {
                        //If on a possible move, in front of player 1 checker, there is player 2 checker
                        if (board[possibleMoves.first]?.getColorInfo() == 2) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second
                            //If player 2 checker is not on the left or and top
                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 1) {
                                //Calculate the possible move to beat
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"
                                //If the calculated cell is empty
                                if (board[move]?.getColorInfo() == 0) {
                                    needToBeatMap[checkerOnBoardName] = move
                                }

                            }
                        }
                    }
                    //If the checker is on the two rightmost cells, in the top row
                    if (!checkerOnBoardName.contains('7') && !checkerOnBoardName.contains('8') && !checkerOnBoardName.contains('a')) {
                        //If on a possible move, in front of player 1 checker, there is player 2 checker
                        if (board[possibleMoves.second]?.getColorInfo() == 2) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second
                            //If player 2 checker is not on the right and top
                            if (possibleMoveLetter != 'a' && possibleMoveInteger != 8) {
                                //Calculate the possible move to beat
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"
                                //If the calculated cell is empty
                                if (board[move]?.getColorInfo() == 0) {
                                    needToBeatMap[checkerOnBoardName] = move
                                }

                            }
                        }
                    }
                }

                if (colorChecker == 2) {
                    //If the checker is on the two leftmost cells, in the bottom row
                    if (!checkerOnBoardName.contains('1') && !checkerOnBoardName.contains('2') && !checkerOnBoardName.contains('h')) {
                        //If on a possible move, in front of player 2 checker, there is player 1 checker
                        if (board[possibleMoves.first]?.getColorInfo() == 1) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.first).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.first).second
                            //If player 2 checker is not on the right and bottom
                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 8) {
                                //Calculate the possible move to beat
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger - 1
                                move = "$checkLetter$checkInteger"
                                //If the calculated cell is empty
                                if (board[move]?.getColorInfo() == 0) {
                                    needToBeatMap[checkerOnBoardName] = move
                                }

                            }
                        }
                    }
                    //If the checker is on the two rightmost cells, in the bottom row
                    if (!checkerOnBoardName.contains('7') && !checkerOnBoardName.contains('8') && !checkerOnBoardName.contains('h')) {
                        //If on a possible move, in front of player 2 checker, there is player 1 checker
                        if (board[possibleMoves.second]?.getColorInfo() == 1) {

                            possibleMoveLetter = Converter().cellNameSeparate(possibleMoves.second).first
                            possibleMoveInteger = Converter().cellNameSeparate(possibleMoves.second).second
                            //If player 2 checker is not on the left and bottom
                            if (possibleMoveLetter != 'h' && possibleMoveInteger != 1) {
                                //Calculate the possible move to beat
                                checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                checkInteger = possibleMoveInteger + 1
                                move = "$checkLetter$checkInteger"
                                //If the calculated cell is empty
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
     * Check potential further move for default checker.
     */

    private fun potentialFurtherMovesForQueenChecker(chosenQueenCheckerCellName: String): List<String> {

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

    private fun possibleMovesForQueenChecker(chosenCheckerCellName: String): List<String> {

        val result = mutableListOf<String>()
        val possibleMoves = potentialFurtherMovesForQueenChecker(chosenCheckerCellName)

        for (move in possibleMoves) {
            if (checkEmptyCell(move)) {
                result.add(move)
            }
        }

        return result.toList()
    }

    /*
     * Check queen checker need to beat.
     */

    private fun queenCheckerNeedToBeat(colorChecker: Int?) {

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

                potentialMoves = potentialFurtherMovesForQueenChecker(queenName)

                queenLetter = Converter().cellNameSeparate(queenName).first
                queenInteger = Converter().cellNameSeparate(queenName).second

                        //If on a possible move, in front of player checker, there is enemy checker
                        for (potMove in potentialMoves) {
                            //If potential move is enemy checker
                            if (board[potMove]?.getColorInfo() != colorChecker && board[potMove]?.getColorInfo() != 0) {

                                possibleMoveLetter = Converter().cellNameSeparate(potMove).first
                                possibleMoveInteger = Converter().cellNameSeparate(potMove).second
                                //If you need to beat left up
                                if (possibleMoveLetter != 'a' && possibleMoveInteger != 1) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) < cellToLetter.indexOf(queenLetter) && possibleMoveInteger < queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                        checkInteger = possibleMoveInteger - 1
                                        move = "$checkLetter$checkInteger"
                                        if (board[move]?.getColorInfo() == 0) {
                                            needToBeatMap[queenName] = move
                                        }
                                    }
                                }
                                //If you need to beat right up
                                if (possibleMoveLetter != 'a' && possibleMoveInteger != 8) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) < cellToLetter.indexOf(queenLetter) && possibleMoveInteger > queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) - 1]
                                        checkInteger = possibleMoveInteger + 1
                                        move = "$checkLetter$checkInteger"
                                        if (board[move]?.getColorInfo() == 0) {
                                            needToBeatMap[queenName] = move
                                        }
                                    }
                                }
                                //If you need to beat left down
                                if (possibleMoveLetter != 'h' && possibleMoveInteger != 1) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) > cellToLetter.indexOf(queenLetter) && possibleMoveInteger < queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                        checkInteger = possibleMoveInteger - 1
                                        move = "$checkLetter$checkInteger"
                                        if (board[move]?.getColorInfo() == 0) {
                                            needToBeatMap[queenName] = move
                                        }
                                    }
                                }
                                //If you need to beat right down
                                if (possibleMoveLetter != 'h' && possibleMoveInteger != 8) {
                                    if (cellToLetter.indexOf(possibleMoveLetter) > cellToLetter.indexOf(queenLetter) && possibleMoveInteger > queenInteger) {
                                        checkLetter = cellToLetter[cellToLetter.indexOf(possibleMoveLetter) + 1]
                                        checkInteger = possibleMoveInteger + 1
                                        move = "$checkLetter$checkInteger"
                                        if (board[move]?.getColorInfo() == 0) {
                                            needToBeatMap[queenName] = move
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
     * Check if there was a touch on the player turn checker.
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
        return xIsRightPlace &&
                yIsRightPlace &&
                (checkersOnBoard[Converter().coordinateToCellName(intX, intY)] != null) &&
                (checkersOnBoard[Converter().coordinateToCellName(intX, intY)]?.getColor() == playerTurn)
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

    /*
    * Create cells with information on board once.
    */

    fun placeCellsOnBoard() {
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
     * Making game over with winner by beating all enemy checkers.
     *
     * 0 - No win & No draw
     * 1 - Win Player 1
     * 2 - Win Player 2
     *
     */

    private fun endGameWithWinnerByBeatingAllCheckers(): Int {

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
                println("Player 1 is win! (By Beating all checkers)")
                //Dialog box - first player win
            }
            if (winPlayerTwo) {
                println("Player 2 is win! (By Beating all checkers)")
                //Dialog box - second player win
            }

        }
        return 0
    }

    /*
     * Making game over by no possible moves for one player.
     */

    private fun endGameWithWinnerByNoPossibleMoves() {

        var moveIsPossible = true

        for (checkerName in checkersOnBoard.keys) {
            if (checkersOnBoard[checkerName]?.getColor() == playerTurn) {
                if (checkersOnBoard[checkerName]?.getQueenInfo() == true) {
                    moveIsPossible = possibleMovesForQueenChecker(checkerName).isNotEmpty()
                } else {
                    moveIsPossible = possibleMovesForDefaultChecker(checkerName, playerTurn).isNotEmpty()
                }
            }
        }

        if (!moveIsPossible) {
            println("Player $playerTurn is win! (By Beating no Possible moves)")
            //Dialog box - $playerTurn player winner.
        }
    }

    /*
     * Making game over with draw.
     */

    private fun endGameWithDraw() {
        println("End game! Draw.")
        //Popup Dialog Box with "Restart" and "Menu" buttons.
    }


}
