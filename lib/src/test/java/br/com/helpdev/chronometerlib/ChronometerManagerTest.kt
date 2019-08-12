package br.com.helpdev.chronometerlib

import org.junit.Assert.*
import org.junit.Test

class ChronometerManagerTest {

    private val chronometerManager = ChronometerManager { System.currentTimeMillis() }
    private val imprecisionVisibleValue = 100 * 0.5 // 50% of visible value (100 millis)

    @Test
    fun testRemoveItem() {
        chronometerManager.start()
        Thread.sleep(1000)
        assertEquals(1000.0, chronometerManager.getChronometerTime().toDouble(), imprecisionVisibleValue)
        chronometerManager.lap()
        chronometerManager.removeLap(1)
        assertEquals(1.0, chronometerManager.getChronometerTime().toDouble(), imprecisionVisibleValue)
    }

}