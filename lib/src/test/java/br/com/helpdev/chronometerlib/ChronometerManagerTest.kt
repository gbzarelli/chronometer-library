package br.com.helpdev.chronometerlib

import org.junit.Assert.*
import org.junit.Test

class ChronometerManagerTest {

    private val chronometerManager = ChronometerManager { System.currentTimeMillis() }
    private val imprecisionVisibleValue = 100 * 0.5 // 50% of visible value (100 millis)

    @Test
    fun x() {
        chronometerManager.start()
        Thread.sleep(1000)
        chronometerManager.lap()
        Thread.sleep(1000)
        chronometerManager.lap()
        Thread.sleep(1000)
        chronometerManager.lap()
        Thread.sleep(1000)
        chronometerManager.stop()

        val chronometer = chronometerManager.chronometer
        assertEquals("00:04.0", ChronometerUtils.getFormattedTime(chronometer.getChronometerTime()))
            
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(1)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(2)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(3)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(1)!!.getAccumulatedTime())
        )
        assertEquals(
            "00:02.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(2)!!.getAccumulatedTime())
        )
        assertEquals(
            "00:03.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(3)!!.getAccumulatedTime())
        )
        assertEquals(
            "00:04.0",
            ChronometerUtils.getFormattedTime(chronometer.getLap(4)!!.getAccumulatedTime())
        )
        chronometerManager.removeLap(2)

        val chronometer2 = chronometerManager.chronometer
        assertEquals(
            "00:03.0",
            ChronometerUtils.getFormattedTime(chronometer2.getChronometerTime())
        )

        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(1)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(2)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(3)!!.getChronometerTime())
        )
        assertEquals(
            "00:01.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(1)!!.getAccumulatedTime())
        )
        assertEquals(
            "00:02.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(2)!!.getAccumulatedTime())
        )
        assertEquals(
            "00:03.0",
            ChronometerUtils.getFormattedTime(chronometer2.getLap(3)!!.getAccumulatedTime())
        )
    }

    @Test
    fun testRemoveItem() {
        chronometerManager.start()
        Thread.sleep(1000)
        assertEquals(
            1000.0,
            chronometerManager.getChronometerTime().toDouble(),
            imprecisionVisibleValue
        )
        chronometerManager.lap()
        chronometerManager.removeLap(1)
        assertEquals(
            1.0,
            chronometerManager.getChronometerTime().toDouble(),
            imprecisionVisibleValue
        )
    }

    @Test
    fun startStatus() {
        chronometerManager.start()
        assertTrue(chronometerManager.isStarted())
        assertFalse(chronometerManager.isFinished())
        assertFalse(chronometerManager.isPaused())
    }

    @Test
    fun pauseStatus() {
        chronometerManager.start()
        chronometerManager.pause()
        assertFalse(chronometerManager.isStarted())
        assertFalse(chronometerManager.isFinished())
        assertTrue(chronometerManager.isPaused())
    }

    @Test
    fun finishedStatus() {
        chronometerManager.start()
        chronometerManager.stop()
        assertFalse(chronometerManager.isStarted())
        assertTrue(chronometerManager.isFinished())
        assertFalse(chronometerManager.isPaused())
    }
}