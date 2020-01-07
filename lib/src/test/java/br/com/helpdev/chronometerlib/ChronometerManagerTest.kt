package br.com.helpdev.chronometerlib

import org.junit.Assert.*
import org.junit.Test

class ChronometerManagerTest {

    private val chronometerManager = ChronometerManager { System.currentTimeMillis() }
    private val imprecisionVisibleValue = 100 * 0.5 // 50% of visible value (100 millis)

    @Test
    fun shouldBeAddAndRemoveLapsWithSuccessAndCheckAllAccumulatedTimes() {
        chronometerManager.start()

        repeat(3) {
            Thread.sleep(1000)
            chronometerManager.lap()
        }

        Thread.sleep(1000)
        chronometerManager.stop()

        val chronometer = chronometerManager.chronometer
        assertEquals(4, chronometer.getLaps().size)

        assertEquals("00:04.0", ChronometerUtils.getFormattedTime(chronometer.getChronometerTime()))
        val stringCheck: Array<String> = Array(4) { "00:01.0" }
        stringCheck.forEachIndexed { index, s ->
            assertEquals(
                s,
                ChronometerUtils.getFormattedTime(chronometer.getLap(index + 1)!!.getChronometerTime())
            )
        }

        val stringCheck2: Array<String> = Array(4) { i -> "00:0$i.0" }
        stringCheck2.forEachIndexed { index, s ->
            assertEquals(
                s,
                ChronometerUtils.getFormattedTime(chronometer.getLap(index + 1)!!.getAccumulatedTime())
            )
        }

        chronometerManager.removeLap(2)
        assertEquals(3, chronometer.getLaps().size)

        val chronometer2 = chronometerManager.chronometer
        assertEquals(
            "00:03.0",
            ChronometerUtils.getFormattedTime(chronometer2.getChronometerTime())
        )

        val stringCheck3: Array<String> = Array(3) { "00:01.0" }
        stringCheck3.forEachIndexed { index, s ->
            assertEquals(
                s,
                ChronometerUtils.getFormattedTime(chronometer.getLap(index + 1)!!.getChronometerTime())
            )
        }

        assertEquals(3, chronometer.getLaps().size)
        val stringCheck4: Array<String> = Array(3) { i -> "00:0$i.0" }
        stringCheck4.forEachIndexed { index, s ->
            assertEquals(
                s,
                ChronometerUtils.getFormattedTime(chronometer.getLap(index + 1)!!.getAccumulatedTime())
            )
        }
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