package com.coursework.englishcheckers.model

import android.widget.FrameLayout
import android.widget.ImageView

class Checker(
    private val color: Int,
    private var queen: Boolean,
    private var pos: String
) {

    /*
     * 1 - Red checker
     * 2 - White checker
     */

    fun getColor(): Int {
        return this.color
    }

    fun setQueen(queen: Boolean) {
        this.queen = queen
    }

    fun getQueenInfo(): Boolean {
        return this.queen
    }

    fun getPos(): String {
        return this.pos
    }

    fun setPos(container: FrameLayout, newPos: String) {
        val imageViewChecker: ImageView = container.findViewWithTag(this.pos)
        this.pos = newPos
        imageViewChecker.tag = newPos
    }

}