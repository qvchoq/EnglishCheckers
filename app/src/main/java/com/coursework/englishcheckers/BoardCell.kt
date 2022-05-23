package com.coursework.englishcheckers

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentManager

class BoardCell(
    private val nameCell: String,
    private val turnCell: Boolean,
    private var colorChecker: Int,
    private var queenOnBoard: Boolean,
    private var highlighted: Boolean
) {

    /*
     * 0 - Empty cell
     * 1 - Red checker
     * 2 - White checker
     */

    fun setColor(color: Int) {
        this.colorChecker = color
    }

    fun getColorInfo(): Int {
        return this.colorChecker
    }

    fun setQueen(queen: Boolean) {
        this.queenOnBoard = queen
    }

    fun getQueenInfo(): Boolean {
        return this.queenOnBoard
    }

    fun getTurnInfo(): Boolean {
        return this.turnCell
    }

    fun setHighlight(highlight: Boolean) {
        this.highlighted = highlight
    }

    fun getHighlightInfo(): Boolean {
        return this.highlighted
    }


}