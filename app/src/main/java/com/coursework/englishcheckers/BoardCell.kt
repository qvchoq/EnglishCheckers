package com.coursework.englishcheckers

class BoardCell(
    val nameCell: String,
    var colorChecker: Int,
    var queenOnBoard: Boolean,
    var highlightedCell: Boolean
) {

    /*
     * 0 - Empty cell
     * 1 - Red checker
     * 2 - White checker
     */

    fun getColor(): Int {
        return this.colorChecker
    }

    fun setQueen(queen: Boolean) {
        this.queenOnBoard = queen
    }

    fun getQueenInfo(): Boolean {
        return this.queenOnBoard
    }



}