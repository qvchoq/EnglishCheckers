package com.coursework.englishcheckers

import android.view.View
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

    fun getColorChecker(): Int {
        return this.color
    }

    fun setQueenChecker(queen: Boolean) {
        this.queen = queen
    }

    fun getQueenInfo(): Boolean {
        return this.queen
    }

    fun getPosChecker(): String {
        return this.pos
    }

    fun setPosChecker(container: FrameLayout, newPos: String) {
        val imageViewChecker: ImageView = container.findViewWithTag(this.pos)
        this.pos = newPos
        imageViewChecker.tag = newPos
    }

    fun draw(container:View, x: Int, y: Int): ImageView {
        return ImageView(container.context).apply {
            if (color == 2 && !queen) {
                this.setImageResource(R.drawable.checker_white)
            }
            if (color == 1 && !queen) {
                this.setImageResource(R.drawable.checker_red)
            }
            if (color == 2 && queen) {
                this.setImageResource(R.drawable.checker_white_queen)
            }
            if (color == 1 && queen) {
                this.setImageResource(R.drawable.checker_red_queen)
            }
            this.tag = pos
            this.layoutParams = FrameLayout.LayoutParams(110,110)
            this.translationX = x.toFloat()
            this.translationY = y.toFloat()
        }
    }

}