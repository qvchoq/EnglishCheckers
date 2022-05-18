package com.coursework.englishcheckers

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

/*
ENGLISH CHECKERS RULES:
First player make first turn with red checkers.
Default checker can move and beat only in one cell diagonally forward
Default checker can't move and beat backward
Queen checker can move and beat in forward and backward in one cell diagonally,
 */


class GameActivity : AppCompatActivity() {

    var board = mutableMapOf<String, BoardCell>()

    private var checkersOnBoard = mutableMapOf<String, Checker?>()

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
    private val mapCellListX = mapOf(
        30..159 to 30,
        160..289 to 160,
        290..419 to 290,
        420..549 to 420,
        550..679 to 550,
        680..809 to 680,
        810..939 to 810,
        940..1040 to 940
    )
    private val mapCellListY = mapOf(
        450..579 to 450,
        580..709 to 580,
        710..839 to 710,
        840..969 to 840,
        970..1099 to 970,
        1100..1229 to 1100,
        1230..1359 to 1230,
        1360..1460 to 1360
    )
    private val borderOfBoardX = 30..1040
    private val borderOfBoardY = 450..1460

    private val boardSize = 910

    var fromX = 0
    var fromY = 0
    var fromCellName = ""

    private var turnFirstPlayer = true
    private var isHoldingOnChecker = false
    private var isRightPlace = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        drawCheckersPlayerOne()
        drawCheckersPlayerTwo()
        placeCellsOnBoard()
        makePlayerTurn()
    }

    /*
     * Move checker and do a turn by player.
     */

    @SuppressLint("ClickableViewAccessibility")
    private fun makePlayerTurn() {
        val container: FrameLayout = findViewById(R.id.container)
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
                                    val fromCell = convertCoordinateToCell(fromX, fromY)

                                    fromCellName = convertPositionToString(fromCell.first, fromCell.second)

                                }
                            }

                            MotionEvent.ACTION_MOVE -> {
                                if (isHoldingOnChecker) {
                                    println("MOVE")

                                    container.findViewWithTag<ImageView>(fromCellName).translationX = (event.x - origin)
                                    container.findViewWithTag<ImageView>(fromCellName).translationY = (event.y - origin)
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                if (isHoldingOnChecker) {



                                    println("UP")

                                    val newPosX = convertCoordinateToCell(event.x.toInt(), event.y.toInt()).first
                                    val newPosY = convertCoordinateToCell(event.x.toInt(), event.y.toInt()).second
                                    val newPosName = convertPositionToString(newPosX, newPosY)

                                    container.findViewWithTag<ImageView>(fromCellName).translationX =
                                        convertCoordinateToCell(event.x.toInt(), event.y.toInt()).first.toFloat()
                                    container.findViewWithTag<ImageView>(fromCellName).translationY =
                                        convertCoordinateToCell(event.x.toInt(), event.y.toInt()).second.toFloat()

                                    if (newPosName != fromCellName) {
                                        changePosToChecker(newPosName, fromCellName)
                                    }

                                    isHoldingOnChecker = false
                                }
                            }


                        }
                        /*
                            Method to return checker to backside
                            container.findViewWithTag<ImageView>("a2").translationX = (event.x - originX) / convertCoordinateToCell(x, y).first.toFloat()
                            container.findViewWithTag<ImageView>("a2").translationY = (event.y - originY) / convertCoordinateToCell(x, y).second.toFloat()
                         */
                    }
                    return true
                }
            })
    }

    /*
     * Convert coordinates to cell XY.
     */

    private fun convertCoordinateToCell(x: Int, y: Int): Pair<Int, Int> {

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

        isRightPlace = xIsRightPlace && yIsRightPlace && (checkersOnBoard[convertPositionToString(intX, intY)] != null)

        return Pair(intX, intY)
    }

    /*
     * Check if cell is empty.
     */

    private fun checkEmptyCell(pos: String): Boolean {
        return (board[pos]?.getColor() == 0)
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
        clickOnChecker = xIsRightPlace && yIsRightPlace && (checkersOnBoard[convertPositionToString(intX, intY)] != null)
        return clickOnChecker
    }

    /*
     * Check if touch was on board
     */

    private fun touchOnBoard(x: Int, y: Int): Boolean {
        return ( x in borderOfBoardX && y in borderOfBoardY)
    }

    /*
     * Draw first player checkers.
     */

    private fun drawCheckersPlayerOne() {
        val container: FrameLayout = findViewById(R.id.container)
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
                val checker = Checker(2, false, convertPositionToString(x, y))
                runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                    //println("${positionToString(x, y)} white")
                }
                checkersOnBoard[convertPositionToString(x,y)] = checker
                x += 260
            }
        }
    }

    /*
     * Draw second player checkers.
     */

    private fun drawCheckersPlayerTwo() {
        val container: FrameLayout = findViewById(R.id.container)
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
                val checker = Checker(1, false, convertPositionToString(x, y))
                runOnUiThread {
                    container.addView(checker.draw(container, x, y))
                    //println("${positionToString(x, y)} red")
                }
                checkersOnBoard[convertPositionToString(x,y)] = checker
                x += 260
            }
        }
    }

    /*
     * Convert x and y to string format by cells.
     */

    private fun convertPositionToString(x: Int, y: Int): String {
        var letter = ' '
        var int = 0
        when (y) {
            450 -> letter = 'a'
            580 -> letter = 'b'
            710 -> letter = 'c'
            840 -> letter = 'd'
            970 -> letter = 'e'
            1100 -> letter = 'f'
            1230 -> letter = 'g'
            1360 -> letter = 'h'

        }
        when (x) {
            30 -> int = 1
            160 -> int = 2
            290 -> int = 3
            420 -> int = 4
            550 -> int = 5
            680 -> int = 6
            810 -> int = 7
            940 -> int = 8
        }
        return "$letter$int"
    }

    /*
     * Create cells with info on board once.
     */

    private fun placeCellsOnBoard() {
        var nameCell = ""
        var colorChecker = 0
        val cell = BoardCell(nameCell, colorChecker, false, false)
        for (x in 0..7) {
            for (y in 0..7) {
                nameCell = "${cellToLetter[y]}${x + 1}"
                if (checkersOnBoard[nameCell]?.getPosChecker()  == nameCell) {
                    colorChecker = checkersOnBoard[nameCell]!!.getColorChecker()
                }
                board[nameCell] = cell
            }
        }
    }

    /*
     * Update info about checker on cell after move action.
     */

    private fun refreshCellInfo() {
        TODO()
    }

    /*
     * Update checker position after move action.
     */

    private fun changePosToChecker(newPos: String, prevPos: String) {
        val container: FrameLayout = findViewById(R.id.container)
        checkersOnBoard[newPos] = checkersOnBoard[prevPos]
        checkersOnBoard[newPos]?.setPosChecker(container, newPos)
        checkersOnBoard.remove(prevPos)

    }
}