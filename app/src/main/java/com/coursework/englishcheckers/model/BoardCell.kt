package com.coursework.englishcheckers.model

class BoardCell(
    private var colorChecker: Int,
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

    fun setHighlight(highlight: Boolean) {
        this.highlighted = highlight
    }

    fun getHighlightInfo(): Boolean {
        return this.highlighted
    }

}