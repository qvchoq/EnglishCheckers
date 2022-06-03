package com.coursework.englishcheckers.model

import org.junit.Test

import org.junit.Assert.*

class ConverterTest {

    @Test
    fun coordinateToCell() {

        assertEquals((30 to 450), Converter().coordinateToCell(140, 460))
        assertEquals((160 to 710), Converter().coordinateToCell(256, 780))
        assertEquals((680 to 710), Converter().coordinateToCell(765, 744))
        assertEquals((940 to 1360), Converter().coordinateToCell(957, 1401))

    }

    @Test
    fun coordinateToCellName() {

        assertEquals("a1", Converter().coordinateToCellName(30, 450))
        assertEquals("c2", Converter().coordinateToCellName(160, 710))
        assertEquals("c6", Converter().coordinateToCellName(680, 710))
        assertEquals("h8", Converter().coordinateToCellName(940, 1360))
    }

    @Test
    fun cellNameToCoordinate() {

        assertEquals((30 to 450), Converter().cellNameToCoordinate("a1"))
        assertEquals((160 to 710), Converter().cellNameToCoordinate("c2"))
        assertEquals((680 to 710), Converter().cellNameToCoordinate("c6"))
        assertEquals((940 to 1360), Converter().cellNameToCoordinate("h8"))

    }

    @Test
    fun cellNameSeparate() {

        assertEquals(('a' to 1), Converter().cellNameSeparate("a1"))
        assertEquals(('c' to 3), Converter().cellNameSeparate("c3"))
        assertEquals(('f' to 6), Converter().cellNameSeparate("f6"))
        assertEquals(('e' to 8), Converter().cellNameSeparate("e8"))

    }
}