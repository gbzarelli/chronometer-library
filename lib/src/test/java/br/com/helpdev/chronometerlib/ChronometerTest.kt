package br.com.helpdev.chronometerlib

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class ChronometerTest {

    private var chronometer = Chronometer()

    private fun startTime(time: Long = 100) = chronometer.startTime(time)

    @Test
    fun assertStartLap() {
        assertTrue(startTime())
    }

    @Test
    fun nonAcceptedTwoStarts() {
        assertTrue(startTime())
        assertFalse(startTime())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testImmutabilityLaps() {
        chronometer.getLaps().add(Chronometer())
    }

    @Test(expected = IllegalStateException::class)
    fun getLapWithoutStart() {
        chronometer.getLap(1)
    }

    @Test
    fun getLapAfterStarted() {
        startTime()
        val lap = chronometer.getLap(1)
        assertRunningLap(lap)
    }

    private fun assertRunningLap(lap: Chronometer?) {
        assertNotNull(lap)
        assertTrue(lap!!.startTime > 0)
        assertTrue(lap.endTime <= 0)
    }

    @Test
    fun addLap() {
        startTime()
        assertTrue(chronometer.newLap(200))
        val lap = chronometer.getLap(2)
        assertRunningLap(lap)
    }

    @Test(expected = IllegalArgumentException::class)
    fun removeLapWithFailed() {
        startTime()
        chronometer.removeLap(1)
    }

    @Test
    fun removeLapWithSuccess() {
        startTime()
        chronometer.newLap(200)
        assertEquals(100, chronometer.getStartBase())
        chronometer.removeLap(1)
        assertEquals(200, chronometer.getStartBase())
    }

    @Test
    fun removeLap() {
        startTime(100)
        chronometer.newLap(200)
        chronometer.addPausedTime(150)//-> time in last lap

        //assert total time
        assertEquals(250, chronometer.getStartBase())
        assertEquals(150, chronometer.pausedTime)
        assertEquals(100, chronometer.startTime)

        //remove first lap (has 100 time unit)
        chronometer.removeLap(1)

        //assert total time
        assertEquals(350, chronometer.getStartBase())
        assertEquals(150, chronometer.pausedTime)
        assertEquals(200, chronometer.startTime)

        //assert rest lap
        assertEquals(1, chronometer.getLaps().size)
        val lap = chronometer.getLap(1)
        assertEquals(300, lap!!.startTime)
        assertEquals(0, lap.endTime)
        assertEquals(150, lap.pausedTime)
    }

    @Test
    fun removeLapWithPause() {
        startTime(100)
        chronometer.addPausedTime(150)//-> time in first lap
        chronometer.newLap(300)

        //assert total time
        assertEquals(250, chronometer.getStartBase())
        assertEquals(150, chronometer.pausedTime)
        assertEquals(100, chronometer.startTime)

        //remove first lap (has 50 time unit)
        chronometer.removeLap(1)

        //assert total time
        assertEquals(150, chronometer.getStartBase())
        assertEquals(0, chronometer.pausedTime)
        assertEquals(150, chronometer.startTime)

        //assert rest lap
        assertEquals(1, chronometer.getLaps().size)
        val lap = chronometer.getLap(1)
        assertEquals(350, lap!!.startTime)
        assertEquals(0, lap.endTime)
        assertEquals(0, lap.pausedTime)
    }

    @Test
    fun removeLap2() {
        startTime(100)
        chronometer.newLap(200)
        chronometer.removeLap(1)
        assertEquals(101, chronometer.getRunningTime(301))
    }
}