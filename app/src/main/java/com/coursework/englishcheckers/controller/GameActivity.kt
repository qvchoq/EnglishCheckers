package com.coursework.englishcheckers.controller

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.coursework.englishcheckers.R
import com.coursework.englishcheckers.model.*
import com.coursework.englishcheckers.model.Game.Companion.needToBeatMap
import com.coursework.englishcheckers.model.Game.Companion.playerTurn
import com.coursework.englishcheckers.model.Game.Companion.winner
import com.coursework.englishcheckers.view.Board
import com.coursework.englishcheckers.view.Board.Companion.board
import com.coursework.englishcheckers.view.Board.Companion.checkersOnBoard

class GameActivity : AppCompatActivity() {

    private val boardSize = 910

    private var fromX = 0
    private var fromY = 0
    private var prevCellName = ""

    private var isHoldingOnChecker = false

    private var turnsWithoutBeating = 0

    private var hasBeat = false
    private var hasMoved = false
    private var needToBeatNow = false
    private var replacedToQueen = false


    private var possibleMoves = mutableMapOf<String ,List<String>>()

    private var mustToMoveList = mutableListOf<String>()

    /*
     * Entry point in GameActivity.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val container: FrameLayout = findViewById(R.id.container)
        Board().drawCheckers(container)
        Game().placeCellsOnBoard()
        mainMoveLogic(container)
    }

    /*
     * On back pressed, showing dialog with warning.
     */

    override fun onBackPressed() {
        showDialogBackPressed()
    }

    /*
     * Move checker by touching the screen.
     */

    @SuppressLint("ClickableViewAccessibility")
    fun mainMoveLogic(container: FrameLayout) {
        var x: Int
        var y: Int

        val origin = (1040 - boardSize) / 2f

        container.setOnTouchListener { _, event ->
            x = (event!!.x).toInt()
            y = (event.y).toInt()

            if (Game().touchOnBoard(x, y)) {
                when (event.action) {

                    MotionEvent.ACTION_DOWN -> {

                        if (Game().touchOnChecker(x, y)) {
                            isHoldingOnChecker = true
                            fromX = x
                            fromY = y

                            val fromCell = Converter().coordinateToCell(fromX, fromY)

                            prevCellName = Converter().coordinateToCellName(fromCell.first, fromCell.second)

                            //We check the need to beat.
                            Game().checkDefaultCheckerNeedToBeat(checkersOnBoard[prevCellName]?.getColor())
                            Game().checkQueenCheckerNeedToBeat(checkersOnBoard[prevCellName]?.getColor())

                            //We highlight the cells of the moves of the player whose turn is.
                            if (playerTurn == checkersOnBoard[prevCellName]?.getColor()) {

                                if (needToBeatMap.isNotEmpty()) {

                                    for (key in needToBeatMap.keys) {

                                        if (checkersOnBoard[key]?.getColor() == playerTurn) {

                                            //We highlight the necessary cells for beating.
                                            if (needToBeatMap[key] != null) {

                                                //If the selected cell is not already highlighted.
                                                for (value in needToBeatMap[key]!!) {
                                                    if (board[value]?.getHighlightInfo() == false) {
                                                        Board().setHighlightCell(container, value)
                                                    }
                                                }

                                            }

                                        }

                                    }

                                } else {
                                    if (checkersOnBoard[prevCellName]?.getQueenInfo() == false) {
                                        possibleMoves[prevCellName] =
                                            Game().getPossibleMovesForDefaultChecker(prevCellName, playerTurn)
                                    } else {
                                        possibleMoves[prevCellName] =
                                            Game().getPossibleMovesForQueenChecker(prevCellName)
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
                                container.findViewWithTag<ImageView>(prevCellName).translationZ = 3F
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isHoldingOnChecker) {

                            container.findViewWithTag<ImageView>(prevCellName).translationZ = 1F

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
                                        for (value in needToBeatMap[key]!!) {
                                            mustToMoveList.add(value)
                                        }
                                    }
                                }

                                if (beatCondition) {

                                    //If the cell where you need to move to beat matches the cell where we put, then move.
                                    if (mustToMoveList.contains(newCellName) && needToBeatMap[prevCellName]?.contains(newCellName) == true) {

                                        val beatenChecker =  Game().getCellBetweenTwo(prevCellName, newCellName)

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


                                        Game().changePosToChecker(container, newCellName, prevCellName)
                                        Game().changeCellColor(newCellName, prevCellName)

                                        //Replace default checker to queen.
                                        if (checkersOnBoard[newCellName]?.getColor() == 1) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('a') == true &&
                                                checkersOnBoard[newCellName]?.getQueenInfo() == false) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true

                                            }
                                        }

                                        if (checkersOnBoard[newCellName]?.getColor() == 2) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('h') == true &&
                                                checkersOnBoard[newCellName]?.getQueenInfo() == false) {
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

                                        Game().changePosToChecker(container, newCellName, prevCellName)
                                        Game().changeCellColor(newCellName, prevCellName)

                                        //Replace default checker to queen.
                                        if (checkersOnBoard[newCellName]?.getColor() == 1) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('a') == true &&
                                                checkersOnBoard[newCellName]?.getQueenInfo() == false) {
                                                checkersOnBoard[newCellName]?.setQueen(true)
                                                Board().replaceDefaultCheckerToQueen(container, newCellName, newPosX, newPosY)
                                                replacedToQueen = true
                                            }
                                        }

                                        if (checkersOnBoard[newCellName]?.getColor() == 2) {
                                            if (checkersOnBoard[newCellName]?.getPos()?.contains('h') == true &&
                                                checkersOnBoard[newCellName]?.getQueenInfo() == false) {
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
                                        Game().checkDefaultCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColor())
                                    } else {
                                        Game().checkQueenCheckerNeedToBeat(checkersOnBoard[newCellName]?.getColor())
                                    }

                                    if (!needToBeatMap.containsKey(newCellName) || replacedToQueen) {
                                        needToBeatNow = false
                                    }

                                }

                                if (!needToBeatNow || replacedToQueen) {
                                    if (hasMoved) {
                                        playerTurn = if (playerTurn == 1) 2 else 1
                                        Game().determineWhoseTurn(container)
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
                            endGame()
                        }
                    }

                }
            }
            true
        }
    }

    /*
     * Show dialog box on back pressed.
     */

    private fun showDialogBackPressed() {
        val dialog = Dialog(this)

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_backpressed_dialog)

        val yesButton = dialog.findViewById<Button>(R.id.dialog_warning_yes_btn)

        yesButton.setOnClickListener {
            dialog.dismiss()
            finish()
            Game().clearAllData()
            startActivity(Intent(this, MainActivity::class.java))
            super.onBackPressed()
        }

        val noButton = dialog.findViewById<Button>(R.id.dialog_warning_no_btn)

        noButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    /*
     * Show dialog box with end game.
     */

    @SuppressLint("ClickableViewAccessibility")
    private fun showDialogGameEnd() {
        val dialog = Dialog(this)

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog)

        val winnerText: TextView = dialog.findViewById(R.id.dialog_title)

        when (winner) {
            0 -> {
                winnerText.text = getString(R.string.winner_0)
            }
            1 -> {
                winnerText.text = getString(R.string.winner_1)
            }
            2 -> {
                winnerText.text = getString(R.string.winner_2)
            }
        }

        val restartButton = dialog.findViewById<Button>(R.id.dialog_button_restart)

        restartButton.setOnClickListener {
            dialog.dismiss()
            finish()
            Game().clearAllData()
            startActivity(intent)
        }

        val menuButton = dialog.findViewById<Button>(R.id.dialog_button_menu)

        menuButton.setOnClickListener {
            dialog.dismiss()
            finish()
            Game().clearAllData()
            startActivity(Intent(this, MainActivity::class.java))
        }
        dialog.show()
    }

    /*
     * Determine whose winner.
     */

    private fun endGame() {

        if (turnsWithoutBeating >= 30) {
            showDialogGameEnd()
        } else {
            if (Game().endGameWithWinnerByNoPossibleMoves(playerTurn)) {
                showDialogGameEnd()
            }
            if (Game().endGameWithWinnerByBeatingAllCheckers()) {
                showDialogGameEnd()
            }
        }

    }

}