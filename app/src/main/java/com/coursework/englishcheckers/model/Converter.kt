package com.coursework.englishcheckers.model

import com.coursework.englishcheckers.view.checkersOnBoard


val mapCellListX = mapOf(
    30..159 to 30,
    160..289 to 160,
    290..419 to 290,
    420..549 to 420,
    550..679 to 550,
    680..809 to 680,
    810..939 to 810,
    940..1040 to 940
)
val mapCellListY = mapOf(
    450..579 to 450,
    580..709 to 580,
    710..839 to 710,
    840..969 to 840,
    970..1099 to 970,
    1100..1229 to 1100,
    1230..1359 to 1230,
    1360..1460 to 1360
)



class Converter {

    private var isRightPlace = false

    /*
     * Convert coordinates to cell XY.
     */

    fun coordinateToCell(x: Int, y: Int): Pair<Int, Int> {

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

        isRightPlace = xIsRightPlace && yIsRightPlace && (checkersOnBoard[coordinateToCellName(intX, intY)] != null)

        return Pair(intX, intY)
    }

    /*
     * Convert x and y to string format by cells.
     */

    fun coordinateToCellName(x: Int, y: Int): String {
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
     * Cell name to coordinate XY.
     */

    fun cellNameToCoordinate(cellName: String): Pair<Int, Int> {
        var x = 0
        var y = 0

        when (cellNameSeparate(cellName).second) {
            1 -> x = 30
            2 -> x = 160
            3 -> x = 290
            4 -> x = 420
            5 -> x = 550
            6 -> x = 680
            7 -> x = 810
            8 -> x = 940
        }

        when (cellNameSeparate(cellName).first) {
            'a' -> y = 450
            'b' -> y = 580
            'c' -> y = 710
            'd' -> y = 840
            'e' -> y = 970
            'f' -> y = 1100
            'g' -> y = 1230
            'h' -> y = 1360

        }

        return x to y
    }

    /*
     * Convert cell name to char and int.
     */

    fun cellNameSeparate(cellName: String): Pair<Char, Int> {
        cellName.toCharArray()
        return cellName.toCharArray().first() to cellName.toCharArray().last().digitToInt()
    }





}